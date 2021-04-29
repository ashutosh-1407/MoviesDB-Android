package com.example.moviesdb.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.moviesdb.activity.DetailsActivity;
import com.example.moviesdb.utility.QueryUtils;
import com.example.moviesdb.R;
import com.example.moviesdb.model.CardAdapter;
import com.example.moviesdb.model.MediaItem;
import com.example.moviesdb.model.SliderAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    private ArrayList<MediaItem> watchlistItems;
    private BottomNavigationView bottomNavigationView;

    TextView movieTextView;
    TextView tvTextView;
    String mMediatype = "movie";
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
            if (mParam1 != null) mMediatype = mParam1;
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
        TextView footerTextView = rootView.findViewById(R.id.footer);
        final ProgressBar progressBar = rootView.findViewById(R.id.progress_view);
        final TextView loadingText = rootView.findViewById(R.id.loading_text);
        final LinearLayout linearLayout = rootView.findViewById(R.id.content);

        movieTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediatype.equals("tv")) {
                    activateFragment("movie");
                }
            }
        });

        tvTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediatype.equals("movie")) {
                    activateFragment("tv");
                }
            }
        });

        footerTextView.setOnClickListener(new View.OnClickListener() {
            String url = "https://themoviedb.org";
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        });

        String urlStart = "http://10.0.2.2:8080";
        String url = "";

        if (mMediatype == null || mMediatype.equals("movie")) {
            url = urlStart + "/apis/movieSearch";
            mMediatype = "movie";
        } else if (mMediatype.equals("tv")) {
            url = urlStart + "/apis/tvSearch";
            mMediatype = "tv";
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
                        loadingText.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);
                        getActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.VISIBLE);
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
        mSliderView.setSliderAdapter(mSliderAdapter);
        mSliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        mSliderView.setScrollTimeInSec(3);
        mSliderView.setAutoCycle(true);
        mSliderView.startAutoCycle();
    }

    private void setCardData(final ArrayList<MediaItem> cardItems, final View rootView, String str) {
        if (str.equals("popular")) mRecyclerView = rootView.findViewById(R.id.popular_recycler_view);
        else if (str.equals("top-rated")) mRecyclerView = rootView.findViewById(R.id.top_rated_recycler_view);
        mCardAdapter = new CardAdapter(cardItems, getContext(), "main");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mCardAdapter);
        mCardAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            int mediaId;
            String mediaType, mediaUrl, mediaName;
            @Override
            public void onItemClick(int position) {
                mediaId = cardItems.get(position).getId();
                mediaType = cardItems.get(position).getType();
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("mediaType", mediaType);
                intent.putExtra("mediaId", mediaId);
                startActivity(intent);
//                final NavController navController = Navigation.findNavController(rootView);
//                MovieTVFragmentDirections.ActionMovieTVFragmentToDetailsFragment action = MovieTVFragmentDirections.actionMovieTVFragmentToDetailsFragment(mediaType, mediaId);
//                navController.navigate(action);
            }
            @Override
            public void onOptionsClick(final int position) {
                mediaId = cardItems.get(position).getId();
                mediaType = cardItems.get(position).getType();
                mediaUrl = cardItems.get(position).getImgUrl();
                mediaName = cardItems.get(position).getName();
                final PopupMenu popupMenu = new PopupMenu(getContext(), rootView.findViewById(R.id.media_options));
                if (QueryUtils.checkMedia(requireContext(), mediaType, mediaId))
                    popupMenu.getMenu().add(Menu.NONE, 101, 3, "Remove from Watchlist");
                else
                    popupMenu.getMenu().add(Menu.NONE, 102, 3, "Add to Watchlist");
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String url, data;
                        Intent browserIntent;
                        switch (item.getItemId()){
                            case R.id.tmdb_item:
                                url = "https://www.themoviedb.org/" + mediaType + "/" + mediaId;
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);
                                break;
                            case R.id.fb_item:
                                data = "https://www.themoviedb.org/" + mediaType + "/" + mediaId;
                                url = "https://www.facebook.com/sharer.php?u=" + data;
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);
                                break;
                            case R.id.twitter_item:
                                data = "Check this out!\nhttps://www.themoviedb.org/" + mediaType + "/" + mediaId;
                                url = "https://twitter.com/intent/tweet?text=" + data;
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);
                                break;
                            case 101:
                                QueryUtils.removeMedia(requireContext(), mediaType, mediaId);
                                Toast.makeText(getContext(), mediaName + " was removed from Watchlist", Toast.LENGTH_LONG).show();
                                break;
                            case 102:
                                QueryUtils.addMedia(requireContext(), mediaType, mediaId, mediaUrl, mediaName);
                                Toast.makeText(getContext(), mediaName + " was added to Watchlist" , Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();

            }
        });

    }

    private void activateFragment(String str) {
        mediaFragment = MovieTVFragment.newInstance(str);
        if (getFragmentManager() != null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, mediaFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMediatype.equals("tv")) {
            movieTextView.setTextColor(getResources().getColor(R.color.unselected));
            tvTextView.setTextColor(getResources().getColor(R.color.selected));
        } else if (mMediatype.equals("movie")) {
            movieTextView.setTextColor(getResources().getColor(R.color.selected));
            tvTextView.setTextColor(getResources().getColor(R.color.unselected));
        }
//        Toast.makeText(getContext(), mMediatype, Toast.LENGTH_SHORT).show();

    }
}