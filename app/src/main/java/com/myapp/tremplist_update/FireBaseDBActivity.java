package com.myapp.tremplist_update;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.Objects;

public class FireBaseDBActivity extends FirebaseBaseModel{
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    Context context;
    public void setContext(Context context){
        this.context=context;
    }

    public void addUserToDB(User user){
        myRef.child("users").child(user.id).setValue(user);
    }

    public void addRideToDB(Ride ride){
        // getting the UID of the user from the Authentication
        String UID= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        myRef.child("users").child(UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    // Adding the driver to the ride
                    User driver= Objects.requireNonNull(task.getResult()).getValue(User.class);
                    ride.setDriver(driver);
                    myRef.child("rides").push().setValue(ride).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Publish successfully" , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Publishing Error: "+ Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

        });

    }

    public void updateRideOnDB(Ride curr_ride) {

        String UID= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        myRef.child("users").child(UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                    // Adding the driver to the ride
                    User Trempist= Objects.requireNonNull(task.getResult()).getValue(User.class);
                    curr_ride.add_to_trempists(Trempist);
                    myRef.child("rides").child(curr_ride.getId()).setValue(curr_ride).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "you are successfully joined to the ride" , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Error: "+ Objects.requireNonNull(task.getException()).getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });

        }

}

