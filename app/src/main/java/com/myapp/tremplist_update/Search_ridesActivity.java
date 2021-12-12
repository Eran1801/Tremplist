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
import android.widget.Toast;

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
                showFromDatePickerDialog();
            }
        });

        findViewById(R.id.search_hour_from).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(date_from == null){
                    Toast.makeText(Search_ridesActivity.this, "First choose date", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.search_date_from).requestFocus();
                }
                //TODO
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        findViewById(R.id.search_date_to).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(date_from == null){
                    Toast.makeText(Search_ridesActivity.this, "First choose start date", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.search_date_from).requestFocus();
                }
                if (hour_from == null){
                    Toast.makeText(Search_ridesActivity.this, "First choose start time", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.search_hour_from).requestFocus();
                }
                showToDatePickerDialog();
            }
        });

        findViewById(R.id.search_hour_to).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(date_from == null){
                    Toast.makeText(Search_ridesActivity.this, "First choose start date", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.search_date_from).requestFocus();
                }
                if (hour_from == null){
                    Toast.makeText(Search_ridesActivity.this, "First choose start hour", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.search_hour_from).requestFocus();
                }
                if (date_to == null){
                    Toast.makeText(Search_ridesActivity.this, "First choose end date", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.search_date_to).requestFocus();
                }
                //TODO
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
//                    Log.d("tremp: ","src_city:"+src_city+"\n");
                    startActivity(intent);


                }

            }
        });

    }
    private void showFromDatePickerDialog(){
        DatePickerDialog datePickerDialog =new DatePickerDialog(
                this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();

    }
    private void showToDatePickerDialog(){
        DatePickerDialog datePickerDialog =new DatePickerDialog(
                this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        java.util.Date minD = new java.util.Date(date_from.getYear(),date_from.getMonth(), date_from.getDay());
        datePickerDialog.getDatePicker().setMinDate(minD.getTime());
        datePickerDialog.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (!flag_date)
            date_from=new Date(dayOfMonth, month+1, year);
        else date_to=new Date(dayOfMonth, month+1, year);
        flag_date=true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        if (!flag_hour) {
            if(date_from.getDay() == c.get(Calendar.DAY_OF_MONTH)){
                if(hourOfDay < c.get(Calendar.HOUR_OF_DAY) ||
                        hourOfDay==c.get(Calendar.HOUR_OF_DAY) && minute <= c.get(Calendar.MINUTE)) {
                    Toast.makeText(Search_ridesActivity.this, "Cannot be before now", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            hour_from = new Hour(hourOfDay, minute);
        }else {
            if(date_to.getDay() == date_from.getDay() &&
                    date_to.getMonth() == date_from.getMonth()){
                if(hourOfDay < hour_from.getHour() ||
                        (hourOfDay == hour_from.getHour() && minute <= hour_from.getMinute())){
                    Toast.makeText(Search_ridesActivity.this, "Cannot be before start", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            hour_to = new Hour(hourOfDay, minute);
            flag_hour = true;
        }
    }

}
