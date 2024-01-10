package com.example.codehub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codehub.R;
import com.example.codehub.database.SqlConnection;

public class LoginActivity extends AppCompatActivity {
    private SqlConnection connection;
    Button btnLogin;
    TextView btnRegister;
    EditText Username, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        connection = new SqlConnection(this);
        SharedPreferences preferences = this.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        int id = preferences.getInt("id", 0);
        if (id > 0 && connection.checkAccount()) {
            loginSuccess();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        btnLogin.setOnClickListener(v -> {
            if (connection.login(Username.getText().toString(), Password.getText().toString())) {
                loginSuccess();
            } else Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
        });
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

    }

    private void loginSuccess() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void init() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.tv_Register);
        Username = findViewById(R.id.et_Username);
        Password = findViewById(R.id.et_Password);
    }
}