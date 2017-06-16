package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.db.dao.DbManager;
import com.smart.kitchen.smartkitchen.db.daoutils.FoodTypeDaoManager;
import com.smart.kitchen.smartkitchen.entitys.FoodType;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import java.util.List;

public class BreakageModelImpl implements CheckModel {
    FoodTypeDaoManager daoManager = new FoodTypeDaoManager();

    public List<FoodType> getFoodTypeFromDB() {
        FoodTypeDaoManager foodTypeDaoManager = this.daoManager;
        return (List<FoodType>) DbManager.getInstance().queryAll(FoodType.class);
    }

    public void getFoodTypeFromNET(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        HttpUtils.post(context, Contants.GET_MENU_TYPE, requestParams, i, onResultListener, progressDialog);
    }

    public void breakage(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, double d, String str2, String str3) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("waiterid", str);
        requestParams.put("money", Double.valueOf(d));
        requestParams.put("contents", str2);
        requestParams.put("mark", str3);
        HttpUtils.post(context, Contants.BREAKAGE_TYPE, requestParams, i, onResultListener, progressDialog);
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
