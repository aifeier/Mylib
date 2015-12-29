package lib.widget.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.cwf.app.cwf.R;

/**
 * Created by n-240 on 2015/12/29.
 *
 * @author cwf
 */
public class RecordImage extends Button{
    private OnRecordTouchListener onRecordTouchListener;
    public RecordImage(Context context) {
        super(context);
    }

    public RecordImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecordImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(onRecordTouchListener!=null){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    setBackgroundResource(R.drawable.but_item_select);
                    onRecordTouchListener.OnActionDown();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    onRecordTouchListener.OnActionMove();
                    return true;
                case MotionEvent.ACTION_UP:
                    onRecordTouchListener.OnActionUp();
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

    public interface OnRecordTouchListener{
        void OnActionDown();
        void OnActionMove();
        void OnActionUp();
    }

}
