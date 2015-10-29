package demo.List.RecycleView.tool;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cwf.app.cwf.R;
import com.cwf.app.cwflibrary.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import demo.intent.entity.News;
import demo.intent.entity.NewsInfo;

/**
 * Created by n-240 on 2015/10/29.
 */
public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.ViewHolder>
{

    private ArrayList<NewsInfo> news;

    private Context mContext;

    public RecyclerAdapter2( Context context , ArrayList<NewsInfo> news)
    {
        this.mContext = context;
        if(news !=  null)
            this.news = news;
        else{
            this.news = new ArrayList<NewsInfo>();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsInfo info = news.get(position);
        holder.title.setText(info.getTitle());
        holder.description.setText(info.getDescription());
        holder.time.setText(info.getHottime());
        Glide.with(mContext).load(info.getPicUrl())
                .error(R.drawable.error)
                .placeholder(R.drawable.loading3)
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }


    // 重写的自定义ViewHolder
    public static class ViewHolder
            extends RecyclerView.ViewHolder
    {
        public TextView title;

        public TextView description;

        public ImageView pic;

        public TextView time;


        public ViewHolder( View v )
        {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            description = (TextView) v.findViewById(R.id.description);
            time = (TextView) v.findViewById(R.id.time);
            pic = (ImageView) v.findViewById(R.id.pic);
        }
    }
}
