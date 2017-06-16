package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.entitys.VipInfo;
import com.smart.kitchen.smartkitchen.mvp.model.AddVipModelImpl;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.view.AddVipView;

public class AddVipPresenter implements OnResultListener {
    private AddVipModelImpl addVipModel = new AddVipModelImpl();
    private AddVipView addVipView;
    private Context context;

    public AddVipPresenter(AddVipView addVipView, Context context) {
        this.addVipView = addVipView;
        this.context = context;
    }

    public void add() {
        this.addVipModel.add(this.context, this.addVipView.getVipInfo(), this);
    }

    public void onSuccess(int i, String str) {
        this.addVipView.onSuccess((VipInfo) JSON.parseObject(str, new TypeReference<VipInfo>() {
        }, new Feature[0]));
    }

    public void onAlert(int i, String str) {
    }

    public void onFailure(int i, String str) {
    }
}
