package demo.custom;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by n-240 on 2016/2/19.
 *
 * @author cwf
 */
public class ClipboardService extends Service{
    public static String ACTION = "com.cwf.ai.Clipboardservice";
    private static String TAG = "ClipboardService";
    private ClipboardManager clipboardManager;
    @Override
    public void onCreate() {
        super.onCreate();
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
                Intent intent = new Intent(ClipboardActivity.ACTION);
                intent.putExtra("text", item.getText());
                sendBroadcast(intent);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    class MyBinder extends Binder{
        public void start(){

        }
    }
}
