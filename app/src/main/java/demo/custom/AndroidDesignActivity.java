package demo.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cwf.app.cwf.R;

import lib.BaseActivity;
import lib.utils.ActivityUtils;


/**
 * Created by n-240 on 2015/11/10.
 */
public class AndroidDesignActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private EditText account, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_design);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        account = (EditText)findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    /*输入密码后点击键盘确定进行的操作*/
                    ActivityUtils.showTip("执行登录操作", false);
                    return false;
            }
        });

    }
}
