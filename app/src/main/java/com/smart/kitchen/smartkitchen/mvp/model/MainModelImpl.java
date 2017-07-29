package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.db.dao.DbManager;
import com.smart.kitchen.smartkitchen.db.daoutils.FoodTypeDaoManager;
import com.smart.kitchen.smartkitchen.db.daoutils.MessageCenterDaoManager;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import com.smart.kitchen.smartkitchen.entitys.MessageCenter;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import java.util.List;

public class MainModelImpl implements MainModel {
    FoodTypeDaoManager daoManager = new FoodTypeDaoManager();
    MessageCenterDaoManager messageCenterDaoManager = new MessageCenterDaoManager();

    public List<FoodType> getFoodTypeFromDB() {
        FoodTypeDaoManager foodTypeDaoManager = this.daoManager;
        return (List<FoodType>) DbManager.getInstance().queryAll(FoodType.class);
    }

    public List<MessageCenter> getMessageCenterListNORead() {
        return this.messageCenterDaoManager.getNoReadedList();
    }

    public List<MessageCenter> getMessageCenterListReaded() {
        return this.messageCenterDaoManager.getReadedList();
    }

    public void getFoodTypeFromNET(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        HttpUtils.post(context, Contants.GET_MENU_TYPE, requestParams, i, onResultListener, progressDialog);
    }

    public void getAreaFromNET(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        HttpUtils.post(context, Contants.GET_TABLE_AREA, requestParams, i, onResultListener, progressDialog, true);
    }

    public void submitIndent(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("orderid", str);
        requestParams.put("goodslist", str2);
        HttpUtils.post(context, Contants.ADD_TO, requestParams, i, onResultListener, progressDialog);
    }

    public void search(int i, Context context, String str, OnResultListener onResultListener) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("phone", str);
        HttpUtils.post(context, Contants.SEARCH_VIP, requestParams, i, onResultListener, null, true);
    }

    public void saveFoodType(List<FoodType> list) {
        FoodTypeDaoManager foodTypeDaoManager = this.daoManager;
        DbManager.getInstance().deleteAll(FoodType.class);
        foodTypeDaoManager = this.daoManager;
        DbManager.getInstance().insertMultObject(FoodType.class, list);
    }

    public List<FoodType> asyncJson(String str) {
        return (List) JSON.parseObject(str, new TypeReference<List<FoodType>>() {
        }, new Feature[0]);
    }
}
