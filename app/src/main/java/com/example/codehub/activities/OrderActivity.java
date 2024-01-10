package com.example.codehub.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.codehub.R;
import com.example.codehub.adapters.DepositAdapter;
import com.example.codehub.adapters.OrderAdapter;
import com.example.codehub.database.SqlConnection;
import com.example.codehub.models.Order;

import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private TextView btnBack, noData;
    private List<Order> orders;
    private SqlConnection connection;
    private RecyclerView rvOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        init();
        orders = connection.listOrder();
        setupRecycleView(orders);

        btnBack.setOnClickListener(v -> finish());
    }

    private void setupRecycleView(List<Order> orders) {
        if (orders.size() == 0) noData.setVisibility(View.VISIBLE);
        else noData.setVisibility(View.GONE);
        OrderAdapter adtOrder = new OrderAdapter(orders);
        rvOrders.setLayoutManager(new LinearLayoutManager(this));
        rvOrders.setAdapter(adtOrder);
    }

    private void init() {
        connection = new SqlConnection(this);
        noData = findViewById(R.id.noData);
        btnBack = findViewById(R.id.btnBack);
        rvOrders = findViewById(R.id.listOrder);
    }
}