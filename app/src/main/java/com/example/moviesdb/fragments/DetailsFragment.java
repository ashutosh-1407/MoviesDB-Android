package com.example.moviesdb.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.os.DeadObjectException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.moviesdb.MainActivity;
import com.example.moviesdb.QueryUtils;
import com.example.moviesdb.R;
import com.example.moviesdb.ReviewsActivity;
import com.example.moviesdb.model.CardAdapter;
import com.example.moviesdb.model.CastAdapter;
import com.example.moviesdb.model.CastItem;
import com.example.moviesdb.model.MediaItem;
import com.example.moviesdb.model.ReviewAdapter;
import com.example.moviesdb.model.ReviewItem;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView mRecyclerView;
    private CardAdapter mCardAdapter;
    private ArrayList<MediaItem> recommendedItems = new ArrayList<>();

    private RecyclerView mRecyclerView1;
    private ReviewAdapter mReviewAdapter1;
    private ArrayList<ReviewItem> reviewItems = new ArrayList<>();

    private RecyclerView mRecyclerView2;
    private CastAdapter castAdapter;
    private ArrayList<CastItem> castItems = new ArrayList<>();
//    RecyclerView.LayoutManager layoutManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int mParam2;

    private String mediaType, shareUrl;
    private int mediaId;
    private Intent browserIntent;

    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, int param2) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
            mediaType = mParam1;
            mediaId = mParam2;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().findViewById(R.id.bottomNavigationView).setVisibility(View.GONE);
        final View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        final YouTubePlayerView youTubePlayerView = rootView.findViewById(R.id.media_trailer);
        final TextView nameTextView = rootView.findViewById(R.id.media_name);
        final TextView overviewTextView = rootView.findViewById(R.id.media_overview);
        final TextView genresTextView = rootView.findViewById(R.id.media_genres);
        final TextView yearTextView = rootView.findViewById(R.id.media_year);
        final ImageView watchImageView = rootView.findViewById(R.id.media_watchlist);
        final ImageView fbTextView = rootView.findViewById(R.id.media_facebook);
        final ImageView twitterTextView = rootView.findViewById(R.id.media_twitter);
        mRecyclerView = rootView.findViewById(R.id.reco_picks_recycler_view);
        mRecyclerView1 = rootView.findViewById(R.id.reviews_recycler_view);
        mRecyclerView2 = rootView.findViewById(R.id.cast_recycler_view);
        final ProgressBar progressBar = rootView.findViewById(R.id.details_progress_view);
        final LinearLayout linearLayout = rootView.findViewById(R.id.details_content);

        String urlStart = "http://10.0.2.2:8080";
        String url = "";
        if (mediaType == null && mediaId == 0 && getArguments() != null) {
            DetailsFragmentArgs args = DetailsFragmentArgs.fromBundle(getArguments());
            mediaType = args.getMediaType();
            mediaId = args.getMediaId();
        }
        if (mediaType.equals("movie")) {
            url = urlStart + "/apis/watch/" + mediaType + "/" + mediaId;
        } else if (mediaType.equals("tv")) {
            url = urlStart + "/apis/watch/" + mediaType + "/" + mediaId;
        }
        final RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        Context context = getContext();
                        final JSONObject jsonObject = new JSONObject(response);
                        getLifecycle().addObserver(youTubePlayerView);
                        final String videoId = jsonObject.getString("trailer");
                        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                youTubePlayer.loadVideo(videoId, 0);
                            }
                        });
                        nameTextView.setText(jsonObject.getString("title"));
                        overviewTextView.setText(jsonObject.getString("overview"));
                        genresTextView.setText(jsonObject.getString("genres"));
                        yearTextView.setText(jsonObject.getString("year"));
                        if (QueryUtils.checkMedia(Objects.requireNonNull(getContext()), mediaType, mediaId)) {
                            watchImageView.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
                        } else {
                            watchImageView.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
                        }
                        watchImageView.setOnClickListener(new View.OnClickListener() {
                            String mediaUrl = jsonObject.getString("poster_path");
                            String mediaName = jsonObject.getString("title");

                            @Override
                            public void onClick(View v) {
                                if (QueryUtils.checkMedia(Objects.requireNonNull(getContext()), mediaType, mediaId)) {
                                    QueryUtils.removeMedia(Objects.requireNonNull(getContext()), mediaType, mediaId);
                                    watchImageView.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
                                    Toast.makeText(getContext(), mediaName + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
                                } else {
                                    QueryUtils.addMedia(Objects.requireNonNull(getContext()), mediaType, mediaId, mediaUrl, mediaName);
                                    watchImageView.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
                                    Toast.makeText(getContext(), mediaName + " was added to Watchlist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        fbTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shareUrl = "https://www.themoviedb.org/" + mediaType + "/" + mediaId;
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl));
                                startActivity(browserIntent);
                            }
                        });
                        twitterTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                shareUrl = "https://www.themoviedb.org/" + mediaType + "/" + mediaId;
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl));
                                startActivity(browserIntent);
                            }
                        });

                        /* to change
                         */

                        reviewItems = QueryUtils.parseReviewsFromResponse(jsonObject.getJSONArray("reviews"));
                        mReviewAdapter1 = new ReviewAdapter(reviewItems, context);
                        mRecyclerView1.setHasFixedSize(true);
                        mRecyclerView1.setAdapter(mReviewAdapter1);
                        mReviewAdapter1.setOnItemClickListener(new ReviewAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(getActivity(), ReviewsActivity.class);
                                intent.putExtra("label", reviewItems.get(position).getLabel());
                                intent.putExtra("rating", reviewItems.get(position).getRating());
                                intent.putExtra("overview", reviewItems.get(position).getContent());
                                startActivity(intent);
                            }
                        });



                        castItems = QueryUtils.parseCastsFromResponse(jsonObject.getJSONArray("cast"));
                        castAdapter = new CastAdapter(castItems, context);
                        mRecyclerView2.setHasFixedSize(true);
                        mRecyclerView2.setAdapter(castAdapter);


                        /* to change
                         */

                        recommendedItems = QueryUtils.parseMediaFromResponse(jsonObject.getJSONArray("reco_picks")).get("recommended");
                        mCardAdapter = new CardAdapter(recommendedItems, context, "details");
                        mRecyclerView.setHasFixedSize(true);
                        mRecyclerView.setAdapter(mCardAdapter);
                        mCardAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                String mediaType = recommendedItems.get(position).getType();
                                int mediaId = recommendedItems.get(position).getId();
//                                final NavController navController = Navigation.findNavController(rootView);
//                                MovieTVFragmentDirections.ActionMovieTVFragmentToDetailsFragment action = MovieTVFragmentDirections.actionMovieTVFragmentToDetailsFragment(mediaType, mediaId);
//                                navController.navigate(action);
                                DetailsFragment detailsFragment = DetailsFragment.newInstance(mediaType, mediaId);
                                if (getFragmentManager() != null) {
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fragment, detailsFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            }
                            @Override
                            public void onOptionsClick(int position) {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}