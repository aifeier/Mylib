package com.handmark.pulltorefresh.library.autoloadlist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;


public abstract class AutoLoadAdapter<T> extends BaseAdapter {
    private Context mContext;
    private int mItemLayoutId;
    protected ArrayList<T> mData;

    private int mCurrentPage;

    private boolean mFirstRun = true;

    private boolean executing = false;

    private int mTotalRecords = 10000;

    public AutoLoadAdapter(Context mContext, int mItemLayoutId) {
        super();
        this.mItemLayoutId = mItemLayoutId;
        this.mContext = mContext;
        mData = new ArrayList<T>();
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
        loadNextPage();

    }

    public void loadNextPage(){
        if (!executing) {
            if (mFirstRun || getCount() < mTotalRecords) {
                mCurrentPage++;
                getPage(mCurrentPage);
                mFirstRun = false;
            }
        }
    }

    public void setmData(ArrayList<T> datalist, AutoRefreshListView autoRefreshListView){
        if(autoRefreshListView !=null)
            autoRefreshListView.onRefreshComplete();
        if(datalist.size()>0) {
            if(mCurrentPage == 1){
                mData.clear();
            }
            mData.addAll(datalist);
            notifyDataSetChanged();
        }else
            Toast.makeText(mContext , "加载失败！请重试！", Toast.LENGTH_SHORT).show();
    }

    public void clearDataCache() {
        mData.clear();
        notifyDataSetChanged();
    }


    public abstract void buildView(ViewHolder holder, T data);

    public abstract void getPage(int page);


}
