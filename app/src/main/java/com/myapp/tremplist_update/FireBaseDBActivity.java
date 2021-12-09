package com.myapp.tremplist_update;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class FireBaseDBActivity extends FirebaseBaseModel{
    Context context;
    public void setContext(Context context){
        this.context=context;
    }

    public void addUserToDB(User user){
        // The push function generate a unique id foe each user
        myRef.child("users").push().setValue(user);
    }

    public void addRideToDB(Ride ride){
        // The push function generate a unique id foe each ride
        myRef.child("rides").push().setValue(ride).addOnCompleteListener(new OnCompleteListener<Void>() {
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

