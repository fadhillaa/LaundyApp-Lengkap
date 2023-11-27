package com.example.laundryapp.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.laundryapp.R;
import com.example.laundryapp.adapter.MenuAdapter;
import com.example.laundryapp.db.DatabaseHelper;
import com.example.laundryapp.model.ModelMenu;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvMenu;
    private MenuAdapter menuAdapter;
    private LinearLayout layoutHistory;
    private DatabaseHelper databaseHelper;
    private TextView usernameTxt;
    private ImageView btLogout;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvMenu = findViewById(R.id.rvMenu);
        layoutHistory = findViewById(R.id.layoutHistory);
        databaseHelper = new DatabaseHelper(this);
        usernameTxt = findViewById(R.id.usernameTxt);
        btLogout = findViewById(R.id.btLogout);

        email = getIntent().getStringExtra("email");
        String username = databaseHelper.getUsernameByEmail(email);

        if (username != null && !username.isEmpty()) {
            usernameTxt.setText(username);
        }
        layoutHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        });
        btLogout.setOnClickListener(v -> showLogoutConfirmationDialog());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvMenu.setLayoutManager(gridLayoutManager);

        List<ModelMenu> modelMenuList = new ArrayList<>();
        modelMenuList.add(new ModelMenu("Cuci Basah", R.drawable.ic_cuci_basah));
        modelMenuList.add(new ModelMenu("Dry Cleaning", R.drawable.ic_dry_cleaning));
        modelMenuList.add(new ModelMenu("Premium Wash", R.drawable.ic_premium_wash));
        modelMenuList.add(new ModelMenu("Setrika", R.drawable.ic_setrika));

        menuAdapter = new MenuAdapter(this, modelMenuList);
        rvMenu.setAdapter(menuAdapter);

        menuAdapter.setOnItemClickListener(modelMenu -> {
            String title = modelMenu.getTvTitle();
            Intent intent;
            switch (title) {
                case "Cuci Basah":
                    intent = new Intent(MainActivity.this, CuciBasahActivity.class);
                    intent.putExtra(CuciBasahActivity.DATA_TITLE, title);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    break;
                case "Dry Cleaning":
                    intent = new Intent(MainActivity.this, DryCleanActivity.class);
                    intent.putExtra(DryCleanActivity.DATA_TITLE, title);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    break;
                case "Premium Wash":
                    intent = new Intent(MainActivity.this, PremiumWashActivity.class);
                    intent.putExtra(PremiumWashActivity.DATA_TITLE, title);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    break;
                case "Setrika":
                    intent = new Intent(MainActivity.this, IroningActivity.class);
                    intent.putExtra(IroningActivity.DATA_TITLE, title);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    break;
            }
        });
    }
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Logout");
        builder.setMessage("Apakah Anda yakin ingin logout?");

        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutUser();
            }
        });

        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logoutUser() {
        SharedPreferences preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
