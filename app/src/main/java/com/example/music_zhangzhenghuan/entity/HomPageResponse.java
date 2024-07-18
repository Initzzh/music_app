package com.example.music_zhangzhenghuan.entity;

public class HomPageResponse {
    private int code;
    private String msg;
    private Page<HomePageInfo> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Page<HomePageInfo> getData() {
        return data;
    }

    public void setData(Page<HomePageInfo> data) {
        this.data = data;
    }
}
