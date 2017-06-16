package com.smart.kitchen.smartkitchen.utils;

import android.content.Context;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import java.util.UUID;

public class DeviceUtils {
    public static String getDeviceId(Context context) {
        return SPUtils.getUserinfo(context, SPUtils.LOGIN_MMS);
    }

    public static String getstr(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        String userid = new UserInfoDaoManager().getNowUserInfo().getUserid();
        String timestamp = CalendarUtils.getTimestamp();
        String str = "" + Secure.getString(context.getContentResolver(), "android_id");
        UUID uuid = new UUID((long) str.hashCode(), (((long) userid.hashCode()) << 32) | ((long) timestamp.hashCode()));
        return userid + "" + timestamp + str;
    }
}
