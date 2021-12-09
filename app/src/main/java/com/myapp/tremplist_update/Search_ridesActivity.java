package com.myapp.tremplist_update;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Search_ridesActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    Button search_Btn;
    String src_city, dest_city;
    Date date_from, date_to;
    Hour hour_from, hour_to;

    boolean flag_date =false;
    boolean flag_hour =false;


    TextInputEditText txt_src_city;
    TextInputEditText txt_dest_city;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_ride);
        search_Btn = findViewById(R.id.find_ride);

        txt_src_city = findViewById(R.id.search_src_city);
        txt_dest_city = findViewById(R.id.search_dest_city);

        findViewById(R.id.search_date_from).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        findViewById(R.id.search_hour_from).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        findViewById(R.id.search_date_to).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        findViewById(R.id.search_hour_to).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        search_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                src_city = Objects.requireNonNull(txt_src_city.getText()).toString();
                dest_city = Objects.requireNonNull(txt_dest_city.getText()).toString();

                if (TextUtils.isEmpty(src_city)) {
                    txt_src_city.setError("Src city cannot be empty");
                    txt_src_city.requestFocus();
                } else if (TextUtils.isEmpty(dest_city)) {
                    txt_dest_city.setError("Dest city cannot be empty");
                    txt_dest_city.requestFocus();
                }else{//get the data from the firebase

                    Intent intent = new Intent(Search_ridesActivity.this, show_search_resultsActivity.class);
                    intent.putExtra("src_city", src_city);
                    intent.putExtra("dest_city", dest_city);
                    intent.putExtra("date_from",date_from.getDay()+"/"+ date_from.getMonth()+"/"+date_from.getYear());
                    intent.putExtra("date_to",date_to.getDay()+"/"+ date_to.getMonth()+"/"+date_to.getYear());
                    intent.putExtra("hour_from", hour_from.getHour()+":"+hour_from.getMinute());
                    intent.putExtra("hour_to", hour_to.getHour()+":"+hour_to.getMinute());
                    startActivity(intent);


                }

            }
        });

    }
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog =new DatePickerDialog(
                this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (!flag_date)
            date_from=new Date(dayOfMonth, month, year);
        else date_to=new Date(dayOfMonth, month, year);
        flag_date=true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (!flag_hour)
            hour_from=new Hour(hourOfDay, minute);
        else
            hour_to=new Hour(hourOfDay, minute);
        flag_hour=true;

    }

}
