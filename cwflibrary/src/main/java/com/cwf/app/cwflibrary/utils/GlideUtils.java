package com.cwf.app.cwflibrary.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cwf.app.cwflibrary.R;

import java.io.File;

/**
 * Created by n-240 on 2015/10/16.
 */
/*
* google推荐的图片加载类glide
* 可以实现网络、本地等图片的加载，支持gif图片
* */
public class GlideUtils {


    private static int Error_Img = R.drawable.error;

    private static int Loading_Img = R.drawable.loading1;

    private static Context mContext;

    /*
    * set Context
    * */
    public static void init(Context context){
        mContext = context;
    }

    public static void setErrorImg(int resourcesId){
        Error_Img = resourcesId;
    }

    public static void setLoadingImg(int resourcesId){
        Loading_Img = resourcesId;
    }



    /*
    * 使用默认的加载和错误图片。
    * PS: 必须先init(),设置mContext
    * */
    public static void getNetworkImage(String url, ImageView img){
        if(mContext!=null) {
            Glide.with(mContext)
                    .load(url)
                    .error(Error_Img)
                    .placeholder(Loading_Img)
                    .into(img);
        }else{

        }
    }

    /*
* 使用默认的加载和错误图片。
* PS: 必须先init(),设置mContext
* */
    public static void getResourcesImage(int resourcesId, ImageView img){
        if(mContext!=null) {
            Glide.with(mContext)
                    .load(resourcesId)
                    .error(Error_Img)
                    .placeholder(Loading_Img)
                    .into(img);
        }
    }

    /*
    *
    * 使用默认的加载和错误图片。
    * */
    public static void getNetworkImage(Context context , String url, ImageView img){
        Glide.with(context)
                .load(url)
                .error(Error_Img)
                .placeholder(Loading_Img)
                .into(img);
    }


    public static void getNetworkImage(Context context , String url,int Error_img, int Loading_img, ImageView img){
        Glide.with(context)
                .load(url)
                .error(Error_img)
                .placeholder(Loading_img)
                .into(img);
    }

    public static void getNetworkImage(Context context , String url,Drawable Error_img, Drawable Loading_img, ImageView img){
        Glide.with(context)
                .load(url)
                .error(Error_img)
                .placeholder(Loading_img)
                .into(img);
    }

    /*
    获取本地图片
    * */
    public static void getResourcesImage(Context context, int resourcesId, ImageView img){
        Glide.with(context)
                .load(resourcesId)
                .error(Error_Img)
                .placeholder(Loading_Img)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//让Glide既缓存全尺寸又缓存其他尺寸
//                .override(200, 100)//mage Resizing
//                .centerCrop()//Center Cropping
                .into(img);
    }

    public static void getDrawableImage(Context context, Drawable drawable, ImageView img){
        Glide.with(context)
                .load(drawable)
                .error(Error_Img)
                .placeholder(Loading_Img)
                .into(img);
    }

    public static void getUriImage(Context context, Uri uri, ImageView img){
        Glide.with(context)
                .load(uri)
                .error(Error_Img)
                .placeholder(Loading_Img)
                .into(img);
    }

    public static void getFileImage(Context context, File file, ImageView img){
        Glide.with(context)
                .load(file)
                .error(Error_Img)
                .placeholder(Loading_Img)
                .into(img);
    }



}
