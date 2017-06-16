package com.smart.kitchen.smartkitchen.utils;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppUtils {
    private AppUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static String getAppName(Context context) {
        try {
            return context.getResources().getString(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.labelRes);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getPacketName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getVersionCode(Context context) {
        int i = 0;
        try {
            i = Integer.parseInt(String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public static int getFromMarket(Context context) {
        int i = 0;
        try {
            i = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData.getInt("DOWNLOAD_SOURCE", 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return i;
    }
}
