package demo.custom.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cwf.app.cwf.R;

import demo.intent.EventBusDemo;
import lib.utils.CommonUtils;

/**
 * Created by n-240 on 2016/3/2.
 *锁屏通知界面
 * @author cwf
 */
public class LockNoticationActivity extends Activity implements View.OnClickListener{
    TextView textView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*保证界面显示在锁屏前面*/
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//        win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//        );//| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//        win.addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
//                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

//        win.requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lock_notification);
        textView = (TextView) findViewById(R.id.lock_text);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lock_text:
                startActivity(new Intent(LockNoticationActivity.this, EventBusDemo.class));
                finish();
                break;
        }

    }
}
