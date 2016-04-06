package demo.custom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.cwf.app.cwf.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.ColorPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import demo.intent.EventBusDemo;
import demo.intent.NetWorkChangeReceiver;
import lib.BaseActivity;
import lib.utils.ActivityUtils;
import lib.utils.AssetsUtils;
import lib.utils.CommonUtils;
import lib.utils.NotificationUtils;

//import com.alibaba.fastjson.JSON;

/**
 * Created by n-240 on 2016/1/14.
 *
 * @author cwf
 */
/*多级联动选择器*/
public class PickerDemoActivity extends BaseActivity implements View.OnClickListener{
    Button address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_picker);
        address = (Button) findViewById(R.id.picker_address);
        address.setOnClickListener(this);
        findViewById(R.id.picker_color).setOnClickListener(this);
//        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        PendingIntent pi = PendingIntent.getActivity(PickerDemoActivity.this, 1, new Intent(PickerDemoActivity.this,EventBusDemo.class), 0);
//        am.set(AlarmManager.RTC_WAKEUP, 3000, pi);

    }

    private ArrayList<AddressPicker.Province> data = new ArrayList<AddressPicker.Province>();

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.picker_address:
                if(data == null || data.size() <= 0) {
                    data = new ArrayList<AddressPicker.Province>();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    String json = AssetsUtils.readText(this, "city.json");
                    Type type = new TypeToken<List<AddressPicker.Province>>() {
                    }.getType();
                    List<AddressPicker.Province> a = gson.fromJson(json, type);
                    data.addAll(a);
                }
//                data.addAll(JSON.parseArray(json, AddressPicker.Province.class));
                AddressPicker addressPicker = new AddressPicker(this, data);
//                picker.setSelectedItem("贵州", "贵阳", "花溪");
                addressPicker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                    @Override
                    public void onAddressPicked(String province, String city, String county) {
                        ActivityUtils.showTip(province + city + county, false);
                        address.setText(province + city + county);
                    }
                });
                addressPicker.show();
                break;
            case R.id.picker_color:
                ColorPicker colorPicker = new ColorPicker(this);
                colorPicker.setInitColor(0xFFDD00DD);
                colorPicker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
                    @Override
                    public void onColorPicked(int pickedColor) {
                        ActivityUtils.showTip(ConvertUtils.toColorString(pickedColor), true);
                    }
                });
                colorPicker.show();
                break;
            case R.id.wakeup:
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        CommonUtils.wakeUpAndUnlock(PickerDemoActivity.this);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(timerTask, 5000);
                cn.qqtheme.framework.picker.DatePicker
                        datePicker = new cn.qqtheme.framework.picker.DatePicker(this);
                datePicker.setOnDatePickListener(new cn.qqtheme.framework.picker.DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {

                    }
                });
                datePicker.show();
                break;
            case R.id.notification:
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.startNotification("message",EventBusDemo.class,R.drawable.dialog_load, "content" );
//                notificationUtils.showNotification("ok", EventBusDemo.class, R.drawable.file_picker_folder);
                break;
            case R.id.wakeNotification:
                TimerTask timerTask1 = new TimerTask() {
                    @Override
                    public void run() {
                        if(CommonUtils.isSleeping(getApplicationContext()))
                            CommonUtils.wakeUp(getApplicationContext());
                        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//                        notificationUtils.startNotification("message",EventBusDemo.class,R.drawable.dialog_load, "content" );
                        notificationUtils.showNotification("ok", EventBusDemo.class, R.drawable.file_picker_folder);
//                            startActivity(new Intent(PickerDemoActivity.this,LockNoticationActivity.class));
                    }
                };
                Timer timer1 = new Timer();
                timer1.schedule(timerTask1, 5000);
                break;
            case R.id.picker_file:
                /*FilePicker filePicker = new FilePicker(this);
                filePicker.setShowHideDir(false);
                filePicker.setRootPath(StorageUtils.getRootPath(this) + "");
                //picker.setAllowExtensions(new String[]{".apk"});
                filePicker.setMode(FilePicker.Mode.File);
                filePicker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
                    @Override
                    public void onFilePicked(String currentPath) {
                        ActivityUtils.showTip(currentPath, true);
                    }
                });
                filePicker.show();*/
//                CommonUtils.sendSms(PickerDemoActivity.this, "10086", "1581");
                /*
                //亮屏解鎖
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        CommonUtils.wakeUpAndUnlock(PickerDemoActivity.this);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(timerTask, 5000);*/
//                ActivityUtils.showTip(CommonUtils.getMacAddress(this), true);
//                ActivityUtils.showTip(CommonUtils.collectDeviceInfoStr(this), true);
//                CommonUtils.goHome(this);
//                ActivityUtils.showTip(CommonUtils.getNetWorkStatus(this)+"", false);
                if(netWorkChangeReceiver == null) {
                    ActivityUtils.showTip("注册监听", false);
                    netWorkChangeReceiver = new NetWorkChangeReceiver();
                    registerReceiver(netWorkChangeReceiver,
                            new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
                }else{
                    ActivityUtils.showTip("取消监听", false);
                    unregisterReceiver(netWorkChangeReceiver);
                    netWorkChangeReceiver = null;
                }
                break;
        }
    }

    private BroadcastReceiver netWorkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo activeInfo = manager.getActiveNetworkInfo();
            Toast.makeText(context, "mobile:" + mobileInfo.isConnected() + "\n" + "wifi:" + wifiInfo.isConnected()
                    + "\n" + "active:" + activeInfo.getTypeName(), Toast.LENGTH_SHORT).show();
        }
    };

//    private NetWorkChangeReceiver netWorkChangeReceiver;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case CommonUtils.REQUEST_CODE_ASK_CALL_PHONE:
                CommonUtils.onRequestPermissionsResult(
                        PickerDemoActivity.this, "10086", requestCode, permissions, grantResults);
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
