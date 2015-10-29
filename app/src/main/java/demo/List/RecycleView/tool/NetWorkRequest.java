package demo.List.RecycleView.tool;

import com.cwf.app.okhttplibrary.OkHttpClientManager;
import com.squareup.okhttp.Request;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import demo.intent.entity.News;
import demo.intent.entity.NewsInfo;
import lib.utils.ActivityUtils;

/**
 * Created by n-240 on 2015/10/29.
 */
public class NetWorkRequest {
    private static int page = 1;
    public static void getPage(final int page){
        OkHttpClientManager.getAsyn("http://api.huceo.com/meinv/other/?key=e7b0c852050f609d927bc20fe11fde9c&num=20&page=" + page,
                new OkHttpClientManager.ResultCallback<News>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        EventBus.getDefault().post(new ArrayList<NewsInfo>());
                    }

                    @Override
                    public void onResponse(News news) {
                        EventBus.getDefault().post(news.getNewslist());
                    }
                });
    }
}
