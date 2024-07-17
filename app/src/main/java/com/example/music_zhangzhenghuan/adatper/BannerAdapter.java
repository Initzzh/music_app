package com.example.music_zhangzhenghuan.adatper;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.music_zhangzhenghuan.MusicInfo;
import com.example.music_zhangzhenghuan.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    private List<MusicInfo> musicInfoList;
    private ViewPager2 viewPager;

    private RequestOptions requestOptions;

    private Timer timer;

    public BannerAdapter(List<MusicInfo> musicInfoList, ViewPager2 viewPager){
        this.musicInfoList = musicInfoList;
        this.viewPager = viewPager;
        // 开始自动轮播
        startAutoScroll();

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_page, parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicInfo musicInfo = musicInfoList.get(position % musicInfoList.size()); // 实现循环滚动
        String coverUrl = musicInfo.getCoverUrl();
        String httpsUrl = coverUrl.replace("http://", "https://");
        requestOptions = new RequestOptions()
                .transform(new CenterCrop(), new RoundedCorners(20));

        Glide.with(holder.itemView.getContext()).load(httpsUrl).apply(requestOptions).into(holder.musicCoverImage);
//        Log.i("TAG", "onBindViewHolder: ");
//        holder.musicName.setText(musicInfoList.get(position).getMusicName());
//        holder.musicAuthor.setText(musicInfoList.get(position).getAuthor());

    }



    // 开始轮播
    private void startAutoScroll() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }
                });
            }
        }, 3000, 3000); // 设置轮播间隔为3秒
    }

    // 停止自动轮播
    public void stopAutoScroll() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
    }


    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE; // 无限轮播
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView musicCoverImage;

        public ViewHolder(View itemView)
        {
            super(itemView);
            musicCoverImage =  itemView.findViewById(R.id.banner_image_view);
//            musicName = itemView.findViewById(R.id.music_name);
//            musicAuthor = itemView.findViewById(R.id.music_author);
        }
    }


}
