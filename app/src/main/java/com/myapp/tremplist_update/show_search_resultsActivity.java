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
                    String txt_to_add = "From: "+ride.getSrc_city()
                            +"\nTo: "+ride.getDst_city()
                            +"\nDate: "+ride.getDate().getDay()+"/"+ride.getDate().getMonth()+"/"+ride.getDate().getYear()
                            +"\nHour: "+ride.getHour().getHour();


                    ridesList.add(snapshot.getKey().toString());
                    Log.d("add key", snapshot.getKey().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
