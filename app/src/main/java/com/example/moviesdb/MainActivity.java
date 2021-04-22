package com.example.moviesdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


public class MainActivity extends AppCompatActivity {

    LinearLayout homeLayout;
    LinearLayout searchLayout;
    LinearLayout watchlistLayout;
    MovieTVFragment mediaFragment;
    SearchFragment searchFragment;
    WatchlistFragment watchlistFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeLayout = findViewById(R.id.home);
        searchLayout = findViewById(R.id.search);
        watchlistLayout = findViewById(R.id.watchlist);
        activateFragment("movie");

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateFragment("movie");
            }
        });

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateFragment("search");
            }
        });

        watchlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateFragment("watchlist");
            }
        });
    }

    private void activateFragment(String str) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (str.equals("movie") || str.equals("tv")) {
            mediaFragment = MovieTVFragment.newInstance(str);
            transaction.replace(R.id.content, mediaFragment);
        }
        else if (str.equals("search")) {
            searchFragment = new SearchFragment();
            transaction.replace(R.id.content, searchFragment);
        }
        else {
            watchlistFragment = new WatchlistFragment();
            transaction.replace(R.id.content, watchlistFragment);
        }
        transaction.addToBackStack(null);
        transaction.commit();
    }
}