package com.example.codehub.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codehub.R;
import com.example.codehub.adapters.MethodAdapter;
import com.example.codehub.models.Method;

import java.util.ArrayList;
import java.util.List;

public class TopUpActivity extends AppCompatActivity {
    TextView btnBack;
    RecyclerView rvMethods;
    List<Method> methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);
        init();
        ListMethod();
        MethodAdapter methodAdapter = new MethodAdapter(methods);
        rvMethods.setAdapter(methodAdapter);
        rvMethods.setLayoutManager(new LinearLayoutManager(this));


        methodAdapter.setOnItemClickListener(id -> {
            if (id == 1) {
                startActivity(new Intent(this, MomoActivity.class));
            } else if (id == 2) {
                Toast.makeText(this, "Zalo Pay", Toast.LENGTH_SHORT).show();

            } else if (id == 3) {
                Toast.makeText(this, "Viettel Pay", Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void ListMethod() {
        methods = new ArrayList<>();
        methods.add(new Method(1, "MomoPay", "momo_pay"));
        methods.add(new Method(2, "ZaloPay", "zalo_pay"));
        methods.add(new Method(3, "VietelPay", "vt_pay"));
    }

    private void init() {
        btnBack = findViewById(R.id.btnBack);
        rvMethods = findViewById(R.id.listMethod);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}