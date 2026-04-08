package com.example.movieticket.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticket.R;
import com.example.movieticket.adapters.TicketAdapter;
import com.example.movieticket.models.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyTicketsActivity extends AppCompatActivity {

    private RecyclerView rvTickets;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private TicketAdapter adapter;
    private List<Ticket> ticketList = new ArrayList<>();
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Vé Của Tôi");

        db    = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        rvTickets   = findViewById(R.id.rvTickets);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty     = findViewById(R.id.tvEmpty);

        adapter = new TicketAdapter(ticketList);
        rvTickets.setLayoutManager(new LinearLayoutManager(this));
        rvTickets.setAdapter(adapter);

        loadTickets();
    }

    private void loadTickets() {
        String uid = mAuth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);

        db.collection("tickets")
            .whereEqualTo("userId", uid)
            .get()
            .addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    ticketList.clear();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Ticket t = doc.toObject(Ticket.class);
                        t.setId(doc.getId());
                        ticketList.add(t);
                    }
                    adapter.notifyDataSetChanged();
                    tvEmpty.setVisibility(ticketList.isEmpty() ? View.VISIBLE : View.GONE);
                } else {
                    Toast.makeText(this, "Lỗi tải vé", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}
