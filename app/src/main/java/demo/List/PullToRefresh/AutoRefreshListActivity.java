package demo.List.PullToRefresh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cwf.app.cwf.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.autoloadlist.AutoLoadAdapter;
import com.handmark.pulltorefresh.library.autoloadlist.AutoRefreshListView;
import com.handmark.pulltorefresh.library.autoloadlist.ViewHolder;

import java.util.ArrayList;
import java.util.LinkedList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import demo.List.RecycleView.RecyclerViewActivity4;
import demo.List.RecycleView.tool.NetWorkRequest;
import demo.intent.entity.NewsInfo;
import lib.utils.ActivityUtils;

/**
 * Created by n-240 on 2015/10/29.
 */

public class AutoRefreshListActivity extends Activity{

    private AutoRefreshListView autoRefreshListView;
    private LinkedList<String> mListItems;

    private ArrayList<NewsInfo> mData;
    private AutoLoadAdapter<NewsInfo> mAdapter;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pulltorefresh);
        EventBus.getDefault().register(this);
        autoRefreshListView = (AutoRefreshListView) findViewById(R.id.pull_refresh_list);
        mData = new ArrayList<NewsInfo>();
        mAdapter = new AutoLoadAdapter<NewsInfo>(getApplicationContext(), R.layout.listitem) {
            @Override
            public void buildView(ViewHolder holder, NewsInfo data) {
                holder.setValueToButton(R.id.item_button, data.getTitle());
                holder.setValueToTextView(R.id.item_text, data.getDescription());
                holder.setUrlToImageView(R.id.item_img, data.getPicUrl(), R.drawable.error, R.drawable.loading2);
                holder.setUrlToImageView(R.id.item_header_img, data.getPicUrl(), R.drawable.error, R.drawable.loading2);
                ArrayList<String> lists = new ArrayList<String>();
                lists.add(data.getPicUrl());
                lists.add(data.getPicUrl());
                lists.add(data.getPicUrl());
                lists.add(data.getPicUrl());
                lists.add(data.getPicUrl());
                holder.setPicturesToGridView(R.id.item_gridview, lists, R.drawable.error, R.drawable.loading4);
            }

            @Override
            public void getPage(int page) {
                NetWorkRequest.getPage(page);
            }
        };
        autoRefreshListView.setListAdapter(mAdapter);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.penguins);
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AutoRefreshListActivity.this, RecyclerViewActivity4.class));
            }
        });
        autoRefreshListView.setVisiableHeader(imageView, 300);

    }


    @Subscribe
    public void onEventBackground(ArrayList<NewsInfo> list) {
        mAdapter.setmData(list, autoRefreshListView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
