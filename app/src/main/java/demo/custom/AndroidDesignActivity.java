package demo.custom;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cwf.app.cwf.R;

import demo.custom.tabhost.SimpleFragmentPagerAdapter;
import lib.BaseActivity;
import lib.utils.ActivityUtils;


/**
 * Created by n-240 on 2015/11/10.
 */
public class AndroidDesignActivity extends BaseActivity {

    private Toolbar toolbar;
    private EditText account, password;
    private ViewPager viewPager;
    private TabLayout tabLayout;


    private SimpleFragmentPagerAdapter simpleFragmentPagerAdapter;

    private CollapsingToolbarLayout collapsingToolbarLayout;
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
                if(password.getText().length() <=4)
                    password.setError("密码不符合规则");
                ActivityUtils.showTip("执行登录操作", false);
                return false;
            }
        });

/*        ArrayList<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new fragment_1());
        fragments.add(new fragment_2());
        fragments.add(new fragment_3());*/
        viewPager = (ViewPager) findViewById(R.id.design_viewpager);
        simpleFragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(simpleFragmentPagerAdapter);
        viewPager.setCurrentItem(0);
        tabLayout = (TabLayout) findViewById(R.id.design_tablayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*下方的提示，带一条信息和一个按钮*/
                Snackbar.make(tabLayout, "I'm a Snackbar!", Snackbar.LENGTH_SHORT).setAction("Click me!", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityUtils.showTip("you click it!", false);
                    }
                }).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.addTab(tabLayout.newTab().setText("我是自己加的"), 2);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.design_collapsingtoolbarlayout);
        collapsingToolbarLayout.setTitle("AOA");
        collapsingToolbarLayout.setTitleEnabled(false);
    }
}
