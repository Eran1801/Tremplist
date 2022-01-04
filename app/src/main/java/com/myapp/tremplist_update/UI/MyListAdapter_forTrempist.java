package com.myapp.tremplist_update.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
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
    private TextView tv;
    static Dialog d;

    public MyListAdapter_forTrempist(@NonNull Context context, int resource, @NonNull List<String> objects, List<Ride> rides, Context ApplicationContext, Activity activity) {
        super(context, resource, objects);
        layout = resource;
        this.rides = rides;
        this.ApplicationContext = ApplicationContext;
        this.activity = activity;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        mAuth = FirebaseAuth.getInstance();
        String UID = mAuth.getCurrentUser().getUid();

        ViewHolder_trempist mainViewHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, null, false);
            ViewHolder_trempist viewHolder = new ViewHolder_trempist();
            viewHolder.user_img = (ImageView) convertView.findViewById(R.id.user_im_trempist);
            viewHolder.ride_details = (TextView) convertView.findViewById(R.id.ride_details_trempist);
            viewHolder.call = (ImageButton) convertView.findViewById(R.id.call_Mydriver);
            viewHolder.whatsapp = (ImageButton) convertView.findViewById(R.id.whatsapp_MyDriver);

            viewHolder.cancel_ride = (androidx.appcompat.widget.AppCompatButton) convertView.findViewById(R.id.cancel_ride_trempist);
            viewHolder.cancel_ride.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO NOT SHOULD DELETE FROM TREMPISTS?
                    Ride curr_ride = new Ride(rides.get(position));
                    int free_sits = curr_ride.getFree_sits();
                    curr_ride.setFree_sits(free_sits + 1);
                    fb = new FireBaseDBActivity();
                    fb.setContext(getContext());
                    fb.setApplicationContext(ApplicationContext);
                    fb.setActivity(activity);
                    fb.updateRideOnDB_Cancel(curr_ride);
                }
            });

            // If user already rate this ride, don't show this button
            Ride curr_ride = new Ride(rides.get(position));


            viewHolder.rate_ride = (androidx.appcompat.widget.AppCompatButton) convertView.findViewById(R.id.rate_ride);
            if(curr_ride.getUser_rate().containsKey(UID))
                viewHolder.rate_ride.setVisibility(View.GONE);
            viewHolder.rate_ride.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog d = new Dialog(getContext());
                            d.setContentView(R.layout.rate_dialog);
                            d.setTitle("דרג נהג");
                            Button cancelBtn = (Button) d.findViewById(R.id.cancel_rateBtn);
                            Button sendBtn = (Button) d.findViewById(R.id.send_rateBtn);
                            final NumberPicker np = (NumberPicker) d.findViewById(R.id.ratePicker);
                            np.setMaxValue(5); // max value 5
                            np.setMinValue(1);   // min value 1
                            np.setWrapSelectorWheel(false);

                            // Send Btn
                            sendBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println(String.valueOf(np.getValue()));
                                    Ride curr_ride = new Ride(rides.get(position));
                                    fb = new FireBaseDBActivity();
                                    fb.setContext(getContext());
                                    fb.setApplicationContext(ApplicationContext);
                                    fb.setActivity(activity);
                                    fb.updateRate(curr_ride, np.getValue(), UID);
                                    d.dismiss();
                                    viewHolder.rate_ride.setVisibility(View.GONE);
                                }
                            });
                            //Cancel Btn
                            cancelBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    d.dismiss(); // dismiss the dialog
                                }
                            });
                            d.show();
                        }
                    }
            );
            viewHolder.call.setOnClickListener(new View.OnClickListener() {
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

        mainViewHolder = (ViewHolder_trempist) convertView.getTag();
        mainViewHolder.ride_details.setText(getItem(position));
        return convertView;
    }

    class ViewHolder_trempist {
        ImageView user_img;
        TextView ride_details;
        androidx.appcompat.widget.AppCompatButton cancel_ride;
        androidx.appcompat.widget.AppCompatButton rate_ride;
        ImageButton whatsapp;
        ImageButton call;


    }
}
