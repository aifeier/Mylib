package demo.custom;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.Toast;

import com.cwf.app.cwf.R;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import lib.BaseActivity;

/**
 * Created by n-240 on 2016/4/29.
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class BottomNavigationActivity extends BaseActivity {
    BottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_bottom_navigation);
        bottomNavigation = (BottomNavigation) findViewById(R.id.bottom_navigation);
        bottomNavigation.setVisibility(View.VISIBLE);
        bottomNavigation.setOnMenuItemClickListener(new BottomNavigation.OnMenuItemSelectionListener() {
            @Override
            public void onMenuItemSelect(@IdRes int itemId, int position) {

            }

            @Override
            public void onMenuItemReselect(@IdRes int itemId, int position) {

            }
        });
    }

}
