package demo.custom.tabhost;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.cwf.app.cwf.R;

import demo.custom.tabhost.fragment_1;
import demo.custom.tabhost.fragment_2;
import demo.custom.tabhost.fragment_3;

/**
 * Created by n-240 on 2015/11/5.
 */
public class TabHostActivity extends FragmentActivity {

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_home_btn,
            R.drawable.tab_home_btn1,R.drawable.tab_home_btn2};
    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {fragment_1.class
            ,fragment_2.class,fragment_3.class};
    //Tab选项卡的文字
    private String mTextviewArray[] = {"这里", "那里", "不知道"};

    //定义FragmentTabHost对象
    private FragmentTabHost fragmentTabHost;

    //定义一个布局
    private LayoutInflater layoutInflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homelayout);
        initView();
    }

    @SuppressLint("NewApi")
    private void initView(){
        View actionbar_custom = View.inflate(this, R.layout.actionbar_customview, null);
        getActionBar().setCustomView(actionbar_custom, new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
//        getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.ao2));
        getActionBar().setTitle("tab");
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        fragmentTabHost.getTabWidget().setDividerDrawable(null);
        //得到fragment的个数
        int count = fragmentArray.length;

        for(int i = 0; i < count; i++){
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = fragmentTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            fragmentTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

            }
        });
        fragmentTabHost.setAnimation(new AlphaAnimation(0.0f, 1.0f));
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }

}
