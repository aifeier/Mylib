package lib.widget.autoloadrecyclerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;


/**
 * Created by cwf on 2015/10/30.
 */
/*自定义RecyclerView，可下拉刷新和上拉加载*/
public class AutoLoadRecyclerView<T> extends RecyclerView {

    private SwipeRefreshLayout swipeRefreshLayout;
    private Context context;
    private  int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;
    private Boolean canLoadNextPage = true;//是否可以加载下一页
    private Boolean canRefresh = true;//是否可以手动刷新
    private Boolean loadFirst = true;//是否设置adapter就加载数据

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
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, context.getResources()
                        .getDisplayMetrics()));
        if(getAdapter() == null)
            return ;
        final AutoLoadRecyclerAdapter<T> mAdapter =(AutoLoadRecyclerAdapter<T>) getAdapter();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.refreshAndClearData();
            }
        });

        //设置背景
//        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(0xFF000066);
        //设置颜色
//        swipeRefreshLayout.setColorSchemeResources(R.color.deepskyblue, R.color.gray_color, R.color.purple);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == recyclerView.getAdapter().getItemCount() && !swipeRefreshLayout.isRefreshing()) {
                   if(mAdapter.hasNextPage()&&canLoadNextPage) {
                       swipeRefreshLayout.setRefreshing(true);
                       mAdapter.loadNextPage();
                   }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                swipeRefreshLayout.setEnabled(linearLayoutManager
                        .findFirstCompletelyVisibleItemPosition() == 0 && canRefresh);
            }
        });
        //第一次加载
        if(loadFirst) {
            swipeRefreshLayout.setRefreshing(true);
            mAdapter.refreshAndClearData();
        }
    }

    public void setRefreshFinish(){
        if(swipeRefreshLayout!=null){
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public Boolean getCanLoadNextPage() {
        return canLoadNextPage;
    }

    public void setCanLoadNextPage(Boolean canLoadNextPage) {
        this.canLoadNextPage = canLoadNextPage;
    }

    public Boolean getCanRefresh() {
        return canRefresh;
    }

    public void setCanRefresh(Boolean canRefresh) {
        this.canRefresh = canRefresh;
    }

    public Boolean getLoadFirst() {
        return loadFirst;
    }

    public void setLoadFirst(Boolean loadFirst) {
        this.loadFirst = loadFirst;
    }
}
