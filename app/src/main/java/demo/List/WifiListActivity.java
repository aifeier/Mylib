package demo.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.text.style.TtsSpan;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import lib.widget.autoloadrecyclerview.DividerItemDecoration;
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
                    connectionInfo = wifiManager.getConnectionInfo();
                    list = wifiManager.getScanResults();
                    if (list != null && list.size() > 0)
                        this.setmData(list, autoLoadRecyclerView, false);
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
        autoLoadRecyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST));
        autoLoadRecyclerView.setHasFixedSize(true);
        autoLoadRecyclerView.setCanLoadNextPage(false);
        autoLoadRecyclerView.setAdapter(autoLoadRecyclerAdapter);
        autoLoadRecyclerView.setSwipeRefreshLayout(swipeRefreshLayout);
    }

    private AutoLoadRecyclerAdapter.RecyclerOnClickListener<ScanResult> resultRecyclerOnClickListener
            = new AutoLoadRecyclerAdapter.RecyclerOnClickListener<ScanResult>() {
        @Override
        public void onItemClick(ScanResult ItemData, int position) {
            WifiConfiguration wifiConfiguration = wifiIsSaved(ItemData);
            if(wifiConfiguration!=null){
                connectWifi(wifiConfiguration);
            }
            else {
                EditPassword(ItemData);
            }
        }

        @Override
        public void onItemLongClick(ScanResult ItemData, int position) {
            ActivityUtils.showTip(ItemData.SSID, false);
        }
    };

    private Dialog editPWDDialog;
    private void EditPassword(final ScanResult ItemData){
        editPWDDialog = new Dialog(WifiListActivity.this);
        View view = LayoutInflater.from(WifiListActivity.this).inflate(R.layout.dialog_edit_password, null);
        final EditText passowrod = (EditText) view.findViewById(R.id.dialog_pwd);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        title.setText("连接WIFI: "+ItemData.SSID);
        Button config = (Button) view.findViewById(R.id.dialog_config);
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWifi(passowrod.getText().toString(), ItemData);
                editPWDDialog.dismiss();
            }
        });
        editPWDDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        editPWDDialog.setContentView(view);
        editPWDDialog.setCanceledOnTouchOutside(true);
        editPWDDialog.show();

    }

    /*判断wifi是否已保存*/
    private WifiConfiguration wifiIsSaved(ScanResult scanResult){
        WifiConfiguration configuration = null;
        configurationList = wifiManager.getConfiguredNetworks();
        for(WifiConfiguration item: configurationList){
            if(item.BSSID != null && item.BSSID.equals(scanResult.BSSID)){
                configuration = item;
                break;
            }else if(item.SSID.toString().equals("\""+scanResult.SSID+"\"")){
                configuration = item;
                break;
            }
        }
        return configuration;
    }

    /*连接指定wifi*/
    private void addWifi(String password, ScanResult scanResult){
            WifiConfiguration configuration = createWifiInfo(scanResult.SSID, password, scanResult.capabilities);
            if(configuration!=null) {
                wifiManager.addNetwork(configuration);
                connectWifi(wifiIsSaved(scanResult));
            }else{
                ActivityUtils.showTip("连接wifi失败", false);
            }

    }

    private boolean connectWifi(WifiConfiguration wifiConfiguration){
        if(wifiConfiguration!=null) {
            wifiManager.disconnect();
            wifiManager.enableNetwork(wifiConfiguration.networkId, true);
            boolean success = wifiManager.reconnect();
            return success;
        }else{
            ActivityUtils.showTip("连接wifi失败", false);
            return false;
        }
    }

    private WifiConfiguration createWifiInfo(String SSID, String Password,
                                             String capabilities) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        if (capabilities.toUpperCase().contains("WPA")) {

            // 修改之后配置
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);

        } else
        if (capabilities.toUpperCase().contains("WEP")) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }else{
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if(config.allowedKeyManagement == null)
            return null;
        return config;
    }
}
