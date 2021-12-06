package com.myapp.tremplist_update;

public class FireBaseDBUser extends FirebaseBaseModel {

    public void addUserToDB(String firstName,String lastName,String phone, String password){
        writeNewUser(firstName,lastName,phone, password);
    }
    public void writeNewUser(String firstName,String lastName ,String phone,String password){
        User user = new User(firstName,lastName,password,phone);
        myRef.child("users").child(user.getFirst_name()).setValue(user);
    }
}
