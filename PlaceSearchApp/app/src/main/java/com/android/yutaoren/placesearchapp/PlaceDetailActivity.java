package com.android.yutaoren.placesearchapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivity extends AppCompatActivity
        implements infoTab.OnFragmentInteractionListener,
                    photosTab.OnFragmentInteractionListener,
                    mapTab.OnFragmentInteractionListener,
                    reviewsTab.OnFragmentInteractionListener {

    JSONObject detailResult;


    String placeAddress;
    String placePhoneNumber;
    String placePriceLevel;
    double placeRating;
    String placeGooglePage;
    String placeWebsite;
    String place_id;
    double placeLat;
    double placeLng;
    String placeName;
    JSONArray placeGoogleReview;
    List<PlaceItem> favPlaceItems;
    SharedPreferences prefs;
    String key;
    Gson gson;
    PlaceItem placeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        enable back button functionality
        getSupportActionBar().setHomeButtonEnabled(true);
//        enable back button functionality in older (before API 14)as well as newer APIs
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabGenerator();

        initPlaceDetailInfo();

//        get the detail from place list activity
        try {
            JSONObject jsonObject = new JSONObject(getIntent().getExtras().getString("ShowMeTheDetail"));
            detailResult = new JSONObject(jsonObject.getString("result"));

            toolbar.setTitle(detailResult.getString("name"));
            if(detailResult.has("formatted_address")) {
                placeAddress = detailResult.getString("formatted_address");
            }
            if(detailResult.has("formatted_phone_number")) {
                placePhoneNumber = detailResult.getString("formatted_phone_number");
            }
            if(detailResult.has("price_level")) {
                for(int p = 0; p < detailResult.getInt("price_level"); p++) {
                    placePriceLevel += "$";
                }
            }
            if(detailResult.has("rating")) {
                placeRating = detailResult.getDouble("rating");
            }
            if(detailResult.has("url")) {
                placeGooglePage = detailResult.getString("url");
            }
            if(detailResult.has("website")) {
                placeWebsite = detailResult.getString("website");
            }
//            in order to retrieve the photos of the place
            place_id = detailResult.getString("place_id");
//            in order to set the map marker
            placeLat = detailResult.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            placeLng = detailResult.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            placeName = detailResult.getString("name");

            if(detailResult.has("reviews")) {
                placeGoogleReview =   detailResult.getJSONArray("reviews");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        create the placeItem object for fav adding and removing
        try {
            placeItem = new PlaceItem(
                    detailResult.getString("name"),
                    detailResult.getString("vicinity"),
                    detailResult.getString("icon"),
                    detailResult.getString("place_id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void tabGenerator() {
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        host the section contents
        ViewPager mViewPager = (ViewPager) findViewById(R.id.detailViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        List<Integer> tabIcons = new ArrayList<>();
        tabIcons.add(R.drawable.info);
        tabIcons.add(R.drawable.photos);
        tabIcons.add(R.drawable.maps);
        tabIcons.add(R.drawable.review);

        for (int i = 0; i < tabLayout.getTabCount(); i++ ) {
            LinearLayout tabLinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_detail_tab, null);
            TextView tabContent = (TextView) tabLinearLayout.findViewById(R.id.detailTabContent);
//            TextView tabContent = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_detail_tab, null);
            tabContent.setText(getResources().getStringArray(R.array.detailTabContent)[i]);
            tabContent.setTextColor(getResources().getColor(R.color.colorSelectedTabText));
            tabContent.setGravity(Gravity.CENTER);
            tabContent.setPadding(40, 0, 40,0);
            tabContent.setCompoundDrawablesWithIntrinsicBounds(tabIcons.get(i), 0, 0, 0);
            tabLayout.getTabAt(i).setCustomView(tabContent);
        }

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    private void initPlaceDetailInfo() {
        placeAddress = "";
        placePhoneNumber = "";
        placePriceLevel = "";
        placeRating = 0.0;
        placeGooglePage = "";
        placeWebsite = "";
        place_id = "";
        placeLat = 34.0266;
        placeLng = -118.2831;
        placeGoogleReview = new JSONArray();
        favPlaceItems = new ArrayList<>();
        prefs = this.getSharedPreferences("com.android.yutaoren.placesearchapp", Context.MODE_PRIVATE);
        key = "com.android.yutaoren.placesearchapp.key";
        gson = new Gson();
    }

//    String object is unchangeable when created. No need to worry about changing by other class !
    public String getPlaceAddress() {
        return placeAddress;
    }

    public String getPlacePhoneNumber() {
        return placePhoneNumber;
    }

    public String getPlacePriceLevel() {
        return placePriceLevel;
    }

    public double getPlaceRating() {
        return placeRating;
    }

    public String getPlaceGooglePage() {
        return placeGooglePage;
    }

    public String getPlaceWebsite() {
        return placeWebsite;
    }

    public String getPlace_id() {
        return place_id;
    }

    public double getPlaceLat() {
        return placeLat;
    }

    public double getPlaceLng() {
        return placeLng;
    }

    public String getPlaceName() {
        return placeName;
    }

    public JSONArray getPlaceGoogleReview() {
        return placeGoogleReview;
    }

    //    implement the tool bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place_detail, menu);

//        init the fav status of the place
        if(!prefs.getAll().isEmpty()) {
            String json = prefs.getString(key, "yutaoren");
            favPlaceItems = gson.fromJson(json, new TypeToken<List<PlaceItem>>(){}.getType());
            for(int i = 0; i < favPlaceItems.size(); i++) {
                if(favPlaceItems.get(i).getPlace_id().equals(placeItem.getPlace_id())) {
                    menu.findItem(R.id.favoriteBtn).setIcon(R.drawable.heart_fill_white);
                    menu.findItem(R.id.favoriteBtn).setTitle("unlike it");
                }
            }
        }

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.twitterbtn) {
            try {
                String twitterUrl = "";
                if(!placeWebsite.equals("")) {
                    twitterUrl = "https://twitter.com/intent/tweet?text=Check out "
                            + detailResult.getString("name") + " located at "
                            + placeAddress + ". Website:&url="
                            + placeWebsite + "&hashtags=TravelAndEntertainmentSearch";
                } else if(!placeGooglePage.equals("")) {
                    twitterUrl = "https://twitter.com/intent/tweet?text=Check out "
                            + detailResult.getString("name") + " located at "
                            + placeAddress + ". Website:&url="
                            + placeGooglePage + "&hashtags=TravelAndEntertainmentSearch";
                }
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse(twitterUrl));
                startActivity(intent);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (id == R.id.favoriteBtn) {

            String json = "";

            if(item.getTitle().equals("like it")) {
                item.setIcon(R.drawable.heart_fill_white);
                try {
                    Toast.makeText(this, detailResult.getString("name")
                            + " was added to favorites", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                item.setTitle("unlike it");

//                add place to the fav list
                if(!prefs.getAll().isEmpty()) {
                    json = prefs.getString(key, "yutaoren");
                    favPlaceItems = gson.fromJson(json, new TypeToken<List<PlaceItem>>(){}.getType());
                }
                favPlaceItems.add(placeItem);
                json = gson.toJson(favPlaceItems);
                prefs.edit().putString(key, json).apply();

            } else {
                item.setIcon(R.drawable.heart_outline_white);
                try {
                    Toast.makeText(this, detailResult.getString("name")
                            + " was removed from favorites", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                item.setTitle("like it");

//                remove place to the fav list
                json = prefs.getString(key, "yutaoren");
                favPlaceItems = gson.fromJson(json, new TypeToken<List<PlaceItem>>(){}.getType());
                for(int i = 0; i < favPlaceItems.size(); i++) {
                    if(favPlaceItems.get(i).getPlace_id().equals(placeItem.getPlace_id())) {
                        favPlaceItems.remove(i);
                        json = gson.toJson(favPlaceItems);
                        prefs.edit().putString(key, json).apply();
                    }
                }

            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void startActivityAfterCleanup(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
//           return the current tab
            switch (position) {
                case 0:
                    infoTab infoTab = new infoTab();
                    return infoTab;
                case 1:
                    photosTab photosTab = new photosTab();
                    return photosTab;
                case 2:
                    mapTab mapTab = new mapTab();
                    return mapTab;
                case 3:
                    reviewsTab reviewsTab = new reviewsTab();
                    return reviewsTab;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}
}
