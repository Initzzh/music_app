package com.example.music_zhangzhenghuan.adatper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.music_zhangzhenghuan.entity.MusicInfo;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;

import java.util.List;

public class BannerAdapter2 extends BannerImageAdapter<MusicInfo>{
    public BannerAdapter2(List<MusicInfo> mData) {
        super(mData);
    }

    @Override
    public void onBindView(BannerImageHolder holder, MusicInfo musicInfo, int position, int size) {
        String coverUrl = musicInfo.getCoverUrl();
        String httpsUrl = coverUrl.replace("http://", "https://");
        RequestOptions requestOptions = new RequestOptions()
                .transform(new CenterCrop(), new RoundedCorners(20));

        Glide.with(holder.itemView.getContext()).load(httpsUrl).apply(requestOptions).into(holder.imageView);
    }
}