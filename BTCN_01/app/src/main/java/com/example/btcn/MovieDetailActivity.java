package com.example.btcn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.btcn.models.Movie;
import com.google.firebase.firestore.FirebaseFirestore;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView ivMovieDetail;
    private TextView tvTitleDetail, tvGenreDetail, tvDurationDetail, tvDescriptionDetail;
    private Button btnBookNow;
    private FirebaseFirestore db;
    private String movieId;
    private Movie currentMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        db = FirebaseFirestore.getInstance();
        movieId = getIntent().getStringExtra("movieId");

        ivMovieDetail = findViewById(R.id.ivMovieDetail);
        tvTitleDetail = findViewById(R.id.tvTitleDetail);
        tvGenreDetail = findViewById(R.id.tvGenreDetail);
        tvDurationDetail = findViewById(R.id.tvDurationDetail);
        tvDescriptionDetail = findViewById(R.id.tvDescriptionDetail);
        btnBookNow = findViewById(R.id.btnBookNow);

        loadMovieDetails();

        btnBookNow.setOnClickListener(v -> {
            if (currentMovie != null) {
                Intent intent = new Intent(MovieDetailActivity.this, BookingActivity.class);
                intent.putExtra("movieId", currentMovie.getId());
                intent.putExtra("movieTitle", currentMovie.getTitle());
                startActivity(intent);
            }
        });
    }

    private void loadMovieDetails() {
        db.collection("movies").document(movieId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    currentMovie = documentSnapshot.toObject(Movie.class);
                    if (currentMovie != null) {
                        currentMovie.setId(documentSnapshot.getId());
                        tvTitleDetail.setText(currentMovie.getTitle());
                        tvGenreDetail.setText(currentMovie.getGenre());
                        tvDurationDetail.setText(currentMovie.getDuration() + " min");
                        tvDescriptionDetail.setText(currentMovie.getDescription());
                        Glide.with(this).load(currentMovie.getImageUrl()).into(ivMovieDetail);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading details", Toast.LENGTH_SHORT).show());
    }
}
