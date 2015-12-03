package lib.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class GridAdapter<T> extends BaseAdapter {
    private Context mContext;
    private int mItemLayoutId;
    protected List<T> mData = new ArrayList<>();

    public GridAdapter(Context mContext, int mItemLayoutId, T[] mData) {
        super();
        this.mItemLayoutId = mItemLayoutId;
        this.mContext = mContext;
        if(mData!=null && mData.length>0)
        for(T item: mData)
            this.mData.add(item);
        if(this.mData == null && this.mData.size() <= 0)
            this.mData = new ArrayList<>();
    }

    public GridAdapter(Context mContext, int mItemLayoutId, List<T> mData) {
        super();
        this.mItemLayoutId = mItemLayoutId;
        this.mContext = mContext;
        if (mData != null)
            this.mData = mData;
        if(this.mData == null && this.mData.size() <= 0)
            this.mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        if(canAddItem() && mData.size() < getMaxSize() && getMaxSize() != -1)
            return mData.size() + 1;
        else
            return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.instances(mContext, position, convertView, parent,
                mItemLayoutId);
        if(canAddItem() && position == getCount()-1 && mData.size()< getMaxSize()) {
                buildAddView(holder);
        }
        else if(position < getMaxSize()){
            T data = mData.get(position);
            buildView(holder, data);
        }
        return holder.getConvertView();
    }

    public abstract void buildView(ViewHolder holder, T data);

    public abstract  void buildAddView(ViewHolder holder);

    public abstract boolean canAddItem();

    public abstract int getMaxSize();
}
