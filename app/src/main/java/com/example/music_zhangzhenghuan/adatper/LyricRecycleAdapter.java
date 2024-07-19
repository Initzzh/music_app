package com.example.music_zhangzhenghuan.adatper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_zhangzhenghuan.R;
import com.example.music_zhangzhenghuan.RecycleAdapter;
import com.example.music_zhangzhenghuan.entity.Lyric;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LyricRecycleAdapter extends RecyclerView.Adapter<LyricRecycleAdapter.ViewHolder> {


    private List<Lyric> lyricList;

    private int hightPosition;

    public LyricRecycleAdapter( List<Lyric> lyricList){
        this.lyricList = lyricList;
    }

    public void setHightPosition(int hightPosition){
        this.hightPosition = hightPosition;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public LyricRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyric_layout, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull LyricRecycleAdapter.ViewHolder holder, int position) {

        Lyric lyric = lyricList.get(position);
        holder.lyricText.setText(lyric.getContent());
        if(position == hightPosition){
            holder.lyricText.setTextColor(holder.itemView.getResources().getColor(R.color.white));
        }else {
            holder.lyricText.setTextColor(holder.itemView.getResources().getColor(R.color.text_gray));
        }
    }

    @Override
    public int getItemCount() {
        return lyricList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView  lyricText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lyricText = itemView.findViewById(R.id.lyric_text);
        }
    }
}
