package demo.picture.toolbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.cwf.app.cwf.R;

import java.util.ArrayList;
import java.util.List;

import demo.picture.SelfActivity;
import demo.picture.toolbox.entiy.ImageCollection;
import demo.picture.toolbox.entiy.ImageItem;

/**
 * Created by n-240 on 2015/9/28.
 */
public abstract  class ShowFilePhoto extends Activity implements View.OnClickListener{
    public static final int ACTION_VIEW_SELECT = 0;
    public static final int ACTION_VIEW_ALBUM = 1;

    public static final String KEY_IMAGECOLLECTION = "image_collection";
    private static ImageCollection mData;
    private Context mContext;
    private GridView mGridView;
    private ShowPhotosFragment showPhotosFragment;
    private GalleryFragment galleryFragment;
    public static TextView file_select_num;//确定按钮，显示数量
    private static int key = 1;
    protected boolean isAlbumShow = false;
    private static int position = 0;
    private TextView preview;//预览


    /*
    *key_id = 1:表示从文件列表进入查看图片列表
    * key_id = 0: 表示从主界面查看图片大图
    * @param data:需要显示的数据
    * */
    public static Intent startThisActivity(Activity activity, int key_id, ImageCollection data, int position_id ){
        if(data!=null)
            mData = data;
        if(key!=-1)
            key = key_id;
        if(position_id != -1)
            position = position_id;
        return new Intent(activity, ShowFilePhoto.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_file_photos_list);
        setTitle(getResources().getString(R.string.select_photos));
        file_select_num = (TextView)findViewById(R.id.file_select_num);
        file_select_num.setOnClickListener(this);
        preview = (TextView) findViewById(R.id.preview);
        preview.setOnClickListener(this);
        mContext = this;
        if(key==ACTION_VIEW_ALBUM) {
            showPhotosFragment = new ShowPhotosFragment(this, mData, call);
            getFragmentManager().beginTransaction()
                    .replace(R.id.file_framlayout, showPhotosFragment).commit();
            isAlbumShow = true;
        }
        else
            if(key==ACTION_VIEW_SELECT){
                galleryFragment = new GalleryFragment(getApplicationContext(), mData.getImageList(), position);
                getFragmentManager().beginTransaction()
                        .replace(R.id.file_framlayout, galleryFragment).commit();
                isAlbumShow = false;
            }

    }

    private ShowPhotosFragment.ShowPhotosCallBack call = new ShowPhotosFragment.ShowPhotosCallBack() {
        @Override
        public void gotoGalleryView(int position) {
            galleryFragment = new GalleryFragment(getApplicationContext(), mData.getImageList(), position);
            getFragmentManager().beginTransaction()
                    .replace(R.id.file_framlayout, galleryFragment).commit();
            isAlbumShow = false;
        }
    };

    public static void referNum(){
        int num = SelfActivity.listImageItem.size()+ BitmapTemp.tempSelectBitmap.size();
        file_select_num.setText("确定(" + num + "/" +
                    SelfActivity.Max + ")");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && !isAlbumShow && key == ACTION_VIEW_ALBUM){
            showPhotosFragment = new ShowPhotosFragment(this, mData, call);
            getFragmentManager().beginTransaction()
                    .replace(R.id.file_framlayout, showPhotosFragment).commit();
            isAlbumShow = true;
            preview.setVisibility(View.GONE);
            invalidateOptionsMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(key == ACTION_VIEW_ALBUM)
            menu.findItem(R.id.menu_id_submit).setTitle("取消");
        else
            if(key == ACTION_VIEW_SELECT)
                menu.findItem(R.id.menu_id_submit).setTitle("删除");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_id_submit){
            if(key == ACTION_VIEW_ALBUM) {
                BitmapTemp.tempSelectBitmap.clear();
                setResult(Activity.RESULT_OK);
                finish();
            }
            else if(key == ACTION_VIEW_SELECT){
               delPhoto(galleryFragment.getSelectItem());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void delPhoto(ImageItem item){
        SelfActivity.listImageItem.remove(item);
//        BitmapTemp.tempSelectBitmap.remove(item);
        if(SelfActivity.listImageItem.size() /*+ BitmapTemp.tempSelectBitmap.size()*/<= 0){
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.file_select_num:
                    SelfActivity.listImageItem.addAll(BitmapTemp.tempSelectBitmap);
                    BitmapTemp.tempSelectBitmap.clear();
                    setResult(Activity.RESULT_OK);
                    finish();
                break;
            case R.id.preview:
                List<ImageItem> t= new ArrayList<ImageItem>();
//                t.addAll(BitmapTemp.tempSelectBitmap);
                t.addAll(SelfActivity.listImageItem);
                if(t.size()>0) {
                    galleryFragment = new GalleryFragment(getApplicationContext(), t , position);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.file_framlayout, galleryFragment).commit();
                    isAlbumShow = false;
                    preview.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        referNum();
        super.onResume();
    }
}
