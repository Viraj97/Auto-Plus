package com.example.autoplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class EditMyVehicleActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String vehicleNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_vehicle);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        if (intent != null) {
            vehicleNumber = intent.getStringExtra("vehicleNumber");
            if (vehicleNumber != null) {
                populateVehicleDetails(vehicleNumber);
            }
        }

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVehicleChanges(vehicleNumber);
            }
        });
    }

    private void populateVehicleDetails(String vehicleNumber) {
        db.collection("vehicles")
                .whereEqualTo("vehicleNumber", vehicleNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String brand = document.getString("brand");
                                String model = document.getString("model");
                                String manufactureYear = document.getString("manufactureYear");
                                String odo = document.getString("odoReading");
                                String color = document.getString("color");

                                EditText editTextVehicleNumber = findViewById(R.id.editTextEditVehicleNumber);
                                editTextVehicleNumber.setText(vehicleNumber);

                                EditText editTextBrand = findViewById(R.id.editTextEditBrand);
                                editTextBrand.setText(brand);

                                EditText editTextModel = findViewById(R.id.editTextEditModel);
                                editTextModel.setText(model);

                                EditText editTextManufactureYear = findViewById(R.id.editTextEditManufactureYear);
                                editTextManufactureYear.setText(manufactureYear);

                                EditText editTextODO = findViewById(R.id.editTextEditODO);
                                editTextODO.setText(odo);

                                EditText editTextColor = findViewById(R.id.editTextEditColor);
                                editTextColor.setText(color);
                            }
                        } else {
                            Toast.makeText(EditMyVehicleActivity.this, "Error getting vehicle details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void saveVehicleChanges(String vehicleNumber) {
        String newVehicleNumber = ((EditText) findViewById(R.id.editTextEditVehicleNumber)).getText().toString();
        String brand = ((EditText) findViewById(R.id.editTextEditBrand)).getText().toString();
        String model = ((EditText) findViewById(R.id.editTextEditModel)).getText().toString();
        String manufactureYear = ((EditText) findViewById(R.id.editTextEditManufactureYear)).getText().toString();
        String odo = ((EditText) findViewById(R.id.editTextEditODO)).getText().toString();
        String color = ((EditText) findViewById(R.id.editTextEditColor)).getText().toString();

        db.collection("vehicles")
                .whereEqualTo("vehicleNumber", vehicleNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().update(
                                        "vehicleNumber", newVehicleNumber,
                                        "brand", brand,
                                        "model", model,
                                        "manufactureYear", manufactureYear,
                                        "odoReading", odo,
                                        "color", color
                                ).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> updateTask) {
                                        if (updateTask.isSuccessful()) {
                                            Toast.makeText(EditMyVehicleActivity.this, "Changes saved for vehicle: ", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(EditMyVehicleActivity.this, "Failed to save changes for vehicle: ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(EditMyVehicleActivity.this, "Error getting vehicle details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
