package lib.utils;

/**
 * Created by n-240 on 2015/10/15.
 */
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 跟App相关的辅助类
 *
 *
 *
 */
public class AppUtils
{

    private AppUtils()
    {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");

    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (NameNotFoundException e)
        {
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
    public static String getVersionName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;

        } catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /*获取以安装应用安装包等信息的list*/
    public static List<PackageInfo> getInstallApk(Context context){
        PackageManager packageManager = null;
        packageManager = context.getPackageManager();
        List<PackageInfo> mAllPackages=new ArrayList<PackageInfo>();
        mAllPackages = packageManager.getInstalledPackages(0);
        for(int i = 0; i < mAllPackages.size(); i ++)
        {
            PackageInfo packageInfo = mAllPackages.get(i);
            String path = packageInfo.applicationInfo.sourceDir;
            String name = packageInfo.applicationInfo.loadLabel(packageManager).toString();
            Log.e(context.getPackageName(), path);
            Log.e(context.getPackageName(), name);
            if(i == 1 || i ==2)
                FileUtils.getInstance(context).saveFile(path, name + ".apk");
        }
        return mAllPackages;
    }


}
