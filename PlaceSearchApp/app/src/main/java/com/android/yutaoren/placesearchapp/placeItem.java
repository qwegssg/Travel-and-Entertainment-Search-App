package com.android.yutaoren.placesearchapp;

public class placeItem {

    private String placeTitle;
    private String placeAddress;

    public placeItem(String placeTitle, String placeAddress) {
        this.placeTitle = placeTitle;
        this.placeAddress = placeAddress;
    }

    public String getPlaceTitle() {
        return placeTitle;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }
}
