package demo.List.RecycleView.tool;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.cwf.app.cwf.R;

import lib.widget.AutoAdapter2;

/**
 * Created by n-240 on 2015/10/29.
 */

/*
* RecyclerView 管理器*/

    /*
    * PS：需要在activity中先加载一次网络数据，在设置适配器，才能正确显示数据
    * */
public  class RecyclerViewUtils {
    private Activity activity;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;
    private LinearLayoutManager linearLayoutManager;
    private  int lastVisibleItem;


    /*
    * @param activity
    * @Param recyclerViewId recyclerView的资源id
    * @param swipeRefreshLayoutId swipeRefreshLayout的资源id
    * */
    public RecyclerViewUtils(Activity activity, int recyclerViewId, int swipeRefreshLayoutId){
        this.activity = activity;
        this.recyclerView = (RecyclerView)activity.findViewById(recyclerViewId);
        this.swipeRefreshLayout = (SwipeRefreshLayout)activity.findViewById(swipeRefreshLayoutId);
        linearLayoutManager = new LinearLayoutManager(activity);
        init();
    }

    private void init(){
        if(linearLayoutManager != null)
            recyclerView.setLayoutManager(linearLayoutManager);
        // 设置ItemAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置固定大小
        recyclerView.setHasFixedSize(true);

        //设置背景
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(0xFF000066);
        //设置箭头颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_light, R.color.holo_red_light, R.color.purple);

    }



    /*设置适配器*/
    public void setAdapter(AutoAdapter2 adapter){
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, activity.getResources()
                        .getDisplayMetrics()));
        if(adapter != null) {
            recyclerView.setAdapter(adapter);
        }

    }

    /*设置下拉和上拉的动作*/
    public  void setRefreshListener(final RecyclerViewRefreshListener listener){

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    listener.refresh();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == recyclerView.getAdapter().getItemCount()&&!swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(true);
                    listener.loadingMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                swipeRefreshLayout.setEnabled(linearLayoutManager
                        .findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
    }

    /*设置刷新的logo是否显示*/
    public void setRefreshState(boolean isRefresh){
        swipeRefreshLayout.setRefreshing(isRefresh);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    public void setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    public interface  RecyclerViewRefreshListener{
        public  void loadingMore();
        public void refresh();
    }

}
