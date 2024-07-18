package com.example.music_zhangzhenghuan.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;

public class MusicPlayService extends Service {

    private MediaPlayer mediaPlayer;


    public MusicPlayService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action = intent.getAction();
        if("play".equals(action)){
            playMusic(intent.getStringExtra("musicUrl"));
        }else  if("pause".equals(action)){
            pauseMusic();
        }



        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void playMusic(String url){
        if(mediaPlayer == null){
            mediaPlayer = new MediaPlayer();
            try{
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
                int totalTime = mediaPlayer.getDuration();
            }catch (IOException e){
                throw new RuntimeException(e);
            }
            mediaPlayer.start();
        }

    }

    private void pauseMusic(){
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMusic();

    }
}