//package com.example.music_wumei.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.bitmap.CenterCrop;
//import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
//import com.bumptech.glide.request.RequestOptions;
//import com.example.music_wumei.R;
//import com.example.music_wumei.info.MusicInfo;
//
//import java.util.List;
//
//public class MusicImageAdapter extends RecyclerView.Adapter<MusicImageAdapter.MusicImageViewHolder> {
//    private List<MusicInfo> musicInfoList;
//    private int num;
//
//    public MusicImageAdapter(List<MusicInfo> musicInfoList, int num) {
//        this.musicInfoList = musicInfoList;
//        this.num = num;
//    }
//
//    @NonNull
//    @Override
//    public MusicImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_page, parent, false);
//        return new MusicImageViewHolder(view);
//    }
//
//
//
//    @Override
//    public void onBindViewHolder(@NonNull MusicImageViewHolder holder, int position) {
//        int widthInPx = 0;
//        int heightInPx = 0;
//        if (num == 2) {
//            widthInPx = dpToPx(195, holder.itemView.getContext());
//            heightInPx = dpToPx(101, holder.itemView.getContext());
//        } else {
//            widthInPx = dpToPx(390, holder.itemView.getContext());
//            heightInPx = dpToPx(144, holder.itemView.getContext());
//        }
//
//        MusicInfo musicInfo = musicInfoList.get(position);
//        String url = secureUrl(musicInfo.getCoverUrl());
//
//        RequestOptions requestOptions = new RequestOptions()
//                .override(widthInPx, heightInPx) // 指定裁剪后的宽度和高度
//                .transform(new CenterCrop(), new RoundedCorners(30));
//
//        // 加载图片并应用圆角处理
//        Glide.with(holder.itemView.getContext())
//                .load(url)
//                .apply(requestOptions)
//                .into(holder.musicCoverImageView);
//
//        holder.musicNameTextView.setText(musicInfo.getMusicName());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 点击事件处理
//            }
//        });
//        holder.musicCoverImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 点击事件处理
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return musicInfoList.size();
//    }
//
//    public class MusicImageViewHolder extends RecyclerView.ViewHolder {
//        private ImageView musicCoverImageView;
//        private TextView musicNameTextView;
//
//        public MusicImageViewHolder(View itemView) {
//            super(itemView);
//            musicCoverImageView = itemView.findViewById(R.id.musicCoverImageView);
//            musicNameTextView = itemView.findViewById(R.id.musicNameTextView);
//        }
//    }
//
//    // 将 dp 转换为 px 的方法
//    private int dpToPx(int dp, Context context) {
//        return (int) (dp * context.getResources().getDisplayMetrics().density);
//    }
//
//    // 确保安全的 URL 方法
//    private String secureUrl(String url) {
//        if (url != null && url.startsWith("http://")) {
//            return url.replace("http://", "https://");
//        }
//        return url; // 返回原始 URL，如果它已经是 HTTPS 或者是 null
//    }
//}
