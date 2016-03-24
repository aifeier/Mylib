package lib.widget.autoloadrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


/**
 * Created by n-240 on 2015/10/29.
 */

/*
* RecyclerView的Item设置
* */
public class ViewHolderRecycler extends RecyclerView.ViewHolder {

    private Context mContext;
    private SparseArray<View> mViews;//存储item中的子view

    public ViewHolderRecycler(View itemView) {
        super(itemView);
        mViews = new SparseArray<View>();
    }


    /**
     * 根据viewId获取item中对应的子view
     * @param viewId  item中子view的Id
     * @return  返回item中的子view
     */
    public View findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;

    }


    /**
     * 给TextView设置值
     * @param viewId  textView的id
     * @param value  要设置的值
     * @return  返回viewholder对象，便于链式编程
     */
    public ViewHolderRecycler setValueToTextView(int viewId, String value) {
        TextView tv = (TextView) findViewById(viewId);
        tv.setText(value);
        return this;
    }

    /**
     * 给TextView设置值
     * @param viewId  textView的id
     * @param value  要设置的值
     * @return  返回viewholder对象，便于链式编程
     */
    public ViewHolderRecycler setValueToTextView(int viewId, Spanned value) {
        TextView tv = (TextView) findViewById(viewId);
        tv.setText(value);
        return this;
    }

    /**
     * 给button设置值
     * @param viewId button的id
     * @param value 要设置的值
     * @return 返回viewholder对象，便于链式编程
     */
    public ViewHolderRecycler setValueToButton(int viewId, String value) {
        Button btn = (Button) findViewById(viewId);
        btn.setText(value);
        return this;
    }

    public ViewHolderRecycler setValuetoProgress(int viewId, int max, int progress){
        ProgressBar progressBar = (ProgressBar) findViewById(viewId);
        if(max>0)
           progressBar.setMax(max);
        if(progress>0)
            progressBar.setProgress(progress);
        return this;
    }

    /*设置URL到ImageView*/
    public ViewHolderRecycler setUrltoImageView(int viewId, String url){
        ImageView img = (ImageView) findViewById(viewId);
        Glide.with(itemView.getContext()).load(url)
                .into(img);
        return this;
    }

    public ViewHolderRecycler setImagesToGridView(int viewId, final ArrayList<String> imgs){
        GridView gridView = (GridView) findViewById(viewId);
        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return imgs.size();
            }

            @Override
            public String getItem(int position) {
                return imgs.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView==null)
                    convertView = new ImageView(parent.getContext());
                Glide.with(parent.getContext())
                        .load(getItem(position))
                        .override(100, 100)
                        .centerCrop()
                        .into((ImageView) convertView);
                return convertView;
            }

        });
        return this;
    }


    public ViewHolderRecycler setViewOnClickListener(int viewId , View.OnClickListener listener){
        findViewById(viewId).setOnClickListener(listener);
        return this;
    }

    public ViewHolderRecycler setViewOnLongClickListener(int viewId , View.OnLongClickListener listener){
        findViewById(viewId).setOnLongClickListener(listener);
        return this;
    }


}
