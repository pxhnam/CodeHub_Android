package com.example.codehub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codehub.R;
import com.example.codehub.database.SqlConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import vn.momo.momo_partner.AppMoMoLib;

public class MomoActivity extends AppCompatActivity {
    TextView btnBack, vnd;
    Button btnConfirm;
    EditText inputCoin;
    SqlConnection connection;
    private final String merchantCode = "MOMO";
    int environment = 0;//developer default

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_momo);
        init();
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT);
        btnConfirm.setOnClickListener(v -> {
            String coin = inputCoin.getText().toString().trim();
            if (!coin.isEmpty()) {
                int amount = Integer.parseInt(coin) * 1000;
                requestPayment(amount);
            }
        });

        inputCoin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần thực hiện gì trước khi text thay đổi
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Không cần thực hiện gì khi text thay đổi
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();
                if (!input.isEmpty()) {
                    vnd.setVisibility(View.VISIBLE);
                    try {
                        vnd.setText("= " + convertMoney(input));
                    } catch (NumberFormatException ex) {
                        Log.e("NumberFormatException", "afterTextChanged: ", ex);
                    }
                } else {
                    vnd.setVisibility(View.GONE);
                }
            }
        });

        btnBack.setOnClickListener(view -> onBackPressed());
    }

    //Get token through MoMo app
    private void requestPayment(int amount) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", "CodeHub"); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount); //Kiểu integer

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", 0); //Kiểu integer
        eventValue.put("description", "Nạp xu"); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId", System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if (data != null) {
                if (data.getIntExtra("status", -1) == 0) {
                    // Nạp tiền thành công
                    int coin = Integer.parseInt(inputCoin.getText().toString().trim());
                    connection.rechargeCoins(coin);
                    Toast.makeText(this, "Nạp xu thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (data.getIntExtra("status", -1) == 1) {
                    // Giao dịch thất bại
                    String message = data.getStringExtra("message") != null ? data.getStringExtra("message") : "Thất bại";
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                } else if (data.getIntExtra("status", -1) == 2) {
                    // Giao dịch thất bại
                    Toast.makeText(this, "Giao dịch thất bại", Toast.LENGTH_SHORT).show();
                } else {
                    // Giao dịch thất bại
                    Toast.makeText(this, "Giao dịch thất bại", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Không nhận được dữ liệu từ MoMo
                Toast.makeText(this, "Không nhận được dữ liệu từ MoMo", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Giao dịch không thành công
            Toast.makeText(this, "Giao dịch không thành công", Toast.LENGTH_SHORT).show();
        }
    }


    private String convertMoney(String input) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormat.format(Integer.parseInt(input) * 1000L);
    }

    private void init() {
        btnBack = findViewById(R.id.btnBack);
        vnd = findViewById(R.id.makeVnd);
        btnConfirm = findViewById(R.id.btnConfirm);
        inputCoin = findViewById(R.id.inputCoin);
        connection = new SqlConnection(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}