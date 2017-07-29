package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import java.util.List;

public interface OrderLeftModel {
    List<OrderInfo> asyncJson(String str);

    List<OrderInfo> getFromDB();

    void getFromNet(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2, int i2);

    void saveOrder(List<OrderInfo> list);
}
