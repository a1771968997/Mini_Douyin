package com.example.mini_douyin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mini_douyin.bean.Feed;
import com.example.mini_douyin.bean.FeedResponse;
import com.example.mini_douyin.network.IMiniDouyin;
import com.example.mini_douyin.network.RetrofitManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class VideoList extends Fragment {

    private RecyclerView recyclerView;
    private List<Feed> mFeeds = new ArrayList<>();
    private ImageView imageView;
    private TextView id;
    private TextView name;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videolist,container,false);
        recyclerView = view.findViewById(R.id.recycler_view);
        initRecyclerView();
        fetchFeed(view);
        return view;
    }

    private void  initRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
                return new MyViewHolder(v);
            }

            @NonNull

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                Random random = new Random();
                final int ranndnum = random.nextInt(mFeeds.size()-1);
                String url = mFeeds.get(ranndnum).getImage_url();
                Glide.with(viewHolder.itemView.getContext()).load(url).into(imageView);
                //id.setText(mFeeds.get(ranndnum).getCreateAt());
                //System.out.println(ranndnum);
                //System.out.println(mFeeds.get(i).getCreateAt());
                name.setText(mFeeds.get(ranndnum).getUser_name());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),VideoPlayer.class);
                        intent.putExtra("url",mFeeds.get(ranndnum).getVideo_url());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public int getItemCount() {
                return mFeeds.size();
            }
        });
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_cover);
            //id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
        }
    }

    public void  fetchFeed(View view)
    {
        Retrofit retrofit = RetrofitManager.get(IMiniDouyin.Host);
        IMiniDouyin iMiniDouyinService = retrofit.create(IMiniDouyin.class);
        Call<FeedResponse> feedResponseCall = iMiniDouyinService.fetchFeed();
        feedResponseCall.enqueue(new Callback<FeedResponse>() {
            @Override
            public void onResponse(Call<FeedResponse> call, Response<FeedResponse> response) {
                if(response.body().isSuccess()) {
                    mFeeds = response.body().getFeeds();
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<FeedResponse> call, Throwable t) {
            }
        });
    }

}
