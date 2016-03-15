package demo.baidumap;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MapView;
import com.cwf.app.cwf.R;
import com.handmark.pulltorefresh.library.autoloadlist.AutoLoadAdapter;
import com.handmark.pulltorefresh.library.autoloadlist.AutoRefreshListView;
import com.handmark.pulltorefresh.library.autoloadlist.ViewHolder;

import java.util.ArrayList;

import lib.BaseActivity;
import lib.utils.ActivityUtils;
import lib.utils.TimeUtils;

/**
 * Created by n-240 on 2015/9/25.
 */
public class BaseMapActivity extends BaseActivity{
    private MapView mMapView;

    private AutoRefreshListView<BDLocation> autoRefreshListView;
    private AutoLoadAdapter<BDLocation> autoLoadAdapter;
    private ArrayList<BDLocation> arrayList ;

    private static LocationClient mLocClient;
    private static MyLocationListenner mLocationListenner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pulltorefresh);
        setTitle("百度地图Demo");
//        mMapView = (MapView)findViewById(R.id.mapview);
        arrayList = new ArrayList<BDLocation>();
        autoRefreshListView = (AutoRefreshListView<BDLocation>) findViewById(R.id.pull_refresh_list);
        autoLoadAdapter = new AutoLoadAdapter<BDLocation>(this, android.R.layout.simple_list_item_1) {
            @Override
            public void buildView(ViewHolder holder, BDLocation data) {
                ((TextView) holder.findViewById(android.R.id.text1)).setText(
                        data.getTime()+"\n"
                +data.getLatitude()+", " + data.getLongitude());
            }

            @Override
            public void getPage(int page) {

            }
        };
        autoRefreshListView.setListAdapter(autoLoadAdapter);
        autoRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mLocClient!=null) {
                    mLocClient.requestLocation();
//                    mLocClient.requestNotifyLocation();
//                    mLocClient.requestOfflineLocation();
                }
            }
        });

        mLocClient = new LocationClient(this);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("gcj02");
        option.setScanSpan(1000 * 10);
        option.disableCache(true);
        mLocClient.setLocOption(option);
        option.setPriority(LocationClientOption.GpsFirst);
        mLocClient.setLocOption(option);
        mLocationListenner = new MyLocationListenner();
        mLocClient.registerLocationListener(mLocationListenner);
        mLocClient.start();

    }

    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || location.getLatitude() == 4.9E-324) {
                ActivityUtils.showTip("定位失败", false);
                return;
            }
                location.setTime(TimeUtils.getSimpleDate()+ "\n" +location.getTime());
                arrayList.add(0, location);
                autoLoadAdapter.setmData(arrayList, autoRefreshListView);

        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocClient.unRegisterLocationListener(mLocationListenner);
        mLocClient.stop();
    }
}
