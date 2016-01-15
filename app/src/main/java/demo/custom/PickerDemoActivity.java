package demo.custom;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.cwf.app.cwf.R;

import java.util.ArrayList;

import cn.qqtheme.framework.picker.AddressPicker;
import lib.BaseActivity;
import lib.utils.ActivityUtils;
import lib.utils.AssetsUtils;
import me.iwf.photopicker.utils.PhotoPickerIntent;

/**
 * Created by n-240 on 2016/1/14.
 *
 * @author cwf
 */
/*多级联动选择器*/
public class PickerDemoActivity extends BaseActivity implements View.OnClickListener{
    Button address;
    Button photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        address = (Button) findViewById(R.id.picker_address);
        address.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.picker_address:
                ArrayList<AddressPicker.Province> data = new ArrayList<AddressPicker.Province>();
                String json = AssetsUtils.readText(this, "city.json");
                data.addAll(JSON.parseArray(json, AddressPicker.Province.class));
                AddressPicker picker = new AddressPicker(this, data);
//                picker.setSelectedItem("贵州", "贵阳", "花溪");
                picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                    @Override
                    public void onAddressPicked(String province, String city, String county) {
                        ActivityUtils.showTip(province + city + county, false);
                        address.setText(province + city + county);
                    }
                });
                picker.show();
                break;
        }
    }
}
