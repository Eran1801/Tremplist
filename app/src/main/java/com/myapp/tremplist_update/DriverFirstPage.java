package com.myapp.tremplist_update;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DriverFirstPage extends AppCompatActivity {
    Button publish_rideBtn;
    Button myRidesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_first_page);

        publish_rideBtn = (Button)findViewById(R.id.publish_ride);
        myRidesBtn = (Button)findViewById(R.id.driver_my_rides);

        publish_rideBtn.setOnClickListener(view -> {
            startActivity(new Intent(DriverFirstPage.this, Publish_activity.class));
        });

        myRidesBtn.setOnClickListener(view -> {
            startActivity(new Intent(DriverFirstPage.this, My_rides_Driver_Activity.class));
        });


    }

}
