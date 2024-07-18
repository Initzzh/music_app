package com.example.music_zhangzhenghuan.entity;

import java.util.Map;

public class Lyric {


    private Long id;


    private  Map<String,String> lyricMap;

    public  Lyric(Long id){
        this.id = id;
    }

    public Lyric(Long id, Map<String ,String> lyricMap){
        this.id = id;
        this.lyricMap = lyricMap;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, String> getLyricMap() {
        return lyricMap;
    }

    public void setLyricMap(Map<String, String> lyricMap) {
        this.lyricMap = lyricMap;
    }
}
