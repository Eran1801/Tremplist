package com.myapp.tremplist_update;

public class Date implements Comparable<Date> {
    private int day;
    private int month;
    private int year;

    public Date(){}
    public Date(int d, int m, int y){
        this.day = d;
        this.month = m;
        this.year = y;
    }

    public Date(com.myapp.tremplist_update.Date other) {
        this.day = other.day;
        this.month = other.month;
        this.year = other.year;
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
    

    @Override
    public int compareTo(Date d2) {
        int day1 = this.getDay();
        int day2 = d2.getDay();
        int month1 = this.getMonth();
        int month2= d2.getMonth();
        int year1=this.getYear();
        int year2=d2.getYear();

        if(year1>year2 || (year1==year2 && month1>month2) || (year1==year2 && month1==month2 && day1>day2))
            return 1;
        else if (year2>year1 || (year2==year1 && month2>month1) || (year2==year1 && month2==month1 && day2>day1))
            return -1;
        else
            return 0;
        
    }

}
