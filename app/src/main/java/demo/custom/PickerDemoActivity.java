package demo.custom;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.cwf.app.cwf.R;

import java.util.ArrayList;

import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.ColorPicker;
import cn.qqtheme.framework.picker.FilePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.StorageUtils;
import lib.BaseActivity;
import lib.utils.ActivityUtils;
import lib.utils.AssetsUtils;

/**
 * Created by n-240 on 2016/1/14.
 *
 * @author cwf
 */
/*多级联动选择器*/
public class PickerDemoActivity extends BaseActivity implements View.OnClickListener{
    Button address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        address = (Button) findViewById(R.id.picker_address);
        address.setOnClickListener(this);
        findViewById(R.id.picker_color).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.picker_address:
                ArrayList<AddressPicker.Province> data = new ArrayList<AddressPicker.Province>();
                String json = AssetsUtils.readText(this, "city.json");
                data.addAll(JSON.parseArray(json, AddressPicker.Province.class));
                AddressPicker addressPicker = new AddressPicker(this, data);
//                picker.setSelectedItem("贵州", "贵阳", "花溪");
                addressPicker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                    @Override
                    public void onAddressPicked(String province, String city, String county) {
                        ActivityUtils.showTip(province + city + county, false);
                        address.setText(province + city + county);
                    }
                });
                addressPicker.show();
                break;
            case R.id.picker_color:
                ColorPicker colorPicker = new ColorPicker(this);
                colorPicker.setInitColor(0xFFDD00DD);
                colorPicker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
                    @Override
                    public void onColorPicked(int pickedColor) {
                        ActivityUtils.showTip(ConvertUtils.toColorString(pickedColor), true);
                    }
                });
                colorPicker.show();
                break;
            case R.id.picker_file:
                FilePicker filePicker = new FilePicker(this);
                filePicker.setShowHideDir(false);
                filePicker.setRootPath(StorageUtils.getRootPath(this) + "");
                //picker.setAllowExtensions(new String[]{".apk"});
                filePicker.setMode(FilePicker.Mode.File);
                filePicker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
                    @Override
                    public void onFilePicked(String currentPath) {
                        ActivityUtils.showTip(currentPath, true);
                    }
                });
                filePicker.show();
                break;
        }
    }
}
