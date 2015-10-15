package demo.intent;

import android.os.Bundle;
import android.util.Log;

import com.cwf.app.cwf.R;
import com.cwf.app.okhttplibrary.OkHttpUtil;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Response;

import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lib.BaseActivity;
import lib.utils.ActivityUtils;

/**
 * Created by n-240 on 2015/9/24.
 */
public class OkhttpDemo extends BaseActivity implements Callback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_text_img);
        setTheme(R.style.FullBleedTheme);
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("tel", "15867117181"));
        FormEncodingBuilder form = new FormEncodingBuilder();
        form.add("", "");
        RequestBody b = form.build();
        String u = OkHttpUtil.attachHttpGetParams(
                "http://apis.baidu.com/apistore/mobilephoneservice/mobilephone", params);
        OkHttpUtil.enqueue(
                new Request.Builder().url(u).post(b)
                        .addHeader("apikey", " ed238d5e9c0f41c0155b8c2aead25e73").build(), 1, this);
    }

    @Override
    public void onFailure(Request request, IOException e) {
        ActivityUtils.showTip(e.getMessage(), true);
    }

    @Override
    public void onResponse(Response response) throws IOException {
        switch (Integer.getInteger(response.request().tag().toString(), 0)) {
            case 0:
                Log.e("", response.body().string());
                break;
        }
        ActivityUtils.showTip(response.body().string(), true);
    }
}
