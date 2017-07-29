package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;

public class VipPayModelImpl implements VipPayModel {
    public void memberTop(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, Double d) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, str);
        requestParams.put("money", (Object) d);
        HttpUtils.post(context, Contants.MEMBERTOP, requestParams, i, onResultListener, progressDialog, true);
    }

    public void querySales(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, str);
        HttpUtils.post(context, Contants.GETSTAFFALL, requestParams, i, onResultListener, progressDialog, true);
    }

    public void aliPayFacePay(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, Double d, String str, String str2, String str3, String str4) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("total_amount", (Object) d);
        requestParams.put("user_id", str2);
        requestParams.put(SPUtils.STORE_ID, str);
        requestParams.put("giftid", str3);
        requestParams.put("staffid", str4);
        HttpUtils.post(context, Contants.VIPPAYQRPAY, requestParams, i, onResultListener, progressDialog, true);
    }

    public void alipayScanCode(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, Double d, String str2, String str3, String str4, String str5) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("auth_code", str);
        requestParams.put("total_amount", (Object) d);
        requestParams.put("user_id", str3);
        requestParams.put(SPUtils.STORE_ID, str2);
        requestParams.put("giftid", str4);
        requestParams.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
        if (!Contants.isEmpty(str5)) {
            requestParams.put("daostaffid", str5);
        }
        HttpUtils.post(context, Contants.VIPPAYFACEPAY, requestParams, i, onResultListener, progressDialog, false);
    }

    public void wxPayFacePay(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, Double d, String str, String str2, String str3, String str4) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("total_amount", (Object) d);
        requestParams.put("user_id", str2);
        requestParams.put(SPUtils.STORE_ID, str);
        requestParams.put("giftid", str3);
        requestParams.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
        if (!Contants.isEmpty(str4)) {
            requestParams.put("daostaffid", str4);
        }
        HttpUtils.post(context, Contants.VIPPAYQRPAYWX, requestParams, i, onResultListener, progressDialog, true);
    }

    public void wxScanCode(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, Double d, String str2, String str3, String str4, String str5) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("auth_code", str);
        requestParams.put("total_amount", (Object) d);
        requestParams.put("user_id", str3);
        requestParams.put(SPUtils.STORE_ID, str2);
        requestParams.put("giftid", str4);
        requestParams.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
        if (!Contants.isEmpty(str5)) {
            requestParams.put("daostaffid", str5);
        }
        HttpUtils.post(context, Contants.VIPPAYWEIXINPAY, requestParams, i, onResultListener, progressDialog, true);
    }

    public void cashMoney(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, Double d, String str, String str2, String str3, String str4) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("total_amount", (Object) d);
        requestParams.put("user_id", str2);
        requestParams.put(SPUtils.STORE_ID, str);
        requestParams.put("giftid", str3);
        requestParams.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
        if (!Contants.isEmpty(str4)) {
            requestParams.put("daostaffid", str4);
        }
        HttpUtils.post(context, Contants.VIPPAYCASH, requestParams, i, onResultListener, progressDialog, true);
    }

    public void requestWXPay() {
    }

    public void requestAliPay() {
    }
}
