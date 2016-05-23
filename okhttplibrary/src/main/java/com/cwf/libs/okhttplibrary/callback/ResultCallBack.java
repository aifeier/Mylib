package com.cwf.libs.okhttplibrary.callback;

/**
 * Created by n-240 on 2016/5/20.
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public abstract class ResultCallBack {

    public void onStart() {

    }

    public void onStop() {

    }

    public void onDownloading(long downSize, long allSize) {

    }

    public abstract void onFailure(Exception e);

    public abstract void onSuccess(String result);
}
