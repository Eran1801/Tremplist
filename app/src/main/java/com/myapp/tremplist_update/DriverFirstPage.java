package com.myapp.tremplist_update;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DriverFirstPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_first_page);

        Button publish_rideBtn = findViewById(R.id.publish_ride);
        Button my_ridesBtn = findViewById(R.id.driver_my_rides);
        Button personal_infoBtn = findViewById(R.id.personal_infoBtn);

        publish_rideBtn.setOnClickListener(v -> setContentView(R.layout.activity_publish_ride));
        my_ridesBtn.setOnClickListener(v -> setContentView(R.layout.activity_passanger_history_rides));
        personal_infoBtn.setOnClickListener(v -> setContentView(R.layout.activity_passanger_personal_info));


    }
}
