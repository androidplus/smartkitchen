package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import com.smart.kitchen.smartkitchen.entitys.MessageCenter;
import java.util.List;

public interface MainModel {
    List<FoodType> asyncJson(String str);

    List<FoodType> getFoodTypeFromDB();

    void getFoodTypeFromNET(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog);

    List<MessageCenter> getMessageCenterListNORead();

    List<MessageCenter> getMessageCenterListReaded();

    void saveFoodType(List<FoodType> list);
}
