package com.android.yutaoren.placesearchapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link mapTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link mapTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mapTab extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private AutoCompleteTextView fromInput;
    private String selectedMode;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private PlaceDetailActivity activity;
    private RequestQueue requestQueue;
    private List<LatLng> markers;



    public mapTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mapTab.
     */
    // TODO: Rename and change types and number of parameters
    public static mapTab newInstance(String param1, String param2) {
        mapTab fragment = new mapTab();
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

        mView = inflater.inflate(R.layout.fragment_map_tab, container, false);
        activity = (PlaceDetailActivity) getActivity();

        fromInput = mView.findViewById(R.id.fromInput);
        CustomAutoCompleteAdapter adapter =  new CustomAutoCompleteAdapter(getContext());
        fromInput.setAdapter(adapter);
        fromInput.setOnItemClickListener(onItemClickListener);

        Spinner modeInput = (Spinner) mView.findViewById(R.id.modeInput);
        ArrayAdapter<CharSequence> modeAdapter;
        modeAdapter = ArrayAdapter.createFromResource(getContext(), R.array.modeName, android.R.layout.simple_spinner_item);
        modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeInput.setAdapter(modeAdapter);
        //        handle the spinner selection
        modeInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedMode = getResources().getStringArray(R.array.modeValue)[position];

//                when travel mode is changed, redirect the route
                if(markers.size() == 2) {
//                    clear the former route
                    mGoogleMap.clear();
                    mGoogleMap.addMarker(new MarkerOptions()
                                            .position(markers.get(0)));
                    mGoogleMap.addMarker(new MarkerOptions()
                                            .position(markers.get(1)));
                    getDirections(getRequestUrl(markers.get(0), markers.get(1), selectedMode));
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        markers = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getActivity());

        // Inflate the layout for this fragment
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if(mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        MarkerOptions placeMarker = new MarkerOptions()
                                    .position(new LatLng(activity.getPlaceLat(), activity.getPlaceLng()))
                                    .title(activity.getPlaceName());
        mGoogleMap.addMarker(placeMarker).showInfoWindow();
        markers.add(new LatLng(activity.getPlaceLat(), activity.getPlaceLng()));

        CameraPosition camera = CameraPosition
                                .builder()
                                .target(new LatLng(activity.getPlaceLat(), activity.getPlaceLng()))
                                .zoom(15)
                                .build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera));
//        enable the zoom control
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //    for autocomplete input
    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    fromInput.setText(
                            ((com.android.yutaoren.placesearchapp.Place)adapterView.getItemAtPosition(i)).getPlaceText()
                    );

//                    remove the former marker and renew the marker
                    if(markers.size() == 2) {
                        markers.remove(1);
                        mGoogleMap.clear();
                        mGoogleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(activity.getPlaceLat(), activity.getPlaceLng()))
                                    .title(activity.getPlaceName()));
                    }

//                    when user selects a place from the autocomplete suggestions, fetch the geoLocation of the place
                    String placeUrl = "http://nodejsyutaoren.us-east-2.elasticbeanstalk.com/search?&otherLocation="
                                        + fromInput.getText();
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, placeUrl, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        double fromLat = response.getDouble("lat");
                                        double fromLng = response.getDouble("lng");

                                        MarkerOptions fromMarker = new MarkerOptions()
                                                                    .position(new LatLng(fromLat, fromLng));
                                        mGoogleMap.addMarker(fromMarker);
                                        markers.add(new LatLng(fromLat, fromLng));

//                                        fetch and parse the direction json data to show the direction
                                        String directionUrl = getRequestUrl(markers.get(0), markers.get(1), selectedMode);
                                        getDirections(directionUrl);

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
            };

//    get the request url for fetching direction json data
    private String getRequestUrl(LatLng destPlace, LatLng fromPlace, String travelMode) {
        String str_dest = "destination=" + destPlace.latitude + "," + destPlace.longitude;
        String str_org = "origin=" + fromPlace.latitude + "," + fromPlace.longitude;
        String mode = "mode=" + travelMode;
        String param = str_org + "&" + str_dest + "&" + mode;

        String url = "https://maps.googleapis.com/maps/api/directions/json?" + param;

        return url;
    }

    private void getDirections(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getString("status").equals("OK")) {

                        TaskParser taskParser = new TaskParser();
                        taskParser.execute(response.toString());

                        zoomRoute(mGoogleMap, markers);
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



    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
//                parse json into directions
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        //Get list route and display it into the map
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {

            ArrayList points = null;
            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }
                polylineOptions.addAll(points);
                polylineOptions.width(25);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions!=null) {
                mGoogleMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getContext(), "Direction not found!", Toast.LENGTH_LONG).show();
            }
        }
    }

//    zoom in and zoom out the map
    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 100;
        LatLngBounds latLngBounds = boundsBuilder.build();

//        set the lat and lng bounds of the camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
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
