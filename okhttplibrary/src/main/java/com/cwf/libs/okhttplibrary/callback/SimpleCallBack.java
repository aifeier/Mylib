package com.cwf.libs.okhttplibrary.callback;

import android.app.ProgressDialog;
import android.content.Context;

import okhttp3.Callback;

/**
 * Created by n-240 on 2016/5/20.
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public abstract class SimpleCallBack extends ResultCallBack {
    private ProgressDialog dialog;

    public SimpleCallBack(Context context) {
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setMessage("请求中...");
    }

    @Override
    public void onStart() {
        if (!dialog.isShowing())
            dialog.show();
    }

    @Override
    public void onStop() {
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
