package com.myapp.tremplist_update;

import android.content.Context;
import android.provider.Contacts;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
        String UID= mAuth.getCurrentUser().getUid();
        myRef.child("users").child(UID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    User r= task.getResult().getValue(User.class);
                    ride.setDriver(r);
                    myRef.child("rides").push().setValue(ride).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Publish successfully" , Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Publishing Error: "+task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });                }
            }
        });

    }
}

