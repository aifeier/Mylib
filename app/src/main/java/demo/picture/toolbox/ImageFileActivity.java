package demo.picture.toolbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cwf.app.cwf.R;

import demo.picture.toolbox.entiy.ImageCollection;
import lib.BaseActivity;
import lib.utils.ActivityUtils;

/**
 * Created by n-240 on 2015/9/28.
 */
public class ImageFileActivity extends BaseActivity{

    private ListView mList;
    private FolderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_base_list);
        setTitle(getResources().getString(R.string.select_photos));
        mAdapter = new FolderAdapter(this);
        mList = (ListView) findViewById(R.id.base_list);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageCollection item = (ImageCollection) mList.getAdapter().getItem(position);
//                ActivityUtils.showTip(item.getImageList().size() + item.getBucketName() + item.getImageList().get(0).getImagePath(), false);
//                Intent i = ShowFilePhoto.startThisActivity(ImageFileActivity.this, 1, item, -1);
//                startActivityForResult(i, 0);
                Intent i = ShowAlbumActivity.startThisActivity(ImageFileActivity.this, item);
                startActivityForResult(i, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 0){
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}


