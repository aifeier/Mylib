package demo.baidumap;

import android.os.Bundle;

import com.baidu.mapapi.map.MapView;
import com.cwf.app.cwf.R;

import lib.BaseActivity;

/**
 * Created by n-240 on 2015/9/25.
 */
public class BaseMapActivity extends BaseActivity{
    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_base);
        setTitle("百度地图Demo");
        mMapView = (MapView)findViewById(R.id.mapview);
    }
}
