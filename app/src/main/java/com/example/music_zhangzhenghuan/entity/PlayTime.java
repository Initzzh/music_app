package com.example.music_zhangzhenghuan.entity;

import androidx.annotation.NonNull;

public class PlayTime {
    int timeS;
    int timeM;

    public int getTimeS() {
        return timeS;
    }

    public void setTimeS(int timeS) {
        this.timeS = timeS;
    }

    public int getTimeM() {
        return timeM;
    }

    public void setTimeM(int timeM) {
        this.timeM = timeM;
    }

    public PlayTime(int timeM, int timeS) {
        this.timeS = timeS;
        this.timeM = timeM;
    }

    @NonNull
    @Override
    public String toString() {
        return timeM+":"+timeS;
    }
}
