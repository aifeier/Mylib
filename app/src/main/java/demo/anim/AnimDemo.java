package demo.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.cwf.app.cwf.R;

import lib.BaseActivity;

/**
 * Created by n-240 on 2015/12/29.
 *
 * @author cwf
 */
public class AnimDemo extends BaseActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_anim);
        setTitle("动画实例");
        initView();
        initAnim();
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
