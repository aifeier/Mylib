package demo.List.RecycleView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cwf.app.cwf.R;
import com.cwf.app.okhttplibrary.OkHttpClientManager;
import com.squareup.okhttp.Request;

import java.util.ArrayList;

import lib.widget.AutoRecyclerAdapter.RecyclerViewUtils;
import demo.intent.entity.News;
import demo.intent.entity.NewsInfo;
import lib.utils.ActivityUtils;
import lib.widget.AutoRecyclerAdapter.AutoAdapter2;
import lib.widget.AutoRecyclerAdapter.ViewHolder2;

/**
 * Created by n-240 on 2015/10/29.
 */
public class RecyclerViewActivity3 extends Activity implements RecyclerViewUtils.RecyclerViewRefreshListener{

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
        infos = new ArrayList<NewsInfo>();
        RVU = new RecyclerViewUtils(RecyclerViewActivity3.this,R.id.recycler_view, R.id.swipe_refresh_layout);
        RVU.setRefreshListener(this);
        RVU.setRefreshState(true);
        fristrefresh();
    }

    private void fristrefresh(){
        page = 1;
        OkHttpClientManager.getAsyn("http://api.huceo.com/meinv/other/?key=e7b0c852050f609d927bc20fe11fde9c&num=20&page=1",
                new OkHttpClientManager.ResultCallback<News>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        RVU.setRefreshState(false);
                        e.printStackTrace();
                        ActivityUtils.showTip("刷新失败！请重试！", false);
                    }

                    @Override
                    public void onResponse(News news) {
                        RVU.setRefreshState(false);
                        infos.clear();
                        infos.addAll(news.getNewslist());
                        if(myAdapter == null){
                            myAdapter = new AutoAdapter2<NewsInfo>(RecyclerViewActivity3.this, R.layout.card_view2, infos) {
                                @Override
                                public void buildView2(ViewHolder2 holder, NewsInfo data) {
                                    holder.setValueToTextView(R.id.description, data.getDescription());
                                    holder.setUrltoImageView(R.id.pic, data.getPicUrl());

                                }
                            };
//                            // 为mRecyclerView设置适配器
                            RVU.setAdapter(myAdapter);
                        }
                        else
                            myAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void loadMore(){
        page++;
        OkHttpClientManager.getAsyn("http://api.huceo.com/meinv/other/?key=e7b0c852050f609d927bc20fe11fde9c&num=20&page="+page,
                new OkHttpClientManager.ResultCallback<News>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        page--;
                        RVU.setRefreshState(false);
                        ActivityUtils.showTip("加载失败！请重试！", false);
                    }

                    @Override
                    public void onResponse(News news) {
                        RVU.setRefreshState(false);
                        infos.addAll(news.getNewslist());
                        myAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void loadingMore() {
        loadMore();
    }

    @Override
    public void refresh() {
        fristrefresh();
    }
}
