package com.myapp.tremplist_update;

public class Date {
    private int day;
    private int month;
    private int year;

    public Date(int d, int m, int y){
        this.day = d;
        this.month = m;
        this.year = y;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
