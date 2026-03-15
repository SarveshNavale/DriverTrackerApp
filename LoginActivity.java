package com.example.drivertracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin, buttonRegister;
    private TextView textViewSwitch, loginTitle, appTitle;
    private ImageView logo;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);

        logo = findViewById(R.id.logo);
        appTitle = findViewById(R.id.appTitle);

        loginTitle = findViewById(R.id.loginTitle);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewSwitch = findViewById(R.id.textViewSwitch);

        // Hide login UI initially
        loginTitle.setVisibility(View.INVISIBLE);
        editTextUsername.setVisibility(View.INVISIBLE);
        editTextPassword.setVisibility(View.INVISIBLE);
        buttonLogin.setVisibility(View.INVISIBLE);
        buttonRegister.setVisibility(View.INVISIBLE);
        textViewSwitch.setVisibility(View.INVISIBLE);

        // Splash delay
        new Handler().postDelayed(() -> {

            // Hide splash
            logo.setVisibility(View.GONE);
            appTitle.setVisibility(View.GONE);

            // Show login
            loginTitle.setVisibility(View.VISIBLE);
            editTextUsername.setVisibility(View.VISIBLE);
            editTextPassword.setVisibility(View.VISIBLE);
            buttonLogin.setVisibility(View.VISIBLE);
            buttonRegister.setVisibility(View.VISIBLE);
            textViewSwitch.setVisibility(View.VISIBLE);

        }, 4000);

        buttonLogin.setOnClickListener(v -> login());

        buttonRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        textViewSwitch.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login() {

        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (databaseHelper.checkUser(username, password)) {

            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
