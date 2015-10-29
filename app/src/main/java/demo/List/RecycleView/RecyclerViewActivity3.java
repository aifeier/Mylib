package demo.List.RecycleView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.cwf.app.cwf.R;
import com.cwf.app.okhttplibrary.OkHttpClientManager;
import com.squareup.okhttp.Request;

import demo.List.RecycleView.tool.RecyclerAdapter;
import demo.List.RecycleView.tool.RecyclerAdapter2;
import demo.intent.entity.News;
import demo.intent.entity.NewsInfo;
import lib.utils.ActivityUtils;
import lib.widget.AutoAdapter;
import lib.widget.AutoAdapter2;
import lib.widget.ViewHolder2;

/**
 * Created by n-240 on 2015/10/29.
 */
public class RecyclerViewActivity3 extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;

    private  int lastVisibleItem;

    private AutoAdapter2<NewsInfo> myAdapter;

    private News mData;

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
/*        //设置背景
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(0xFF000066);
        //设置箭头颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_light, R.color.holo_red_light, R.color.purple);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));*/
        mRecyclerView=(RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == myAdapter.getItemCount()) {
                    swipeRefreshLayout.setRefreshing(true);
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                swipeRefreshLayout.setEnabled(mLayoutManager
                        .findFirstCompletelyVisibleItemPosition() == 0);
            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
/*        // 设置ItemAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置固定大小
        mRecyclerView.setHasFixedSize(true);*/

        swipeRefreshLayout.setRefreshing(true);
        OkHttpClientManager.getAsyn("http://api.huceo.com/meinv/other/?key=e7b0c852050f609d927bc20fe11fde9c&num=10&page=1",
                new OkHttpClientManager.ResultCallback<News>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                        ActivityUtils.showTip("刷新失败！请重试！", false);
                    }

                    @Override
                    public void onResponse(News news) {
                        swipeRefreshLayout.setRefreshing(false);
                        mData = news;
//                        myAdapter = new RecyclerAdapter2(RecyclerViewActivity3.this, mData.getNewslist());
                        myAdapter = new AutoAdapter2<NewsInfo>(RecyclerViewActivity3.this, R.layout.card_view2, mData.getNewslist()) {
                            @Override
                            public void buildView2(ViewHolder2 holder, NewsInfo data) {
                                holder.setValueToTextView(R.id.description, data.getDescription());
                                holder.setUrltoImageView(R.id.pic, data.getPicUrl());

                            }
                        };
//                            // 为mRecyclerView设置适配器
                        mRecyclerView.setAdapter(myAdapter);
                    }
                });
    }

    @Override
    public void onRefresh() {
        page = 1;
        OkHttpClientManager.getAsyn("http://api.huceo.com/meinv/other/?key=e7b0c852050f609d927bc20fe11fde9c&num=10&page=1",
                new OkHttpClientManager.ResultCallback<News>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                        ActivityUtils.showTip("刷新失败！请重试！", false);
                    }

                    @Override
                    public void onResponse(News news) {
                        swipeRefreshLayout.setRefreshing(false);
                        mData = null;
                        mData = news;
                        myAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void loadMore(){
        page++;
        OkHttpClientManager.getAsyn("http://api.huceo.com/meinv/other/?key=e7b0c852050f609d927bc20fe11fde9c&num=10&page="+page,
                new OkHttpClientManager.ResultCallback<News>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        swipeRefreshLayout.setRefreshing(false);
                        ActivityUtils.showTip("加载失败！请重试！", false);
                    }

                    @Override
                    public void onResponse(News news) {
                        swipeRefreshLayout.setRefreshing(false);
                        mData.getNewslist().addAll(news.getNewslist());
                        myAdapter.notifyDataSetChanged();
                    }
                });
    }
}
