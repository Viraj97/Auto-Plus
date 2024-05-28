package com.example.autoplus;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NewAppointmentActivity extends AppCompatActivity {

    Resources resources;

    private ProgressBar progressBar;
    private EditText editTextDateTime;
    private EditText editTextOdoMeter;
    private Spinner spinnerVehicle;
    private Button buttonSaveAppointment;
    private FirebaseFirestore db;
    private String userId;
    private Calendar currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> finish());

        resources = getResources();

        progressBar = findViewById(R.id.progressBar);
        spinnerVehicle = findViewById(R.id.spinnerVehicle);
        editTextDateTime = findViewById(R.id.editTextDateTime);
        editTextOdoMeter = findViewById(R.id.editTextOdoMeter);
        buttonSaveAppointment = findViewById(R.id.buttonSaveAppointment);

        loadVehicles();

        editTextDateTime.setOnClickListener(v -> showDateTimePicker());

        buttonSaveAppointment.setOnClickListener(v -> saveAppointment());
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
                        populateSpinner(vehicleList);
                    } else {
                        Toast.makeText(NewAppointmentActivity.this, "Failed to load vehicles: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void populateSpinner(List<String> vehicleList) {
        if (vehicleList.isEmpty()) {
            vehicleList.add("No vehicles added");
            spinnerVehicle.setEnabled(false);
            editTextDateTime.setEnabled(false);
            editTextOdoMeter.setEnabled(false);
            buttonSaveAppointment.setEnabled(false);

            editTextDateTime.setBackgroundColor(resources.getColor(com.google.android.material.R.color.material_on_background_disabled, getTheme()));
            editTextOdoMeter.setBackgroundColor(resources.getColor(com.google.android.material.R.color.material_on_background_disabled, getTheme()));
        } else {
            spinnerVehicle.setEnabled(true);
            editTextDateTime.setEnabled(true);
            editTextOdoMeter.setEnabled(true);
            buttonSaveAppointment.setEnabled(true);

            editTextDateTime.setBackgroundColor(resources.getColor(R.color.white, getTheme()));
            editTextOdoMeter.setBackgroundColor(resources.getColor(R.color.white, getTheme()));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicleList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVehicle.setAdapter(adapter);
    }

    private void showDateTimePicker() {
        currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        currentDate.add(Calendar.MINUTE, 30); // Adding 30 minutes to the current time

        DatePickerDialog datePickerDialog = new DatePickerDialog(NewAppointmentActivity.this, (view, year, month, dayOfMonth) -> {
            date.set(year, month, dayOfMonth);

            TimePickerDialog timePickerDialog = new TimePickerDialog(NewAppointmentActivity.this, (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);

                // Check if the selected date and time is at least 30 minutes from the current date and time
                if (date.before(currentDate)) {
                    Toast.makeText(NewAppointmentActivity.this, "Please select a time at least 30 minutes from now.", Toast.LENGTH_LONG).show();
                    showDateTimePicker();
                } else {
                    editTextDateTime.setText(DateFormat.format("yyyy-MM-dd HH:mm", date));
                }

            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), true);

            timePickerDialog.show();

        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));

        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        datePickerDialog.show();
    }

    private void saveAppointment() {
        progressBar.setVisibility(View.VISIBLE);
        String selectedVehicle = spinnerVehicle.getSelectedItem().toString();
        String dateTime = editTextDateTime.getText().toString();
        String odoMeter = editTextOdoMeter.getText().toString();

        if (TextUtils.isEmpty(selectedVehicle) || TextUtils.isEmpty(dateTime) || TextUtils.isEmpty(odoMeter)) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "All fields are mandatory.", Toast.LENGTH_LONG).show();
            return;
        }

        Map<String, Object> appointment = new HashMap<>();
        appointment.put("userId", userId);
        appointment.put("vehicleNumber", selectedVehicle);
        appointment.put("dateTime", dateTime);
        appointment.put("odoMeter", odoMeter);

        db.collection("appointments")
                .add(appointment)
                .addOnSuccessListener(documentReference -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(NewAppointmentActivity.this, "Appointment saved successfully", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(NewAppointmentActivity.this, "Failed to save appointment: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

}
