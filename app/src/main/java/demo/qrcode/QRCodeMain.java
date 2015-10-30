package demo.qrcode;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.betterman.login.LoginActivity;
import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.CaptureActivityHandler;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.Intents;

import lib.BaseActivity;
import lib.utils.ActivityUtils;
import lib.utils.NetUtils;

import com.cwf.app.cwf.R;
import com.google.zxing.client.android.camera.CameraManager;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by n-240 on 2015/10/13.
 */
public class QRCodeMain extends BaseActivity
//        implements SurfaceHolder.Callback
{

    private String TAG = "demo.qrcode.QRCodeMain";

    private boolean hasSurface;

    private Result savedResultToShow;


    private CameraManager cameraManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Window window = getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//保持屏幕不变暗
        if(true){
            ActivityUtils.showTip("" + NetUtils.isWifi(getApplicationContext()), false);
            Intent i = new Intent(QRCodeMain.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        setContentView(R.layout.layout_surfaceview);
//        Intent intent = new Intent(Intents.Encode.ACTION);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//        intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
//        intent.putExtra(Intents.Encode.DATA, "nishi2bma \nhaohao");
//        intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE.toString());
//        startActivity(intent);
        hasSurface = false;
//        PreferenceManager.setDefaultValues(this, com.google.zxing.client.android.R.xml.preferences, false);
    }

 /*   @Override
    protected void onPause() {
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(com.google.zxing.client.android.R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraManager = new CameraManager(getApplication());

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if(hasSurface){
            initCamera(surfaceHolder);
        }else{
            surfaceHolder.addCallback(this);
        }

        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            String dataString = intent.getDataString();
            if (Intents.Scan.ACTION.equals(action)) {
                if (intent.hasExtra(Intents.Scan.WIDTH) && intent.hasExtra(Intents.Scan.HEIGHT)) {
                    int width = intent.getIntExtra(Intents.Scan.WIDTH, 0);
                    int height = intent.getIntExtra(Intents.Scan.HEIGHT, 0);
                    if (width > 0 && height > 0) {
                        cameraManager.setManualFramingRect(width, height);
                    }
                }
            }
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        surfaceHolder.setKeepScreenOn(true);
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            cameraManager.startPreview();
            // Creating the handler starts the preview, which can also throw a RuntimeException.
//            if (handler == null) {
//                handler = new CaptureActivityHandler(this, decodeFormats, characterSet, cameraManager);
//            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        // Bitmap isn't used yet -- will be used soon
*//*        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler, com.google.zxing.client.android.R.id.decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }*//*
        savedResultToShow = result;
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(com.google.zxing.client.android.R.string.app_name));
        builder.setMessage(getString(com.google.zxing.client.android.R.string.msg_camera_framework_bug));
        builder.setPositiveButton(com.google.zxing.client.android.R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        initCamera(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }*/
}
