package demo.intent;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.squareup.okhttp.Request;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import lib.utils.ActivityUtils;
import lib.utils.FileUtils;
import lib.utils.OkHttpClientManager;

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
        int i;
        for(i =0 ; i< 3;i++){
            final int a = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClientManager.downloadAsyn("http://i6.topit.me/6/3d/c7/1132049425fc9c73d6o.jpg",
                            FileUtils.getInstance(getApplicationContext()).fileCache, new OkHttpClientManager.ResultCallback<String>() {
                                @Override
                                public void onFileDownSize(long downsize, long allSize) {
                                    Log.e(getPackageName(), downsize + "/" + allSize);
                                    ActivityUtils.showTip(a + "正在下载" + downsize * 100 / allSize + "%", false);
                                }

                                @Override
                                public void onError(Request request, Exception e) {
                                    ActivityUtils.showTip("下载失败", false);
                                }


                                @Override
                                public void onResponse(String response) {
                                    ActivityUtils.showTip("下载完成", false);
                                }

                            });
                }
            });
            Log.e("ABC", a+"");
            thread.start();
        }
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
