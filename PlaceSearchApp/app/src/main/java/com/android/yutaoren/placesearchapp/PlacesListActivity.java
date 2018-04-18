package com.android.yutaoren.placesearchapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

//        init place list Widgets
        placeItems = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.placesRecyclerView);

        try {

//            TextView textView3 = (TextView) findViewById(R.id.textView3);
//            textView3.setText(jsonObject.getJSONArray("results").toString());

            Bundle bundle = new Bundle();
            bundle = getIntent().getExtras();
            JSONObject jsonObject = new JSONObject(bundle.getString("ShowMeTheList"));
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
}
