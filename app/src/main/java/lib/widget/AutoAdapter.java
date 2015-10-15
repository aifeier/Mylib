package lib.widget;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AutoAdapter<T> extends BaseAdapter {
    private Context mContext;
    private int mItemLayoutId;
    protected List<T> mData;

    public AutoAdapter(Context mContext, int mItemLayoutId, List<T> mData) {
        super();
        this.mItemLayoutId = mItemLayoutId;
        this.mContext = mContext;
        if (mData != null)
            this.mData = mData;
    }

    @Override
    public int getCount() {
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
        T data = mData.get(position);
        ViewHolder holder = ViewHolder.instances(mContext, position, convertView, parent,
                mItemLayoutId);
        buildView(holder, data);
        return holder.getConvertView();
    }

    public abstract void buildView(ViewHolder holder, T data);
}
