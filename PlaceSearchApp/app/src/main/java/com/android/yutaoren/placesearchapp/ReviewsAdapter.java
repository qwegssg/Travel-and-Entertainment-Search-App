package com.android.yutaoren.placesearchapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<ReviewItem> reviewItems;

    public ReviewsAdapter(List<ReviewItem> reviewItems) {
        this.reviewItems = reviewItems;
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

        final ReviewItem reviewItem = reviewItems.get(position);

        holder.textViewName.setText(reviewItem.getReviewName());
        holder.textViewContent.setText(reviewItem.getReviewContent());
        Picasso.get()
                .load(reviewItem.getReviewPhotoUrl())
                .into(holder.imageViewPhoto);
    }

    @Override
    public int getItemCount() {
        return reviewItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewContent;
        private ImageView imageViewPhoto;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.reviewName);
            textViewContent = (TextView) itemView.findViewById(R.id.reviewContent);
            imageViewPhoto = (ImageView) itemView.findViewById(R.id.reviewPhoto);
        }
    }
}