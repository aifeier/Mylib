package demo.custom.tabhost;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.cwf.app.cwf.R;

import java.io.File;
import java.util.ArrayList;

import lib.utils.FileUtils;
import lib.utils.SDCardUtils;
import lib.widget.GridAdapter;
import lib.widget.NoScrollGridView;
import lib.widget.ViewHolder;


/**
 * Created by n-240 on 2015/11/5.
 */
public class fragment_1 extends Fragment{

    private View mView;

    private ImageView imageView;

    private ViewPager viewPager;

    private NoScrollGridView noScrollGridView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView(View view){
        imageView = (ImageView) view.findViewById(R.id.imageview);
        Glide.with(this)
                .load(R.drawable.ic_audio_filetype)
                .crossFade()
                .into(imageView);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(1);
        final ArrayList<Integer> lists = new ArrayList<>();
        lists.add(R.drawable.ic_apk_filetype);
        lists.add(R.drawable.ic_audio_filetype);
        lists.add(R.drawable.ic_audiofolder_filetype);
        lists.add(R.drawable.ic_bluetooth_menu);
        lists.add(R.drawable.ic_compress_filetype);
        lists.add(R.drawable.ic_device_menu);
        lists.add(R.drawable.ic_excel_filetype);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return lists.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }


            @Override
            public View instantiateItem(ViewGroup container, int position) {
                PhotoView photoView = new PhotoView(container.getContext());
                photoView.enable();
                Glide.with(container.getContext())
                        .load(lists.get(position))
                        .fitCenter()
                        .into(photoView);
                container.addView(photoView);
                return photoView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        viewPager.setCurrentItem(0);

        ArrayList<File> files = new ArrayList<>();

        noScrollGridView = (NoScrollGridView) view.findViewById(R.id.noScrollgridview);
        noScrollGridView.setAdapter(new GridAdapter<File>(getContext(), R.layout.item_photo,
                new File(SDCardUtils.getAbleDirectoryPath()).listFiles()) {
            @Override
            public void buildView(ViewHolder holder, File data) {
                Glide.with(fragment_1.this.getContext())
                        .load(R.drawable.ic_logo_about)
                        .error(R.drawable.error)
                        .fitCenter()
                        .into((ImageView) holder.findViewById(R.id.item_imageview));
                TextView text = (TextView) holder.findViewById(R.id.item_textview);
                text.setVisibility(View.VISIBLE);
                text.setText(data.getName());

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
                return 12;
            }
        });

    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if(enter)
            return AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        else
            return AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.layout_fragment1, container, false);
        initView(mView);
        return mView;
    }

}
