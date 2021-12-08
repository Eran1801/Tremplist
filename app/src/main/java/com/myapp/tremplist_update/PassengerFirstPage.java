package com.myapp.tremplist_update;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PassengerFirstPage extends AppCompatActivity {
    Button search_rideBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_first_page);

        search_rideBtn= findViewById(R.id.search_ride);

        search_rideBtn.setOnClickListener(view -> {
            startActivity(new Intent(PassengerFirstPage.this, Search_ridesActivity.class));
//            startActivity(new Intent(PassengerFirstPage.this, show_search_resultsActivity.class));

        });

    }
}
