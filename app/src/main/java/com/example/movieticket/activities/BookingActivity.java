package com.example.movieticket.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.movieticket.R;
import com.example.movieticket.models.Showtime;
import com.example.movieticket.models.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    private String movieId, movieTitle;
    private Spinner spinnerShowtime;
    private NumberPicker numberPicker;
    private TextView tvMovieTitle, tvShowInfo, tvTotalPrice;
    private Button btnConfirm;
    private ProgressBar progressBar;

    private List<Showtime> showtimes = new ArrayList<>();
    private Showtime selectedShowtime = null;
    private int seatCount = 1;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Đặt Vé");

        movieId    = getIntent().getStringExtra("movieId");
        movieTitle = getIntent().getStringExtra("movieTitle");

        db    = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        tvMovieTitle  = findViewById(R.id.tvMovieTitle);
        spinnerShowtime = findViewById(R.id.spinnerShowtime);
        numberPicker  = findViewById(R.id.numberPicker);
        tvShowInfo    = findViewById(R.id.tvShowInfo);
        tvTotalPrice  = findViewById(R.id.tvTotalPrice);
        btnConfirm    = findViewById(R.id.btnConfirm);
        progressBar   = findViewById(R.id.progressBar);

        tvMovieTitle.setText(movieTitle);

        // NumberPicker for seat count
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setValue(1);
        numberPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            seatCount = newVal;
            updateTotalPrice();
        });

        loadShowtimes();
        btnConfirm.setOnClickListener(v -> confirmBooking());
    }

    private void loadShowtimes() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("showtimes")
            .whereEqualTo("movieId", movieId)
            .get()
            .addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    showtimes.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Showtime s = doc.toObject(Showtime.class);
                        s.setId(doc.getId());
                        showtimes.add(s);
                    }
                    if (showtimes.isEmpty()) {
                        seedSampleShowtimes();
                    } else {
                        setupSpinner();
                    }
                }
            });
    }

    private void seedSampleShowtimes() {
        List<Showtime> samples = new ArrayList<>();
        samples.add(new Showtime(null, movieId, "t1", "CGV Vincom", "2025-06-15", "10:00", 50, 85000));
        samples.add(new Showtime(null, movieId, "t1", "CGV Vincom", "2025-06-15", "14:30", 50, 85000));
        samples.add(new Showtime(null, movieId, "t2", "Lotte Cinema", "2025-06-15", "19:00", 60, 90000));
        samples.add(new Showtime(null, movieId, "t2", "Lotte Cinema", "2025-06-16", "15:00", 60, 90000));
        samples.add(new Showtime(null, movieId, "t3", "BHD Star", "2025-06-16", "20:30", 45, 95000));

        for (Showtime s : samples) {
            db.collection("showtimes").add(s)
                .addOnSuccessListener(ref -> {
                    s.setId(ref.getId());
                    showtimes.add(s);
                    setupSpinner();
                });
        }
    }

    private void setupSpinner() {
        List<String> labels = new ArrayList<>();
        for (Showtime s : showtimes) {
            labels.add(String.format("%s | %s | %s | %,.0fđ",
                s.getDate(), s.getTime(), s.getTheaterName(), s.getPrice()));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            android.R.layout.simple_spinner_item, labels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerShowtime.setAdapter(adapter);

        spinnerShowtime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedShowtime = showtimes.get(pos);
                tvShowInfo.setText(String.format("🏟 %s\n📅 %s  ⏰ %s\n💺 Còn %d ghế",
                    selectedShowtime.getTheaterName(),
                    selectedShowtime.getDate(),
                    selectedShowtime.getTime(),
                    selectedShowtime.getAvailableSeats()));
                updateTotalPrice();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateTotalPrice() {
        if (selectedShowtime != null) {
            double total = selectedShowtime.getPrice() * seatCount;
            tvTotalPrice.setText(String.format("Tổng tiền: %,.0f VNĐ", total));
        }
    }

    private void confirmBooking() {
        if (selectedShowtime == null) {
            Toast.makeText(this, "Chọn suất chiếu!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (seatCount > selectedShowtime.getAvailableSeats()) {
            Toast.makeText(this, "Không đủ ghế trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        double total = selectedShowtime.getPrice() * seatCount;
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận đặt vé")
            .setMessage(String.format(
                "🎬 %s\n🏟 %s\n📅 %s  ⏰ %s\n💺 %d ghế\n💰 %,.0f VNĐ",
                movieTitle,
                selectedShowtime.getTheaterName(),
                selectedShowtime.getDate(),
                selectedShowtime.getTime(),
                seatCount, total))
            .setPositiveButton("Xác nhận", (dialog, which) -> saveTicket())
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void saveTicket() {
        progressBar.setVisibility(View.VISIBLE);
        btnConfirm.setEnabled(false);

        String uid = mAuth.getCurrentUser().getUid();
        String bookingTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            .format(new Date());
        double total = selectedShowtime.getPrice() * seatCount;

        // Get FCM token then save ticket
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(tokenTask -> {
            String fcmToken = tokenTask.isSuccessful() ? tokenTask.getResult() : "";

            Ticket ticket = new Ticket(uid, movieId, movieTitle,
                selectedShowtime.getId(),
                selectedShowtime.getTheaterName(),
                selectedShowtime.getDate(),
                selectedShowtime.getTime(),
                seatCount, total, bookingTime, fcmToken);

            db.collection("tickets").add(ticket)
                .addOnSuccessListener(ref -> {
                    // Update available seats
                    int remaining = selectedShowtime.getAvailableSeats() - seatCount;
                    db.collection("showtimes")
                        .document(selectedShowtime.getId())
                        .update("availableSeats", remaining);

                    progressBar.setVisibility(View.GONE);
                    btnConfirm.setEnabled(true);

                    Toast.makeText(this, "✅ Đặt vé thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    btnConfirm.setEnabled(true);
                    Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}
