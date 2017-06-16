package com.smart.kitchen.smartkitchen.mvp.model;

import android.content.Context;
import com.smart.kitchen.smartkitchen.entitys.VipInfo;

public interface SearchModel {
    VipInfo asyncJson(String str);

    void search(int i, Context context, String str, OnResultListener onResultListener);
}
