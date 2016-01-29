package demo.List;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.DragEvent;
import android.view.View;

import com.cwf.app.cwf.R;
import com.handmark.pulltorefresh.library.autoloadlist.AutoLoadAdapter;
import com.handmark.pulltorefresh.library.autoloadlist.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lib.utils.ActivityUtils;
import lib.widget.autoloadrecyclerview.AutoLoadRecyclerAdapter;
import lib.widget.autoloadrecyclerview.AutoLoadRecyclerView;
import lib.widget.autoloadrecyclerview.ViewHolderRecycler;

/**
 * Created by n-240 on 2016/1/29.
 *
 * wifi列表
 *
 * @author cwf
 */
public class WifiListActivity extends Activity{
    private SwipeRefreshLayout swipeRefreshLayout;
    private AutoLoadRecyclerView<ScanResult> autoLoadRecyclerView;
    private AutoLoadRecyclerAdapter<ScanResult> autoLoadRecyclerAdapter;

    private List<ScanResult> list;
    private WifiManager wifiManager;
    private List<WifiConfiguration> configurationList;
    private WifiInfo connectionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_list);
//        WifiP2pManager wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        autoLoadRecyclerView = (AutoLoadRecyclerView) findViewById(R.id.autoloadrecyclerview);
        initWifi();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0x001:
                    initWifi();
                    break;
            }
            return false;
        }
    });

    private void initWifi(){
        if(wifiManager.isWifiEnabled()) {
            configurationList = wifiManager.getConfiguredNetworks();
            configurationList.clear();
            connectionInfo = wifiManager.getConnectionInfo();
            initWifiList();
        }else{
            wifiManager.setWifiEnabled(true);
            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0x001);
                }
            };
            timer.schedule(timerTask, 5000);
        }
    }

    private void initWifiList(){
        list = new ArrayList<>();
        autoLoadRecyclerAdapter = new AutoLoadRecyclerAdapter<ScanResult>(this, R.layout.wifi_list_item) {
            @Override
            public void buildView(ViewHolderRecycler holder, ScanResult data) {
                String name = data.SSID;
                if(TextUtils.isEmpty(name))
                    name = "未知";
                holder.setValueToTextView(R.id.wifi_ssid, name);
                holder.setValueToTextView(R.id.wifi_level, data.level + "");
                if(connectionInfo!=null && connectionInfo.getBSSID().equals(data.BSSID)){
                    holder.setValueToTextView(R.id.wifi_secret, "已连接");
                }else{
                    holder.setValueToTextView(R.id.wifi_secret, data.capabilities);
                }
            }

            @Override
            public void getPage(int page) {
                if(wifiManager.isWifiEnabled() && wifiManager.startScan()) {
                    list = wifiManager.getScanResults();
                    if (list != null && list.size() > 0)
                        this.setmData(list, autoLoadRecyclerView);
                    else
                        initWifi();
                }else{
                    initWifi();
                }
            }
        };
        autoLoadRecyclerAdapter.setRecyclerOnClickListener(resultRecyclerOnClickListener);
        // 设置ItemAnimator
        autoLoadRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置固定大小
        autoLoadRecyclerView.setHasFixedSize(true);
        autoLoadRecyclerView.setCanLoadNextPage(false);
        autoLoadRecyclerView.setAdapter(autoLoadRecyclerAdapter);
        autoLoadRecyclerView.setSwipeRefreshLayout(swipeRefreshLayout);
    }

    private AutoLoadRecyclerAdapter.RecyclerOnClickListener<ScanResult> resultRecyclerOnClickListener
            = new AutoLoadRecyclerAdapter.RecyclerOnClickListener<ScanResult>() {
        @Override
        public void onItemClick(ScanResult ItemData) {
            ActivityUtils.showTip(ItemData.SSID + ItemData.frequency, false);
            connenctWifi(ItemData);
        }

        @Override
        public void onItemLongClick(ScanResult ItemData) {
            ActivityUtils.showTip(ItemData.SSID, false);
        }
    };

    private boolean connenctWifi(ScanResult scanResult){
        /*判断该wifi是否在列表中*/
        boolean isSaved = false;
        int position = 0;
        configurationList = wifiManager.getConfiguredNetworks();
        for(position = 0 ;position < configurationList.size(); position++){
            WifiConfiguration item = configurationList.get(position);
            if(item.BSSID != null && item.BSSID.equals(scanResult.BSSID)){
                isSaved = true;
                break;
            }else if(item.SSID.toString().equals("\"tianque1\"")){
                isSaved = true;
                break;
            }
        }
        if(!isSaved) {
            /*添加新的wifi*/
            WifiConfiguration configuration = new WifiConfiguration();
            configuration.allowedAuthAlgorithms.clear();
            configuration.allowedGroupCiphers.clear();
            configuration.allowedKeyManagement.clear();
            configuration.allowedPairwiseCiphers.clear();
            configuration.allowedProtocols.clear();

            configuration.BSSID = scanResult.BSSID;
            configuration.SSID = "\"" + "tianque1" + "\"";
            String capabilities = scanResult.capabilities;
            if (capabilities.contains("WPA-PSK") || capabilities.contains("WPA2-PSK") )
                configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            else if (capabilities.contains("WPA_EAP") || capabilities.contains("WPA2_EAP")){
                configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
            }
            else{
                configuration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            }
            if(capabilities.contains("WEP")){
                configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            }
            if(capabilities.contains("CCMP")){
                configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            }
            if(capabilities.contains("TKIP")){
                configuration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            }
            if(capabilities.contains("WPA"))
                configuration.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            else if(capabilities.contains("RSN"))
                configuration.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            if(configuration.allowedKeyManagement.isEmpty()){
                configuration.wepKeys[0] = "";
            }else{
                configuration.preSharedKey = "\"" + "tianqueshuage" + "\"";
            }
            wifiManager.addNetwork(configuration);
            connenctWifi(scanResult);
        }else{
            boolean success = wifiManager.enableNetwork(configurationList.get(position).networkId, true);
            ActivityUtils.showTip("连接结果：" + success , false);
            return success;
        }
        return false;
    }

    private WifiConfiguration CreateWifiInfo(String SSID, String Password, String capabilities)
    {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        if(capabilities.contains("null"))
        {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if(capabilities.contains("WEP"))
        {
            config.preSharedKey = "\""+Password+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if(capabilities.contains("WPA"))
        {
            config.preSharedKey = "\""+Password+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        else
        {
            return null;
        }
        return config;
    }
}
