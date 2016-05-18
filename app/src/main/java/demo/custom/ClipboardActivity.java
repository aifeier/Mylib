package demo.custom;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import lib.utils.ActivityUtils;

/**
 * Created by n-240 on 2016/2/19.
 * 模拟监听剪切板，打印剪切板内容
 *
 * @author cwf
 */
public class ClipboardActivity extends Activity {

    public static String ACTION = "demo.intent.clipboardReceiver";
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*系统广播只能动态注册，在main中注册无效*/
            if (intent.getAction().equals(ACTION)) {
                Log.e(getPackageName(), intent.getStringExtra("text"));
            }
        }
    };

    private ClipboardManager clipboardManager;
    private Intent clipboardService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData data = clipboardManager.getPrimaryClip();
                String str = data.getItemAt(0).getText().toString();
                EventBus.getDefault().post(str);
            }
        });

        EventBus.getDefault().register(this);
        registerReceiver(broadcastReceiver, new IntentFilter(ACTION));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (clipboardService == null) {
//            clipboardService = new Intent(ClipboardService.ACTION);
//            clipboardService.setPackage(getPackageName());
            startService(ActivityUtils.getServiceIntent(this, ClipboardService.ACTION));
            bindService(ActivityUtils.getServiceIntent(this, ClipboardService.ACTION), serviceConnection, BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    ClipData.Item data = clipboardManager.getPrimaryClip().getItemAt(0);
                    EventBus.getDefault().post(data);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Subscribe
    public void onEventMainThread(ClipData.Item item) {
        if (item != null) {
            Log.e("ClipboardActivity", item.getText().toString());
            Toast.makeText(this, item.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }


    @Subscribe
    public void onEventMainThread(String str) {
        Log.e("ABC", str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (clipboardService != null) {
            stopService(clipboardService);
            unbindService(serviceConnection);
        }
        unregisterReceiver(broadcastReceiver);
    }


}
