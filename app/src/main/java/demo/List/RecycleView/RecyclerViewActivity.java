package demo.List.RecycleView;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.cwf.app.cwf.R;

import java.util.ArrayList;
import java.util.List;

import demo.List.RecycleView.tool.Actor;
import demo.List.RecycleView.tool.RecyclerAdapter;
import lib.BaseActivity;

/**
 * Created by n-240 on 2015/9/30.
 */
public class RecyclerViewActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener
{
    private RecyclerView mRecyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;

private RecyclerAdapter myAdapter;

private List<Actor> actors = new ArrayList<Actor>();

private String[] names = { "朱茵", "张柏芝", "张敏", "巩俐", "黄圣依" , "朱茵", "张柏芝", "张敏", "巩俐", "黄圣依"};

private String[] pics = { "icon_addpic_focused", "penguins", "icon_addpic_focused", "penguins", "icon_addpic_focused",
        "icon_addpic_focused", "penguins", "icon_addpic_focused", "penguins", "icon_addpic_focused"};

    private  int lastVisibleItem;
    private LinearLayoutManager mLayoutManager;
    private boolean hasMore = true;
        @Override
        protected void onCreate( Bundle savedInstanceState )
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.recycler_view);
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//            swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.red, null),
//                    getResources().getColor(R.color.gray,null),
//                    getResources().getColor(R.color.blue,null),
//                    getResources().getColor( R.color.red,null));
            swipeRefreshLayout.setOnRefreshListener(this);

            //设置背景
            swipeRefreshLayout.setProgressBackgroundColorSchemeColor(0xFF000066);
            //设置箭头颜色
            swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_light, R.color.holo_red_light,R.color.purple);

            // 这句话是为了，第一次进入页面的时候显示加载进度条
            swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                            .getDisplayMetrics()));
            // 拿到RecyclerView
            mRecyclerView=(RecyclerView)

                    findViewById(R.id.recycler_view);
            mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView,
                                                 int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE
                            && lastVisibleItem + 1 == myAdapter.getItemCount()&&hasMore) {
                        swipeRefreshLayout.setRefreshing(true);
                        // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
//                        handler.sendEmptyMessageDelayed(0, 3000);
                        if (myAdapter.getItemCount() != names.length) {
                            actors.add(new Actor(names[myAdapter.getItemCount()], pics[myAdapter.getItemCount()]));
//                            mRecyclerView.scrollToPosition(myAdapter.getItemCount() - 1);
                            myAdapter.notifyDataSetChanged();
                        }
                        swipeRefreshLayout.setRefreshing(false);
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


            actors.add(new

                            Actor("朱茵", "icon_addpic_focused")

            );

                getActionBar()

                .

                setTitle("那些年我们追的星女郎");

//                // 拿到RecyclerView
//                mRecyclerView=(RecyclerView)
//
//                findViewById(R.id.recycler_view);
                // 设置LinearLayoutManager

            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
//            mRecyclerView.setLayoutManager(new
//
//                LinearLayoutManager(this)
//
//                );
                // 设置ItemAnimator
                mRecyclerView.setItemAnimator(new

                DefaultItemAnimator()

                );
                // 设置固定大小
                mRecyclerView.setHasFixedSize(true);
                // 初始化自定义的适配器
                myAdapter=new RecyclerAdapter(this,actors);
                // 为mRecyclerView设置适配器
                mRecyclerView.setAdapter(myAdapter);

            }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_add_del, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch(item.getItemId()) {
                // 当点击actionbar上的添加按钮时，向adapter中添加一个新数据并通知刷新
                case R.id.menu_id_add:
                    if (myAdapter.getItemCount() != names.length) {
                        actors.add(new Actor(names[myAdapter.getItemCount()], pics[myAdapter.getItemCount()]));
                        mRecyclerView.scrollToPosition(myAdapter.getItemCount() - 1);
                        myAdapter.notifyDataSetChanged();
                    }
                    return true;
                // 当点击actionbar上的删除按钮时，向adapter中移除最后一个数据并通知刷新
                case R.id.menu_id_del:
                    if (myAdapter.getItemCount() != 0) {
                        actors.remove(myAdapter.getItemCount()-1);
                        mRecyclerView.scrollToPosition(myAdapter.getItemCount() - 1);
                        myAdapter.notifyDataSetChanged();
                    }
                    return true;
            }
            return super.onOptionsItemSelected(item);
        }

    @Override
    public void onRefresh() {

        actors.clear();
        myAdapter.notifyDataSetChanged();
        actors.add(new
                        Actor("朱茵", "icon_addpic_focused")
        );
        actors.add(new
                        Actor("456茵", "icon_addpic_focused")
        );
        actors.add(new
                        Actor("123", "icon_addpic_focused")
        );
        actors.add(new
                        Actor("3546", "icon_addpic_focused")
        );
//            if (myAdapter.getItemCount() != 0) {
//                actors.remove(myAdapter.getItemCount()-1);
////                mRecyclerView.scrollToPosition(myAdapter.getItemCount() - 1);
//                myAdapter.notifyDataSetChanged();
//            }
        swipeRefreshLayout.setRefreshing(false);

    }
}
