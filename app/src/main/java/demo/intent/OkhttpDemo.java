package demo.intent;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cwf.app.cwf.R;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;

import demo.intent.entity.News;
import demo.intent.entity.WeaherData;
import lib.BaseActivity;
import lib.utils.ActivityUtils;
import lib.utils.FileUtils;
import lib.utils.OkHttpClientManager;

/**
 * Created by n-240 on 2015/9/24.
 */
public class OkhttpDemo extends BaseActivity implements Callback{

    private TextView textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_text_img);
        textview = (TextView) findViewById(R.id.textview);
        setTheme(R.style.FullBleedTheme);
 /*       HashMap<String, String> params = new HashMap<String, String>();
        params.put("tel", "15867117181");
        FormEncodingBuilder form = new FormEncodingBuilder();
        form.add("", "");
        RequestBody b = form.build();

        OkHttpClientManager.getAsyn("http://api.huceo.com/meinv/other/?key=e7b0c852050f609d927bc20fe11fde9c&num=10&page=1",
                new OkHttpClientManager.ResultCallback<News>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(News news) {
                        textview.setText(news.getNewslist().get(0).getTitle());//UI线程
                    }
                });*/

        OkHttpClientManager.downloadAsyn("http://zx.kaitao.cn/UserFiles/Image/beijingtupian6.jpg",
                FileUtils.createPath("files"), new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                ActivityUtils.showTip("下载失败", false);
            }

            @Override
            public void onResponse(String response) {
                ActivityUtils.showTip("下载成功", false);
            }
        });

    }

    @Override
    public void onFailure(Request request, IOException e) {
        ActivityUtils.showTip(e.getMessage(), true);
    }

    @Override
    public void onResponse(Response response) throws IOException {
        Log.e("ABC", response.body().string() + response.message() + response.request().body().toString());
//        ActivityUtils.showTip(response.body().string(), true);
        final String str  = response.body().string();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textview.setText(str);
            }

        });
    }

}
