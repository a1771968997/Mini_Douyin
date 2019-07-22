package com.example.mini_douyin;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class Update extends Fragment {


    public Update() {
        // Required empty public constructor
    }

    private ImageButton updateBtn,videoBtn;
    private static final int PICK_IMAGE = 1;
    private static final int PICK_VIDEO = 2;
    private static final int GRANT_PERMISSION = 3;
    private static final String TAG = "Solution2C2Activity";
    private static final int REQUEST_READ = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private Uri mSelectedImage;
    private Uri mSelectedVideo;
    private Uri a;
    private Uri b;
    private int updatestate = 0;
    private TextView update_text;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        updateBtn = view.findViewById(R.id.update_btn);
        videoBtn = view.findViewById(R.id.video_btn);
        update_text = view.findViewById(R.id.update_text_in_main);

        //videoBtn.setEnabled(false);
        initupdateButton();
        videoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA) !=PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.RECORD_AUDIO) !=PackageManager.PERMISSION_GRANTED)
                {
                    String [] permsion = {
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO
                    };
                    ActivityCompat.requestPermissions(getActivity(),permsion,2);
                }
                else
                    startActivity(new Intent(getActivity(), CameraVideo.class));
            }
        });
        return view;
    }

    private  void  initupdateButton()
    {
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)
                !=PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }
                else
                {
                    chooseUpdate();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_READ: {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(),"未授予权限",Toast.LENGTH_LONG);
                } else {
                   chooseUpdate();
                }
            }
            case REQUEST_EXTERNAL_STORAGE:
            {
                if(ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CAMERA) !=PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.RECORD_AUDIO) !=PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getContext(),"failure",Toast.LENGTH_LONG);
                }
                else
                {
                    startActivity(new Intent(getContext(), CameraVideo.class));
                }
            }
            default:
                break;
        }
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
            Log.d(TAG, "selectedImage = " + mSelectedImage);
            updatestate+=1;
            update_text.setText("选择视频");
        }
        else if(requestCode == PICK_VIDEO)
        {
            mSelectedVideo = data.getData();
            Log.d(TAG, "mSelectedVideo = " + mSelectedVideo);
            updatestate+=1;
            update_text.setText("点击按钮上传");
        }
        else ;
    }

    public void chooseUpdate()
    {
        switch (updatestate) {
            case 0: {
                chooseImage();
                break;
            }
            case 1: {
                chooseVideo();
                break;
            }
            case 2: {
                postVideo();
                break;
            }
            default:
                break;

        }
    }


    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        // if NullPointerException thrown, try to allow storage permission in system settings
        File f = new File(ResourceUtils.getRealPath(getContext(), uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }


    private void postVideo(){
        updateBtn.setEnabled(false);
        update_text.setText("上传中");
        Retrofit retrofit = RetrofitManager.get(IMiniDouyin.Host);
        IMiniDouyin postvideo = retrofit.create(IMiniDouyin.class);
        Call<PostVideoResponse> postVideoResponseCall = postvideo.createVideo("1120161958","wpc",
                getMultipartFromUri("cover_image",mSelectedImage),getMultipartFromUri("video",mSelectedVideo));
        postVideoResponseCall.enqueue(new Callback<PostVideoResponse>() {
            @Override
            public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {

                if(response.body().isSuccesss()&&response.body()!=null) {
                    Toast.makeText(getContext(), "success", Toast.LENGTH_LONG);
                    update_text.setText("上传成功");
                }
                else
                    Toast.makeText(getContext(),"failure",Toast.LENGTH_LONG);
                updateBtn.setEnabled(true);
            }

            @Override
            public void onFailure(Call<PostVideoResponse> call, Throwable t) {
                Toast.makeText(getContext(),"failure",Toast.LENGTH_LONG);
                updateBtn.setEnabled(true);
            }
        });
        updatestate = 0;
    }
}
