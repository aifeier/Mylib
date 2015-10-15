package demo.picture.toolbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import lib.BaseActivity;

/**
 * Created by n-240 on 2015/9/30.
 */
public class BaseSelectPhotos extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("选择图片");
        getActionBar().setDisplayShowHomeEnabled(false);
    }

    @Override
    public void onClick(View v) {

    }
}
