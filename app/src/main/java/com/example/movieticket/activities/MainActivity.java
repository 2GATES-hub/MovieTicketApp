package com.example.movieticket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticket.R;
import com.example.movieticket.adapters.MovieAdapter;
import com.example.movieticket.models.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMovies;
    private ProgressBar progressBar;
    private MovieAdapter adapter;
    private List<Movie> movieList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("🎬 Movie Tickets");

        db    = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        rvMovies    = findViewById(R.id.rvMovies);
        progressBar = findViewById(R.id.progressBar);

        adapter = new MovieAdapter(movieList, movie -> {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("movieId",    movie.getId());
            intent.putExtra("movieTitle", movie.getTitle());
            intent.putExtra("movieGenre", movie.getGenre());
            intent.putExtra("movieDuration", movie.getDuration());
            intent.putExtra("movieDescription", movie.getDescription());
            intent.putExtra("moviePoster", movie.getPosterUrl());
            intent.putExtra("movieRating", movie.getRating());
            startActivity(intent);
        });

        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        rvMovies.setAdapter(adapter);

        // Subscribe to FCM topic for broadcast notifications
        FirebaseMessaging.getInstance().subscribeToTopic("movies")
            .addOnCompleteListener(task -> {});

        // Save FCM token to Firestore
        saveFcmToken();

        loadMovies();
    }

    private void loadMovies() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("movies")
            .get()
            .addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    movieList.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Movie movie = doc.toObject(Movie.class);
                        movie.setId(doc.getId());
                        movieList.add(movie);
                    }
                    if (movieList.isEmpty()) {
                        // Seed sample data if Firestore is empty
                        seedSampleMovies();
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(this, "Lỗi tải danh sách phim", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void seedSampleMovies() {
        List<Movie> samples = new ArrayList<>();
        samples.add(new Movie(null, "Avengers: Endgame", "Hành động", "181 phút",
            "Các siêu anh hùng tập hợp để đánh bại Thanos và cứu vũ trụ.",
            "https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg", 8.4));
        samples.add(new Movie(null, "The Lion King", "Hoạt hình", "118 phút",
            "Simba trưởng thành và trở lại để lấy lại ngôi vương của mình.",
            "https://image.tmdb.org/t/p/w500/2bXbqYdUdNVa8VIWXVfclP2ICtT.jpg", 7.1));
        samples.add(new Movie(null, "Inception", "Khoa học viễn tưởng", "148 phút",
            "Một tên trộm xâm nhập vào giấc mơ của người khác để đánh cắp bí mật.",
            "https://image.tmdb.org/t/p/w500/9gk7adHYeDvHkCSEqAvQNLV5Uge.jpg", 8.8));
        samples.add(new Movie(null, "Interstellar", "Khoa học viễn tưởng", "169 phút",
            "Một nhóm phi hành gia tìm kiếm hành tinh mới để cứu nhân loại.",
            "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg", 8.6));
        samples.add(new Movie(null, "Joker", "Tâm lý - Tội phạm", "122 phút",
            "Nguồn gốc của nhân vật phản diện huyền thoại Joker.",
            "https://image.tmdb.org/t/p/w500/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg", 8.4));
        samples.add(new Movie(null, "Spider-Man: No Way Home", "Hành động", "148 phút",
            "Peter Parker mở ra đa vũ trụ với sự giúp đỡ của Doctor Strange.",
            "https://image.tmdb.org/t/p/w500/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg", 8.2));

        for (Movie m : samples) {
            db.collection("movies").add(m)
                .addOnSuccessListener(ref -> {
                    m.setId(ref.getId());
                    movieList.add(m);
                    adapter.notifyDataSetChanged();
                });
        }
    }

    private void saveFcmToken() {
        String uid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        if (uid == null) return;
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();
                db.collection("users").document(uid)
                    .update("fcmToken", token);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_my_tickets) {
            startActivity(new Intent(this, MyTicketsActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.action_logout) {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
