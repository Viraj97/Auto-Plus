package com.example.autoplus;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ActivityRequetsHistory extends AppCompatActivity {

    private Resources resources;

    private ProgressBar progressBar;
    private Spinner spinnerVehicle;
    private Spinner spinnerRecord;
    private TextView textViewRecordDetails;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_history);

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> finish());

        resources = getResources();

        progressBar = findViewById(R.id.progressBar);
        spinnerVehicle = findViewById(R.id.spinnerVehicle);
        spinnerRecord = findViewById(R.id.spinnerRecord);
        textViewRecordDetails = findViewById(R.id.textViewRecordDetails);

        loadVehicles();

        spinnerVehicle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadRecordsForVehicle();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        spinnerRecord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displayRecordDetails();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void loadVehicles() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("vehicles")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        List<String> vehicleList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String vehicleNumber = document.getString("vehicleNumber");
                            vehicleList.add(vehicleNumber);
                        }
                        Collections.sort(vehicleList);
                        if (vehicleList.isEmpty()) {
                            vehicleList.add("No vehicles added"); // Placeholder item
                            spinnerVehicle.setEnabled(false);
                            spinnerRecord.setEnabled(false);

                            textViewRecordDetails.setEnabled(false);
                            textViewRecordDetails.setBackgroundColor(resources.getColor(com.google.android.material.R.color.material_on_background_disabled, getTheme()));
                        } else {
                            spinnerVehicle.setEnabled(true);
                            spinnerRecord.setEnabled(true);

                            textViewRecordDetails.setEnabled(true);
                            textViewRecordDetails.setBackgroundColor(resources.getColor(R.color.white, getTheme()));
                        }
                        populateVehicleSpinner(vehicleList);
                    } else {
                        Toast.makeText(this, "Failed to load vehicles: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void populateVehicleSpinner(List<String> vehicleList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicleList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicle.setAdapter(adapter);
    }

    private void loadRecordsForVehicle() {
        String selectedVehicle = (String) spinnerVehicle.getSelectedItem();
        if (selectedVehicle != null && !selectedVehicle.equals("No vehicles added")) {
            progressBar.setVisibility(View.VISIBLE);
            db.collection("appointments")
                    .whereEqualTo("vehicleNumber", selectedVehicle)
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            List<String> recordList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String dateTime = document.getString("dateTime");
                                recordList.add(dateTime);
                            }
                            if (recordList.isEmpty()) {
                                recordList.add("No records found"); // Placeholder item
                                spinnerRecord.setEnabled(false);

                                textViewRecordDetails.setEnabled(false);
                                textViewRecordDetails.setBackgroundColor(resources.getColor(com.google.android.material.R.color.material_on_background_disabled, getTheme()));
                            } else {
                                spinnerRecord.setEnabled(true);

                                textViewRecordDetails.setEnabled(true);
                                textViewRecordDetails.setBackgroundColor(resources.getColor(R.color.white, getTheme()));
                            }
                            Collections.sort(recordList, Collections.reverseOrder());
                            populateRecordSpinner(recordList);
                        } else {
                            Toast.makeText(this, "Failed to load records: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            populateRecordSpinner(new ArrayList<>()); // Reset record spinner if no vehicle is selected
        }
    }

    private void populateRecordSpinner(List<String> recordList) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, recordList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRecord.setAdapter(adapter);
    }

    private void displayRecordDetails() {
        String selectedVehicle = (String) spinnerVehicle.getSelectedItem();
        String selectedRecord = (String) spinnerRecord.getSelectedItem();
        if (selectedRecord != null && selectedRecord.equals("No records found")) {
            textViewRecordDetails.setText("No records found for the selected vehicle."); // Set meaningful message
            return;
        }
        if (selectedVehicle != null && selectedRecord != null) {
            progressBar.setVisibility(View.VISIBLE);
            db.collection("appointments")
                    .whereEqualTo("vehicleNumber", selectedVehicle)
                    .whereEqualTo("dateTime", selectedRecord)
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            StringBuilder recordDetails = new StringBuilder();
                            recordDetails.append("Vehicle Number: ").append(document.getString("vehicleNumber")).append("\n")
                                    .append("Date and Time: ").append(document.getString("dateTime")).append("\n")
                                    .append("ODO Meter Reading (KM)t: ").append(document.getString("odoMeter")).append("\n");
                            textViewRecordDetails.setText(recordDetails.toString());
                        } else {
                            Toast.makeText(this, "Failed to load record details: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

}
