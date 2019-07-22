package com.example.mini_douyin;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        String url = getIntent().getStringExtra("url");
        Uri videourl = Uri.parse(url);
        //System.out.println(url);
        videoView = findViewById(R.id.video_view);
        mediaController = new MediaController(this);
        videoView.setVideoURI(videourl);
        videoView.setMediaController(mediaController);
        videoView.start();
        videoView.requestFocus();
    }
}
