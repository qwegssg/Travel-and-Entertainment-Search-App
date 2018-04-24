package com.android.yutaoren.placesearchapp;

import android.content.Context;
import android.content.Intent;
import android.media.Rating;
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
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<ReviewItem> reviewItems;
    private Context context;

    public ReviewsAdapter(List<ReviewItem> reviewItems, Context context) {
        this.reviewItems = reviewItems;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final ReviewItem reviewItem = reviewItems.get(position);

        holder.textViewName.setText(reviewItem.getReviewName());
        holder.textViewContent.setText(reviewItem.getReviewContent());
        Picasso.get()
                .load(reviewItem.getReviewPhotoUrl())
                .into(holder.imageViewPhoto);
        holder.reviewsListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(reviewItem.getReviewUrl()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.reviewsRating.setRating(reviewItem.getReviewRating());

        long unixSeconds = reviewItem.getReviewTime();
        // convert seconds to milliseconds
        Date date = new Date(unixSeconds * 1000L);
        // the format of the date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        // give a timezone reference for formatting (see comment at the bottom)
        String formattedDate = sdf.format(date);

        holder.textViewTime.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return reviewItems.size();
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