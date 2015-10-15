package demo.picture.toolbox;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.cwf.app.cwf.R;
import com.cwf.app.photolibrary.utils.photoviewlibs.PhotoView;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import demo.picture.toolbox.entiy.ImageItem;

/**
 * 大图查看
 * Created by n-240 on 2015/9/29.
 */
public class GalleryFragment extends Fragment{

    public static final String KEY_ID = "location_id";
    public static final String KEY_PHOTO_LIST = "myList";
    public static final String KEY_CAN_DELETE = "can_del";

    private static List<ImageItem> mData;
    private ArrayList<View> mView = null;
    private Context mContext;
    private static int position = 0;
    private ViewPager mViewPager;
    private MyAdapter adapter;

    public GalleryFragment(Context context, List<ImageItem> list, int position){
        mData = list;
        mContext = context;
        if(position != -1)
            this.position = position;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_photos_view, null);
        if(mView==null)
            mView = new ArrayList<View>();
        PhotoView img;
        for(ImageItem item : mData){
            img = new PhotoView(mContext);
            img.setId(Integer.parseInt(item.getImageId()));
            img.setImageBitmap(item.getBitmap());
            img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            img.setTag(item.getImageId());
            mView.add(img);
        }
        adapter = new MyAdapter();
        mViewPager = (ViewPager) view.findViewById(R.id.photo_viewpager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position_id) {
                position = position_id;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(adapter);
        mViewPager.setPageMargin(20);
        mViewPager.setCurrentItem(position);
        return view;
    }

    private class MyAdapter extends PagerAdapter {
        private int size;
        public MyAdapter() {
            size = mData!=null?mData.size():0;
        }

        public void setListViews() {
            size = mData!=null?mData.size():0;
        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(mView.get(position % size), 0);
            return mView.get(position % size);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View)object);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public static ImageItem getSelectItem(){
        return mData.get(position);
    }
}
