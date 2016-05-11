package demo.custom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cwf.app.cwf.R;

import java.util.List;

import lib.utils.AppUtils;
import lib.utils.entity.ContactsInfo;
import lib.widget.siderbarlist.SideBar;

/**
 * Created by n-240 on 2016/5/11.
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class ContactsActivity extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private SideBar sideBar;
    private List<ContactsInfo> contactsInfos;

    private MyExpandableListAdapter myExpandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        contactsInfos = AppUtils.getLocalContactsInfos(this);
        sideBar = (SideBar) findViewById(R.id.sidebar);
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_listview);
        myExpandableListAdapter = new MyExpandableListAdapter();
        expandableListView.setAdapter(myExpandableListAdapter);
    }

    private class MyExpandableListAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return contactsInfos.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return contactsInfos.size() % groupPosition;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return contactsInfos.get(groupPosition).getName();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return contactsInfos.get(childPosition).getPhone();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition % (contactsInfos.size() / 4);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView textView = new TextView(parent.getContext());
            textView.setText(contactsInfos.get(groupPosition).getName());
            return textView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView textView = new TextView(parent.getContext());
            textView.setText(contactsInfos.get(childPosition).getPhone());
            return textView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}
