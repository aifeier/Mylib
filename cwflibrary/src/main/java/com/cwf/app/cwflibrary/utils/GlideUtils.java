package com.cwf.app.cwflibrary.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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

    private static Drawable Error_drawable = null;

    private static int Loading_Img = R.drawable.loading4;

    private static Drawable Loading_drawale = null;

    private static Context mContext;

    private static Fragment mFragment;

    private static Activity mActivity;

    public static int getError_Img() {
        return Error_Img;
    }

    public static void setError_Img(int error_Img) {
        Error_Img = error_Img;
    }

    public static Drawable getError_drawable() {
        return Error_drawable;
    }

    public static void setError_drawable(Drawable error_drawable) {
        Error_drawable = error_drawable;
    }

    public static int getLoading_Img() {
        return Loading_Img;
    }

    public static void setLoading_Img(int loading_Img) {
        Loading_Img = loading_Img;
    }

    public static Drawable getLoading_drawale() {
        return Loading_drawale;
    }

    public static void setLoading_drawale(Drawable loading_drawale) {
        Loading_drawale = loading_drawale;
    }

    public static Context getmContext() {
        return mContext;
    }

    public static void setmContext(Context mContext) {
        clearContext();
        GlideUtils.mContext = mContext;
    }

    public static Fragment getmFragment() {
        return mFragment;
    }

    public static void setmFragment(Fragment mFragment) {
        clearContext();
        GlideUtils.mFragment = mFragment;
    }

    public static Activity getmActivity() {
        return mActivity;
    }

    public static void setmActivity(Activity mActivity) {
        clearContext();
        GlideUtils.mActivity = mActivity;
    }

    public static FragmentActivity getmFragmentActivity() {
        return mFragmentActivity;
    }

    public static void setmFragmentActivity(FragmentActivity mFragmentActivity) {
        clearContext();
        GlideUtils.mFragmentActivity = mFragmentActivity;
    }

    private static FragmentActivity mFragmentActivity;

    private static void setData(DrawableTypeRequest drawableTypeRequest, ImageView img){
        if(Error_drawable != null)
            drawableTypeRequest.error(Error_drawable);
        else
            drawableTypeRequest.error(Error_Img);

        if(Loading_drawale != null)
            drawableTypeRequest.placeholder(Loading_drawale);
        else
            drawableTypeRequest.placeholder(Loading_drawale);
        drawableTypeRequest.diskCacheStrategy(DiskCacheStrategy.RESULT).into(img);

    }

    private static RequestManager getInstance(){
        if(mContext != null )
            return Glide.with(mContext);
        else
            if(mActivity != null)
                return Glide.with(mActivity);
            else
                if(mFragment != null)
                    return Glide.with(mFragment);
                else
                    if(mFragmentActivity != null)
                        return Glide.with(mFragment);
                    else
                        return null;
    }

    private static void clearContext(){
        mContext = null;
        mFragmentActivity = null;
        mFragment = null;
        mActivity = null;
    }


    /*error和placeholder无效*/
    public static void setImageView(String url, ImageView img){
        setData(getInstance().load(url), img);
    }

    public static void setImageView(Drawable drawable, ImageView img){
        setData(getInstance().load(drawable), img);
    }

    public static void setImageView(int resources, ImageView img){
        setData(getInstance().load(resources), img);
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
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
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
