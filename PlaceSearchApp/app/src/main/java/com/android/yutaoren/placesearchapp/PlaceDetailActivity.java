package com.android.yutaoren.placesearchapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetailActivity extends AppCompatActivity
        implements infoTab.OnFragmentInteractionListener,
                    photosTab.OnFragmentInteractionListener,
                    mapTab.OnFragmentInteractionListener,
                    reviewsTab.OnFragmentInteractionListener {

    String placeAddress;


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

        createView();

        try {
            JSONObject jsonObject = new JSONObject(getIntent().getExtras().getString("ShowMeTheDetail"));
            JSONObject detailResult = new JSONObject(jsonObject.getString("result"));

            Toast.makeText(getApplicationContext(), detailResult.getString("name"), Toast.LENGTH_LONG ).show();

            toolbar.setTitle(detailResult.getString("name"));


//            Bundle bundle = new Bundle();
//            bundle.putString("address", detailResult.getString("formatted_address"));
//            bundle.putString("placeAddress", "From Activity");

            placeAddress = detailResult.getString("formatted_address");


//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();

//            fragmentTransaction.add(R.id.detailViewPager, infoTab);
//            fragmentTransaction.commit();

//            infoTab.setArguments(bundle);
//            mSectionsPagerAdapter.getItem(0);
//            infoTab = new infoTab();
//            infoTab.putArguments(bundle);

//            int id = infoTab.getView().getId();
//            FragmentManager fm = getSupportFragmentManager();
//            fm.beginTransaction().replace(id, infoTab).commit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
//
//
//        EditText textView4 = (EditText) findViewById(R.id.qwe);
//        textView4.setText(detailUrl);

    }


    public String getPlaceAddress() {
        return placeAddress;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_place_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
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

    private void createView() {
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


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
