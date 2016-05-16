package demo.custom.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cwf.app.cwf.R;
import com.materialdialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import demo.custom.BottomNavigationActivity;
import demo.custom.ContactsActivity;
import demo.custom.SimpleViewActivity;
import demo.intent.EventBusDemo;
import lib.BaseActivity;
import lib.MainApplication;
import lib.utils.AppUtils;
import lib.utils.CommonUtils;
import lib.utils.NotificationUtils;

/**
 * Created by n-240 on 2016/3/2.
 *
 * @author cwf
 */
public class SelfFuncationActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private ListView mList;
    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        mList = (ListView) findViewById(R.id.main_list);
        mList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, data));
        mList.setOnItemClickListener(this);
        MainApplication.currentActivity = this;
    }

    private void initData() {
        data = new ArrayList<>();
        data.add("5秒后唤醒屏幕并解锁");
        data.add("显示notification");
        data.add("5秒后唤醒屏幕不解锁显示通知");
        data.add("测试SlidingLayout");
        data.add("测试MaterialDialog");
        data.add("测试BottomNacigation");
        data.add("测试自定义绘制");
        data.add("打开QQ");
        data.add("通讯录");

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TimerTask timerTask;
        Timer timer;
        switch (position) {
            case 0:
                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        CommonUtils.wakeUpAndUnlock(getApplicationContext());
                    }
                };
                timer = new Timer();
                timer.schedule(timerTask, 5000);
                break;
            case 1:
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.startNotification("message", EventBusDemo.class, R.drawable.dialog_load, "content");
//                notificationUtils.showNotification("ok", EventBusDemo.class, R.drawable.file_picker_folder);
                break;
            case 2:
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                        notificationUtils.showNotification("ok", EventBusDemo.class, R.drawable.file_picker_folder);
                        if (CommonUtils.isSleeping(getApplicationContext())) {
                            CommonUtils.wakeUp(getApplicationContext());
                            Intent i = new Intent(getApplicationContext(), LockNoticationActivity.class);
                            startActivity(i);
                        }
                    }
                }, 5000);
                break;
            case 3:
                startActivity(new Intent(this, SlidingAcitivity1.class));
                break;
            case 4:
                final MaterialDialog materialDialog = new MaterialDialog(this);
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 30; i++) {
                    list.add("ABC" + i);
                }
                materialDialog.setListData(list, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }).setTitle("我是标题")
                        .setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                            }
                        })
                        .show();

                break;
            case 5:
                startActivity(new Intent(SelfFuncationActivity.this, BottomNavigationActivity.class));
                break;
            case 6:
                startActivity(new Intent(this, SimpleViewActivity.class));
                break;
            case 7:
                AppUtils.startApp(this, "com.tencent.mobileqq");
                break;
            case 8:
                startActivity(new Intent(this, ContactsActivity.class));
                break;
            default:
                break;
        }

    }


}
