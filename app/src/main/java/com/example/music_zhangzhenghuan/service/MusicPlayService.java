package com.example.music_zhangzhenghuan.service;

import android.app.LocaleConfig;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.BoringLayout;

import com.example.music_zhangzhenghuan.entity.MusicEvent;
import com.example.music_zhangzhenghuan.entity.MusicInfo;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayService extends Service {

    private final LocalBinder mBinder = new LocalBinder();

    private final int MSG_UPDATE_ACTIVITY = 1;

    private final int SEQUENCE_PLAY = 0;
    private final int LOOP_PLAY = 1;
    private final int RANDOM_PLAY = 2;



    private final int MSG_PROGRESS = 0;

    private List<MusicInfo> musicInfoList;

    private Handler handler;

    private MediaPlayer mediaPlayer;

    private int playMode = 0; // 1, 2

    private int curMusicIndex = 0;

    private int preMusicIndex;

    private boolean isInit = false;

//    private List<Integer> playedMusicIndex;






    @Override
    public void onCreate() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
            @Override
            public void onCompletion(MediaPlayer mp) {
                switch (playMode){
                    case 0:
                        playNextMusic();
                        break;
                    case 1:
                        playLoopMusic();
                        break;
                    case 2:
                        playNextRandomMusic();
                        break;
                    default:
                        playNextMusic();
                        break;
                }

            }
        });

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;

    }

    public void addMusicInfoList(List<MusicInfo> musicInfoList){
        if(this.musicInfoList == null){
            this.musicInfoList = new ArrayList<>();
        }
        this.musicInfoList.addAll(musicInfoList);
    }

    public void initMusic(String url){
        if(mediaPlayer != null ){
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public int getCurPosition(){
        if(mediaPlayer != null)
        {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    public void playMusic(){
        if(mediaPlayer != null){
            if(!isInit){
                initMusic(musicInfoList.get(curMusicIndex).getMusicUrl());
            }
            mediaPlayer.start();
        }


    }


    public void playNextMusic(int playMode){
        switch (playMode) {
            case SEQUENCE_PLAY:
                playNextMusic();
                break;
            case LOOP_PLAY:
                playLoopMusic();
                break;
            case RANDOM_PLAY:
                playNextRandomMusic();
                break;
            default:
                playNextMusic();
                break;
        }
    }

    public void playNextMusic(){
        if(mediaPlayer != null){

            mediaPlayer.reset();
            preMusicIndex = curMusicIndex;
            if(!musicInfoList.isEmpty()){
                curMusicIndex = (curMusicIndex + 1) % musicInfoList.size();
            }
            // 加载数据
            initMusic(musicInfoList.get(curMusicIndex).getMusicUrl());
            mediaPlayer.start();

            MusicInfo musicInfo = musicInfoList.get(curMusicIndex);
            MusicEvent  musicEvent = new MusicEvent(MSG_UPDATE_ACTIVITY, musicInfo, curMusicIndex);
            EventBus.getDefault().postSticky(musicEvent);

        }
    }

    public void playLoopMusic(){
        if(mediaPlayer != null){

            mediaPlayer.reset();
            mediaPlayer.start();
        }
    }

    public void playNextRandomMusic(){
        if(mediaPlayer != null){
            mediaPlayer.reset();
            preMusicIndex = curMusicIndex;
            curMusicIndex = (int) (Math.random() * musicInfoList.size());
            // 加载数据
            initMusic(musicInfoList.get(curMusicIndex).getMusicUrl());
            mediaPlayer.start();
            MusicInfo musicInfo = musicInfoList.get(curMusicIndex);
            MusicEvent  musicEvent = new MusicEvent(MSG_UPDATE_ACTIVITY, musicInfo, curMusicIndex);
            EventBus.getDefault().postSticky(musicEvent);
//            EventBus.getDefault().postSticky(musicInfoList.get(curMusicIndex));
//            EventBus.getDefault().postSticky(new MusicEvent(musicInfoList.get(curMusicIndex),MSG_UPDATE_ACTIVITY));
        }
    }

    public void playPrevMusic(){
        if(mediaPlayer != null){
            mediaPlayer.reset();
            curMusicIndex = (curMusicIndex - 1 + musicInfoList.size()) % musicInfoList.size();
            initMusic(musicInfoList.get(curMusicIndex).getMusicUrl());
            mediaPlayer.start();
            MusicInfo musicInfo = musicInfoList.get(curMusicIndex);
            MusicEvent  musicEvent = new MusicEvent(MSG_UPDATE_ACTIVITY, musicInfo, curMusicIndex);
            EventBus.getDefault().postSticky(musicEvent);
//            EventBus.getDefault().postSticky(musicInfoList.get(curMusicIndex));
//            EventBus.getDefault().postSticky(new MusicEvent(musicInfoList.get(curMusicIndex),MSG_UPDATE_ACTIVITY));

        }
    }

    public boolean isPlaying(){
        return  mediaPlayer.isPlaying();
    }


    public void pauseMusic(){
        if(mediaPlayer != null){
            mediaPlayer.pause();

        }
    }

    private void stopMusic(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // 设置进度
    public void seekTo(int progress){
        if(mediaPlayer != null){
            mediaPlayer.seekTo(progress);
        }
    }


    public class LocalBinder extends Binder{
        public MusicPlayService getService(){
            return MusicPlayService.this;
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMusic();

    }
}