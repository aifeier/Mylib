package demo.intent.mode.toolbox;

import android.app.ActivityManager;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import demo.intent.entity.BitmapLruCache;

/**
 * Created by n-240 on 2015/9/23.
 */
public class RequestManager {
    public final static String HTTP_TAG = "htmlAccess_Volley";

    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;

    private static Map<String, String > mHeader;

    private static Map<String, String > mParams;

    private RequestManager(){

    }

    public static void init(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mHeader = new HashMap<String, String>();
        mParams = new HashMap<String, String>();
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        // Use 1/8th of the available memory for this memory cache.
        int cacheSize = 1024 * 1024 * memClass / 8;
        mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache(cacheSize));
    }

    public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }

    public static StringRequest getStringRequestGet(String url){
        return getStringRequestGet(url, null, null);
    }

    public static StringRequest getStringRequestGet(String url , Response.Listener<String> listener) {
        return getStringRequestGet(url, listener, null);
    }

    public static StringRequest getStringRequestGet(String url , Response.Listener<String> listener,
                                                 Response.ErrorListener errorListener){
        return new StringRequest(Request.Method.GET, url, listener,
                errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return mHeader;
            }
        };
    }

    public static StringRequest getStringRequestPost(String url ,Response.Listener<String> listener) {
        return getStringRequestPost(url, listener, null);
    }

    public static StringRequest getStringRequestPost(String url, Response.Listener<String> listener,
                                                 Response.ErrorListener errorListener) {
        return new StringRequest(Request.Method.POST, url, listener,
                errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return mParams;
            }
        };
    }

//    public static <T> JsonRequest<T> getGsonRequest(String url, Class<T> clazz,
//                                                    Response.Listener<T> listener,
//                                                    Response.ErrorListener errorListener) {
//        return new JsonRequest<T>(url, clazz, listener, errorListener);
//    }

    public static void addHeader(Map<String,String> header){
        if (mHeader!=null)
            mHeader.clear();
        mHeader = header;
    }

    public static void addParams(Map<String, String> params){
        if(mParams!=null)
            mParams.clear();
        mParams = params;
    }
}
