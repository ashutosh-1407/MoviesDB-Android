package com.example.moviesdb.model;

import android.content.Context;
import android.content.pm.LauncherActivityInfo;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesdb.R;
import com.example.moviesdb.helper.ItemTouchHelperAdapter;
import com.example.moviesdb.helper.OnStartDragListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.WatchlistAdapterViewHolder> implements ItemTouchHelperAdapter {
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mCardItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mCardItems.remove(position);
        notifyItemRemoved(position);
    }

    private ArrayList<MediaItem> mCardItems;
    private Context mContext;
    private OnItemClickListener mListener;
    OnStartDragListener listener;

    public WatchlistAdapter(ArrayList<MediaItem> cardItems, Context context, OnStartDragListener listener) {
        mCardItems = cardItems;
        mContext = context;
        this.listener = listener;
    }
    @Override
    public void onBindViewHolder(@NonNull WatchlistAdapterViewHolder viewHolder, int position) {
        MediaItem currentItem = mCardItems.get(position);
        Picasso.with(mContext).load(currentItem.getImgUrl()).into(viewHolder.mMovieImage);
        viewHolder.mName.setText(mCardItems.get(position).getType());
        viewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = event.getAction();
                listener.onStartDrag(viewHolder);
                return false;
            }
        });
    }

    @NonNull
    @Override
    public WatchlistAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.watchlist_item, parent, false);
        return new WatchlistAdapterViewHolder(rootView, mListener);
    }

    @Override
    public int getItemCount() {
        return mCardItems.size();
    }

    public static class WatchlistAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView mMovieImage, mOptionsImage;
        public TextView mName;

        public WatchlistAdapterViewHolder(@NonNull View itemView, final WatchlistAdapter.OnItemClickListener listener) {
            super(itemView);
            mMovieImage = itemView.findViewById(R.id.media_image);
            mOptionsImage = itemView.findViewById(R.id.media_options);
            mName = itemView.findViewById(R.id.media_name);
            mOptionsImage.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
            itemView.setMinimumWidth(150);
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

    public void setOnItemClickListener(WatchlistAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
}
