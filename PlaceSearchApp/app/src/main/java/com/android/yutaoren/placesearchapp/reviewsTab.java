package com.android.yutaoren.placesearchapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link reviewsTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link reviewsTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class reviewsTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private Spinner reviewsSource;
    private Spinner reviewsOrder;
    private TextView noReviews;
    private boolean isFoundYelpReviews;
    private  boolean isFoundGoogleReviews;

    List<ReviewItem> reviewItems;
    List<YelpReviewItem> yelpReviewItems;


    public reviewsTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment reviewsTab.
     */
    // TODO: Rename and change types and number of parameters
    public static reviewsTab newInstance(String param1, String param2) {
        reviewsTab fragment = new reviewsTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reviews_tab, container, false);

//        show the reviews lst
        final PlaceDetailActivity activity = (PlaceDetailActivity) getActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.reviewsRecyclerView);
        noReviews = (TextView) view.findViewById(R.id.noReviews);
        reviewsSource = (Spinner) view.findViewById(R.id.reviewsSource);
        reviewsOrder = (Spinner) view.findViewById(R.id.reviewsOrder);
        reviewItems = new ArrayList<>();
        yelpReviewItems = new ArrayList<>();
        isFoundYelpReviews = true;
        isFoundGoogleReviews = true;

//        show the google reviews and init the Yelp reviews
        initGoogleReviews(activity.getPlaceGoogleReview());

        String yelpUrl = getYelpReviewsReqUrl(activity.getPlaceAddress(), activity.getPlaceName());
        sendYelpReqUrl(yelpUrl);

//        handle the reviews sources spinner
        ArrayAdapter<CharSequence> reviewSourceAdapter;
        reviewSourceAdapter = ArrayAdapter.createFromResource(getContext(), R.array.reviewSource, android.R.layout.simple_spinner_item);
        reviewSourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reviewsSource.setAdapter(reviewSourceAdapter);
        reviewsSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(getResources().getStringArray(R.array.reviewSource)[position].equals("Yelp reviews")) {
                    if(isFoundYelpReviews) {
                        noReviews.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter  = new YelpReviewsAdapter(yelpReviewItems, getContext());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);

                    } else {
                        noReviews.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                } else if(getResources().getStringArray(R.array.reviewSource)[position].equals("Google reviews")) {
                    if(isFoundGoogleReviews) {
                        noReviews.setVisibility(View.INVISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter = new ReviewsAdapter(reviewItems, getContext());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                    } else {
                        noReviews.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


//        handle the reviews orders spinner

        //        use reviewItem.getReviewName() to get the attribute for sorting


        ArrayAdapter<CharSequence> reviewOrderAdapter;
        reviewOrderAdapter = ArrayAdapter.createFromResource(getContext(), R.array.reviewOrder, android.R.layout.simple_spinner_item);
        reviewOrderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reviewsOrder.setAdapter(reviewOrderAdapter);




        return view;
    }

    private void initGoogleReviews(JSONArray googleReviews) {

//        if there is no google reviews, the length is 0;
        if(googleReviews.length() == 0) {
            isFoundGoogleReviews = false;
        }
        else {
            for(int i = 0; i < googleReviews.length(); i++) {
                try {
                    JSONObject reviewObj = googleReviews.getJSONObject(i);
                    ReviewItem reviewItem = new ReviewItem(
                            reviewObj.getString("author_name"),
                            reviewObj.getString("text"),
                            reviewObj.getString("profile_photo_url"),
                            reviewObj.getString("author_url"),
                            reviewObj.getInt("rating"),
                            reviewObj.getInt("time")
                    );
                    reviewItems.add(reviewItem);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter  = new ReviewsAdapter(reviewItems, getContext());
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }
    }

    private String getYelpReviewsReqUrl(String formatted_address, String name) {

        String state = "";
        String city = "";
        String yelpReviewsUrl = "";

        int stateEndIndex = formatted_address.lastIndexOf(",");
        String addressInUS = formatted_address.substring(0, stateEndIndex);
        int cityEndIndex = addressInUS.lastIndexOf(",");
        String stateAndZIP = addressInUS.substring(cityEndIndex + 2);
        state = stateAndZIP.substring(0, 2);

        String addressInCity = addressInUS.substring(0, cityEndIndex);
        int cityStartIndex = addressInCity.lastIndexOf(",");
//        if "," is not found, then the city is the beginning of the address
        if(cityStartIndex == -1) {
            city = addressInCity;
        } else {
            city = addressInCity.substring(cityStartIndex + 2);
        }
        if(formatted_address.length() > 64) {
            yelpReviewsUrl = "http://nodejsyutaoren.us-east-2.elasticbeanstalk.com/yelpSearch?name="
                            + name + "&city=" + city + "&state=" + state + "&country=US";
        }
//        it is acceptable to assign the address that has less than 64 char to one of the the params
        else {
            yelpReviewsUrl = "http://nodejsyutaoren.us-east-2.elasticbeanstalk.com/yelpSearch?name="
                    + name + "&city=" + city + "&state=" + state + "&country=US&address1=" + formatted_address;
        }
        return yelpReviewsUrl;
    }

    private void sendYelpReqUrl(String url) {
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                                                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.has("businesses") && response.getJSONArray("businesses").length() > 0) {

                        String yelpReviewsUrl = "http://nodejsyutaoren.us-east-2.elasticbeanstalk.com/yelpReview?id="
                                                + response.getJSONArray("businesses").getJSONObject(0).getString("id");
//
                        JsonObjectRequest yelpRequest = new JsonObjectRequest(Request.Method.GET, yelpReviewsUrl, null,
                                                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if(response.has("reviews") && response.getJSONArray("reviews").length() > 0) {
                                        getYelpReviews(response.getJSONArray("reviews"));
                                    }
                                    else {
                                        isFoundYelpReviews = false;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {}
                        });

                        requestQueue.add(yelpRequest);

                    } else {
                        isFoundYelpReviews = false;
                    }
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

    private void getYelpReviews(JSONArray reviews) {

        for(int i = 0; i < reviews.length(); i++) {
            try {
                JSONObject reviewObj = reviews.getJSONObject(i);
                YelpReviewItem yelpReviewItem = new YelpReviewItem(
                        reviewObj.getJSONObject("user").getString("name"),
                        reviewObj.getString("text"),
                        reviewObj.getJSONObject("user").getString("image_url"),
                        reviewObj.getString("url"),
                        reviewObj.getInt("rating"),
                        reviewObj.getString("time_created")
                );
                yelpReviewItems.add(yelpReviewItem);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
