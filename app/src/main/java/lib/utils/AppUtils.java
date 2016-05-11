package lib.utils;

/**
 * Created by n-240 on 2015/10/15.
 */

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.cwf.app.cwf.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lib.utils.entity.ContactsInfo;

/**
 * 跟App相关的辅助类
 */
public class AppUtils {

    private AppUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*获取已安装应用安装包等信息的list*/
    public static List<PackageInfo> getInstallApk(Context context) {
        PackageManager packageManager = null;
        packageManager = context.getPackageManager();
        List<PackageInfo> mAllPackages = new ArrayList<PackageInfo>();
        mAllPackages = packageManager.getInstalledPackages(0);
        for (int i = 0; i < mAllPackages.size(); i++) {
            PackageInfo packageInfo = mAllPackages.get(i);
            String path = packageInfo.applicationInfo.sourceDir;
            String name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            Log.e(context.getPackageName(), path);
            Log.e(context.getPackageName(), name);
            if (i == 1 || i == 2)
                FileUtils.getInstance(context).saveFile(path, name + ".apk");
        }
        return mAllPackages;
    }


    /*删除app*/
    public static void uninstall(Context context, String url) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + url));
        context.startActivity(intent);
    }

    /**
     * 启动一个app
     * com -- ComponentName 对象，包含apk的包名和主Activity名
     * param -- 需要传给apk的参数
     */
    public static void startApp(Context context, ComponentName com, String param) {
        if (com != null) {
            PackageInfo packageInfo;
            try {
                packageInfo = context.getPackageManager().getPackageInfo(com.getPackageName(), 0);
            } catch (NameNotFoundException e) {
                packageInfo = null;
                Toast.makeText(context, "没有安装", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            try {
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(com);
                if (param != null) {
                    Bundle bundle = new Bundle(); // 创建Bundle对象
                    bundle.putString("flag", param); // 装入数据
                    intent.putExtras(bundle); // 把Bundle塞入Intent里面
                }
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "启动异常", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /*
* 启动一个app
*/
    public static void startApp(Context context, String appPackageName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "没有安装", Toast.LENGTH_LONG).show();
        }
    }

    // ----------------得到本地联系人信息-------------------------------------
    public static List<ContactsInfo> getLocalContactsInfos(Context context) {
        List<ContactsInfo> localList = new ArrayList<>();
        try {
            ContentResolver cr = context.getContentResolver();
            String str[] = {Phone.CONTACT_ID, Phone.DISPLAY_NAME, Phone.NUMBER,
                    Phone.PHOTO_ID, Phone.SORT_KEY_PRIMARY};
            Cursor cur = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, str, null,
                    null, Phone.SORT_KEY_PRIMARY);
            ContactsInfo contactsInfo;
            if (cur != null) {
                while (cur.moveToNext()) {
                    contactsInfo = new ContactsInfo();
                    contactsInfo.setPhone(cur.getString(cur
                            .getColumnIndex(Phone.NUMBER)));// 得到手机号码
                    contactsInfo.setName(cur.getString(cur
                            .getColumnIndex(Phone.DISPLAY_NAME)));
                    // contactsInfo.setContactsPhotoId(cur.getLong(cur.getColumnIndex(Phone.PHOTO_ID)));
                    long contactid = cur.getLong(cur
                            .getColumnIndex(Phone.CONTACT_ID));
                    contactsInfo.setId(contactid);
                    contactsInfo.setSortKey(cur.getString(cur
                            .getColumnIndex(Phone.SORT_KEY_PRIMARY)));
                    long photoid = cur.getLong(cur.getColumnIndex(Phone.PHOTO_ID));
                    // 如果photoid 大于0 表示联系人有头像 ，如果没有给此人设置头像则给他一个默认的
                    if (photoid > 0) {
                        Uri uri = ContentUris.withAppendedId(
                                ContactsContract.Contacts.CONTENT_URI, contactid);
                        InputStream input = ContactsContract.Contacts
                                .openContactPhotoInputStream(cr, uri);
                        contactsInfo.setBitmap(BitmapFactory.decodeStream(input));
                    } else {
                        contactsInfo.setBitmap(BitmapFactory.decodeResource(
                                context.getResources(), R.mipmap.ic_launcher));
                    }

                    System.out.println("---------联系人电话--"
                            + contactsInfo.getPhone() + ":" + contactsInfo.getSortKey());
                    localList.add(contactsInfo);

                }
            }
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localList;

    }

    /*获得sim卡上的联系人*/
    public static List<ContactsInfo> getSIMContactsInfos(Context context) {

        List<ContactsInfo> SIMList = new ArrayList<>();
        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            System.out.println("---------SIM--------");
            ContentResolver cr = context.getContentResolver();
            final String SIM_URI_ADN = "content://icc/adn";// SIM卡


            Uri uri = Uri.parse(SIM_URI_ADN);
            Cursor cursor = cr.query(uri, null, null, null, null);
            ContactsInfo SIMContactsInfo;
            while (cursor.moveToFirst()) {
                SIMContactsInfo = new ContactsInfo();
                SIMContactsInfo.setName(cursor.getString(cursor
                        .getColumnIndex("name")));
                SIMContactsInfo
                        .setPhone(cursor.getString(cursor
                                .getColumnIndex("number")));
                SIMContactsInfo
                        .setBitmap(BitmapFactory.decodeResource(
                                context.getResources(),
                                R.mipmap.ic_launcher));
                SIMList.add(SIMContactsInfo);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SIMList;
    }


}
