package com.example.codehub.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.codehub.R;
import com.example.codehub.models.Order;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Viewholder> {
    private List<Order> orders;
    private Context context;


    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.viewholder_order, parent, false);
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Order order = orders.get(position);
        holder.name.setText(order.getName());
        holder.fee.setText(String.valueOf(order.getFee()));
        holder.date.setText(formattedDateTime(order.getDatetime()));
        Glide.with(context)
                .load(order.getImage())
                .transform(new CenterCrop(), new RoundedCorners(30))
                .into(holder.pic);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private String formattedDateTime(Timestamp timestamp) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        return sdf.format(timestamp);
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView name, fee, date;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
            fee = itemView.findViewById(R.id.fee);
            date = itemView.findViewById(R.id.date);
        }
    }
}
