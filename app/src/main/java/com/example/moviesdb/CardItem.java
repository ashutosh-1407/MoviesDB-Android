package com.example.moviesdb;

public class CardItem {
    private String mImgUrl;
    private int mId;

    public CardItem(String imgUrl, int id) {
        mImgUrl = imgUrl;
        mId = id;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public int getId() {
        return mId;
    }
}
