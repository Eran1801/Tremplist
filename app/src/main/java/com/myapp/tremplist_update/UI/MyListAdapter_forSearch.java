package com.myapp.tremplist_update.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myapp.tremplist_update.fireBase.FireBaseDBActivity;
import com.myapp.tremplist_update.R;
import com.myapp.tremplist_update.model.Ride;

import java.util.List;

public class MyListAdapter_forSearch extends ArrayAdapter<String> {
    FireBaseDBActivity fb;
    private int layout;
    private int curr_position;
    List<Ride> rides;
    Context ApplicationContext;
    Activity activity;

    public MyListAdapter_forSearch(@NonNull Context context, int resource, @NonNull List<String> objects, List<Ride> rides, Context ApplicationContext, Activity activity) {
        super(context, resource, objects);
        layout=resource;
        this.rides=rides;
        this.ApplicationContext=ApplicationContext;
        this.activity=activity;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder_search mainViewHolder=null;
        if(convertView==null){


            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(layout, null, false);
            ViewHolder_search viewHolder=new ViewHolder_search();
            viewHolder.user_img=(ImageView) convertView.findViewById(R.id.user_im);
            viewHolder.whatsapp=(ImageButton) convertView.findViewById(R.id.whatsapp);
            viewHolder.ride_details=(TextView) convertView.findViewById(R.id.ride_details);
            viewHolder.call_driver=(ImageButton) convertView.findViewById(R.id.call_driver);
            viewHolder.join_ride=(androidx.appcompat.widget.AppCompatButton) convertView.findViewById(R.id.join_ride);
            viewHolder.join_ride.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ride curr_ride= new Ride(rides.get(position));
                    fb = new FireBaseDBActivity();
                    fb.setContext(getContext());
                    fb.setApplicationContext(ApplicationContext);
                    fb.setActivity(activity);
                    fb.updateRideOnDB_join(curr_ride);
                }
            });

            viewHolder.call_driver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ride curr_ride= new Ride(rides.get(position));
                    String phone_number = "tel:"+curr_ride.getDriver().getPhone();
                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(phone_number));
                    getContext().startActivity(intent);
                }
            });

            viewHolder.whatsapp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ride curr_ride= new Ride(rides.get(position));
                    String phone_number = curr_ride.getDriver().getPhone();
                   try {
                       Intent intent = new Intent(Intent.ACTION_VIEW);
                       phone_number = phone_number.substring(1);
                       intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+972" + phone_number));
                       getContext().startActivity(intent);
                   }catch (Exception e){
                        Toast.makeText(getContext(), "אין לך whatsapp על הפלאפון", Toast.LENGTH_SHORT);
                    }

                }
            });

            convertView.setTag(viewHolder);
        }

        mainViewHolder=(ViewHolder_search) convertView.getTag();
        mainViewHolder.ride_details.setText(getItem(position));


        return convertView;
    }

    public void setRides_List(List<Ride> rides) {
        this.rides=rides;
    }

}



class ViewHolder_search{
    ImageView user_img;
    TextView ride_details;
    androidx.appcompat.widget.AppCompatButton join_ride;
    ImageButton call_driver;
    ImageButton whatsapp;

}
