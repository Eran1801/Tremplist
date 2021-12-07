package com.myapp.tremplist_update;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Publish_activity extends AppCompatActivity {
    FireBaseDBActivity fb;

    Button publish_rideBtn;
    private String src_city;
    private String src_details;
    private String dst_city;
    private String dst_details;

    private Date date;
    private Hour hour;

    private String car_type;
    private String car_color;
    private int sits;
    private int free_sits;
    private int ride_cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("44444444444444","444444444444444");
        setContentView(R.layout.activity_publish_ride);
        publish_rideBtn = findViewById(R.id.publish_a_ride);
        src_city = findViewById(R.id.src_city2).toString();
        src_details = findViewById(R.id.src_detail).toString();
        dst_city = findViewById(R.id.dest_city).toString();
        dst_details = findViewById(R.id.dest_detail).toString();

        String[] tmp_date = findViewById(R.id.date).toString().split(".");
        date = new Date(Integer.parseInt(tmp_date[0]), Integer.parseInt(tmp_date[1]), Integer.parseInt(tmp_date[2]));
        String[] tmp_hour = findViewById(R.id.hour).toString().split(":");
        hour = new Hour(Integer.parseInt(tmp_hour[0]), Integer.parseInt(tmp_hour[0]));
        car_type = findViewById(R.id.car_type).toString();
        car_color = findViewById(R.id.car_color).toString();
        sits = Integer.parseInt(findViewById(R.id.sits).toString());
        free_sits = Integer.parseInt(findViewById(R.id.free_sits).toString());
        ride_cost = sits = Integer.parseInt(findViewById(R.id.cost).toString());

        Ride ride = new Ride(src_city,dst_city,date,hour,free_sits,ride_cost);

        publish_rideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("5555555555555555","55555555555555");
                fb = new FireBaseDBActivity();
                fb.addRideToDB(ride);
            }
        });
    }
}
