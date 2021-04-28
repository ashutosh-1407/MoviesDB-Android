package com.example.moviesdb.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesdb.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private ArrayList<ReviewItem> mReviewItems;
    private Context mContext;
    private OnItemClickListener mListener;

    public ReviewAdapter(ArrayList<ReviewItem> reviewItems, Context context) {
        mReviewItems = reviewItems;
        mContext = context;
    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewAdapterViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder viewHolder, int position) {
        ReviewItem currentItem = mReviewItems.get(position);
        viewHolder.mLabelTextView.setText(currentItem.getLabel());
        viewHolder.mRatingTextView.setText(currentItem.getRating());
        viewHolder.mContentTextView.setText(currentItem.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewItems.size();
    }

    public static class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView mLabelTextView, mRatingTextView, mContentTextView;
        public ReviewAdapterViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            mLabelTextView = itemView.findViewById(R.id.review_label);
            mRatingTextView = itemView.findViewById(R.id.review_rating);
            mContentTextView = itemView.findViewById(R.id.review_overview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ReviewAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

}
