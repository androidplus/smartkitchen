package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import java.util.List;

public class HistoryLeftModelImpl implements OrderLeftModel {
    public List<OrderInfo> getFromDB() {
        return null;
    }

    public void getFromNet(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2, int i2) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("page", i2);
        if (!(Contants.isEmpty(str) || Contants.isEmpty(str2))) {
            requestParams.put("start_time", str);
            requestParams.put("end_time", str2);
        }
        HttpUtils.post(context, Contants.GET_ORDER_HISTORY, requestParams, i, onResultListener, progressDialog);
    }

    public void salesReturnOrder(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("id", str);
        requestParams.put("waiterid", new UserInfoDaoManager().getNowUserInfo().getUserid());
        requestParams.put("mark", str2);
        HttpUtils.post(context, Contants.BACKORDERS, requestParams, i, onResultListener, progressDialog, true);
    }

    public List<OrderInfo> asyncJson(String str) {
        return (List) JSON.parseObject(str, new TypeReference<List<OrderInfo>>() {
        }, new Feature[0]);
    }

    public void saveOrder(List<OrderInfo> list) {
    }
}
