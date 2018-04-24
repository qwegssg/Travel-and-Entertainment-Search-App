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
import android.widget.Spinner;
import android.widget.Toast;

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
        recyclerView = (RecyclerView) view.findViewById(R.id.reviewsRecyclerView);
        PlaceDetailActivity activity = (PlaceDetailActivity) getActivity();
        getGoogleReviews(activity.getPlaceGoogleReview());


//        need to be implemented!
//        use reviewItem.getReviewName() to get the attribute for sorting
        reviewsSource = (Spinner) view.findViewById(R.id.reviewsSource);
        reviewsOrder = (Spinner) view.findViewById(R.id.reviewsOrder);




        return view;
    }

    private void getGoogleReviews(JSONArray googleReviews) {

        List<ReviewItem> reviewItems = new ArrayList<>();

//        if there is no google reviews, the length is 0;
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
