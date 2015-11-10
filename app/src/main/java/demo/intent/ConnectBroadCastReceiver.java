package demo.intent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cwf.app.cwf.MainActivity;

import lib.utils.ActivityUtils;

/**
 * Created by n-240 on 2015/11/9.
 */
public class ConnectBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
//            Intent service = new Intent(context,ServiceDemo.class);
//            context.startService(service);
//            Log.e("BroadCastReceiver", "开机了！");
//            ActivityUtils.showTip("开机了！", true);
            Intent bootActivityIntent=new Intent(context,MainActivity.class);
            bootActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(bootActivityIntent);//要启动应用程序的首界面
        }else if(intent.getAction().equals(Intent.ACTION_SHUTDOWN)){
            Log.e("BroadCastReceiver", "关机了");
            ActivityUtils.showTip("关机了！", true);
        }
    }

}
