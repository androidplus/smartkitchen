package com.smart.kitchen.smartkitchen.mvp.view;

import com.smart.kitchen.smartkitchen.entitys.UserInfo;

public interface LoginView {
    String getUserName();

    String getUserPwd();

    void onLoginFail(String str);

    void onLoginSuccess(UserInfo userInfo);

    void setErrorTips(String str);

    void setUserName(String str);
}
