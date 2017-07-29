package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.utils.AuthorityUtil;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import java.util.List;

public class OrderLeftModelImpl implements OrderLeftModel {
    public List<OrderInfo> getFromDB() {
        return null;
    }

    public void getFromNet(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2, int i2) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("page", i2);
        if (!(AuthorityUtil.getInstance().getRoleFlag() == 2 || AuthorityUtil.getInstance().permitCollectMoney())) {
            requestParams.put("areaid", SPUtils.getUserinfo(context, SPUtils.SETTING_AREA));
        }
        if (!(Contants.isEmpty(str) || Contants.isEmpty(str2))) {
            requestParams.put("start_time", str);
            requestParams.put("end_time", str2);
        }
        HttpUtils.post(context, Contants.GET_ORDER_LIST, requestParams, i, onResultListener, progressDialog);
    }

    public void onCancellation(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2, String str3) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("orderid", str);
        requestParams.put("waiterid", str2);
        requestParams.put("mark", str3);
        HttpUtils.post(context, Contants.CANCELLATION, requestParams, i, onResultListener, progressDialog);
    }

    public void salesReturn(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2, int i2, String str3, String str4, String str5, String str6) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("orderdetailid", str);
        requestParams.put("orderid", str2);
        requestParams.put("count", i2);
        requestParams.put("goodsinfo", str3);
        requestParams.put("mark", str4);
        requestParams.put("waiterid", str5);
        requestParams.put("onwerid", str6);
        HttpUtils.post(context, Contants.BACKGOODS, requestParams, i, onResultListener, progressDialog);
    }

    public void updateorderGoods(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("orderdetailid", str);
        requestParams.put("status", str2);
        HttpUtils.post(context, Contants.UPDATEORDERGOODS, requestParams, i, onResultListener, progressDialog);
    }

    public void finishOrders(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("orderid", str);
        requestParams.put("waiterid", str2);
        requestParams.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
        HttpUtils.post(context, Contants.FINISHORDERS, requestParams, i, onResultListener, progressDialog, true);
    }

    public void updateStatus(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, int i2, String str2) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderid", str);
        requestParams.put("status", i2);
        requestParams.put("type", str2);
        requestParams.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
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

    public List<OrderInfo> asyncJson(String str) {
        return (List) JSON.parseObject(str, new TypeReference<List<OrderInfo>>() {
        }, new Feature[0]);
    }

    public void saveOrder(List<OrderInfo> list) {
    }
}
