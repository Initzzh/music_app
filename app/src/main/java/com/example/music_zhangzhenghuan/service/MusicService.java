//package com.example.music_liuwenya;
//
//import android.app.Service;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Binder;
//import android.os.IBinder;
//
//import androidx.annotation.Nullable;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Random;
//
//public class MusicService extends Service {
//    private MediaPlayer mediaPlayer;
//    private ArrayList<MusicInfo> musicList;  // 数据源列表
//    private int currentSongIndex;             // 当前播放的位置
//    private int mode;                         // 播放模式
//    Random random = new Random();
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        mediaPlayer = new MediaPlayer();
//        musicList = new ArrayList<>();
//
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                if(mode == 0) {  // 如果是顺序播放
//                    mediaPlayer.reset();
//                    next();
//                    EventBus.getDefault().postSticky(new StickyEvent(musicList.get(currentSongIndex)));
//                }
//                if(mode == 1) {  // 如果是单曲播放
//                    mediaPlayer.reset();
//                    playMusic(musicList.get(currentSongIndex));
//                }
//                if(mode == 2) {  // 如果是随机播放
//                    mediaPlayer.reset();
//                    playMusic(musicList.get(random.nextInt(musicList.size())));
////                    EventBus.getDefault().postSticky(new StickyEvent(musicList.get(currentSongIndex)));
//                }
//
//            }
//        });
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return new myMusicBind(this);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//    }
//    private void playMusic(MusicInfo musicInfo){
//        if (mediaPlayer.isPlaying()) {
//            mediaPlayer.stop();
//            mediaPlayer.reset();
//        }
//
//        String url = musicInfo.getMusicUrl();
//        String musicUrl = url.replace("http://","https://");
//        // 加载音乐资源
//        try {
//            mediaPlayer.setDataSource(musicUrl);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            mediaPlayer.prepare();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        play();
//    }
//    private void updateMusicList(ArrayList<MusicInfo> musicInfoList){
//        this.musicList = musicInfoList;              //  更新列表
////        currentSongIndex = musicList.size()-1;
//        // 检查当前是否正在播放音乐，如果正在播放，则停止。然后播放新音乐
////        if (mediaPlayer.isPlaying()){
////            mediaPlayer.stop();
////            mediaPlayer.reset();
////            playMusic(musicList.get(currentSongIndex));
////        }else {
////            playMusic(musicList.get(currentSongIndex));
////        }
//        playMusic(musicList.get(currentSongIndex));
//    }
//    private void updateCurrentSongIndex(int index){
//        if (index<0 || index >= musicList.size()){
//            return;
//        }
//        this.currentSongIndex = index;
//    }
//    private void updateMode(int mode){
//        this.mode = mode;
//    }
//
//    private void last() {
//        int preIndex = currentSongIndex-1;
//        if (mode == 0|| mode == 1) {
//            if (preIndex < 0) {
//                preIndex = musicList.size() - 1;
//            }
//        }else {  // 随机模式
//            preIndex = random.nextInt(musicList.size());
//        }
//        updateCurrentSongIndex(preIndex);  // 更新要播放的歌曲索引
//        playMusic(musicList.get(currentSongIndex));
//
//    }
//    private void next() {
//        int nextIndex = currentSongIndex+1;
//        if(mode == 0|| mode == 1) {  // 顺序模式，或单曲循环
//            if (nextIndex >= musicList.size()) {
//                nextIndex = 0;
//            }
//        }else{  // 随机模式
//            nextIndex = random.nextInt(musicList.size());
//        }
//        updateCurrentSongIndex(nextIndex);  // 更新要播放的歌曲索引
//        playMusic(musicList.get(currentSongIndex));
//    }
//
//    // 当前是否在播放
//    private boolean isPlay(){
//        return mediaPlayer.isPlaying();
//    }
//    // 暂停
//    public void pause(){
//        if(mediaPlayer.isPlaying()) {
//            mediaPlayer.pause();
//        }
//    }
//    public void play(){
//        mediaPlayer.start();
//    }
//    public int getDuration() {
//        return mediaPlayer.getDuration();
//    }
//
//    // 得到当前正在播放的音乐数据
//    public MusicInfo getMusicInfo(){
//        return musicList.get(currentSongIndex);
//    }
//
//    public int getCurrentPosition() {
//        return mediaPlayer.getCurrentPosition();
//    }
//    public void seekTo(int position){
//        mediaPlayer.seekTo(position);
//    }
//
//
//    class myMusicBind extends Binder{
//        private MusicService myMusicService;
//        public myMusicBind(MusicService musicService){
//            myMusicService = musicService;
//        }
//        public void updateMusicList(ArrayList<MusicInfo> musicInfoList){
//            myMusicService.updateMusicList(musicInfoList);
//        }
//        public void updateCurrentSongIndex(int index){
//            myMusicService.updateCurrentSongIndex(index);
//        }
//        public boolean isPlay(){
//            return myMusicService.isPlay();
//        }
//        public void pause(){
//            myMusicService.pause();
//        }
//        public void play(){
//            myMusicService.play();
//        }
//        public int getCurrentPosition() {
//            return myMusicService.getCurrentPosition();
//        }
//        public int getDuration() {
//            return myMusicService.getDuration();
//        }
//        public void updateMode(int mode){
//            myMusicService.updateMode(mode);
//        }
//        public void last() {
//            myMusicService.last();
//        }
//        public void next() {
//            myMusicService.next();
//        }
//        public MusicInfo getMusicInfo(){
//            return myMusicService.getMusicInfo();
//        }
//        public void seekTo(int position){
//            myMusicService.seekTo(position);
//        }
//
//
//    }
//}
