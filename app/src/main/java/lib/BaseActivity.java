package lib;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

//import com.cwf.app.cwflibrary.utils.ThemePreferences;
import com.cwf.app.cwf.R;
import com.cwf.app.cwf.SlidingLayout;

import lib.utils.Systembartint.SystemBarTintManager;

/**
 * Created by n-240 on 2015/9/23.
 */
public  class BaseActivity extends AppCompatActivity {

    private static ProgressDialog progressDialog;
    /**状态栏管理器*/
    private SystemBarTintManager tintManager;


    /*用户数据，个人设置*/
//    private ThemePreferences themePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.currentActivity = this;//application中当前的activity


//        themePreferences = new ThemePreferences(this);
        if(getActionBar()!=null)
            getActionBar().setDisplayShowHomeEnabled(false);

        /***请求等待*/
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("请等待...");

        /**当sdk大于19即android4.4时，修改actionbar的演示颜色*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        tintManager = new SystemBarTintManager(this);//设置监听这里
        tintManager.setStatusBarTintEnabled(true);//设置statusbar可用


        SlidingLayout rootView = new SlidingLayout(this);
        rootView.bindActivity(this);
//        tintManager.setTintResource(R.color.holo_blue_light);
        /*
        * 主题为actionbar
        * android:fitsSystemWindows="true"防止actionbar和状态栏重合
        * */
    }

/*    protected ThemePreferences getThemePreferences(){
        return themePreferences;
    }*/


    protected void setActionBarColor(String color){
        if(getActionBar()!=null)
            getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
    }

    public SystemBarTintManager getSystemBatTintManger(){
        return tintManager;
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static void setWaitProgressDialog(boolean show){
        if(show&&!progressDialog.isShowing())
            progressDialog.show();
        else if(!show&&progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    protected void onResume() {
//        tintManager.setTintResource(themePreferences.getStatusTineColor());
        super.onResume();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_open, R.anim.anim_close);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
