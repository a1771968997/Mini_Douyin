package com.example.mini_douyin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mini_douyin.bean.Feed;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView textView;

    VideoViewHolder(@NonNull View view)
    {
        super(view);
        imageView = view.findViewById(R.id.image_cover);
        textView = view.findViewById(R.id.name);
    }

    void  bind(Feed feed)
    {
        Glide.with(imageView.getContext()).load(feed.getImage_url()).into(imageView);
        textView.setText(feed.getUser_name());
    }


}
