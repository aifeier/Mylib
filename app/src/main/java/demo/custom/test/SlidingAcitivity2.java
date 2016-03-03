package demo.custom.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.cwf.app.cwf.R;
import com.cwf.app.cwf.SlidingLayout;

/**
 * Created by n-240 on 2016/3/3.
 *
 * @author cwf
 */
public class SlidingAcitivity2 extends AppCompatActivity{
    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_text_img);
        SlidingLayout slidingLayout = new SlidingLayout(this);
        slidingLayout.bindActivity(this);
        textview = (TextView) findViewById(R.id.textview);
        textview.setText("第二页");
    }
}
