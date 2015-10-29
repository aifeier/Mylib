package lib.widget.AutoRecyclerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

/**
 * Created by n-240 on 2015/10/29.*/

/*
* RecyclerView的适配器
* */
public abstract class AutoAdapter2<T> extends RecyclerView.Adapter<ViewHolder2>{

    private Context mContext;
    private int mItemLayoutId;
    protected ArrayList<T> mData;

    public AutoAdapter2(Context mContext, int mItemLayoutId, ArrayList<T> mData){
        super();
        this.mItemLayoutId = mItemLayoutId;
        this.mContext = mContext;
        if (mData != null)
            this.mData = mData;
    }

    @Override
    public ViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
        return new ViewHolder2(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder2 holder, int position) {
        buildView2(holder, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public abstract void buildView2(ViewHolder2 holder, T data);
}
