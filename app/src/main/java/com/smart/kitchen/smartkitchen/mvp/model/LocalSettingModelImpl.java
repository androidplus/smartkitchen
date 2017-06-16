package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.entitys.TableArea;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import java.util.List;

public class LocalSettingModelImpl implements LocalSettingModel {
    public void getTableAreaFromNET(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        HttpUtils.post(context, Contants.GET_TABLE_AREA, requestParams, i, onResultListener, progressDialog, true);
    }

    public List<TableArea> asyncJson(String str) {
        return (List) JSON.parseObject(str, new TypeReference<List<TableArea>>() {
        }, new Feature[0]);
    }
}
