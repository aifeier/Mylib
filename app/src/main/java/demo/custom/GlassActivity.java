package demo.custom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.cwf.app.cwf.R;

import lib.BaseActivity;
import lib.utils.Blur;

/**
 * Created by n-240 on 2016/4/8.
 */
public class GlassActivity extends BaseActivity {
    private ImageView imageView;
    Bitmap bitmap;
    Bitmap newImg;
    private Handler header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useToolbar = false;
        setContentView(R.layout.ac_glass);
        imageView = (ImageView) findViewById(R.id.glass_img);
        header = new Handler();
        header.postDelayed(new Runnable() {
            @Override
            public void run() {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.penguins, options);
                Log.e("ABC", "start");
                newImg = Blur.fastblur(GlassActivity.this, bitmap, 12);
                Log.e("ABC", "stop");
                imageView.setImageBitmap(newImg);
            }
        }, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null)
            bitmap.recycle();
        if (newImg != null) {
            newImg.recycle();
        }
    }
}
