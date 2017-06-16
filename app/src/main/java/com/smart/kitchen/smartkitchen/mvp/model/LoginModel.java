package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;

public interface LoginModel {
    void add(UserInfo userInfo);

    UserInfo asyncJson(String str);

    void getStoreInfo(int i, Context context, String str, OnResultListener onResultListener, ProgressDialog progressDialog);

    UserInfo getUserInfo();

    void login(int i, Context context, String str, String str2, OnResultListener onResultListener, ProgressDialog progressDialog);

    void update(UserInfo userInfo);
}
