package com.myapp.tremplist_update.viewModel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.myapp.tremplist_update.R;
import com.myapp.tremplist_update.model.Date;
import com.myapp.tremplist_update.model.Hour;

import java.util.Calendar;
import java.util.Objects;

// In this class we implement the search ride, when a passenger search for a Ride

public class Search_ridesActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    String src_city, dest_city;
    Date date_from, date_to;
    Hour hour_from, hour_to;

    ImageButton date_fromBtn, hour_fromBtn, date_toBtn, hour_toBtn;
    Button search_Btn;

    boolean flag_date; //
    boolean flag_hour; //

    TextInputEditText txt_src_city;
    TextInputEditText txt_dest_city;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the xml that relevant for that activity
        setContentView(R.layout.activity_find_ride);

        //collect all the buttons and the text fields
        search_Btn = findViewById(R.id.find_ride);

        txt_src_city = findViewById(R.id.search_src_city);
        txt_dest_city = findViewById(R.id.search_dest_city);

        date_fromBtn = findViewById(R.id.search_date_from);
        hour_fromBtn = findViewById(R.id.search_hour_from);
        date_toBtn = findViewById(R.id.search_date_to);
        hour_toBtn = findViewById(R.id.search_hour_to);

        // When you press the data from button
        date_fromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag_date = false; // initialize a flag to know that's a "from" date that clicked
                showFromDatePickerDialog();
            }
        });

        // When you press the hour from button
        hour_fromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initialize a flag to know that's a "from" Hour that clicked
                flag_hour = false;
                if (date_from == null) {
                    Toast.makeText(Search_ridesActivity.this, "First choose start date", Toast.LENGTH_SHORT).show();
                    date_fromBtn.requestFocus();
                } else {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                }
            }
        });

        date_toBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initialize a flag to know that's a "to" date that clicked
                flag_date = true;
                if (date_from == null) {
                    Toast.makeText(Search_ridesActivity.this, "First choose start date", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.search_date_from).requestFocus();
                } else if (hour_from == null) {
                    Toast.makeText(Search_ridesActivity.this, "First choose start time", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.search_hour_from).requestFocus();
                } else {
                    showToDatePickerDialog();
                }
            }
        });

        hour_toBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initialize a flag to know that's a "to" Hour that clicked
                flag_hour = true;
                if (date_from == null) {
                    Toast.makeText(Search_ridesActivity.this, "First choose start date", Toast.LENGTH_SHORT).show();
                    date_fromBtn.requestFocus();
                } else if (hour_from == null) {
                    Toast.makeText(Search_ridesActivity.this, "First choose start time", Toast.LENGTH_SHORT).show();
                    hour_fromBtn.requestFocus();
                } else if (date_to == null) {
                    Toast.makeText(Search_ridesActivity.this, "First choose end date", Toast.LENGTH_SHORT).show();
                    date_toBtn.requestFocus();
                } else {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                }
            }
        });

        // When your press the button "Search Ride"
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
                } else if (date_from == null) {
                    Toast.makeText(Search_ridesActivity.this, "Choose from date", Toast.LENGTH_SHORT).show();
                    date_fromBtn.requestFocus();
                } else if (hour_from == null) {
                    Toast.makeText(Search_ridesActivity.this, "Choose from hour", Toast.LENGTH_SHORT).show();
                    hour_fromBtn.requestFocus();
                } else if (date_to == null) {
                    Toast.makeText(Search_ridesActivity.this, "Choose to date", Toast.LENGTH_SHORT).show();
                    date_toBtn.requestFocus();
                } else if (hour_to == null) {
                    Toast.makeText(Search_ridesActivity.this, "Choose to hour", Toast.LENGTH_SHORT).show();
                    hour_toBtn.requestFocus();
                } else {//send the details for the search to the show_search_resultsActivity
                    Intent intent = new Intent(Search_ridesActivity.this, show_search_resultsActivity.class);
                    intent.putExtra("src_city", src_city);
                    intent.putExtra("dest_city", dest_city);
                    intent.putExtra("date_from", date_from.getDay() + "/" + date_from.getMonth() + "/" + date_from.getYear());
                    intent.putExtra("date_to", date_to.getDay() + "/" + date_to.getMonth() + "/" + date_to.getYear());
                    intent.putExtra("hour_from", hour_from.getHour() + ":" + hour_from.getMinute());
                    intent.putExtra("hour_to", hour_to.getHour() + ":" + hour_to.getMinute());
                    startActivity(intent);
                }
            }
        });

    }

    // When you picking a "from" data
    private void showFromDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    // When you picking a "to" data
    private void showToDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        Calendar c1 = Calendar.getInstance();
        System.out.println("day: " + date_from.getDay() + "\nmonth: " + date_from.getMonth() + "\nyear: " + date_from.getYear());
        c1.set(Calendar.MONTH, (date_from.getMonth() - 1) % 12);
        c1.set(Calendar.DATE, date_from.getDay());
        c1.set(Calendar.YEAR, date_from.getYear());
        java.util.Date minD = c1.getTime();
        datePickerDialog.getDatePicker().setMinDate(minD.getTime());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (!flag_date) {
            // we needed to increment the month by one because google start the months from zero and not from one
            date_from = new Date(dayOfMonth, month + 1, year);
        } else {
            date_to = new Date(dayOfMonth, month + 1, year);
        }
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        if (!flag_hour) {
            if (date_from.getDay() == c.get(Calendar.DAY_OF_MONTH)) {
                if (hourOfDay < c.get(Calendar.HOUR_OF_DAY) ||
                        hourOfDay == c.get(Calendar.HOUR_OF_DAY) && minute < c.get(Calendar.MINUTE)) {
                    Toast.makeText(Search_ridesActivity.this, "Cannot be before now", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            hour_from = new Hour(hourOfDay, minute);
        } else {
            if (date_to.getDay() == date_from.getDay() &&
                    date_to.getMonth() == date_from.getMonth()) {
                if (hourOfDay < hour_from.getHour() ||
                        (hourOfDay == hour_from.getHour() && minute <= hour_from.getMinute())) {
                    Toast.makeText(Search_ridesActivity.this, "Cannot be before start", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            hour_to = new Hour(hourOfDay, minute);
        }
    }

}
