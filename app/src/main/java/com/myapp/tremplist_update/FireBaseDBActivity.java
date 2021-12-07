package com.myapp.tremplist_update;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class FireBaseDBActivity extends FirebaseBaseModel{
    Context context;
    public void setContext(Context context){
        this.context=context;
    }

    public void addUserToDB(User user){
        myRef.child("users").child(user.getFirst_name()).setValue(user);
    }

    public void addRideToDB(Ride ride){
//        myRef.child("rides").child(ride.getId()+"").setValue(ride);
        myRef.child("rides").child(ride.getId()+"").setValue(ride).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Do what you need to do

                    Toast.makeText(context, "Publish successfully" , Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "Publishing Error: "+task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

