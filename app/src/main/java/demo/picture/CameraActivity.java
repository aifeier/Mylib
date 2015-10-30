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

import java.io.File;
import java.io.FileNotFoundException;

import demo.intent.entity.Base;
import lib.BaseActivity;
import lib.utils.ActivityUtils;
import lib.utils.DirectoryUtil;
import lib.utils.TimeUtils;

/**
 * Created by n-240 on 2015/9/25.
 */
public class CameraActivity extends BaseActivity implements View.OnClickListener{
    ImageView mImg ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_text_img);
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
        Intent i  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File mFile = new File(DirectoryUtil.getImageCacheDir(),
                TimeUtils.getSimpleDate("yyyy_MM_dd HH_mm_ss")+".jpg");
        i.putExtra(MediaStore.EXTRA_OUTPUT, mFile);
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==Activity.RESULT_OK)
        switch (requestCode){
            case 0:
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                mImg.setImageBitmap(bitmap);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
