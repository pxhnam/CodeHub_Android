package com.example.codehub.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.codehub.R;
import com.example.codehub.activities.DepositActivity;
import com.example.codehub.activities.LoginActivity;
import com.example.codehub.activities.OrderActivity;
import com.example.codehub.activities.RequestActivity;
import com.example.codehub.activities.TopUpActivity;
import com.example.codehub.database.SqlConnection;
import com.example.codehub.models.User;

import java.text.NumberFormat;
import java.util.Locale;

public class SettingsFragment extends Fragment {
    Button btnLogout, btnCoin, btnOrder, btnRequest;
    TextView fullname, username, coin, btnDeposit;
    private SqlConnection connection;
    private boolean isCoin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        init(root);
        User user = connection.getUser();
        if (user != null) {
            fullname.setText(user.getFullName());
            username.setText(user.getUsername());
            coin.setText(convertCoin(user.getCurrency(), true));
            coin.setOnClickListener(v -> {
                coin.setText(convertCoin(user.getCurrency(), isCoin));
                isCoin = !isCoin;
            });
        }

        btnCoin.setOnClickListener(v -> addCoin());
        btnLogout.setOnClickListener(v -> logout());
        btnDeposit.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), DepositActivity.class));
        });
        btnOrder.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), OrderActivity.class));
        });
        btnRequest.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), RequestActivity.class));
        });
        return root;
    }

    private void logout() {
        SharedPreferences preferences = requireContext().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        startActivity(new Intent(getContext(), LoginActivity.class));
        requireActivity().finish();
    }

    private void addCoin() {
        startActivity(new Intent(getContext(), TopUpActivity.class));
    }

    private String convertCoin(int coin, boolean is) {
        if (is) {
            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            return decimalFormat.format(coin) + " xu";
        } else {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            return currencyFormat.format(coin * 1000L);
        }
    }

    private void init(View root) {
        connection = new SqlConnection(getContext());
        btnLogout = root.findViewById(R.id.btnLogout);
        btnCoin = root.findViewById(R.id.btnAddCoin);
        btnOrder = root.findViewById(R.id.btnOrder);
        btnRequest = root.findViewById(R.id.btnRequest);
        fullname = root.findViewById(R.id.tv_FullName);
        username = root.findViewById(R.id.tv_Username);
        coin = root.findViewById(R.id.tv_Coin);
        btnDeposit = root.findViewById(R.id.btnDeposit);
    }
}