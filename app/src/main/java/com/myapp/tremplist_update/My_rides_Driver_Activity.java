package com.myapp.tremplist_update;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

// In this class we implement the search result that will show when a passenger want to find a ride

public class My_rides_Driver_Activity extends AppCompatActivity {
    ListView listView;
    FirebaseAuth mAuth;
    ArrayList<String> ridesList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        listView = findViewById(R.id.list_view);

        ridesList = new ArrayList<>();
        MyListAdapter_forDriver adapter = new MyListAdapter_forDriver(this, R.layout.list_item_driver, ridesList);
        listView.setAdapter(adapter);


        mAuth = FirebaseAuth.getInstance();
        DatabaseReference referenceRide = FirebaseDatabase.getInstance().getReference().child("rides");

        referenceRide.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                // if something has left from the last use
                ridesList.clear();
                //go over all the rides in the firebase
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                        Ride ride = snapshot.getValue(Ride.class);
                    assert ride != null; // make sure Ride not null
                    //check if the current ride is fit to the search details
                    String curr_id=mAuth.getCurrentUser().getUid();
                    String driver_id=ride.getDriver().getId();
                    if (driver_id.equals(curr_id)) {
                        String txt_to_add;
                        String dest_src = ride.getSrc_city();
                        if (!ride.getSrc_details().isEmpty())
                            dest_src += "(" + ride.getSrc_details() + ")";
                        dest_src += "-->" + ride.getDst_city();
                        if (!ride.getDst_details().isEmpty())
                            dest_src += "(" + ride.getDst_details() + ")";

                        String available_sits = "\n" + "מקומות פנויים: " + ride.getFree_sits() + " מתוך " + ride.getSits();


                        String hour = "";
                        if (ride.getHour().getHour() < 10)
                            hour += "0" + ride.getHour().getHour();
                        else
                            hour += ride.getHour().getHour() + ":";
                        if (ride.getHour().getMinute() < 10)
                            hour += "0" + ride.getHour().getMinute();
                        else hour += ride.getHour().getMinute();
                        String date_hour = "\n" + hour + " ," + ride.getDate().getDay() + "/" + ride.getDate().getMonth() + "/" + ride.getDate().getYear();


                        String car_details = "";
                        if (!ride.getCar_color().isEmpty() && !ride.getCar_type().isEmpty())
                            car_details = "\nפרטי הרכב: " + ride.getCar_type() + " ," + ride.getCar_color();
                        else if (!ride.getCar_type().isEmpty())
                            car_details = "\nסוג הרכב: " + ride.getCar_type();
                        else if (!ride.getCar_color().isEmpty())
                            car_details = "\nצבע הרכב: " + ride.getCar_color();


                        txt_to_add = dest_src + date_hour + available_sits + car_details;
                        //add the current ride (as a string) to the list
                        ridesList.add(txt_to_add);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}