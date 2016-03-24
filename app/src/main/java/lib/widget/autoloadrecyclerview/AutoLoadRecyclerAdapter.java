package lib.widget.autoloadrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n-240 on 2015/10/30.
 */
 /*
* RecyclerView的适配器
* */
public abstract class AutoLoadRecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolderRecycler>{

    private final int maxPage = 10000;
    private Context mContext;
    private int mItemLayoutId;
    protected ArrayList<T> mData;
    private int mCurrentPage ;
    private boolean isFrist = true;
    private int allPage = 10000;
    private RecyclerOnClickListener recyclerOnClickListener;

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
    public void onBindViewHolder(final ViewHolderRecycler holder, final int position) {
        buildView(holder, mData.get(position));
        if(recyclerOnClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerOnClickListener.onItemClick(mData.get(holder.getLayoutPosition()), holder.getLayoutPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    recyclerOnClickListener.onItemLongClick(mData.get(holder.getLayoutPosition()), holder.getLayoutPosition());
                    return true;
                }
            });
        }
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


    /*设置数据, 在getNextPage后调用，每获取一次数据调用一次*/
    public void setmData(List<T> datalist, AutoLoadRecyclerView autoLoadRecyclerView, boolean hasNext){
        if(autoLoadRecyclerView !=null)
            autoLoadRecyclerView.setRefreshFinish();
        autoLoadRecyclerView.setCanLoadNextPage(hasNext);
        if(datalist!=null&&datalist.size()>0) {
            if(mCurrentPage == 1){
                mData.clear();
            }
            mData.addAll(datalist);
            notifyDataSetChanged();
            allPage = maxPage;
        }else{
            allPage = mCurrentPage;
        }
//        else
//            Toast.makeText(mContext, "加载失败！请重试！", Toast.LENGTH_SHORT).show();
    }

    /*设置数据, 在getNextPage后调用，每获取一次数据调用一次*/
    public void setmData(ArrayList<T> datalist, AutoLoadRecyclerView autoLoadRecyclerView, boolean hasNext){
        if(autoLoadRecyclerView !=null)
            autoLoadRecyclerView.setRefreshFinish();
        autoLoadRecyclerView.setCanLoadNextPage(hasNext);
        if(datalist!=null&&datalist.size()>0) {
            if(mCurrentPage == 1){
                mData.clear();
            }
            mData.addAll(datalist);
            notifyDataSetChanged();
            allPage = maxPage;
        }else{
            allPage = mCurrentPage;
        }

//        else
//            Toast.makeText(mContext, "加载失败！请重试！", Toast.LENGTH_SHORT).show();
    }

    public boolean hasNextPage(){
        return mCurrentPage < allPage;
    }

    /*设置item view*/
    public abstract void buildView(ViewHolderRecycler holder, T data);

    /*设置获取数据*/
    public abstract  void getPage(int page);

    /*设置整个Item的点击事件*/
    public interface RecyclerOnClickListener<T>{
        public void onItemClick(T ItemData, int positon);
        public void onItemLongClick(T ItemData, int positon);
    }

    public RecyclerOnClickListener getRecyclerOnClickListener() {
        return recyclerOnClickListener;
    }

    public void setRecyclerOnClickListener(RecyclerOnClickListener recyclerOnClickListener) {
        this.recyclerOnClickListener = recyclerOnClickListener;
    }

    public void addData(int position, T itemData){
        mData.add(position, itemData);
        notifyItemInserted(position);
    }

    public void removeData(int position){
        mData.remove(position);
        notifyItemRemoved(position);
    }

}
