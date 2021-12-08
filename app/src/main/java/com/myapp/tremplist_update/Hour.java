package com.myapp.tremplist_update;

import java.util.Comparator;

public class Hour implements Comparable<Hour> {
    private int hour;
    private int minute;

    public Hour(){}

    public Hour(int h, int m){
        this.minute = m;
        this.hour = h;
    }

    public Hour(Hour other) {
        this.hour=other.hour;
        this.minute=other.minute;
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

    @Override
    public int compareTo(Hour h2) {
        int hour1=this.getHour();
        int hour2=h2.getHour();
        int min1=this.getMinute();
        int min2=h2.getMinute();

        if(hour1>hour2 || (hour1==hour2 && min1>min2))
            return 1;
        else if (hour1==hour2 && min1==min2)
            return 0;
        else
            return -1;
    }
}

