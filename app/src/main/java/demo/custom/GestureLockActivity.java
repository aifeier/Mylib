package demo.custom;

import android.os.Bundle;
import android.util.Log;

import com.cwf.app.cwf.R;
import com.cwf.app.cwflibrary.utils.MD5Util;
import com.cwf.app.cwflibrary.widget.gesture.GestureLockViewGroup;

import lib.BaseActivity;
import lib.utils.ActivityUtils;

/**
 * Created by n-240 on 2015/10/20.
 */
public class GestureLockActivity extends BaseActivity{

    private GestureLockViewGroup mLockGroup ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_gesture);
        mLockGroup = (GestureLockViewGroup) findViewById(R.id.id_gestureLockViewGroup);
        mLockGroup.setAnswer(MD5Util.md5Encrypt("1234"));
        mLockGroup.setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {
            @Override
            public void onBlockSelected(int cId) {

            }

            @Override
            public void onGestureEvent(boolean matched) {
                ActivityUtils.showTip("" + matched, false);
            }

            @Override
            public void onUnmatchedExceedBoundary() {

            }

            @Override
            public void onGestureResult(String mResult) {
                Log.e("", mResult);
            }
        });
    }
}
