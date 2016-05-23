package com.cwf.libs.okhttplibrary;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cwf.libs.okhttplibrary.callback.ResultCallBack;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by n-240 on 2016/5/20.
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;
    private final MediaType mMediaType = MediaType.parse("text/html, charset=utf-8");
    private static final MediaType MEDIA_TYPE_ATTACH = MediaType.parse("application/octet-stream");

    private OkHttpClientManager() {

        mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(10000, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .build();
        mDelivery = new Handler(Looper.getMainLooper());
        final int sdk = Build.VERSION.SDK_INT;

        if (sdk >= 23) {
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .excludeFieldsWithModifiers(
                            Modifier.FINAL,
                            Modifier.TRANSIENT,
                            Modifier.STATIC);
            mGson = gsonBuilder.create();
        } else {
            mGson = new Gson();
        }
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    public Handler getDelivery() {
        return mDelivery;
    }


    /*同步get请求*/
    public String get(String url) throws Exception {
        return get(url, new HashMap<String, String>());
    }

    /*同步get请求*/
    public String get(String url, HashMap<String, String> params) throws Exception {
        if (params.size() > 0)
            url = getGetUrl(url, params);
        Request request = new Request.Builder().url(url).build();
        return enqueue(request);

    }

    /*异步get请求*/
    public void get(String url, ResultCallBack resultCallBack) {
        get(url, new HashMap<String, String>(), resultCallBack);
    }

    /*异步get请求*/
    public void get(String url, HashMap<String, String> params, ResultCallBack resultCallBack) {
        if (params.size() > 0)
            url = getGetUrl(url, params);
        Request request = new Request.Builder()
                .addHeader("key", "ed238d5e9c0f41c0155b8c2aead25e73")
                .get().url(url).build();
        enqueue(resultCallBack, request);
    }

    /*同步post请求*/
    public String post(String url) throws Exception {
        return post(url, new HashMap<String, String>());
    }

    /*同步post请求*/
    public String post(String url, HashMap<String, String> params) throws Exception {
        Request request = new Request.Builder().url(url).post(getRequestBody(params)).build();
        return enqueue(request);
    }

    /*异步post请求*/
    public void post(String url, ResultCallBack resultCallBack) {
        post(url, new HashMap<String, String>(), resultCallBack);
    }

    /*异步post请求*/
    public void post(String url, HashMap<String, String> params, ResultCallBack resultCallBack) {
        Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", "ed238d5e9c0f41c0155b8c2aead25e73")
                .post(getRequestBody(params)).build();
        enqueue(resultCallBack, request);
    }

    public void downloadFile(String url, String destFileDir, ResultCallBack resultCallBack) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        enqueueDownload(resultCallBack, request, destFileDir, url);
    }

    public void uploadFile(String url, List<File> fileList, ResultCallBack resultCallBack) {
        Request request = new Request.Builder()
                .url(url)
                .post(getRequestBody(new HashMap<String, String>(), fileList)).build();
        enqueue(resultCallBack, request);
    }

    public void uploadFile(String url, HashMap<String, String> params, List<File> fileList, ResultCallBack resultCallBack) {
        Request request = new Request.Builder()
                .url(url)
                .post(getRequestBody(params, fileList)).build();
        enqueue(resultCallBack, request);
    }

    private RequestBody getRequestBody(HashMap<String, String> params, List<File> fileList) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        Iterator iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = params.get(key);
            builder.addFormDataPart(key, value);
        }
        for (File file : fileList) {
            if (!file.exists())
                continue;
            builder.addFormDataPart("files", file.getName(),
                    RequestBody.create(MEDIA_TYPE_ATTACH, file));
        }
        return builder.build();
    }

    private RequestBody getRequestBody(HashMap<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        Iterator iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = params.get(key);
            builder.add(key, value);
        }

        return builder.build();
    }

    /*获取get方式的真实Url*/
    private String getGetUrl(String url, HashMap<String, String> params) {
        url += "?";
        Iterator iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            String value = params.get(key);
            url += key + "=" + value + "&";
        }
        url = url.substring(0, url.length() - 2);
        return url;
    }

    private String enqueue(Request request) throws IOException {
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            return response.message();
        }
    }

    /*发送请求*/
    private void enqueueDownload(ResultCallBack resultCallBack, Request request, String destFileDir, String url) {
        resultCallBack.onStart();
        Log.e("OkHttpClientManager", request.url().toString());
        mOkHttpClient.newCall(request).enqueue(getDownloadCallback(resultCallBack, destFileDir, url));
    }

    /*发送请求*/
    private void enqueue(ResultCallBack resultCallBack, Request request) {
        resultCallBack.onStart();
        Log.e("OkHttpClientManager", request.url().toString());
        mOkHttpClient.newCall(request).enqueue(getCallback(resultCallBack));
    }

    private Callback getDownloadCallback(final ResultCallBack resultCallBack, final String destFileDir, final String url) {
        return new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                sendFailure(resultCallBack, e);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    long allSize = response.body().contentLength();
                    long donwSize = 0;
                    is = response.body().byteStream();
                    File dir = new File(destFileDir);
                    if (!dir.exists())
                        dir.mkdirs();
                    File file = new File(destFileDir, getFileName(url));
                    if (!file.exists())
                        file.createNewFile();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        donwSize += len;
                        updateFileSize(resultCallBack, donwSize, allSize);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccess(resultCallBack, file.getAbsolutePath());
                } catch (Exception e) {
                    sendFailure(resultCallBack, e);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }

            }
        };
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    /*异步请求返回*/
    private Callback getCallback(final ResultCallBack resultCallBack) {
        return new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                sendFailure(resultCallBack, e);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    sendSuccess(resultCallBack, response.body().string());
                } catch (Exception e) {
                    sendFailure(resultCallBack, e);
                }
            }
        };
    }

    private void updateFileSize(final ResultCallBack resultCallBack, final long downSize, final long allSize) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                resultCallBack.onDownloading(downSize, allSize);
            }
        });
    }

    private void sendSuccess(final ResultCallBack resultCallBack, final String result) {
        Log.e("OkHttpClientManager", result);
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                resultCallBack.onSuccess(result);
                resultCallBack.onStop();
            }
        });
    }

    private void sendFailure(final ResultCallBack resultCallBack, final Exception e) {
        Log.e("OkHttpClientManager", e.getMessage());
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                resultCallBack.onFailure(e);
                resultCallBack.onStop();
            }
        });
    }
}
