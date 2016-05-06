package demo.picture;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
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
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cwf.app.cwf.R;
import com.handmark.pulltorefresh.library.autoloadlist.AutoLoadAdapter;
import com.handmark.pulltorefresh.library.autoloadlist.AutoRefreshListView;
import com.handmark.pulltorefresh.library.autoloadlist.ViewHolder;

import java.io.IOException;
import java.util.ArrayList;

import lib.utils.ActivityUtils;
import lib.utils.ScreenUtils;
import lib.utils.TimeUtils;

/**
 * Created by chenw on 2015/11/1.
 */
public class VideoPlayActivity2 extends Activity implements SurfaceHolder.Callback{
    private SurfaceView surface;
    private SurfaceHolder surfaceHolder;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private Button start;
    private Button stop;
    /*播放时间*/
    private TextView time_now, time_all;
    /*暂停界面*/
    private ImageView stop_imageview;

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

    private  AudioManager audioManager ;
    private AutoRefreshListView autoRefreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_videorecord);
        videos = new ArrayList<String>();
        videos.add("http://115.231.144.58/6/j/t/s/d/jtsdhetjxhxpaawfotxqrzuknrohqk/hc.yin" +
                "yuetai.com/CA7A014F2176C6967B183E333F6C36FB.flv?sc=02fb295b193c140b&br=784&vid=2349335&aid=1559&area=KR&vst=0");
        videos.add("http://he.yinyuetai.com/uploads/videos/common/6D04014F217B486A" +
                "0B379CEE9924D14E.flv?sc=f5794470b4e2f81b&br=3098&vid=2349335&aid=1559&area=KR&vst=0");
        videos.add("http://hc.yinyuetai.com/uploads/videos/common/97F901444C05EB571E045240C23" +
                "51768.flv?sc=553d7df6599cef7c&br=777&vid=876958&aid=1559&area=KR&vst=0");
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
        playVideoID = -1;
        autoRefreshListView = (AutoRefreshListView<String>)findViewById(R.id.videos_list);
        AutoLoadAdapter<String> autoLoadAdapter = new AutoLoadAdapter<String>(VideoPlayActivity2.this, android.R.layout.activity_list_item) {
            @Override
            public void buildView(ViewHolder holder, String data) {
                Uri uri = Uri.parse(data);
                holder.setValueToTextView(android.R.id.text1,
                        data);
            }

            @Override
            public void getPage(int page) {

            }
        };
        autoLoadAdapter.setmData(videos, autoRefreshListView);
        autoRefreshListView.setAdapter(autoLoadAdapter);
        autoRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(playVideoID != position - 1){
                    playVideoID = position;
                    initPlay((String)parent.getAdapter().getItem(position));
                }
            }
        });
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    private void initView(){
        /*设置窗口大小*/
        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                ScreenUtils.getScreenWidth(this) / 16 * 9);
        relativelayout = (RelativeLayout) findViewById(R.id.relativelayout);
        relativelayout.setLayoutParams(lp);

        /*初始化进度条*/
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setClickable(false);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isPrepared && mediaPlayer != null)
                    mediaPlayer.seekTo(mediaPlayer.getDuration() * seekBar.getProgress() / 100);
            }
        });

        /*初始化surfaceview等*/
        stop_imageview = (ImageView) findViewById(R.id.stop_imageview);
        time_now = (TextView) findViewById(R.id.time_now);
        time_all = (TextView) findViewById(R.id.time_all);
        surface = (SurfaceView) findViewById(R.id.surfaceview);
        surfaceHolder = surface.getHolder();
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
        surfaceHolder.addCallback(this);
        mediaPlayer = new MediaPlayer();
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigurationChangeed();
            }
        });
        stop = (Button) findViewById(R.id.stop);
        stop.setText("Next");
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPlay(null);
            }
        });
        relativelayout.setLayoutParams(lp);
        surface.setLayoutParams(lp);
        surface.setOnTouchListener(surfaceview_touch_listener);

    }

    private float start_x = 0;
    private float start_y = 0;
    private View.OnTouchListener surfaceview_touch_listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(isPrepared && mediaPlayer !=null)
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    start_x = event.getX();
                    start_y = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    if(Math.abs(start_x - event.getX()) < 5
                            && Math.abs(start_y - event.getY()) < 5) {
                        /*点击事件*/
                        if (isPrepared) {
                            if (mediaPlayer.isPlaying()) {
                                isManualPause = true;
                                mediaPlayer.pause();
                                stop_imageview.setVisibility(View.VISIBLE);
                            } else {
                                mediaPlayer.start();
                                stop_imageview.setVisibility(View.GONE);
                            }
                        }
                    }
                    else if(Math.abs(start_y - event.getY()) < Math.abs(start_x - event.getX())){
                        /*调节播放进度*/
                        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() +
                                (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition())
                                *(int) (Math.abs(start_x - event.getX())) / v.getWidth() / 10);
                        ActivityUtils.showTip("播放：" +
                                TimeUtils.intToString(mediaPlayer.getCurrentPosition()/1000)
                                , false);
                    }
                    else if(start_x > v.getWidth() /2){
                        /*调节音量*/
                        float move_y = start_y -  event.getY();
                        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + (int)(move_y/v.getHeight() * 10);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC , currentVolume, 1);
                        ActivityUtils.showTip("声音：" + audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), false);
                    }else{
                        /*调节亮度*/
                        float move_y = start_y - event.getY();
                        ScreenUtils.setBrightness(VideoPlayActivity2.this, move_y / v.getHeight() * 100);
                        ActivityUtils.showTip("屏幕亮度：" +ScreenUtils.getBrightness(VideoPlayActivity2.this), false);
                    }
                    start_x = 0;
                    start_y = 0;
                    break;
            }
            return true;
        }
    };


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /*延时加载视频*/
                /*优先加载其它界面*/
                initPlay(null);
            }
        }, 200);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mediaPlayer.release();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initPlay(String url){
        /*设置窗口大小*/
        relativelayout.setLayoutParams(lp);
        surface.setLayoutParams(lp);
        if(surfaceHolder == null)
            return;
        /*半透明背景*/
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
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
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    seekBar.setSecondaryProgress(percent);
                    start.setText("全屏" + percent);
                    if(isPrepared && percent > 0) {
                        playseek = mp.getCurrentPosition();
                        seekBar.setProgress(mp.getCurrentPosition() * 100 / mp.getDuration());
                        time_now.setText(TimeUtils.intToString(mp.getCurrentPosition() / 1000));
                        stop.setText(mp.getVideoHeight() + "*" + mp.getVideoWidth());
                        time_all.setText(TimeUtils.intToString(mp.getDuration() / 1000));
                    }
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
                    seekBar.setClickable(true);
                    if (playseek != -1) {
                        mediaPlayer.seekTo(playseek);
                    }
                    if (!isManualPause) {
                        mediaPlayer.start();
                    }
                    isFrist = false;
                    time_now.setText("00:00");
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
            /*同步*/
//            mediaPlayer.prepare();
            /*异步*/
            mediaPlayer.prepareAsync();
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
        if(mediaPlayer !=null) {
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        seekBar.setClickable(false);
        if(!isFrist) {
            isManualPause = false;
            isPrepared = false;
            playseek = -1;
            seekBar.setClickable(false);
            seekBar.setProgress(0);
        }
    }

    private void ConfigurationChangeed(){
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
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
        surfaceHolder.removeCallback(this);
        mediaPlayer.release();
        isFrist = true;
        isPrepared = false;
    }

    @Override
    protected void onDestroy() {
        surfaceHolder.removeCallback(this);
        mediaPlayer.stop();
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
