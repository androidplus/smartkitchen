package com.smart.kitchen.smartkitchen.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SPUtils {
    public static final String GUANDAN_PRINT_NUM = "guandanPrintNum";
    public static final String ID_JIAOJIE = "id_jiaojie";
    public static final String IS_LOGIN = "login";
    public static final String LOGIN_MMS = "login_mms";
    public static final String NEED_JIAOJIE = "need_jiaojie";
    public static final String ORDERNUMBER = "orderNumber";
    public static final String PRINT_DELAY_TIME = "printDelayTime";
    public static final String PRINT_FONT = "printFont";
    public static final String PRINT_KITCHEN_IP = "kitchenip";
    public static final String PRINT_KITCHEN_PORT = "kitchenport";
    public static final String PRINT_NUM = "printNum";
    public static final String SETTING_AREA = "areaSetting";
    public static final String SETTING_NEW_ORDER_REFRESH_TIME = "settingNewOrderRefreshTime";
    public static final String SETTING_NOTIFICATIONAUTOPAY = "NotificationAutoPay";
    public static final String SETTING_NOTIFICATIONCANCEL = "NotificationCancel";
    public static final String SETTING_NOTIFICATIONCOUNT = "NotificationCount";
    public static final String SETTING_NOTIFICATIONHAVEORDER = "NotificationHaveOrder";
    public static final String SETTING_NOTIFICATIONPAY = "NotificationPay";
    public static final String SETTING_NOTIFICATIONWARRING = "NotificationWarring";
    public static final String SETTING_PAYMENTSUCCEED = "paymentsucceed";
    public static final String SETTING_PRINT_KITCHEN = "printKitchen";
    public static final String SETTING_PRINT_KITCHENSTYLE = "printKitchenStyle";
    public static final String SETTING_PRINT_LOGO = "printLogo";
    public static final String SETTING_PRINT_MERGE = "printMerge";
    public static final String SETTING_PRINT_PAPERSTYLE = "printPaperStyle";
    public static final String SETTING_SELECT_LOGO = "settingSelectLogo";
    public static final String SETTING_TAKEOUTNEW = "takeoutnew";
    public static final String STORE_ID = "store_id";
    public static final String STORE_INFO = "store_info";
    private static final String TAG = "SPUtils";
    public static final String TAKEOUT_RECEIVING = "takeoutreceiving";
    public static final String USER_INFO = "userInfo";
    public static final String USER_NAME = "user_name";

    public static boolean isLogin(Context context) {
        return context.getSharedPreferences(USER_INFO, 0).getBoolean(IS_LOGIN, false);
    }

    public static boolean isNeedJiaojie(Context context) {
        return context.getSharedPreferences(USER_INFO, 0).getBoolean(NEED_JIAOJIE, false);
    }

    public static String getUserinfo(Context context, String str) {
        return context.getSharedPreferences(USER_INFO, 0).getString(str, null);
    }

    public static int getUserinfos(Context context, String str) {
        return context.getSharedPreferences(USER_INFO, 0).getInt(str, -1);
    }

    public static boolean getUserinfos2(Context context, String str) {
        return context.getSharedPreferences(USER_INFO, 0).getBoolean(str, false);
    }

    public static void setUserinfo(Context context, String str, Object obj) {
        Editor edit = context.getSharedPreferences(USER_INFO, 0).edit();
        if (obj instanceof String) {
            edit.putString(str, (String) obj);
        } else if (obj instanceof Integer) {
            edit.putInt(str, ((Integer) obj).intValue());
        } else if (obj instanceof Long) {
            edit.putLong(str, ((Long) obj).longValue());
        } else if (obj instanceof Boolean) {
            edit.putBoolean(str, ((Boolean) obj).booleanValue());
        }
        edit.commit();
    }

    public static void remove(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO, 0);
        Editor edit = sharedPreferences.edit();
        if (sharedPreferences.getString(str, null) != null) {
            edit.remove(str);
            edit.commit();
        }
    }

    public static void clear(Context context) {
        Editor edit = context.getSharedPreferences(USER_INFO, 0).edit();
        edit.clear();
        edit.commit();
    }
}
