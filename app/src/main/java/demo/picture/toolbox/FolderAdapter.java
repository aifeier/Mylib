package demo.picture.toolbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cwf.app.cwf.R;

import java.util.List;

import demo.picture.toolbox.entiy.BitmapCache;
import demo.picture.toolbox.entiy.ImageCollection;
import demo.picture.toolbox.entiy.ImageItem;

/**
 * Created by n-240 on 2015/9/28.
 */
public class FolderAdapter extends BaseAdapter {

    private List<ImageCollection> albumList;
    private List<ImageItem> dataList;
    private Context mContext;
    private Intent mIntent;
    private DisplayMetrics dm;
    BitmapCache cache;
    final String TAG = getClass().getSimpleName();
    public FolderAdapter(Context c) {
        cache = new BitmapCache();
        AlbumHelper helper = AlbumHelper.getHelper();
        helper.init(c.getApplicationContext());
        albumList = helper.getImagesBucketList(false);
        init(c);
    }

    // 初始化
    public void init(Context c) {
        mContext = c;
        mIntent = ((Activity) mContext).getIntent();
        dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
    }



    @Override
    public int getCount() {
        return albumList.size();
    }

    @Override
    public Object getItem(int position) {
//        return position;
        return albumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        // 封面
        public ImageView imageView;
        // 文件夹名称
        public TextView folderName;
    }
    ViewHolder holder = null;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_file_photos_list1, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.file_list_img);
            holder.folderName = (TextView) convertView.findViewById(R.id.file_list_title);
            holder.imageView.setAdjustViewBounds(true);
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        String path;
        if (albumList != null) {

            //封面图片路径
            path = albumList.get(position).getImageList().get(0).getThumbnailPath();
            // 给folderName设置值为文件夹名称
            holder.folderName.setText(albumList.get(position).getBucketName() +
                    "(" + albumList.get(position).getCount()+")");


        } else
            path = "android_hybrid_camera_default";
        if (path.contains("android_hybrid_camera_default"))
            Glide.with(mContext)
                    .load(R.drawable.plugin_camera_no_pictures)
                    .error(R.drawable.error)
//                    .placeholder(R.drawable.loading4)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.imageView);
        else {
            final ImageItem item = albumList.get(position).getImageList().get(0);
            Glide.with(mContext)
                    .load(path)
                    .error(R.drawable.plugin_camera_no_pictures)
//                    .placeholder(R.drawable.loading4)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .into(holder.imageView);
        }

        return convertView;
    }


    public int dipToPx(int dip) {
        return (int) (dip * dm.density + 0.5f);
    }

}

