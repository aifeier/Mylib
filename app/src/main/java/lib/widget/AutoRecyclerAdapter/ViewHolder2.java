package lib.widget.AutoRecyclerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cwf.app.cwf.R;

/**
 * Created by n-240 on 2015/10/29.
 */

/*
* RecyclerView的Item设置
* */
public class ViewHolder2 extends RecyclerView.ViewHolder {

    private Context mContext;
    private SparseArray<View> mViews;//存储item中的子view

    public ViewHolder2(View itemView) {
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
    public ViewHolder2 setValueToTextView(int viewId, String value) {
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
    public ViewHolder2 setValueToButton(int viewId, String value) {
        Button btn = (Button) findViewById(viewId);
        btn.setText(value);
        return this;
    }

    /**
     * 给Imageview设置值
     * @param viewId Imageview的id
     * @param url 要设置的t图片地址
     * @return 返回viewholder对象，便于链式编程
     */
    public ViewHolder2 setUrltoImageView(int viewId, String url){
        ImageView img = (ImageView) findViewById(viewId);
        Glide.with(itemView.getContext()).load(url)
                .error(R.drawable.error)
//                .placeholder(R.drawable.loading4)
                .into(img);
        return this;
    }

    public ViewHolder2 setViewOnClickListener(int viewId , View.OnClickListener listener){

        return this;
    }


}
