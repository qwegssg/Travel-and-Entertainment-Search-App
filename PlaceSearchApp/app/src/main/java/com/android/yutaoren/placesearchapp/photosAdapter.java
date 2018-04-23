package com.android.yutaoren.placesearchapp;

import android.content.Context;
import android.graphics.Bitmap;
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

public class photosAdapter extends RecyclerView.Adapter<photosAdapter.ViewHolder> {

    private List<Bitmap> photoList;

    public photosAdapter(List<Bitmap> photoList) {
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_item, parent, false);
        return new ViewHolder(view);
    }

    //    bind the view with data
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Bitmap bitmap = photoList.get(position);

        holder.placeDetailPhoto.setImageBitmap(bitmap);

    }

    @Override
    public int getItemCount() {

        return photoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView placeDetailPhoto;


        public ViewHolder(View itemView) {
            super(itemView);

            placeDetailPhoto = (ImageView) itemView.findViewById(R.id.placeDetailPhoto);

        }
    }
}
