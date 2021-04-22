package com.example.moviesdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

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
    public void onBindViewHolder(SliderAdapter.SliderAdapterViewHolder viewHolder, int position) {
        MediaItem currentItem = mSliderItems.get(position);
        Picasso.with(mContext).load(currentItem.getImgUrl()).into(viewHolder.mImageView);
//        Glide.with(viewHolder.itemView)
//                .load(currentItem.getImgUrl())
//                .fitCenter()
//                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    public static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        View mItemView;
        ImageView mImageView;
        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.slides_image_view);
            this.mItemView = itemView;
        }
    }
}
