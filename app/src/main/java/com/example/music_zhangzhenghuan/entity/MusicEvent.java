package com.example.music_zhangzhenghuan.entity;

public class MusicEvent {



    int msgType ;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    private MusicInfo musicInfo;
    public MusicEvent(MusicInfo musicInfo, int msgType){
        this.msgType = msgType;
        this.musicInfo = musicInfo;
    }
    public MusicInfo getMusicInfo() {
        return musicInfo;
    }

    public void setMusicInfo(MusicInfo musicInfo) {
        this.musicInfo = musicInfo;
    }
}
