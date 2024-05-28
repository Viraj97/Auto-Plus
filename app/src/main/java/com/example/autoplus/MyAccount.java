package com.example.autoplus;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class MyAccount extends AppCompatActivity {

    private EditText editUsername;
    private EditText editEmail;
    private EditText editPhone;
    private EditText editAddress;
    private EditText editIdentityCard;
    private Button buttonUpdate;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> finish());

        editUsername = findViewById(R.id.edit_username);
        editEmail = findViewById(R.id.edit_email);
        editPhone = findViewById(R.id.edit_phone);
        editAddress = findViewById(R.id.edit_address);
        editIdentityCard = findViewById(R.id.edit_identity_card);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        fetchUserInfo();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonUpdate.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        editUsername.addTextChangedListener(textWatcher);
        editEmail.addTextChangedListener(textWatcher);
        editPhone.addTextChangedListener(textWatcher);
        editAddress.addTextChangedListener(textWatcher);
        editIdentityCard.addTextChangedListener(textWatcher);

        buttonUpdate.setEnabled(false);
        buttonUpdate.setOnClickListener(v -> updateUserInfo());
    }

    private void fetchUserInfo() {
        progressBar.setVisibility(View.VISIBLE);
        if (currentUser != null) {
            currentUser.reload().addOnCompleteListener(task -> {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    editUsername.setText(currentUser.getDisplayName());
                    editEmail.setText(currentUser.getEmail());

                    // TODO: Handle this appropriately
                    editPhone.setText("");
                    editAddress.setText("");
                    editIdentityCard.setText("");
                } else {
                    Toast.makeText(MyAccount.this, "Failed to reload user information.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void updateUserInfo() {
        String username = editUsername.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String identityCard = editIdentityCard.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build();

            user.updateProfile(profileUpdates).addOnCompleteListener(task0 -> {
                if (task0.isSuccessful()) {
                    if (!email.equals(user.getEmail())) {
                        showPasswordDialog(user, email);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MyAccount.this, "User info updated successfully", Toast.LENGTH_LONG).show();
                        buttonUpdate.setEnabled(false);
                    }
                } else {
                    Toast.makeText(MyAccount.this, "Failed to update profile", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });

            // TODO: Update phone, address, and identity card
        }
    }

    private void showPasswordDialog(FirebaseUser user, String newEmail) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_password, null);

        final EditText editPassword = dialogView.findViewById(R.id.edit_password);

        new AlertDialog.Builder(this)
                .setTitle("Re-authenticate")
                .setMessage("Please enter your password to update the email")
                .setView(dialogView)
                .setPositiveButton("Submit", (dialog, which) -> {
                    String password = editPassword.getText().toString().trim();
                    if (password.isEmpty()) {
                        Toast.makeText(MyAccount.this, "Password is required", Toast.LENGTH_LONG).show();
                    } else {
                        reAuthenticateAndUpdateEmail(user, newEmail, password);
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> progressBar.setVisibility(View.GONE))
                .create()
                .show();
    }

    private void reAuthenticateAndUpdateEmail(FirebaseUser user, String email, String password) {
        user.reauthenticate(EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()), password))
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        user.verifyBeforeUpdateEmail(email).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(MyAccount.this, "User info updated successfully. Please verify your new email address.", Toast.LENGTH_LONG).show();
                                buttonUpdate.setEnabled(false);
                            } else {
                                Toast.makeText(MyAccount.this, "Failed to update email", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(MyAccount.this, "Re-authentication failed", Toast.LENGTH_LONG).show();
                    }
                });
    }

}
