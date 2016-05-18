package lib.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.inputmethodservice.KeyboardView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.cwf.app.cwf.R;

/**
 * Created by n-240 on 2016/5/18.
 * 自定义输入框
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class PopupKeyBoard extends PopupWindow {

    private Activity activity;
    private EditText editText;
    private KeyboardView keyboardView;
    private KeyboardBuilder keyboardBuilder;

    public PopupKeyBoard(Context context) {
        super(context);
        this.activity = (Activity) context;
        initView();
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
        if (editText != null) {
            keyboardBuilder.registerEditText(editText);
        }
    }

    private void initView() {
        View view = activity.getLayoutInflater().inflate(R.layout.layout_board, null);
        keyboardView = (KeyboardView) view.findViewById(R.id.keyboardview);
        keyboardBuilder = new KeyboardBuilder(activity, keyboardView, R.xml.keys_layout);
        keyboardBuilder.setPopupKeyBoard(this);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        //因为某些机型是虚拟按键的,所以要加上以下设置防止挡住按键.
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    public void show() {
        showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }
}
