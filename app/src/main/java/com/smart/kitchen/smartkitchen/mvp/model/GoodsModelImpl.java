package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.db.dao.DbManager;
import com.smart.kitchen.smartkitchen.db.dao.GoodsDao.Properties;
import com.smart.kitchen.smartkitchen.db.daoutils.GoodsDaoManager;
import com.smart.kitchen.smartkitchen.entitys.Goods;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import java.util.ArrayList;
import java.util.List;

public class GoodsModelImpl implements GoodsModel {
    GoodsDaoManager daoManager = new GoodsDaoManager();

    public List<Goods> getGoodsListFromDB(long j) {
        List arrayList = new ArrayList();
        arrayList.add(Properties.Goods_type);
        List arrayList2 = new ArrayList();
        arrayList2.add(Long.valueOf(j));
        GoodsDaoManager goodsDaoManager = this.daoManager;
        return DbManager.getInstance().queryMultObject(Goods.class, arrayList, arrayList2);
    }

    public void getGoodsListFromNET(int i, Context context, long j, OnResultListener onResultListener, ProgressDialog progressDialog) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("menu_id", j);
        HttpUtils.post(context, Contants.GET_MENU, requestParams, i, onResultListener, progressDialog);
    }

    public void saveMenu(List<Goods> list) {
        GoodsDaoManager goodsDaoManager = this.daoManager;
        DbManager.getInstance().deleteAll(Goods.class);
        goodsDaoManager = this.daoManager;
        DbManager.getInstance().insertMultObject(Goods.class, list);
    }

    public List<Goods> asyncJson(String str) {
        return (List) JSON.parseObject(str, new TypeReference<List<Goods>>() {
        }, new Feature[0]);
    }
}
