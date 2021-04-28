package com.example.moviesdb.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesdb.helper.ItemTouchHelperAdapter;
import com.example.moviesdb.helper.ItemTouchHelperCallback;
import com.example.moviesdb.helper.OnStartDragListener;
import com.example.moviesdb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardAdapterViewHolder> {

    private ArrayList<MediaItem> mCardItems;
    private Context mContext;
    private OnItemClickListener mListener;
//    OnStartDragListener listener;
//
//    @Override
//    public boolean onItemMove(int fromPosition, int toPosition) {
//        Collections.swap(mCardItems, fromPosition, toPosition);
//        notifyItemMoved(fromPosition, toPosition);
//        return true;
//    }
//
//    @Override
//    public void onItemDismiss(int position) {
//        mCardItems.remove(position);
//        notifyItemRemoved(position);
//    }

    private static String mPage;

    public CardAdapter(ArrayList<MediaItem> cardItems, Context context, String page) {
        mCardItems = cardItems;
        mContext = context;
        mPage = page;
    }

//    public CardAdapter(ArrayList<MediaItem> cardItems, Context context, String page, OnStartDragListener listener) {
//        mCardItems = cardItems;
//        mContext = context;
//        mPage = page;
//        this.listener = listener;
//    }

    @NonNull
    @Override
    public CardAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView;
        if (mPage.equals("search")) rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        else rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_item, parent, false);
        return new CardAdapterViewHolder(rootView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapterViewHolder viewHolder, int position) {
        MediaItem currentItem = mCardItems.get(position);
        Picasso.with(mContext).load(currentItem.getImgUrl()).into(viewHolder.mMovieImage);
        if (mPage.equals("search")) {
            String label = mCardItems.get(position).getType() + "(" + mCardItems.get(position).getYear() + ")";
            viewHolder.mLabelTextView.setText(label);
            viewHolder.mRatingTextView.setText(mCardItems.get(position).getRating());
            viewHolder.mName.setText(mCardItems.get(position).getName());
        } else if (mPage.equals("watchlist")) {
            viewHolder.mName.setText(mCardItems.get(position).getType());
//            viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    final int action =  event.getAction();
//                    if (action == MotionEvent.ACTION_DOWN)
//                        listener.onStartDrag(viewHolder);
//                    return false;
//                }
//            });
        }
    }

    @Override
    public int getItemCount() {
        return mCardItems.size();
    }

    public static class CardAdapterViewHolder extends RecyclerView.ViewHolder {
        public ImageView mMovieImage, mOptionsImage;
        public TextView mLabelTextView, mRatingTextView, mName;
        public CardAdapterViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            if (mPage.equals("search")) {
                mMovieImage = itemView.findViewById(R.id.media_image);
                mLabelTextView = itemView.findViewById(R.id.media_label);
                mRatingTextView = itemView.findViewById(R.id.media_rating);
                mName = itemView.findViewById(R.id.media_name);
            } else {
                mMovieImage = itemView.findViewById(R.id.media_image);
                mOptionsImage = itemView.findViewById(R.id.media_options);
                mName = itemView.findViewById(R.id.media_name);
            }
            if (mPage.equals("watchlist")) mOptionsImage.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
            else if (mPage.equals("details")) mOptionsImage.setVisibility(View.GONE);
            mMovieImage.setOnClickListener(new View.OnClickListener() {
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
            if (!mPage.equals("search")) {
                mOptionsImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onOptionsClick(position);
                            }
                        }
                    }
                });
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onOptionsClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
