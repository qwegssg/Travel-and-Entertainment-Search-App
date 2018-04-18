package com.android.yutaoren.placesearchapp;

public class PlaceItem {

    private String placeTitle;
    private String placeAddress;
    private String imageUrl;

    public PlaceItem(String placeTitle, String placeAddress, String imageUrl) {
        this.placeTitle = placeTitle;
        this.placeAddress = placeAddress;
        this.imageUrl = imageUrl;

    }

    public String getPlaceTitle() {

        return placeTitle;
    }

    public String getPlaceAddress() {

        return placeAddress;
    }

    public String getImageUrl() {

        return imageUrl;

    }
}
