package com.myapp.tremplist_update;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Objects;

// In this class we implement the Publish a ride option for the Driver.

public class Publish_activity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    FireBaseDBActivity fb;

    Button publish_Btn;
    private String src_city;
    private String src_details;
    private String dst_city;
    private String dst_details;

    private Date date;
    private Hour hour;

    private String car_type;
    private String car_color;
    private int sits;
    private int ride_cost;


    TextInputEditText txt_src_city;
    TextInputEditText txt_src_details;
    TextInputEditText txt_dst_city;
    TextInputEditText txt_dst_details;
    TextInputEditText txt_car_type;
    TextInputEditText txt_car_color;
    TextInputEditText txt_sits;
    TextInputEditText txt_ride_cost;

    ImageButton btn_date;
    ImageButton btn_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_ride);
        publish_Btn = findViewById(R.id.publish);
        txt_src_city = findViewById(R.id.src_city);
        txt_src_details = findViewById(R.id.src_detail);
        txt_dst_city = findViewById(R.id.dest_city);
        txt_dst_details = findViewById(R.id.dest_detail);
        txt_car_type = findViewById(R.id.car_type);
        txt_car_color = findViewById(R.id.car_color);
        txt_sits = findViewById(R.id.sits);
        txt_ride_cost = findViewById(R.id.cost);
        btn_date = findViewById(R.id.date);
        btn_time = findViewById(R.id.hour);

        // Choose Date button
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Choose Time button
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if date not choose yet, toast error
                if(date == null){
                    Toast.makeText(Publish_activity.this, "First choose date", Toast.LENGTH_SHORT).show();
                    btn_date.requestFocus();
                } else {
                    showTimePickerDialog();
                };
            }
        });

        // Choose Publish button
        publish_Btn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onClick(View view) {

                    // Getting all the data from the Driver about the Ride
                    src_city = Objects.requireNonNull(txt_src_city.getText()).toString();
                    src_details = Objects.requireNonNull(txt_src_details.getText()).toString();
                    dst_city = Objects.requireNonNull(txt_dst_city.getText()).toString();
                    dst_details = Objects.requireNonNull(txt_dst_details.getText()).toString();
                    car_type = Objects.requireNonNull(txt_car_type.getText()).toString();
                    car_color = Objects.requireNonNull(txt_car_color.getText()).toString();
                    String tmp_sits = Objects.requireNonNull(txt_sits.getText()).toString();
                    String tmp_ride_cost = Objects.requireNonNull(txt_ride_cost.getText()).toString();

                    // Checks if any of the fields is empty
                    if (TextUtils.isEmpty(src_city)) {
                        txt_src_city.setError("Src city cannot be empty");
                        txt_src_city.requestFocus();
                    } else if (TextUtils.isEmpty(dst_city)) {
                        txt_dst_city.setError("Dest city cannot be empty");
                        txt_dst_city.requestFocus();
                    } else if (TextUtils.isEmpty(tmp_sits)){
                        txt_sits.setError("Number of sits cannot be empty");
                        txt_sits.requestFocus();

                    } else if (TextUtils.isEmpty(tmp_ride_cost)){
                        txt_ride_cost.setError("Ride cost cannot be empty");
                        txt_ride_cost.requestFocus();
                    }else if (date == null) {
                        Toast.makeText(Publish_activity.this, "Choose Date", Toast.LENGTH_SHORT).show();
                        btn_date.requestFocus();
                    }else if (hour == null) {
                        Toast.makeText(Publish_activity.this, "Choose Time", Toast.LENGTH_SHORT).show();
                        btn_time.requestFocus();
                    }
                    else {
                        try {
                            sits = Integer.parseInt(tmp_sits);
                        }catch (Exception e){
                            txt_sits.setError("Number of sits need to be only numbers");
                            txt_sits.requestFocus();
                        }

                        try {
                            ride_cost = Integer.parseInt(tmp_ride_cost);
                        }catch (Exception e){
                            txt_ride_cost.setError("the cost need to be only numbers");
                            txt_ride_cost.requestFocus();
                        }


                        Ride ride = new Ride(src_city, dst_city, date, hour, sits, ride_cost);
                        if(!car_color.isEmpty())
                            ride.setCar_color(car_color);
                        if(!car_type.isEmpty())
                            ride.setCar_type(car_type);
                        if(!src_details.isEmpty())
                            ride.setSrc_details(src_details);
                        if(!dst_details.isEmpty())
                            ride.setDst_details(dst_details);

                        fb = new FireBaseDBActivity();
                        fb.setContext(Publish_activity.this);
                        // Sending the ride to a function that will add it to the Database
                        fb.addRideToDB(ride);
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
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(){
        TimePickerDialog timePickerDialog =new TimePickerDialog(
                this,  AlertDialog.THEME_HOLO_DARK,this,
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE)+5, true);
        timePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date=new Date(dayOfMonth, month+1, year);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        if(date.getDay() == c.get(Calendar.DAY_OF_MONTH)){
            if(hourOfDay < c.get(Calendar.HOUR_OF_DAY) ||
                    hourOfDay==c.get(Calendar.HOUR_OF_DAY) && minute <= c.get(Calendar.MINUTE)) {
                Toast.makeText(Publish_activity.this, "Cannot be before now", Toast.LENGTH_SHORT).show();
                btn_time.requestFocus();
                return;
            }
        }
        hour=new Hour(hourOfDay, minute);
        Log.d("THE TIME IS: ", "hour= "+hourOfDay+", minute= "+minute);
    }


}
