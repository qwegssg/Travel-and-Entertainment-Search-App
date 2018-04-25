package com.android.yutaoren.placesearchapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class YelpReviewsAdapter extends RecyclerView.Adapter<YelpReviewsAdapter.ViewHolder>{

    private List<YelpReviewItem> yelpReviewItems;
    private Context context;

    public YelpReviewsAdapter(List<YelpReviewItem> yelpReviewItems, Context context) {
        this.yelpReviewItems = yelpReviewItems;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final YelpReviewItem yelpReviewItem = yelpReviewItems.get(position);

        holder.textViewName.setText(yelpReviewItem.getYelpName());
        holder.textViewContent.setText(yelpReviewItem.getYelpText());
        Picasso.get()
                .load(yelpReviewItem.getYelpPhotoUrl())
                .into(holder.imageViewPhoto);
        holder.reviewsListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(yelpReviewItem.getYelpUrl()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.reviewsRating.setRating(yelpReviewItem.getYelpRating());
        holder.textViewTime.setText(yelpReviewItem.getYelpTime());
    }

    @Override
    public int getItemCount() {
        return yelpReviewItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewContent;
        private ImageView imageViewPhoto;
        private RelativeLayout reviewsListLayout;
        private RatingBar reviewsRating;
        private TextView textViewTime;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.reviewName);
            textViewContent = (TextView) itemView.findViewById(R.id.reviewContent);
            imageViewPhoto = (ImageView) itemView.findViewById(R.id.reviewPhoto);
            reviewsListLayout = (RelativeLayout) itemView.findViewById(R.id.reviewsListLayout);
            reviewsRating = (RatingBar) itemView.findViewById(R.id.reviewRating);
            textViewTime = (TextView) itemView.findViewById(R.id.reviewTime);
        }
    }
}
