package demo.intent;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.cwf.app.cwf.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import demo.intent.entity.Weather;
import demo.intent.mode.toolbox.RequestManager;
import lib.BaseActivity;

/**
 * Created by n-240 on 2015/9/23.
 */
public class JsonTest extends BaseActivity implements  Response.Listener,Response.ErrorListener{

    private TextView text;
    private NetworkImageView mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_text_img);
        setWaitProgressDialog(true);

        ImageView img = (ImageView) findViewById(R.id.imageview);
        //goole推荐的图片加载
        Glide.with(this)
                .load("http://b.hiphotos.baidu.com/image/pic/item/29381f30e924b899d84ce5396c061d950a7bf6bb.jpg")
                .error(R.drawable.image_bg).placeholder(R.drawable.image_bg).into(img);
        text = (TextView) findViewById(R.id.textview);
        Map<String ,String> header = new HashMap<String , String>();
        header.put("apikey", "ed238d5e9c0f41c0155b8c2aead25e73");
        RequestManager.addHeader(header);
        RequestManager.getRequestQueue().add(
                RequestManager.getStringRequestGet("http://apis.baidu.com/apistore/weatherservice/weather?citypinyin=hangzhou"
                        , this, this));
/*        mImg = (NetworkImageView) findViewById(R.id.NetWorkImg);
        mImg.setDefaultImageResId(R.drawable.penguins);
        mImg.setErrorImageResId(R.drawable.penguins);
        mImg.setImageUrl("http://img0.imgtn.bdimg.com/it/u=1070902365,2619384777&fm=21&gp=0.jpg",
                RequestManager.getImageLoader());*/
//        CircleNetworkImageView photoview = new CircleNetworkImageView(this);
//        photoview.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        photoview.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT));
//        photoview.setErrorImageResId(R.drawable.penguins);
//        photoview.setImageUrl("http://img0.imgtn.bdimg.com/it/u=1070902365,2619384777&fm=21&gp=0.jpg",
//                RequestManager.getImageLoader());
//        zoomImg = (ZoomNetworkImageView) findViewById(R.id.zoomImg);
//        zoomImg.setZoomable(true);
//        zoomImg.setImageUrl("http://img0.imgtn.bdimg.com/it/u=1070902365,2619384777&fm=21&gp=0.jpg"
//                , RequestManager.getImageLoader());
//        RelativeLayout.LayoutParams relLayoutParams=new RelativeLayout.LayoutParams
//                ( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        relLayoutParams.addRule(RelativeLayout.BELOW,R.id.zoomImg);
//        PhotoView mv = new PhotoView(this);
//        mv.setDefaultImageResId(R.drawable.penguins);
//        addContentView(mv, relLayoutParams);

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        text.setText(volleyError.getMessage());
        setWaitProgressDialog(false);

    }

    @Override
    public void onResponse(Object o) {
        setWaitProgressDialog(false);
        text.setText(o.toString());
//        IDCard result = (IDCard) o;
        Weather result=null;
        try {
            Gson gson = new Gson();
            gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            result = gson.fromJson(o.toString(), Weather.class);
        }catch (Exception e){
            e.printStackTrace();
            text.setText(e.getMessage());
        }
        if(result!=null&&result.getErrNum() == 0)
            text.setText(result.getRetData().getCity() + "\n" + result.getRetData().getDate() + "\n"
                    + result.getRetData().getTime()+"\n"+result.getRetData().getWeather());
        else{
            text.setText("错误：" + result.getErrNum());
        }
    }
}
