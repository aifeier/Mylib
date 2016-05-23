package demo.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.drawable.AnimationDrawable;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cwf.app.cwf.R;

import lib.BaseActivity;
import lib.widget.KeyboardBuilder;
import lib.widget.PopupKeyBoard;

/**
 * Created by n-240 on 2015/12/29.
 *
 * @author cwf
 */
public class AnimDemo extends BaseActivity {
    private ImageView imageView;
    private EditText editText;
    private KeyboardBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        useToolbar = false;
        setContentView(R.layout.layout_anim);
        setTitle("动画实例");
        initView();
        initAnim();
        editText = (EditText) findViewById(R.id.edit);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Toast.makeText(AnimDemo.this, s.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
//        KeyboardView keyboardView = (KeyboardView) findViewById(R.id.keyboardview);
//        builder = new KeyboardBuilder(this, keyboardView, R.xml.keys_layout);
//        builder.registerEditText(editText);
    }

    PopupKeyBoard popupKeyBoard;

    @Override
    protected void onResume() {
        super.onResume();
        popupKeyBoard = new PopupKeyBoard(this);
        popupKeyBoard.setEditText(editText);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupKeyBoard.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (builder != null && builder.isCustomKeyboardVisible()) {
            builder.hideCustomKeyboard();
        } else {
            super.onBackPressed();
        }
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.anim_image);
    }

    private void initAnim() {
        final Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.ball_up_bottom);
        final Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.down_up_infinite);
        imageView.setAnimation(animation1);
        animation1.start();
        findViewById(R.id.img2).setAnimation(animation2);
        animation2.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        findViewById(R.id.img1).setBackgroundResource(R.drawable.animationlist);
        ((AnimationDrawable) findViewById(R.id.img1).getBackground()).stop();
        ((AnimationDrawable) findViewById(R.id.img1).getBackground()).start();
        super.onWindowFocusChanged(hasFocus);
    }
}
