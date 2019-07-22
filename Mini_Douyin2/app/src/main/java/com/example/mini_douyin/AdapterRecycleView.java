package com.example.mini_douyin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.example.mini_douyin.bean.Feed;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AdapterRecycleView extends RecyclerView.Adapter<VideoViewHolder>{

    private OnItemClickListener listener;
    private List<Feed> feeds = new ArrayList<>();

    public AdapterRecycleView(OnItemClickListener listener){
        this.listener=  listener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item,viewGroup,false);
        return new VideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoViewHolder videoViewHolder, int i) {
        videoViewHolder.bind(feeds.get(i));
        videoViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                int pos = videoViewHolder.getLayoutPosition();
                listener.onItemClick(videoViewHolder.itemView,pos);
            }
        });
    }


    @Override
    public int getItemCount() {
        return feeds.size();
    }

    public void refresh(List<Feed> videoList){
        feeds.clear();
        if(videoList!=null){
            Random r=new Random();
            int showSize=Math.min(50,videoList.size());
            for(int i=0;i<showSize;i++){
                feeds.add(videoList.get(i/*r.nextInt(videoList.size())*/));
            }
        }
        notifyDataSetChanged();
    }


    public Feed getFeed(int position)
    {
        return feeds.get(position);
    }

}
