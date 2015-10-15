package demo.intent;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.cwf.app.cwf.R;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;

import de.greenrobot.event.EventBus;
import demo.intent.mode.eventbus.TestEvent;
import lib.BaseActivity;
import lib.utils.ActivityUtils;

/**
 * Created by n-240 on 2015/9/23.
 */
public class VolleyDemoList extends BaseActivity implements OnItemClickListener , Response.Listener
                                            ,Response.ErrorListener{
    private static String TAG = "VolleyDemoList" ;
    private ListView mList;
    private final String[] demo = {"VoleyJsonTest","VolleyNetworkImageTest","OkHttp", "EventBus"};
    private NetworkImageView imageView;

    @Override
    protected  void onCreate (Bundle
        savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSystemBatTintManger().setTintResource(R.color.holo_green_light);
        mList = (ListView) findViewById(R.id.main_list);
            mList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, demo));
        mList.setOnItemClickListener(this);
        setActionBarColor("#55009999");
//        demo_normal();
//        demo_normal();
//        demo_post();
//        demo_json();
//        demo_image();
//        demo_networkImageView();
//        demo_XML();

    }

//    private void demo_networkImageView(){
//        imageView = (NetworkImageView) findViewById(R.id.main_image);
//        imageView.setVisibility(View.VISIBLE);
//        imageView.setDefaultImageResId(R.drawable.penguins);
//        imageView.setErrorImageResId(R.drawable.penguins);
//        imageView.setImageUrl("http://img0.imgtn.bdimg.com/it/u=1070902365,2619384777&fm=21&gp=0.jpg",
//                RequestManager.getImageLoader());
//    }
//
//
//    private void demo_normal(){
//        RequestQueue mQueue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest("http://www.baidu.com",new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e(TAG, response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, error.getMessage(), error);
//            }
//        });
//        stringRequest.setTag(0);
//        mQueue.add(stringRequest);
//    }
//
//    private void demo_post(){
//        RequestQueue mQueue = Volley.newRequestQueue(this);
//        StringRequest  stringRequest = new StringRequest(Request.Method.POST, "http://www.baidu.com",  this, this) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("params1", "value1");
//                map.put("params2", "value2");
//                return map;
//            }
//        };
//        stringRequest.setTag(1);
//        mQueue.add(stringRequest);
//    }
//
//    private void demo_json(){
//        RequestQueue mQreue = Volley.newRequestQueue(this);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://m.weather.com.cn/data/101010100.html", null, this,this);
//        mQreue.add(jsonObjectRequest);
//
//    }
//
//    public void demo_image() {
//        RequestQueue mQuesue = Volley.newRequestQueue(this);
//        ImageRequest imageRequest = new ImageRequest(
//                "http://developer.android.com/images/home/aw_dac.png",
//                new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap response) {
//                        imageView.setImageBitmap(response);
//                    }
//                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                imageView.setImageResource(R.drawable.penguins);
//            }
//        });
//        mQuesue.add(imageRequest);
//    }
//
//    private void demo_XML(){
//        RequestQueue mQueue = Volley.newRequestQueue(this);
//        XMLRequest xmlRequest = new XMLRequest(
//                "http://flash.weather.com.cn/wmaps/xml/china.xml",
//                new Response.Listener<XmlPullParser>() {
//                    @Override
//                    public void onResponse(XmlPullParser response) {
//                        try {
//                            int eventType = response.getEventType();
//                            while (eventType != XmlPullParser.END_DOCUMENT) {
//                                switch (eventType) {
//                                    case XmlPullParser.START_TAG:
//                                        String nodeName = response.getName();
//                                        if ("city".equals(nodeName)) {
//                                            String pName = response.getAttributeValue(0);
//                                            Log.d("TAG", "pName is " + pName);
//                                        }
//                                        break;
//                                }
//                                eventType = response.next();
//                            }
//                        } catch (XmlPullParserException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("TAG", error.getMessage(), error);
//            }
//        });
//        mQueue.add(xmlRequest);
//    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = null;
        switch (position){
            case 0:
                i = new Intent(VolleyDemoList.this, JsonTest.class);
                break;
            case 1:
                i = new Intent(VolleyDemoList.this, NetworkImageTest.class);
                break;
            case 2:
                i = new Intent(VolleyDemoList.this, OkhttpDemo.class);
                break;
            case 3:
                i = new Intent(VolleyDemoList.this, EventBusDemo.class);
                break;
        }
        if(i != null)
            startActivity(i);

    }

    @Override
    public void onResponse(Object o) {
        if (o instanceof String)
            ActivityUtils.showTip((String) o ,true);
        else
            if (o instanceof JSONArray)
                ActivityUtils.showTip(o.toString(), true);

    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        Log.e(TAG , volleyError.getMessage().toString(), volleyError);
    }

}
