package com.myapp.tremplist_update;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

// In this class we implement the search result that will show when a passenger want to find a ride

public class show_search_resultsActivity extends AppCompatActivity {
    private Hour hour_from;
    private Hour hour_to;
    private Date date_from;
    private Date date_to;
    private String from;
    private String to;
    ListView listView;
    List<Ride> rides=new LinkedList<>();
    ArrayList<String> ridesList;

//    public static void setRideToJoin(int position) {
//        Ride ride_to_join =new Ride(rides.get(position));
//    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        from = getIntent().getStringExtra("src_city");
        to = getIntent().getStringExtra("dest_city");

        /*
        Here we store the date and the hour in a String array and each index store a part of the date and hour
        d1 = [day,month,year]
        h1 = [ hour, minutes]
         */

        String[] d1 = getIntent().getStringExtra("date_from").split("/");
        String[] d2 = getIntent().getStringExtra("date_to").split("/");
        String[] h1 = getIntent().getStringExtra("hour_from").split(":");
        String[] h2 = getIntent().getStringExtra("hour_to").split(":");

        date_from = new Date(Integer.parseInt(d1[0]), Integer.parseInt(d1[1]), Integer.parseInt(d1[2]));
        date_to = new Date(Integer.parseInt(d2[0]), Integer.parseInt(d2[1]), Integer.parseInt(d2[2]));
        hour_from = new Hour(Integer.parseInt(h1[0]), Integer.parseInt(h1[1]));
        hour_to = new Hour(Integer.parseInt(h2[0]), Integer.parseInt(h2[1]));


        listView = findViewById(R.id.list_view);
        ridesList = new ArrayList<>();


        MyListAdapter_forSearch adapter = new MyListAdapter_forSearch(this, R.layout.list_item_search, ridesList, rides);
        listView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("rides");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                // if something has left from the last use
                ridesList.clear();
                rides.clear();
                //go over all the rides in the firebase
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {

                    Ride ride = snapshot.getValue(Ride.class);
                    assert ride != null; // make sure Ride not null
                    //check if the current ride is fit to the search details
                    if (ride.getFree_sits()>0 && ride.getSrc_city().equals(from) && ride.getDst_city().equals(to)
                            && ((ride.getDate().compareTo(date_from) > 0 || (ride.getDate().compareTo(date_from) == 0 && ride.getHour().compareTo(hour_from) >= 0))
                            && (ride.getDate().compareTo(date_to) < 0
                            || (ride.getDate().compareTo(date_to) == 0 && ride.getHour().compareTo(hour_to) < 0)))) {

                        String ride_key= snapshot.getKey();
                        ride.setId(ride_key);
                        rides.add(ride);

                        String txt_to_add;
                        String dest_src = ride.getSrc_city();
                        if (!ride.getSrc_details().isEmpty())
                            dest_src += "(" + ride.getSrc_details() + ")";
                        dest_src += "-->" + ride.getDst_city();
                        if (!ride.getDst_details().isEmpty())
                            dest_src += "(" + ride.getDst_details() + ")";

                        String available_sits = "\n" + "מקומות פנויים: " + ride.getFree_sits() + " מתוך " + ride.getSits();
                        String Driver = "\nנהג/ת: " + ride.getDriver().getFirst_name() + " " + ride.getDriver().getLast_name()
                                + "\nמספר פלאפון: " + ride.getDriver().getPhone();

                        String hour = "";
                        if (ride.getHour().getHour() < 10)
                            hour += "0"+ride.getHour().getHour();
                        else
                            hour += ride.getHour().getHour() + ":";
                        if (ride.getHour().getMinute() < 10)
                            hour += "0"+ride.getHour().getMinute();
                        else hour += ride.getHour().getMinute();

                        String date_hour = "\n" + hour + " ," + ride.getDate().getDay() + "/" + ride.getDate().getMonth() + "/" + ride.getDate().getYear();


                        String car_details = "";
                        if (!ride.getCar_color().isEmpty() && !ride.getCar_type().isEmpty())
                            car_details = "\nפרטי הרכב: " + ride.getCar_type() + " ," + ride.getCar_color();
                        else if (!ride.getCar_type().isEmpty())
                            car_details = "\nסוג הרכב: " + ride.getCar_type();
                        else if (!ride.getCar_color().isEmpty())
                            car_details = "\nצבע הרכב: " + ride.getCar_color();


                        txt_to_add = dest_src + date_hour + available_sits + car_details + Driver;
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
