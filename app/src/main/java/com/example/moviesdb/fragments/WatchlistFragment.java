package com.example.moviesdb.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviesdb.activity.DetailsActivity;
import com.example.moviesdb.R;
import com.example.moviesdb.helper.ItemTouchHelperCallback;
import com.example.moviesdb.model.MediaItem;
import com.example.moviesdb.model.WatchlistAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WatchlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchlistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView mRecyclerView;
    private WatchlistAdapter mCardAdapter;
    private ArrayList<MediaItem> watchlistItems;

    private TextView emptyTextView;
    ItemTouchHelper itemTouchHelper;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WatchlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WatchlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WatchlistFragment newInstance(String param1, String param2) {
        WatchlistFragment fragment = new WatchlistFragment();
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
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_watchlist, container, false);
        mRecyclerView = rootView.findViewById(R.id.watchlist_recycler_view);
        emptyTextView = rootView.findViewById(R.id.empty_text_view);
        final SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("shared_pref", Context.MODE_PRIVATE);
        final Gson gson = new Gson();
        final TypeToken<ArrayList<MediaItem>> token = new TypeToken<ArrayList<MediaItem>>() {
        };
        watchlistItems = gson.fromJson(sharedPreferences.getString("values", ""), token.getType());
        if (watchlistItems == null) watchlistItems = new ArrayList<>();
        if (!watchlistItems.isEmpty()) {
            mRecyclerView.setHasFixedSize(true);
            mCardAdapter = new WatchlistAdapter(watchlistItems, getContext(), viewHolder -> itemTouchHelper.startDrag(viewHolder));
            mRecyclerView.setAdapter(mCardAdapter);
            ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mCardAdapter);
            itemTouchHelper = new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(mRecyclerView);
            mCardAdapter.setOnItemClickListener(new WatchlistAdapter.OnItemClickListener() {
                String mediaType, mediaName;
                int mediaId;
                @Override
                public void onItemClick(int position) {
                    mediaId = watchlistItems.get(position).getId();
                    mediaType = watchlistItems.get(position).getType();
                    Intent intent = new Intent(getContext(), DetailsActivity.class);
                    intent.putExtra("mediaType", mediaType);
                    intent.putExtra("mediaId", mediaId);
                    startActivity(intent);
//                    final NavController navController = Navigation.findNavController(rootView);
//                    WatchlistFragmentDirections.ActionWatchlistFragmentToDetailsFragment action = WatchlistFragmentDirections.actionWatchlistFragmentToDetailsFragment(mediaType, mediaId);
//                    navController.navigate(action);
                }

                @Override
                public void onOptionsClick(int position) {
                    mediaId = watchlistItems.get(position).getId();
                    mediaType = watchlistItems.get(position).getType();
                    mediaName = watchlistItems.get(position).getName();
                    if (!watchlistItems.isEmpty()) {
                        int i=0;
                        for (; i<watchlistItems.size(); ++i) {
                            if (watchlistItems.get(i).getType().equals(mediaType) && watchlistItems.get(i).getId() == mediaId) {
                                watchlistItems.remove(i);
                                mCardAdapter.notifyItemRemoved(i);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                String jsonString = gson.toJson(watchlistItems);
                                editor.putString("values", jsonString);
                                editor.apply();
                            }
                        }
                    }
//                    QueryUtils.removeMedia(Objects.requireNonNull(getContext()), mediaType, mediaId, mCardAdapter);
                    Toast.makeText(getContext(), mediaName + " was removed from Watchlist" , Toast.LENGTH_SHORT).show();
                    if (watchlistItems.isEmpty()) {
                        mRecyclerView.setVisibility(View.GONE);
                        emptyTextView.setVisibility(View.VISIBLE);
                    }
                }
            });
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyTextView.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            emptyTextView.setVisibility(View.VISIBLE);
        }

        return rootView;
    }
}