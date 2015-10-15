package demo.intent;

import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.toolbox.NetworkImageView;
import com.cwf.app.cwf.R;

import demo.intent.mode.toolbox.RequestManager;
import lib.BaseActivity;

/**
 * Created by n-240 on 2015/9/23.
 */
public class NetworkImageTest extends BaseActivity{

    private NetworkImageView mImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_text_img);
        mImg = (NetworkImageView) findViewById(R.id.NetWorkImg);
        mImg.setDefaultImageResId(R.drawable.penguins);
        mImg.setErrorImageResId(R.drawable.penguins);
        mImg.setImageUrl("http://img0.imgtn.bdimg.com/it/u=1070902365,2619384777&fm=21&gp=0.jpg",
                RequestManager.getImageLoader());

    }

    private class mRequest extends AsyncTask<Void, Integer , String>{

        private int[] requestTags = null;
        private String result;
        private String url;

        public mRequest(String url, int[] tags){
            this.requestTags = tags;
            this.url = url;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }



        @Override
        protected String doInBackground(Void... params) {
            return null;
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }
}
