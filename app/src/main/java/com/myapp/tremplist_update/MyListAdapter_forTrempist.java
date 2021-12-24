package com.myapp.tremplist_update;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter_forTrempist extends ArrayAdapter<String> {
    FireBaseDBActivity fb;
    private int layout;
    private int curr_position;
    List<Ride> rides;
    Context ApplicationContext;
    Activity activity;

    public MyListAdapter_forTrempist(@NonNull Context context, int resource, @NonNull List<String> objects, List<Ride> rides, Context ApplicationContext, Activity activity) {
        super(context, resource, objects);
        layout=resource;
        this.rides=rides;
        this.ApplicationContext=ApplicationContext;
        this.activity=activity;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder_trempist mainViewHolder=null;
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(layout, null, false);
            ViewHolder_trempist viewHolder=new ViewHolder_trempist();
            viewHolder.user_img=(ImageView) convertView.findViewById(R.id.user_im_trempist);
            viewHolder.ride_details=(TextView) convertView.findViewById(R.id.ride_details_trempist);
            viewHolder.cancel_ride=(androidx.appcompat.widget.AppCompatButton) convertView.findViewById(R.id.cancel_ride_trempist);
            viewHolder.cancel_ride.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ride curr_ride= new Ride(rides.get(position));
                    int free_sits = curr_ride.getFree_sits();
                    curr_ride.setFree_sits(free_sits+1);
                    fb = new FireBaseDBActivity();
                    fb.setContext(getContext());
                    fb.setApplicationContext(ApplicationContext);
                    fb.setActivity(activity);
                    fb.updateRideOnDB_Cancel(curr_ride);
                }
            });
            convertView.setTag(viewHolder);
        }

        mainViewHolder=(ViewHolder_trempist) convertView.getTag();
        mainViewHolder.ride_details.setText(getItem(position));


        return convertView;
    }

}

class ViewHolder_trempist{
    ImageView user_img;
    TextView ride_details;
    androidx.appcompat.widget.AppCompatButton cancel_ride;


}
