package demo.anim;

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
public class AnimDemo extends BaseActivity{
    private ImageView imageView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_anim);
        setTitle("动画实例");
        initView();
        initAnim();
    }

    private void initView(){
        imageView = (ImageView) findViewById(R.id.anim_image);
    }

    private void initAnim(){
        final Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.ball_up_bottom);
        final Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.down_up_infinite);
        animation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(R.id.img2).setAnimation(animation1);
                animation1.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setAnimation(animation2);
                animation2.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.setAnimation(animation1);
        animation1.start();
        findViewById(R.id.img2).setAnimation(animation2);
        animation2.start();
    }
}
