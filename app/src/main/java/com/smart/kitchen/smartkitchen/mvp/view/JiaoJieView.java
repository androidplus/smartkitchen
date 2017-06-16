package com.smart.kitchen.smartkitchen.mvp.view;

import com.smart.kitchen.smartkitchen.entitys.JiaoJieInfo;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;

public interface JiaoJieView {
    JiaoJieInfo getJiaoJieInfo();

    UserInfo getUserInfo();

    void onFail();

    void onSuccess(String str);

    void showInfo(JiaoJieInfo jiaoJieInfo);
}
