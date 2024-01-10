package com.example.codehub.pager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.codehub.R;
import com.example.codehub.adapters.RequestAdapter;
import com.example.codehub.database.SqlConnection;
import com.example.codehub.models.Request;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class ProgressFragment extends Fragment {
    private SqlConnection connection;
    private List<Request> requests;
    private RequestAdapter adtRequest;
    private RecyclerView rvRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_progress, container, false);
        init(root);
        requests = connection.listRequest(1);
        setupRecycleView(requests);
        adtRequest.setOnItemClickListener(this::showDialogInformation);
        return root;
    }

    private void showDialogInformation(int id) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_request, null);
        dialogBuilder.setView(dialogView);
        TextView name, desc, status, date;
        Button btnOK;
        btnOK = dialogView.findViewById(R.id.btnOK);
        name = dialogView.findViewById(R.id.tv_name);
        desc = dialogView.findViewById(R.id.tv_desc);
        status = dialogView.findViewById(R.id.tv_status);
        date = dialogView.findViewById(R.id.tv_date);

        Request request = connection.getRequest(id);
        name.setText(request.getName());
        desc.setText(request.getDesc());
        date.setText(formattedDateTime(request.getDatetime()));
        if (request.getStatus() == -1) {
            status.setText("Đã bị hủy");
        } else if (request.getStatus() == 0) {
            status.setText("Chờ xác nhận");
        } else if (request.getStatus() == 1) {
            status.setText("Đang xử lí");
        } else if (request.getStatus() == 2) {
            status.setText("Đã xong");
        }
        AlertDialog alertDialog = dialogBuilder.create();
        btnOK.setOnClickListener(view -> alertDialog.dismiss());
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void setupRecycleView(List<Request> requests) {
        adtRequest = new RequestAdapter(requests);
        rvRequest.setAdapter(adtRequest);
        rvRequest.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void init(View root) {
        connection = new SqlConnection(getContext());
        rvRequest = root.findViewById(R.id.listRequest);
    }

    private String formattedDateTime(Timestamp timestamp) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        return sdf.format(timestamp);
    }
}