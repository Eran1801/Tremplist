package com.myapp.tremplist_update;

import androidx.annotation.NonNull;

public class User {

    private String first_name, last_name, phone, password, confirm_password;

    public User(String first_name, String last_name, String phone, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.password = password;
    }

    public User() {
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

    public String getConfirm_password() {
        return confirm_password;
    }

    public void setConfirm_password(String confirm_password) {
        this.confirm_password = confirm_password;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}