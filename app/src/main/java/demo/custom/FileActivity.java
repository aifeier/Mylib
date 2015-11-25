package demo.custom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import lib.BaseActivity;
import lib.utils.FileExploreActivity;

/**
 * Created by n-240 on 2015/11/25.
 */
public class FileActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(FileActivity.this, FileExploreActivity.class);
        startActivity(intent);
        finish();
    }
}
