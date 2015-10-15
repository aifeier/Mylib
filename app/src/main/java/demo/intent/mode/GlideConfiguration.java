package demo.intent.mode;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/*
在配置文件中添加
*  <meta-data android:name="demo.intent.mode.GlideConfiguration"
            android:value="GlideModule"/>

    更改Gilde默认的图片显示 RGB_565为PREFER_ARGB_8888，更高清但更占内存
* */

/**
 * Created by n-240 on 2015/10/15.
 */
public class GlideConfiguration implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Apply options to the builder here.
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
    }
}
