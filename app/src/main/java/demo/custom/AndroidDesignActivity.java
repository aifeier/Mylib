package demo.custom;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.cwf.app.cwf.R;

import java.util.ArrayList;

import demo.custom.tabhost.SimpleFragmentPagerAdapter;
import demo.custom.tabhost.fragment_1;
import demo.custom.tabhost.fragment_2;
import demo.custom.tabhost.fragment_3;
import lib.utils.ActivityUtils;


/**
 * Created by n-240 on 2015/11/10.
 */
public class AndroidDesignActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private EditText account, password;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private SimpleFragmentPagerAdapter simpleFragmentPagerAdapter;
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

        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new fragment_1());
        fragments.add(new fragment_2());
        fragments.add(new fragment_3());
        viewPager = (ViewPager) findViewById(R.id.design_viewpager);
        simpleFragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(simpleFragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        tabLayout = (TabLayout) findViewById(R.id.design_tablayout);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


    }
}
