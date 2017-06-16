package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.entitys.JiaoJieInfo;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;

public class JiaoJieModelImpl implements JiaoJieModel {
    private static final String TAG = "JiaoJieModelImpl";
    UserInfoDaoManager userM = new UserInfoDaoManager();

    public UserInfo getBeforeUserInfo() {
        return this.userM.getBeforeUserInfo();
    }

    public UserInfo getNowUserInfo() {
        return this.userM.getNowUserInfo();
    }

    public void getJiaoJieInfo(int i, Context context, UserInfo userInfo, String str, OnResultListener onResultListener, ProgressDialog progressDialog) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("staffid", userInfo.getUserid());
        requestParams.put("workontime", userInfo.getWorkontime());
        requestParams.put("workofftime", str);
        HttpUtils.post(context, Contants.GETJIAOBANINGO, requestParams, i, onResultListener, progressDialog);
    }

    public void getJiaoJieInfoById(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", SPUtils.getUserinfo(context, SPUtils.ID_JIAOJIE));
        HttpUtils.post(context, Contants.GETJIAOBANINGOBYIDBYID, requestParams, i, onResultListener, progressDialog);
    }

    public void updateJiaoBan(int i, Context context, JiaoJieInfo jiaoJieInfo, OnResultListener onResultListener, ProgressDialog progressDialog) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", SPUtils.getUserinfo(context, SPUtils.ID_JIAOJIE));
        requestParams.put("jieid", jiaoJieInfo.getJieid());
        requestParams.put("mark", jiaoJieInfo.getMark());
        HttpUtils.post(context, Contants.UPDATE_JIAOBAN, requestParams, i, onResultListener, progressDialog);
    }

    public void doJiaoJie(int i, Context context, JiaoJieInfo jiaoJieInfo, OnResultListener onResultListener, ProgressDialog progressDialog) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("jiaoid", jiaoJieInfo.getJiaoid());
        requestParams.put("money", jiaoJieInfo.getMoney());
        requestParams.put("crmmoney", jiaoJieInfo.getCrmmoney());
        requestParams.put("totalmoney", jiaoJieInfo.getTotalmoney());
        requestParams.put("ordercount", jiaoJieInfo.getOrdercount());
        requestParams.put("nonpayedorder", jiaoJieInfo.getNonpayedorder());
        requestParams.put("workontime", jiaoJieInfo.getWorkontime());
        requestParams.put("workofftime", jiaoJieInfo.getWorkofftime());
        requestParams.put("saleorder", jiaoJieInfo.getSaleorder());
        requestParams.put("restmoney", jiaoJieInfo.getRestmoney());
        HttpUtils.post(context, Contants.JIAOBAN, requestParams, i, onResultListener, progressDialog);
    }
}
