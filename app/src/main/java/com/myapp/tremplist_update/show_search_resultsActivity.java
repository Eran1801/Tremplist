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
    private Hour hour_to;
    private Date date_from;
    private Date date_to;
    private String from;
    private String to;
    ListView listView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_results);

        from = getIntent().getStringExtra("src_city");
        to= getIntent().getStringExtra("dest_city");
        String[] d1 = getIntent().getStringExtra("date_from").split("/");
        String[] d2 = getIntent().getStringExtra("date_to").split("/");
        String[] h1 = getIntent().getStringExtra("hour_from").split(":");
        String[] h2 = getIntent().getStringExtra("hour_to").split(":");

        date_from= new Date(Integer.parseInt(d1[0]), Integer.parseInt(d1[1]), Integer.parseInt(d1[2]));
        date_to= new Date(Integer.parseInt(d2[0]), Integer.parseInt(d2[1]), Integer.parseInt(d2[2]));
        hour_from= new Hour(Integer.parseInt(h1[0]), Integer.parseInt(h1[1]));
        hour_to= new Hour(Integer.parseInt(h2[0]), Integer.parseInt(h2[1]));


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

                    if(ride.getSrc_city().equals(from) && ride.getDst_city().equals(to)
                            && ((ride.getDate().compareTo(date_from)>0 ||(ride.getDate().compareTo(date_from)==0 && ride.getHour().compareTo(hour_from)>=0))
                            && (ride.getDate().compareTo(date_to)<0
                            ||(ride.getDate().compareTo(date_to)==0 && ride.getHour().compareTo(hour_to)<0)))) {

                        String txt_to_add, car_color = "", car_type = "", from_details = "", to_details = "";
                        String from = "From: " + ride.getSrc_city(),
                                to = "\nTo: " + ride.getDst_city(),
                                date = "\nDate: " + ride.getDate().getDay() + "/" + ride.getDate().getMonth() + "/" + ride.getDate().getYear(),
                                available_sits = "\nfree sits: " + ride.getFree_sits() + " out of " + ride.getSits();
                        String hour = "\nHour: ";
                        if (ride.getHour().getHour()==0)
                             hour+= "00:";
                        else
                            hour+=ride.getHour().getHour() + ":";
                        if (ride.getHour().getMinute()==0)
                            hour+="00";
                        else  hour+= ride.getHour().getMinute();

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
                    }

                    Log.d("*****1",(ride.getDate().compareTo(date_from))+"");
                    Log.d("*****2",(ride.getHour().compareTo(hour_from)) +"");
                    Log.d("*****3",(ride.getDate().compareTo(date_to)) +"");
                    Log.d("*****4",(ride.getHour().compareTo(hour_to)) +"");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}
