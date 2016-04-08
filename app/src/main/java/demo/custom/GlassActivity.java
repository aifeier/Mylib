package demo.custom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.cwf.app.cwf.R;

import lib.BaseActivity;
import lib.utils.Blur;

/**
 * Created by n-240 on 2016/4/8.
 */
public class GlassActivity extends BaseActivity{
    private ImageView imageView;
    Bitmap bitmap;
    Bitmap newImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_glass);
        imageView = (ImageView) findViewById(R.id.glass_img);
        Handler header = new Handler();
        header.postDelayed(new Runnable() {
            @Override
            public void run() {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.penguins, options);
                Bitmap newImg = Blur.fastblur(GlassActivity.this, bitmap, 12);
                imageView.setImageBitmap(newImg);
            }
        }, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap != null)
            bitmap.recycle();
        if(newImg != null){
            newImg.recycle();
        }
    }
}
