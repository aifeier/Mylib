package demo.List.RecycleView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.cwf.app.cwf.R;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import demo.List.RecycleView.tool.NetWorkRequest;
import demo.intent.entity.NewsInfo;
import lib.widget.autoloadrecyclerview.AutoLoadRecyclerAdapter;
import lib.widget.autoloadrecyclerview.AutoLoadRecyclerView;
import lib.widget.autoloadrecyclerview.ViewHolderRecycler;

/**
 * Created by n-240 on 2015/10/30.
 */
public class AutoLoadRecyclerActivity extends Activity {

    private AutoLoadRecyclerView<NewsInfo> autoLoadRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerAdapter<NewsInfo> autoLoadRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoloadrecycler);
        EventBus.getDefault().register(this);
        autoLoadRecyclerView = (AutoLoadRecyclerView<NewsInfo>) findViewById(R.id.autoloadrecyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        autoLoadRecyclerAdapter = new AutoLoadRecyclerAdapter<NewsInfo>(AutoLoadRecyclerActivity.this, R.layout.card_view2) {
            @Override
            public void buildView(ViewHolderRecycler holder, NewsInfo data) {
                holder.setValueToTextView(R.id.description, data.getDescription());
                holder.setUrltoImageView(R.id.pic, data.getPicUrl());
            }

            @Override
            public void getPage(int page) {
                NetWorkRequest.getPage(page);
            }
        };
        autoLoadRecyclerView.setAdapter(autoLoadRecyclerAdapter);
        autoLoadRecyclerView.setSwipeRefreshLayout(swipeRefreshLayout);
    }

    @Subscribe
    public void onEventBackground(ArrayList<NewsInfo> list) {
        autoLoadRecyclerAdapter.setmData(list, autoLoadRecyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
