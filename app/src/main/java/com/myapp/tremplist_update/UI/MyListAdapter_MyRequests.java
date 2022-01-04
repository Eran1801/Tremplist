package com.myapp.tremplist_update.UI;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.myapp.tremplist_update.R;
import com.myapp.tremplist_update.fireBase.FireBaseDBActivity;
import com.myapp.tremplist_update.model.Ride;

import java.util.List;

public class MyListAdapter_MyRequests extends ArrayAdapter<String> {
    FireBaseDBActivity fb;
    private int layout;
    private int curr_position;
    List<Ride> rides;
    Context ApplicationContext;
    Activity activity;
    FirebaseAuth mAuth;

    public MyListAdapter_MyRequests(@NonNull Context context, int resource, @NonNull List<String> objects, List<Ride> rides, Context ApplicationContext, Activity activity) {
        super(context, resource, objects);
        layout=resource;
        this.rides=rides;
        this.ApplicationContext=ApplicationContext;
        this.activity=activity;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();

        ViewHolder_request mainViewHolder=null;
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(layout, null, false);
            ViewHolder_request viewHolder=new ViewHolder_request();
            viewHolder.user_img=(ImageView) convertView.findViewById(R.id.user_im_request);
            viewHolder.ride_details=(TextView) convertView.findViewById(R.id.ride_details_request);
            viewHolder.cancel_request=(androidx.appcompat.widget.AppCompatButton) convertView.findViewById(R.id.cancel_request);
            viewHolder.cancel_request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO NOT SHOULD DELETE FROM TREMPISTS?
                    Ride curr_ride= new Ride(rides.get(position));
                    fb = new FireBaseDBActivity();
                    fb.setContext(getContext());
                    fb.setApplicationContext(ApplicationContext);
                    fb.setActivity(activity);
                    fb.updateRideOnDB_Cancel_request(curr_ride);
                }
            });
            convertView.setTag(viewHolder);
        }

        mainViewHolder=(ViewHolder_request) convertView.getTag();
        mainViewHolder.ride_details.setText(getItem(position));


        return convertView;
    }

}

class ViewHolder_request{
    ImageView user_img;
    TextView ride_details;
    androidx.appcompat.widget.AppCompatButton cancel_request;

}
