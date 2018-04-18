package com.android.yutaoren.placesearchapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.ViewHoder> {

    private List<PlaceItem> placeItems;
    private Context context;

    public PlacesListAdapter(List<PlaceItem> placeItems, Context context) {
        this.placeItems = placeItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_item, parent, false);
        return new ViewHoder(view);
    }

//    bind the view with data
    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        final PlaceItem placeItem = placeItems.get(position);

        holder.textViewTitle.setText(placeItem.getPlaceTitle());
        holder.textViewAddress.setText(placeItem.getPlaceAddress());

        Picasso.get()
                .load(placeItem.getImageUrl())
                .into(holder.imageViewCategory);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You clicked " + placeItem.getPlaceTitle(), Toast.LENGTH_LONG ).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        return placeItems.size();
    }

    public class ViewHoder extends RecyclerView.ViewHolder {

        //        define view objects
        private TextView textViewTitle;
        private TextView textViewAddress;
        private ImageView imageViewCategory;
        private RelativeLayout relativeLayout;


        public ViewHoder(View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.placeTitle);
            textViewAddress = (TextView) itemView.findViewById(R.id.placeAddress);
            imageViewCategory = (ImageView) itemView.findViewById(R.id.placeCategory);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.placesListLayout);
        }
    }
}
