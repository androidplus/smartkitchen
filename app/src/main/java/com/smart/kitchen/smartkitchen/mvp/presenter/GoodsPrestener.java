package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.content.Context;
import com.smart.kitchen.smartkitchen.mvp.model.GoodsModelImpl;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.view.GoodsView;
import java.util.List;

public class GoodsPrestener implements OnResultListener {
    private Context context;
    private GoodsModelImpl goodsModel = new GoodsModelImpl();
    private GoodsView goodsView;

    public GoodsPrestener(Context context, GoodsView goodsView) {
        this.context = context;
        this.goodsView = goodsView;
    }

    public void getGoodsListFromDB(long j) {
        this.goodsView.ShowGoods(this.goodsModel.getGoodsListFromDB(this.goodsView.getMenuTypeId()));
    }

    public void getGoodsListFromNET(long j) {
        this.goodsModel.getGoodsListFromNET(0, this.context, this.goodsView.getMenuTypeId(), this, null);
    }

    public void onSuccess(int i, String str) {
        List asyncJson = this.goodsModel.asyncJson(str);
        this.goodsModel.saveMenu(asyncJson);
        this.goodsView.onSuccess(asyncJson);
    }

    public void onAlert(int i, String str) {
    }

    public void onFailure(int i, String str) {
    }
}
