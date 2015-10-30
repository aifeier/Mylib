package demo.picture;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cwf.app.cwf.R;

import java.io.File;
import java.io.FileNotFoundException;

import lib.BaseActivity;
import lib.utils.ActivityUtils;
import lib.utils.DirectoryUtil;
import lib.utils.GlideUtils;

import com.cwf.app.photolibrary.utils.photoviewlibs.PhotoView;

/**
 * Created by n-240 on 2015/9/25.
 */
public class Photos extends BaseActivity implements View.OnClickListener{
    ImageView mImg ;
    private File mFile;
    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_text_img);
        PhotoView img = (PhotoView) findViewById(R.id.img);
        GlideUtils.setmContext(this);
        GlideUtils.getNetworkImage("http://img4.imgtn.bdimg.com/it/u=1621924933,968611299&fm=21&gp=0.jpg", (ImageView) img);
        //goole推荐的图片加载
/*        Glide.with(this).load(R.drawable.gg).into(img);//本地
        Glide.with(this)
                .load("http://pic14.nipic.com/20110522/7411759_164157418126_2.jpg")//可用输文件地址，网址，本地等
                .error(R.drawable.image_bg)//错误图片
                .placeholder(R.drawable.gg)//加载图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//让Glide既缓存全尺寸又缓存其他尺寸
                .override(200, 100)//mage Resizing
                .centerCrop()//Center Cropping
                .into((ImageView) img);*/
        mImg  = (ImageView) findViewById(R.id.img);
        mImg.setOnClickListener(this);
        textview = (TextView)findViewById(R.id.textview);
        textview.setClickable(true);
        textview.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.img:
                openPhotos();
                break;
            case R.id.textview:
                showPopupWindow(v);
                break;
        }
    }

    private void showPopupWindow(View view) {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.layout_text_img, null);
        TextView textView = new TextView(this);
        textView.setText("你是色盲啊！                ");
        // 设置按钮的点击事件
//        Button button = (Button) contentView.findViewById(R.id.button1);
//        button.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "button is pressed",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });

        final PopupWindow popupWindow = new PopupWindow(textView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

//                Log.i("mengdd", "onTouch : ");

                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.penguins));

        // 设置好参数之后再show
//        popupWindow.showAsDropDown(view);
        popupWindow.showAtLocation(view, Gravity.CENTER, view.getLayoutParams().width, view.getLayoutParams().height);

    }

    private void openPhotos(){

        Intent i  = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(resultCode == Activity.RESULT_OK)
        switch (requestCode){
            case 0:
                if(data.getData()!=null) {
                    Uri uri = data.getData();
                    ActivityUtils.showTip(uri.getPath(), false);
//                    doCrop(Uri.fromFile(new File(uri.getPath())));
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        mImg.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void doCrop(Uri uri){

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");//可裁剪
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);//若为false则表示不返回数据
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, 1);
    }
}
