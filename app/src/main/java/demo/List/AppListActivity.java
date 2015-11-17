package demo.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.cwf.app.cwf.R;
import com.handmark.pulltorefresh.library.autoloadlist.AutoLoadAdapter;
import com.handmark.pulltorefresh.library.autoloadlist.AutoRefreshListView;
import com.handmark.pulltorefresh.library.autoloadlist.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import demo.intent.entity.NewsInfo;

import static com.cwf.app.cwf.R.color.black;

/**
 * Created by n-240 on 2015/11/17.
 */
public class AppListActivity extends Activity {

    private AutoRefreshListView autoRefreshListView;
    private AutoLoadAdapter<AppInfo> mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pulltorefresh);
        autoRefreshListView = (AutoRefreshListView) findViewById(R.id.pull_refresh_list);
        mAdapter = new AutoLoadAdapter<AppInfo>(getApplicationContext(), android.R.layout.simple_list_item_2) {

            @Override
            public void buildView(ViewHolder holder, AppInfo data) {
                //noinspection ResourceType
                holder.getConvertView().setBackgroundColor(R.color.black);
                holder.setValueToTextView(android.R.id.text1, data.appName + "|" + data.packageName);
                holder.setValueToTextView(android.R.id.text2, data.versionName+"|"+data.versionCode);
            }

            @Override
            public void getPage(int page) {

            }
        };
        autoRefreshListView.setListAdapter(mAdapter);
        ArrayList<AppInfo> appList = new ArrayList<AppInfo>(); //用来存储获取的应用信息数据
        List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);

        for(int i=0;i<packages.size();i++) {
            PackageInfo packageInfo = packages.get(i);
            AppInfo tmpInfo =new AppInfo();
            tmpInfo.appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
            tmpInfo.packageName = packageInfo.packageName;
            tmpInfo.versionName = packageInfo.versionName;
            tmpInfo.versionCode = packageInfo.versionCode;
            tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
            if((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0) {
                //非系统应用
                appList.add(tmpInfo);
            }
            else {
                //系统应用　　　　　　　　
            }
//            appList.add(tmpInfo);
        }
        mAdapter.setmData(appList, autoRefreshListView);
        autoRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                uninstall(mAdapter.getItem(--position).packageName);
            }
        });
    }

    public void install(View v){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);

        File file = new File(Environment.getExternalStorageDirectory(),"HtmlUI1.apk");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

        startActivity(intent);
    }

    public void uninstall(String url){

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:"+url));
        startActivity(intent);
    }
}
