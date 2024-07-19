package com.example.music_zhangzhenghuan.entity;

import java.util.List;

public class MusicEvent {


    int msgType ;

    int curMusicInfoIndex;

    private MusicInfo curMusicInfo;
    private  List<MusicInfo> remainPlayMusicInfoList;

    private List<MusicInfo> playMusicInfoList;



    public List<MusicInfo> getPlayMusicInfoList() {
        return playMusicInfoList;
    }

    public void setPlayMusicInfoList(List<MusicInfo> playMusicInfoList) {
        this.playMusicInfoList = playMusicInfoList;
    }

    public int getCurMusicInfoIndex() {
        return curMusicInfoIndex;
    }

    public void setCurMusicInfoIndex(int curMusicInfoIndex) {
        this.curMusicInfoIndex = curMusicInfoIndex;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }


    public MusicEvent(int msgType, MusicInfo curMusicInfo, List<MusicInfo> remainPlayMusicInfoList){
        this.msgType = msgType;
        this.curMusicInfo = curMusicInfo;
        this.remainPlayMusicInfoList = remainPlayMusicInfoList;

    }

    public List<MusicInfo> getRemainPlayMusicInfoList() {
        return remainPlayMusicInfoList;
    }

    public void setRemainPlayMusicInfoList(List<MusicInfo> remainPlayMusicInfoList) {
        this.remainPlayMusicInfoList = remainPlayMusicInfoList;
    }


    public MusicEvent(int msgType, MusicInfo musicInfo){
        this.msgType = msgType;
        this.curMusicInfo = musicInfo;
    }

    public MusicEvent(int msgType, int curMusicInfoIndex){
        this.msgType = msgType;
        this.curMusicInfoIndex = curMusicInfoIndex;
    }

    public MusicEvent(int msgType,MusicInfo musicInfo, int curMusicInfoIndex){

        this.msgType = msgType;
        this.curMusicInfo = musicInfo;
        this.curMusicInfoIndex = curMusicInfoIndex;

    }

    public MusicEvent(int msgType,int curMusicInfoIndex, List<MusicInfo> musicInfo){

        this.msgType = msgType;
        this.curMusicInfoIndex = curMusicInfoIndex;
        this.playMusicInfoList = musicInfo;

    }


    public MusicInfo getCurMusicInfo() {
        return curMusicInfo;
    }

    public void setCurMusicInfo(MusicInfo musicInfo) {
        this.curMusicInfo = musicInfo;
    }
}
