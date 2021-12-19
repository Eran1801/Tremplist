package com.myapp.tremplist_update;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/*
In this class we implement the Passanger HomePage
The option we have is a Search ride, My rides, and Personal info
*/

public class PassengerFirstPage extends AppCompatActivity {
    Button search_rideBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_first_page);

        search_rideBtn= findViewById(R.id.search_ride);

        // When you choose the search button
        search_rideBtn.setOnClickListener(view -> {
            startActivity(new Intent(PassengerFirstPage.this, Search_ridesActivity.class));
        });

    }
}
