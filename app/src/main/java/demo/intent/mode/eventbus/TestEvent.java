package demo.intent.mode.eventbus;




/**
 * Created by n-240 on 2015/10/12.
 */
public class TestEvent {

    private String mMsg;


    public TestEvent(String message){
      /*  List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("tel", "15867117181"));
        FormEncodingBuilder form = new FormEncodingBuilder();
        form.add("", "");
        RequestBody b = form.build();
        String u = OkHttpUtil.attachHttpGetParams(
                "http://apis.baidu.com/apistore/mobilephoneservice/mobilephone", params);
        OkHttpUtil.enqueue(
                new Request.Builder().url(u).post(b)
                        .addHeader("apikey", " ed238d5e9c0f41c0155b8c2aead25e73").build());
        mMsg = message;*/
    }

    public String getMsg(){
        return mMsg;
    }
}
