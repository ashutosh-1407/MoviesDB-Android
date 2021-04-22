package com.example.moviesdb;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        final YouTubePlayerView youTubePlayerView = rootView.findViewById(R.id.media_trailer);
        final TextView nameTextView = rootView.findViewById(R.id.media_name);
        final TextView overviewTextView = rootView.findViewById(R.id.media_overview);
        final TextView genresTextView = rootView.findViewById(R.id.media_genres);
        final TextView yearTextView = rootView.findViewById(R.id.media_year);
        mRecyclerView = rootView.findViewById(R.id.reco_picks_recycler_view);
        mRecyclerView1 = rootView.findViewById(R.id.reviews_recycler_view);
        mRecyclerView2 = rootView.findViewById(R.id.cast_recycler_view);

        String urlStart = "http://10.0.2.2:8080";
        String url = "";

        if (mParam1.equals("movie")) {
            url = urlStart + "/apis/watch/movie/" + mParam2;
        } else if (mParam1.equals("tv")) {
            url = urlStart + "/apis/watch/tv/" + mParam2;
        }
        final RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
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
                        /* to change
                         */

                        reviewItems = QueryUtils.parseReviewsFromResponse(jsonObject.getJSONArray("reviews"));
                        mReviewAdapter1 = new ReviewAdapter(reviewItems, context);
                        mRecyclerView1.setHasFixedSize(true);
                        mRecyclerView1.setAdapter(mReviewAdapter1);

                        castItems = QueryUtils.parseCastsFromResponse(jsonObject.getJSONArray("cast"));
                        castAdapter = new CastAdapter(castItems, context);
                        mRecyclerView2.setHasFixedSize(true);
                        mRecyclerView2.setAdapter(castAdapter);


                        /* to change
                         */

                        recommendedItems = QueryUtils.parseMediaFromResponse(jsonObject.getJSONArray("reco_picks")).get("recommended");
                        mCardAdapter = new CardAdapter(recommendedItems, context);
                        mRecyclerView.setHasFixedSize(true);
                        mRecyclerView.setAdapter(mCardAdapter);
                        mCardAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                DetailsFragment detailsFragment = DetailsFragment.newInstance(mParam1, recommendedItems.get(position).getId());
                                if (getFragmentManager() != null) {
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.content, detailsFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            }
                            @Override
                            public void onOptionsClick(int position) {
                                Toast.makeText(getContext(), "Options clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
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
}