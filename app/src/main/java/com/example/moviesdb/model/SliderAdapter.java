package com.example.moviesdb.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.moviesdb.activity.DetailsActivity;
import com.example.moviesdb.R;

import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {
    private ArrayList<MediaItem> mSliderItems;
    private Context mContext;

    public SliderAdapter(ArrayList<MediaItem> sliderItems, Context context) {
        this.mSliderItems = sliderItems;
        mContext = context;
    }

    @Override
    public SliderAdapter.SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item, parent, false);
        return new SliderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SliderAdapter.SliderAdapterViewHolder viewHolder, int position) {
        final MediaItem currentItem = mSliderItems.get(position);
//        Picasso.with(mContext).load(currentItem.getImgUrl()).into(viewHolder.mImageView1);
        Glide.with(viewHolder.itemView)
                .load(currentItem.getImgUrl())
                .fitCenter()
                .into(viewHolder.mImageView1);

        Glide.with(viewHolder.itemView)
                .load(currentItem.getImgUrl())
                .override(50, 50)
                .fitCenter()
                .into(viewHolder.mImageView2);

        viewHolder.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mediaType = currentItem.getType();
                int mediaId = currentItem.getId();
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("mediaType", mediaType);
                intent.putExtra("mediaId", mediaId);
                mContext.startActivity(intent);
//                final NavController navController = Navigation.findNavController(viewHolder.mItemView);
//                MovieTVFragmentDirections.ActionMovieTVFragmentToDetailsFragment action = MovieTVFragmentDirections.actionMovieTVFragmentToDetailsFragment(mediaType, mediaId);
//                navController.navigate(action);
            }
        });
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        View mItemView;
        ImageView mImageView1, mImageView2;
        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView1 = itemView.findViewById(R.id.slides_image_view);
            mImageView2 = itemView.findViewById(R.id.overlay_image_view);
            this.mItemView = itemView;
        }
    }

}
