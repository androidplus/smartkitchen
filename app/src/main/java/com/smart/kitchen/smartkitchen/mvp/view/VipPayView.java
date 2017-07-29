package com.smart.kitchen.smartkitchen.mvp.view;

public interface VipPayView {
    void changeBackground(int i);

    String getIncomeMoney();

    int getPayStyle();

    String getRestMoney();

    String getTotalMoney();

    void onFails(int i, String str);

    void onPaySucess(int i, String str);
}
