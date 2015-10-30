package lib.widget.autoload;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import lib.widget.ViewHolder;

public abstract class AutoLoadAdapter<T> extends BaseAdapter {
    private Context mContext;
    private int mItemLayoutId;
    protected List<T> mData;

    private int mCurrentPage;

    private boolean mFirstRun = true;

    private boolean executing = false;

    private int mTotalRecords = 10000;

    public AutoLoadAdapter(Context mContext, int mItemLayoutId, List<T> mData) {
        super();
        this.mItemLayoutId = mItemLayoutId;
        this.mContext = mContext;
        if (mData != null)
            this.mData = mData;
        mCurrentPage =  1;
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

    public void resetAdapterAndRefresh() {
        mCurrentPage = 0;
        mFirstRun = true;
        clearDataCache();
        if (executing) {
//            mAutoLoadListView.removeFooterView(mFootView);
//            cancelTask();
        }
        loadNextPage();

    }

    public void loadNextPage(){
        if (!executing) {
            if (mFirstRun || getCount() < mTotalRecords) {
                mCurrentPage++;
                getPage(mCurrentPage);
            }
        }
    }

    public void clearDataCache() {
        mData.clear();
        notifyDataSetChanged();
    }


    public abstract void buildView(ViewHolder holder, T data);

    public abstract void getPage(int page);
}
