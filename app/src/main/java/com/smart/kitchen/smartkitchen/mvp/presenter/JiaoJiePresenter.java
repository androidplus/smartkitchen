package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.content.Context;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.entitys.JiaoJieInfo;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;
import com.smart.kitchen.smartkitchen.mvp.model.JiaoJieModelImpl;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.view.JiaoJieView;

public class JiaoJiePresenter implements OnResultListener {
    private Context context;
    private JiaoJieModelImpl jiaoJieModel = new JiaoJieModelImpl();
    private JiaoJieView jiaoJieView;

    public JiaoJiePresenter(JiaoJieView jiaoJieView, Context context) {
        this.jiaoJieView = jiaoJieView;
        this.context = context;
    }

    public void doJiaoJie() {
        this.jiaoJieModel.doJiaoJie(0, this.context, this.jiaoJieView.getJiaoJieInfo(), this, null);
    }

    public void updateJiaoJie() {
        this.jiaoJieModel.updateJiaoBan(0, this.context, this.jiaoJieView.getJiaoJieInfo(), this, null);
    }

    public UserInfo getBeforeUserInfo() {
        return this.jiaoJieModel.getBeforeUserInfo();
    }

    public UserInfo getNowUserInfo() {
        return this.jiaoJieModel.getNowUserInfo();
    }

    public void showInfo(String str) {
        this.jiaoJieModel.getJiaoJieInfo(1, this.context, this.jiaoJieView.getUserInfo(), str, this, null);
    }

    public void showInfoById() {
        this.jiaoJieModel.getJiaoJieInfoById(1, this.context, this, null);
    }

    public void onSuccess(int i, String str) {
        if (i == 1) {
            this.jiaoJieView.showInfo((JiaoJieInfo) JSON.parseObject(str, new TypeReference<JiaoJieInfo>() {
            }, new Feature[0]));
        } else {
            this.jiaoJieView.onSuccess(str);
        }
    }

    public void onAlert(int i, String str) {
        this.jiaoJieView.onFail();
    }

    public void onFailure(int i, String str) {
    }
}
