package demo.qrcode;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.cwf.app.cwflibrary.utils.NetUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Contents;
import com.google.zxing.client.android.Intents;

import lib.BaseActivity;
import lib.utils.ActivityUtils;
import com.cwf.app.cwf.R;

/**
 * Created by n-240 on 2015/10/13.
 */
public class QRCodeMain extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_text_img);
        ActivityUtils.showTip("" + NetUtils.isWifi(getApplicationContext()), false);
        Intent i = new Intent(QRCodeMain.this, CaptureActivity.class);
        startActivity(i);
        finish();
//        Intent intent = new Intent(Intents.Encode.ACTION);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//        intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
//        intent.putExtra(Intents.Encode.DATA, "nishi2bma \nhaohao");
//        intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE.toString());
//        startActivity(intent);


    }
}
