package com.smart.kitchen.smartkitchen.utils;

import android.util.Log;

public class LogUtils {
    private static boolean mDebug = false;

    private LogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void setDebug(boolean z) {
        mDebug = z;
    }

    public static void v(String str, String str2) {
        if (mDebug) {
            Log.v(str, str2);
        }
    }

    public static void d(String str, String str2) {
        if (mDebug) {
            Log.d(str, str2);
        }
    }

    public static void i(String str, String str2) {
        if (mDebug) {
            Log.i(str, str2);
        }
    }

    public static void w(String str, String str2) {
        if (mDebug) {
            Log.w(str, str2);
        }
    }

    public static void e(String str, String str2) {
        if (mDebug) {
            Log.e(str, str2);
        }
    }
}
