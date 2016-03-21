package demo.custom;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * Created by n-240 on 2016/2/19.
 *
 * @author cwf
 */
public class SuspendWindowManager {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams windowParams;
    private RelativeLayout myView;

    private void createWindow(final Context context){
        final WindowManager windowManager = getWindowManager(context);
        if(windowParams == null){
            windowParams = getWindowParams(context);
        }
    }

    private WindowManager.LayoutParams getWindowParams(Context context) {
        final WindowManager windowManager = getWindowManager(context);
        Point sizePoint = new Point();
        windowManager.getDefaultDisplay().getSize(sizePoint);
        int screenWidth = sizePoint.x;
        int screenHeight = sizePoint.y;
        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        windowParams.format = PixelFormat.RGBA_8888;
        windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        windowParams.gravity = Gravity.START | Gravity.TOP;
        windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        windowParams.x = screenWidth;
        windowParams.y = screenHeight / 2;
        return windowParams;
    }

    private WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }
}
