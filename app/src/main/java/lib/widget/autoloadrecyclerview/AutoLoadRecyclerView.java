package lib.widget.autoloadrecyclerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.cwf.app.cwf.R;

/**
 * Created by n-240 on 2015/10/30.
 */
public class AutoLoadRecyclerView<T> extends RecyclerView{

    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private  int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;

    public AutoLoadRecyclerView(Context context) {
        super(context);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs){
        this.context = context;
        // 设置ItemAnimator
        setItemAnimator(new DefaultItemAnimator());
        // 设置固定大小
        setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(context);
        setLayoutManager(linearLayoutManager);
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public void setSwipeRefreshLayout(final SwipeRefreshLayout swipeRefreshLayout) {
        if(swipeRefreshLayout==null)
            return ;
        this.swipeRefreshLayout = swipeRefreshLayout;
        final AutoLoadRecyclerAdapter<T> mAdapter =(AutoLoadRecyclerAdapter<T>) getAdapter();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.refreshAndClearData();
            }
        });

        //设置背景
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(0xFF000066);
        //设置箭头颜色
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_light, R.color.holo_red_light, R.color.purple);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == recyclerView.getAdapter().getItemCount()&&!swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(true);
                    mAdapter.loadNextPage();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                swipeRefreshLayout.setEnabled(linearLayoutManager
                        .findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
    }

    public void setRefreshFinish(){
        if(swipeRefreshLayout!=null){
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
