package com.example.music_zhangzhenghuan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.music_zhangzhenghuan.RecycleAdapter;
import com.example.music_zhangzhenghuan.adatper.MusicListBottomFragment;
import com.example.music_zhangzhenghuan.adatper.MusicListRecycleAdapter;
import com.example.music_zhangzhenghuan.entity.HomPageResponse;
import com.example.music_zhangzhenghuan.entity.HomePageInfo;
import com.example.music_zhangzhenghuan.R;
import com.example.music_zhangzhenghuan.adatper.IndexAdapter;
import com.example.music_zhangzhenghuan.entity.MusicEvent;
import com.example.music_zhangzhenghuan.entity.MusicInfo;
import com.example.music_zhangzhenghuan.service.MusicPlayService;
import com.google.gson.Gson;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IndexActivity extends AppCompatActivity {



    private static final int MSG_ADD_MUSIC = 0;
    private static final int MSG_MUSIC_DATA = 1;

    private static final int MSG_PLAY_MUSIC = 3;

    private static final int MSG_CLICK_ITEM = 4;

    private static final int MSG_CLOSE_ACTIVITY = 5;
    private static final int MSG_SHOW_TOAST = 2;



    private RecyclerView contentRecyclerView;

    private SmartRefreshLayout smartRefreshLayout;

    // 悬浮窗
    private ConstraintLayout suspendLayout;
    private ImageView suspendCoverImage;
    private TextView suspendMusicName;
    private TextView suspendMusicAuthor;

    private ImageView suspendPlayImage;
    private ImageView suspendListImage;

    private int currentPage = 1;

    private String searchContext;

    private int pageSize = 10;
    private IndexAdapter indexAdapter;

    private List<MusicInfo> playMusicList;

    private List<MusicInfo> suspendMusicList;

    private OkHttpClient client;

    private List<HomePageInfo> homePageInfos;

    private Handler mhandler;

    private int curMusicInfoIndex = 0;

    private MusicInfo curMusicInfo;

    private int curPlayMode = 0;


    // 后台服务
    private MusicPlayService musicPlayService;
    private ServiceConnection mConnection;
    private MusicPlayService.LocalBinder mBinder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        EventBus.getDefault().register(this);
        contentRecyclerView = findViewById(R.id.recycle_content);

        suspendLayout = findViewById(R.id.suspend);
        suspendLayout.bringToFront(); // 最上层

//        EventBus.getDefault().register(this);


        homePageInfos = new ArrayList<>();
        playMusicList = new ArrayList<>();

        // 显示悬浮窗

        suspendCoverImage = findViewById(R.id.suspend_cover_image);
        suspendMusicName  = findViewById(R.id.suspend_music_name);
        suspendMusicAuthor = findViewById(R.id.suspend_music_author);
        suspendPlayImage = findViewById(R.id.suspend_play_image);
        suspendListImage = findViewById(R.id.suspend_music_list);


//         服务连接, 获取服务
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                mBinder = (MusicPlayService.LocalBinder) iBinder;
                musicPlayService = mBinder.getService();

                if(musicPlayService != null){

                    Log.i("TAG", "onServiceConnected: 已初始化");

                    musicPlayService.addMusicInfoList(playMusicList);

                    // 音乐资源初始化
//                    musicPlayService.initMusic(playMusicList.get(curMusicInfoIndex).getMusicUrl());
//                    int totalDuration = musicPlayService.getDuration();

                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mBinder = null;
                musicPlayService = null;
            }
        };
//
        // 绑定服务
        Intent musicPlayServiceIntent = new Intent(this, MusicPlayService.class);
        bindService(musicPlayServiceIntent, mConnection, Context.BIND_AUTO_CREATE);

