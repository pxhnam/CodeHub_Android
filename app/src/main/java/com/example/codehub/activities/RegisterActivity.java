package com.example.codehub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codehub.R;
import com.example.codehub.database.SqlConnection;
import com.example.codehub.models.User;
import com.example.codehub.utils.Email;
import com.example.codehub.utils.generate;
import com.example.codehub.validation.EmailValidator;
import com.example.codehub.validation.UsernameValidator;
import com.example.codehub.validation.DateValidator;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    private SqlConnection connection;
    private String selectedGender = "";
    Button btnRegister;
    TextView btnBack;
    EditText etDOB, etFirstName, etLastName, etEmail, etUsername, etPassword;
    AutoCompleteTextView altGender;
    ArrayAdapter<String> adapterGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        connection = new SqlConnection(this);
        init();
        btnBack.setOnClickListener(v -> onBackPressed());

//        String[] items = {"Java", "C#", "HTML", "CSS"};
//        adapterItems = new ArrayAdapter<String>(requireActivity(), R.layout.list_item, items);
//        autoComplete.setAdapter(adapterItems);

        String[] items = {"Nam", "Nữ"};
        adapterGender = new ArrayAdapter<>(this, R.layout.list_item, items);
        altGender.setAdapter(adapterGender);

        altGender.setOnItemClickListener((parent, view, position, id) -> selectedGender = (String) parent.getItemAtPosition(position));


        etDOB.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                etDOB.setText(selectedDate);
            }, year, month, day);
            datePickerDialog.show();
        });

        btnRegister.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String dob = etDOB.getText().toString().trim();
            if (validateForm(firstName, lastName, email, username, password, selectedGender.trim(), dob)) {
                String otp = generate.OTP();
                new Thread(() -> Email.sendOTP(email,
                        "Xác minh tài khoản",
                        "Mã xác nhận của bạn là: " + otp)).start();
//                Toast.makeText(this, "Kiểm tra email của bạn để lấy mã xác nhận", Toast.LENGTH_SHORT).show();
                showEmailConfirmationDialog(code -> {
                    if (otp.equals(code)) {
                        User user = new User(username, password, firstName, lastName, email, selectedGender.equals("Nam"), dob);
                        if (connection.register(user)) {
                            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else
                            Toast.makeText(this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, "Mã xác nhận sai!", Toast.LENGTH_SHORT).show();
                });
            } else
                Toast.makeText(this, "Xem lại thông tin cá nhân!", Toast.LENGTH_SHORT).show();
        });
    }

    public boolean validateForm(String f, String l, String e, String u, String p, String g, String d) {
        //l: LastName, f: FirstName, e: Email, u: Username, p: Password, g: Gender, d: Date
        if (f == null || f.trim().isEmpty() ||
                l == null || l.trim().isEmpty() ||
                e == null || e.trim().isEmpty() ||
                u == null || u.trim().isEmpty() ||
                p == null || p.trim().isEmpty() ||
                g == null || g.trim().isEmpty() ||
                d == null || d.trim().isEmpty()) {
            return false;
        }
        boolean isEmailValid = EmailValidator.isValidEmail(e);
        boolean isUsernameValid = UsernameValidator.isValidUsername(u);

        // Tách chuỗi
        String[] dateParts = d.split("/");
        if (dateParts.length != 3) {
            return false;
        }

        int day, month, year;
        try {
            day = Integer.parseInt(dateParts[0]);
            month = Integer.parseInt(dateParts[1]);
            year = Integer.parseInt(dateParts[2]);
        } catch (NumberFormatException ex) {
            return false;
        }
        boolean isDateValid = DateValidator.isValidDate(day, month, year);

        return isEmailValid && isUsernameValid && isDateValid;
    }

    private void showEmailConfirmationDialog(final EmailConfirmationListener listener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_input, null);

        dialogBuilder.setView(dialogView);
        TextView hint = dialogView.findViewById(R.id.textHint);
        EditText code = dialogView.findViewById(R.id.inputEditText);
        Button btnClose = dialogView.findViewById(R.id.btnClose);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        hint.setText("Nhập mã: ");
        AlertDialog alertDialog = dialogBuilder.create();
        btnConfirm.setOnClickListener(v -> listener.onEmailConfirmed(code.getText().toString().trim()));
        btnClose.setOnClickListener(v -> alertDialog.dismiss());
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void init() {
        altGender = findViewById(R.id.alt_Gender);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);
        etDOB = findViewById(R.id.et_DOB);
        etFirstName = findViewById(R.id.et_FirstName);
        etLastName = findViewById(R.id.et_LastName);
        etEmail = findViewById(R.id.et_Email);
        etUsername = findViewById(R.id.et_Username);
        etPassword = findViewById(R.id.et_Password);
    }

    public interface EmailConfirmationListener {
        void onEmailConfirmed(String otp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}