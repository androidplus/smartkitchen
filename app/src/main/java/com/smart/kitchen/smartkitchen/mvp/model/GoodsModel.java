package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.entitys.Goods;
import java.util.List;

public interface GoodsModel {
    List<Goods> asyncJson(String str);

    List<Goods> getGoodsListFromDB(long j);

    void getGoodsListFromNET(int i, Context context, long j, OnResultListener onResultListener, ProgressDialog progressDialog);

    void saveMenu(List<Goods> list);
}