//         播放按钮点击
        suspendPlayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MusicInfo musicInfo = playMusicList.get(curMusicInfoIndex);
                if(musicPlayService != null){
//                    musicPlayService.initMusic(playMusicList.get(curMusicInfoIndex).getMusicUrl());
//                    musicPlayService.playMusic();
//                    suspendPlayImage.setImageResource(R.drawable.play_black);

                    if(musicPlayService.isPlaying()){
                        musicPlayService.pauseMusic();

                        suspendPlayImage.setImageResource(R.drawable.pause_black);

                    }else{
//                        musicPlayService.initMusic(playMusicList.get(curMusicInfoIndex).getMusicUrl());
                        musicPlayService.playMusic();
                        suspendPlayImage.setImageResource(R.drawable.play_black);
                    }
                }
            }
        });




//        // 悬浮列表
//        Intent intent = getIntent();
//        Serializable musicInfoList = intent.getSerializableExtra("playMusicList");
//        curMusicInfoIndex = intent.getIntExtra("curMusicInfoIndex",0);
//        if(musicInfoList != null){
//            playMusicList = (List<MusicInfo>) musicInfoList;
//            suspendLayout.setVisibility(View.VISIBLE);
//            loadSuspendLayout();
//        }


        indexAdapter = new IndexAdapter(homePageInfos, playMusicList);
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        contentRecyclerView.setAdapter(indexAdapter);


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

        searchMusic(1,4);

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
//
            }
        };


        // 播放列表点击时间

        suspendListImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicListBottomFragment musicListBottomFragment = new MusicListBottomFragment(playMusicList,curPlayMode);
                musicListBottomFragment.show(getSupportFragmentManager(),"musicListBottomSheetFragment");

            }
        });


    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessage(MusicEvent musicEvent){
        if(musicEvent.getMsgType() == MSG_PLAY_MUSIC){
            suspendLayout.setVisibility(View.VISIBLE);
            if(playMusicList == null){
                playMusicList = new ArrayList<>();
            }
            playMusicList.clear();
            playMusicList.addAll(musicEvent.getPlayMusicInfoList());
            suspendLayout.setVisibility(View.VISIBLE);
            loadSuspendLayout();
            // 加载悬浮窗
            if(musicPlayService != null){
                musicPlayService.addMusicInfoList(musicEvent.getPlayMusicInfoList());
                musicPlayService.initMusic(playMusicList.get(curMusicInfoIndex).getMusicUrl());
                musicPlayService.playMusic();
                suspendPlayImage.setImageResource(R.drawable.play_black);
            }

            curMusicInfoIndex = 0;

        }
        if(musicEvent.getMsgType() == MSG_CLICK_ITEM){
            if(playMusicList == null) {
                playMusicList = new ArrayList<>();
            }

            // 去重
            MusicInfo curMusicInfo = musicEvent.getCurMusicInfo();
            if(playMusicList.contains(curMusicInfo)){
                playMusicList.remove(curMusicInfo);
                playMusicList.add(0, curMusicInfo);
            }else{
                playMusicList.add(0, curMusicInfo);
            }
            List<MusicInfo> remainPlayMusicInfoList = musicEvent.getRemainPlayMusicInfoList();

            for(MusicInfo musicInfo: remainPlayMusicInfoList){
                if(!playMusicList.contains(musicInfo)){
                    playMusicList.add(musicInfo);
                }
            }

            Intent musicPlayIntent = new Intent(IndexActivity.this, MusicPlayActivity.class);
            Serializable musicInfoListSerializable = (Serializable) playMusicList;
            musicPlayIntent.putExtra("playMusicList", musicInfoListSerializable);
            startActivity(musicPlayIntent);
        }

        if(musicEvent.getMsgType() == MSG_ADD_MUSIC){
            Boolean  isExist = false;

            MusicInfo curMusicInfo = musicEvent.getCurMusicInfo();
            if(playMusicList == null) {
                playMusicList = new ArrayList<>();
            }
            for(int i=0 ;i<playMusicList.size(); ++i){
                if(playMusicList.get(i).getId() == curMusicInfo.getId()){
                    isExist = true;
                }

            }
            if(isExist == false){
                playMusicList.add(0, musicEvent.getCurMusicInfo());
            }

        }

        if(musicEvent.getMsgType() == MSG_CLOSE_ACTIVITY){
            curMusicInfoIndex = musicEvent.getCurMusicInfoIndex();
            suspendLayout.setVisibility(View.VISIBLE);
            loadSuspendLayout();

        }
    }


    private void searchMusic(int currentPage, int pageSize){
//        https://hotfix-service-prod.g.mi.com/music/homePage?current=1&size=4

        client = new OkHttpClient.Builder().build();
        String urlPre=  "https://hotfix-service-prod.g.mi.com/music/homePage?";
        String url = urlPre + "current=" + currentPage + "&size="+ pageSize;

        Request request = new Request.Builder()
                .get()
                .url(url)
                .header("content-type","application/json")
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


//    @Subscribe(sticky = true)
//    public void onAddMessageEvent(MusicEvent addMusicEvent){
//        playMusicList.add(addMusicEvent.getMusicInfo());
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


        unbindService(mConnection);
        EventBus.getDefault().unregister(this);
        mhandler.removeCallbacksAndMessages(null);
        mhandler.removeMessages(0);



    }

    private void suspendLayout(){

    }

    private void loadImage(){

//        String coverUrl = musicInfo.getCoverUrl();
//        String httpsUrl = coverUrl.replace("http://", "https://");
//
//        RequestOptions requestOptions = new RequestOptions()
//                .transform(new CenterCrop(), new RoundedCorners(20));
//        Glide.with(holder.itemView.getContext()).load(httpsUrl).apply(requestOptions).into(holder.musicCoverImage);
    }

    private void loadSuspendLayout(){


        curMusicInfo = playMusicList.get(curMusicInfoIndex);

        String coverUrl = curMusicInfo.getCoverUrl();
        String httpsUrl = coverUrl.replace("http://", "https://");

        RequestOptions options = new RequestOptions().circleCropTransform();
        Glide.with(getApplicationContext()).load(httpsUrl).apply(options).into(suspendCoverImage);

        suspendMusicName.setText(curMusicInfo.getMusicName());
        suspendMusicAuthor.setText("-"+curMusicInfo.getAuthor());
        suspendPlayImage.setImageResource(R.drawable.play_black);
        Log.i("TAG", "loadSuspendLayout: ");

        contentRecyclerView.bringToFront();
    }

