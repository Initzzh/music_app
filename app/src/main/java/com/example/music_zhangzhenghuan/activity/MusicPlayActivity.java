package com.example.music_zhangzhenghuan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.PixelCopy;
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
import com.example.music_zhangzhenghuan.entity.Lyric;
import com.example.music_zhangzhenghuan.entity.MusicInfo;
import com.example.music_zhangzhenghuan.service.MusicPlayService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MusicPlayActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    private ImageView playImage;

    private ImageView musicCoverImage;

    private ImageView closePlay;

    private ImageView prevMusicImage;

    private ImageView nextMusicImage;

    private ImageView playRuleImage;

    private ImageView loveImage;


    private TextView lyricText;


    private SeekBar playProgress;

    private TextView musicName;
    private TextView musicAuthor;

    private List<MusicInfo> playMusicList;

    private TextView startTime;
    private TextView endTime;

    private Boolean userDrag;

    private int playRule = 0;

    private final int MSG_PLAY_TIME = 0;

    private final int MSG_LYRIC = 1;

    private Handler handler;

    private Map<String,String> lyricMap;

    private int preMusicInfoIndx;
    private int nextMusicInfoIndex;

    private Boolean playStatus = false;



//    private int timeS; // 总时长分钟
//    private int timeM; // 总时长秒


    private int currentMusicIndex  = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        Intent intent = getIntent();
        playMusicList = (List<MusicInfo>) intent.getSerializableExtra("musicInfoList");
        Log.i("TAG", "onCreate: ");

        closePlay = findViewById(R.id.image_close);

        loveImage = findViewById(R.id.image_love);

        musicCoverImage = findViewById(R.id.music_cover_image);
        musicName = findViewById(R.id.music_name);
        musicAuthor = findViewById(R.id.music_author);
        startTime = findViewById(R.id.start_time);
        endTime = findViewById(R.id.end_time);

        playProgress = findViewById(R.id.play_progress);

        playImage = findViewById(R.id.play_music);
        playImage.setImageResource(R.drawable.play);

        prevMusicImage = findViewById(R.id.prev_music);
        nextMusicImage = findViewById(R.id.next_music);

        playRuleImage = findViewById(R.id.play_rule);
        playRuleImage.setImageResource(R.drawable.play_rule1);

        lyricText = findViewById(R.id.lyrics_text_view);


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
                loveImage.setImageResource(R.drawable.subtract_red);
                Toast.makeText(MusicPlayActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
            }
        });




        // 点击空白处显示歌词
        ConstraintLayout container = findViewById(R.id.container);

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 加载歌词
                MusicInfo musicInfo = playMusicList.get(currentMusicIndex);

                loadlyricText(musicInfo.getLyricUrl(), musicInfo.getId());

                // 设置TextView可见，图片不可见
                if(lyricText.getVisibility() == View.GONE){
                    lyricText.setVisibility(View.VISIBLE);
                    musicCoverImage.setVisibility(View.GONE);
                }
//
            }
        });

        // 上一曲
        prevMusicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preMusic(playRule);
            }
        });

        // 下一曲
        nextMusicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMusic(playRule);
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

        // 更新数据
        updateLayout(currentMusicIndex);

        handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {

                super.handleMessage(msg);

                switch (msg.what){
                    case MSG_PLAY_TIME:{
                        int durationTime = (int) msg.obj;
                        if(durationTime != 0){
                            int timeM = durationTime/1000/60;
                            int timeS = durationTime/1000%60;
                            if(timeS < 10){
                                startTime.setText(timeM + ":0" + timeS);
                            }else {
                                startTime.setText(timeM + ":" + timeS);
                            }

                        }
                        break;

                    }
                    case MSG_LYRIC:{
                        Lyric lyric = (Lyric) msg.obj;
                        lyricText.setVisibility(View.VISIBLE);
                        musicCoverImage.setVisibility(View.GONE);

                        if(lyric != null){
                            Map<String,String> lyricMap = lyric.getLyricMap();
                            lyricMap.forEach((key,value)->{
                                lyricText.append(value);
                                lyricText.append("\n");
                            });
                        }
                        
                        
                        // 设置可见
                        lyricText.setVisibility(View.VISIBLE);
                        musicCoverImage.setVisibility(View.INVISIBLE);

                    } default:
                        break;

                }
            }
        };

        playImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //后台播放音乐
