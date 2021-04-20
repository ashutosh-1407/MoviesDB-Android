package com.example.moviesdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;


public class MainActivity extends AppCompatActivity {

    LinearLayout homeLayout;
    LinearLayout searchLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeLayout = findViewById(R.id.home);
        searchLayout = findViewById(R.id.search);
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
                SearchFragment searchFragment = new SearchFragment();
                activateFragment("search", searchFragment);
            }
        });

    }

    private void activateFragment(String str, SearchFragment ...fragments) {
        MovieTVFragment mediaFragment = MovieTVFragment.newInstance(str);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragments.length > 0 ? fragments[0] : mediaFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}