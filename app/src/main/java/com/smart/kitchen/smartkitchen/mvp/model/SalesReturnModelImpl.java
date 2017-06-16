package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;

public class SalesReturnModelImpl {
    public void salesReturnConsumer(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2, String str3, String str4, String str5, String str6) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("orderid", str);
        requestParams.put("totalprice", str2);
        requestParams.put("mark", str3);
        requestParams.put("waiterid", str4);
        requestParams.put("onwerid", str5);
        requestParams.put("goodslist", str6);
        HttpUtils.post(context, Contants.BACKGOODS_TO, requestParams, i, onResultListener, progressDialog, true);
    }
}
