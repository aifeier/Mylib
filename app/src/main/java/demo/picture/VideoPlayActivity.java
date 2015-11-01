package demo.picture;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.cwf.app.cwf.R;

import lib.BaseActivity;
import lib.utils.FileUtils;

/**
 * Created by chenw on 2015/10/31.
 */

/**
* 本实例演示如何在Android中播放网络上的视频，这里牵涉到视频传输协议，视频编解码等知识点
 *Android当前支持两种协议来传输视频流一种是Http协议，另一种是RTSP协议
 *Http协议最常用于视频下载等，但是目前还不支持边传输边播放的实时流媒体
 *同时，在使用Http协议 传输视频时，需要根据不同的网络方式来选择合适的编码方式，
 *比如对于GPRS网络，其带宽只有20kbps,我们需要使视频流的传输速度在此范围内。
 *比如，对于GPRS来说，如果多媒体的编码速度是400kbps，那么对于一秒钟的视频来说，就需要20秒的时间。这显然是无法忍受的
 *Http下载时，在设备上进行缓存，只有当缓存到一定程度时，才能开始播放。
 *
 *所以，在不需要实时播放的场合，我们可以使用Http协议
 *
 *RTSP：Real Time Streaming Protocal，实时流媒体传输控制协议。
 *使用RTSP时，流媒体的格式需要是RTP。
 *RTSP和RTP是结合使用的，RTP单独在Android中式无法使用的。
 *
 *RTSP和RTP就是为实时流媒体设计的，支持边传输边播放。
 *
 *同样的对于不同的网络类型（GPRS，3G等），RTSP的编码速度也相差很大。根据实际情况来
 *
 *使用前面介绍的三种方式，都可以播放网络上的视频，唯一不同的就是URI
 *
 *本例中使用VideoView来播放网络上的视频
  */

public class VideoPlayActivity extends Activity{

    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);
        videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setMediaController(new MediaController(this));
        Uri uri = Uri.parse(
                "http://hc.yinyuetai.com/uploads/videos/common/BC3C013F426AC39ECC7DE6E43ECDAD43.flv?sc=4a1a2e90ba57c4c4&br=776&vid=687613&aid=1406&area=KR&vst=4\n");
//        uri = Uri.parse(FileUtils.SDPATH+"video/2015-10-25-00-36-01.mp4");
        uri = Uri.parse("http://he.yinyuetai.com/uploads/videos/common/BF6E0144973D496A0DEE6F4D99529FE8.flv?sc=fdf0d21351abf9a2&br=3131&vid=2005287&aid=1559&area=KR&vst=4");
        uri = Uri.parse("http://he.yinyuetai.com/uploads/videos/common/ED7E0150647552A1F94E35CDA9803703.flv?sc=125ee2c11e8cb7c0&br=3087&vid=2390250&aid=25339&area=KR&vst=0");
        videoView.setVideoURI(uri);
        videoView.requestFocus();
        videoView.start();

    }

}
