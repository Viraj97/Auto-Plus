package com.example.autoplus;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EmergencyServicesActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap gmap;
    private ImageButton backButton;
    private Button requestCarrier, help, preGuid;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;

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
                onBackPressed();
            }
        });

        requestCarrier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EmergencyServicesActivity.this, "Request a Carrier clicked", Toast.LENGTH_SHORT).show();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EmergencyServicesActivity.this, "Call for help clicked", Toast.LENGTH_SHORT).show();
            }
        });

        preGuid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EmergencyServicesActivity.this, "View Vehicle Pre Guid clicked", Toast.LENGTH_SHORT).show();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationCallback();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gmap= googleMap;
        enableMyLocation();
        moveCameraToCurrentLocation();
        LatLng locationOne = new LatLng(-37.826171233953836, 144.9755273911699);
        LatLng locationtwo = new LatLng(-37.7643158307081, 145.00608311440956);
        LatLng locationthree = new LatLng(-37.773678855046484, 144.96333943414734);
        LatLng locationfour = new LatLng(-37.773678855046484, 144.96694432284417);
        LatLng locationfive = new LatLng(-37.854138848827176, 144.99826111525743);
        LatLng locationsix = new LatLng(-37.843595586837, 145.09391085332584);
        LatLng locationseven = new LatLng(-37.7601548698663, 144.8882881930621);
        LatLng locationeight = new LatLng(-37.749406228822544, 144.83779289986862);
        googleMap.addMarker(new MarkerOptions().position(locationOne).title(""));
        googleMap.addMarker(new MarkerOptions().position(locationtwo).title(""));
        googleMap.addMarker(new MarkerOptions().position(locationthree).title(""));
        googleMap.addMarker(new MarkerOptions().position(locationfour).title(""));
        googleMap.addMarker(new MarkerOptions().position(locationfive).title(""));
        googleMap.addMarker(new MarkerOptions().position(locationsix).title(""));
        googleMap.addMarker(new MarkerOptions().position(locationseven).title(""));
        googleMap.addMarker(new MarkerOptions().position(locationeight).title(""));
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            gmap.setMyLocationEnabled(true);
        } else {
            // Request permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void moveCameraToCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
                }
            });
        }
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    null);
        }
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
