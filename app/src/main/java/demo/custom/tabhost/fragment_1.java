package demo.custom.tabhost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.cwf.app.cwf.R;


/**
 * Created by n-240 on 2015/11/5.
 */
public class fragment_1 extends Fragment{

    private TextView textView;

    private View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if(enter)
            return AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        else
            return AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null){
            mView = inflater.inflate(R.layout.layout_text_img, container, false);
            textView = (TextView) mView.findViewById(R.id.textview);
        }
        textView.setText("this is a way");
        return mView;
    }
}
