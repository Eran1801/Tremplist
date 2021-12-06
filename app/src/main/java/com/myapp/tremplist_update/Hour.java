package com.myapp.tremplist_update;

public class Hour {
    private int hour;
    private int minute;

    public Hour(int m, int h){
        this.minute = m;
        this.hour = h;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}

