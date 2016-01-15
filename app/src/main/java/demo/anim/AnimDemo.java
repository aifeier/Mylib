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
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.ball_up_bottom);
        imageView.setAnimation(animation1);
        animation1.start();
    }
}
