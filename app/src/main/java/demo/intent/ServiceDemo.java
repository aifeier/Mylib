package demo.intent;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;

/**
 * Created by n-240 on 2015/11/9.
 */
public class ServiceDemo extends Service{

    public static String ACTION = "com.cwf.ai.service";
    private String TAG = "ServiceDemo";
    private EventBus eventBus;
    private int times;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
//                Intent i = new Intent(OkhttpDemo.ACTION);
//                i.putExtra("msg", Intent.ACTION_TIME_TICK + times);
//                sendBroadcast(intent);
                System.out.println(Intent.ACTION_TIME_TICK);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate");
        eventBus = new EventBus();
        times = 0;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(OkhttpDemo.ACTION);
                intent.putExtra("msg", "timer run! " + ++times);
                sendBroadcast(intent);
            }
        }, 0, 10000);
        /*Intent.ACTION_TIME_TICK系统时钟，每分钟运行一次，到好像有问题*/
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver,filter);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG, "onBind");
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e(TAG, "onStart");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }

    public String getString(){
        return "ni hao a!";
    }
}
