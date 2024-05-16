package com.example.autoplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyVehiclesActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private LinearLayout containerLayout;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicles);

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        containerLayout = findViewById(R.id.containerLayout);
        progressBar = findViewById(R.id.progressBarLoading);

        FloatingActionButton fabAddVehicle = findViewById(R.id.fabAddVehicle);
        fabAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyVehiclesActivity.this, AddNewVehicleActivity.class));
            }
        });

        loadUserVehicles();
    }

    private void loadUserVehicles() {
        progressBar.setVisibility(View.VISIBLE);

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("vehicles")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            progressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String vehicleNumber = document.getString("vehicleNumber");
                                    if (vehicleNumber != null) {
                                        addVehicleTile(vehicleNumber);
                                    }
                                }
                            }
                        }
                    });
        }
    }

    private void addVehicleTile(String vehicleNumber) {
        // Inflate the tile layout
        View tileView = LayoutInflater.from(this).inflate(R.layout.item_vehicle_tile, containerLayout, false);

        // Set the vehicle number
        TextView textViewVehicleNumber = tileView.findViewById(R.id.textViewVehicleNumber);
        textViewVehicleNumber.setText(vehicleNumber);

        // Add the tile to the container layout
        containerLayout.addView(tileView);
    }
}
