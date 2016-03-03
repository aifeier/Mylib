package lib.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.rtp.AudioStream;
import android.provider.MediaStore;

import com.cwf.app.cwf.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by n-240 on 2016/3/1.
 *
 * @author cwf
 */
public class NotificationUtils {


        private Context mContext;
        NotificationManager mNM;
        public static final int NOTIFYID = 2048;

        public NotificationUtils(Context context) {
            this.mContext = context;
            mNM = (NotificationManager) mContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }


        /**
         * 开启，一直在
         *
         * @param message
         * @param cla
         * @param icon
         * @param content
         */
        public void startNotification(String message, Class<?> cla, int icon,
                                      String content) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClass(mContext, cla);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FILL_IN_DATA);

            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);

            Notification notification = new Notification.Builder(mContext)
                    .setSmallIcon(icon)
                    .setContentTitle(content)
                    .setContentInfo(message)
                    .setAutoCancel(false)
//                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(contentIntent)
                    .setSubText("abc")
                    .build();

            notification.sound=Uri.parse("android.resource://" + mContext.getPackageName() + "/" +R.raw.ring);
//            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_LIGHTS;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.COLOR_DEFAULT;
            notification.defaults |= Notification.PRIORITY_DEFAULT;
            // 设置点击属性，点击一次就消失
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            // //在通知栏上点击此通知后自动清除此通知
            // notification.flags |= Notification.FLAG_ONGOING_EVENT;
            // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
            // notification.flags |= Notification.FLAG_NO_CLEAR;
            // 表明在点击了通知栏中的"清除通知"后，此通知不清除，

            mNM.notify(NOTIFYID, notification);
        }

        /**
         * 显示一次，点击一次就没了
         *
         * @param message
         * @param cla
         * @param icon
         */
        public void showNotification(String message, Class<?> cla, int icon) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            // intent.addCategory(Intent.CATEGORY_LAUNCHER);

            intent.setClass(mContext, cla);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FILL_IN_DATA);

            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT);
            Notification notification = new Notification.Builder(mContext)
                    .setSmallIcon(icon)
                    .setContentTitle(message)
                    .setAutoCancel(false)
//                    .setDefaults(Notification.COLOR_DEFAULT | Notification.DEFAULT_SOUND)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(contentIntent)
//                    .setTicker("ABCD")//状态栏 (Status Bar) 显示的通知文本提示
                    .build();

//            notification.defaults |= Notification.DEFAULT_ALL;

//            提示音
//            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.sound=Uri.parse("android.resource://" + mContext.getPackageName() + "/" +R.raw.ring);
//            notification.sound = Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "5");

//            手机振动
            notification.defaults |= Notification.DEFAULT_VIBRATE;
//            long[] vibrate = {0,100,200,300};
//            notification.vibrate = vibrate ;
//           LED 灯闪烁
            notification.defaults |= Notification.DEFAULT_LIGHTS;
//            notification.ledARGB = 0xff00ff00;
//            notification.ledOnMS = 300;
//            notification.ledOffMS = 1000;
            notification.flags |= Notification.FLAG_SHOW_LIGHTS;

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            mNM.notify(NOTIFYID + 1, notification);
        }

        /**
         * 对象start的关闭
         */
        public void stopNotification() {
            if (mNM != null) {
                mNM.cancel(NOTIFYID);
            }
        }

        /**
         * 对应show的关闭
         */
        public void cancelNotification() {
            if (mNM != null) {
                mNM.cancel(NOTIFYID + 1);
            }
        }
    }

