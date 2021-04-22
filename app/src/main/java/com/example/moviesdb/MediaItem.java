package com.example.moviesdb;

public class MediaItem {
    private String mImgUrl;
    private int mId;
    private String mTrailerUrl;

    public MediaItem(String name, int id, String trailerUrl) {
        mImgUrl = name;
        mId = id;
        mTrailerUrl = trailerUrl;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public int getId() {
        return mId;
    }

    public String getTrailerUrl() {
        return mTrailerUrl;
    }
}
