package com.example.autoplus;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNewVehicleActivity extends AppCompatActivity {

    private EditText editTextVehicleNumber, editTextBrand, editTextModel, editTextManufactureYear,
            editTextODO, editTextColor;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_vehicle);

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        editTextVehicleNumber = findViewById(R.id.editTextVehicleNumber);
        editTextBrand = findViewById(R.id.editTextBrand);
        editTextModel = findViewById(R.id.editTextModel);
        editTextManufactureYear = findViewById(R.id.editTextManufactureYear);
        editTextODO = findViewById(R.id.editTextODO);
        editTextColor = findViewById(R.id.editTextColor);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(view -> saveVehicle());
    }

    private void saveVehicle() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String vehicleNumber = editTextVehicleNumber.getText().toString().trim();
        String brand = editTextBrand.getText().toString().trim();
        String model = editTextModel.getText().toString().trim();
        String manufactureYear = editTextManufactureYear.getText().toString().trim();
        String odoReading = editTextODO.getText().toString().trim();
        String color = editTextColor.getText().toString().trim();

        if (TextUtils.isEmpty(vehicleNumber) || TextUtils.isEmpty(brand) || TextUtils.isEmpty(model) ||
                TextUtils.isEmpty(manufactureYear) || TextUtils.isEmpty(odoReading) || TextUtils.isEmpty(color)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> vehicle = new HashMap<>();
        vehicle.put("userId", userId);
        vehicle.put("vehicleNumber", vehicleNumber);
        vehicle.put("brand", brand);
        vehicle.put("model", model);
        vehicle.put("manufactureYear", manufactureYear);
        vehicle.put("odoReading", odoReading);
        vehicle.put("color", color);

        FirebaseFirestore.getInstance()
                .collection("vehicles")
                .add(vehicle)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddNewVehicleActivity.this, "Vehicle saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddNewVehicleActivity.this, "Failed to save vehicle: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
