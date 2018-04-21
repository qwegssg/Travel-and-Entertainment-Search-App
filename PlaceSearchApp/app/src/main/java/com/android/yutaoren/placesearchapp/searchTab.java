package com.android.yutaoren.placesearchapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.Provider;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link searchTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link searchTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class searchTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private final int REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 1;

//    google play service location client
    private FusedLocationProviderClient mFusedLocationClient;
//    volley request queue
    private RequestQueue requestQueue;

    private EditText keywordInput, otherLocInput, distanceInput;
    private TextView keywordValidation;
    private TextView otherLocVlidation;
    private Button searchBtn, clearBtn;
    private RadioButton currentLocBtn, otherLocBtn;
    private Spinner categoryInput;
    private ArrayAdapter<CharSequence> categoryAdapter;

    private String keyword, otherLocation, category, selectedCategory;
    private int distance;
    private double lat, lng;



    private EditText editText2;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public searchTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchTab.
     */
    // TODO: Rename and change types and number of parameters
    public static searchTab newInstance(String param1, String param2) {
        searchTab fragment = new searchTab();
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

        View searchView = inflater.inflate(R.layout.fragment_search_tab, container, false);

        initSearchWidgets(searchView);

//        create google Fused Location Provider Client instance
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                                Toast.makeText(getActivity(), lat + " " + lng, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "cannot get current loc", Toast.LENGTH_SHORT).show();
//                                hardcode lat & lng
                                lat = 34.0266;
                                lng = -118.2832;
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("MapDemoActivity", "Error trying to get last GPS location");
                            e.printStackTrace();
                        }
                    });
        }

//        request the permission from the user.
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
        }

//        handle the spinner selection
        categoryInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedCategory = getResources().getStringArray(R.array.categoryValue)[position];

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

//        if otherLocBtn is checked, enable the other location input field
        otherLocBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    otherLocInput.setEnabled(true);
                } else {
                    otherLocInput.setEnabled(false);
                }
            }
        });
//        if currentLocBtn is checked, hide the validation of other location input field
        currentLocBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    otherLocVlidation.setVisibility(View.GONE);
                }
            }
        });
//        when input has changed, hide the validation
        keywordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                keywordValidation.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                keywordValidation.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                keywordValidation.setVisibility(View.GONE);
            }
        });
        otherLocInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                otherLocVlidation.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                otherLocVlidation.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                otherLocVlidation.setVisibility(View.GONE);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPlaces();
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keywordValidation.setVisibility(View.GONE);
                otherLocVlidation.setVisibility(View.GONE);
                otherLocBtn.setChecked(false);
                otherLocInput.setEnabled(false);
                currentLocBtn.setChecked(true);
                categoryAdapter = ArrayAdapter.createFromResource(getContext(), R.array.categoryName, android.R.layout.simple_spinner_item);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categoryInput.setAdapter(categoryAdapter);
                keywordInput.setText("");
                distanceInput.setText("");
                otherLocInput.setText("");
            }
        });

        return searchView;
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

    private void initSearchWidgets(View v) {
        keywordInput = (EditText) v.findViewById(R.id.keywordInput);
        distanceInput = (EditText) v.findViewById(R.id.distanceInput);
        otherLocInput = (EditText) v.findViewById(R.id.otherLocInput);
        otherLocInput.setEnabled(false);

        keywordValidation = (TextView) v.findViewById(R.id.keywordValidation);
        otherLocVlidation = (TextView) v.findViewById(R.id.otherLocValidation);

        currentLocBtn = (RadioButton) v.findViewById(R.id.currentLocBtn);
        currentLocBtn.setChecked(true);
        otherLocBtn = (RadioButton) v.findViewById(R.id.otherLocBtn);

        searchBtn = (Button) v.findViewById(R.id.searchBtn);
        clearBtn = (Button) v.findViewById(R.id.clearBtn);

        categoryInput = (Spinner) v.findViewById(R.id.categoryInput);
        categoryAdapter = ArrayAdapter.createFromResource(getContext(), R.array.categoryName, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categoryInput.setAdapter(categoryAdapter);



//    created to test output
        editText2 = (EditText) v.findViewById(R.id.editText2);



    }

    private void searchPlaces() {
        boolean isValid = true;
//        init request queue
        requestQueue = Volley.newRequestQueue(getActivity());

//        Trim removes first and last space of a string entered by a user.
//        if the keyword input is empty:
        if(keywordInput.getText().toString().trim().isEmpty()) {
            keywordValidation.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), R.string.validationToast, Toast.LENGTH_LONG).show();
            isValid = false;
        }
//        if otherLocBtn is checked and the other location input is empty:
        if(otherLocBtn.isChecked()) {
            if(otherLocInput.getText().toString().trim().isEmpty()) {
                otherLocVlidation.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), R.string.validationToast, Toast.LENGTH_LONG).show();
                isValid = false;
            }
        }

        if(isValid) {
//            1. keyword input
            keyword = keywordInput.getText().toString();
//            2. distance input (handle the case that user uses the default distance value)
            if(distanceInput.getText().length() == 0) {
                distance = 10;
            } else {
                distance = Integer.valueOf(distanceInput.getText().toString());
            }
//            3. category input
            category = "default";
            if(!selectedCategory.equals("default")) {
                category = selectedCategory;
            }
//            4. location input
            if(otherLocInput.getText().length() == 0) {
                otherLocation = "undefined";
            } else {
                otherLocation = otherLocInput.getText().toString();
            }

            String url = "http://nodejsyutaoren.us-east-2.elasticbeanstalk.com/search?keyword="
                        + keyword + "&category=" + category + "&distance=" + distance
                        + "&geoLocation=" + lat + "," + lng + "&otherLocation=" + otherLocation;

            sendJSONRequest(url);
        }
    }

    public void sendJSONRequest(String url) {
//        show the progressing dialog
        final ShowProgressDialog showProgressDialog = new ShowProgressDialog(getActivity());
        showProgressDialog.onPreExecute();

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
        editText2.setText(url);
    }


//    check if the permission is granted or not
//    if granted, fetch the user's current location
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case REQUEST_PERMISSION_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    // Got last known location. In some rare situations this can be null.
                                    if (location != null) {

                                        lat = location.getLatitude();
                                        lng = location.getLongitude();

                                    }
                                }
                            });
                    }
                } else {
                    Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private static class ShowProgressDialog extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;

        private ShowProgressDialog(Activity activity) {
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Fetching results");
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

    private void initPlacesList(JSONObject response) {
        Intent intent = new Intent(getActivity(), PlacesListActivity.class);

        String resString = response.toString();
        intent.putExtra("ShowMeTheList", resString);
        startActivity(intent);
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
