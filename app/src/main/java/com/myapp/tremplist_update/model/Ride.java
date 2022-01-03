package com.myapp.tremplist_update.model;

// In this class we implement the Ride class

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ride {

    private String src_city;
    private String src_details;
    private String dst_city;
    private String dst_details;

    private Date date;
    private Hour hour;

    private String car_type;
    private String car_color;

    private int sits;
    private int free_sits;
    private int ride_cost;
    private String id;

    private User Driver;
    private Map<String , User> trempists; // will be tha list of passengers in the Ride
    private Map<String, User> user_rate;
    private Map<String, User> waiting_list;

    public Ride() {
        this.user_rate = new HashMap<>();
        this.waiting_list = new HashMap<>();
        this.user_rate = new HashMap<>();
    }

    public Ride(String src, String dst, Date date, Hour hour, int sits, int cost) {
        this.src_city = src;
        this.dst_city = dst;
        this.date = date;
        this.hour = hour;
        this.sits = sits;
        this.free_sits = sits;
        this.ride_cost = cost;
        this.src_details = "";
        this.dst_details = "";
        this.car_color = "";
        this.car_type = "";
        this.trempists=new HashMap<>();
        this.Driver=new User();
        this.user_rate = new HashMap<>();
        this.waiting_list = new HashMap<>();
    }

    public Ride(String src, String src_details, String dst, String dst_details, Date date, Hour hour, int sits, int cost, String car_color, String car_type) {
        this.src_city = src;
        this.dst_city = dst;
        this.date = date;
        this.hour = hour;
        this.sits = sits;
        this.free_sits = sits;
        this.ride_cost = cost;
        this.src_details = src_details;
        this.dst_details = dst_details;
        this.car_color = car_color;
        this.car_type = car_type;
        this.trempists=new HashMap<>();
        this.Driver=new User();
        this.user_rate = new HashMap<>();
        this.waiting_list = new HashMap<>();
    }

    public Ride(Ride other) {
        this.src_city = other.src_city;
        this.dst_city = other.dst_city;
        this.date = new Date(other.date);
        this.hour =new Hour(other.hour);
        this.sits = other.sits;
        this.free_sits = other.free_sits;
        this.ride_cost = other.ride_cost;
        this.src_details = other.src_details;
        this.dst_details = other.dst_details;
        this.car_color = other.car_color;
        this.car_type = other.car_type;
        this.id= other.id;
        if(other.Driver!= null)
            this.Driver=new User(other.Driver);
        else this.Driver=new User();
        if(other.trempists!=null)
            this.trempists = new HashMap<>(other.trempists);
        else this.trempists=new HashMap<>();
        if(other.user_rate != null)
            this.user_rate = new HashMap<>(other.user_rate);
        else this.user_rate = new HashMap<>();
        if(other.waiting_list != null)
            this.waiting_list = new HashMap<>(other.waiting_list);
        else this.waiting_list= new HashMap<>();
    }


    public String getSrc_city() {
        return src_city;
    }

    public void setSrc_city(String src_city) {
        this.src_city = src_city;
    }

    public String getSrc_details() {
        return src_details;
    }

    public void setSrc_details(String src_details) {
        this.src_details = src_details;
    }

    public String getDst_city() {
        return dst_city;
    }

    public void setDst_city(String dst_city) {
        this.dst_city = dst_city;
    }

    public String getDst_details() {
        return dst_details;
    }

    public void setDst_details(String dst_details) {
        this.dst_details = dst_details;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Hour getHour() {
        return hour;
    }

    public void setHour(Hour hour) {
        this.hour = hour;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getCar_color() {
        return car_color;
    }

    public void setCar_color(String car_color) {
        this.car_color = car_color;
    }

    public int getSits() {
        return sits;
    }

    public void setSits(int sits) {
        this.sits = sits;
    }

    public int getFree_sits() {
        return free_sits;
    }

    public void setFree_sits(int free_sits) {
        this.free_sits = free_sits;
    }

    public int getRide_cost() {
        return ride_cost;
    }

    public void setRide_cost(int ride_cost) {
        this.ride_cost = ride_cost;
    }

    public void setDriver(User r) {
        this.Driver = new User(r);
    }

    public User getDriver() {
        return Driver;
    }

    public Map<String,User> getTrempists(){
        return trempists;
    }

    public void add_to_trempists(User trempist){
        trempists.put(trempist.id, trempist);
    }

    public boolean add_to_waiting_list(User trempist){
        if(waiting_list.containsKey(trempist.getId()))
            return false;
        waiting_list.put(trempist.id, trempist);
        return true;
    }
    public void add_rated(User trempist){
        user_rate.put(trempist.id, trempist);
    }

    public void remove_from_Tremplists(User trempist) {
        trempists.remove(trempist.id);
    }
    public void remove_from_waitingList(User trempist) {
        waiting_list.remove(trempist.id);
    }

    public void setTrempists(Map<String, User> trempists){
        this.trempists=trempists;
    }

    public void setId(String id) {
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public Map<String, User> getUser_rate() {
        return user_rate;
    }

    public void setUser_rate(HashMap<String, User> user_rate) {
        this.user_rate = user_rate;
    }

    public Map<String, User> getWaiting_list() {
        return waiting_list;
    }

    public void setWaiting_list(Map<String, User> waiting_list) {
        this.waiting_list = waiting_list;
    }

    @Override
    public String toString() {
        return "Ride{" +
                "src_city='" + src_city + '\'' +
                ", src_details='" + src_details + '\'' +
                ", dst_city='" + dst_city + '\'' +
                ", dst_details='" + dst_details + '\'' +
                ", date=" + date +
                ", hour=" + hour +
                ", car_type='" + car_type + '\'' +
                ", car_color='" + car_color + '\'' +
                ", sits=" + sits +
                ", free_sits=" + free_sits +
                ", ride_cost=" + ride_cost +
                ", id='" + id + '\'' +
                ", Driver=" + Driver+
                ", trempists=" + trempists +
                '}';
    }



}