package com.example.codehub.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codehub.R;
import com.example.codehub.adapters.CartAdapter;
import com.example.codehub.database.SqlConnection;
import com.example.codehub.models.Cart;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    TextView btnBack, noData;

    RecyclerView rvCarts;
    private SqlConnection connection;
    private List<Cart> carts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
        connection = new SqlConnection(this);
        carts = connection.listCart();
        handleNoData(carts);
        rvCarts.setLayoutManager(new LinearLayoutManager(this));
        CartAdapter cartAdapter = new CartAdapter(carts);
        rvCarts.setAdapter(cartAdapter);


        cartAdapter.setOnSeeDetailClickListener(id -> {
            Intent intent = new Intent(CartActivity.this, DetailActivity.class);
            intent.putExtra("IdSource", id);
            connection.updateViewCounter(id);
            startActivity(intent);
        });

        cartAdapter.setOnRemoveItemClickListener((pos, id) -> {
            if (connection.removeCart(id)) {
                Toast.makeText(this, "Đã xóa thành công khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                cartAdapter.removeCartAtPosition(pos);
                handleNoData(carts);
            } else
                Toast.makeText(this, "Đã xảy ra lỗi! Hãy thử lại.", Toast.LENGTH_SHORT).show();
        });


        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void handleNoData(List<Cart> carts) {
        if (carts.size() == 0) noData.setVisibility(View.VISIBLE);
        else noData.setVisibility(View.GONE);
    }

    private void init() {
        rvCarts = findViewById(R.id.listCart);
        btnBack = findViewById(R.id.btnBack);
        noData = findViewById(R.id.noData);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}