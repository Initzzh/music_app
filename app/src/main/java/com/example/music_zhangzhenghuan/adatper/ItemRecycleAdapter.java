package com.example.music_zhangzhenghuan.adatper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.music_zhangzhenghuan.activity.MusicPlayActivity;
import com.example.music_zhangzhenghuan.entity.MusicEvent;
import com.example.music_zhangzhenghuan.entity.MusicInfo;
import com.example.music_zhangzhenghuan.R;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemRecycleAdapter extends RecyclerView.Adapter<ItemRecycleAdapter.ViewHolder>{

    private List<MusicInfo> musicInfoList;

    private final int MSG_ADD_MUSIC = 0;

    private final int MSG_CLICK_ITEM = 4;

    private final int MSG_PLAY_MUSIC = 3;


    private List<MusicInfo> playMusicList;



    private int layoutId;



    public ItemRecycleAdapter(List<MusicInfo> musicInfoList, int layoutId) {
        this.musicInfoList = musicInfoList;
        this.layoutId = layoutId;
    }
    @NonNull
    @Override
    public ItemRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRecycleAdapter.ViewHolder holder, int position) {
        MusicInfo musicInfo = musicInfoList.get(position);

        String coverUrl = musicInfo.getCoverUrl();
        String httpsUrl = coverUrl.replace("http://", "https://");

        RequestOptions requestOptions = new RequestOptions()
                .transform(new CenterCrop(), new RoundedCorners(20));
        Glide.with(holder.itemView.getContext()).load(httpsUrl).apply(requestOptions).into(holder.musicCoverImage);

        holder.musicName.setText(musicInfo.getMusicName());
        holder.musicAuthor.setText(musicInfo.getAuthor());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(holder.itemView.getContext(), musicInfo.getMusicName(), Toast.LENGTH_SHORT).show();

                List<MusicInfo> remainPlayMusicList = new ArrayList<>();
                MusicInfo curMusicInfo = musicInfoList.get(position);
                for(int i=0; i<musicInfoList.size(); ++i){
                    if(i != position){
                        remainPlayMusicList.add(musicInfoList.get(i));
                    }
                }
                MusicEvent musicEvent = new MusicEvent(MSG_CLICK_ITEM,curMusicInfo, remainPlayMusicList );
                EventBus.getDefault().postSticky(musicEvent);
//                Intent musicPlayIntent = new Intent(holder.itemView.getContext(), MusicPlayActivity.class);
//                Serializable musicInfoListSerializable = (Serializable) playMusicList;
//                musicPlayIntent.putExtra("playMusicList", musicInfoListSerializable);
//
//                holder.itemView.getContext().startActivity(musicPlayIntent);

            }
        });

        holder.playImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicEvent musicEvent = new MusicEvent(MSG_PLAY_MUSIC,musicInfo);
                List<MusicInfo> playMusicList = new ArrayList<>();
                playMusicList.add(musicInfoList.get(position));
                for(int i = 0; i < musicInfoList.size(); i++){
                    if(i!=position){
                        playMusicList.add(musicInfoList.get(i));
                    }
                }
                musicEvent.setPlayMusicInfoList(musicInfoList);
                EventBus.getDefault().postSticky(musicEvent);
                Toast.makeText(holder.itemView.getContext(), "播放"+musicInfo.getMusicName(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.textAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new MusicEvent(MSG_ADD_MUSIC, musicInfo));

                Toast.makeText(holder.itemView.getContext(), "将"+musicInfo.getMusicName()+"添加到音乐列表", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView musicCoverImage;
        TextView musicName;
        TextView musicAuthor;

        ImageView playImage;
        TextView textAdd;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            musicCoverImage =  itemView.findViewById(R.id.music_cover_image);
            musicName = itemView.findViewById(R.id.music_name);
            musicAuthor = itemView.findViewById(R.id.music_author);
            playImage = itemView.findViewById(R.id.image_play);
            textAdd = itemView.findViewById(R.id.add);

        }
    }
}
