package demo.custom.test;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cwf.app.cwf.R;

import demo.intent.EventBusDemo;
import lib.BaseActivity;
import lib.utils.CommonUtils;

/**
 * Created by n-240 on 2016/3/2.
 * 锁屏通知界面
 *
 * @author cwf
 */
public class LockNoticationActivity extends BaseActivity implements View.OnClickListener
        , GestureDetector.OnGestureListener {
    TextView textView;
    private GestureDetector gestureDetector;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LOW_PROFILE
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                | View.SYSTEM_UI_FLAG_IMMERSIVE
//                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    KeyguardManager km;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*保证界面显示在锁屏前面*/
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        );
//        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//        );//| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//        win.addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
//                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

//        win.requestFeature(Window.FEATURE_NO_TITLE);
        useToolbar = false;
        km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (km.isKeyguardLocked())
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        setContentView(R.layout.lock_notification);
        textView = (TextView) findViewById(R.id.lock_text);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        gestureDetector = new GestureDetector(this);
        gestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                startActivity(new Intent(LockNoticationActivity.this, EventBusDemo.class));
                finish();
                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lock_text:
//                startActivity(new Intent(LockNoticationActivity.this, EventBusDemo.class));
//                finish();
                break;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    protected void onStop() {
        if (!km.isKeyguardLocked())
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        super.onStop();
    }
}
