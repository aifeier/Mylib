package demo.picture.toolbox;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cwf.app.cwf.R;

import java.util.List;

import demo.picture.SelfActivity;
import demo.picture.toolbox.entiy.ImageCollection;
import demo.picture.toolbox.entiy.ImageItem;
import lib.widget.GridAdapter;
import lib.widget.ViewHolder;

/**
 * Created by n-240 on 2015/9/30.
 */
public class ShowAlbumActivity extends Activity implements View.OnClickListener{
    private TextView file_select_num;//确定按钮，显示数量
    private TextView preview;//预览
    private GridView flie_list_gridview;
    private GridAdapter<ImageItem> mAdapter;
    private static ImageCollection mData;

    /*
*key_id = 1:表示从文件列表进入查看图片列表
* key_id = 0: 表示从主界面查看图片大图
* @param data:需要显示的数据
* */
    public static Intent startThisActivity(Activity activity, ImageCollection data ){
        if(data!=null)
            mData = data;
        return new Intent(activity, ShowAlbumActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("选择图片");
        getActionBar().setDisplayShowHomeEnabled(false);
        setTitle(getResources().getString(R.string.select_photos));
        setContentView(R.layout.layout_file_photos_list);
        file_select_num = (TextView) findViewById(R.id.file_select_num);
        preview = (TextView) findViewById(R.id.preview);
        preview.setVisibility(View.VISIBLE);
        file_select_num.setOnClickListener(this);
        preview.setOnClickListener(this);
        mAdapter = initAdapter();
        flie_list_gridview = (GridView) findViewById(R.id.flie_list_gridview);
        flie_list_gridview.setAdapter(mAdapter);
        flie_list_gridview.setOnItemClickListener(onItemClickListener);
        refNum();

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }
    };

    private GridAdapter<ImageItem> initAdapter(){
        GridAdapter<ImageItem> adapter = new GridAdapter<ImageItem>(ShowAlbumActivity.this, R.layout.item_file_photos_list2,mData.getImageList()) {
            @Override
            public void buildView(ViewHolder holder, ImageItem data) {
                holder.setImageBitmapToImageView(R.id.item_grid_image, data.getImagePath(), callback);
                ImageView select = (ImageView) holder.findViewById(R.id.imv_select);

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
                            BitmapTemp.tempSelectBitmap.remove(d);
                            ((ImageView)v).setImageResource(R.drawable.image_unselect);
                        }
                        else {
                            if(SelfActivity.canAddPhotos()) {
                                d.setSelected(true);
                                BitmapTemp.tempSelectBitmap.add(d);
                                ((ImageView)v).setImageResource(R.drawable.image_select);
                            }
                        }
                        refNum();
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

    private ViewHolder.ImageCallback callback = new ViewHolder.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
            imageView.setImageBitmap(bitmap);
        }
    };

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.file_select_num:
                setResult(Activity.RESULT_OK);
                finish();
                break;
            case R.id.preview:
                Intent i = new Intent(ShowAlbumActivity.this, GalleryActivity.class);
                startActivityForResult(i, 0);
                break;

        }
    }

    //更新显示的数据
    private void refNum(){
        file_select_num.setText("确定（"+ BitmapTemp.tempSelectBitmap.size()+"/"+ SelfActivity.Max+"）");
    }
}