//                Intent musicPlayIntent = new Intent(MusicPlayActivity.this, MusicPlayService.class);
//                musicPlayIntent.putExtra("playStatus",playStatus);
//                musicPlayIntent.putExtra("musicUrl",playMusicList.get(currentMusicIndex).getMusicUrl());
//                startService(musicPlayIntent);
//                playImage.setImageResource(R.drawable.play);

                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
//
//                    // 开启服务进行播放
//                    Intent musicPlayIntent = new Intent(MusicPlayActivity.this, MusicPlayService.class);
//                    musicPlayIntent.putExtra("playStatus","play");
//                    musicPlayIntent.putExtra("musicUrl",playMusicList.get(currentMusicIndex).getMusicUrl());
//                    startService(musicPlayIntent);
                    playImage.setImageResource(R.drawable.play);


                }else{
                    mediaPlayer.start();
//                    startService(musicPlayIntent);
                    playImage.setImageResource(R.drawable.pause);
                }

            }


        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                playProgress.setMax(mediaPlayer.getDuration());
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        playProgress.setProgress(mediaPlayer.getCurrentPosition());
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        Message message = handler.obtainMessage(MSG_PLAY_TIME,currentPosition);
                        handler.sendMessage(message);

                    }
                },0,1000);

            }
        });


        playProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
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

    private void playMusic(int currentMusicIndex)  {
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.start();
    }

    private void nextMusic(int playRule){
        currentMusicIndex++;
        if(currentMusicIndex >= playMusicList.size()){
            currentMusicIndex = 0;
        }
        updateLayout(currentMusicIndex);
    }

    private void preMusic(int playRule){
        currentMusicIndex--;
        if(currentMusicIndex < 0){
            currentMusicIndex = playMusicList.size() - 1;
        }
        updateLayout(currentMusicIndex);
    }

    private void updateLayout(int currentMusicIndex){

        MusicInfo curMusicInfo = playMusicList.get(currentMusicIndex);
        String url = curMusicInfo.getCoverUrl();
        String httpsUrl = url.replace("http://","https://");
        RequestOptions options = new RequestOptions().circleCropTransform();
        Glide.with(getApplicationContext()).load(httpsUrl).apply(options).into(musicCoverImage);
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

        // 图片旋转
        rotateAnimation.setDuration(10000);
        rotateAnimation.setRepeatCount(RotateAnimation.INFINITE);
        musicCoverImage.setAnimation(rotateAnimation);

        musicName.setText(curMusicInfo.getMusicName());
        musicAuthor.setText(curMusicInfo.getAuthor());


        switch (playRule){
            case 0:
            case 1:
            {
                if(currentMusicIndex == 0){
                    preMusicInfoIndx = playMusicList.size()-1;
                }else{
                    preMusicInfoIndx = currentMusicIndex -1;
                }
                if(currentMusicIndex == playMusicList.size()-1){
                    nextMusicInfoIndex = 0;
                }else{
                    nextMusicInfoIndex = currentMusicIndex + 1;
                }
            }
            case 2:{
                Random random = new Random();
                nextMusicInfoIndex = random.nextInt(playMusicList.size());
            }
        }
        // 音乐播放器
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
        }
        mediaPlayer.reset();
        startTime.setText("00:00");

        try {
            mediaPlayer.setDataSource(curMusicInfo.getMusicUrl());
            mediaPlayer.prepare();

            int totalDuration = mediaPlayer.getDuration();
            if(totalDuration/1000%60 < 10){
                endTime.setText(totalDuration/1000/60 + ":0" + totalDuration/1000%60);
            }else{
                endTime.setText(totalDuration/1000/60 + ":" + totalDuration/1000%60);
            }
            playProgress.setMax(mediaPlayer.getDuration());
            playProgress.setProgress(mediaPlayer.getCurrentPosition());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void loadlyricText(String url, Long id){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MusicPlayActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String lyricString = response.body().string();

                    // 处理为事件：内容对
                    String[] lyricContentList = lyricString.split("\n");
                    Map<String, String> contentTimeMap = new HashMap<>();
                    for (String str : lyricContentList) {
                        String contentTime = str.substring(1, 8);
                        String content = str.substring(10);
                        contentTimeMap.put(contentTime, content);

                    }

                    Lyric lyric = new Lyric(id, contentTimeMap);


                    Log.i("TAG", "onResponse: "+lyric);
                    Message lyricMessage = handler.obtainMessage(MSG_LYRIC, lyric);
                    handler.sendMessage(lyricMessage);
                }

            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        handler.removeMessages(MSG_PLAY_TIME);

    }


}
