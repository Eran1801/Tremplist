package com.myapp.tremplist_update;

public class FireBaseDBActivity extends FirebaseBaseModel {

    public void addUserToDB(User user){
        myRef.child("users").child(user.getFirst_name()).setValue(user);
    }
    public void addRideToDB(Ride ride){
        myRef.child("rides").child(ride.getId()+"").setValue(ride);
    }
}
