package com.example.mini_douyin;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.mini_douyin.bean.PostVideoResponse;
import com.example.mini_douyin.network.IMiniDouyin;
import com.example.mini_douyin.network.RetrofitManager;
import com.example.mini_douyin.utils.ResourceUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PreviewVideo extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;
    private ImageView upBtm,delBtn;
    private Uri uri;
    private int state =0;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_VIDEO = 2;
    private static final int UPDATE_SUCCESS =1;
    private Uri mSelectedImage;
    private Uri mSelectedVideo;
    private TextView update_text,return_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_video);

        upBtm = findViewById(R.id.up_Btn);
        delBtn = findViewById(R.id.del_Btn);
        update_text=findViewById(R.id.update_text);

        videoView = findViewById(R.id.pre_view);
        mediaController = new MediaController(this);
        String video_url= getIntent().getStringExtra("video_url");
        uri = getIntent().getData();
        System.out.println(uri);
        //videoView.setVideoPath(video_url);
        videoView.setVideoURI(uri);
        videoView.setMediaController(mediaController);
        videoView.start();
        videoView.requestFocus();
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreviewVideo.this.finish();
            }
        });
        upBtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (state)
                {
                    case 0:
                    {
                        chooseImage();
                        break;
                    }
                    case 1:
                    {
                        postVideo();
                        break;
                    }
                }
            }
        });
    }

    public void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
    }

    public void chooseVideo()
    {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE){
            mSelectedImage = data.getData();
            //Log.d(TAG, "selectedImage = " + mSelectedImage);
            //System.out.println(mSelectedImage);
            state+=1;
            update_text.setText("上传视频");

        }
        else if(requestCode == PICK_VIDEO)
        {
            mSelectedVideo = data.getData();
            state+=1;
            //System.out.println(mSelectedVideo);
        }
        else ;
    }

    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        // if NullPointerException thrown, try to allow storage permission in system settings
        File f = new File(ResourceUtils.getRealPath(getApplicationContext(), uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }


    private void postVideo(){
        upBtm.setEnabled(false);
        update_text.setText("上传中");
        Retrofit retrofit = RetrofitManager.get(IMiniDouyin.Host);
        IMiniDouyin postvideo = retrofit.create(IMiniDouyin.class);
        Call<PostVideoResponse> postVideoResponseCall = postvideo.createVideo("1120161958","wpc",
                getMultipartFromUri("cover_image",mSelectedImage),getMultipartFromUri("video",uri));
        postVideoResponseCall.enqueue(new Callback<PostVideoResponse>() {
            @Override
            public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {

                if(response.body().isSuccesss()&&response.body()!=null) {
                    update_text.setText("上传成功");
                    setResult(UPDATE_SUCCESS);
                }
                else
                    Toast.makeText(getApplicationContext(),"failure",Toast.LENGTH_LONG);
                upBtm.setEnabled(true);
            }

            @Override
            public void onFailure(Call<PostVideoResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"failure",Toast.LENGTH_LONG);
                upBtm.setEnabled(true);
            }
        });
        state = 0;
    }
}
