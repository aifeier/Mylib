package demo.List.PullToRefresh;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cwf.app.cwf.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import demo.List.RecycleView.tool.NetWorkRequest;
import demo.intent.entity.NewsInfo;
import lib.utils.ActivityUtils;
import lib.widget.AutoAdapter;
import lib.widget.AutoRecyclerAdapter.AutoAdapter2;
import lib.widget.AutoRecyclerAdapter.ViewHolder2;
import lib.widget.ViewHolder;

/**
 * Created by n-240 on 2015/10/29.
 */

public class PullToRefreshListActivity extends Activity{

    private PullToRefreshListView pullToRefreshListView;
    private LinkedList<String> mListItems;

    private ArrayList<NewsInfo> mData;
    private AutoAdapter<NewsInfo> mAdapter;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pulltorefresh);
        EventBus.getDefault().register(this);
        page = 1;
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                NetWorkRequest.getPage(page);
            }
        });
        pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                page++;
                NetWorkRequest.getPage(page);
            }
        });
        mData = new ArrayList<NewsInfo>();
        mAdapter = new AutoAdapter<NewsInfo>(getApplicationContext(), R.layout.card_view2, mData) {
            @Override
            public void buildView(ViewHolder holder, NewsInfo data) {
                holder.setValueToTextView(R.id.description, data.getDescription());
                holder.setImageViewByGlide(R.id.pic, data.getPicUrl());
            }
        };

        pullToRefreshListView.getRefreshableView().setAdapter(mAdapter);
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    pullToRefreshListView.setRefreshing(true);
                }
                ActivityUtils.showTip("you click" + position, false);
            }
        });
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                pullToRefreshListView.setRefreshing();
            }
        }, 200);
    }


    @Subscribe
    public void onEventBackground(ArrayList<NewsInfo> list) {
        pullToRefreshListView.onRefreshComplete();
        if(list.size()>0) {
            if(page == 1){
                mData.clear();
            }
            mData.addAll(list);
//            if(mAdapter==null){
//                mAdapter = new AutoAdapter<NewsInfo>(PullToRefreshListActivity.this, R.layout.card_view2, mData) {
//                    @Override
//                    public void buildView(ViewHolder holder, NewsInfo data) {
//                        holder.setValueToTextView(R.id.description, data.getDescription());
//                    }
//                };
//                // 为mRecyclerView设置适配器
//                pullToRefreshListView.getRefreshableView().setAdapter(mAdapter);
//            }else
                mAdapter.notifyDataSetChanged();
        }else
            ActivityUtils.showTip("加载失败！请重试！", false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
