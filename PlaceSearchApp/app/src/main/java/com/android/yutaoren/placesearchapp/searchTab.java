package com.android.yutaoren.placesearchapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


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

    private EditText keywordInput, otherLocInput, distanceInput;
    private TextView keywordValidation;
    private TextView otherLocVlidation;
    private Button searchBtn, clearBtn;
    private RadioButton currentLocBtn, otherLocBtn;

    String keyword, otherLocation;
    int distance;


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

//        request the permission from the user.
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
        }
//        else {
//            Toast.makeText(getActivity(), "Permission (already) Granted!", Toast.LENGTH_SHORT).show();
//        }

        initSearchWidgets(searchView);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPlaces();
            }
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
    }

    private void searchPlaces() {
        boolean isValid = true;

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
            keyword = keywordInput.getText().toString();
            distance = Integer.valueOf(distanceInput.getText().toString());

//            Toast.makeText(getActivity(), keyword, Toast.LENGTH_LONG).show();
//            Toast.makeText(getActivity(), String.valueOf(distance), Toast.LENGTH_LONG).show();
        }
    }

//    check if the permission is granted or not
//    if granted, fetch the user's current location
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case REQUEST_PERMISSION_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getActivity(), "Permission Granted!", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                return;
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
