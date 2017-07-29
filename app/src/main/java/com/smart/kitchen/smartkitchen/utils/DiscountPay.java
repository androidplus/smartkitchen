package com.smart.kitchen.smartkitchen.utils;

public class DiscountPay {
    public static String getZhekou(String str, String str2) {
        return ArithmeticUtils.mul(str, String.valueOf(Double.valueOf(str2).doubleValue() / 10.0d), 2);
    }

    public static String getFavorable(String str) {
        return "全场只要五块九";
    }

    public static String getFavorableMoney(String str, String str2) {
        return "999";
    }

    public static String getVipFavorable(String str, String str2) {
        return "1";
    }
}
