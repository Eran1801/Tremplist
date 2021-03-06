package com.myapp.tremplist_update.viewModel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.tremplist_update.R;
import com.myapp.tremplist_update.model.Date;
import com.myapp.tremplist_update.model.Hour;
import com.myapp.tremplist_update.model.Ride;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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

    FirebaseAuth mAuth = FirebaseAuth.getInstance();


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
        Context ApplicationContext = getApplicationContext();
        Activity activity = show_search_resultsActivity.this;

        MyListAdapter_forSearch adapter = new MyListAdapter_forSearch(this, R.layout.list_item_search, ridesList, rides, ApplicationContext, activity);
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
                    assert ride != null;
                    System.out.println(ride.toString());
                    //check if the current ride is fit to the search details
                    if (ride.getFree_sits()>0 && ride.getSrc_city().equals(from) && ride.getDst_city().equals(to)
                            && ((ride.getDate().compareTo(date_from) > 0 || (ride.getDate().compareTo(date_from) == 0 && ride.getHour().compareTo(hour_from) >= 0))
                            && (ride.getDate().compareTo(date_to) < 0
                            || (ride.getDate().compareTo(date_to) == 0 && ride.getHour().compareTo(hour_to) < 0))) &&
                            !ride.getDriver().getId().equals(Objects.requireNonNull(mAuth.getCurrentUser()).getUid())) {

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

                        String available_sits = "\n" + "???????????? ????????????: " + ride.getFree_sits() + " ???????? " + ride.getSits();
                        String Driver = "\n??????/??: " + ride.getDriver().getFirst_name() + " " + ride.getDriver().getLast_name()
                                + "\n???????? ????????????: " + ride.getDriver().getPhone();
                        try{
                            int rate = Math.round(ride.getDriver().getSum_rate() / ride.getDriver().getCount_rate());
                            Driver += "\n?????????? ??????: ";
                            Driver += ""+rate;
                            Driver += "/5";
                        }catch (Exception exc){

                        }

                        String hour = "";
                        if (ride.getHour().getHour() < 10)
                            hour += "0";
                        hour += ride.getHour().getHour() + ":";
                        if (ride.getHour().getMinute() < 10)
                            hour += "0";
                        hour += ride.getHour().getMinute();

                        String date_hour = "\n" + hour + " ," + ride.getDate().getDay() + "/" + ride.getDate().getMonth() + "/" + ride.getDate().getYear();

                        String car_details = "";
                        if (!ride.getCar_color().isEmpty() && !ride.getCar_type().isEmpty())
                            car_details = "\n???????? ????????: " + ride.getCar_type() + " ," + ride.getCar_color();
                        else if (!ride.getCar_type().isEmpty())
                            car_details = "\n?????? ????????: " + ride.getCar_type();
                        else if (!ride.getCar_color().isEmpty())
                            car_details = "\n?????? ????????: " + ride.getCar_color();




                        txt_to_add = dest_src + date_hour + available_sits + car_details + Driver;
                        //add the current ride (as a string) to the list
                        ridesList.add(txt_to_add);
                    }
                }
//                if(ridesList.size() == 0)
//                    Toast.makeText(show_search_resultsActivity.this, "No rides found!", Toast.LENGTH_SHORT).show();

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
