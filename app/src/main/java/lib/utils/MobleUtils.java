package lib.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
                + " \nSimSerialNumber: " + mTelephonyMgr.getSimSerialNumber()
                + " \nSimOperator: " + mTelephonyMgr.getSimOperator()
                + " \nLine1Number: " + mTelephonyMgr.getLine1Number()
                + "\nSMSI: " + mTelephonyMgr.getSubscriberId()
                + "\nIMEI: " + mTelephonyMgr.getDeviceId();
        }

    public void getS(Context context){
        SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService(
                Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            subscriptionManager.getActiveSubscriptionInfoCountMax();
        }else{
        }
    }

    public static DoubleSIMInfo initMtkDoubleSim(Context mContext) {
        DoubleSIMInfo mtkDoubleInfo = new DoubleSIMInfo();
        try {
            TelephonyManager tm = (TelephonyManager) mContext
                    .getSystemService(Context.TELEPHONY_SERVICE);
            Class<?> c = Class.forName("com.android.internal.telephony.Phone");
            Field fields1 = c.getField("BM_AUS_BAND");
//            Field fields1 = c.getField("GEMINI_SIM_1");
            fields1.setAccessible(true);
            mtkDoubleInfo.setSimId_1((Integer) fields1.get(null));
//            Field fields2 = c.getField("GEMINI_SIM_2");
            Field fields2 = c.getField("BM_AUS2_BAND");
            fields2.setAccessible(true);
            mtkDoubleInfo.setSimId_2((Integer) fields2.get(null));
//            TelephonyManager.class.getDeclaredMethods();
            /*Method m = TelephonyManager.class.getDeclaredMethod(
                    "getSubscriberIdGemini", int.class);*/
            Method m = null;
            try {
                m = TelephonyManager.class.getDeclaredMethod(
                        "getSubscriberIdGemini", int.class);
            }catch (Exception e){
                try {
                    m = TelephonyManager.class.getDeclaredMethod(
                            "getSubscriberId", long.class);

                }catch (Exception e1){

                }
            }

            if(m!=null) {
                m.invoke(tm, 2);
                mtkDoubleInfo.setImsi_1((String) m.invoke(tm,
                        mtkDoubleInfo.getSimId_1()));
                mtkDoubleInfo.setImsi_2((String) m.invoke(tm,
                        mtkDoubleInfo.getSimId_2()));
                if(mtkDoubleInfo.getImsi_1()==null&&mtkDoubleInfo.getImei_2()==null){
                    mtkDoubleInfo.setImsi_1((String) m.invoke(tm,
                            0));
                    mtkDoubleInfo.setImsi_2((String) m.invoke(tm,
                            1));
                }
            }

            Method m1 = null;
            try {
                 m1 = TelephonyManager.class.getDeclaredMethod(
                        "getDeviceIdGemini", int.class);
            }catch (Exception e){
                try {
                     m1 = TelephonyManager.class.getDeclaredMethod(
                            "getDeviceId", int.class);
                }catch (Exception e1){

                }
            }
            if(m1!=null) {
                m1.invoke(tm, 2);
                mtkDoubleInfo.setImei_1((String) m1.invoke(tm,
                        mtkDoubleInfo.getSimId_1()));
                mtkDoubleInfo.setImei_2((String) m1.invoke(tm,
                        mtkDoubleInfo.getSimId_2()));
                mtkDoubleInfo.setImei_1((String) m1.invoke(tm,
                        0));
                mtkDoubleInfo.setImei_2((String) m1.invoke(tm,
                        1));
            }

            /*Method mx = TelephonyManager.class.getDeclaredMethod(
                    "getPhoneType", int.class);
            mx.invoke(tm, 5);
            mtkDoubleInfo.setPhoneType_1((Integer) mx.invoke(tm,
                    mtkDoubleInfo.getSimId_1()));
            mtkDoubleInfo.setPhoneType_2((Integer) mx.invoke(tm,
                    mtkDoubleInfo.getSimId_2()));*/

            if (TextUtils.isEmpty(mtkDoubleInfo.getImsi_1())
                    && (!TextUtils.isEmpty(mtkDoubleInfo.getImsi_2()))) {
                mtkDoubleInfo.setDefaultImsi(mtkDoubleInfo.getImsi_2());
            }else {
                mtkDoubleInfo.setDefaultImsi(mtkDoubleInfo.getImsi_1());
            }
        } catch (Exception e) {
            mtkDoubleInfo.setMtkDoubleSim(false);
            return mtkDoubleInfo;
        }
        mtkDoubleInfo.setMtkDoubleSim(true);
        return mtkDoubleInfo;
    }

    public static DoubleSIMInfo initQualcommDoubleSim(Context mContext) {
        DoubleSIMInfo gaotongDoubleInfo = new DoubleSIMInfo();
        gaotongDoubleInfo.setSimId_1(0);
        gaotongDoubleInfo.setSimId_2(1);
        try {
            Class<?> cx = Class
                    .forName("android.telephony.MSimTelephonyManager");
            Object obj = mContext.getSystemService("phone_msim");

            Method md = cx.getMethod("getDeviceId", int.class);
            Method ms = cx.getMethod("getSubscriberId", int.class);
            cx.getMethods();
            md.invoke(obj, 1);
            gaotongDoubleInfo.setImei_1((String) md.invoke(obj,
                    gaotongDoubleInfo.getSimId_1()));
            gaotongDoubleInfo.setImei_2((String) md.invoke(obj,
                    gaotongDoubleInfo.getSimId_2()));
            gaotongDoubleInfo.setImsi_1((String) ms.invoke(obj,
                    gaotongDoubleInfo.getSimId_1()));
            gaotongDoubleInfo.setImsi_2((String) ms.invoke(obj,
                    gaotongDoubleInfo.getSimId_2()));
        } catch (Exception e) {
            e.printStackTrace();
            gaotongDoubleInfo.setGaotongDoubleSim(false);
            return gaotongDoubleInfo;
        }
        gaotongDoubleInfo.setGaotongDoubleSim(true);
        return gaotongDoubleInfo;
    }

    /**
     * @param c
     * @return 返回平台数据
     */
    public static DoubleSIMInfo isDoubleSim(Context c) {
        DoubleSIMInfo gaotongDoubleInfo = initQualcommDoubleSim(c);
        DoubleSIMInfo mtkDoubleInfo = initMtkDoubleSim(c);
        boolean isGaoTongCpu = gaotongDoubleInfo.getGaotongDoubleSim();
        boolean isMtkCpu = mtkDoubleInfo.getMtkDoubleSim();
        if (isGaoTongCpu) {
            // 高通芯片双卡
            return gaotongDoubleInfo;
        } else if (isMtkCpu) {
            // MTK芯片双卡
            return mtkDoubleInfo;
        } else {
            //普通单卡手机
            return null;
        }
    }
}
