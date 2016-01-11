package com.cwf.app.cwf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import demo.List.DemoListActivity;
import demo.anim.AnimDemo;
import demo.baidumap.BaseMapActivity;
import demo.custom.CustomLayoutList;
import demo.intent.VolleyDemoList;
import demo.picture.PictureDemoList;
import demo.qrcode.QRCodeMain;
import demo.systembartint.SamplesListActivity;
import lib.BaseActivity;
import lib.MainApplication;

public class MainActivity extends BaseActivity implements OnItemClickListener{

    private ListView mList;
    private final String[] demo = {"百度地图" , "网络请求使用" , "多媒体", "ListDemo" , "QRCode" ,  "动画", "自定义控件"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mList = (ListView) findViewById(R.id.main_list);
        mList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, demo));
        mList.setOnItemClickListener(this);
        MainApplication.currentActivity = this;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = null;
        switch(position){
            case 0:
                i = new Intent(MainActivity.this, BaseMapActivity.class);
                break;
            case 1:
                i = new Intent(MainActivity.this, VolleyDemoList.class);
                break;
            case 2:
                i = new Intent(MainActivity.this, PictureDemoList.class);
                break;
            case 3:
                i = new Intent(MainActivity.this, DemoListActivity.class);
                break;
            case 4:
                i = new Intent(MainActivity.this, QRCodeMain.class);
                break;
            case 5:
                i = new Intent(MainActivity.this, AnimDemo.class);
                break;
            case 6:
                i = new Intent(MainActivity.this, CustomLayoutList.class);
                break;
            default:
                break;
        }
        if(i != null) {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            startActivity(i);
        }

    }
}
