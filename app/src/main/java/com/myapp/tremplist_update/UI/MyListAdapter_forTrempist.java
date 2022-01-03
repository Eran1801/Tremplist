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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.myapp.tremplist_update.fireBase.FireBaseDBActivity;
import com.myapp.tremplist_update.R;
import com.myapp.tremplist_update.model.Ride;
import com.myapp.tremplist_update.model.User;

import java.util.List;

public class MyListAdapter_forTrempist extends ArrayAdapter<String> {
    FireBaseDBActivity fb;
    private int layout;
    private int curr_position;
    List<Ride> rides;
    Context ApplicationContext;
    Activity activity;
    FirebaseAuth mAuth;

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
        mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();

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
                    //TODO NOT SHOULD DELETE FROM TREMPISTS?
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
            Spinner dropdown = (Spinner) convertView.findViewById(R.id.spinner1);
            String[] items = new String[]{"בחר דירוג","1", "2", "3","4","5"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
            dropdown.setAdapter(adapter);
            dropdown.setPrompt("דירוג");
            dropdown.setSelection(0);
            viewHolder.rate_ride=(androidx.appcompat.widget.AppCompatButton) convertView.findViewById(R.id.rate_ride);
            Ride curr_ride = new Ride(rides.get(position));
            if(curr_ride.getUser_rate().containsKey(UID)){
                viewHolder.rate_ride.setVisibility(View.GONE);
                dropdown.setVisibility(View.GONE);
            }
            viewHolder.rate_ride.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dropdown.getSelectedItemPosition() > 0) {
                        Ride curr_ride = new Ride(rides.get(position));
                        fb = new FireBaseDBActivity();
                        fb.setContext(getContext());
                        fb.setApplicationContext(ApplicationContext);
                        fb.setActivity(activity);
                        fb.updateRate(curr_ride,Integer.parseInt(dropdown.getSelectedItem().toString()),UID);
                        dropdown.setVisibility(View.GONE);
                        viewHolder.rate_ride.setVisibility(View.GONE);
                    }
                    //TODO ADD MESSAGE THAT TELL TO CHOOSE RATE
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
    androidx.appcompat.widget.AppCompatButton rate_ride;

}
