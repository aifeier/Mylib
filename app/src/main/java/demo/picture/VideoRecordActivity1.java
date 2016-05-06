package demo.picture;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cwf.app.cwf.R;

import java.io.IOException;
import java.util.List;

import lib.BaseActivity;
import lib.utils.FileUtils;
import lib.utils.TimeUtils;
import lib.widget.customview.RecordImage;

/**
 * Created by n-240 on 2015/10/22.
 */
public class VideoRecordActivity1 extends BaseActivity implements SurfaceHolder.Callback {

    /*预览类*/
    private RecordImage recordImage;

    private SurfaceView surfaceView;

    private SurfaceHolder surfaceHolder;

    /*录制*/
    private MediaRecorder mediaRecorder;

    /*拍摄的相机*/
    private Camera camera;
    /*屏幕分辨率*/
    private DisplayMetrics displayMetrics;

    private CamcorderProfile camcorderProfile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 设置横屏显示
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 选择支持半透明模式,在有surfaceview的activity中使用。
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.layout_videorecord1);
        displayMetrics = getResources().getDisplayMetrics();
        initView();
    }

    private void initView() {
        recordImage = (RecordImage) findViewById(R.id.recordbutton);
        recordImage.setVisibility(View.VISIBLE);
        recordImage.setOnRecordTouchListener(new RecordImage.OnRecordTouchListener() {
            @Override
            public void OnActionDown() {
                recordImage.setText("抬起停止录制");
                startRecord();
            }

            @Override
            public void OnActionMove() {

            }

            @Override
            public void OnActionUp() {
                recordImage.setText("按住录制视频");
                stopRecord();
            }
        });
        surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                displayMetrics.widthPixels * 4 / 3);
        surfaceView.setLayoutParams(params);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setKeepScreenOn(true);
    }

    private void startPreview() {
        if (camera == null)
            camera = Camera.open();
        try {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size previewSize = null;
            for (Camera.Size size : camera.getParameters().getSupportedPreviewSizes()) {
                if (size.width * 4 / 3 == size.height) {
                    previewSize = size;
                }
            }
            if (previewSize != null) {
                parameters.setPreviewSize(previewSize.width, previewSize.height);
                camera.setParameters(parameters);
            }
            camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPreview() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }
    }

    private void startRecord() {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
            camera.unlock();
            mediaRecorder.setCamera(camera);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            if (camcorderProfile == null) {
                if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_QVGA))
                    camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA);
                else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_CIF))
                    camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_CIF);
                else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P))
                    camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
                else if (CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_LOW))
                    camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        displayMetrics.widthPixels * camcorderProfile.videoFrameWidth / camcorderProfile.videoFrameHeight);
                surfaceView.setLayoutParams(params);
            }
            mediaRecorder.setProfile(camcorderProfile);
            mediaRecorder.setOutputFile(FileUtils.getInstance(this).fileCache
                    + "/" + TimeUtils.getSimpleDate() + ".mp4");
            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
            mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    mr.reset();
                }
            });
            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void stopRecord() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            camera.lock();
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        surfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override
    protected void onDestroy() {
        surfaceView = null;
        surfaceHolder.removeCallback(this);
        surfaceHolder = null;
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
        }
        mediaRecorder = null;
        if (camera != null)
            camera.lock();
        super.onDestroy();
    }
}