package com.android.yutaoren.placesearchapp;

public class ReviewItem {

    private String reviewName;
    private String reviewContent;
    private String reviewPhotoUrl;

    public ReviewItem(String reviewName, String reviewContent, String reviewUrl) {
        this.reviewName = reviewName;
        this.reviewContent = reviewContent;
        this.reviewPhotoUrl = reviewUrl;
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
}
