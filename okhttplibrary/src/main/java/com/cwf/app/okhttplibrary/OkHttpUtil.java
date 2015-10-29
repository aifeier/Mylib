package com.cwf.app.okhttplibrary;

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

//import org.apache.http.client.utils.URLEncodedUtils;
//import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {
	private static final OkHttpClient mOkHttpClient = new OkHttpClient();
	static {
		mOkHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
		}

	/**
	 * 不会开启异步线程。
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static Response execute(Request request) throws IOException {
		return mOkHttpClient.newCall(request).execute();
	}

	/**
	 * post submit
	 * @param murl
	 * @param responseCallback
	 * @param params
	 */
//	public static void enqueue(String murl, int tag, Callback responseCallback,
//			List<BasicNameValuePair> params) {
//		FormEncodingBuilder form = new FormEncodingBuilder();
//		for (BasicNameValuePair p : params)
//			form.add(p.getName(), p.getValue());
//		RequestBody mBody = form.build();
//
//		Request request = new Request.Builder().url(murl).post(mBody).tag(tag).build();
//		mOkHttpClient.newCall(request).enqueue(responseCallback);
//	}

	/***
	 * post submit
	 * @param murl
	 * @param responseCallback
	 * @param params
	 */

	public static void enqueue(String murl, int tag, Callback responseCallback,
			Map<String, String> params) {
		FormEncodingBuilder form = new FormEncodingBuilder();
		for (Entry<String, String> p : params.entrySet())
			if (p.getValue() != null && p.getKey() != null)
				form.add(p.getKey(), p.getValue());
		RequestBody mBody = form.build();

		Request request = new Request.Builder().url(murl).post(mBody).tag(tag).build();
		mOkHttpClient.newCall(request).enqueue(responseCallback);
	}

	/**
	 * 开启异步线程访问网络
	 * @param request
	 * @param responseCallback
	 */
	public static void enqueue(Request request, int tags, Callback responseCallback) {
		mOkHttpClient.newCall(request).enqueue(responseCallback);
	}

	/**
	 * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
	 * @param request
	 */
	public static void enqueue(Request request) {
		Log.e("http", request.httpUrl().toString());
		mOkHttpClient.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(Response arg0) throws IOException {
			}

			@Override
			public void onFailure(Request arg0, IOException arg1) {
			}
		});
	}

	public static String getStringFromServer(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		Response response = execute(request);
		if (response.isSuccessful()) {
			String responseUrl = response.body().string();
			return responseUrl;
		} else {
			throw new IOException("Unexpected code " + response);
		}
	}

	private static final String CHARSET_NAME = "UTF-8";

	/**
	 * 这里使用了HttpClinet的API。只是为了方便
	 * @param params
	 * @return
	 */
//	public static String formatParams(List<BasicNameValuePair> params) {
//		return URLEncodedUtils.format(params, CHARSET_NAME);
//	}

	/**
	 * 为HttpGet 的 url 方便的添加多个name value 参数。
	 * @param url
	 * @param params
	 * @return
	 */
	public static String attachHttpGetParams(String url, HashMap<String, String> params) {
		return url;
//		+ "?" + formatParams(params);
	}

	/**
	 * 为HttpGet 的 url 方便的添加1个name value 参数。
	 * @param url
	 * @param name
	 * @param value
	 * @return
	 */
	public static String attachHttpGetParam(String url, String name, String value) {
		return url + "?" + name + "=" + value;
	}
}
