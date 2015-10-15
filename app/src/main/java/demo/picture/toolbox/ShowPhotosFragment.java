package demo.picture.toolbox;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.cwf.app.cwf.R;

import demo.picture.SelfActivity;
import demo.picture.toolbox.entiy.ImageCollection;
import demo.picture.toolbox.entiy.ImageItem;
import lib.widget.GridAdapter;
import lib.widget.ViewHolder;

/**
 * Created by n-240 on 2015/9/29.
 */
public class ShowPhotosFragment extends android.app.Fragment {

    private ImageCollection mData;
    private GridView mGridView;
    private GridAdapter<ImageItem> mAdapter;
    private Context mContext;

    private ShowPhotosCallBack callBack1;

    public ShowPhotosFragment(Context context, ImageCollection data , ShowPhotosCallBack callBack){
        mData = data;
        mContext = context;
        this.callBack1 = callBack;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_file_photos_list_fragment, null);
        mGridView = (GridView) view.findViewById(R.id.fragment_gridview);
        mAdapter = initAdapter();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(onItemClickListener);
        return view;
    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(callBack1!=null){
//                callBack1.gotoGalleryView(position);
            }
        }
    };

    private ViewHolder.ImageCallback callback = new ViewHolder.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
            imageView.setImageBitmap(bitmap);
        }
    };

    private GridAdapter<ImageItem> initAdapter(){
        GridAdapter<ImageItem> adapter = new GridAdapter<ImageItem>(mContext, R.layout.item_file_photos_list2,mData.getImageList()) {
            @Override
            public void buildView(ViewHolder holder, ImageItem data) {
                holder.setImageBitmapToImageView(R.id.item_grid_image, data.getImagePath(), callback);
                ImageView select = (ImageView) holder.findViewById(R.id.imv_select);

                for(ImageItem i: SelfActivity.listImageItem){
                    if(i.getImagePath()!=null)
                        if(i.getImagePath().equals(data.getImagePath())) {
                            data.setSelected(true);
                            break;
                        }
                }

                for(ImageItem i: BitmapTemp.tempSelectBitmap){
                    if(i.getImagePath()!=null)
                        if(i.getImagePath().equals(data.getImagePath())) {
                            data.setSelected(true);
                            break;
                        }
                }
                select.setImageResource(data.isSelected()? R.drawable.image_select :
                        R.drawable.image_unselect);
                final ImageItem d = data;
                select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(d.isSelected()) {
                            d.setSelected(false);
                            for(ImageItem t: SelfActivity.listImageItem){
                                if(t.getImagePath()!=null)
                                if(t.getImagePath().equals(d.getImagePath())){
                                    SelfActivity.listImageItem.remove(t);
                                    break;
                                }
                            }
                            for(ImageItem t: BitmapTemp.tempSelectBitmap){
                                if(t.getImagePath()!=null)
                                if(t.getImagePath().equals(d.getImagePath())){
                                    BitmapTemp.tempSelectBitmap.remove(d);
                                    break;
                                }
                            }
                            ((ImageView)v).setImageResource(R.drawable.image_unselect);
                        }
                        else {
                            if(SelfActivity.canAddPhotos()) {
                                d.setSelected(true);
                                BitmapTemp.tempSelectBitmap.add(d);
                                ((ImageView)v).setImageResource(R.drawable.image_select);
                            }
                        }
                        ShowFilePhoto.referNum();
                    }
                });
            }

            @Override
            public void buildAddView(ViewHolder holder) {

            }

            @Override
            public boolean canAddItem() {
                return false;
            }

            @Override
            public int getMaxSize() {
                return -1;
            }
        };
        return adapter;

    }



    @Override
    public void onDestroy() {
        mGridView.destroyDrawingCache();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }

    public interface ShowPhotosCallBack{
        public abstract void gotoGalleryView(int position);
    }


}
