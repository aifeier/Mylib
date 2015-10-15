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
import android.view.View;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cwf.app.cwf.R;

import java.io.File;
import java.io.FileNotFoundException;

import lib.BaseActivity;
import lib.utils.ActivityUtils;
import lib.utils.DirectoryUtil;
import com.cwf.app.cwflibrary.utils.TimeUtils;
import com.cwf.app.photolibrary.utils.photoviewlibs.PhotoView;

/**
 * Created by n-240 on 2015/9/25.
 */
public class Photos extends BaseActivity implements View.OnClickListener{
    ImageView mImg ;
    private File mFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_text_img);
        PhotoView img = (PhotoView) findViewById(R.id.img);
        //goole推荐的图片加载
        Glide.with(this).load(R.drawable.gg).into(img);//本地
        Glide.with(this)
                .load("http://pic14.nipic.com/20110522/7411759_164157418126_2.jpg")//可用输文件地址，网址，本地等
                .error(R.drawable.image_bg)//错误图片
                .placeholder(R.drawable.gg)//加载图片
                .diskCacheStrategy(DiskCacheStrategy.ALL)//让Glide既缓存全尺寸又缓存其他尺寸
                .override(200, 100)//mage Resizing
                .centerCrop()//Center Cropping
                .into((ImageView)img);//网络
        mImg  = (ImageView) findViewById(R.id.img);
        mImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.img:
                openPhotos();
                break;
        }
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
