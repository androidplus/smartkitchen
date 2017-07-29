package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;

public class NewsParticularsModelImpl {
    public void takeOut(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderid", str);
        HttpUtils.post(context, Contants.ORDERFROM, requestParams, i, onResultListener, progressDialog);
    }

    public void updateStatus(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, int i2, String str2) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
        requestParams.put("orderid", str);
        requestParams.put("status", i2);
        requestParams.put("type", str2);
        HttpUtils.post(context, Contants.UPDATEWAIMAISTATUS, requestParams, i, onResultListener, progressDialog);
    }

    public void rejectRefund(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderid", str);
        requestParams.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
        HttpUtils.post(context, Contants.REJECTREFUND, requestParams, i, onResultListener, progressDialog);
    }

    public void confirmRefund(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderid", str);
        requestParams.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
        HttpUtils.post(context, Contants.CONFIRMREFUND, requestParams, i, onResultListener, progressDialog);
    }
}
