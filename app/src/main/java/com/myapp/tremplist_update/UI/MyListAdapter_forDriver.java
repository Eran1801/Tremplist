package com.myapp.tremplist_update.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.myapp.tremplist_update.fireBase.FireBaseDBActivity;
import com.myapp.tremplist_update.R;
import com.myapp.tremplist_update.model.Ride;
import com.myapp.tremplist_update.model.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MyListAdapter_forDriver extends ArrayAdapter<String> {
    private int layout;
    Context ApplicationContext;
    Activity activity;
    List<Ride> rides;
    FireBaseDBActivity fb;

    // for the pop up window
    private TextView on_popup_first_last_name, on_popup_telephone;
    private Button cancel_button;


    public MyListAdapter_forDriver(@NonNull Context context, int resource, @NonNull List<String> objects, List<Ride> rides, Context ApplicationContext, Activity activity) {
        super(context, resource, objects);
        layout = resource;
        this.ApplicationContext = ApplicationContext;
        this.activity = activity;
        this.rides = rides;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder_driver mainViewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, null, false);
            ViewHolder_driver viewHolder = new ViewHolder_driver();
            viewHolder.ride_details_driver = (TextView) convertView.findViewById(R.id.ride_details_driver);
            viewHolder.cancel_ride = (androidx.appcompat.widget.AppCompatButton) convertView.findViewById(R.id.cancel_ride);
            viewHolder.my_passengers = (androidx.appcompat.widget.AppCompatButton) convertView.findViewById(R.id.my_passangers);
            viewHolder.cancel_ride.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ride curr_ride = new Ride(rides.get(position));
                    fb = new FireBaseDBActivity();
                    fb.setContext(getContext());
                    fb.setApplicationContext(ApplicationContext);
                    fb.setActivity(activity);
                    fb.Cancel_by_Driver(curr_ride);
                }
            });
            viewHolder.my_passengers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openPopupWindow(position);
                }
            });

            convertView.setTag(viewHolder);
        }

        mainViewHolder = (ViewHolder_driver) convertView.getTag();
        mainViewHolder.ride_details_driver.setText(getItem(position));


        return convertView;
    }

    private void openPopupWindow(int position) {
        Intent popupView = new Intent(getContext(), PopUpWindow.class);
        ArrayList<String> passenger_list=new ArrayList<>();
        Ride curr_ride = new Ride(rides.get(position));
//        LinkedList<User> passengers = (LinkedList<User>) curr_ride.getTrempists().values();
        for (User u: curr_ride.getTrempists().values()){
            String s = "שם: "+u.getFirst_name()+" "+u.getLast_name();
            s+="\n"+"פלאפון: "+ u.getPhone();
            passenger_list.add(s);
        }
        if(passenger_list.size()==0){
            Toast.makeText(getContext(), "אין לך טרמפיסטים לנסיעה זו",Toast.LENGTH_SHORT).show();
        }else {
            popupView.putExtra("passenger_list", passenger_list);
            popupView.putExtra("driver_phone", curr_ride.getDriver().getPhone());
            activity.startActivity(popupView);
        }
    }


}

class ViewHolder_driver {
    TextView ride_details_driver;
    androidx.appcompat.widget.AppCompatButton cancel_ride;
    androidx.appcompat.widget.AppCompatButton my_passengers;

}

