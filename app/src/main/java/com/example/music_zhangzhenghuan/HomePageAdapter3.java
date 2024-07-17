//package com.example.music_wumei.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.viewpager2.widget.ViewPager2;
//import com.example.music_wumei.R;
//import com.example.music_wumei.info.HomePageInfo;
//import com.example.music_wumei.info.MusicInfo;
//import java.util.ArrayList;
//import java.util.List;
//
//public class HomePageAdapter3 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private List<HomePageInfo> dataList = new ArrayList<>();
//
//
//    public HomePageAdapter3(List<HomePageInfo> dataList) {
//        this.dataList = dataList;
//    }
//
//
//    @Override
//    public int getItemViewType(int position) {//position 列表中项的位置
//        return dataList.get(position).getStyle();
//    }
//
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//
//        switch (viewType) {
//            case 1:
//                return new BannerViewHolder(inflater.inflate(R.layout.item_banner, parent, false));
//            case 2:
//                return new MusicCardViewHolder(inflater.inflate(R.layout.item_music_card, parent, false));
//            case 3:
//                return new OneColumnViewHolder(inflater.inflate(R.layout.item_one_column, parent, false));
//            case 4:
//                return new TwoColumnViewHolder(inflater.inflate(R.layout.item_two_column, parent, false));
//            default:
//                return new MusicCardViewHolder(inflater.inflate(R.layout.item_music_card, parent, false));
//        }
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {//绑定数据到视图
//        HomePageInfo item = dataList.get(position);
//        if (item != null) {
//            switch (holder.getItemViewType()) {
//                case 1:
//                    ((BannerViewHolder) holder).bind(item.getMusicInfoList());//banner
//                    break;
//                case 2:
//                    ((MusicCardViewHolder) holder).bind(item.getMusicInfoList());//musicCard
//                    break;
//                case 3:
//                    ((OneColumnViewHolder) holder).bind(item);//oneColumn
//                    break;
//                case 4:
//                    ((TwoColumnViewHolder) holder).bind(item);//twoColumn
//                    break;
//            }
//        }
//    }
//
//
//    @Override
//    public int getItemCount() {//多少
//        return dataList.size();
//    }
//
//
//    public class BannerViewHolder extends RecyclerView.ViewHolder {
//        private ViewPager2 viewPager;
////        private LinearLayout indicatorLayout;
//
//
//        public BannerViewHolder(View itemView) {
//            super(itemView);
//            viewPager = itemView.findViewById(R.id.bannerViewPager);
////            indicatorLayout = itemView.findViewById(R.id.indicatorLayout);
//        }
//
//        public void bind(List<MusicInfo> bannerList) {
//            BannerAdapter bannerAdapter = new BannerAdapter(bannerList, viewPager);
//            viewPager.setAdapter(bannerAdapter);
//        }
//    }
//
//
//    public class MusicCardViewHolder extends RecyclerView.ViewHolder {
//        private ViewPager2 viewPager;
////        private LinearLayout indicatorLayout;
//
//        public MusicCardViewHolder(View itemView) {
//            super(itemView);
//            viewPager = itemView.findViewById(R.id.musicCardViewPager);
////            indicatorLayout = itemView.findViewById(R.id.indicatorLayout2);
//        }
//
//        public void bind(List<MusicInfo> musicList) {
//            MusicCardAdapter musicCardAdapter = new MusicCardAdapter(musicList);
//            viewPager.setAdapter(musicCardAdapter);
//        }
//    }
//
//
//    public class OneColumnViewHolder extends RecyclerView.ViewHolder {//单列
//        private RecyclerView recyclerView;
//
//        public OneColumnViewHolder(View itemView) {
//            super(itemView);
//            recyclerView = itemView.findViewById(R.id.oneColumnRecyclerView);
//        }
//
//        public void bind(HomePageInfo homePageInfo) {
//            List<MusicInfo> musicInfoList = homePageInfo.getMusicInfoList();
//            recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(),1));//1列
//            recyclerView.setAdapter(new MusicImageAdapter(musicInfoList, 1));//1表示1列
//        }
//    }
//
//
//    public class TwoColumnViewHolder extends RecyclerView.ViewHolder {
//        private RecyclerView recyclerView;
//
//
//        public TwoColumnViewHolder(View itemView) {//构造函数
//            super(itemView);
//            recyclerView = itemView.findViewById(R.id.twoColumnRecyclerView);
//        }
//
//        public void bind(HomePageInfo homePageInfo) {
//            recyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(), 2));
//            recyclerView.setAdapter(new MusicImageAdapter(homePageInfo.getMusicInfoList(), 2));//两列
//        }
//    }
//}
