package demo.List.RecycleView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;

import com.cwf.app.cwf.R;
import com.cwf.app.okhttplibrary.OkHttpClientManager;
import com.squareup.okhttp.Request;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import demo.List.RecycleView.tool.NetWorkRequest;
import demo.List.RecycleView.tool.RecyclerAdapter;
import demo.List.RecycleView.tool.RecyclerAdapter2;
import demo.List.RecycleView.tool.RecyclerViewUtils;
import demo.intent.entity.News;
import demo.intent.entity.NewsInfo;
import demo.intent.mode.eventbus.TestEvent;
import lib.utils.ActivityUtils;
import lib.widget.AutoAdapter;
import lib.widget.AutoAdapter2;
import lib.widget.ViewHolder2;

/**
 * Created by n-240 on 2015/10/29.
 */
public class RecyclerViewActivity4 extends Activity implements RecyclerViewUtils.RecyclerViewRefreshListener{

    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;

    private  int lastVisibleItem;

    private AutoAdapter2<NewsInfo> myAdapter;

    private ArrayList<NewsInfo> infos;

    private int page = 1;
    private RecyclerViewUtils RVU;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        EventBus.getDefault().register(this);
        infos = new ArrayList<NewsInfo>();
        RVU = new RecyclerViewUtils(RecyclerViewActivity4.this,R.id.recycler_view, R.id.swipe_refresh_layout);
        RVU.setRefreshListener(this);
        RVU.setRefreshState(true);
        NetWorkRequest.getPage(1);
    }

    @Subscribe
    public void onEventBackground(ArrayList<NewsInfo> list) {
        RVU.setRefreshState(false);
        if(list.size()>0) {
            if(page == 1){
                infos.clear();
            }
            infos.addAll(list);
            if(myAdapter==null){
                myAdapter = new AutoAdapter2<NewsInfo>(RecyclerViewActivity4.this, R.layout.card_view2, infos) {
                    @Override
                    public void buildView2(ViewHolder2 holder, NewsInfo data) {
                        holder.setValueToTextView(R.id.description, data.getDescription());
                        holder.setUrltoImageView(R.id.pic, data.getPicUrl());

                    }
                };
                            // 为mRecyclerView设置适配器
                RVU.setAdapter(myAdapter);
            }else
                myAdapter.notifyDataSetChanged();
        }else
            ActivityUtils.showTip("加载失败！请重试！", false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void loadingMore() {
        page++;
        NetWorkRequest.getPage(page);
    }
    @Override
    public void refresh() {
        page = 1;
        NetWorkRequest.getPage(page);
    }
}
