package com.example.movieticket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.movieticket.R;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get data from Intent
        String movieId    = getIntent().getStringExtra("movieId");
        String title      = getIntent().getStringExtra("movieTitle");
        String genre      = getIntent().getStringExtra("movieGenre");
        String duration   = getIntent().getStringExtra("movieDuration");
        String desc       = getIntent().getStringExtra("movieDescription");
        String poster     = getIntent().getStringExtra("moviePoster");
        double rating     = getIntent().getDoubleExtra("movieRating", 0);

        getSupportActionBar().setTitle(title);

        TextView tvTitle    = findViewById(R.id.tvTitle);
        TextView tvGenre    = findViewById(R.id.tvGenre);
        TextView tvDuration = findViewById(R.id.tvDuration);
        TextView tvDesc     = findViewById(R.id.tvDescription);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        TextView tvRating   = findViewById(R.id.tvRating);
        ImageView ivPoster  = findViewById(R.id.ivPoster);
        Button btnBook      = findViewById(R.id.btnBook);

        tvTitle.setText(title);
        tvGenre.setText("🎭 " + genre);
        tvDuration.setText("⏱ " + duration);
        tvDesc.setText(desc);
        ratingBar.setRating((float) (rating / 2)); // Convert 10 scale to 5 scale
        tvRating.setText(String.format("%.1f/10 ⭐", rating));

        Glide.with(this)
            .load(poster)
            .placeholder(R.drawable.ic_movie_placeholder)
            .error(R.drawable.ic_movie_placeholder)
            .into(ivPoster);

        btnBook.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookingActivity.class);
            intent.putExtra("movieId",    movieId);
            intent.putExtra("movieTitle", title);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
