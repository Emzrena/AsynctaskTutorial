package com.example.asynctasktutorial;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.widget.Toast;


import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static com.example.asynctasktutorial.R.id.textView;

/**
 * Created by Emilia on 21/2/2018.
 */

public class MyTask   {
    private AudioRecord audioRecord;
    private boolean isRecording = false;
    Context mcontext;
    TextView textView;
    Button button;
    ProgressDialog progressDialog;
    private File lastCacheFile = null;
    final int SAMPLE_RATE = 44100;
    boolean mShouldContinue;
    private String mOutputFileName;
    private AsyncTask<Void,Void,Void> mRecordingAsyncTask;
    private static final String TAG = "SoundRecorder";
    private static final int RECORDING_RATE = 8000;//or frequency // can go up to 44K, if needed
    private static final int CHANNEL_IN = AudioFormat.CHANNEL_IN_MONO;
    private static final int CHANNELS_OUT = AudioFormat.CHANNEL_OUT_MONO;
    private static final int FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    //referencing from MATLAB, Fs, samples per second is 8000

    private static int BUFFER_SIZE = AudioRecord
           .getMinBufferSize(RECORDING_RATE, CHANNEL_IN, FORMAT);


//    private static final int RECORDER_SAMPLERATE = 8000;
//    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
//    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
//    int BufferElements2Rec = 1024; // want to play 2048 (2K) since 2 bytes we use only 1024
//    int BytesPerElement = 2; // 2 bytes in 16bit format
    private Thread recordingThread = null;

    private UICallBack uiCallBack;

//    MyTask(Context context, String outputFileName, OnVoicePlaybackStateChangedListener listner) {
//        mcontext = context;
//        mOutputFileName = outputFileName;
//        this.textView = textView;
//        this.button = button;
//
//
//    }

    MyTask(Context context, String outputFileName) {
        mcontext = context;
        mOutputFileName = outputFileName;
        this.textView = textView;
        this.button = button;
        uiCallBack = (UICallBack) context;
    }

    public void startRecording() {

        isRecording = true;

        mRecordingAsyncTask = new AsyncTask<Void, Void, Void>() {
            private AudioRecord mAudioRecord;

            Handler handler = new Handler();

            @Override
            protected void onPreExecute(){

            }








            @Override
            protected Void doInBackground(Void... params) {
                mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                        RECORDING_RATE, CHANNEL_IN, FORMAT, BUFFER_SIZE);
                BufferedOutputStream bufferedOutputStream = null;

                try {
                    bufferedOutputStream = new BufferedOutputStream(mcontext.openFileOutput(mOutputFileName, Context.MODE_PRIVATE));
                    //buffer has 640 frames

                        final short[] buffer = new short[BUFFER_SIZE];
                        mAudioRecord.startRecording();
                        int read = mAudioRecord.read(buffer, 0, buffer.length);

                        //bufferedOutputStream.write(buffer, 0, read);

                        Runnable runnable = new Runnable() {

                            @Override
                            synchronized public void run() {
                                // get the buffer and do stuff
                                // Log.d("runnable", "2 seconds");
                                if (isRecording) handler.postDelayed(this, 5000);

            // NOTE, How to change buffer everytime in an inner class? I have to declare as final:/
                                //Log.d("BufferSize", String.valueOf((BUFFER_SIZE)));

                                Log.d("runnable", Arrays.toString(buffer));

                            }
                        };

                        if (isRecording) handler.postDelayed(runnable, 2000);







                    while (isRecording) {

//                        new CountDownTimer(5000, 1000) {
//
//                            @Override
//                            public void onTick(long millisUntilFinished) {
//                                Log.d("timer: ", "recording");
//                            }
//
//                            @Override
//                            public void onFinish() {
//                                Log.d("timer: ", "done!");
//                            }
//                        }.start();

//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.d("handler", "run");
//                                handler.postDelayed(this, 1000);
//                            }
//                        }, 1000);



//                        synchronized (this) {
//                            try {
//                                wait(3000);
//                                // get buffer process it
//                                // clear buffer
//                                Log.d("sync", "2 seconds passed");
//                            } catch (InterruptedException e) {
//                                // pass
//                            }
//                        }

//                        int read = mAudioRecord.read(buffer, 0, buffer.length);
//
//                        bufferedOutputStream.write(buffer, 0, read);
                        //Log.d("print buffer length", String.valueOf(mAudioRecord));
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedOutputStream != null) {
                        try {
                            bufferedOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    mAudioRecord.release();
                    mAudioRecord = null;
                }
                return null;
            }
                @Override
                        protected void onPostExecute(Void aVoid) {
                    mRecordingAsyncTask = null;
                }
                @Override
                protected void onCancelled () {
                    Log.w(TAG, "Requesting to stop recording while state was not recording");
                    mRecordingAsyncTask = null;
                }


        };
        mRecordingAsyncTask.execute();
    }













    public void stopRecording() {
        // stops the recording activity
        if (null != audioRecord) {
            isRecording = false;
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
            recordingThread = null;
        }
        isRecording = false;
        Log.d("StopRecordFired", "it is running");
        uiCallBack.updateTextView();
    }


//    public interface OnVoicePlaybackStateChangedListener {
//        public void onPlaybackStopped();
//    }

    public interface AudioRecordingCallback {

        public void onRecording(byte[] data, int startIndex, int length);

        /**
         * 录音结束后的回调
         *
         * @param savedPath
         *            录音文件存储的路径
         */
        public void onStopRecord(String savedPath);
    }

    public interface UICallBack {
        void updateTextView();
    }
}

//Dependency Inversion Principle
//Create new interface for UpdatingUI
//call from MainActivity