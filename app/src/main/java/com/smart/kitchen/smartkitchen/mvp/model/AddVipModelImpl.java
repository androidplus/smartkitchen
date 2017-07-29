package com.smart.kitchen.smartkitchen.mvp.model;

import android.content.Context;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.entitys.VipInfo;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;

public class AddVipModelImpl implements AddVipModel {
    public void add(Context context, VipInfo vipInfo, OnResultListener onResultListener) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("username", vipInfo.getUsername());
        requestParams.put("displayname", vipInfo.getDisplayname());
        requestParams.put("phone", vipInfo.getPhone());
        if (!Contants.isEmpty(vipInfo.getBirth())) {
            requestParams.put("birth", vipInfo.getBirth());
        }
        if (!Contants.isEmpty(vipInfo.getExtras())) {
            requestParams.put("mark", vipInfo.getExtras());
        }
        HttpUtils.post(context, Contants.ADD_VIP, requestParams, onResultListener, true);
    }
}
