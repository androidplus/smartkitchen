package com.smart.kitchen.smartkitchen.utils;

import android.util.Log;

public class LogUtils {
    private static boolean mDebug = false;

    private LogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void setDebug(boolean isDebug) {
        mDebug = isDebug;
    }

    public static void v(String tag, String msg) {
        if (mDebug) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (mDebug) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (mDebug) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (mDebug) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (mDebug) {
            Log.e(tag, msg);
        }
    }
}
