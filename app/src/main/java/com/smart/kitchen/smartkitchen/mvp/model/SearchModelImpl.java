package com.smart.kitchen.smartkitchen.mvp.model;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.entitys.VipInfo;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;

public class SearchModelImpl implements SearchModel {
    private static final String TAG = "SearchModelImpl";

    public void search(int i, Context context, String str, OnResultListener onResultListener) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("phone", str);
        HttpUtils.post(context, Contants.SEARCH_VIP, requestParams, onResultListener);
    }

    public VipInfo asyncJson(String str) {
        LogUtils.e(TAG, "asyncJson: " + str);
        return (VipInfo) JSON.parseObject(str, new TypeReference<VipInfo>() {
        }, new Feature[0]);
    }
}
