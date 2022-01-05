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
import com.myapp.tremplist_update.model.Ride;
import com.myapp.tremplist_update.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class driver_waiting_listActivity extends AppCompatActivity {
    ListView listView;
    FirebaseAuth mAuth;
    ArrayList<String> ridesList;
    List<Ride> rides=new LinkedList<>();
    List<User> trempists=new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        listView = findViewById(R.id.list_view);

        ridesList = new ArrayList<>();
        Context ApplicationContext = getApplicationContext();
        Activity activity = driver_waiting_listActivity.this;

        MyListAdapter_forWaitingList adapter = new MyListAdapter_forWaitingList(this, R.layout.list_item_waitinglist, ridesList, rides, trempists, ApplicationContext, activity);
        listView.setAdapter(adapter);


        mAuth = FirebaseAuth.getInstance();
        DatabaseReference referenceRide = FirebaseDatabase.getInstance().getReference().child("rides");

        referenceRide.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                // if something has left from the last use
                ridesList.clear();
                trempists.clear();
                rides.clear();
                //go over all the rides in the firebase
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    System.out.println("key= "+snapshot.getKey());
//                    if(snapshot.getKey().equals("-MsVqRxxBuZJaQmxrWNf")){
//                        System.out.println("here");
//                    }
                    Ride ride = snapshot.getValue(Ride.class);
                    assert ride != null; // make sure Ride not null
                    //check if the current ride is fit to the search details
                    String curr_id= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    String driver_id=ride.getDriver().getId();
                    Map<String, User> waiting_list=new HashMap<>(ride.getWaiting_list());

                    if (driver_id.equals(curr_id) && waiting_list.size()>0) {
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
                            hour += "0";
                        hour += ride.getHour().getHour() + ":";
                        if (ride.getHour().getMinute() < 10)
                            hour += "0";
                        hour += ride.getHour().getMinute();
                        String date_hour = "\n" + hour + " ," + ride.getDate().getDay() + "/" + ride.getDate().getMonth() + "/" + ride.getDate().getYear();


                        String car_details = "";
                        if (!ride.getCar_color().isEmpty() && !ride.getCar_type().isEmpty())
                            car_details = "\nפרטי הרכב: " + ride.getCar_type() + " ," + ride.getCar_color();
                        else if (!ride.getCar_type().isEmpty())
                            car_details = "\nסוג הרכב: " + ride.getCar_type();
                        else if (!ride.getCar_color().isEmpty())
                            car_details = "\nצבע הרכב: " + ride.getCar_color();


                        txt_to_add = dest_src + date_hour + available_sits + car_details;
                        for(User u: waiting_list.values()){
                            rides.add(ride);
                            trempists.add(u);
                            String trempist_details="\n"+"שם הטרמפיסט: "+u.getFirst_name()+" "+u.getLast_name();
                            ridesList.add(txt_to_add+trempist_details);
                        }
                        //add the current ride (as a string) to the list
                    }
                }
                if(ridesList.size() == 0)
                    Toast.makeText(driver_waiting_listActivity.this, "אין בקשות לאישור", Toast.LENGTH_SHORT).show();

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
