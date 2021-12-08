package com.myapp.tremplist_update;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class show_search_resultsActivity  extends AppCompatActivity {
    private  Hour hour_from;
    private  Hour hour_to;
    private  Date date_from;
    private  Date date_to;
    private  String from, to;
    ListView listView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        listView=findViewById(R.id.list_view);

        ArrayList<String> ridesList=new ArrayList<>();
        ArrayAdapter adapter= new ArrayAdapter<String>(show_search_resultsActivity.this, R.layout.list_item, ridesList);
        listView.setAdapter(adapter);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("rides");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                ridesList.clear();
                for (DataSnapshot snapshot: datasnapshot.getChildren())
                {
                    Ride ride = snapshot.getValue(Ride.class);

//                    if(ride.getSrc_city().equals(from) && ride.getDst_city().equals(to)
//                            && (ride.getDate().compareTo(date_from)>0
//                                ||(ride.getDate().compareTo(date_from)==0 && ride.getHour().compareTo(hour_from)>=0))
//                            && (ride.getDate().compareTo(date_to)<0
//                            ||(ride.getDate().compareTo(date_to)==0 && ride.getHour().compareTo(hour_from)<0))) {

                        String txt_to_add, car_color = "", car_type = "", from_details = "", to_details = "";
                        String from = "From: " + ride.getSrc_city(),
                                to = "\nTo: " + ride.getDst_city(),
                                date = "\nDate: " + ride.getDate().getDay() + "/" + ride.getDate().getMonth() + "/" + ride.getDate().getYear(),
                                hour = "\nHour: " + ride.getHour().getHour() + ":" + ride.getHour().getMinute(),
                                available_sits = "\nfree sits: " + ride.getFree_sits() + " out of " + ride.getSits();

                        if (!ride.getCar_color().isEmpty())
                            car_color = "\nthe car's color: " + ride.getCar_color();
                        if (!ride.getCar_type().isEmpty())
                            car_type = "\nthe car's type:" + ride.getCar_type();
                        if (!ride.getSrc_details().isEmpty())
                            from_details = "\n      " + ride.getSrc_details();
                        if (!ride.getDst_details().isEmpty())
                            to_details = "\n      " + ride.getDst_details();

                        txt_to_add = from + from_details + to + to_details + date + hour + available_sits + car_type + car_color;
                        ridesList.add(txt_to_add);
                        Log.d("add key", snapshot.getKey().toString());

//                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
//    public void setSearchDetails(String From, String To, Date Date_from,
//                                 Hour Hour_from,Date Date_to, Hour Hour_to){
//        hour_to=new Hour(Hour_to);
//        hour_from=new Hour(Hour_from);
//        date_from=new Date(Date_from);
//        date_to=new Date(Date_to);
//        from=From;
//        to=To;
//    }
}
