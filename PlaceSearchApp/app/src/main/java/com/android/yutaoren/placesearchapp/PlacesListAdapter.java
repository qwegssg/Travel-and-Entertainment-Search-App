package com.android.yutaoren.placesearchapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.ViewHolder> {

    private List<PlaceItem> placeItems;
    private Context context;

    public PlacesListAdapter(List<PlaceItem> placeItems, Context context) {
        this.placeItems = placeItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_item, parent, false);
        return new ViewHolder(view);
    }

//    bind the view with data
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final PlaceItem placeItem = placeItems.get(position);
        final Gson gson = new Gson();

        holder.textViewTitle.setText(placeItem.getPlaceTitle());
        holder.textViewAddress.setText(placeItem.getPlaceAddress());

        Picasso.get()
                .load(placeItem.getImageUrl())
                .into(holder.imageViewCategory);

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PlacesListActivity)context).onClickCalled(placeItem.getPlace_id(), placeItem.getPlaceTitle());
            }
        });




//        fetch the fav places data
        final SharedPreferences prefs = context.getSharedPreferences(
                                        "com.android.yutaoren.placesearchapp", Context.MODE_PRIVATE);
        final String key = "com.android.yutaoren.placesearchapp.key";

//        prefs.edit().clear().apply();
//        Toast.makeText(context, prefs.getAll().size() + "", Toast.LENGTH_LONG).show();

//        init the fav status of the place list
        if(!prefs.getAll().isEmpty()) {
            for(int i = 0; i < prefs.getAll().size(); i++) {
                for(int j = 0; j < placeItems.size(); j++) {
                    if(prefs.contains(key + placeItems.get(j).getPlace_id())) {
                        String json = prefs.getString(key + placeItems.get(j).getPlace_id(), "yutaoren");
                        PlaceItem place = gson.fromJson(json, PlaceItem.class);
                        if (place.getPlace_id().equals(placeItem.getPlace_id())) {
                            holder.imageButtonFav.setBackgroundResource(R.drawable.heart_fill_red);
                        }
                    }
                }
            }
        }

        holder.imageButtonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if the place is already added to the fav list
                if(holder.imageButtonFav.getBackground().getConstantState().equals(
                        context.getDrawable(R.drawable.heart_fill_red).getConstantState())) {

                    for(int i = 0; i < prefs.getAll().size(); i++) {
                        for(int j = 0; j < placeItems.size(); j++) {
                            if(prefs.contains(key + placeItems.get(j).getPlace_id())) {
                                String json = prefs.getString(key + placeItems.get(j).getPlace_id(), "yutaoren");
                                PlaceItem place = gson.fromJson(json, PlaceItem.class);
                                if (place.getPlace_id().equals(placeItem.getPlace_id())) {
                                    prefs.edit().remove(key + placeItems.get(j).getPlace_id()).apply();
                                }
                            }
                        }
                    }

                    Toast.makeText(context, placeItem.getPlaceTitle()+ " was removed from favorites", Toast.LENGTH_LONG).show();
                    holder.imageButtonFav.setBackgroundResource(R.drawable.heart_outline_black);

                }
//                if the place has not added into the fav list yet
                else if(holder.imageButtonFav.getBackground().getConstantState().equals(
                        context.getDrawable(R.drawable.heart_outline_black).getConstantState())) {

                    String placeItemFavKey = "com.android.yutaoren.placesearchapp.key" + placeItem.getPlace_id();
                    String placeItemFavJson = gson.toJson(placeItem);
                    prefs.edit().putString(placeItemFavKey, placeItemFavJson).apply();

                    Toast.makeText(context, placeItem.getPlaceTitle()+ " was added to favorites", Toast.LENGTH_LONG).show();
                    holder.imageButtonFav.setBackgroundResource(R.drawable.heart_fill_red);

                }


            }
        });
    }

    @Override
    public int getItemCount() {

        return placeItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //        define view objects
        private TextView textViewTitle;
        private TextView textViewAddress;
        private ImageView imageViewCategory;
        private RelativeLayout relativeLayout;
        private ImageButton imageButtonFav;


        public ViewHolder(View itemView) {
            super(itemView);

            textViewTitle = (TextView) itemView.findViewById(R.id.placeTitle);
            textViewAddress = (TextView) itemView.findViewById(R.id.placeAddress);
            imageViewCategory = (ImageView) itemView.findViewById(R.id.placeCategory);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.placesListLayout);
            imageButtonFav = (ImageButton) itemView.findViewById(R.id.favBtnInList);
        }
    }
}
