package com.smart.kitchen.smartkitchen.utils;

import java.math.BigDecimal;

public class ArithmeticUtils {
    public static int compare(String str, String str2) {
        return new BigDecimal(str).compareTo(new BigDecimal(str2));
    }

    public static String add(String str, String str2) {
        return new BigDecimal(str).add(new BigDecimal(str2)).toString();
    }

    public static double add(double d, double d2) {
        return new BigDecimal(Double.toString(d)).add(new BigDecimal(Double.toString(d2))).doubleValue();
    }

    public static String add(String str, String str2, int i) {
        if (i >= 0) {
            return new BigDecimal(str).add(new BigDecimal(str2)).setScale(i, 4).toString();
        }
        throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }

    public static BigDecimal sub(String str, String str2) {
        return new BigDecimal(str).subtract(new BigDecimal(str2));
    }

    public static String sub(String str, String str2, int i) {
        if (i >= 0) {
            return new BigDecimal(str).subtract(new BigDecimal(str2)).setScale(i, 4).toString();
        }
        throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }

    public static double mul(double d, double d2) {
        return new BigDecimal(Double.toString(d)).multiply(new BigDecimal(Double.toString(d2))).doubleValue();
    }

    public static BigDecimal mul(String str, String str2) {
        return new BigDecimal(str).multiply(new BigDecimal(str2));
    }

    public static String mul(String str, String str2, int i) {
        if (i >= 0) {
            return new BigDecimal(str).multiply(new BigDecimal(str2)).setScale(i, 4).toString();
        }
        throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
}
