package com.smart.kitchen.smartkitchen.mvp.model;

public interface OnResultListener {
    void onAlert(int i, String str);

    void onFailure(int i, String str);

    void onSuccess(int i, String str);
}
