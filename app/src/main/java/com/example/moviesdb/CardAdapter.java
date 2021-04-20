package com.example.moviesdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardAdapterViewHolder> {

    private ArrayList<CardItem> mCardItems;
    private Context mContext;
    private OnItemClickListener mListener;

    public CardAdapter(ArrayList<CardItem> cardItems, Context context) {
        mCardItems = cardItems;
        mContext = context;
    }

    @NonNull
    @Override
    public CardAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CardAdapterViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapterViewHolder viewHolder, int position) {
        CardItem currentItem = mCardItems.get(position);
        Picasso.with(mContext).load(currentItem.getImgUrl()).into(viewHolder.mMovieImage);
    }

    @Override
    public int getItemCount() {
        return mCardItems.size();
    }

    public static class CardAdapterViewHolder extends RecyclerView.ViewHolder {
        public ImageView mMovieImage;
        public ImageView mOptionsImage;
        public CardAdapterViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mMovieImage = itemView.findViewById(R.id.card_image_view);
            mOptionsImage = itemView.findViewById(R.id.options_image_view);
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

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onOptionsClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
