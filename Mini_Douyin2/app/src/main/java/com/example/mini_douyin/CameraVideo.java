package com.example.mini_douyin;


import android.content.Intent;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.hardware.Camera;
import android.widget.ImageView;

import java.io.IOException;

import static com.example.mini_douyin.utils.Utils.MEDIA_TYPE_VIDEO;
import static com.example.mini_douyin.utils.Utils.getOutputMediaFile;

public class CameraVideo extends AppCompatActivity {

    private SurfaceView mSurfaceView;
    private Camera mCamera;
    private ImageView imageView,faceimage;

    private int CAMERA_TYPE = android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;

    private boolean isRecording = false;

    private int rotationDegree = 0;
    private String video_file;
    private Uri uri;
    private String path;
    private static final int UPDATE_SUCCESS =1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);

        mSurfaceView = findViewById(R.id.sur_view);
        //todo 给SurfaceHolder添加Callback
        //实现摄像头数据实时显示
        mCamera = getCamera(CAMERA_TYPE);
        SurfaceHolder surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mCamera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCamera.startPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        });
        faceimage = findViewById(R.id.rotation);
        faceimage.setImageDrawable(getDrawable(R.drawable.ic_rotate_left_black_24dp));
        imageView = findViewById(R.id.camera_image);
        imageView.setImageDrawable(getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
        imageView.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                if(isRecording)
                {
                    releaseMediaRecorder();
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_play_circle_outline_black_24dp));
                }
                else {
                    isRecording = prepareVideoRecorder();
                    imageView.setImageDrawable(getDrawable(R.drawable.ic_pause_circle_outline_black_24dp));
                }
            }
        });
        faceimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.stopPreview();
                if(Camera.getNumberOfCameras()>1)
                {
                    switch (CAMERA_TYPE)
                    {
                        case Camera.CameraInfo.CAMERA_FACING_FRONT: {
                            mCamera=getCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
                            break;
                        }
                        case Camera.CameraInfo.CAMERA_FACING_BACK:{
                            mCamera=getCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
                            break;
                        }
                        default:
                            break;
                    }
                }
                try {
                    mCamera.setPreviewDisplay(mSurfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCamera.startPreview();
            }
        });

    }

    public android.hardware.Camera getCamera(int position) {
        CAMERA_TYPE = position;
        if (mCamera != null) {
            releaseCameraAndPreview();
        }
        android.hardware.Camera cam = android.hardware.Camera.open(position);

        //todo 摄像头添加属性，例是否自动对焦，设置旋转方向等
        //cam.cancelAutoFocus();
        cam.getParameters().setFlashMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        rotationDegree = getCameraDisplayOrientation(position);
        cam.setDisplayOrientation(rotationDegree);
        return cam;
    }

    private static final int DEGREE_90 = 90;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_270 = 270;
    private static final int DEGREE_360 = 360;

    private int getCameraDisplayOrientation(int cameraId) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = DEGREE_90;
                break;
            case Surface.ROTATION_180:
                degrees = DEGREE_180;
                break;
            case Surface.ROTATION_270:
                degrees = DEGREE_270;
                break;
            default:
                break;
        }

        int result;
        if (info.facing == android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % DEGREE_360;
            result = (DEGREE_360 - result) % DEGREE_360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + DEGREE_360) % DEGREE_360;
        }
        return result;
    }

    private void releaseCameraAndPreview() {
        //todo 释放camera资源
        if(mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
    }

    android.hardware.Camera.Size size;

    private void startPreview(SurfaceHolder holder) {
        //todo 开始预览
    }

    private MediaRecorder mMediaRecorder;

    private boolean prepareVideoRecorder() {
        //todo 准备MediaRecorder
        mCamera.unlock();
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        path =getOutputMediaFile(MEDIA_TYPE_VIDEO).getPath();
        uri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_VIDEO));

        mMediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
        //rotationDegree = getCameraDisplayOrientation(CAMERA_TYPE);
        mMediaRecorder.setOrientationHint(rotationDegree);
        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    private void releaseMediaRecorder() {
        //todo 释放MediaRecorder
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        mCamera.lock();
        isRecording = false;
        Intent intent = new Intent(CameraVideo.this,PreviewVideo.class);
        intent.putExtra("video_url",uri);
        intent.putExtra("path",path);
        intent.setData(uri);
        startActivityForResult(intent,UPDATE_SUCCESS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPDATE_SUCCESS){
            CameraVideo.this.finish();
        }
    }
}

