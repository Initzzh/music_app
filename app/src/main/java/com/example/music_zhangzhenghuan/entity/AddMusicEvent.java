package com.example.music_zhangzhenghuan.entity;

public class AddMusicEvent {

    private MusicInfo musicInfo;
    public AddMusicEvent(MusicInfo musicInfo){
        this.musicInfo = musicInfo;
    }
    public MusicInfo getMusicInfo() {
        return musicInfo;
    }

    public void setMusicInfo(MusicInfo musicInfo) {
        this.musicInfo = musicInfo;
    }
}
