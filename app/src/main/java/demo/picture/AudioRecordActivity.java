package demo.picture;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cwf.app.cwf.R;

import java.io.IOException;

import lib.BaseActivity;
import lib.utils.FileUtils;
import lib.utils.TimeUtils;

/**
 * Created by n-240 on 2015/10/23.
 */
public class AudioRecordActivity extends BaseActivity{
    private Button start;
    private Button stop;

    private MediaRecorder mediaRecorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_videorecord);
        initView();
    }

    private void initView(){
        start = (Button)findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        start.setText("开始录制声音");
        stop.setEnabled(false);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecord();
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecord();
            }
        });
    }


    private void startRecord(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setAudioEncodingBitRate(44100);
        mediaRecorder.setOutputFile(FileUtils.createPath("audio/")
                +"/"+TimeUtils.getSimpleDate().replace(" ", "-").replace(":", "-") + ".aac");
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
        start.setEnabled(false);
        stop.setEnabled(true);
    }

    private void stopRecord(){
        if(mediaRecorder!=null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            start.setEnabled(true);
            stop.setEnabled(false);
        }

    }
}
