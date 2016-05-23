package com.cwf.libs.okhttplibrary;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by n-240 on 2016/5/20.
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class DeliveryHandler extends Handler {
    @Override
    public void dispatchMessage(Message msg) {
        super.dispatchMessage(msg);
        DeliveryMessage deliveryMessage = (DeliveryMessage) msg.getData().get("data");
        try {
            deliveryMessage.getCallback()
                    .onResponse(deliveryMessage.getCall(), deliveryMessage.getResponse());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSuccess(okhttp3.Callback callback, Call call, Response response) {
        Message message = new Message();
        DeliveryMessage deliveryMessage = new DeliveryMessage();
        deliveryMessage.setCallback(callback);
        deliveryMessage.setCall(call);
        deliveryMessage.setResponse(response);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", deliveryMessage);
        message.setData(bundle);
        sendMessage(message);
    }
}
