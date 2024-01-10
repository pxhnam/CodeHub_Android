package com.example.codehub.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.codehub.R;
import com.example.codehub.models.Deposit;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DepositAdapter extends RecyclerView.Adapter<DepositAdapter.Viewholder> {
    private List<Deposit> deposits;

    public DepositAdapter(List<Deposit> deposits) {
        this.deposits = deposits;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_deposit, parent, false);
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Deposit deposit = deposits.get(position);
        holder.desc.setText(deposit.getDesc());
        holder.date.setText(formattedDateTime(deposit.getDatetime()));
        if (deposit.isType()) {
            holder.coin.setText("+ " + deposit.getCoin());
            holder.coin.setTextColor(Color.GREEN);
        } else {
            holder.coin.setText("- " + deposit.getCoin());
            holder.coin.setTextColor(Color.RED);
        }
    }

    private String formattedDateTime(Timestamp timestamp) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        return sdf.format(timestamp);
    }

    @Override
    public int getItemCount() {
        return deposits.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        TextView desc, date, coin;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.tv_desc);
            date = itemView.findViewById(R.id.tv_date);
            coin = itemView.findViewById(R.id.tv_coin);
        }
    }
}
