package com.cwf.libs.okhttplibrary;

import java.io.Serializable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by n-240 on 2016/5/20.
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class DeliveryMessage implements Serializable{
    private okhttp3.Callback callback;
    private okhttp3.Call call;
    private okhttp3.Response response;

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
