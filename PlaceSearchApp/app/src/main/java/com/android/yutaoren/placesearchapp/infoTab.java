package com.android.yutaoren.placesearchapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link infoTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link infoTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class infoTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;




    public infoTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment infoTab.
     */
    // TODO: Rename and change types and number of parameters
    public static infoTab newInstance(String param1, String param2) {
        infoTab fragment = new infoTab();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_tab, container, false);

        PlaceDetailActivity activity = (PlaceDetailActivity) getActivity();

        String place_address = activity.getPlaceAddress();
        String place_phone_number = activity.getPlacePhoneNumber();
        String place_price_level = activity.getPlacePriceLevel();
        float place_rating = (float) activity.getPlaceRating();
        String place_google_page = activity.getPlaceGooglePage();
        String place_website = activity.getPlaceWebsite();

        TextView placeAddress = (TextView) view.findViewById(R.id.placeAddress);
        TextView placePhoneNumber = (TextView) view.findViewById(R.id.placePhoneNumber);
        TextView placePriceLevel = (TextView) view.findViewById(R.id.placePriceLevel);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        TextView placeGooglePage = (TextView) view.findViewById(R.id.placeGooglePage);
        TextView placeWebsite = (TextView) view.findViewById(R.id.placeWebsite);

        placeAddress.setText(place_address);
        placePhoneNumber.setText(place_phone_number);
        placePriceLevel.setText(place_price_level);
//        if the rating is not available
        if(place_rating == 0.0) {
//            set the weight of ratingBar to 0 so that it is set hidden
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0);
            ratingBar.setLayoutParams(params);
        }
        else {
            ratingBar.setRating(place_rating);
        }
        placeGooglePage.setText(place_google_page);
        placeWebsite.setText(place_website);



        return view;
    }



    public void putArguments(Bundle args) {
    String place_address = args.getString("placeAddress");
//        placeAddress.setText("place_address");
}



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
