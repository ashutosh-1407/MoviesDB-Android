package com.example.moviesdb.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.example.moviesdb.R;

import android.os.Bundle;
import android.widget.TextView;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        TextView labelTextView = findViewById(R.id.review_label);
        TextView ratingTextView = findViewById(R.id.review_rating);
        TextView overviewTextView = findViewById(R.id.review_overview);

        labelTextView.setText(getIntent().getStringExtra("label"));
        ratingTextView.setText(getIntent().getStringExtra("rating"));
        overviewTextView.setText(getIntent().getStringExtra("overview"));
    }
}