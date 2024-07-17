package com.example.music_zhangzhenghuan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IndexActivity extends AppCompatActivity {



    private RecyclerView recyclerBanner;
    private BannerAdapter bannerAdapter;

    private RecyclerView excellentSongView;

    private RecycleAdapter recycleAdapter;

    private RecyclerView recommandRecyclerView;

    private RecycleAdapter recommandAdapter;

    private RecyclerView hotRecyclerView;

    private RecycleAdapter hotAdapter;


    private OkHttpClient client;


    private List<HomePageInfo> homePageInfos;

    private List<MusicInfo> bannerDataList;

    private List<MusicInfo> excellentSongList;

    private List<MusicInfo> recommandSongList;

    private List<MusicInfo> hotSongList;

    private List<Integer> bannerImg;

    private Handler mhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);


        // 轮播图
        recyclerBanner = findViewById(R.id.banner);
        recyclerBanner.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        bannerDataList  = new ArrayList<>();
        bannerAdapter = new BannerAdapter(R.layout.music_item,bannerDataList);

//        recyclerBanner.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
//
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });

        // 专属好歌RecycleView
        recyclerBanner.setAdapter(bannerAdapter);
        excellentSongView = findViewById(R.id.excell_song);
        excellentSongView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        excellentSongList = new ArrayList<>();
        recycleAdapter= new RecycleAdapter(R.layout.music_item,excellentSongList);
        excellentSongView.setAdapter(recycleAdapter);

        // 每日推荐
        recommandSongList = new ArrayList<>();
        recommandRecyclerView = findViewById(R.id.recommand_song);
        recommandRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recommandAdapter = new RecycleAdapter(R.layout.music_item,recommandSongList);
        recommandRecyclerView.setAdapter(recycleAdapter);

        // 热门金曲

        hotSongList = new ArrayList<>();
        hotRecyclerView = findViewById(R.id.hot_song);
        hotRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        hotAdapter = new RecycleAdapter(R.layout.music_item, hotSongList);
        hotRecyclerView.setAdapter(hotAdapter);

        musicHomePage(1,4);
        mhandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                bannerAdapter.notifyDataSetChanged();
                recycleAdapter.notifyDataSetChanged();
                recommandAdapter.notifyDataSetChanged();
                hotAdapter.notifyDataSetChanged();
            }
        };
    }

    private void musicHomePage(int currentPage, int pageSize){
//        https://hotfix-service-prod.g.mi.com/music/homePage?current=1&size=4

        client = new OkHttpClient.Builder().build();
        String urlPre=  "https://hotfix-service-prod.g.mi.com/music/homePage?";
        String url = urlPre + "current=" + currentPage + "&size="+ pageSize;

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "onFailure: 网络请求失败");
                Toast.makeText(getApplicationContext(), "网络请求失败！",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    List<HomePageInfo> homePageInfos = parseJsonWithObject(response);
                    List<MusicInfo> curBannerList = homePageInfos.get(0).getMusicInfoList();
                    List<MusicInfo> curExcellentSongList = homePageInfos.get(1).getMusicInfoList();
                    List<MusicInfo> curReommandSongList = homePageInfos.get(2).getMusicInfoList();
                    List<MusicInfo> curHotSongList = homePageInfos.get(3).getMusicInfoList();
                    bannerDataList.addAll(curBannerList);
                    excellentSongList.addAll(curExcellentSongList);
                    recommandSongList.addAll(curReommandSongList);
                    hotSongList.addAll(curHotSongList);

                    mhandler.sendEmptyMessage(0);


                }

            }
        });


    }

    private List<HomePageInfo> parseJsonWithObject(Response response) throws IOException{
        String responseData = response.body().string();
        Gson gson = new Gson();
        HomPageResponse homPageResponse = gson.fromJson(responseData, HomPageResponse.class);
        List<HomePageInfo>  data = homPageResponse.getData().getRecords();
        return data;
    }
}