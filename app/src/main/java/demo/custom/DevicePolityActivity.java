package demo.custom;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSeekBar;
import android.view.View;

import com.cwf.app.cwf.R;

import demo.custom.test.AdminReceiver;
import lib.BaseActivity;

/**
 * Created by n-240 on 2016/5/16.
 * 设备管理器
 * 可以实现一键锁屏
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class DevicePolityActivity extends BaseActivity implements View.OnClickListener {

    /*
    * 这是设备管理的主类。通过它可以实现屏幕锁定、屏幕亮度调节、出厂设置等功能。
    */
    private DevicePolicyManager devicePolicyManager;
    /*
     * componentName可以用于启动第三方程序的代码
     */
    private ComponentName componentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_device_polity);
        setTitle("设备管理");
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, AdminReceiver.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_enable:
                enableDevice();
                break;
            case R.id.btn_disenable:
                disableDevice();
                break;
            case R.id.btn_lock_screen:
                lockScreen();
                break;
        }
    }

    /**
     * 激活设备管理
     */
    private void enableDevice() {
        if (!devicePolicyManager.isAdminActive(componentName)) {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "测试设备管理器");
            startActivityForResult(intent, 0);
        } else {
            Snackbar.make(DevicePolityActivity.this.getWindow().getDecorView(), "已经授权", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Snackbar.make(DevicePolityActivity.this.getWindow().getDecorView(), "授权成功", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(DevicePolityActivity.this.getWindow().getDecorView(), "授权失败", Snackbar.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 禁用设备管理
     */
    private void disableDevice() {
        boolean bActive = devicePolicyManager.isAdminActive(componentName);
        if (bActive) {
            devicePolicyManager.removeActiveAdmin(componentName);
            Snackbar.make(DevicePolityActivity.this.getWindow().getDecorView(), "取消授权成功", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(DevicePolityActivity.this.getWindow().getDecorView(), "应用未授权", Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * 锁屏(调用系统锁屏)
     */
    private void lockScreen() {
        boolean bActive = devicePolicyManager.isAdminActive(componentName);
        if (bActive) {
            devicePolicyManager.lockNow();
        } else {
            Snackbar.make(DevicePolityActivity.this.getWindow().getDecorView(), "应用未授权", Snackbar.LENGTH_SHORT).show();
        }
    }
}
