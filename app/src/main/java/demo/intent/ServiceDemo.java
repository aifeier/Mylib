package demo.intent;

import android.app.Service;
import android.content.Intent;
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
//                Log.e(TAG, "timer run! " + ++times);
                Intent intent = new Intent(OkhttpDemo.ACTION);
                intent.putExtra("msg", "timer run! " + times);
                sendBroadcast(intent);
            }
        }, 0, 10000);
//        eventBus.register(this);
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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
//        eventBus.unregister(this);
        super.onDestroy();
    }

    public String getString(){
        return "ni hao a!";
    }
}
