package com.example.btcn;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btcn.models.Movie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {

    private RecyclerView rvMovies;
    private MovieAdapter adapter;
    private List<Movie> movieList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        movieList = new ArrayList<>();
        adapter = new MovieAdapter(movieList, this);
        rvMovies.setAdapter(adapter);

        fetchMovies();
    }

    private void fetchMovies() {
        db.collection("movies").addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(MainActivity.this, "Error fetching movies", Toast.LENGTH_SHORT).show();
                return;
            }

            if (value != null) {
                movieList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Movie movie = doc.toObject(Movie.class);
                    movie.setId(doc.getId());
                    movieList.add(movie);
                }
                adapter.notifyDataSetChanged();
                
                // If list is empty, add some dummy data for testing
                if (movieList.isEmpty()) {
                    addDummyData();
                }
            }
        });
    }

    private void addDummyData() {
        List<Movie> dummyMovies = new ArrayList<>();
        dummyMovies.add(new Movie(null, "Avengers: Endgame", "After the devastating events of Infinity War...", "https://image.tmdb.org/t/p/w500/or06vSney9MqNIvXEBqGvYjFccS.jpg", "Action", 181));
        dummyMovies.add(new Movie(null, "Interstellar", "A team of explorers travel through a wormhole...", "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg", "Sci-Fi", 169));
        dummyMovies.add(new Movie(null, "Inception", "A thief who steals corporate secrets...", "https://image.tmdb.org/t/p/w500/edv5CZvfkjSfm9kvQH67no2o6u7.jpg", "Sci-Fi", 148));

        for (Movie m : dummyMovies) {
            db.collection("movies").add(m);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movieId", movie.getId());
        startActivity(intent);
    }

    @Override
    public void onBookClick(Movie movie) {
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra("movieId", movie.getId());
        intent.putExtra("movieTitle", movie.getTitle());
        startActivity(intent);
    }
}
