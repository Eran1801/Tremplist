package com.myapp.tremplist_update.model;

import androidx.annotation.NonNull;

public class User {
    private String first_name, last_name, phone, password, email;
    private int sum_rate, count_rate;
    public String id;



    public User() {
    }

    public User(User other) {
        this.first_name = other.first_name;
        this.last_name = other.last_name;
        this.phone = other.phone;
        this.password = other.password;
        this.email = other.email;
        this.id = other.id;
        this.sum_rate = other.sum_rate;
        this.count_rate = other.count_rate;
    }

//    public User(String id, String first_name, String last_name, String phone, String email, int count_rate, int sum_rate) {
////    public User(String id, String first_name, String last_name, String phone, String email) {
//        this.id = id;
//        this.first_name = first_name;
//        this.last_name = last_name;
//        this.phone = phone;
//        this.email = email;
//        this.sum_rate = sum_rate;
//        this.count_rate = count_rate;
//
//    }

    public User(String id, String first_name, String last_name, String phone, String email) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
    }

    public int getSum_rate() {
        return sum_rate;
    }

    public void setSum_rate(int sum_rate) {
        this.sum_rate = sum_rate;
    }

    public int getCount_rate() {
        return count_rate;
    }

    public void setCount_rate(int count_rate) {
        this.count_rate = count_rate;
    }

    // for update details
    public static User create_user_for_personal_info(String id, String first_name, String last_name, String phone) {
//    public static User create_user_for_personal_info(String id, String first_name, String last_name, String phone) {
        User user = new User();
        user.id = id;
        user.first_name = first_name;
        user.last_name = last_name;
        user.phone = phone;
        return user;
    }

    public User(String first_name, String last_name, String phone, String email, int sum_rate, int count_rate ) {
//    public User(String first_name, String last_name, String phone, String email) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.email = email;
        this.sum_rate = sum_rate;
        this.count_rate = count_rate;

    }

    public String getId() {
        return id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}