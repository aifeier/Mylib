package demo.picture;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.*;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cwf.app.cwf.R;

import lib.BaseActivity;

/**
 * Created by n-240 on 2015/10/22.
 */
public class Camera2Activity extends BaseActivity implements SurfaceHolder.Callback{

    private SurfaceView mSurfaceView;
    private Handler mHandler;
    private HandlerThread mThreadHandler;
    private Camera.Size mPreviewSize;
    private CaptureRequest.Builder mPreviewBuilder;

    public Camera2Activity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_surfaceview);
        initLooper();
        mSurfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        mSurfaceView.getHolder().addCallback(this);
    }

    //很多过程都变成了异步的了，所以这里需要一个子线程的looper
    private void initLooper() {
        mThreadHandler = new HandlerThread("Camera2");
        mThreadHandler.start();
        mHandler = new Handler(mThreadHandler.getLooper());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
/*        try {
            //获得CameraManager
            CameraManager cameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
            //获得属性
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics("0");
            //支持的STREAM CONFIGURATION
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            //显示的size
             mPreviewSize = map.getOutputSizes(SurfaceView.class)[0];
            //打开相机
            mPreviewSize = map.getOutputSizes(SurfaceTexture.class)[0];
            //打开相机
            cameraManager.openCamera(0+"", null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
