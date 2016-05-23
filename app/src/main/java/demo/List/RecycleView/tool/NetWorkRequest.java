package demo.List.RecycleView.tool;

import com.cwf.libs.okhttplibrary.OkHttpClientManager;
import com.cwf.libs.okhttplibrary.callback.ResultCallBack;
import com.squareup.okhttp.Request;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import demo.intent.entity.News;
import demo.intent.entity.NewsInfo;

/**
 * Created by n-240 on 2015/10/29.
 */
public class NetWorkRequest {
    private static int page = 1;

    public static void getPage(final int page) {
        OkHttpClientManager.getInstance().get("http://api.huceo.com/meinv/other/?key=e7b0c852050f609d927bc20fe11fde9c&num=10&page=" + page,
                new ResultCallBack() {
                    @Override
                    public void onFailure(Exception e) {
                        EventBus.getDefault().post(new ArrayList<NewsInfo>());
                    }

                    @Override
                    public void onSuccess(String result) {
                        EventBus.getDefault().post(result);
                    }
                });
    }
}
