package com.android.yutaoren.placesearchapp;

public class PlaceItem {

    private String placeTitle;
    private String placeAddress;
    private String imageUrl;
    private String place_id;

    public PlaceItem(String placeTitle, String placeAddress, String imageUrl, String place_id) {
        this.placeTitle = placeTitle;
        this.placeAddress = placeAddress;
        this.imageUrl = imageUrl;
        this.place_id = place_id;

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

    public String getPlace_id() {
        return place_id;
    }
}
