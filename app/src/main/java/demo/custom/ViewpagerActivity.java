package demo.custom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;
import android.widget.Toolbar;

import com.cwf.app.cwf.R;

import java.util.ArrayList;

import demo.custom.tabhost.fragment_1;
import demo.custom.tabhost.fragment_2;
import demo.custom.tabhost.fragment_3;

/**
 * Created by n-240 on 2015/11/6.
 */
public class ViewpagerActivity extends FragmentActivity{

    private TabHost tabHost;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_fragment);
        initView();
    }

    private void initView(){

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fragments = new ArrayList<Fragment>();
        fragments.add(new fragment_1());
        fragments.add(new fragment_2());
        fragments.add(new fragment_3());
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setCurrentItem(0);
        viewPager.setPageMargin(10);
    }

}
