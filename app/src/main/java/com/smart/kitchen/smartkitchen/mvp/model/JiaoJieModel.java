package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.entitys.JiaoJieInfo;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;

public interface JiaoJieModel {
    void doJiaoJie(int i, Context context, JiaoJieInfo jiaoJieInfo, OnResultListener onResultListener, ProgressDialog progressDialog);

    UserInfo getBeforeUserInfo();

    void getJiaoJieInfo(int i, Context context, UserInfo userInfo, String str, OnResultListener onResultListener, ProgressDialog progressDialog);

    void getJiaoJieInfoById(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog);

    UserInfo getNowUserInfo();

    void updateJiaoBan(int i, Context context, JiaoJieInfo jiaoJieInfo, OnResultListener onResultListener, ProgressDialog progressDialog);
}
