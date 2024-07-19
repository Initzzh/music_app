package com.example.music_zhangzhenghuan.adatper;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_zhangzhenghuan.R;
import com.example.music_zhangzhenghuan.entity.MusicInfo;

import java.util.List;

public class MusicListRecycleAdapter extends RecyclerView.Adapter<MusicListRecycleAdapter.ViewHolder>{


    private List<MusicInfo> curMusicInfoList;
    private int selectedPositon;

    public  MusicListRecycleAdapter(List<MusicInfo> curMusicInfoList){
        this.curMusicInfoList = curMusicInfoList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListRecycleAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        MusicInfo musicInfo = curMusicInfoList.get(position);
        holder.musicName.setText(musicInfo.getMusicName());
        holder.musicAuthor.setText("·"+musicInfo.getAuthor());

        final int colorGray = ContextCompat.getColor(holder.itemView.getContext(), R.color.gray);
        final int colorWhite =  ContextCompat.getColor(holder.itemView.getContext(), R.color.white);

        if(selectedPositon == position){
            holder.itemView.setBackgroundColor(colorGray);
            holder.musicName.setTextColor(holder.itemView.getResources().getColor(R.color.purple));
            holder.musicAuthor.setTextColor(holder.itemView.getResources().getColor(R.color.light_purple));
        }else{
            holder.itemView.setBackgroundColor(colorWhite);
            holder.musicName.setTextColor(holder.itemView.getResources().getColor(R.color.black));
            holder.musicAuthor.setTextColor(holder.itemView.getResources().getColor(R.color.text_gray));
        }

        holder.musicRemoveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curMusicInfoList.remove(position);
                notifyItemRemoved(position);
                if(position < getItemCount()){
                    notifyItemRangeChanged(position, getItemCount());
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(selectedPositon != position){
                    notifyItemChanged(selectedPositon);
                    selectedPositon = position;
                    notifyItemChanged(position);
                }

                ColorDrawable background = (ColorDrawable) holder.itemView.getBackground();
                if(background.getColor() == colorWhite){
                    holder.itemView.setBackgroundColor(colorGray);
                    holder.musicName.setTextColor(holder.itemView.getResources().getColor(R.color.purple));
                    holder.musicAuthor.setTextColor(holder.itemView.getResources().getColor(R.color.light_purple));

                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return curMusicInfoList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView  musicName;
        private TextView  musicAuthor;
        private ImageView musicRemoveImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            musicName = itemView.findViewById(R.id.music_name);
            musicAuthor = itemView.findViewById(R.id.music_author);

            musicRemoveImage = itemView.findViewById(R.id.music_remove);
        }
    }
}
