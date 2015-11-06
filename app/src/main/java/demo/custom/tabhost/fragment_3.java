package demo.custom.tabhost;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.cwf.app.cwf.R;
import com.handmark.pulltorefresh.library.autoloadlist.AutoLoadAdapter;
import com.handmark.pulltorefresh.library.autoloadlist.AutoRefreshListView;
import com.handmark.pulltorefresh.library.autoloadlist.ViewHolder;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import demo.List.RecycleView.RecyclerViewActivity4;
import demo.List.RecycleView.tool.NetWorkRequest;
import demo.intent.entity.NewsInfo;
import demo.picture.toolbox.GalleryActivity;
import demo.picture.toolbox.entiy.ImageItem;

/**
 * Created by n-240 on 2015/11/5.
 */
public class fragment_3 extends Fragment{

    private View view;
    private AutoRefreshListView autoRefreshListView;
    private AutoLoadAdapter<NewsInfo> mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if(enter)
            return AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        else
            return AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_pulltorefresh, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        autoRefreshListView = (AutoRefreshListView) view.findViewById(R.id.pull_refresh_list);
        mAdapter = new AutoLoadAdapter<NewsInfo>(getActivity(), R.layout.listitem) {
            @Override
            public void buildView(ViewHolder holder, NewsInfo data) {
                holder.setValueToButton(R.id.item_button, data.getTitle());
                holder.setValueToTextView(R.id.item_text, data.getDescription());
                holder.setUrlToImageView(R.id.item_img, data.getPicUrl(), R.drawable.error, R.drawable.loading2);
                holder.setUrlToImageView(R.id.item_header_img, data.getPicUrl(), R.drawable.error, R.drawable.loading2);
                final ArrayList<String> lists = new ArrayList<String>();
                lists.add(data.getPicUrl());
                lists.add(data.getPicUrl());
                lists.add(data.getPicUrl());
                lists.add(data.getPicUrl());
                lists.add(data.getPicUrl());
                holder.setPicturesToGridView(R.id.item_gridview, lists, R.drawable.error, R.drawable.loading4);
                GridView gridView = (GridView) holder.findViewById(R.id.item_gridview);
                final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
                ImageItem imageItem = new ImageItem();
                imageItem.setImagePath(data.getPicUrl());
                imageItems.add(imageItem);
                imageItems.add(imageItem);
                imageItems.add(imageItem);
                imageItems.add(imageItem);
                imageItems.add(imageItem);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        GalleryActivity.startThisActivity(getActivity(), imageItems, position);
                    }
                });
            }

            @Override
            public void getPage(int page) {
                NetWorkRequest.getPage(page);
            }
        };
        autoRefreshListView.setListAdapter(mAdapter);
        View a = View.inflate(getActivity(), R.layout.layout_text_img, null);
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(R.drawable.penguins);
        imageView.setClickable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), RecyclerViewActivity4.class));
            }
        });
        autoRefreshListView.addVisiableHeader(imageView, 300);
        autoRefreshListView.addVisiableHeader(a, 200);
    }


    @Subscribe
    public void onEventBackground(ArrayList<NewsInfo> list) {
        mAdapter.setmData(list, autoRefreshListView);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
