package com.example.music_zhangzhenghuan.adatper;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.music_zhangzhenghuan.entity.MusicInfo;
import com.example.music_zhangzhenghuan.R;

import java.util.List;

public class RecycleAdapter extends BaseQuickAdapter<MusicInfo, BaseViewHolder> {

    public RecycleAdapter(int layoutResId, @NonNull List<MusicInfo> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(@NonNull BaseViewHolder helper, MusicInfo musicInfo) {
        helper.setText(R.id.music_name,musicInfo.getMusicName());
        helper.setText(R.id.music_author,musicInfo.getAuthor());
        ImageView musicImage = helper.getView(R.id.music_cover_image);
        String url = musicInfo.getCoverUrl();
        String httpsUrl = url.replace("http://", "https://");

        RequestOptions requestOptions = new RequestOptions()
                .override(390, 101) // 指定裁剪后的宽度和高度
                .transform(new CenterCrop(), new RoundedCorners(20));
        Glide.with(helper.itemView.getContext())
                .load(httpsUrl).apply(requestOptions)
                .into(musicImage);
        System.out.println(musicInfo.getCoverUrl());
    }
}
