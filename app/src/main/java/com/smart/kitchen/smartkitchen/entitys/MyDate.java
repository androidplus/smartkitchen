package com.smart.kitchen.smartkitchen.entitys;

import java.io.Serializable;

public class MyDate implements Serializable {
    private int color;
    private int date;
    private int month;
    private String time;
    private String title;
    private int week;
    private int year;

    public MyDate() {
    }

    public MyDate(int i, int i2, int i3, int i4, String str) {
        this.year = i;
        this.month = i2;
        this.date = i3;
        this.week = i4;
        this.time = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int i) {
        this.color = i;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int i) {
        this.year = i;
    }

    public int getMonth() {
        return this.month;
    }

    public void setMonth(int i) {
        this.month = i;
    }

    public int getDate() {
        return this.date;
    }

    public void setDate(int i) {
        this.date = i;
    }

    public int getWeek() {
        return this.week;
    }

    public void setWeek(int i) {
        this.week = i;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String str) {
        this.time = str;
    }

    public String toString() {
        return "MyDate{year=" + this.year + ", month=" + this.month + ", date=" + this.date + ", week=" + this.week + ", time='" + this.time + '\'' + '}';
    }
}
