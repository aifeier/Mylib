package demo.picture;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cwf.app.cwf.R;

import java.io.IOException;
import java.net.URL;

import lib.utils.ScreenUtils;
import lib.utils.TimeUtils;

/**
 * Created by n-240 on 2015/11/2.
 */
@SuppressLint("NewApi")
public class VideoPlayInTextureView extends Activity implements TextureView.SurfaceTextureListener{

    private TextureView textureView;
    private SurfaceTexture surfaceTexture;
    private MediaPlayer mediaPlayer;
    private TextView time_now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_videorecord);
        ((SurfaceView) findViewById(R.id.surfaceview)).setVisibility(View.GONE);
        time_now = (TextView) findViewById(R.id.time_now);
        textureView = (TextureView)findViewById(R.id.textureview);
        textureView.setSurfaceTextureListener(this);
        surfaceTexture = textureView.getSurfaceTexture();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ScreenUtils.getScreenWidth(this) / 16 *9);
        textureView.setLayoutParams(lp);
        textureView.setAlpha(0.8f);
        RelativeLayout relativelayout = (RelativeLayout) findViewById(R.id.relativelayout);
        relativelayout.setLayoutParams(lp);
    }

    private void initPlay(){
        if(surfaceTexture == null)
            return;
        mediaPlayer = new MediaPlayer();
        Uri uri = Uri.parse("http://he.yinyuetai.com/uploads/videos/common/0DA4015080D10E8F5D592F80220E92" +
                "E5.flv?sc=14520cce7b04809b&br=3091&vid=2398409&aid=37822&area=KR&vst=0");
        try {
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.setSurface(new Surface(surfaceTexture));
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    time_now.setText(TimeUtils.intToString(mp.getCurrentPosition() / 1000));
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        surfaceTexture = surface;
        surfaceTexture.setDefaultBufferSize(1280 , 720);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initPlay();
            }
        }, 200);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        surfaceTexture.release();
    }
}
