package com.android.yutaoren.placesearchapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PlacesListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Button nextBtn, previousBtn;
    private RequestQueue requestQueue;

    private int currentPage;
    private int totalPage;
    private String next_page_token;
//    a list storing every page's list
    private List<List<PlaceItem>> placeItemsStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);

//        enable back button functionality
        getSupportActionBar().setHomeButtonEnabled(true);
//        enable back button functionality in older (before API 14)as well as newer APIs
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        init place list Widgets
        initPlacesWidgets();

        try {
            JSONObject jsonObject = new JSONObject(getIntent().getExtras().getString("ShowMeTheList"));
//            if there is no place result found
            if(jsonObject.getString("status").equals("ZERO_RESULTS")) {

                TextView noPlaces = (TextView) findViewById(R.id.noPlaces);
                noPlaces.setText("No results");
                previousBtn.setVisibility(View.INVISIBLE);
                nextBtn.setVisibility(View.INVISIBLE);

            }
            else {
                initPlacesList(jsonObject);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                show the places list
                adapter  = new PlacesListAdapter(placeItemsStorage.get(currentPage - 2),PlacesListActivity.this);
//                set the fixed view size
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(adapter);

                currentPage--;
                pageBtnCheck();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if next page's list hes aleady been fetched
                if(placeItemsStorage.size() > currentPage + 1 || placeItemsStorage.size() == currentPage + 1) {
//                    show the places list
                    adapter  = new PlacesListAdapter(placeItemsStorage.get(currentPage), PlacesListActivity.this);
//                    set the fixed view size
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(adapter);

                    currentPage++;
                    pageBtnCheck();
                } else {
                    String nextPageUrl = "http://nodejsyutaoren.us-east-2.elasticbeanstalk.com/next?next_page_token=" + next_page_token;
                    sendJSONRequest(nextPageUrl);
                }
            }
        });

    }

    private void initPlacesWidgets() {
        recyclerView = (RecyclerView) findViewById(R.id.placesRecyclerView);

        previousBtn = (Button) findViewById(R.id.previousBtn);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        previousBtn.setEnabled(false);
        nextBtn.setEnabled(false);

        currentPage = 0;
        totalPage = 0;
        placeItemsStorage = new ArrayList<>();
    }

    private void pageBtnCheck() {
        if(currentPage == 2 || currentPage == 3) {
            previousBtn.setEnabled(true);
        } else {
            previousBtn.setEnabled(false);
        }
//        the total page is calculated means the last page is already reached
        if(totalPage != 0) {
            if(currentPage < totalPage) {
                nextBtn.setEnabled(true);
            } else {
                nextBtn.setEnabled(false);
            }
        }
//        the last page has not been reached yet, so the next btn can always be true
        else {
            nextBtn.setEnabled(true);
        }

    }

    private void sendJSONRequest(String url) {
//        show the progressing dialog
        final ShowProgressDialog showProgressDialog = new ShowProgressDialog(this);
        showProgressDialog.onPreExecute();

        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("OK")) {
                                initPlacesList(response);
                            }
//                    dismiss the progressing dialog
                            showProgressDialog.onPostExecute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        requestQueue.add(jsonObjectRequest);
    }


    private void initPlacesList(JSONObject jsonObject) {

        List<PlaceItem> placeItems = new ArrayList<>();

        currentPage++;
        pageBtnCheck();

        try {
//            next page check
            if(jsonObject.has("next_page_token")) {
                next_page_token = jsonObject.getString("next_page_token");
            } else {
//                currentPage is the last page
                totalPage = currentPage;
                nextBtn.setEnabled(false);
            }

//            init the place list items
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject placeObj = jsonArray.getJSONObject(i);
                PlaceItem place = new PlaceItem(
                        placeObj.getString("name"),
                        placeObj.getString("vicinity"),
                        placeObj.getString("icon"),
//                        it is "place_id", not "id"!!!!
                        placeObj.getString("place_id")
                );
                placeItems.add(place);
            }
        } catch(JSONException e){
            e.printStackTrace();
        }

        placeItemsStorage.add(placeItems);

//        show the places list
        adapter  = new PlacesListAdapter(placeItems, this);
//        set the fixed view size
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private static class ShowProgressDialog extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        private ShowProgressDialog(Activity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Fetching next page");
            dialog.show();
        }

        protected Void doInBackground(Void... args) {
            // do background work here
            return null;
        }

        protected void onPostExecute() {
            // do UI work here
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
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
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


//    fetch the place details info
    public void onClickCalled(String place_id, String placeTitle) {
        String theUrl = "http://nodejsyutaoren.us-east-2.elasticbeanstalk.com/detail?placeid=" + place_id;
        sendJSONRequestforDetail(theUrl);
    }

    private void sendJSONRequestforDetail(String theUrl) {
//        show the progressing dialog
        final ShowProgressDialog showProgressDialog = new ShowProgressDialog(this);
        showProgressDialog.onPreExecute();

        requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, theUrl, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("status").equals("OK")) {
                                initPlacesDetail(response);
                            }
//                    dismiss the progressing dialog
                            showProgressDialog.onPostExecute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void initPlacesDetail(JSONObject response) {
        Intent intent = new Intent(this, PlaceDetailActivity.class);

        String resString = response.toString();
        intent.putExtra("ShowMeTheDetail", resString);
        startActivity(intent);
    }
}
