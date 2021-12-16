package com.myapp.tremplist_update;

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

    public MyListAdapter_forDriver(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        layout=resource;
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
                    Toast.makeText(getContext(), position+" clicked", Toast.LENGTH_SHORT).show();

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
