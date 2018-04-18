package com.android.yutaoren.placesearchapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class PlacesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);

        try {

            Bundle bundle = new Bundle();
            bundle = getIntent().getExtras();
            JSONObject jsonObject = new JSONObject(bundle.getString("ShowMeTheList"));

            TextView textView3 = (TextView) findViewById(R.id.textView3);
            textView3.setText(jsonObject.getJSONArray("results").toString());

        }catch(JSONException e){
             e.printStackTrace();
        }


    }
}
