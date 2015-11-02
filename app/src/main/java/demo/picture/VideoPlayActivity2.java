package demo.picture;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cwf.app.cwf.R;

import java.io.IOException;
import java.util.ArrayList;

import lib.utils.ScreenUtils;
import lib.utils.TimeUtils;

/**
 * Created by chenw on 2015/11/1.
 */
public class VideoPlayActivity2 extends Activity implements SurfaceHolder.Callback{
    private SurfaceView surface;
    private SurfaceHolder surfaceHolder;
    private ProgressBar progressBar;
    private MediaPlayer mediaPlayer;
    private Button start;
    private Button stop;

    /*当前播放时间*/
    private int playseek = -1;
    /*是否准备完成*/
    private boolean isPrepared = false;
    /*手动暂停*/
    private boolean isManualPause = false;

    private boolean isFrist = true;
    private ArrayList<String> videos;
    private int playVideoID;
    private RelativeLayout.LayoutParams lp ;
    private  RelativeLayout relativelayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_videorecord);
        videos = new ArrayList<String>();
        videos.add("http://he.yinyuetai.com/uploads/videos/common/A60C014EBF4DA368A591526EA8712E8" +
                "C.flv?sc=04b740fe57884841&br=3132&vid=721484&aid=25339&area=KR&vst=0");
        videos.add("http://he.yinyuetai.com/uploads/videos/common/DC08014E16B2FB6" +
                "79E9BC2A20E68A8B0.flv?sc=a38224836811ca49&br=3099&vid=2313726&aid=25339&area=KR&vst=0");
        videos.add("http://he.yinyuetai.com/uploads/videos/common/" +
                "1859896_he_B9610146B23576D1D07DF6D2C7C0B22C.flv?sc=" +
                "37320d30c1985596&br=3126&vid=2076482&aid=25339&area=KR&vst=0");
        videos.add("http://he.yinyuetai.com/uploads/videos/common/0E26014ECF198EAE4A430A7F16B58D" +
                "D3.flv?sc=eca62b3086bf94ca&br=3130&vid=782827&aid=25339&area=KR&vst=0");
        videos.add("http://he.yinyuetai.com/uploads/videos/common/0DA4015080D10E8F5D592F80220E92" +
                "E5.flv?sc=14520cce7b04809b&br=3091&vid=2398409&aid=37822&area=KR&vst=0");
        playVideoID = 0;
    }

    private void initView(){
        /*设置窗口大小*/
        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                ScreenUtils.getScreenWidth(this) / 16 * 9);
        relativelayout = (RelativeLayout) findViewById(R.id.relativelayout);
        relativelayout.setLayoutParams(lp);
        surface.setLayoutParams(lp);

        /*初始化surfaceview等*/
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        surface = (SurfaceView) findViewById(R.id.surfaceview);
        surfaceHolder = surface.getHolder();
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        surfaceHolder.addCallback(this);
        mediaPlayer = new MediaPlayer();
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mediaPlayer.isPlaying() && isPrepared)
                    mediaPlayer.start();
                ConfigurationChangeed();
            }
        });
        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                initPlay(null);
            }
        });
        ArrayList<View> views = new ArrayList<View>();
        final TextView a = new TextView(this);
        surface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPrepared) {
                    if (mediaPlayer.isPlaying()) {
                        isManualPause = true;
                        mediaPlayer.pause();
                    } else {
                        mediaPlayer.start();
                    }
                }
            }
        });

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initPlay(null);
            }
        }, 1000);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mediaPlayer.stop();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initPlay(String url){
        /*设置窗口大小*/
        relativelayout.setLayoutParams(lp);
        surface.setLayoutParams(lp);
        if(surfaceHolder == null)
            return;
        resetMediaStatue();
        if(url==null || url.equals("")) {
            playVideoID = (playVideoID + 1) % videos.size();
            url = videos.get(playVideoID);
        }
        final Uri uri = Uri.parse(url);
        try {
            mediaPlayer.setDataSource(VideoPlayActivity2.this, uri);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setDisplay(surfaceHolder);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(false);
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    playseek = mp.getCurrentPosition();
                    progressBar.setSecondaryProgress(mp.getCurrentPosition() * 100 / mp.getDuration());
                    start.setText(TimeUtils.intToString(mp.getCurrentPosition() / 1000) + "/"
                            + TimeUtils.intToString(mp.getDuration() / 1000) + "现在缓存：" + percent);
                }
            });
            mediaPlayer.setOnTimedTextListener(new MediaPlayer.OnTimedTextListener() {
                @Override
                public void onTimedText(MediaPlayer mp, TimedText text) {
                    stop.setText(text.getText());
                }
            });
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isPrepared = true;
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_ERROR_IO:
                            break;
                        case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                            break;
                        case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                            mp.reset();
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    switch (what) {
                        case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                            break;
                        case MediaPlayer.MEDIA_INFO_UNKNOWN:
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    initPlay(null);
                }
            });

            mediaPlayer.prepare();
            if(!isManualPause && isPrepared)
                mediaPlayer.start();
            if(playseek !=-1){
                mediaPlayer.seekTo(playseek);
            }
            isFrist = false;
            stop.setText(mediaPlayer.getVideoHeight() + "*" + mediaPlayer.getVideoWidth());
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        } catch (IllegalStateException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetMediaStatue(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
        if(mediaPlayer !=null)
            mediaPlayer.reset();
        mediaPlayer = new MediaPlayer();
        if(!isFrist) {
            isManualPause = false;
            isPrepared = false;
            playseek = -1;
            progressBar.setProgress(0);
        }
    }

    private void ConfigurationChangeed(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        /*为了使方向感应有效，屏幕方向又设置为不确定*/
        /*setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation){
            case Configuration.ORIENTATION_PORTRAIT:
                start.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                start.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                break;
        }
        /*设置窗口大小*/
        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                ScreenUtils.getScreenWidth(this) / 16 * 9);
        relativelayout.setLayoutParams(lp);
        surface.setLayoutParams(lp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
        isFrist = true;
    }

    @Override
    protected void onDestroy() {
        surfaceHolder.removeCallback(this);
        mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK)
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                /*全屏的时候按返回键退出全屏*/
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                return true;
            }
        return super.onKeyDown(keyCode, event);
    }
}
