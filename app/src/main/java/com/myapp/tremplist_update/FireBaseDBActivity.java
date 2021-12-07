package com.myapp.tremplist_update;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

class Post {

    public String author;
    public String title;

    public Post(String author, String title) {
        this.author=author;
        this.title=title;
    }

    @Override
    public String toString() {
        return "Post{" +
                "author='" + author + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}


public class FireBaseDBActivity extends FirebaseBaseModel {
    boolean flag=false;


    public void addUserToDB(User user){
        myRef.child("users").child(user.getFirst_name()).setValue(user);
    }

    public void addRideToDB(Ride ride){
        myRef.child("rides").child(ride.getId()+"").setValue(ride);
//
//
//        // Attach a listener to read the data at our posts reference
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Post post = dataSnapshot.getValue(Post.class);
//                System.out.println(post);
//                Log.d("999999999999", "999999999999");
//                Log.d("YESS", "44444");
//                flag=true;
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                System.out.println("The read failed: " + databaseError.getCode());
//                Log.d("NOT", "333333");
//                flag=false;
//            }
//        });
//
//        return flag;
    }
}

