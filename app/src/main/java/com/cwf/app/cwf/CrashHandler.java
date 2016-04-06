package com.cwf.app.cwf;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import lib.utils.FileUtils;
import lib.utils.TimeUtils;

/**
 * ClassName: CrashHandler Function:
 * UncaughtException处理抽象类,当程序发生Uncaught异常的时候,由该类来接管程序,并保存错误报告.
 * 由子类实现具体的错误文件上传功能
 */
public abstract class CrashHandler implements UncaughtExceptionHandler {
    protected Context mContext;
    protected String errorTitle;
    private UncaughtExceptionHandler mDefaultHandler;
    /**
     * 用来存储设备信息和异常信息
     */
    private Map<String, String> mLogInfo = new HashMap<>();
    private long mInitTime;//CrashHandler初始化时间

    public static void RecursionDeleteFile(File file) {
        File[] files = file.listFiles();
        for (File tempFile : files) {
            int day = getDeleteDay(tempFile);
            if (day > 7) {
                tempFile.delete();
            }
        }
    }

    public static int getDeleteDay(File file) {
        String name = file.getName();
        name = name.replace("error-", "");
        name = name.replace(".log", "");
        int day = -1;
        try {
            day = daysBetween(name, TimeUtils.getSimpleDate("yyyy-MM-dd"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    public static String setData(String dstr) {
        dstr = dstr.replace("/", "-");
        return dstr;
    }

    public static int daysBetween(String smdate, String bdate) throws ParseException {
        smdate = setData(smdate);
        bdate = setData(bdate);
        SimpleDateFormat sdf = TimeUtils.getSimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    public void init(Context context) {
        mInitTime = System.currentTimeMillis();
        mContext = context;
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该重写的方法来处理 (non-Javadoc)
     * 此方法可能会执行多次
     */
    public void uncaughtException(Thread thread, Throwable paramThrowable) {
        /**软件杀死后有可能立刻再次启动执行初始化方法init(Context context),并且立刻发生异常,
         故加入时间差做判断，只有第一次发生异常时保存并上传至服务器**/
        if (System.currentTimeMillis() - mInitTime > 1000) {
            if (!handleException(thread, paramThrowable) && mDefaultHandler != null) {
                // 如果自定义的没有处理则让系统默认的异常处理器来处理
                mDefaultHandler.uncaughtException(thread, paramThrowable);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        goToLaunchActivity();
        killCurrentProcess();
    }

    /**
     * 回到启动页
     */
    protected void goToLaunchActivity() {
        final Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 一秒后重启应用
     */
    protected void restartApplication() {
        final Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent);
    }

    /**
     * handleException:{自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.}
     *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    public boolean handleException(Thread thread, Throwable paramThrowable) {
        if (paramThrowable == null)
            return false;
        new Thread() {
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        errorTitle = paramThrowable.getClass().getName();
        // 获取设备参数信息
        getDeviceInfo(mContext);
        // 保存日志文件
        String errorFilePath = saveCrashLogToFile(paramThrowable);
        if (errorFilePath != null) {
            try {
                Thread uploadErrorThread = new Thread(new UploadFileRunnable(errorFilePath));
                uploadErrorThread.start();
                uploadErrorThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 继承本类 实现此方法实现错误日志上传功能
     *
     * @param filePath 错误文件绝对路径
     */
    public abstract void upLoadErrorFile(String filePath);

    /**
     * 获取设备参数信息
     */
    public void getDeviceInfo(Context paramContext) {
        try {
            // 获得包管理器
            PackageManager mPackageManager = paramContext.getPackageManager();
            // 得到该应用的信息，即主Activity
            PackageInfo mPackageInfo = mPackageManager.getPackageInfo(
                    paramContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (mPackageInfo != null) {
                String versionName = mPackageInfo.versionName == null ? "null"
                        : mPackageInfo.versionName;
                String versionCode = mPackageInfo.versionCode + "";
                mLogInfo.put("versionName", versionName);
                mLogInfo.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        // 反射机制
        Field[] mFields = Build.class.getDeclaredFields();
        // 迭代Build的字段key-value 此处的信息主要是为了在服务器端手机各种版本手机报错的原因
        for (Field field : mFields) {
            try {
                field.setAccessible(true);
                mLogInfo.put(field.getName(), field.get("").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存错误日志到文件
     *
     * @return 保存成功返回文件绝对路径，否则为null
     */
    private String saveCrashLogToFile(Throwable paramThrowable) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : mLogInfo.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.append(key + "=" + value + "\r\n");
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        paramThrowable.printStackTrace(printWriter);
        paramThrowable.printStackTrace();
        Throwable throwable = paramThrowable.getCause();
        // 迭代栈队列把所有的异常信息写入writer中
        while (throwable != null) {
            throwable.printStackTrace(printWriter);
            // 换行 每个个异常栈之间换行
            printWriter.append("\r\n");
            throwable = throwable.getCause();
        }
        // 记得关闭
        printWriter.close();
        String logTime = TimeUtils.getSimpleDate("yyyy-MM-dd hh-mm-ss") + "\r\n";
        builder.append(logTime);
        String result = writer.toString() + "\r\n";
        builder.append(result);
        // 保存文件，设置文件名
        String currentTime = TimeUtils.getSimpleDate("yyyy-MM-dd-hh-mm-ss");
        String fileName = "error-" + currentTime + ".txt";
        try {
            File mDirectory = new File(FileUtils.logCache);
            if (mDirectory.exists() || mDirectory.mkdirs()) {
                File logFile = new File(mDirectory, fileName);
                FileOutputStream fos = new FileOutputStream(logFile, false);
                fos.write(builder.toString().getBytes());
                fos.close();
//                TQLogUtils.e("保存错误到文件:" + logFile.getPath());
                return logFile.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    class UploadFileRunnable implements Runnable {
        String errorFilePath;

        public UploadFileRunnable(String errorFilePath) {
            this.errorFilePath = errorFilePath;
        }

        @Override
        public void run() {
            upLoadErrorFile(errorFilePath);
        }
    }

}