//
//    private void showMusicListDialog(){
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater  = getLayoutInflater();
//        View musicListView = inflater.inflate(R.layout.dialog_music_list,null);
//        builder.setView(musicListView);
//
//
//
//
//        //dialog
//        TextView curMusicListSize = musicListView.findViewById(R.id.cur_music_list_size);
//        curMusicListSize.setText(playMusicList.size()+"");
//
//        ConstraintLayout muicModeView = musicListView.findViewById(R.id.music_mode_view);
//        ImageView musicModeImage = musicListView.findViewById(R.id.music_mode_image);
//        TextView muiscModeText = musicListView.findViewById(R.id.music_mode_text);
//
//        switch (curPlayMode){
//            case 0:
//                musicModeImage.setImageResource(R.drawable.seq_black);
//                muiscModeText.setText("顺序播放");
//                break;
//            case 1:
//                musicModeImage.setImageResource(R.drawable.loop_black);
//                muiscModeText.setText("循环播放");
//            case 2:
//                musicModeImage.setImageResource(R.drawable.rand_black);
//                muiscModeText.setText("随机播放");
//        }
//
//        RecyclerView musicListRecycle = musicListView.findViewById(R.id.music_list_recycle);
//        MusicListRecycleAdapter musicListRecycleAdapter = new MusicListRecycleAdapter(playMusicList);
//        musicListRecycle.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
//        musicListRecycle.setAdapter(musicListRecycleAdapter);
//
//
////        builder.setCancelable(false);
//        AlertDialog musicListDialog = builder.create();
//
//        musicListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x;
//
//// 设置Dialog的宽度为屏幕宽度，位置在底部
//        Window window = musicListDialog.getWindow();
//        if (window != null) {
//            window.setGravity(Gravity.BOTTOM);
//            WindowManager.LayoutParams params = window.getAttributes();
//            params.width = width; // 设置宽度为屏幕宽度
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度自适应内容
//            window.setAttributes(params);
//            window.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
//        }
//        musicListDialog.show();
//    }



}