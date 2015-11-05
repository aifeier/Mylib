package demo.custom.tabhost;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.cwf.app.cwf.R;

/**
 * Created by n-240 on 2015/11/5.
 */
public class fragment_2 extends Fragment{

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if(enter)
            return AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        else
            return AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_color, container, false);
    }
}
