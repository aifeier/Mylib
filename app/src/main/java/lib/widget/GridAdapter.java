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
    protected ArrayList<T> mData = new ArrayList<>();

    public GridAdapter(Context mContext, int mItemLayoutId, T[] mData) {
        super();
        this.mItemLayoutId = mItemLayoutId;
        this.mContext = mContext;
        if(mData!=null && mData.length>0)
        for(int i = 0; i< mData.length && i< getMaxSize();i++)
            this.mData.add(mData[i]);
        checkData();
    }

    public GridAdapter(Context mContext, int mItemLayoutId, ArrayList<T> mData) {
        super();
        this.mItemLayoutId = mItemLayoutId;
        this.mContext = mContext;
        if (mData != null)
            this.mData = mData;
        checkData();
    }

    private void checkData(){
        if(mData == null && mData.size() <= 0)
            mData = new ArrayList<>();
    }

    @Override
    public int getCount() {
        if(canAddItem() && mData.size() < getMaxSize() && getMaxSize() != -1)
            return mData.size() + 1;
        else if(getMaxSize() !=-1)
            return mData.size() < getMaxSize() ? mData.size() : getMaxSize();
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
        else if(getMaxSize()==-1 || position < getMaxSize()){
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
