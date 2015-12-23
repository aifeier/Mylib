package demo.intent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cwf.app.cwf.R;

import java.util.Observable;
import java.util.concurrent.ThreadPoolExecutor;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import demo.intent.mode.eventbus.TestEvent;
import lib.BaseActivity;
import lib.utils.ActivityUtils;

/**
 * Created by n-240 on 2015/10/12.
 */
public class EventBusDemo extends BaseActivity{

    private TextView tv;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_text_img);
        EventBus.getDefault().register(this);//注册eventbus
        time = 0;
        tv = (TextView) findViewById(R.id.textview);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time++;
                /*根据post中的参数类型来判断使用哪个Subscribe接收*/
                EventBus.getDefault().post(new TestEvent("you click it " + time));
                EventBus.getDefault().post("you click it " + time);
                EventBus.getDefault().post(time);
            }
        });

    }



    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);//反注册eventbus，即取消evnetbus的注册
        super.onDestroy();
    }

    /*如果使用onEventMainThread作为订阅函数，那么不论事件是在哪个线程中发布出来的，
    onEventMainThread都会在UI线程中执行，接收事件就会在UI线程中运行，这个在Android中是非常有用的，
    因为在Android中只能在UI线程中跟新UI，所以在onEvnetMainThread方法中是不能执行耗时操作的*/
    @Subscribe
    public void onEventMainThread(TestEvent event) {

        String msg = "onEventMainThread：" + event.getMsg();
        Log.d("harvic", msg);
        tv.setText(msg);
        ActivityUtils.showTip(msg, true);
    }

    @Subscribe
    public void onEventMainThread(String event) {

        String msg = "onEventMainThread(String)：" + event;
        Log.d("harvic", msg);
        tv.setText(msg);
        ActivityUtils.showTip(msg, true);
    }

    /*如果使用onEvent作为订阅函数，那么该事件在哪个线程发布出来的，onEvent就会在这个线程中运行，
    也就是说发布事件和接收事件线程在同一个线程。使用这个方法时，在onEvent方法中不能执行耗时操作，
    如果执行耗时操作容易导致事件分发延迟。*/
    @Subscribe
    public void onEvent(TestEvent event){
        ActivityUtils.showTip("onEvent: " + event.getMsg(), false);
        Log.e("", "onEvent" + event.getMsg());
    }

    /*如果使用onEventBackgrond作为订阅函数，那么如果事件是在UI线程中发布出来的，
    那么onEventBackground就会在子线程中运行，如果事件本来就是子线程中发布出来的，
    那么onEventBackground函数直接在该子线程中执行*/
    @Subscribe
    public void onEventBackground(TestEvent event){
        ActivityUtils.showTip("onEventBackground: " + event.getMsg(), false);
        Log.e("","onEventBackground" +  event.getMsg());
    }

    /*使用这个函数作为订阅函数，那么无论事件在哪个线程发布，都会创建新的子线程在执行onEventAsync*/
    @Subscribe
    public void onEventAsync(TestEvent event){
        ActivityUtils.showTip("onEventAsync: " + event.getMsg(), false);
        Log.e("","onEventAsync" +  event.getMsg());
    }
}
