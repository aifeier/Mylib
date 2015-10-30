package lib.widget.autoloadrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.autoloadlist.AutoRefreshListView;

import java.util.ArrayList;

import lib.widget.AutoRecyclerAdapter.ViewHolder2;

/**
 * Created by n-240 on 2015/10/30.
 */
 /*
* RecyclerView的适配器
* */
public abstract class AutoLoadRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolderRecycler>{

    private Context mContext;
    private int mItemLayoutId;
    protected ArrayList<T> mData;
    private int mCurrentPage ;
    private boolean isFrist = true;
    private int allPage = 10000;

    public AutoLoadRecyclerAdapter(Context mContext, int mItemLayoutId){
        super();
        this.mItemLayoutId = mItemLayoutId;
        this.mContext = mContext;
        mData = new ArrayList<T>();
    }

    @Override
    public ViewHolderRecycler onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(mItemLayoutId, parent, false);
        return new ViewHolderRecycler(v);
    }

    @Override
    public void onBindViewHolder(ViewHolderRecycler holder, int position) {
        buildView(holder, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void refreshAndClearData(){
        mCurrentPage = 0;
        mData.clear();
        loadNextPage();
    }

    public void loadNextPage(){
        if(isFrist || mCurrentPage < allPage) {
            mCurrentPage++;
            isFrist = false;
            getPage(mCurrentPage);
        }
    }

    public void setmData(ArrayList<T> datalist, AutoLoadRecyclerView autoLoadRecyclerView){
        if(autoLoadRecyclerView !=null)
            autoLoadRecyclerView.setRefreshFinish();
        if(datalist.size()>0) {
            if(mCurrentPage == 1){
                mData.clear();
            }
            mData.addAll(datalist);
            notifyDataSetChanged();
        }else
            Toast.makeText(mContext, "加载失败！请重试！", Toast.LENGTH_SHORT).show();
    }

    public abstract void buildView(ViewHolderRecycler holder, T data);

    public abstract  void getPage(int page);

}
