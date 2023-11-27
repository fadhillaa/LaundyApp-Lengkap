package com.example.laundryapp.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.laundryapp.R;
import com.example.laundryapp.adapter.HistoryAdapter;
import com.example.laundryapp.db.DatabaseHelper;
import com.example.laundryapp.model.ModelHistory;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvNotFound;
    private List<ModelHistory> historyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.rvHistory);
        tvNotFound = findViewById(R.id.tvNotFound);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        String email = getIntent().getStringExtra("email");
        getCheckoutHistory(email);

        HistoryAdapter historyAdapter = new HistoryAdapter(historyList, dbHelper);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(historyAdapter);

        if (historyAdapter.getItemCount() == 0) {
            tvNotFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNotFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    private void getCheckoutHistory(String userEmail) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        historyList = dbHelper.getAllCheckoutData(userEmail);
    }

}
