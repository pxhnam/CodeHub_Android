package com.example.codehub.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.codehub.R;
import com.example.codehub.adapters.DepositAdapter;
import com.example.codehub.database.SqlConnection;
import com.example.codehub.models.Deposit;


import java.util.List;

public class DepositActivity extends AppCompatActivity {
    RecyclerView rvDeposits;
    TextView btnBack, noData;
    private SqlConnection connection;
    private List<Deposit> deposits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        init();

        deposits = connection.listDeposit();
        if (deposits.size() == 0) noData.setVisibility(View.VISIBLE);
        else noData.setVisibility(View.GONE);
        DepositAdapter adtDeposit = new DepositAdapter(deposits);
        rvDeposits.setLayoutManager(new LinearLayoutManager(this));
        rvDeposits.setAdapter(adtDeposit);


        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void init() {
        connection = new SqlConnection(this);
        rvDeposits = findViewById(R.id.listDeposit);
        btnBack = findViewById(R.id.btnBack);
        noData = findViewById(R.id.noData);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}