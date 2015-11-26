package lib.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by n-240 on 2015/11/26.
 */
public class MobleUtils {
    private MobleUtils()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    public static String getImSI(Context context){
        TelephonyManager mTelephonyMgr =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getSubscriberId();
    }

    public static String getImEI(Context context){
        TelephonyManager mTelephonyMgr =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getDeviceId();
    }

    public static String getTelephonyAllInfo(Context context){
        TelephonyManager mTelephonyMgr =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return "软件版本：" + mTelephonyMgr.getDeviceSoftwareVersion()
                + " SimSerialNumber: " + mTelephonyMgr.getSimSerialNumber()
                + " SimOperator: " + mTelephonyMgr.getSimOperator()
                + " Line1Number: " + mTelephonyMgr.getLine1Number();
    }
}
