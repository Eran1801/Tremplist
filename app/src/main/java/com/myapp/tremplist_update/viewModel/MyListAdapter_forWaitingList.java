package com.myapp.tremplist_update.viewModel;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.myapp.tremplist_update.R;
import com.myapp.tremplist_update.fireBase.FireBaseDBActivity;
import com.myapp.tremplist_update.model.Ride;
import com.myapp.tremplist_update.model.User;

import java.util.List;

public class MyListAdapter_forWaitingList extends ArrayAdapter<String> {
    private int layout;
    Context ApplicationContext;
    Activity activity;
    List<Ride> rides;
    List<User> trempists;
    FireBaseDBActivity fb;



    public MyListAdapter_forWaitingList(@NonNull Context context, int resource, @NonNull List<String> objects, List<Ride> rides, List<User> trempists, Context ApplicationContext, Activity activity) {
        super(context, resource, objects);
        layout=resource;
        this.ApplicationContext=ApplicationContext;
        this.activity=activity;
        this.rides=rides;
        this.trempists=trempists;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder_waitingList mainViewHolder=null;
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(layout, null, false);
            ViewHolder_waitingList viewHolder=new ViewHolder_waitingList();
            viewHolder.ride_details_waiting_list=(TextView) convertView.findViewById(R.id.ride_details_waitingList);
            viewHolder.reject=(androidx.appcompat.widget.AppCompatButton) convertView.findViewById(R.id.reject);
            viewHolder.approve =(androidx.appcompat.widget.AppCompatButton) convertView.findViewById(R.id.approve);

            viewHolder.approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ride curr_ride= new Ride(rides.get(position));
                    User curr_trempist= new User(trempists.get(position));
                    int free_sits = curr_ride.getFree_sits();
                    if(free_sits>0) {
                        curr_ride.setFree_sits(free_sits - 1);
                        fb = new FireBaseDBActivity();
                        fb.setContext(getContext());
                        fb.setApplicationContext(ApplicationContext);
                        fb.setActivity(activity);
                        fb.approve_joining(curr_ride, curr_trempist);
                    }
                    else
                        Toast.makeText(getContext(),"לא נשאר לך מקום כדי לאשר עוד נסיעות", Toast.LENGTH_SHORT).show();
                }
            });


            viewHolder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Ride curr_ride= new Ride(rides.get(position));
                    User curr_trempist= new User(trempists.get(position));
                    fb = new FireBaseDBActivity();
                    fb.setContext(getContext());
                    fb.setApplicationContext(ApplicationContext);
                    fb.setActivity(activity);
                    fb.reject_joining(curr_ride, curr_trempist);
                }
            });

            convertView.setTag(viewHolder);
        }

        mainViewHolder=(ViewHolder_waitingList) convertView.getTag();
        mainViewHolder.ride_details_waiting_list.setText(getItem(position));


        return convertView;
    }
}

class ViewHolder_waitingList{
    TextView ride_details_waiting_list;
    androidx.appcompat.widget.AppCompatButton approve;
    androidx.appcompat.widget.AppCompatButton reject;

}
