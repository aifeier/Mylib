package demo.picture;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.cwf.app.cwf.R;
import com.cwf.app.cwflibrary.utils.FileUtils;
import com.cwf.app.cwflibrary.utils.TimeUtils;
import com.google.zxing.client.android.camera.CameraManager;

import java.io.IOException;

/**
 * Created by n-240 on 2015/10/22.
 */
public class VideoRecordActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener{

    private Button start;// 开始录制按钮
    private Button stop;// 停止录制按钮
    private MediaRecorder mediarecorder;// 录制视频的类
    private SurfaceView surfaceview;// 显示视频的控件
    // 用来显示视频的一个接口，我靠不用还不行，也就是说用mediarecorder录制视频还得给个界面看
    // 想偷偷录视频的同学可以考虑别的办法。。嗯需要实现这个接口的Callback接口
    private SurfaceHolder surfaceHolder;

    private CameraManager cameraManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置横屏显示
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 选择支持半透明模式,在有surfaceview的activity中使用。
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.layout_videorecord);
        cameraManager = new CameraManager(getApplication());
        initView();
    }

    private void initView(){
        start = (Button) this.findViewById(R.id.start);
        stop = (Button) this.findViewById(R.id.stop);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        stop.setEnabled(false);
        surfaceview = (SurfaceView) this.findViewById(R.id.surfaceview);
        SurfaceHolder holder = surfaceview.getHolder();// 取得holder
        holder.addCallback(this); // holder加入回调接口
        holder.setKeepScreenOn(true);
        // setType必须设置，要不出错.
//        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder

        surfaceHolder = holder;
        try {
            cameraManager.setMinCameraSize(true);
            cameraManager.openDriver(holder);
            cameraManager.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LinearLayout.LayoutParams cameraFL = new LinearLayout.LayoutParams(cameraManager.getCameraResolution().y,
                cameraManager.getCameraResolution().x, Gravity.CENTER);
        surfaceview.setLayoutParams(cameraFL);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
        surfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // surfaceDestroyed的时候同时对象设置为null
        surfaceview = null;
        surfaceHolder = null;
        mediarecorder = null;
        cameraManager.stopPreview();
        cameraManager.closeDriver();
    }

//    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.start:
                mediarecorder = new MediaRecorder();// 创建mediarecorder对象
                Camera camera = cameraManager.getCamera();
                if (camera != null) {
                    camera.unlock();
                    mediarecorder.setCamera(camera);
                }
                // 设置录制视频源为Camera(相机)
                mediarecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//                mediarecorder.setInputSurface(surfaceHolder.getSurface());
                mediarecorder.setOrientationHint(90);

                // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4
                mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                // 设置录制的视频编码,音频编码
                mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                /*设置比特率*/
                mediarecorder.setAudioEncodingBitRate(44100);
                int width = 480;
                int height = 320;
//                mediarecorder.setVideoEncodingBitRate(5 * 1920 * 1080);
                mediarecorder.setVideoEncodingBitRate(5 * width * height);
//                mediarecorder.setVideoEncodingBitRate(5 * 480 * 320);
                // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错
//                mediarecorder.setVideoSize(480, 320);
                mediarecorder.setVideoSize(width, height);
                // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错
                mediarecorder.setVideoFrameRate(24);
//                mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());
                // 设置视频文件输出的路径
                mediarecorder.setOutputFile(FileUtils.createPath("video/",
                        TimeUtils.getSimpleDate().replace(" ","-").replace(":","-")+".mp4"));
                try {
                    // 准备录制
                    mediarecorder.prepare();
                    // 开始录制
                    mediarecorder.start();
                    start.setEnabled(false);
                    stop.setEnabled(true);
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.stop:
                if (mediarecorder != null) {
                    // 停止录制
                    mediarecorder.stop();
                    // 释放资源
                    mediarecorder.release();
                    mediarecorder = null;
                    start.setEnabled(true);
                    stop.setEnabled(false);
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
