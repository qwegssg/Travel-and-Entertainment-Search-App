package com.android.yutaoren.placesearchapp;

public class ReviewItem {

    private String reviewName;
    private String reviewContent;
    private String reviewPhotoUrl;
    private String reviewUrl;
    private int reviewRating;
    private int reviewTime;
    private int index;

    public ReviewItem(String reviewName, String reviewContent, String reviewPhotoUrl, String reviewUrl, int reviewRating, int reviewTime, int index) {
        this.reviewName = reviewName;
        this.reviewContent = reviewContent;
        this.reviewPhotoUrl = reviewPhotoUrl;
        this.reviewUrl = reviewUrl;
        this.reviewRating = reviewRating;
        this.reviewTime = reviewTime;
        this.index = index;
    }

    public String getReviewName() {
        return reviewName;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public String getReviewPhotoUrl() {
        return reviewPhotoUrl;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public int getReviewRating() {
        return reviewRating;
    }

    public int getReviewTime() {
        return reviewTime;
    }

    public int getIndex() {
        return index;
    }
}
