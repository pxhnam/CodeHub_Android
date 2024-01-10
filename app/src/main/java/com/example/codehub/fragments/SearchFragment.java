package com.example.codehub.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.codehub.R;
import com.example.codehub.adapters.SearchAdapter;
import com.example.codehub.database.SqlConnection;
import com.example.codehub.models.Source;

import java.util.List;

public class SearchFragment extends Fragment {
    private SqlConnection connection;
    List<Source> sources;
    RecyclerView rvSources;
    EditText etSearch;
    Spinner spinner;
    SearchAdapter adtSearch;
    TextView noData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        init(root);
        connection = new SqlConnection(getContext());
        loadLanguage();
        setupRecycleView();
        handleSearch();
        return root;
    }

    private void setupRecycleView() {
        sources = connection.listSource();
        if (sources.size() == 0) noData.setVisibility(View.VISIBLE);
        else noData.setVisibility(View.GONE);
        rvSources.setLayoutManager(new LinearLayoutManager(getActivity()));
        adtSearch = new SearchAdapter(sources);
        rvSources.setAdapter(adtSearch);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void handleSearch() {
        etSearch.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                int drawableEnd = etSearch.getRight() - etSearch.getCompoundDrawables()[2].getBounds().width();

                if (motionEvent.getRawX() >= drawableEnd) {
                    sources.clear();
                    sources.addAll(connection.listSource(etSearch.getText().toString().trim(),
                            spinner.getSelectedItem().toString()));
                    adtSearch.notifyDataSetChanged();
                    if (sources.size() == 0) noData.setVisibility(View.VISIBLE);
                    else noData.setVisibility(View.GONE);
                    return true;
                }
            }
            return false;
        });

    }

    private void loadLanguage() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, connection.listLanguageName());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void init(View root) {
        rvSources = root.findViewById(R.id.listSource);
        etSearch = root.findViewById(R.id.et_Search);
        spinner = root.findViewById(R.id.spinner);
        noData = root.findViewById(R.id.noData);
    }
}