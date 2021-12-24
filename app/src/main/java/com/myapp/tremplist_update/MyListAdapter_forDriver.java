package com.myapp.tremplist_update;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MyListAdapter_forDriver extends ArrayAdapter<String> {
    private int layout;
    Context ApplicationContext;
    Activity activity;
    List<Ride> rides;
    FireBaseDBActivity fb;



    public MyListAdapter_forDriver(@NonNull Context context, int resource, @NonNull List<String> objects, List<Ride> rides,  Context ApplicationContext, Activity activity) {
        super(context, resource, objects);
        layout=resource;
        this.ApplicationContext=ApplicationContext;
        this.activity=activity;
        this.rides=rides;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder_driver mainViewHolder=null;
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(layout, null, false);
            ViewHolder_driver viewHolder=new ViewHolder_driver();
            viewHolder.ride_details_driver=(TextView) convertView.findViewById(R.id.ride_details_driver);
            viewHolder.cancel_ride=(androidx.appcompat.widget.AppCompatButton) convertView.findViewById(R.id.cancel_ride);
            viewHolder.cancel_ride.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ride curr_ride= new Ride(rides.get(position));
                    fb = new FireBaseDBActivity();
                    fb.setContext(getContext());
                    fb.setApplicationContext(ApplicationContext);
                    fb.setActivity(activity);
                    fb.Cancel_by_Driver(curr_ride);
                }
            });
            convertView.setTag(viewHolder);
        }

        mainViewHolder=(ViewHolder_driver) convertView.getTag();
        mainViewHolder.ride_details_driver.setText(getItem(position));


        return convertView;
    }
}

class ViewHolder_driver{
    TextView ride_details_driver;
    androidx.appcompat.widget.AppCompatButton cancel_ride;


}
