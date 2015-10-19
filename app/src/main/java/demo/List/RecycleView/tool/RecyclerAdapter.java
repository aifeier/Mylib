package demo.List.RecycleView.tool;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.DrawFilter;
import android.graphics.drawable.DrawableContainer;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cwf.app.cwf.R;
import com.cwf.app.cwflibrary.utils.GlideUtils;

import java.util.List;

import demo.picture.toolbox.BitmapTemp;

/**
 * Created by n-240 on 2015/9/30.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
{

    private List<Actor> actors;

    private Context mContext;

    public RecyclerAdapter( Context context , List<Actor> actors)
    {
        this.mContext = context;
        this.actors = actors;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i )
    {
        // 给ViewHolder设置布局文件
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int i )
    {
        // 给ViewHolder设置元素
        Actor p = actors.get(i);
        viewHolder.mTextView.setText(p.name);
        GlideUtils.getNetworkImage(mContext, "http://b.hiphotos.baidu.com/image/pic/item/29381f30e924b899d84ce5396c061d950a7bf6bb.jpg", viewHolder.mImageView );
//        viewHolder.mImageView.setImageDrawable(DrawableContainer.createFromPath(mContext.getResources().getString(p.getImageResourceId(mContext))));
//        viewHolder.mImageView.setImageResource(p.getImageResourceId(mContext));
    }

    @Override
    public int getItemCount()
    {
        // 返回数据总数
        return actors == null ? 0 : actors.size();
    }

    // 重写的自定义ViewHolder
    public static class ViewHolder
            extends RecyclerView.ViewHolder
    {
        public TextView mTextView;

        public ImageView mImageView;

        public ViewHolder( View v )
        {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.name);
            mImageView = (ImageView) v.findViewById(R.id.pic);
        }
    }
}
