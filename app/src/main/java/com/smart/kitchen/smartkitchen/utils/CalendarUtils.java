package com.smart.kitchen.smartkitchen.utils;

import com.smart.kitchen.smartkitchen.entitys.MyDate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarUtils {
    private static final String TAG = "CalendarUtils";

    public static List<MyDate> getMonthDays(int i, int i2) {
        int nowMonth;
        int nowYear = i == 0 ? getNowYear() : i;
        if (i2 == 0) {
            nowMonth = getNowMonth();
        } else {
            nowMonth = i2;
        }
        Calendar instance = Calendar.getInstance();
        instance.clear();
        instance.set(1, nowYear);
        instance.set(2, nowMonth - 1);
        int actualMaximum = instance.getActualMaximum(5);
        List<MyDate> arrayList = new ArrayList();
        for (int i3 = 0; i3 < actualMaximum; i3++) {
            arrayList.add(new MyDate(nowYear, nowMonth, i3 + 1, getMonthoneWeek(i, i2, i3 + 1), ""));
        }
        return arrayList;
    }

    public static int getMonthoneWeek(int i, int i2, int i3) {
        if (i == 0) {
            i = getNowYear();
        }
        if (i2 == 0) {
            i2 = getNowMonth();
        }
        if (i3 == 0) {
            i3 = 1;
        }
        Calendar instance = Calendar.getInstance();
        instance.clear();
        instance.set(1, i);
        instance.set(2, i2 - 1);
        instance.set(5, i3);
        return instance.get(7) - 1;
    }

    public static int getNowYear() {
        return Calendar.getInstance().get(1);
    }

    public static int getNowMonth() {
        return Calendar.getInstance().get(2) + 1;
    }

    public static int getNowDate() {
        return Calendar.getInstance().get(5);
    }

    public static int getNowHour() {
        return Calendar.getInstance().get(11);
    }

    public static int getNowMinute() {
        return Calendar.getInstance().get(12);
    }

    public static int getNowWeek() {
        return Calendar.getInstance().get(7) - 1;
    }

    public static String getNowFullMonth() {
        return getNowYear() + "-" + numToTwo(getNowMonth());
    }

    public static String getNowFullDate() {
        return getNowYear() + "-" + numToTwo(getNowMonth()) + "-" + numToTwo(getNowDate());
    }

    public static String getNowFullTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String getNowFullTime2() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a").format(new Date());
    }

    public static void tostring() {
        LogUtils.e(TAG, "tostring: " + getNowFullTime());
    }

    public static String numToTwo(int i) {
        String str = "" + i;
        if (i < 10) {
            return "0" + i;
        }
        return str;
    }

    public static String strWeek(int i) {
        String str = "";
        switch (i) {
            case 0:
                return "周日";
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
            default:
                return str;
        }
    }

    public static String strWeek2(int i) {
        String str = "";
        switch (i) {
            case 0:
                return "日";
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            default:
                return str;
        }
    }

    public static long getNowMms() {
        return new Date().getTime();
    }

    public static List<Integer> getYearBeforeAndAfter() {
        int nowYear = getNowYear();
        nowYear += 3;
        List<Integer> arrayList = new ArrayList();
        for (int i = nowYear - 3; i <= nowYear; i++) {
            arrayList.add(Integer.valueOf(i));
        }
        return arrayList;
    }

    public static String getTimestamp() {
        return String.valueOf(System.currentTimeMillis());
    }
}
