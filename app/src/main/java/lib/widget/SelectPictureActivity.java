package lib.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cwf.app.cwf.R;

import java.util.ArrayList;
import java.util.List;

import lib.BaseActivity;
import lib.utils.ActivityUtils;

/**
 * Created by chenw on 2016/1/17.
 *
 * @author cwf
 */
/*选择本地图片*/
public class SelectPictureActivity extends BaseActivity implements LoaderCallbacks<Cursor> {
    public static final String KEY_SELECTED_ONE = "select_one";
    private Boolean isSelectedOne = false;
    private GridView gridView;
    private List<PhotoItem> list;
    private BaseAdapter gridAdapter;
    private List<PhotoItem> selectedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_select_picture);
        isSelectedOne = getIntent().getBooleanExtra(KEY_SELECTED_ONE , true);
        gridView = (GridView) findViewById(R.id.ac_select_picture_gridview);
        getSupportLoaderManager().initLoader(0, null, this);
        selectedList = new ArrayList<>();
    }

    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media._ID,
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // 为了查看信息，需要用到CursorLoader。
        CursorLoader cursorLoader = new CursorLoader(
                this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                STORE_IMAGES,
                null,
                null,
                null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        list = new ArrayList<>();
        int column_index = data.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        data.moveToLast();
        if(data.getCount() > 0) {
            while (!data.isFirst()) {
                PhotoItem item = new PhotoItem();
                item.select = false;
                item.path = data.getString(column_index);
                list.add(item);
                data.moveToPrevious();
            }
            PhotoItem item = new PhotoItem();
            item.select = false;
            item.path = data.getString(column_index);
            list.add(item);
        }
        initGridView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    class PhotoItem{
        boolean select;
        String path;
    }

    private void initGridView(){
        gridAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public PhotoItem getItem(int position) {
                return list.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, final ViewGroup parent) {
                if(convertView == null){
                    convertView = LayoutInflater.from(SelectPictureActivity.this).inflate(R.layout.select_picture_item, null);
                }
                ImageView img = (ImageView) convertView.findViewById(R.id.select_picture_item_img);
                ImageView select = (ImageView) convertView.findViewById(R.id.select_picture_item_select);
                PhotoItem item = getItem(position);
                if(item.path!=null){
                    Glide.with(SelectPictureActivity.this)
                            .load(item.path)
                            .override(150,150)
                            .into(img);
                }
                if(isSelectedOne)
                    select.setVisibility(View.GONE);
                else {
                    if (item.select) {
                        select.setImageResource(R.mipmap.photo_selected);
                    } else {
                        select.setImageResource(R.mipmap.photo_unselect);
                    }
                    select.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PhotoItem item1 = list.get(position);
                            if (item1.select) {
                                selectedList.remove(item1);
                                PhotoItem photo = new PhotoItem();
                                photo.path = item1.path;
                                photo.select = false;
                                list.set(position, photo);
                                ((ImageView) v).setImageResource(R.mipmap.photo_unselect);
                            } else {
                                PhotoItem photo = new PhotoItem();
                                photo.path = item1.path;
                                photo.select = true;
                                selectedList.add(photo);
                                list.set(position, photo);
                                ((ImageView) v).setImageResource(R.mipmap.photo_selected);
                            }
//                        gridAdapter.notifyDataSetChanged();
                        }
                    });
                }
                return convertView;
            }
        };
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isSelectedOne) {
                    ActivityUtils.showTip(((PhotoItem)gridAdapter.getItem(position)).path, false);
                    Intent intent = new Intent();
                    intent.putExtra("data", ((PhotoItem)gridAdapter.getItem(position)).path);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_submit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_id_submit).setIcon(null);
        menu.findItem(R.id.menu_id_submit).setTitle("完成");
        if(isSelectedOne){
            menu.findItem(R.id.menu_id_submit).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_id_submit) {
            Intent intent = new Intent();
            ArrayList<String> strings = new ArrayList<>();
            for(PhotoItem i : selectedList){
                strings.add(i.path);
            }
            ActivityUtils.showTip(strings.size() + "张", false);
            intent.putStringArrayListExtra("data", strings);
            setResult(RESULT_OK, intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
}
