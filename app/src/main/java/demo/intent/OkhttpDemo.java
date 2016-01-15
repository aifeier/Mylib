package demo.intent;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cwf.app.cwf.R;
import com.cwf.app.okhttputilslibrary.okhttp.callback.ResultCallback;
import com.cwf.app.okhttputilslibrary.okhttp.request.OkHttpDisplayImgRequest;
import com.cwf.app.okhttputilslibrary.okhttp.request.OkHttpGetRequest;
import com.cwf.app.okhttputilslibrary.okhttp.request.OkHttpRequest;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import demo.intent.entity.News;
import demo.intent.entity.WeaherData;
import lib.BaseActivity;
import lib.utils.ActivityUtils;
import lib.utils.FileUtils;
import lib.utils.OkHttpClientManager;
import lib.utils.TimeUtils;

/**
 * Created by n-240 on 2015/9/24.
 */
public class OkhttpDemo extends BaseActivity implements Callback{

    public static String ACTION = "demo.intent.broadcastReceiver";
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*系统广播只能动态注册，在main中注册无效*/
            String mAction = intent.getAction();
            if(intent.getAction().equals(ACTION)) {
                Log.e(getPackageName(), ACTION);
                textview.setText(intent.getStringExtra("msg"));
            }else if(mAction.equals(Intent.ACTION_SCREEN_ON)){
                Log.e(getPackageName(), "Screen_On");
            }else if(mAction.equals(Intent.ACTION_SCREEN_OFF)){
                Log.e(getPackageName(), "Screen_Off");
            }
        }
    };



    private TextView textview;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                textview.setText((String) msg.obj);
            }
        }

    };


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
        /*OkHttpClientManager.downloadAsyn("http://i6.topit.me/6/3d/c7/1132049425fc9c73d6o.jpg",
                FileUtils.createPath("files"), new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onFileDownSize(long downsize, long allSize) {
                        Log.e(getPackageName(), downsize + "/" + allSize);
                        ActivityUtils.showTip("正在下载" + downsize * 100 / allSize + "%", false);
                        Message message = new Message();
                        message.what = 1;
                        message.obj = "正在下载" + downsize * 100 / allSize + "%";
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        ActivityUtils.showTip("下载失败", false);
                    }


                    @Override
                    public void onResponse(String response) {
                        ActivityUtils.showTip("下载完成", false);
                    }

                });*/
        bindService(ActivityUtils.getServiceIntent(this, ServiceDemo.ACTION), serviceConnection, BIND_AUTO_CREATE);
        startService(ActivityUtils.getServiceIntent(this, ServiceDemo.ACTION));

        registerReceiver(broadcastReceiver, new IntentFilter(ACTION));
        registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(broadcastReceiver, new IntentFilter((Intent.ACTION_SCREEN_OFF)));
        /*new OkHttpDisplayImgRequest.Builder()
                .url("http://i6.topit.me/6/3d/c7/1132049425fc9c73d6o.jpg")
                .errResId(R.drawable.error)
                .imageView((ImageView) findViewById(R.id.imageview))
                .displayImage(null);
        new OkHttpRequest.Builder()
                .url("http://i6.topit.me/6/3d/c7/1132049425fc9c73d6o.jpg")
                .destFileDir(FileUtils.createPath("files"))
                .destFileName("abc.jpg")
                .download(new ResultCallback<String>() {
                    @Override
                    public void onBefore(Request request) {
                        ActivityUtils.showTip("开始下载", false);
                        super.onBefore(request);
                    }

                    @Override
                    public void onAfter() {
                        ActivityUtils.showTip("下载结束", false);
                        super.onAfter();
                    }

                    @Override
                    public void onError(Request request, Exception e) {
                        ActivityUtils.showTip("下载失败", false);
                    }

                    @Override
                    public void onResponse(String response) {
                        ActivityUtils.showTip("下载完成", false);
                    }

                    @Override
                    public void inProgress(float progress) {
                        textview.setText("" + progress);
                        ActivityUtils.showTip("下载进度：" + ((int) (progress * 100)) + "%", false);
                        super.inProgress(progress);
                    }
                });


        new OkHttpRequest.Builder()
                .url("http://www.baidu.com")
                .headers(null)
                .params(null)
                .tag(1)
                .get(new ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ActivityUtils.showTip("失败" + e.getMessage(), false);
                    }

                    @Override
                    public void onResponse(String response) {
                        ActivityUtils.showTip(response, false);
                    }
                });*/

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("mobile", "15867117181");
        new OkHttpRequest.Builder()
                .url("http://120.26.45.151:8081/yyg/member!code.action")
                .params(params)
                .tag(1)
                .post(new ResultCallback<String>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ActivityUtils.showTip("失败" + e.getMessage(), false);
                    }

                    @Override
                    public void onResponse(String response) {
                        ActivityUtils.showTip(response, true);
                    }
                });
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
           Log.e(getPackageName(), ((ServiceDemo) service).getString());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        stopService(ActivityUtils.getServiceIntent(this, ServiceDemo.ACTION));
        unbindService(serviceConnection);
        super.onDestroy();
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
