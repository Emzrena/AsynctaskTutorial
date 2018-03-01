package com.example.asynctasktutorial;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity implements MyTask.AudioRecordingCallback, MyTask.UICallBack{

    TextView textView;
    Button record, stop;

    private String outputFile;
    private File lastCacheFile = null;
    final int SAMPLE_RATE =8000;
    boolean mShouldContinue;
    private MyTask SoundRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        record = (Button) findViewById(R.id.btnrecord);
        stop = (Button) findViewById(R.id.btnstop);



        SoundRecorder = new MyTask(this, "file_name");

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SoundRecorder.startRecording();
                } catch (IllegalStateException ise) {
                    ise.printStackTrace();
                    //make something
                }
                record.setEnabled(false);
                stop.setEnabled(true);


                Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_LONG).show();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                record.setEnabled(true);
                stop.setEnabled(false);
                SoundRecorder.stopRecording();
                Toast.makeText(getApplicationContext(), "Recorded Successfully", Toast.LENGTH_LONG).show();

            }
        });
    }













    public boolean deleteLastRecordFile() {
        boolean success = false;
        if (lastCacheFile != null && lastCacheFile.exists()) {
            success = lastCacheFile.delete();
        }
        return success;
    }


//    @Override
//    public void onPlaybackStopped() {
//
//    }

    @Override
    public void onRecording(byte[] data, int startIndex, int length) {

    }

    @Override
    public void onStopRecord(String savedPath) {

    }

    @Override
    public void updateTextView() {
        Log.d("MainActivity", "call back");
    }
}
