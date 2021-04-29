package com.example.moviesdb.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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
import com.example.moviesdb.utility.QueryUtils;
import com.example.moviesdb.R;
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

public class DetailsActivity extends AppCompatActivity {
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

    private String mediaType, shareUrl, data;
    private int mediaId;
    private Intent browserIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mediaType = getIntent().getStringExtra("mediaType");
        mediaId = getIntent().getIntExtra("mediaId", 0);

        final YouTubePlayerView youTubePlayerView = findViewById(R.id.media_trailer);
        final TextView nameTextView = findViewById(R.id.media_name);
        final TextView overviewTextView = findViewById(R.id.media_overview);
        final TextView genresTextView = findViewById(R.id.media_genres);
        final TextView yearTextView = findViewById(R.id.media_year);
        final ImageView watchImageView = findViewById(R.id.media_watchlist);
        final ImageView fbTextView = findViewById(R.id.media_facebook);
        final ImageView twitterTextView = findViewById(R.id.media_twitter);
        mRecyclerView = findViewById(R.id.reco_picks_recycler_view);
        mRecyclerView1 = findViewById(R.id.reviews_recycler_view);
        mRecyclerView2 = findViewById(R.id.cast_recycler_view);
        final ProgressBar progressBar = findViewById(R.id.details_progress_view);
        final TextView loadingText = findViewById(R.id.details_loading_text);
        final LinearLayout linearLayout = findViewById(R.id.details_content);

        final TextView overviewText = findViewById(R.id.overview_text);
        final TextView genresText = findViewById(R.id.genres_text);
        final TextView yearText = findViewById(R.id.year_text);
        final TextView castText = findViewById(R.id.cast_text);
        final TextView reviewsText = findViewById(R.id.reviews_text);
        final TextView recommendedText = findViewById(R.id.recommended_text);

        String urlStart = "http://10.0.2.2:8080";
        String url = "";
//        if (mediaType == null && mediaId == 0 && getArguments() != null) {
//            DetailsFragmentArgs args = DetailsFragmentArgs.fromBundle(getArguments());
//            mediaType = args.getMediaType();
//            mediaId = args.getMediaId();
//        }
        if (mediaType.equals("movie")) {
            url = urlStart + "/apis/watch/" + mediaType + "/" + mediaId;
        } else if (mediaType.equals("tv")) {
            url = urlStart + "/apis/watch/" + mediaType + "/" + mediaId;
        }
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
//                        Context context = getContext();
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
                        if (jsonObject.getString("overview").equals("")) overviewText.setVisibility(View.GONE);
                        else overviewTextView.setText(jsonObject.getString("overview"));
                        if (jsonObject.getString("genres").equals("")) genresText.setVisibility(View.GONE);
                        else genresTextView.setText(jsonObject.getString("genres"));
                        if (jsonObject.getString("year").equals("")) yearText.setVisibility(View.GONE);
                        else yearTextView.setText(jsonObject.getString("year"));
                        if (QueryUtils.checkMedia(DetailsActivity.this, mediaType, mediaId)) {
                            watchImageView.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
                        } else {
                            watchImageView.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
                        }
                        watchImageView.setOnClickListener(new View.OnClickListener() {
                            String mediaUrl = jsonObject.getString("poster_path");
                            String mediaName = jsonObject.getString("title");
                            @Override
                            public void onClick(View v) {
                                if (QueryUtils.checkMedia(DetailsActivity.this, mediaType, mediaId)) {
                                    QueryUtils.removeMedia(DetailsActivity.this, mediaType, mediaId);
                                    watchImageView.setImageResource(R.drawable.ic_baseline_add_circle_outline_24);
                                    Toast.makeText(DetailsActivity.this, mediaName + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
                                } else {
                                    QueryUtils.addMedia(DetailsActivity.this, mediaType, mediaId, mediaUrl, mediaName);
                                    watchImageView.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
                                    Toast.makeText(DetailsActivity.this, mediaName + " was added to Watchlist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        fbTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                data = "https://www.themoviedb.org/" + mediaType + "/" + mediaId;
                                shareUrl = "https://www.facebook.com/sharer.php?u=" + data;
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl));
                                startActivity(browserIntent);
                            }
                        });
                        twitterTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                data = "Check this out!\nhttps://www.themoviedb.org/" + mediaType + "/" + mediaId;
                                shareUrl = "https://twitter.com/intent/tweet?text=" + data;
                                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(shareUrl));
                                startActivity(browserIntent);
                            }
                        });

                        /* to change
                         */
                        reviewItems = QueryUtils.parseReviewsFromResponse(jsonObject.getJSONArray("reviews"));
                        if (reviewItems.isEmpty()) reviewsText.setVisibility(View.GONE);
                        else {
                            mReviewAdapter1 = new ReviewAdapter(reviewItems, DetailsActivity.this);
                            mRecyclerView1.setHasFixedSize(true);
                            mRecyclerView1.setAdapter(mReviewAdapter1);
                            mReviewAdapter1.setOnItemClickListener(new ReviewAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Intent intent = new Intent(DetailsActivity.this, ReviewsActivity.class);
                                    intent.putExtra("label", reviewItems.get(position).getLabel());
                                    intent.putExtra("rating", reviewItems.get(position).getRating());
                                    intent.putExtra("overview", reviewItems.get(position).getContent());
                                    startActivity(intent);
                                }
                            });
                        }

                        castItems = QueryUtils.parseCastsFromResponse(jsonObject.getJSONArray("cast"));
                        if (castItems.isEmpty()) castText.setVisibility(View.GONE);
                        else {
                            castAdapter = new CastAdapter(castItems, DetailsActivity.this);
                            mRecyclerView2.setHasFixedSize(true);
                            mRecyclerView2.setAdapter(castAdapter);
                        }

                        /* to change
                         */

                        recommendedItems = QueryUtils.parseMediaFromResponse(jsonObject.getJSONArray("reco_picks")).get("recommended");
                        assert recommendedItems != null;
                        if (recommendedItems.isEmpty()) recommendedText.setVisibility(View.GONE);
                        else {
                            mCardAdapter = new CardAdapter(recommendedItems, DetailsActivity.this, "details");
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
                                    Intent intent = new Intent(DetailsActivity.this, DetailsActivity.class);
                                    intent.putExtra("mediaType", mediaType);
                                    intent.putExtra("mediaId", mediaId);
                                    startActivity(intent);
                                }
                                @Override
                                public void onOptionsClick(int position) {

                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);
                    loadingText.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }
}