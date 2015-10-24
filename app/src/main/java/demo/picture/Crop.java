package demo.picture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.cwf.app.cwf.R;
import com.cwf.app.cwflibrary.utils.FileUtils;
import com.cwf.app.cwflibrary.utils.TimeUtils;

import java.io.File;
import java.io.FileNotFoundException;

import lib.BaseActivity;
import lib.utils.ActivityUtils;
import lib.utils.DirectoryUtil;

/**
 * Created by n-240 on 2015/9/25.
 */
public class Crop extends BaseActivity implements View.OnClickListener{
    ImageView mImg ;
    private File mFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_text_img);
        mImg  = (ImageView) findViewById(R.id.img);
        mImg.setClickable(true);
        mImg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.img:
                video();
//                openPhotos();
//                mFile = new File(DirectoryUtil.getImageCacheDir(), TimeUtils.getSimpleDate()+".jpg");
//                doCrop(Uri.fromFile(mFile));
                break;
        }
    }

    private void video(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri uri = Uri.fromFile(new File(FileUtils.createPath("", "a.mp4")));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivity(intent);
    }


    private void doCrop(Uri uri){

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case 1:
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    mImg.setImageBitmap(bitmap);
                    break;
            }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
