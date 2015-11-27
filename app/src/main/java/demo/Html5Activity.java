package demo;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cwf.app.cwf.R;

import java.io.IOException;

import lib.utils.ActivityUtils;

/**
 * Created by n-240 on 2015/11/26.
 */
public class Html5Activity extends Activity{
    private  WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webview);
        webView = (WebView) findViewById(R.id.web_view);
        WebSettings webSettings =  webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/html/first.htm");

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
