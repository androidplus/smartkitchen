package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import java.util.List;

public interface BreakageModel {
    List<FoodType> asyncJson(String str);

    List<FoodType> getFoodTypeFromDB();

    void getFoodTypeFromNET(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog);

    void saveFoodType(List<FoodType> list);
}
