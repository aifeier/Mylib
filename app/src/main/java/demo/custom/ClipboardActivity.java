package demo.custom;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.cwf.app.cwf.R;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by n-240 on 2016/2/19.
 *模拟监听剪切板，打印剪切板内容
 * @author cwf
 */
public class ClipboardActivity extends Activity {

    private ClipboardManager clipboardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData data = clipboardManager.getPrimaryClip();
                String str = data.getItemAt(0).getText().toString();
                EventBus.getDefault().post(str);
            }
        });

        EventBus.getDefault().register(this);
    }


    @Subscribe
    public void onEventMainThread(String str){
        Log.e("ABC", str);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
