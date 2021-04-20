package com.example.moviesdb;

public class ReviewItem {
    private String mLabel;
    private String mRating;
    private String mContent;

    public ReviewItem(String label, String  rating, String content) {
        mLabel = label;
        mRating = rating;
        mContent = content;
    }

    public String getLabel() {
        return mLabel;
    }

    public String  getRating() {
        return mRating;
    }

    public String getContent() {
        return mContent;
    }
}
