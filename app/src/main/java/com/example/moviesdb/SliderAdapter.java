package com.example.moviesdb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {
    private ArrayList<CardItem> sliderItems;

    public SliderAdapter(Context context, ArrayList<CardItem> sliderItems) {
        this.sliderItems = sliderItems;
    }

    @Override
    public SliderAdapter.SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, parent, false);
        return new SliderAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapter.SliderAdapterViewHolder viewHolder, int position) {
        CardItem currentItem = sliderItems.get(position);
        Glide.with(viewHolder.itemView)
                .load(currentItem.getImgUrl())
                .fitCenter()
                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return sliderItems.size();
    }

    public static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageView;
        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slides_image_view);
            this.itemView = itemView;
        }
    }
}
