package com.smart.kitchen.smartkitchen.mvp.model;

import android.content.Context;
import com.smart.kitchen.smartkitchen.entitys.VipInfo;

public interface AddVipModel {
    void add(Context context, VipInfo vipInfo, OnResultListener onResultListener);
}
