package demo.picture;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Choreographer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cwf.app.cwf.R;

import java.io.IOException;

import lib.BaseActivity;
import lib.utils.FileUtils;
import lib.utils.TimeUtils;
import lib.widget.customview.RecordImage;

/**
 * Created by n-240 on 2015/10/22.
 */
public class VideoRecordActivity1 extends BaseActivity implements SurfaceHolder.Callback {

    /*录制按钮*/
    private RecordImage recordImage;

    /*预览类*/
    private SurfaceView surfaceView;

    private SurfaceHolder surfaceHolder;

    /*时间计数器*/
    private Chronometer chronometer;

    /*录制*/
    private MediaRecorder mediaRecorder;

    /*拍摄的相机*/
    private Camera camera;
    /*屏幕分辨率*/
    private DisplayMetrics displayMetrics;

    private CamcorderProfile camcorderProfile = null;

    private Camera.Size previewSize;

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
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        recordImage = (RecordImage) findViewById(R.id.recordbutton);
        recordImage.setVisibility(View.VISIBLE);
        recordImage.setOnRecordTouchListener(new RecordImage.OnRecordTouchListener() {
            @Override
            public void OnActionDown() {
                recordImage.setText("抬起停止录制");
                startRecord();
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();
            }

            @Override
            public void OnActionMove() {

            }

            @Override
            public void OnActionUp() {
                recordImage.setText("按住录制视频");
                stopRecord();
                chronometer.stop();
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

    @Override
    protected void onResume() {
        super.onResume();
        startPreview();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopPreview();
    }

    private void startPreview() {
        if (surfaceHolder != null) {
            if (camera == null)
                camera = Camera.open();
            if (camera != null)
                try {
                    Camera.Parameters parameters = camera.getParameters();
                    if (previewSize == null) {
                        for (Camera.Size size : camera.getParameters().getSupportedPreviewSizes()) {
                            if (size.height > displayMetrics.widthPixels)
                                break;
                            if (size.width * 3 / 4 == size.height) {
                                previewSize = size;
                            }
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
            else {
                Toast.makeText(this, "打开摄像头失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void stopPreview() {
        if (camera != null) {
            camera.stopPreview();
            camera.lock();
            camera.release();
            camera = null;
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

    /*打开前置摄像头*/
    private Camera openFaceCamera() {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return Camera.open(i);
            }
        }
        return null;
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
        surfaceHolder.removeCallback(this);
        surfaceHolder = null;
        surfaceView = null;
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
        }
        mediaRecorder = null;
        stopPreview();
        super.onDestroy();
    }
}