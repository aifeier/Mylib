package lib.utils;

import java.io.File;

import android.annotation.SuppressLint;
import android.os.Environment;

import lib.MainApplication;

/**
 * 获取音频、图片、视频等目录
 */
public class DirectoryUtil {
    /**根目录**/
    public static final String ROOTPATH = "cwf";
    /**音频缓存目录**/
    public static final String RECORDINGCACHEPATH = "cache/recording";
    /**图片缓存目录**/
    public static final String IMAGECACHEPATH = "cache/image";
    /**视频缓存目录**/
    public static final String VIDEOCACHEPATH = "cache/video";
    /**音频附件目录**/
    public static final String RECORDINGPATH = "attachments/recording";
    /**图片附件目录**/
    public static final String IMAGEPATH = "attachments/image";
    /**视频附件目录**/
    public static final String VIDEOPATH = "attachments/video";
    /**版本升级时存放apk文件**/
    public static final String APKPATH="attatchments/apk";

    /**
     * 根据传入的子路径childDir获取硬盘缓存的路径地址。
//     * @param childDir
     * @return 缓存路径的文件对象
     */
    @SuppressLint("NewApi")
    public static File getDiskCacheDir(String uniqueName) {
        String cachePath;
        //当sd卡挂载或者设备的外存不可以拆卸时
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = Environment.getExternalStorageDirectory() + File.separator + ROOTPATH;
        } else {
            cachePath = MainApplication.getInstance().getCacheDir().getPath();
        }
        File tempFile = new File(cachePath + File.separator + uniqueName);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
        return tempFile;
    }

    /**获取音频缓存目录**/
    public static File getRecordingCacheDir() {
        return getDiskCacheDir(RECORDINGCACHEPATH);
    }

    /**获取图片缓存目录**/
    public static File getImageCacheDir() {
        return getDiskCacheDir(IMAGECACHEPATH);
    }

    /**获取视频缓存目录**/
    public static File getVideoCacheDir() {
        return getDiskCacheDir(VIDEOCACHEPATH);
    }

    /**获取音频附件目录**/
    public static File getRecoedingDir() {
        return getDiskCacheDir(RECORDINGPATH);
    }

    /**获取图片附件目录**/
    public static File getImageDir() {
        return getDiskCacheDir(IMAGEPATH);
    }

    /**获取视频附件目录**/
    public static File getVideoDir() {
        return getDiskCacheDir(VIDEOPATH);
    }
    /**获取apk存储目录**/
    public static File getApkDir(){
        return getDiskCacheDir(APKPATH);
    }
}
