package lib.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by n-240 on 2016/1/21.
 *
 * @author cwf
 */
/*常用的方法*/
public class CommonUtils {

    /*拨打电话*/
    private static void call(Context context, String phoneNumber){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }
    /*android6.0拨打电话*/
    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
    public static void onCall(Context context, String phoneNumber){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CODE_ASK_CALL_PHONE);
                return;
            }else{
                //上面已经写好的拨号方法
                call(context, phoneNumber);
            }
        } else {
            //上面已经写好的拨号方法
            call(context, phoneNumber);
        }
    }


    /*获取6.0电话权限*/
    public static void onRequestPermissionsResult(Context context,String phoneNumber, int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    call(context, phoneNumber);
                    } else {
                    // Permission Denied
                    Toast.makeText(context, "CALL_PHONE Denied", Toast.LENGTH_SHORT)
                    .show();
                    }
                break;
            default:
        }
    }


    /*跳转至拨号界面*/
    public static void callDial(Context context, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /*发送短信*/
    public static void sendSms(Context context, String phoneNumber, String content){
        Uri uri = Uri.parse("smsto:" +
                (TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", TextUtils.isEmpty(content) ? "" : content);
        context.startActivity(intent);
    }

    /*唤醒屏幕并解锁*/
    /*需要添加权限 WAKE_LOCK 和 DISABLE_KEYGUARD*/
    public static void wakeUpAndUnlock(Context context){
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unlock");
        /*解锁*/
        kl.disableKeyguard();
        /*获得电源管理器对象*/
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        /*获取PowerManager.Wakelock对象，后面的参数|变色同时传入两个值，最后的是LogCat里用的Tag*/
        PowerManager.WakeLock wl = pm.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP|PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        /*点了屏幕*/
        wl.acquire();
        /*释放*/
        wl.release();
    }

    /*判断当前app处于前台还是后台*/
    /*需要添加权限 GET_TASKS*/
    public static boolean isApplicationBackgroud(final Context context){
        ActivityManager am = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfos = am.getRunningTasks(1);
        if(!taskInfos.isEmpty()){
            ComponentName topActivity = taskInfos.get(0).topActivity;
            if(!topActivity.getPackageName().equals(context.getPackageName())){
                return true;
            }
        }
        return false;
    }

    /*判断当前手机是否处于锁屏（睡眠）状态*/
    public static boolean isSleeping(Context context){
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.inKeyguardRestrictedInputMode();
    }

    /*判断是否有网络连接*/
    public static boolean isOnline(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if(info != null && info.isConnected()){
            return true;
        }
        return false;
    }

    /*判断当前是否是WIFI连接*/
    public static boolean isWifiConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(info != null && info.isConnected()){
            return true;
        }
        return false;
    }

    /*安装apk*/
    public static void installApk(Context context, File file){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        intent.setType("application/vnd.android.package-archive");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /*判断当前设置是否为手机*/
    public static boolean isPhone(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if(telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE)
            return false;
        else
            return true;
    }

    /*获取当前设备的高宽，单位px*/

    public static DisplayMetrics getDisplayMetrics(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static int getDeviceWidth(Activity activity){
        return getDisplayMetrics(activity).widthPixels;
    }

    public static int getDeviceHeight(Activity activity){
        return getDisplayMetrics(activity).heightPixels;
    }

    /*获取当前设备的IMEI*/
    public static String getDeviceIMEI(Context context){
        String deviceId;
        if(isPhone(context)){
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceId  = telephonyManager.getDeviceId();
        }else{
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    /*获取当前设备的mac地址*/
    public static String getMacAddress(Context context){
        String macAddress;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        macAddress = info.getMacAddress();
        if(macAddress == null)
            return "";
        macAddress = macAddress.replace(":", ".");
        return macAddress;
    }

    /*获取当前程序的版本号*/
    public static String getAppVersion(Context context){
        String version = "0";
        try {
            version = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),0).versionName;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return version;
    }

    public static String TAG = "CommonUtils";
    public static String VERSION_NAME = "version_name";
    public static String VERSION_CODE = "version_code";

    /*搜集设备信息，用户信息统计分析*/
    public static Properties collectDeviceInfo(Context context){
        Properties mDeviceCrashInfo = new Properties();
        try{
            PackageManager packageManager = (PackageManager) context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo
                    (context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if(packageInfo != null){
                mDeviceCrashInfo.put(VERSION_NAME, packageInfo.versionName == null? "not set" : packageInfo.versionName);
                mDeviceCrashInfo.put(VERSION_CODE, packageInfo.versionCode);
            }
        }catch (PackageManager.NameNotFoundException e){
            Log.e(TAG, "Error while collect package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for(Field field : fields){
            try{
                field.setAccessible(true);
                mDeviceCrashInfo.put(field.getName(), field.get(null));
            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, "Error while collect carsh info", e);
            }
        }
        return  mDeviceCrashInfo;
    }

    public static String collectDeviceInfoStr(Context context){
        Properties properties = collectDeviceInfo(context);
        Set deviceInfos = properties.keySet();
        StringBuffer deviceInfoStr = new StringBuffer("{\n");
        for(Iterator iterator = deviceInfos.iterator(); iterator.hasNext();){
            Object item = iterator.next();
            deviceInfoStr.append("\t\t\t" + item + ":" + properties.get(item) + ",\n");
        }

        deviceInfoStr.append("}");
        return deviceInfoStr.toString();
    }

    /*判断是否有sd卡*/
    public static boolean haveSDCard(){
        return android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /*动态隐藏软键盘*/
    public static void hideSoftInput(Activity activity){
        View view = activity.getWindow().peekDecorView();
        if(view != null){
            InputMethodManager inputMethodManager = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideSoftInput(Context context, EditText editText){
        editText.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

    }

    /*动态显示或隐藏软键盘*/
    public static void toggleSoftInput(Context context, EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /*主动回到home, 后台运行*/
    public static void goHome(Context context){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(intent);
    }

    /*获取状态栏高度*/
    /*注意，要在onWindowFocusChanged中调用，在onCreate中获取高度为0*/
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static int getStatusBarHeight(Activity activity){
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /*获取状态栏+标题栏高度*/
    public static int getTopBarHeight(Activity activity){
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    /*获取MCC + MNC代码（SIM卡运营商国家代码和运营商网络代码*/
    /*仅当用户已在网络注册时有效，CDMA可能无效（中国移动：46000 46002，中国联通：46001，中国电信：46003*/
    public static String getNetworkOperator(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getNetworkOperator();
    }

    /*获取网络运营商名*/
    public static String getNetworkOperatorName(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getNetworkOperatorName();
    }
    /*返回移动终端类型*/
    /*
    * PHONE_TYPE_NONE: 0 未知
    * PHONE_TYOE_GSM: 1 手机制式为GSM，移动和联通
    * PHONR_TYPE_CDMA: 2 手机制式为CDMA，电信
    * PHONE_TYPE_SIP: 3
    * */
    public static int getPhoneType(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getPhoneType();
    }

    /*判断手机连接的网络类型*/
    public class Constants{
        /*
        * Unknown network class
        * */
        public static final int NERWORK_CLASS_UNKNOWN = 0;

        /*
        * wifi net work
        * */
        public static final int NERWORK_WIFI = 1;

        /*
        * "2G" networks
        * */

        public static final int NETWORK_CLASS_2_G = 2;

        /*
        * "3G" networks
        * */
        public static final int NETWORK_CLASS_3_G = 3;

        /*
        * "4G" networks
        * */

        public static final int NERWORK_CALSS_4_G = 4;
    }
    public static int getNetWorkClass(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()){
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return Constants.NETWORK_CLASS_2_G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return Constants.NETWORK_CLASS_3_G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return Constants.NERWORK_CALSS_4_G;
            default:
                return Constants.NERWORK_CLASS_UNKNOWN;

        }
    }

    /*判断当前手机的网络类型 WIFI还是2,3,4G*/
    public static int getNetWorkStatus(Context context){
        int netWorkType = Constants.NERWORK_CLASS_UNKNOWN;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            int type = networkInfo.getType();
            if(type == ConnectivityManager.TYPE_WIFI){
                netWorkType = Constants.NERWORK_WIFI;
            }else if(type == ConnectivityManager.TYPE_MOBILE){
                netWorkType = getNetWorkClass(context);
            }
        }

        return netWorkType;
    }




}
