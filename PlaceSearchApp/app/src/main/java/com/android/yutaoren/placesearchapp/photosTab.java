package com.android.yutaoren.placesearchapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link photosTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link photosTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class photosTab extends Fragment {
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
    private List<Bitmap> photoList;
    private TextView showNoPhotos;


    public photosTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment photosTab.
     */
    // TODO: Rename and change types and number of parameters
    public static photosTab newInstance(String param1, String param2) {
        photosTab fragment = new photosTab();
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

        View view = inflater.inflate(R.layout.fragment_photos_tab, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.photosRecyclerView);
        showNoPhotos = (TextView) view.findViewById(R.id.noPhotos);
        photoList = new ArrayList<>();

//        get the place id
        PlaceDetailActivity activity = (PlaceDetailActivity) getActivity();
        getPhotos(activity.getPlace_id());



        return view;
    }

    private void getPhotos(String place_id) {
        final GeoDataClient mGeoDataClient = Places.getGeoDataClient(getActivity(), null);

        final Task<PlacePhotoMetadataResponse> photoMetadataResponse = mGeoDataClient.getPlacePhotos(place_id);
        photoMetadataResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
            @Override
            public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                // Get the list of photos.
                PlacePhotoMetadataResponse photos = task.getResult();
                // Get the PlacePhotoMetadataBuffer (metadata for all of the photos).
                PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
//                if there is not photo of the place
                if(photoMetadataBuffer.getCount() == 0) {
                    showNoPhotos.setText("No photos");
                } else {
                    for(int i = 0; i < photoMetadataBuffer.getCount(); i++) {
                        final int photoNum = photoMetadataBuffer.getCount();

                        PlacePhotoMetadata photoMetadata = photoMetadataBuffer.get(i);
                        Task<PlacePhotoResponse> photoResponse = mGeoDataClient.getPhoto(photoMetadata);
                        photoResponse.addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                            @Override
                            public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                                PlacePhotoResponse photo = task.getResult();
                                Bitmap bitmap = photo.getBitmap();
//                        scale the bitmap to fit the screen size
                                int photoWidth = 1275;
                                photoList.add(scaleBitmap(bitmap, photoWidth));
                                if(photoList.size() == photoNum) {
                                    adapter = new photosAdapter(photoList);
                                    recyclerView.setHasFixedSize(true);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    recyclerView.setAdapter(adapter);
                                }

                            }
                        });
                    }
                }
            }
        });
    }

    private Bitmap scaleBitmap(Bitmap bitmap, int photoWidth) {
        int photoHeight = bitmap.getHeight() * photoWidth / bitmap.getWidth();
        return Bitmap.createScaledBitmap(bitmap,photoWidth, photoHeight, false);
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
