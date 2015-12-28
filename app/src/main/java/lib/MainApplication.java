package lib;

import android.app.Activity;
import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.zhy.autolayout.config.AutoLayoutConifg;

import demo.intent.mode.toolbox.RequestManager;

/**
 * Created by n-240 on 2015/9/23.
 */
public class MainApplication extends Application{

    private static MainApplication mInstance;
    public static Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        RequestManager.init(this);
        SDKInitializer.initialize(this);

        /*设备的物理高度进行百分比化*/
//        AutoLayoutConifg.getInstance().useDeviceSize();
    }

    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }

    public synchronized static MainApplication getInstance() {
        return mInstance;
    }
}
