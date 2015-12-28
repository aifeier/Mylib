package demo.custom;

import android.os.Bundle;
import android.widget.GridView;

import com.cwf.app.cwf.R;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;

import lib.widget.GridAdapter;
import lib.widget.ViewHolder;

/**
 * Created by n-240 on 2015/12/28.
 *
 * @author cwf
 */
public class AutoLayoutDemo extends AutoLayoutActivity{
    private GridView noScrollGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_autodemo);
        noScrollGridView = (GridView) findViewById(R.id.layout_gridview);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("c");
        arrayList.add("d");
        arrayList.add("e");
        arrayList.add("f");
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("c");
        arrayList.add("d");
        arrayList.add("e");
        arrayList.add("f");
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("c");
        arrayList.add("d");
        arrayList.add("e");
        arrayList.add("f");
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("c");
        arrayList.add("d");
        arrayList.add("e");
        arrayList.add("f");
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("c");
        arrayList.add("d");
        arrayList.add("e");
        arrayList.add("f");
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("c");
        arrayList.add("d");
        arrayList.add("e");
        arrayList.add("f");
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("c");
        arrayList.add("d");
        arrayList.add("e");
        arrayList.add("f");
        arrayList.add("a");
        arrayList.add("b");
        arrayList.add("c");
        arrayList.add("d");
        arrayList.add("e");
        arrayList.add("f");

        noScrollGridView.setAdapter(new GridAdapter<String>(this, android.R.layout.simple_gallery_item, arrayList) {
            @Override
            public void buildView(ViewHolder holder, String data) {
                holder.setValueToTextView(android.R.id.text1, data);
            }

            @Override
            public void buildAddView(ViewHolder holder) {

            }

            @Override
            public boolean canAddItem() {
                return false;
            }

            @Override
            public int getMaxSize() {
                return -1;
            }
        });
    }
}
