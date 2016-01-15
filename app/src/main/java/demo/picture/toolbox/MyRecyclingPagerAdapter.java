package demo.picture.toolbox;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.finalteam.toolsfinal.adapter.RecyclingPagerAdapter;
import demo.picture.toolbox.entiy.ImageItem;

/**
 * Created by n-240 on 2016/1/11.
 *
 * @author cwf
 */
public class MyRecyclingPagerAdapter extends RecyclingPagerAdapter{

    private Context mContext;
    private ArrayList<ImageItem> list;

    public MyRecyclingPagerAdapter(Context mContext, ArrayList<ImageItem> list){
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        PhotoView mView = new PhotoView(container.getContext());
        mView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mView.enable();
        Glide.with(mContext)
                .load(list.get(position).getImagePath())
                .override(720, 1280)
                .into(mView);
        return mView;
    }

    @Override
    public int getCount() {
        return list.size();
    }


}
