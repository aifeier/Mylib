package demo.picture;

import android.os.Bundle;

import lib.BaseListActivity;

/**
 * Created by n-240 on 2015/9/25.
 */
public class PictureDemoList extends BaseListActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public String getTag_Category() {
        return "com.cwf.app.cwf.demo.picture";
    }
}
