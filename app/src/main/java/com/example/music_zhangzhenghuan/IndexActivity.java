package com.example.music_zhangzhenghuan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IndexActivity extends AppCompatActivity {


    private static final int MSG_MUSIC_DATA = 1;
    private static final int MSG_SHOW_TOAST = 2;

    private RecyclerView contentRecyclerView;

    private SmartRefreshLayout smartRefreshLayout;

    private int currentPage = 1;

    private String searchContext;

    private int pageSize = 10;


    private IndexAdapter indexAdapter;



    private OkHttpClient client;


    private List<HomePageInfo> homePageInfos;


    private Handler mhandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        contentRecyclerView = findViewById(R.id.recycle_content);


        homePageInfos = new ArrayList<>();

        indexAdapter = new IndexAdapter(homePageInfos);
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        contentRecyclerView.setAdapter(indexAdapter);

        searchMusic(1,4);

        smartRefreshLayout = findViewById(R.id.smart_fresh_layout);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                searchMusic(currentPage, pageSize);
                refreshLayout.finishRefresh(500);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageSize = pageSize+1;
//                int currentSize = indexAdapter.getHomePageInfos().size();
//                int currentPage = (currentSize % 4 == 0) ? (currentSize / 10) : (currentSize / 10 + 1);
//                searchGames(searchContext, currentPage, pageSize, true);
                searchMusic(pageSize, pageSize);
                refreshLayout.finishLoadMore(500);
            }
        });


        mhandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case MSG_MUSIC_DATA:
                        List<HomePageInfo> newData = (List<HomePageInfo>) msg.obj;
                        homePageInfos.addAll(newData);
                        indexAdapter.notifyDataSetChanged();
                }
//                itemRecycleAdapter.notifyDataSetChanged();
//                indexAdapter.notifyDataSetChanged();
//                List<HomePageInfo> newData = (List<HomePageInfo>) msg.obj;
//                updateData(newData);
            }
        };
    }

    private void searchMusic(int currentPage, int pageSize){
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

                    List<HomePageInfo> curHomePageInfos = parseJsonWithObject(response);
//                    homePageInfos.addAll(curHomePageInfos);
                    Message message = mhandler.obtainMessage(MSG_MUSIC_DATA, curHomePageInfos);
                    mhandler.sendMessage(message);
//                    List<MusicInfo> musicInfoList1 = curHomePageInfos.get(0).getMusicInfoList();
//                    musicInfoList.addAll(musicInfoList1);
//                    mhandler.sendEmptyMessage(0);

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

    private void updateData(List<HomePageInfo> newData) {
        if (currentPage == 1) {
            homePageInfos.clear(); // 如果是第一页数据，则清空现有数据
        }
        homePageInfos.addAll(newData); // 添加新数据到列表
        indexAdapter.notifyDataSetChanged();

    }
}