package com.example.codehub.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.codehub.R;
import com.example.codehub.activities.RequestActivity;
import com.example.codehub.database.SqlConnection;
import com.example.codehub.models.Request;

public class TaskFragment extends Fragment {
    private Button btnSend;
    private EditText et_name, et_desc;
    private SqlConnection connection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task, container, false);
        init(root);
        btnSend.setOnClickListener(v -> {
            String name = et_name.getText().toString().trim();
            String desc = et_desc.getText().toString().trim();

            if (!name.isEmpty() && !desc.isEmpty()) {
                if (connection.postRequest(new Request(name, desc))) {
                    Toast.makeText(getContext(), "Gửi yêu cầu thành công", Toast.LENGTH_SHORT).show();
                    et_name.setText("");
                    et_desc.setText("");
                    et_name.requestFocus();
                    startActivity(new Intent(getContext(), RequestActivity.class));
                } else
                    Toast.makeText(getContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        });
        return root;
    }

    private void init(View root) {
        connection = new SqlConnection(getContext());
        btnSend = root.findViewById(R.id.btnSend);
        et_name = root.findViewById(R.id.et_name);
        et_desc = root.findViewById(R.id.et_desc);
    }
}