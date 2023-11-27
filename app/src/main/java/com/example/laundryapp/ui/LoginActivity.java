package com.example.laundryapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laundryapp.R;
import com.example.laundryapp.db.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextInputEditText emailEditText, passwordEditText;
    private TextView registerText;
    private TextInputLayout usernameInputLayout, passwordInputLayout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerText = findViewById(R.id.registerText);
        usernameInputLayout = findViewById(R.id.usernameInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);


        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            String userEmail = sharedPreferences.getString("userEmail", "");
            if (!userEmail.isEmpty()) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("email", userEmail);
                startActivity(intent);
                finish();
            }
        }

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (isValidEmail(email) && isValidPassword(password)) {
                    if (databaseHelper.checkUser(email, password)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("userEmail", email);
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!isValidEmail(email)) {
                        emailEditText.setError("Invalid email");
                    }
                    if (!isValidPassword(password)) {
                        passwordEditText.setError("Password should be at least 8 characters");
                    }
                }
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }
}

