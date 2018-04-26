package com.android.yutaoren.placesearchapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavListAdapter extends RecyclerView.Adapter<FavListAdapter.ViewHolder> {

    private List<PlaceItem> placeItems;
    private Context context;
    private List<PlaceItem> favPlaceItems;
    private favoritesTab favoritesTab;

    public FavListAdapter(List<PlaceItem> placeItems, Context context, favoritesTab favoritesTab) {
        this.placeItems = placeItems;
        this.context = context;
        this.favoritesTab = favoritesTab;
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
                favoritesTab.generateUrl(placeItem.getPlace_id(), placeItem.getPlaceTitle());
            }
        });

//        fetch the fav places data
        final SharedPreferences prefs = context.getSharedPreferences(
                "com.android.yutaoren.placesearchapp", Context.MODE_PRIVATE);
        final String key = "com.android.yutaoren.placesearchapp.key";

//        init the fav status of the place list
        favPlaceItems = new ArrayList<>();
        if(!prefs.getAll().isEmpty()) {
            String json = prefs.getString(key, "yutaoren");
            favPlaceItems = gson.fromJson(json, new TypeToken<List<PlaceItem>>(){}.getType());

            for(int i = 0; i < favPlaceItems.size(); i++) {
                if(favPlaceItems.get(i).getPlace_id().equals(placeItem.getPlace_id())) {
                    holder.imageButtonFav.setBackgroundResource(R.drawable.heart_fill_red);
                    break;
                } else {
                    holder.imageButtonFav.setBackgroundResource(R.drawable.heart_outline_black);
                }
            }
        }

        holder.imageButtonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String favPlaceItemsJson = "";

                if(holder.imageButtonFav.getBackground().getConstantState().equals(
                        context.getDrawable(R.drawable.heart_fill_red).getConstantState())) {

                    favPlaceItemsJson = prefs.getString(key, "yutaoren");
                    favPlaceItems = gson.fromJson(favPlaceItemsJson, new TypeToken<List<PlaceItem>>(){}.getType());
                    for(int i = 0; i < favPlaceItems.size(); i++) {
                        if(favPlaceItems.get(i).getPlace_id().equals(placeItem.getPlace_id())) {

                            favPlaceItems.remove(i);
                            favPlaceItemsJson = gson.toJson(favPlaceItems);
                            prefs.edit().putString(key, favPlaceItemsJson).apply();

                            favoritesTab.adapter = new FavListAdapter(favPlaceItems, context, favoritesTab);
                            favoritesTab.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            favoritesTab.recyclerView.setAdapter(favoritesTab.adapter);
                        }
                    }
//                    if there is no fav places in the list
                    if(favPlaceItems.size() == 0) {
                        favoritesTab.getView().findViewById(R.id.noFavorites).setVisibility(View.VISIBLE);
                        favoritesTab.getView().findViewById(R.id.favRecyclerView).setVisibility(View.INVISIBLE);
                    }

                    Toast.makeText(context, placeItem.getPlaceTitle()+ " was removed from favorites", Toast.LENGTH_LONG).show();
                    holder.imageButtonFav.setBackgroundResource(R.drawable.heart_outline_black);
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
