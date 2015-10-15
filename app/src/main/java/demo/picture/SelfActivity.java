package demo.picture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.cwf.app.cwf.R;
import com.cwf.app.cwflibrary.utils.FileUtils;

import java.util.ArrayList;

import demo.picture.toolbox.BitmapTemp;
import demo.picture.toolbox.GalleryActivity;
import demo.picture.toolbox.ImageFileActivity;
import demo.picture.toolbox.ShowFilePhoto;
import demo.picture.toolbox.entiy.ImageCollection;
import lib.utils.ActivityUtils;
import demo.picture.toolbox.entiy.ImageItem;
import lib.BaseActivity;
import lib.widget.GridAdapter;
import lib.widget.ViewHolder;

/**
 * Created by n-240 on 2015/9/28.
 */
public class SelfActivity extends BaseActivity{

    private static final int TAKE_CAMERA = 0x000001;
    private static final int TAKE_PHOTOS = 0x000002;
    private static final int VIEW_PHOTOS = 0x000003;

    private GridView noScrollGridView;
    private GridAdapter<ImageItem> mAdapter;
    public static int Max = 5;
    public static Bitmap bitmap;
    private LinearLayout ll_popup;

    private PopupWindow popupWindow;

    private View parentView;

    public static ArrayList<ImageItem> listImageItem = new ArrayList<ImageItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentView = getLayoutInflater().inflate(R.layout.layout_gridview, null);
        setContentView(parentView);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_unfocused);
        init();
    }
    private void init(){
        popupWindow = new PopupWindow(this);

        View view = getLayoutInflater().inflate(R.layout.item_popupwindows,null);

        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(view);

        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                popupWindow.dismiss();
                ll_popup.clearAnimation();
            }
        });

        Button photos = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SelfActivity.this, ImageFileActivity.class);
                startActivity(i);
                popupWindow.dismiss();
                ll_popup.clearAnimation();
            }
        });

        Button camera = (Button) view.findViewById(R.id.item_popupwindows_camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera();
                popupWindow.dismiss();
                ll_popup.clearAnimation();
            }
        });

        Button cancel = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                ll_popup.clearAnimation();
            }
        });

        noScrollGridView = (GridView)findViewById(R.id.noScrollgridview);
        noScrollGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mAdapter = initAdapter();
        noScrollGridView.setAdapter(mAdapter);
        noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position == BitmapTemp.tempSelectBitmap.size()) {
                    ll_popup.startAnimation(AnimationUtils.loadAnimation(SelfActivity.this,
                            R.anim.abc_fade_in));
                    popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else {
//                    ImageCollection IC = new ImageCollection();
//                    IC.setImageList(BitmapTemp.tempSelectBitmap);
//                    Intent i = ShowFilePhoto.startThisActivity(SelfActivity.this, 0, IC, position);
//                    startActivity(i);
                    Intent i = new Intent(SelfActivity.this, GalleryActivity.class);
                    startActivityForResult(i, 1);

                }
            }
        });

    }


    /*
    * 打开相机
    * */
    public void camera() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_CAMERA);
    }


    /*
    * activity回调
    * */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_CAMERA:
                if (canAddPhotos() && resultCode == RESULT_OK) {

                    String fileName = String.valueOf(System.currentTimeMillis());
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    FileUtils.saveBitmap(bm, fileName);

                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setBitmap(bm);
                    takePhoto.setImageId(BitmapTemp.tempSelectBitmap.size() + "");
                    BitmapTemp.tempSelectBitmap.add(takePhoto);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case TAKE_PHOTOS:
                break;
            case VIEW_PHOTOS:
                break;
        }
    }


    /**
     * item 适配
     * @return
     */
    private GridAdapter<ImageItem> initAdapter() {
        GridAdapter<ImageItem> mAdapter = new GridAdapter<ImageItem>(this, R.layout.item_grid_photos,
                BitmapTemp.tempSelectBitmap) {


            @Override
            public void buildView(ViewHolder holder, ImageItem data) {
                holder.setImageBitmapToImageView(R.id.item_grid_image, data.getBitmap());
            }

            @Override
            public void buildAddView(ViewHolder holder) {
                holder.setImageBitmapToImageView(R.id.item_grid_image, bitmap);
            }


            @Override
            public boolean canAddItem() {
                return true;
            }

            @Override
            public int getMaxSize() {
                return 5;
            }

        };
        return mAdapter;
    }

    public static boolean canAddPhotos(){
        if(Max==-1 || BitmapTemp.tempSelectBitmap.size() < Max)
            return true;
        else {
            ActivityUtils.showTip("最多添加" + Max +"张照片",false);
            return false;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_id_submit).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_id_submit){
            ll_popup.startAnimation(AnimationUtils.loadAnimation(SelfActivity.this,
                    R.anim.fade_in));
            popupWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        }
    return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        mAdapter.notifyDataSetChanged();
//        BitmapTemp.tempSelectBitmap.clear();
        super.onResume();
    }
}
