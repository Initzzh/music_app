package com.example.music_zhangzhenghuan.entity;

import java.io.Serializable;
import java.util.Objects;

public class MusicInfo implements Serializable {

    private Long id;
    private String musicName;
    private String author;
    private String coverUrl;
    private String musicUrl;
    private String lyricUrl;

    public boolean equals(MusicInfo musicInfo){
        if(this == musicInfo) return true;
        if(this.id == musicInfo.getId()){
            return true;
        }
        return false;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, musicName, author, coverUrl, musicUrl, lyricUrl);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getLyricUrl() {
        return lyricUrl;
    }

    public void setLyricUrl(String lyricUrl) {
        this.lyricUrl = lyricUrl;
    }
}
