package com.example.music_zhangzhenghuan;

import java.util.List;

public class Page<T> {
    private List<HomePageInfo> records;
    private int total;
    private int size;
    private int current;
    private int pages;

    public List<HomePageInfo> getRecords() {
        return records;
    }

    public void setRecords(List<HomePageInfo> records) {
        this.records = records;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
