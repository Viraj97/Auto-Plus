package com.example.autoplus;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.autoplus.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private TextView navHeaderTitle;
    private TextView navHeaderSubtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mAuth = FirebaseAuth.getInstance();

        ImageButton myVehiclesButton = findViewById(R.id.myVehicles);
        ImageButton emergencyServiceButton = findViewById(R.id.emagency);
        ImageButton promosDiscountButton = findViewById(R.id.discounts);
        ImageButton newAppointment = findViewById(R.id.appointment);
        ImageButton requestHistory = findViewById(R.id.history);
        ImageButton status = findViewById(R.id.status);
        myVehiclesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyVehiclesActivity.class);
                startActivity(intent);
            }
        });

        emergencyServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EmergencyServicesActivity.class);
                startActivity(intent);
            }
        });

        promosDiscountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PromosDiscountsActivity.class);
                startActivity(intent);
            }
        });

        requestHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityRequetsHistory.class);
                startActivity(intent);
            }
        });

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActivityOngoingStatus.class);
                startActivity(intent);
            }
        });

        newAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewAppointmentActivity.class);
                startActivity(intent);
            }
        });

        View headerView = navigationView.getHeaderView(0);
        navHeaderTitle = headerView.findViewById(R.id.nav_header_title);
        navHeaderSubtitle = headerView.findViewById(R.id.nav_header_subtitle);
        setUserProfileInfo();
    }

    private void setUserProfileInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String displayName = user.getDisplayName();
            String email = user.getEmail();
            if (displayName != null) {
                navHeaderTitle.setText(displayName);
            }
            if (email != null) {
                navHeaderSubtitle.setText(email);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
