package com.example.codehub.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codehub.R;
import com.example.codehub.activities.CartActivity;
import com.example.codehub.activities.DetailActivity;
import com.example.codehub.adapters.LanguagesAdapter;
import com.example.codehub.adapters.SourcesAdapter;
import com.example.codehub.database.SqlConnection;
import com.example.codehub.models.User;


public class HomeFragment extends Fragment {
    RecyclerView rvSources, rvLanguages;
    TextView username;
    ImageButton btnCart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        SqlConnection connection = new SqlConnection(getContext());
        init(root);
        //recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        User user = connection.getUser();
        if (user != null) {
            username.setText(user.getFullName());
        }
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CartActivity.class);
            startActivity(intent);
        });

        //List Source
        rvSources.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        SourcesAdapter sourcesAdapter = new SourcesAdapter(connection.listSource());
        rvSources.setAdapter(sourcesAdapter);
        sourcesAdapter.setOnItemClickListener(id -> {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra("IdSource", id);
            connection.updateViewCounter(id);
            startActivity(intent);
        });
        //List Language
        rvLanguages.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
        LanguagesAdapter languagesAdapter = new LanguagesAdapter(connection.listLanguage());
        rvLanguages.setAdapter(languagesAdapter);
        languagesAdapter.setOnItemClickListener(id -> {
            Toast.makeText(getContext(), String.valueOf(id), Toast.LENGTH_SHORT).show();
        });
        return root;
    }

    private void init(View root) {
        rvSources = root.findViewById(R.id.listSource);
        rvLanguages = root.findViewById(R.id.listLanguage);
        btnCart = root.findViewById(R.id.btnCart);
        username = root.findViewById(R.id.tv_Username);
    }
}