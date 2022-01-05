package com.myapp.tremplist_update.viewModel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.myapp.tremplist_update.R;
import com.myapp.tremplist_update.model.Ride;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PopUpWindow extends AppCompatActivity {
    ListView listView;
    FirebaseAuth mAuth;
    ArrayList<String> ridesList;
    List<Ride> rides=new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_passengers);

        Button back_btn=findViewById(R.id.back_btn);

        ArrayList<String> passenger_list =getIntent().getStringArrayListExtra("passenger_list");
        ArrayList<String> passengers_phones =getIntent().getStringArrayListExtra("passenger_phones");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int hight = dm.heightPixels;

        getWindow().setLayout((int)(width*.7),(int)(hight*.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        listView = findViewById(R.id.list_view_passengers);

        ridesList = new ArrayList<>();
        Context ApplicationContext = getApplicationContext();
        Activity activity = PopUpWindow.this;

        MyListAdapter_MyPassengers adapter = new MyListAdapter_MyPassengers(this, R.layout.list_item_passenger, passenger_list,passengers_phones, ApplicationContext, activity);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}