package com.example.asynctasktutorial;

import android.graphics.drawable.AnimationDrawable;
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
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.RunnableFuture;

public class MainActivity extends AppCompatActivity implements MyTask.AudioRecordingCallback, MyTask.UICallBack{

    TextView textView;
    Button record, stop, nextImage;

    private String outputFile;
    private File lastCacheFile = null;
    final int SAMPLE_RATE =8000;
    boolean mShouldContinue;
    private MyTask SoundRecorder;

    ImageView imageView;
    AnimationDrawable anim;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        record = (Button) findViewById(R.id.btnrecord);
        stop = (Button) findViewById(R.id.btnstop);


imageView = (ImageView) findViewById(R.id.imageView3) ;
        if(imageView == null) throw new AssertionError();
        imageView.setBackgroundResource(R.drawable.animation_loading_wheeze);

       anim = (AnimationDrawable)imageView.getBackground();
        anim.start();


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

//    @Override
//    public void updateTextView() {
//
//        Log.d("MainActivity", "call back");
//    }

    @Override
    public void WheezeText() {
        TextView textView = (TextView) findViewById(R.id.UpdateText);
        textView.setText("Wheeze Detected");
    }

    @Override
    public void NonWheezeText() {
        TextView textView = (TextView) findViewById(R.id.UpdateText);
        textView.setText("Normal Breath");

    }
}
