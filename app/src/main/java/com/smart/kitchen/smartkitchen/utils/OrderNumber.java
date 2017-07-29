package com.smart.kitchen.smartkitchen.utils;

import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderNumber {
    private static OrderNumber instance = null;
    private int i = 1;

    public static synchronized OrderNumber getInstance() {
        OrderNumber orderNumber;
        synchronized (OrderNumber.class) {
            if (instance == null) {
                instance = new OrderNumber();
            }
            orderNumber = instance;
        }
        return orderNumber;
    }

    private String getStoreID(Context context) {
        String userinfo = SPUtils.getUserinfo(context, SPUtils.STORE_ID);
        if (userinfo.length() >= 5) {
            return userinfo;
        }
        return String.format("%05d", new Object[]{Integer.valueOf(userinfo)});
    }

    private String getTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    public String getOrderNumber(Context context) {
        int userinfos = SPUtils.getUserinfos(context, SPUtils.ORDERNUMBER);
        if (userinfos == -1) {
            this.i = 1;
        } else if (userinfos == 9999) {
            this.i = 1;
        } else {
            this.i = userinfos;
        }
        String str = getStoreID(context) + getTime() + String.format("%04d", new Object[]{Integer.valueOf(this.i)});
        SPUtils.setUserinfo(context, SPUtils.ORDERNUMBER, Integer.valueOf(this.i + 1));
        return str;
    }
}
