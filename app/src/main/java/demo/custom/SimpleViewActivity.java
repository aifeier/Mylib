package demo.custom;

import android.os.Bundle;

import com.cwf.app.cwf.R;

import lib.BaseActivity;

/**
 * Created by n-240 on 2016/5/9.
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class SimpleViewActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_simple_view);
    }
}
