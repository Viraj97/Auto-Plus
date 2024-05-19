package com.example.autoplus;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PromosDiscountsActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private Spinner vehicleSpinner;
    private ListView promosListView;
    private ArrayAdapter<String> promosAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promos_discounts);

        ImageButton backButton = findViewById(R.id.buttonBack);
        vehicleSpinner = findViewById(R.id.vehicleSpinner);
        promosListView = findViewById(R.id.promos_listview);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this::getVehiclesForCurrentUser);

        promosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        promosListView.setAdapter(promosAdapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        db = FirebaseFirestore.getInstance();
        getVehiclesForCurrentUser();
    }

    private void getVehiclesForCurrentUser() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("vehicles")
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> vehicleNumbers = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            String vehicleNumber = document.getString("vehicleNumber");
                            if (vehicleNumber != null) {
                                vehicleNumbers.add(vehicleNumber);
                            }
                        }
                        populateVehicleSpinner(vehicleNumbers);
                    } else {
                        handleError(task.getException());
                    }
                    swipeRefreshLayout.setRefreshing(false);
                });
    }

    private void populateVehicleSpinner(List<String> vehicleNumbers) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehicleNumbers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleSpinner.setAdapter(adapter);

        vehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedVehicleNumber = (String) parent.getItemAtPosition(position);
                displayPromosForVehicle(selectedVehicleNumber);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void displayPromosForVehicle(String vehicleNumber) {
        db.collection("vehicles")
                .whereEqualTo("vehicleNumber", vehicleNumber)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        promosAdapter.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            List<String> promos = (List<String>) document.get("promos");
                            if (promos != null) {
                                for (String promoId : promos) {
                                    db.collection("AvailablePromosForVehicles")
                                            .document(promoId)
                                            .get()
                                            .addOnSuccessListener(promoDocument -> {
                                                if (promoDocument.exists()) {
                                                    String promoName = promoDocument.getString("name");
                                                    if (promoName != null) {
                                                        promosAdapter.add(promoName);
                                                    }
                                                } else {
                                                    handleError(new Exception("Promo document not found"));
                                                }
                                            })
                                            .addOnFailureListener(this::handleError);
                                }
                            }
                        }
                    } else {
                        handleError(task.getException());
                    }
                });
    }

    private void handleError(Exception exception) {
        Toast.makeText(this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
