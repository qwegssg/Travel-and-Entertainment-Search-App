package com.android.yutaoren.placesearchapp;

public class YelpReviewItem {

    private String yelpName;
    private String yelpText;
    private String yelpPhotoUrl;
    private String yelpUrl;
    private int yelpRating;
    private String yelpTime;

    public YelpReviewItem(String yelpName, String yelpText, String yelpPhotoUrl, String yelpUrl, int yelpRating, String yelpTime) {
        this.yelpName = yelpName;
        this.yelpText = yelpText;
        this.yelpPhotoUrl = yelpPhotoUrl;
        this.yelpUrl = yelpUrl;
        this.yelpRating = yelpRating;
        this.yelpTime = yelpTime;
    }

    public String getYelpName() {
        return yelpName;
    }

    public String getYelpText() {
        return yelpText;
    }

    public String getYelpPhotoUrl() {
        return yelpPhotoUrl;
    }

    public String getYelpUrl() {
        return yelpUrl;
    }

    public int getYelpRating() {
        return yelpRating;
    }

    public String getYelpTime() {
        return yelpTime;
    }
}