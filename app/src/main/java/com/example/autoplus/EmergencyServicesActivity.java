package com.example.autoplus;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;


public class EmergencyServicesActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap gmap;
    private ImageButton backButton;
    private Button requestCarrier, help, preGuid;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_services);

        backButton = findViewById(R.id.back_button);
        requestCarrier = findViewById(R.id.carrier);
        help = findViewById(R.id.help);
        preGuid = findViewById(R.id.preGuid);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back button click
                onBackPressed();
            }
        });

        requestCarrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button1 click
                Toast.makeText(EmergencyServicesActivity.this, "Request a Carrier clicked", Toast.LENGTH_SHORT).show();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button2 click
                Toast.makeText(EmergencyServicesActivity.this, "Call for help clicked", Toast.LENGTH_SHORT).show();
            }
        });

        preGuid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button3 click
                Toast.makeText(EmergencyServicesActivity.this, "View Vehicle Pre Guid clicked", Toast.LENGTH_SHORT).show();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng location = new LatLng(-37.715740, 145.049988);
        googleMap.addMarker(new MarkerOptions().position(location).title("La Trobe University Bundoora"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));
    }
}
