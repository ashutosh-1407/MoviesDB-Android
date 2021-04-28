package com.example.moviesdb;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        TextView labelTextView = findViewById(R.id.review_label);
        TextView ratingTextView = findViewById(R.id.review_rating);
        TextView overviewTextView = findViewById(R.id.review_overview);

        labelTextView.setText(getIntent().getStringExtra("label"));
        ratingTextView.setText(getIntent().getStringExtra("rating"));
        overviewTextView.setText(getIntent().getStringExtra("overview"));

    }
}