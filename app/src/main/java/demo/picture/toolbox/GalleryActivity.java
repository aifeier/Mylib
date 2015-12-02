package demo.picture.toolbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cwf.app.cwf.R;

import java.util.ArrayList;

import demo.picture.toolbox.entiy.ImageItem;
import lib.BaseActivity;

/**
 * Created by n-240 on 2015/9/28.
 */

/*查看大图*/
public class GalleryActivity extends BaseActivity{

    public static final String KEY_ID = "location_id";
    public static final String KEY_PHOTO_LIST = "myList";
    public static final String KEY_CAN_DELETE = "can_del";

    private ViewPager mViewPager;
    private mPagerAdapter adapter;

    private static ArrayList<ImageItem> mList = new ArrayList<ImageItem>();
    private static int location = -1;


    /*初始化参数*/
    public static void startThisActivity(Activity activity, ArrayList<ImageItem> data, int position){
        mList = data;
        if(position>=0)
            location = position;
        activity.startActivity(new Intent(activity, GalleryActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_photos_view);
        ((RelativeLayout)findViewById(R.id.relat)).setVisibility(View.GONE);

        mViewPager = (ViewPager) findViewById(R.id.photo_viewpager);
        mViewPager.setOffscreenPageLimit(1);
//        mViewPager.addOnPageChangeListener(pageChangeListener);
        adapter = new mPagerAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setPageMargin(20);
        mViewPager.setCurrentItem(location);
    }

    private class mPagerAdapter extends PagerAdapter{



        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView mView = new PhotoView(container.getContext());
            mView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            mView.enable();
//            mView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(container.getContext())
                    .load(mList.get(position).getImagePath())
                    .error(R.drawable.error)
                    .placeholder(R.drawable.loading4)
//                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(mView);
            container.addView(mView);
            return mView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
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

}
