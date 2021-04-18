package com.example.moviesdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private static String mUrl = "https://csci571-hw8-guptaash.wl.r.appspot.com/apis/cpSearch";
    TextView slidesTextView;
    ProgressBar progressBar;
    TextView emptyView;
    LinearLayout mainLayout;
    LinearLayout homeLayout;
    LinearLayout searchLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeLayout = findViewById(R.id.home);
        searchLayout = findViewById(R.id.search);

        homeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieTVFragment movieFragment = new MovieTVFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, movieFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment movieFragment = new SearchFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, movieFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


//        ViewPager viewPager = findViewById(R.id.view_pager);
//
//        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(this, getSupportFragmentManager());
//
//        viewPager.setAdapter(myFragmentPagerAdapter);
//
//        TabLayout tabLayout = findViewById(R.id.tabs);
//
//        tabLayout.setupWithViewPager(viewPager);

//        slidesTextView = (TextView) findViewById(R.id.slides_text_view);
//        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
//        emptyView = (TextView) findViewById(R.id.empty_view);
//        mainLayout = (LinearLayout) findViewById(R.id.main_layout);
//
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//
//        if (networkInfo != null && networkInfo.isConnected()) {
//            getSupportLoaderManager().initLoader(0, null, this);
//        } else {
//            progressBar.setVisibility(View.GONE);
//            emptyView.setText(R.string.no_internet);
//        }
    }



//    @NonNull
//    @Override
//    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
//        return new DataLoader(this, mUrl);
//    }
//
//    @Override
//    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
//        progressBar.setVisibility(View.GONE);
//        slidesTextView.setVisibility(View.VISIBLE);
//        slidesTextView.setText(data);
//    }
//
//    @Override
//    public void onLoaderReset(@NonNull Loader<String> loader) {
//        slidesTextView.setText("");
//    }
}