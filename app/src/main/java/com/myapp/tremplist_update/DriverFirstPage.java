package com.myapp.tremplist_update;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DriverFirstPage extends AppCompatActivity {
    Button publish_rideBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_first_page);

        publish_rideBtn = (Button)findViewById(R.id.publish_ride);

        publish_rideBtn.setOnClickListener(view -> {
            startActivity(new Intent(DriverFirstPage.this, Publish_activity.class));
        });

    }
}
