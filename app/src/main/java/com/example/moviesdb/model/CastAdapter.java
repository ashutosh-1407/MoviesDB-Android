package com.example.moviesdb.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesdb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastAdapterViewHolder> {
    private ArrayList<CastItem> mCastItems;
    private Context mContext;

    public CastAdapter(ArrayList<CastItem> castItems, Context context) {
        this.mCastItems = castItems;
        mContext = context;
    }

    @NonNull
    @Override
    public CastAdapter.CastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_item, parent, false);
        return new CastAdapter.CastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.CastAdapterViewHolder viewHolder, int position) {
        CastItem currentItem = mCastItems.get(position);
        Picasso.with(mContext).load(currentItem.getImgUrl()).into(viewHolder.mImageView);
        viewHolder.mTextView.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return mCastItems.size();
    }

    public static class CastAdapterViewHolder extends RecyclerView.ViewHolder {
//        View mItemView;
        ImageView mImageView;
        TextView mTextView;
        public CastAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.cast_image);
            mTextView = itemView.findViewById(R.id.cast_text);
//            this.mItemView = itemView;
        }
    }
}
