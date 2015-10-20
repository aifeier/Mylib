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
//        for(ImageBucket item: albumList){
//            dataList.addAll(item.imageList);
//        }
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

    BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals((String) imageView.getTag())) {
                    ((ImageView) imageView).setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "callback, bmp not match");
                }
            } else {
                Log.e(TAG, "callback, bmp null");
            }
        }
    };

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
        if (AlbumHelper.getHelper().getImagesBucketList(false) != null) {

            //path = photoAbsolutePathList.get(position);
            //封面图片路径
            path = albumList.get(position).getImageList().get(0).imagePath;
            // 给folderName设置值为文件夹名称
            holder.folderName.setText(albumList.get(position).getBucketName() +
                    "(" + albumList.get(position).getCount()+")");


        } else
            path = "android_hybrid_camera_default";
        if (path.contains("android_hybrid_camera_default"))
            Glide.with(mContext)
                    .load(R.drawable.plugin_camera_no_pictures)
                    .error(R.drawable.error)
                    .placeholder(R.drawable.loading4)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.imageView);
        else {
            final ImageItem item = albumList.get(position).getImageList().get(0);
            Glide.with(mContext)
                    .load(item.getThumbnailPath())
                    .error(R.drawable.error)
                    .placeholder(R.drawable.loading4)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .centerCrop()
                    .into(holder.imageView);
//            holder.imageView.setTag(item.imagePath);
//            cache.displayBmp(holder.imageView, item.thumbnailPath, item.imagePath,
//                    callback);
        }
//        // 为封面添加监听
//        holder.imageView.setOnClickListener(new ImageViewClickListener(
//                position, mIntent,holder.choose_back));

        return convertView;
    }

    // 为每一个文件夹构建的监听器
    private class ImageViewClickListener implements OnClickListener {
        private int position;
        private Intent intent;
        private ImageView choose_back;
        public ImageViewClickListener(int position, Intent intent,ImageView choose_back) {
            this.position = position;
            this.intent = intent;
            this.choose_back = choose_back;
        }

        public void onClick(View v) {
//            ShowAllPhoto.dataList = (ArrayList<ImageItem>) AlbumActivity.contentList.get(position).imageList;
//            Intent intent = new Intent();
//            String folderName = AlbumActivity.contentList.get(position).bucketName;
//            intent.putExtra("folderName", folderName);
//            intent.setClass(mContext, ShowAllPhoto.class);
//            mContext.startActivity(intent);
//            choose_back.setVisibility(v.VISIBLE);
        }
    }

    public int dipToPx(int dip) {
        return (int) (dip * dm.density + 0.5f);
    }

}

