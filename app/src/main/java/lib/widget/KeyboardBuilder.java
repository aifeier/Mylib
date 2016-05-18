package lib.widget;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by n-240 on 2016/5/18.
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class KeyboardBuilder {
    private static final String TAG = "KeyboardBuilder";
    private Activity mActivity;

    private KeyboardView mKeyboardView;

    private PopupKeyBoard popupKeyBoard;

    public KeyboardBuilder(Activity ac, KeyboardView keyboardView, int keyBoardXmlResId) {
        mActivity = ac;
        mKeyboardView = keyboardView;

        Keyboard mKeyboard = new Keyboard(mActivity, keyBoardXmlResId);
        // Attach the keyboard to the view
        mKeyboardView.setKeyboard(mKeyboard);
        // Do not show the preview balloons
        mKeyboardView.setPreviewEnabled(false);

        KeyboardView.OnKeyboardActionListener keyboardListener = new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
                // Get the EditText and its Editable
                View focusCurrent = mActivity.getWindow().getCurrentFocus();
                if (focusCurrent == null || !(focusCurrent instanceof EditText)) {
                    return;
                }

                EditText edittext = (EditText) focusCurrent;
                Editable editable = edittext.getText();
                int start = edittext.getSelectionStart();

                // Handle key
                if (primaryCode == 60002) {
                    hideCustomKeyboard();
                } else if (primaryCode == 60001) {
                    if (editable != null && start > 0) {
                        editable.delete(start - 1, start);
                    }
                } else {
                    // Insert character
                    editable.insert(start, Character.toString((char) primaryCode));
                }
            }

            @Override
            public void onPress(int arg0) {
            }

            @Override
            public void onRelease(int primaryCode) {
            }

            @Override
            public void onText(CharSequence text) {
            }

            @Override
            public void swipeDown() {
            }

            @Override
            public void swipeLeft() {
            }

            @Override
            public void swipeRight() {
            }

            @Override
            public void swipeUp() {
            }
        };
        mKeyboardView.setOnKeyboardActionListener(keyboardListener);
    }

    //绑定一个EditText
    public void registerEditText(EditText editText) {
        // Make the custom keyboard appear
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showCustomKeyboard(v);
                } else {
                    hideCustomKeyboard();
                }
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick");

                showCustomKeyboard(v);
            }
        });

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch");

                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();       // Backup the input type
                edittext.setInputType(InputType.TYPE_NULL); // Disable standard keyboard
                edittext.onTouchEvent(event);               // Call native handler
                edittext.setInputType(inType);              // Restore input type
                edittext.setSelection(edittext.getText().length());

                return true;
            }
        });
    }

    public void hideCustomKeyboard() {
//        mKeyboardView.setVisibility(View.GONE);
//        mKeyboardView.setEnabled(false);
        if (popupKeyBoard != null && popupKeyBoard.isShowing())
            popupKeyBoard.dismiss();
    }

    public void showCustomKeyboard(View v) {
//        mKeyboardView.setVisibility(View.VISIBLE);
//        mKeyboardView.setEnabled(true);
        if (popupKeyBoard != null && !popupKeyBoard.isShowing())
            popupKeyBoard.show();
        if (v != null) {
            ((InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public boolean isCustomKeyboardVisible() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }

    public void setPopupKeyBoard(PopupKeyBoard popupKeyBoard) {
        this.popupKeyBoard = popupKeyBoard;
    }
}
