package com.android.yutaoren.placesearchapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link favoritesTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link favoritesTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class favoritesTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public favoritesTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment favoritesTab.
     */
    // TODO: Rename and change types and number of parameters
    public static favoritesTab newInstance(String param1, String param2) {
        favoritesTab fragment = new favoritesTab();
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

        View view = inflater.inflate(R.layout.fragment_favorites_tab, container, false);

        List<PlaceItem> favPlaceItems = new ArrayList<>();
        String key = "com.android.yutaoren.placesearchapp.key";
        SharedPreferences prefs = getContext().getSharedPreferences(
                "com.android.yutaoren.placesearchapp", Context.MODE_PRIVATE);
        Gson gson = new Gson();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.favRecyclerView);
        TextView noFavs = (TextView) view.findViewById(R.id.noFavorites);



        if(prefs.getAll().size() == 0) {
            noFavs.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            noFavs.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            String json = prefs.getString(key, "yutaoren");
            favPlaceItems = gson.fromJson(json, new TypeToken<List<PlaceItem>>(){}.getType());

            RecyclerView.Adapter adapter = new FavListAdapter(favPlaceItems, getContext(), favoritesTab.this);
//        set the fixed view size
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    //    fetch the place details info
    public void generateUrl(String place_id, String placeTitle) {
        String theUrl = "http://nodejsyutaoren.us-east-2.elasticbeanstalk.com/detail?placeid=" + place_id;
        sendJSONRequestforDetail(theUrl);
    }

    private void sendJSONRequestforDetail(String theUrl) {
//        show the progressing dialog
        final ShowProgressDialog showProgressDialog = new ShowProgressDialog(getActivity());
        showProgressDialog.onPreExecute();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
        Intent intent = new Intent(getContext(), PlaceDetailActivity.class);

        String resString = response.toString();
        intent.putExtra("ShowMeTheDetail", resString);
        startActivity(intent);
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
