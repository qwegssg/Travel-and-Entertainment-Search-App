package com.android.yutaoren.placesearchapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlacesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<placeItem> placeItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);

        try {

            Bundle bundle = new Bundle();
            bundle = getIntent().getExtras();
            JSONObject jsonObject = new JSONObject(bundle.getString("ShowMeTheList"));

//            TextView textView3 = (TextView) findViewById(R.id.textView3);
//            textView3.setText(jsonObject.getJSONArray("results").toString());

        }catch(JSONException e){
             e.printStackTrace();
        }

        recyclerView = (RecyclerView) findViewById(R.id.placesRecyclerView);
//        set every view has the same size
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        placeItems = new ArrayList<>();


//        dummy assignment
        for(int i = 0; i < 10; i++) {
            placeItem dummyPlaceItem = new placeItem(
                    "Title" + i + 1,
                    "bugaosuni"
            );

            placeItems.add(dummyPlaceItem);
        }


        adapter = new placeListAdapter(placeItems, this);

        recyclerView.setAdapter(adapter);
    }
}
