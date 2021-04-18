package com.example.moviesdb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieTVFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieTVFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SliderView mSliderView;
    private SliderAdapter mSliderAdapter;
    private RecyclerView mRecylclerView;
    private RecyclerView mRecylclerView1;
    private CardAdapter mCardAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String url1 = "https://www.geeksforgeeks.org/wp-content/uploads/gfg_200X200-1.png";
    String url2 = "https://qphs.fs.quoracdn.net/main-qimg-8e203d34a6a56345f86f1a92570557ba.webp";
    String url3 = "https://bizzbucket.co/wp-content/uploads/2020/08/Life-in-The-Metro-Blog-Title-22.png";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MovieTVFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieTVFragment newInstance(String param1, String param2) {
        MovieTVFragment fragment = new MovieTVFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_tv, container, false);

        ArrayList<SliderItem> sliderDataArrayList = new ArrayList<>();
        sliderDataArrayList.add(new SliderItem(url1));
        sliderDataArrayList.add(new SliderItem(url2));
        sliderDataArrayList.add(new SliderItem(url3));
        mSliderView = rootView.findViewById(R.id.slider_view);
        mSliderAdapter = new SliderAdapter(getContext(), sliderDataArrayList);
        mSliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        mSliderView.setSliderAdapter(mSliderAdapter);
        mSliderView.setScrollTimeInSec(3);
        mSliderView.setAutoCycle(true);
        mSliderView.startAutoCycle();

        ArrayList<CardItem> recyclerArrayList = new ArrayList<>();
        recyclerArrayList.add(new CardItem(R.drawable.ic_baseline_remove_circle_outline_24));
        recyclerArrayList.add(new CardItem(R.drawable.ic_baseline_remove_circle_outline_24));
        recyclerArrayList.add(new CardItem(R.drawable.ic_baseline_remove_circle_outline_24));
        recyclerArrayList.add(new CardItem(R.drawable.ic_baseline_remove_circle_outline_24));
        recyclerArrayList.add(new CardItem(R.drawable.ic_baseline_remove_circle_outline_24));
        recyclerArrayList.add(new CardItem(R.drawable.ic_baseline_remove_circle_outline_24));
        mRecylclerView = rootView.findViewById(R.id.recycler_view);
        mRecylclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        mCardAdapter = new CardAdapter(recyclerArrayList);
        mRecylclerView.setLayoutManager(mLayoutManager);
        mRecylclerView.setAdapter(mCardAdapter);

        mRecylclerView1 = rootView.findViewById(R.id.recycler_view1);
        mRecylclerView1.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        mCardAdapter = new CardAdapter(recyclerArrayList);
        mRecylclerView1.setLayoutManager(mLayoutManager);
        mRecylclerView1.setAdapter(mCardAdapter);
        return rootView;
    }
}