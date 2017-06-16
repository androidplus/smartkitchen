package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.db.dao.DbManager;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;
import com.smart.kitchen.smartkitchen.utils.CalendarUtils;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;

public class LoginModelImpl implements LoginModel {
    private static final String TAG = "HttpsModelImpl";
    UserInfoDaoManager daoManager = new UserInfoDaoManager();

    public void login(int i, Context context, String str, String str2, OnResultListener onResultListener, ProgressDialog progressDialog) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("account", str);
        requestParams.put("password", str2);
        HttpUtils.post(context, Contants.LOGIN, requestParams, i, onResultListener, progressDialog);
    }

    public void getStoreInfo(int i, Context context, String str, OnResultListener onResultListener, ProgressDialog progressDialog) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, str);
        HttpUtils.post(context, Contants.GET_STORE_INFO, requestParams, i, onResultListener, progressDialog);
    }

    public UserInfo getUserInfo() {
        return this.daoManager.getNowUserInfo();
    }

    public void add(UserInfo userInfo) {
        UserInfo userInfo2 = getUserInfo();
        UserInfoDaoManager userInfoDaoManager;
        if (userInfo2 == null) {
            userInfo.setWorkontime(CalendarUtils.getNowFullTime());
            userInfoDaoManager = this.daoManager;
            DbManager.getInstance().insert(UserInfo.class, userInfo);
        } else if (userInfo2.getAccount() == userInfo.getAccount()) {
            userInfo.setId(userInfo2.getId());
            userInfo.setWorkofftime(null);
            userInfo.setWorkontime(userInfo2.getWorkontime());
            userInfoDaoManager = this.daoManager;
            DbManager.getInstance().update(UserInfo.class, userInfo);
        } else {
            userInfo.setWorkontime(CalendarUtils.getNowFullTime());
            userInfoDaoManager = this.daoManager;
            DbManager.getInstance().insert(UserInfo.class, userInfo);
        }
    }

    public void update(UserInfo userInfo) {
        UserInfo userInfo2 = getUserInfo();
        userInfo.setId(userInfo2.getId());
        userInfo.setWorkontime(userInfo2.getWorkontime());
        userInfo.setWorkofftime(null);
        UserInfoDaoManager userInfoDaoManager = this.daoManager;
        DbManager.getInstance().update(UserInfo.class, userInfo);
    }

    public UserInfo asyncJson(String str) {
        UserInfo userInfo = (UserInfo) JSON.parseObject(str, new TypeReference<UserInfo>() {
        }, new Feature[0]);
        LogUtils.e(TAG, "asyncJson: " + userInfo.getRole());
        return userInfo;
    }
}
