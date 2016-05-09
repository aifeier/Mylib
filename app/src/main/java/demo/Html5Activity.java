package demo;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cwf.app.cwf.R;

import java.io.IOException;

import lib.utils.ActivityUtils;
import lib.utils.SDCardUtils;

/**
 * Created by n-240 on 2015/11/26.
 */
public class Html5Activity extends Activity{
    private  WebView webView;
    private String load = "file:///android_asset/html/first.htm";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webview);
        webView = (WebView) findViewById(R.id.web_view);
        WebSettings webSettings =  webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setAppCacheEnabled(true);
 /*       webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(SDCardUtils.getAbleDirectoryPath());*/

        webView.setWebViewClient(new WebViewClient(){
            /*重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。*/
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
                    webView.goBack();
                    return true;
                }
                return super.shouldOverrideKeyEvent(view, event);
            }
        });
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                    /*返回键*/
                    webView.goBack();
                    return true;
                }
                finish();
                return false;
            }
        });
//        load = "http://192.168.10.73:9087/m/page/login/login.jsp";
        webView.loadUrl(load);

    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
}
