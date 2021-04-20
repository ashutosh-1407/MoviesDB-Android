package com.example.moviesdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private ArrayList<ReviewItem> mReviewItems;
    private Context mContext;

    public ReviewAdapter(ArrayList<ReviewItem> reviewItems, Context context) {
        mReviewItems = reviewItems;
        mContext = context;
    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder viewHolder, int position) {
        ReviewItem currentItem = mReviewItems.get(position);
        viewHolder.labelTextView.setText(currentItem.getLabel());
        viewHolder.ratingTextView.setText(currentItem.getRating());
        viewHolder.contentTextView.setText(currentItem.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewItems.size();
    }

    public static class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView labelTextView, ratingTextView, contentTextView;
        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);
            labelTextView = itemView.findViewById(R.id.review_label);
            ratingTextView = itemView.findViewById(R.id.review_rating);
            contentTextView = itemView.findViewById(R.id.review_overview);
        }
    }
}
