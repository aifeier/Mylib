package lib;

/**
 * Created by n-240 on 2015/9/25.
 * 根据Category来找到activity
 */

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static android.content.Intent.ACTION_MAIN;

public abstract class BaseListActivity extends ListActivity {

    private String Tag_Category = null;

    private final IntentAdapter mAdapter = new IntentAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(mAdapter);
        mAdapter.refresh();
    }

//    protected String getTag_Category(){
//        return Tag_Category;
//    }
//
//    protected void setTag_Category(String tag_category){
//        this.Tag_Category = tag_category;
//    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        startActivity(mAdapter.getItem(position));
    }

    private class IntentAdapter extends BaseAdapter {
        private final List<CharSequence> mNames;
        private final Map<CharSequence, Intent> mIntents;

        IntentAdapter() {
            mNames = new ArrayList<CharSequence>();
            mIntents = new HashMap<CharSequence, Intent>();
        }

        void refresh() {
            mNames.clear();
            mIntents.clear();

            final Intent mainIntent = new Intent(ACTION_MAIN, null);
            /**
             *判断是否是我的activity
             * 不加会显示手机的所以应用
             **/
            if(getTag_Category()!=null)
                 mainIntent.addCategory(getTag_Category());

            PackageManager pm = getPackageManager();
            final List<ResolveInfo> matches = pm.queryIntentActivities(
                    mainIntent, 0);
            for (ResolveInfo match : matches) {
                Intent intent = new Intent();
                intent.setClassName(match.activityInfo.packageName,
                        match.activityInfo.name);
                final CharSequence name = match.loadLabel(pm);
                mNames.add(name);
                mIntents.put(name, intent);
            }

            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mNames.size();
        }

        @Override
        public Intent getItem(int position) {
            return mIntents.get(mNames.get(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = (TextView) convertView;
            if (convertView == null) {
                tv = (TextView) LayoutInflater.from(BaseListActivity.this)
                        .inflate(android.R.layout.simple_list_item_1, parent,
                                false);
            }
            tv.setText(mNames.get(position));
            return tv;
        }
    }

    /**
     * 根据Category获取activity
     * @return
     */
    public abstract String getTag_Category();

}

