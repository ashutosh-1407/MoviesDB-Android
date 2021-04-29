package com.example.moviesdb.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView mRecyclerView;
    private CardAdapter mSearchAdapter;
    private ArrayList<MediaItem> mSearchItems = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TVFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        final EditText searchEditText = rootView.findViewById(R.id.search_edit_text);
        ImageView closeImageView = rootView.findViewById(R.id.search_close_button);
//        androidx.appcompat.widget.SearchView searchView = rootView.findViewById(R.id.search_view);
//        ImageView closeBtn = (ImageView) searchView.findViewById(R.id.search_close_btn);
//        closeBtn.setEnabled(false);
//        closeBtn.setImageDrawable(null);
        TextView noResultText = rootView.findViewById(R.id.search_empty_view);
//        searchView.setIconified(false);
//        searchView.setActivated(true);
        noResultText.setVisibility(View.INVISIBLE);
        mRecyclerView = rootView.findViewById(R.id.search_recycler_view);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                if (s.length() > 0) {
                    closeImageView.setVisibility(View.VISIBLE);
                    closeImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchEditText.setText("");
                        }
                    });
                } else {
                    closeImageView.setVisibility(View.INVISIBLE);
                }
                if (s.length() > 2) {
                    StringRequest stringRequest = new StringRequest(QueryUtils.startUrl + "/apis/search/" + s, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("test", response);
                            if (!response.equals("[]")) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    mSearchItems = QueryUtils.parseSearchFromResponse(jsonArray);
                                    mSearchAdapter = new CardAdapter(mSearchItems, getContext(), "search");
                                    mRecyclerView.setAdapter(mSearchAdapter);
                                    mSearchAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(int position) {
                                            String mediaType = mSearchItems.get(position).getType();
                                            int mediaId = mSearchItems.get(position).getId();
                                            Intent intent = new Intent(getContext(), DetailsActivity.class);
                                            intent.putExtra("mediaType", mediaType);
                                            intent.putExtra("mediaId", mediaId);
                                            startActivity(intent);
//                                            final NavController navController = Navigation.findNavController(rootView);
//                                            SearchFragmentDirections.ActionSearchFragmentToDetailsFragment action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(mediaType, mediaId);
//                                            navController.navigate(action);

                                        }

                                        @Override
                                        public void onOptionsClick(int position) {

                                        }
                                    });
                                    mRecyclerView.setVisibility(View.VISIBLE);
                                    noResultText.setVisibility(View.GONE);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                mRecyclerView.setVisibility(View.INVISIBLE);
                                noResultText.setText("No result found.");
                                noResultText.setVisibility(View.VISIBLE);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    requestQueue.add(stringRequest);
                } else {
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    noResultText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }

}