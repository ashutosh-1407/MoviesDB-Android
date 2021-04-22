package com.example.moviesdb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieTVFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieTVFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    MovieTVFragment mediaFragment;

    private SliderView mSliderView;
    private SliderAdapter mSliderAdapter;
    private ArrayList<MediaItem> trendingItems = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private CardAdapter mCardAdapter;
    private ArrayList<MediaItem> popularItems = new ArrayList<>();
    private ArrayList<MediaItem> topRatedItems = new ArrayList<>();

    private TextView movieTextView;
    private TextView tvTextView;

    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    // TODO: Rename and change types of parameters
    private String mParam1;
//    private String mParam2;

    public MovieTVFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieTVFragment newInstance(String param1) {
        MovieTVFragment fragment = new MovieTVFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_movie_tv, container, false);

        movieTextView = rootView.findViewById(R.id.movie_text_view);
        tvTextView = rootView.findViewById(R.id.tv_text_view);
        progressBar = rootView.findViewById(R.id.progress_view);
        linearLayout = rootView.findViewById(R.id.content);

        movieTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mParam1.equals("tv")) {
                    activateFragment("movie");
                }
            }
        });

        tvTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mParam1.equals("movie")) {
                    activateFragment("tv");
                }
            }
        });

        String urlStart = "http://10.0.2.2:8080";
        String url = "";

        if (mParam1.equals("movie")) {
            url = urlStart + "/apis/movieSearch";
        } else if (mParam1.equals("tv")) {
            url = urlStart + "/apis/tvSearch";
        }

        RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        Map<String, ArrayList<MediaItem>> returnedMap = QueryUtils.parseMediaFromResponse(jsonArray);
                        trendingItems = returnedMap.get("trending");
                        popularItems = returnedMap.get("popular");
                        topRatedItems = returnedMap.get("top-rated");
                        setSliderData(trendingItems, rootView);
                        setCardData(popularItems, rootView, "popular");
                        setCardData(topRatedItems, rootView, "top-rated");
                        progressBar.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
        return rootView;
    }

    private void setSliderData(ArrayList<MediaItem> sliderItems, View rootView) {
        mSliderView = rootView.findViewById(R.id.slider_view);
        mSliderAdapter = new SliderAdapter(sliderItems, getContext());
        mSliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        mSliderView.setSliderAdapter(mSliderAdapter);
        mSliderView.setScrollTimeInSec(3);
        mSliderView.setAutoCycle(true);
        mSliderView.startAutoCycle();
    }

    private void setCardData(final ArrayList<MediaItem> cardItems, final View rootView, String str) {
        if (str.equals("popular")) mRecyclerView = rootView.findViewById(R.id.popular_recycler_view);
        else if (str.equals("top-rated")) mRecyclerView = rootView.findViewById(R.id.top_rated_recycler_view);
        mCardAdapter = new CardAdapter(cardItems, getContext());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mCardAdapter);
        mCardAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                DetailsFragment detailsFragment = DetailsFragment.newInstance(mParam1, cardItems.get(position).getId());
                if (getFragmentManager() != null) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, detailsFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
            @Override
            public void onOptionsClick(int position) {
                PopupMenu popupMenu = new PopupMenu(getContext(), rootView.findViewById(R.id.options_image_view));
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.show();
            }
        });
    }

    private void activateFragment(String str) {
        mediaFragment = MovieTVFragment.newInstance(str);
        if (getFragmentManager() != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.content, mediaFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}