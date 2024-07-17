package com.example.music_zhangzhenghuan;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;

import java.util.List;

public class BannerAdapter extends BaseQuickAdapter<MusicInfo, BaseViewHolder>{
    public BannerAdapter(int layoutResId, @Nullable List<MusicInfo> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, MusicInfo musicInfo) {

        helper.setText(R.id.music_name,musicInfo.getMusicName());
        helper.setText(R.id.music_singer,musicInfo.getAuthor());
        ImageView musicImage = helper.getView(R.id.music_cover_image);
        String url = musicInfo.getCoverUrl();
        String httpsUrl = url.replace("http://", "https://");
        Glide.with(helper.itemView.getContext())
                .load(httpsUrl)
                .into(musicImage);

    }
}
