package com.myapp.tremplist_update;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PassangerFirstPage extends AppCompatActivity {
    Button search_rideBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passanger_first_pagec);

//        findViewById(R.id.search_ride).setOnClickListener(view -> {
//            startActivity(new Intent(PassangerFirstPage.this, Search_rides.class));
//        });

    }
}
