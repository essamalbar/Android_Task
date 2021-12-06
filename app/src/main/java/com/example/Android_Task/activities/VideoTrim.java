package com.example.Android_Task.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.Android_Task.R;
import com.example.Android_Task.videoTrimmer.HgLVideoTrimmer;
import com.example.Android_Task.videoTrimmer.interfaces.OnHgLVideoListener;
import com.example.Android_Task.videoTrimmer.interfaces.OnTrimVideoListener;

public class VideoTrim extends AppCompatActivity implements OnTrimVideoListener, OnHgLVideoListener {
    private HgLVideoTrimmer mVideoTrimmer;
    private static ProgressDialog mProgressDialog;
    String path,absolutePath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_trim);
        Intent extraIntent = getIntent();

        int maxDuration = 10;

        if (extraIntent != null) {
           path = extraIntent.getExtras().getString("EXTRA_VIDEO_PATH");
           maxDuration = extraIntent.getExtras().getInt("VIDEO_TOTAL_DURATION");
           absolutePath=extraIntent.getExtras().getString("absolutePath");

        }


        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mVideoTrimmer = ((HgLVideoTrimmer) findViewById(R.id.timeLine));
        if (mVideoTrimmer != null) {

            Log.e("tg", "maxDuration = " + maxDuration);
            mVideoTrimmer.setMaxDuration(maxDuration);
            mVideoTrimmer.setOnTrimVideoListener(this);
            mVideoTrimmer.setOnHgLVideoListener(this);
            mVideoTrimmer.setDestinationPath("/storage/emulated/0/DCIM/CameraCustom/");
            if(path!=null) {
                mVideoTrimmer.setVideoURI(Uri.parse(path));
                mVideoTrimmer.setVideoInformationVisibility(true);
            }
        }
    }

    @Override
    public void onVideoPrepared() {

    }

    @Override
    public void onTrimStarted() {
        mProgressDialog.show();
    }

    @Override
    public void getResult(final Uri contentUri) {
        mProgressDialog.cancel();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VideoTrim.this, "video_saved_at"+ contentUri.getPath(), Toast.LENGTH_SHORT).show();
                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW, contentUri);
                    intent.setDataAndType(contentUri, "video/*");
                    startActivity(intent);


                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });


    }

    @Override
    public void ffmpegResultSUCCESS() {
        mProgressDialog.dismiss();
    }

    @Override
    public void ffmpegResultCANCEL() {


    }


    @Override
    public String setPath() {

        return absolutePath;
    }

    @Override
    public void cancelAction() {
        finish();

    }

    @Override
    public void onError(String message) {

    }
}