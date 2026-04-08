package com.example.btcn;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btcn.models.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class BookingActivity extends AppCompatActivity {

    private TextView tvBookingMovieTitle;
    private Spinner spTheaters, spShowtimes, spSeats;
    private Button btnConfirmBooking;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String movieId, movieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        movieId = getIntent().getStringExtra("movieId");
        movieTitle = getIntent().getStringExtra("movieTitle");

        tvBookingMovieTitle = findViewById(R.id.tvBookingMovieTitle);
        spTheaters = findViewById(R.id.spTheaters);
        spShowtimes = findViewById(R.id.spShowtimes);
        spSeats = findViewById(R.id.spSeats);
        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        tvBookingMovieTitle.setText(movieTitle);

        setupSpinners();

        btnConfirmBooking.setOnClickListener(v -> confirmBooking());
    }

    private void setupSpinners() {
        // Dữ liệu mẫu cho Spinners (Trong thực tế nên lấy từ Firestore)
        String[] theaters = {"CGV Vincom", "Lotte Cinema", "BHD Star"};
        String[] showtimes = {"10:00 AM", "01:30 PM", "04:45 PM", "08:00 PM"};
        String[] seats = {"A1", "A2", "B5", "C10", "E12"};

        spTheaters.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, theaters));
        spShowtimes.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, showtimes));
        spSeats.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, seats));
    }

    private void confirmBooking() {
        if (mAuth.getCurrentUser() == null) return;

        String userId = mAuth.getCurrentUser().getUid();
        String theater = spTheaters.getSelectedItem().toString();
        String showtime = spShowtimes.getSelectedItem().toString();
        String seat = spSeats.getSelectedItem().toString();

        Ticket ticket = new Ticket(userId, movieId, movieTitle, theater, showtime, seat, 95000.0);

        db.collection("tickets").add(ticket)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Booking Successful! Ticket ID: " + documentReference.getId(), Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Booking Failed", Toast.LENGTH_SHORT).show());
    }
}
