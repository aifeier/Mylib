package demo.List.PullToRefresh;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cwf.app.cwf.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.autoloadlist.AutoLoadAdapter;
import com.handmark.pulltorefresh.library.autoloadlist.ViewHolder;

import java.util.ArrayList;
import java.util.LinkedList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import demo.List.RecycleView.tool.NetWorkRequest;
import demo.intent.entity.NewsInfo;
import lib.utils.ActivityUtils;

/**
 * Created by n-240 on 2015/10/29.
 */

public class PullToRefreshListActivity extends Activity{

    private PullToRefreshListView pullToRefreshListView;
    private LinkedList<String> mListItems;

    private ArrayList<NewsInfo> mData;
    private AutoLoadAdapter<NewsInfo> mAdapter;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pulltorefresh);
        EventBus.getDefault().register(this);
//        page = 1;
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
//        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
//                mAdapter.resetAdapterAndRefresh();
//            }
//        });
//        pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
//            @Override
//            public void onLastItemVisible() {
//                page++;
//                mAdapter.loadNextPage();
//            }
//        });
        mData = new ArrayList<NewsInfo>();
        mAdapter = new AutoLoadAdapter<NewsInfo>(getApplicationContext(), R.layout.card_view2, mData) {
            @Override
            public void buildView(ViewHolder holder, NewsInfo data) {
                holder.setValueToTextView(R.id.description, data.getDescription());
//                holder.setImageViewByGlide(R.id.pic, data.getPicUrl());
            }

            @Override
            public void getPage(int page) {
                NetWorkRequest.getPage(page);
            }
        };
        pullToRefreshListView.setListAdapter(mAdapter);

    }


    @Subscribe
    public void onEventBackground(ArrayList<NewsInfo> list) {
        pullToRefreshListView.onRefreshComplete();
        if(list.size()>0) {
            if(page == 1){
                mData.clear();
            }
            mData.addAll(list);
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
