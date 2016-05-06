package lib.widget.customview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import com.cwf.app.cwf.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by n-240 on 2015/12/29.
 *
 * @author cwf
 */
public class RecordImage extends Button {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                setEnabled(true);
                onRecordTouchListener.OnActionUp();
            } else
                setEnabled(false);
        }
    };

    private long time;
    private OnRecordTouchListener onRecordTouchListener;

    public RecordImage(Context context) {
        super(context);
        setBackgroundResource(R.drawable.but_item_select);
    }

    public RecordImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(R.drawable.but_item_select);
    }

    public RecordImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundResource(R.drawable.but_item_select);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onRecordTouchListener != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onRecordTouchListener.OnActionDown();
                    time = System.currentTimeMillis();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    onRecordTouchListener.OnActionMove();
                    return true;
                case MotionEvent.ACTION_UP:
                    if (System.currentTimeMillis() - time > 1000) {
                        onRecordTouchListener.OnActionUp();
                    } else {
                        handler.sendEmptyMessage(1);
                        Timer timer = new Timer();
                        TimerTask timetask = new TimerTask() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(0);
                            }
                        };
                        timer.schedule(timetask, 1000);
                    }
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public OnRecordTouchListener getOnRecordTouchListener() {
        return onRecordTouchListener;
    }

    public void setOnRecordTouchListener(OnRecordTouchListener onRecordTouchListener) {
        this.onRecordTouchListener = onRecordTouchListener;
    }

    public interface OnRecordTouchListener {
        void OnActionDown();

        void OnActionMove();

        void OnActionUp();

    }

}
