package com.example.music_zhangzhenghuan.adatper;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.music_zhangzhenghuan.entity.HomePageInfo;
import com.example.music_zhangzhenghuan.entity.MusicInfo;
import com.example.music_zhangzhenghuan.R;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.util.List;


public class IndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<HomePageInfo> homePageInfos;

    private List<MusicInfo> playMusicList;


    public List<HomePageInfo> getHomePageInfos() {
        return homePageInfos;
    }

    public void setHomePageInfos(List<HomePageInfo> homePageInfos) {
        this.homePageInfos = homePageInfos;
    }

    public IndexAdapter(List<HomePageInfo> homePageInfos, List<MusicInfo> playMusicList){

        this.homePageInfos = homePageInfos;
        this.playMusicList = playMusicList;
    }

    @Override
    public int getItemViewType(int position) {

        return homePageInfos.get(position).getStyle();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =  LayoutInflater.from(parent.getContext());

        switch (viewType){
            case 1:
                Log.d("TAG", "onCreateViewHolder: ");
                return new BannerViewHolder(inflater.inflate(R.layout.banner_layout, parent,false));
//                return new ExcellentViewHolder(inflater.inflate(R.layout.excellent_layout, parent,  false));
            case 2:
                Log.d("TAG", "onCreateViewHolder: ");
                return new ExcellentViewHolder(inflater.inflate(R.layout.excellent_layout, parent,  false));
            case 3:
                Log.d("TAG", "onCreateViewHolder: ");
                return new RecommandViewHolder(inflater.inflate(R.layout.recommand_layout,parent,false));
            case 4:
                Log.d("TAG", "onCreateViewHolder: ");
                return new HotViewHolder(inflater.inflate(R.layout.hot_layout,parent,false));
            default:
                Log.d("TAG", "onCreateViewHolder: ");
                return new BannerViewHolder(inflater.inflate(R.layout.banner_layout, parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)){
            case 1:
                Log.i("TAG", "onBindViewHolder: ");
                ((BannerViewHolder) holder).bind(homePageInfos.get(position).getMusicInfoList());
                break;
            case 2:
                Log.i("TAG", "onBindViewHolder: ");
                ((ExcellentViewHolder) holder).bind(homePageInfos.get(position).getMusicInfoList());
                break;
            case 3:
                Log.i("TAG", "onBindViewHolder: ");
                ((RecommandViewHolder) holder).bind(homePageInfos.get(position).getMusicInfoList());
                break;
            case 4:
                Log.i("TAG", "onBindViewHolder: ");
                ((HotViewHolder) holder).bind(homePageInfos.get(position).getMusicInfoList());
                break;
        }

    }



    @Override
    public int getItemCount() {
        return homePageInfos.size();
    }


    public class BannerViewHolder extends RecyclerView.ViewHolder{
//        private ViewPager2 bannerViewPager;

        private Banner banner;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            banner =itemView.findViewById(R.id.banner);
        }
        public void bind(List<MusicInfo> musicInfoList){
            banner.setAdapter(new BannerAdapter2(musicInfoList));
            banner.isAutoLoop(true);
            banner.setIndicator(new CircleIndicator(itemView.getContext()));
            banner.setIndicatorSelectedColor(Color.WHITE);
            banner.start();
//            bannerViewPager.setAdapter(new BannerAdapter(musicInfoList, bannerViewPager));
        }
    }

    public class ExcellentViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView excellentRecycle;
        public ExcellentViewHolder(@NonNull View itemView) {
            super(itemView);
            excellentRecycle = itemView.findViewById(R.id.excellent_recycle);
        }

        public void bind(List<MusicInfo> musicInfoList){
            excellentRecycle.setLayoutManager(new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false));
            excellentRecycle.setAdapter(new ItemRecycleAdapter(musicInfoList, R.layout.music_item));
        }


    }

    public class RecommandViewHolder extends RecyclerView.ViewHolder{
        private RecyclerView recommandRecycle;
        public RecommandViewHolder(@NonNull View itemView) {
            super(itemView);
            recommandRecycle = itemView.findViewById(R.id.recommand_recyle);
        }

        public void bind(List<MusicInfo> musicInfoList){
            recommandRecycle.setLayoutManager(new GridLayoutManager(itemView.getContext(), 1));
            recommandRecycle.setAdapter(new ItemRecycleAdapter(musicInfoList, R.layout.music_item));


        }

    }

    public class HotViewHolder extends RecyclerView.ViewHolder{

        private RecyclerView hotRecycle;
        public HotViewHolder(@NonNull View itemView) {
            super(itemView);
            hotRecycle = itemView.findViewById(R.id.hot_recycle);
        }

        public void bind(List<MusicInfo> musicInfoList){
            hotRecycle.setLayoutManager(new GridLayoutManager(itemView.getContext(),2));
            hotRecycle.setAdapter(new ItemRecycleAdapter(musicInfoList, R.layout.music_item_two));

        }
    }




}
//
//public class IndexAdapter extends BaseMultiItemQuickAdapter<MultiContent, BaseViewHolder>{
//    public IndexAdapter(List<MultiContent> data)
//    {
//        super(data);
//        addItemType(MultiContent.TYPE_ZERO, R.layout.banner_layout);
//        addItemType(MultiContent.TYPE_ONE, R.layout.excellent_layout);
//        addItemType(MultiContent.TYPE_TWO, R.layout.recommand_layout);
//        addItemType(MultiContent.TYPE_TREE, R.layout.hot_layout);
//    }
//
//
//    @Override
//    protected void convert(@NonNull BaseViewHolder helper, MultiContent multiContent) {
//        switch (helper.getItemViewType()){
//            case MultiContent.TYPE_ZERO:
//                setBannerRecycle(helper, multiContent);
//                break;
//            case MultiContent.TYPE_ONE:
//                setExcellentRecycle(helper, multiContent);
//            case MultiContent.TYPE_TWO:
//                setRecommandRecycle(helper, multiContent);
//            case MultiContent.TYPE_TREE:
//                setHotRecycle(helper, multiContent);
//
//        }
//
//    }
//
//
//    public void setBannerRecycle(BaseViewHolder helper, MultiContent content){
//        RecyclerView bannerRecycle = helper.getView(R.id.banner_recycle);
//        bannerRecycle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
//        bannerRecycle.setAdapter(new RecycleAdapter(R.layout.banner_layout, content.getMusicInfoList()));
//    }
//    public void setRecommandRecycle(BaseViewHolder helper, MultiContent content){
//        RecyclerView recommandRecycle = helper.getView(R.id.recommand_recyle);
//        recommandRecycle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
//        recommandRecycle.setAdapter(new RecycleAdapter(R.layout.recommand_layout, content.getMusicInfoList()));
//    }
//
//    public void setExcellentRecycle(BaseViewHolder helper, MultiContent content){
//        RecyclerView excellentRecycle = helper.getView(R.id.excellent_recycle);
//        excellentRecycle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
//        excellentRecycle.setAdapter(new RecycleAdapter(R.layout.excellent_layout, content.getMusicInfoList()));
//    }
//
//    public void setHotRecycle(BaseViewHolder helper, MultiContent content){
//        RecyclerView hotRecycle = helper.getView(R.id.hot_recycle);
//        hotRecycle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
//        hotRecycle.setAdapter(new RecycleAdapter(R.layout.hot_layout, content.getMusicInfoList()));
//    }
//
//
//}
//
