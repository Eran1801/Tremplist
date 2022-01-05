package com.myapp.tremplist_update.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.myapp.tremplist_update.R;
import com.myapp.tremplist_update.fireBase.FireBaseDBActivity;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter_MyPassengers extends ArrayAdapter<String> {
    FireBaseDBActivity fb;
    private int layout;
    ArrayList<String > passengers_phones ;
    Context ApplicationContext;
    Activity activity;

    public MyListAdapter_MyPassengers(@NonNull Context context, int resource, @NonNull List<String> objects, ArrayList<String> passengers_phones, Context ApplicationContext, Activity activity) {
        super(context, resource, objects);
        layout=resource;
        this.passengers_phones=passengers_phones;
        this.ApplicationContext=ApplicationContext;
        this.activity=activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder_MyPassengers mainViewHolder=null;
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(layout, null, false);
            ViewHolder_MyPassengers viewHolder=new ViewHolder_MyPassengers();
            viewHolder.user_img=(ImageView) convertView.findViewById(R.id.user_im_Mytrempist);
            viewHolder.Passenger_details=(TextView) convertView.findViewById(R.id.my_passanger_details);
            viewHolder.call_passenger=(ImageButton) convertView.findViewById(R.id.call_MyPassenger);
            viewHolder.whatsapp_passenger=(ImageButton) convertView.findViewById(R.id.whatsapp_MyPassenger);


            viewHolder.call_passenger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone_number = "tel:"+passengers_phones.get(position);
                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(phone_number));
                    getContext().startActivity(intent);
                }
            });

            viewHolder.whatsapp_passenger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone_number =passengers_phones.get(position);
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

        mainViewHolder=(ViewHolder_MyPassengers) convertView.getTag();
        mainViewHolder.Passenger_details.setText(getItem(position));


        return convertView;
    }

}



class ViewHolder_MyPassengers{
    ImageView user_img;
    TextView Passenger_details;
    ImageButton call_passenger;
    ImageButton whatsapp_passenger;
}
