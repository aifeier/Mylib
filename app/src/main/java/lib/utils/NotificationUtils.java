package lib.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
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
        private String soundPath = null;

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
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//            intent.addFlags(Intent.FILL_IN_DATA);

            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new Notification.Builder(mContext)
                    .setSmallIcon(icon)
                    .setContentTitle(content)
                    .setContentInfo(message)
                    .setAutoCancel(false)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(contentIntent)
                    .setSubText("abc")
                    .build();

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
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//这行代码会解决此问题
            intent.addFlags(Intent.FILL_IN_DATA);

            PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(mContext)
                    .setSmallIcon(icon)
                    .setContentTitle(message)
                    .setAutoCancel(false)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen( System.currentTimeMillis())
                    .setContentIntent(contentIntent)
                    .build();

//            if (!new File(soundPath).exists()) {
//                AssetManager am = null;
//                try {
//                    am = mContext.getAssets();
//                    InputStream in = am.open("tishi.wav");
//                    FileOutputStream fileOutputStream = new FileOutputStream(
//                            new File(soundPath));
//                    int remain = -1;
//                    while ((remain = in.read()) != -1) {
//                        fileOutputStream.write(remain);
//                    }
//                    fileOutputStream.flush();
//                    fileOutputStream.close();
//                    in.close();
//                    am.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
            // 配置震动
//            notification.defaults |= Notification.DEFAULT_VIBRATE;
            // long[] vibrate = {0,100,200,300};
            // notification.vibrate = vibrate ;
            // 默认声音
            // notification.defaults |= Notification.DEFAULT_SOUND;
//            notification.sound = Uri.parse(soundPath);
            // 设置点击属性，点击一次就消失
            // notification.flags = Notification.FLAG_ONGOING_EVENT;
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

