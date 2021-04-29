package com.example.moviesdb.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Movie;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.moviesdb.R;
import com.example.moviesdb.fragments.MovieTVFragment;
import com.example.moviesdb.fragments.SearchFragment;
import com.example.moviesdb.fragments.WatchlistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    LinearLayout homeLayout;
    LinearLayout searchLayout;
    LinearLayout watchlistLayout;
    MovieTVFragment mediaFragment;
    SearchFragment searchFragment;
    WatchlistFragment watchlistFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
//        navigationView.setItemIconTintList(null);
//        navigationView.setItemTextColor(null);
        NavigationUI.setupWithNavController(navigationView, navController);
//        homeLayout = findViewById(R.id.home);
//        searchLayout = findViewById(R.id.search);
//        watchlistLayout = findViewById(R.id.watchlist);
//        activateFragment("movie");
//
//        homeLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activateFragment("movie");
//            }
//        });
//
//        searchLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activateFragment("search");
//            }
//        });
//
//        watchlistLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activateFragment("watchlist");
//            }
//        });

    }

//    protected void activateFragment(String str) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        if (str.equals("movie") || str.equals("tv")) {
//            mediaFragment = MovieTVFragment.newInstance(str);
//            transaction.replace(R.id.content, mediaFragment);
//        }
//        else if (str.equals("search")) {
//            searchFragment = new SearchFragment();
//            transaction.replace(R.id.content, searchFragment);
//        }
//        else {
//            watchlistFragment = new WatchlistFragment();
//            transaction.replace(R.id.content, watchlistFragment);
//        }
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }
}