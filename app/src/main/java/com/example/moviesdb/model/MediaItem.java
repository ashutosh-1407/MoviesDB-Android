package com.example.moviesdb.model;

public class MediaItem {
    private String mType;
    private String mImgUrl;
    private int mId;
    private String mName;
    private String mYear;
    private String mRating;

    public MediaItem(String type, String imgUrl, int id, String name, String year, String rating) {
        mType = type;
        mImgUrl = imgUrl;
        mId = id;
        mName = name;
        mYear = year;
        mRating = rating;
    }

    public String getType() {
        return mType;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getYear() {
        return mYear;
    }

    public String getRating() {
        return mRating;
    }
}
