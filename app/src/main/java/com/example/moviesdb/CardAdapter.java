package com.example.moviesdb;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardAdapterViewHolder> {

    private ArrayList<CardItem> mCardItems;

    public CardAdapter(ArrayList<CardItem> cardItems) {
        mCardItems = cardItems;
    }

    @NonNull
    @Override
    public CardAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CardAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapterViewHolder holder, int position) {
        CardItem currentItem = mCardItems.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
    }

    @Override
    public int getItemCount() {
        return mCardItems.size();
    }

    static class CardAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        public CardAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.card_image_view);
        }
    }
}
