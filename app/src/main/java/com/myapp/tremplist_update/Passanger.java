package com.myapp.tremplist_update;

import java.util.HashMap;

public class Passanger {

    private String first_name;
    private String last_name;
    private String phone_number;
    private String email;
    private String password;
    private HashMap<Integer, Ride> ride_map;

    public Passanger(String f_name, String l_name, String pho_number, String email, String pass) {
        this.first_name = f_name;
        this.last_name = l_name;
        this.phone_number = pho_number;
        this.email = email;
        this.password = pass;
        this.ride_map = null;
    }

    public HashMap<Integer, Ride> search_ride(String src_city, String dest_city, Date date, Hour hour) {
        return null;
    }

    public String join_ride(int ride_id) { // It will be with a push of a button. send an alert to the driver
        return "The ride is waiting for approval of the driver";
    }

    public String cancel_ride(int ride_id) {
        return "Ride was cancel";
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HashMap<Integer, Ride> getRide_map() {
        return ride_map;
    }

    public void setRide_map(HashMap<Integer, Ride> ride_map) {
        this.ride_map = ride_map;
    }
}
