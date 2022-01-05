package com.myapp.tremplist_update.viewModel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myapp.tremplist_update.R;


// This is the main screen after Login to the app

public class MainActivity extends AppCompatActivity {

    Button btnLogOut;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button to_passengerBtn = findViewById(R.id.to_passenger); // Passanger page
        Button to_driverBtn = findViewById(R.id.to_driver); // Driver page
        Button to_personalInfo = findViewById(R.id.btnPersonalInfo) ;// personal info

        btnLogOut = findViewById(R.id.btnLogout2);
        mAuth = FirebaseAuth.getInstance();

        // Log out button
        btnLogOut.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

        // Choose Driver page
        to_driverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DriverFirstPage.class));
            }
        });

        // Choose Passanger page
        to_passengerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PassengerFirstPage.class));
            }
        });

        to_personalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PersonalUserInfoActivity.class));
            }
        });

    }

    // If the user is already login no need to login again
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }
}