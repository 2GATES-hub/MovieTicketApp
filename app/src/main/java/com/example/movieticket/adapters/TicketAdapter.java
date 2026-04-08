package com.example.movieticket.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieticket.R;
import com.example.movieticket.models.Ticket;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    private List<Ticket> tickets;

    public TicketAdapter(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket t = tickets.get(position);
        holder.tvMovieTitle.setText("🎬 " + t.getMovieTitle());
        holder.tvTheater.setText("🏟 " + t.getTheaterName());
        holder.tvDateTime.setText("📅 " + t.getDate() + "  ⏰ " + t.getTime());
        holder.tvSeats.setText("💺 " + t.getSeatCount() + " ghế");
        holder.tvPrice.setText(String.format("💰 %,.0f VNĐ", t.getTotalPrice()));
        holder.tvStatus.setText("confirmed".equals(t.getStatus()) ? "✅ Đã xác nhận" : "❌ Đã hủy");
        holder.tvBookingTime.setText("Đặt lúc: " + t.getBookingTime());
    }

    @Override
    public int getItemCount() { return tickets.size(); }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieTitle, tvTheater, tvDateTime, tvSeats, tvPrice, tvStatus, tvBookingTime;

        TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitle  = itemView.findViewById(R.id.tvMovieTitle);
            tvTheater     = itemView.findViewById(R.id.tvTheater);
            tvDateTime    = itemView.findViewById(R.id.tvDateTime);
            tvSeats       = itemView.findViewById(R.id.tvSeats);
            tvPrice       = itemView.findViewById(R.id.tvPrice);
            tvStatus      = itemView.findViewById(R.id.tvStatus);
            tvBookingTime = itemView.findViewById(R.id.tvBookingTime);
        }
    }
}
