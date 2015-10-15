package demo.picture.toolbox;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cwf.app.cwf.R;
import com.cwf.app.photolibrary.utils.photoviewlibs.PhotoView;

import java.util.ArrayList;
import java.util.HashMap;

import demo.picture.SelfActivity;
import demo.picture.toolbox.entiy.ImageItem;
import lib.BaseActivity;
import lib.utils.ActivityUtils;

/**
 * Created by n-240 on 2015/9/28.
 */
public class GalleryActivity extends BaseActivity{

    public static final String KEY_ID = "location_id";
    public static final String KEY_PHOTO_LIST = "myList";
    public static final String KEY_CAN_DELETE = "can_del";

    private ViewPager mViewPager;
    private my adapter;

    private int location;

    private ArrayList<View> listViews = null;
    private PhotoView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_photos_view);
        if(listViews==null)
            listViews = new ArrayList<View>();

        for(ImageItem item : BitmapTemp.tempSelectBitmap){
            img = new PhotoView(this);
            img.setId(Integer.parseInt(item.getImageId()));
            img.setImageBitmap(item.getBitmap());
            img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            listViews.add(img);
            img.destroyDrawingCache();
        }

        mViewPager = (ViewPager) findViewById(R.id.photo_viewpager);
        mViewPager.addOnPageChangeListener(pageChangeListener);
        adapter = new my(listViews);
        mViewPager.setAdapter(adapter);
        mViewPager.setPageMargin(20);
        int id = getIntent().getIntExtra(KEY_ID, 0);
        mViewPager.setCurrentItem(id);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            location = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private class my extends PagerAdapter{
        private ArrayList<View> listViews;
        private int size;
        public my(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
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
            ((ViewPager) container).addView(listViews.get(position % size), 0);
            return listViews.get(position % size);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(listViews.get(position % size));
        }
    }


    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;

        private int size;
        public MyPageAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
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
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(listViews.get(arg1 % size));
        }


        @Override
        public Object instantiateItem(View arg0, int arg1) {
            try {
                ((ViewPager) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    @Override
    protected void onDestroy() {
        listViews.clear();
        listViews.clone();
        super.onDestroy();
    }

}
