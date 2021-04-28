package com.example.moviesdb.model;

public class CastItem {
    private String mImgUrl;
    private String mName;

    public CastItem(String imgUrl, String name) {
        mImgUrl = imgUrl;
        mName = name;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public String getName() {
        return mName;
    }
}
