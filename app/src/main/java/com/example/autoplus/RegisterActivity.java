package com.example.autoplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhoneNumber;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        buttonRegister = findViewById(R.id.buttonRegister);
        TextView textViewBackToLogin = findViewById(R.id.textViewBackToLogin);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve entered information
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String phoneNumber = editTextPhoneNumber.getText().toString();

                // Dummy registration logic, replace with your actual registration logic
                String message = "Name: " + name + "\nEmail: " + email + "\nPhone Number: " + phoneNumber;
                Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

        textViewBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to LoginActivity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                // Start the LoginActivity
                startActivity(intent);
                // Finish the current activity (RegisterActivity)
                finish();
            }
        });
    }
}

