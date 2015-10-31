package demo.List.PullToRefresh;

import android.app.Activity;
import android.app.FragmentContainer;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.cwf.app.cwf.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.autoloadlist.AutoLoadAdapter;
import com.handmark.pulltorefresh.library.autoloadlist.ViewHolder;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import demo.List.RecycleView.tool.NetWorkRequest;
import demo.intent.entity.NewsInfo;

/**
 * Created by chenw on 2015/10/31.
 */
public class AutoRefreshGirdViewActivity extends Activity{
    PullToRefreshGridView pullToRefreshGridView ;
    AutoLoadAdapter<NewsInfo> autoLoadAdapter;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_gridview);
        EventBus.getDefault().register(this);
        pullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.pull_to_refresh_gridview);
        autoLoadAdapter = new AutoLoadAdapter<NewsInfo>(AutoRefreshGirdViewActivity.this, R.layout.listitem) {
            @Override
            public void buildView(ViewHolder holder, NewsInfo data) {
                holder.setValueToButton(R.id.item_button, data.getTitle());
                holder.setValueToTextView(R.id.item_text, data.getDescription());
                holder.setUrlToImageView(R.id.item_img, data.getPicUrl(), R.drawable.error, R.drawable.loading2);
                holder.setUrlToImageView(R.id.item_header_img, data.getPicUrl(), R.drawable.error, R.drawable.loading2);

            }

            @Override
            public void getPage(int page) {
                NetWorkRequest.getPage(page);
            }
        };
        pullToRefreshGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                autoLoadAdapter.resetAdapterAndRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                autoLoadAdapter.loadNextPage();
            }
        });
        pullToRefreshGridView.getRefreshableView().setAdapter(autoLoadAdapter);
        autoLoadAdapter.resetAdapterAndRefresh();
        setDefaultFragment();
    }
    private void setDefaultFragment(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        TextFragment TF = new TextFragment();

        transaction.replace(R.id.auto_gridview_framelayout, TF);
        transaction.commit();
    }

    @Subscribe
    public void onEventBackground(ArrayList<NewsInfo> list) {
        autoLoadAdapter.setmData(list, pullToRefreshGridView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
