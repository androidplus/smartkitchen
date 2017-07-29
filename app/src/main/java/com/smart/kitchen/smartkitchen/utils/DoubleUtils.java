package com.smart.kitchen.smartkitchen.utils;

public class DoubleUtils {
    public boolean pandun(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
