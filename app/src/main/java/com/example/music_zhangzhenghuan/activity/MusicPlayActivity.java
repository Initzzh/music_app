package com.example.music_zhangzhenghuan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.music_zhangzhenghuan.R;
import com.example.music_zhangzhenghuan.adatper.LyricRecycleAdapter;
import com.example.music_zhangzhenghuan.entity.Lyric;
import com.example.music_zhangzhenghuan.entity.MusicEvent;
import com.example.music_zhangzhenghuan.entity.MusicInfo;
import com.example.music_zhangzhenghuan.service.MusicPlayService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MusicPlayActivity extends AppCompatActivity {

    private final int MSG_ADD_MUSIC = 0;
    private final int MSG_UPDATE_ACTIVITY = 1;

    private final int SCROLLPOS = 7;

    private ImageView playImage;
    private ImageView musicCoverImage;
    private ImageView closePlay;
    private ImageView prevMusicImage;
    private ImageView nextMusicImage;
    private ImageView playRuleImage;
    private ImageView loveImage;

    private RecyclerView lyricRecycle;

    private LyricRecycleAdapter lyricRecycleAdapter;

    private TextView lyricText;
    private SeekBar playProgress;
    private TextView musicName;
    private TextView musicAuthor;



    private RotateAnimation rotateAnimation;
    private List<MusicInfo> playMusicList;

    private MusicInfo curMusicInfo;

    private List<MusicInfo> collectedMusicList = new ArrayList<>();

    private Boolean isCollected = false;

    private TextView startTime;
    private TextView endTime;


    private int playRule = 0;

    private final int MSG_PROGRESS = 0;

    private final int MSG_LYRIC = 1;

    private Handler handler;

    private List<Lyric> lyricList;

    private Boolean playStatus = false;

    private Boolean isLyricText = false;

    // 后台服务
    private MusicPlayService musicPlayService;
    private ServiceConnection mConnection;
    private MusicPlayService.LocalBinder mBinder;



    private int currentMusicIndex  = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        EventBus.getDefault().register(this);


        Intent intent = getIntent();
        playMusicList = (List<MusicInfo>) intent.getSerializableExtra("musicInfoList");
        Log.i("TAG", "onCreate: ");

        closePlay = findViewById(R.id.image_close);
        loveImage = findViewById(R.id.image_love);

        musicCoverImage = findViewById(R.id.music_cover_image);
        lyricRecycle = findViewById(R.id.lyric_recycle);

        musicName = findViewById(R.id.music_name);
        musicAuthor = findViewById(R.id.music_author);
        startTime = findViewById(R.id.start_time);
        endTime = findViewById(R.id.end_time);

        playProgress = findViewById(R.id.play_progress);
        playImage = findViewById(R.id.play_music);
        playImage.setImageResource(R.drawable.pause);

        prevMusicImage = findViewById(R.id.prev_music);
        nextMusicImage = findViewById(R.id.next_music);

        playRuleImage = findViewById(R.id.play_rule);
        playRuleImage.setImageResource(R.drawable.play_rule1);


        // 加载页面
        curMusicInfo = playMusicList.get(currentMusicIndex);
        lyricList = new ArrayList<>();
        // 加载歌词
        loadlyricText(curMusicInfo.getLyricUrl());
        musicCoverImage.setVisibility(View.VISIBLE);
        String url = curMusicInfo.getCoverUrl();
        String httpsUrl = url.replace("http://","https://");
        RequestOptions options = new RequestOptions().circleCropTransform();
        Glide.with(getApplicationContext()).load(httpsUrl).apply(options).into(musicCoverImage);
        rotateAnimation = new RotateAnimation(0f, 360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

        // 图片旋转
        rotateAnimation.setDuration(10000);
        rotateAnimation.setRepeatCount(RotateAnimation.INFINITE);
        musicCoverImage.startAnimation(rotateAnimation);


        musicName.setText(curMusicInfo.getMusicName());
        musicAuthor.setText(curMusicInfo.getAuthor());


        // 定期更新进度条
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(musicPlayService != null){
                    final int currentProgress = musicPlayService.getCurPosition();
                    Message message = handler.obtainMessage(MSG_PROGRESS, currentProgress);
                    handler.sendMessage(message);
                }
            }
        },0,1000);

        // 服务连接, 获取服务
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder iBinder) {
                mBinder = (MusicPlayService.LocalBinder) iBinder;
                musicPlayService = mBinder.getService();

                if(musicPlayService != null){

                    Log.i("TAG", "onServiceConnected: 已初始化");

                    musicPlayService.addMusicInfoList(playMusicList);
                    // 音乐资源初始化
                    musicPlayService.initMusic(playMusicList.get(currentMusicIndex).getMusicUrl());
                    int totalDuration = musicPlayService.getDuration();

                    // 初始音乐时长
                    playProgress.setMax(totalDuration);
                    if(totalDuration/1000%60 < 10){
                        endTime.setText(totalDuration/1000/60 + ":0" + totalDuration/1000%60);
                    }else{
                        endTime.setText(totalDuration/1000/60 + ":" + totalDuration/1000%60);
                    }

                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mBinder = null;
                musicPlayService = null;
            }
        };

        // 绑定服务
        Intent musicPlayServiceIntent = new Intent(this, MusicPlayService.class);
        bindService(musicPlayServiceIntent, mConnection, Context.BIND_AUTO_CREATE);

        // 播放按钮监听
        playImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicInfo musicInfo = playMusicList.get(currentMusicIndex);
                if(musicPlayService != null){

                    if(musicPlayService.isPlaying()){
                        musicPlayService.pauseMusic();
                        playImage.setImageResource(R.drawable.pause);

                    }else{
                        musicPlayService.playMusic();
                        playImage.setImageResource(R.drawable.play);
                    }
                }
            }
        });

        // handler 处理消息
        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case MSG_PROGRESS:{
                        int curProgress = (int) msg.obj;
                        if(curProgress != 0){
                            // 更新时间，更新进度条
                            playProgress.setProgress(curProgress);
                            int timeM = curProgress/1000/60;
                            int timeS = curProgress/1000%60;
                            if(timeS < 10){
                                startTime.setText(timeM + ":0" + timeS);
                            }else{
                                startTime.setText(timeM + ":" + timeS);
                            }

                            // 更新歌词
                            if(lyricRecycle.getVisibility()== View.VISIBLE){
                                int lyricPos= 0;
                                int timeMills = 0;
                                // 找到位置
                                for(int i=0; i < lyricList.size(); ++i){
                                    String timeString = lyricList.get(i).getTime();
                                    timeMills = timeStringToMills(timeString);

                                    if(timeMills >= curProgress){
                                        lyricPos = i;
                                        break;

                                    }

                                }
                                if (timeMills > curProgress){
                                    lyricPos -=  1;
                                }
                                int finalLyricPos = lyricPos + SCROLLPOS;

                                lyricRecycleAdapter.setHightPosition(lyricPos);
                                if(finalLyricPos<lyricList.size()){
                                    lyricRecycle.post(() -> lyricRecycle.smoothScrollToPosition(finalLyricPos));
                                }
//                                lyricRecycle.smoothScrollToPosition(lyricPos);
                            }
                        }
                        break;

                    }

                }
            }
        };


        // 退出播放页面
        closePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent indexIntenet = new Intent(MusicPlayActivity.this, IndexActivity.class);
                startActivity(indexIntenet);
            }
        });

        // 收藏
        loveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!collectedMusicList.contains(curMusicInfo)){
                    loveImage.setImageResource(R.drawable.subtract_red);
                    collectedMusicList.add(curMusicInfo);
                    Toast.makeText(MusicPlayActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                }else{
                    loveImage.setImageResource(R.drawable.subtract);
                    collectedMusicList.remove(curMusicInfo);
                    Toast.makeText(MusicPlayActivity.this, "取消收藏成功", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // 点击空白处显示歌词
        ConstraintLayout container = findViewById(R.id.container);

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isLyricText){
                    // 加载歌词
//                    MusicInfo musicInfo = playMusicList.get(currentMusicIndex);
//                    loadlyricText(musicInfo.getLyricUrl());
                    // 设置TextView可见，图片不可见
                    if(lyricRecycle.getVisibility() == View.GONE){

                        // 取消动画，隐藏图片
                        rotateAnimation.cancel();
                        musicCoverImage.invalidate();
                        musicCoverImage.setVisibility(View.GONE);

                        // 歌词
                        lyricRecycle.setVisibility(View.VISIBLE);
                        lyricRecycle.setLayoutManager(new LinearLayoutManager(MusicPlayActivity.this, LinearLayoutManager.VERTICAL, false));

                        lyricRecycleAdapter = new LyricRecycleAdapter(lyricList);

                        lyricRecycle.setAdapter(lyricRecycleAdapter);


                    }
                    isLyricText = true;

                }else{
                    musicCoverImage.setVisibility(View.VISIBLE);
                    lyricRecycle.setVisibility(View.GONE);
                    isLyricText = false;
                }


//
            }
        });

        // 上一曲
        prevMusicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayService.playPrevMusic();
            }
        });

        // 下一曲
        nextMusicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayService.playNextMusic(playRule);
            }
        });

        playRuleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRule++;
                if(playRule > 2){
                    playRule = 0;
                }
                switch (playRule){
                    case 0:
                        playRuleImage.setImageResource(R.drawable.play_rule1);
                        break;
                    case 1:
                        playRuleImage.setImageResource(R.drawable.loop);
                        break;
                    case 2:
                        playRuleImage.setImageResource(R.drawable.rand);
                }
            }
        });


        playProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    if(musicPlayService != null){
                        musicPlayService.seekTo(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    // EvetBus接收播放音乐更新
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void upateMusic(MusicInfo musicInfo){

        musicCoverImage.setVisibility(View.VISIBLE);
        String url = musicInfo.getCoverUrl();
        String httpsUrl = url.replace("http://","https://");
        RequestOptions options = new RequestOptions().circleCropTransform();
        Glide.with(getApplicationContext()).load(httpsUrl).apply(options).into(musicCoverImage);

        // 设置进度条
        int totalDuration = musicPlayService.getDuration();
        playProgress.setMax(totalDuration);
        playProgress.setMax(totalDuration);
        if(totalDuration/1000%60 < 10){
            endTime.setText(totalDuration/1000/60 + ":0" + totalDuration/1000%60);
        }else{
            endTime.setText(totalDuration/1000/60 + ":" + totalDuration/1000%60);
        }

        musicName.setText(curMusicInfo.getMusicName());
        musicAuthor.setText(curMusicInfo.getAuthor());

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void upateMusic(MusicEvent musicEvent){
        switch (musicEvent.getMsgType()){
            case MSG_ADD_MUSIC:
                if(playMusicList !=  null){
                    playMusicList.add(musicEvent.getMusicInfo());
                    // 更新service的数据
                    List<MusicInfo> tmp = new ArrayList<>();
                    tmp.add(musicEvent.getMusicInfo());
                    musicPlayService.addMusicInfoList(tmp);
                }
            case MSG_UPDATE_ACTIVITY:
            {
                curMusicInfo = musicEvent.getMusicInfo();

                // 加载歌词
                loadlyricText(curMusicInfo.getLyricUrl());
                musicCoverImage.setVisibility(View.VISIBLE);
                lyricRecycle.setVisibility(View.GONE);
                String url = curMusicInfo.getCoverUrl();
                String httpsUrl = url.replace("http://","https://");
                RequestOptions options = new RequestOptions().circleCropTransform();
                Glide.with(getApplicationContext()).load(httpsUrl).apply(options).into(musicCoverImage);
                musicName.setText(curMusicInfo.getMusicName());
                musicAuthor.setText(curMusicInfo.getAuthor());
                playImage.setImageResource(R.drawable.pause);

                // 设置进度条
                int totalDuration = musicPlayService.getDuration();
                playProgress.setMax(totalDuration);
                playProgress.setMax(totalDuration);
                if(totalDuration/1000%60 < 10){
                    endTime.setText(totalDuration/1000/60 + ":0" + totalDuration/1000%60);
                }else{
                    endTime.setText(totalDuration/1000/60 + ":" + totalDuration/1000%60);
                }


            }
        }


    }

    public void loadlyricText(String url ){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "onFailure: ");
                Toast.makeText(MusicPlayActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String lyricString = response.body().string();
                    // 处理为事件：内容对
                    String[] lyricContentList = lyricString.split("\n");
//                    if(lyricList == null){
//                        lyricList = new ArrayList<>();
//                    }
                    // 清除之前的歌词
                    lyricList.clear();

                    for (String str : lyricContentList) {
                        String contentTime = str.substring(1, 8);
                        String content = str.substring(10);
                        lyricList.add(new Lyric(contentTime, content));
                    }
                    Log.i("TAG", "onResponse: lyric");

                }

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解除服务绑定
        unbindService(mConnection);

        EventBus.getDefault().unregister(this);


        handler.removeCallbacksAndMessages(null);
        handler.removeMessages(MSG_PROGRESS);

    }

    public int timeStringToMills(String timeString){
        Integer minutes  = Integer.parseInt(timeString.substring(0,2));

        Float  seconds = Float.parseFloat(timeString.substring(3));

        return minutes * 60 * 1000 + (int) (seconds * 1000);

    }


}


