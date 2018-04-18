package com.android.yutaoren.placesearchapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlacesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<PlaceItem> placeItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);

        getSupportActionBar().setHomeButtonEnabled(true);
//        enable back button functionality in older (before API 14)as well as newer APIs
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        init place list Widgets
        placeItems = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.placesRecyclerView);

        try {

//            TextView textView3 = (TextView) findViewById(R.id.textView3);
//            textView3.setText(jsonObject.getJSONArray("results").toString());

            JSONObject jsonObject = new JSONObject(getIntent().getExtras().getString("ShowMeTheList"));
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject placeObj = jsonArray.getJSONObject(i);
                PlaceItem place = new PlaceItem(
                        placeObj.getString("name"),
                        placeObj.getString("vicinity"),
                        placeObj.getString("icon")
                );

                placeItems.add(place);
            }

            adapter  = new PlacesListAdapter(placeItems, this);
//            set the fixed view size
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

        } catch(JSONException e){
            e.printStackTrace();
        }

    }

//    android.R.id.home handling code
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                // home activity
                startActivityAfterCleanup(MainActivity.class);
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    private void startActivityAfterCleanup(Class<?> cls) {
//        if (projectsDao != null) projectsDao.close();
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
