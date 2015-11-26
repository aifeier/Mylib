package demo;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.webkit.WebView;

import com.cwf.app.cwf.R;

import java.io.IOException;

import lib.utils.ActivityUtils;

/**
 * Created by n-240 on 2015/11/26.
 */
public class Html5Activity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webview);
        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl("file:///android_asset/html/first.htm");

    }
}
