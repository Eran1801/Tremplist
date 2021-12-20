package com.myapp.tremplist_update;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/*
In this class we implement the Passenger HomePage
The option we have is a Search ride, My rides, and Personal info
*/

public class PassengerFirstPage extends AppCompatActivity {
    Button search_rideBtn,My_ridesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_first_page);

        search_rideBtn = findViewById(R.id.search_ride);
        My_ridesBtn= findViewById(R.id.my_tremps);

        // When you choose the search button
        search_rideBtn.setOnClickListener(view -> {
            startActivity(new Intent(PassengerFirstPage.this, Search_ridesActivity.class));
        });

        My_ridesBtn.setOnClickListener(view -> {
            System.out.println("111111111111");
            startActivity(new Intent(PassengerFirstPage.this, My_rides_Passenger_Activity.class));
        });


    }
}
