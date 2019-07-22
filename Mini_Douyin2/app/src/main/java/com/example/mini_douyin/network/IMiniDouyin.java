package com.example.mini_douyin.network;

import com.example.mini_douyin.bean.FeedResponse;
import com.example.mini_douyin.bean.PostVideoResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface IMiniDouyin {
    String Host ="http://test.androidcamp.bytedance.com/";
    @Multipart
    @POST("mini_douyin/invoke/video")
    Call<PostVideoResponse> createVideo(
            @Query("student_id") String param1,
            @Query("user_name") String param2,
            @Part MultipartBody.Part image, @Part MultipartBody.Part video);

    @GET("mini_douyin/invoke/video")
    Call<FeedResponse> fetchFeed();
}
