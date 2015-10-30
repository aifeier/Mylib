package demo.List;

import android.app.Activity;
import android.os.Bundle;

import com.cwf.app.cwf.R;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import demo.List.RecycleView.tool.NetWorkRequest;
import demo.intent.entity.NewsInfo;
import lib.utils.ActivityUtils;
import lib.widget.ViewHolder;
import lib.widget.autoload.AutoLoadAdapter;
import lib.widget.autoload.AutoLoadListView;

/**
 * Created by n-240 on 2015/10/30.
 */
public class AutoLoadListActivity extends Activity{
    private AutoLoadListView<NewsInfo> autoLoadListView;
    private AutoLoadAdapter<NewsInfo> autoLoadAdapter;
    private ArrayList<NewsInfo> mData;
    private int mpage=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoload);
        EventBus.getDefault().register(this);
        autoLoadListView = (AutoLoadListView) findViewById(R.id.autoLoadListView);
        mData = new ArrayList<NewsInfo>();
        autoLoadAdapter = new AutoLoadAdapter<NewsInfo>(AutoLoadListActivity.this, R.layout.card_view2, mData) {
            @Override
            public void buildView(ViewHolder holder, NewsInfo data) {
                holder.setValueToTextView(R.id.description, data.getDescription());
                holder.setImageViewByGlide(R.id.pic, data.getPicUrl());
            }

            @Override
            public void getPage(int page) {
                mpage = page;
                NetWorkRequest.getPage(page);
            }
        };
        autoLoadListView.setAdapter(autoLoadAdapter);

    }

    @Subscribe
    public void onEventBackground(ArrayList<NewsInfo> list) {
        autoLoadListView.onRefreshFinish();
        if(list.size()>0) {
            if(mpage == 1){
                mData.clear();
            }
            mData.addAll(list);
            autoLoadAdapter.notifyDataSetChanged();
        }else
            ActivityUtils.showTip("加载失败！请重试！", false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
