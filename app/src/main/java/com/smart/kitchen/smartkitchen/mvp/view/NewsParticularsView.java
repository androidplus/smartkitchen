package com.smart.kitchen.smartkitchen.mvp.view;

public interface NewsParticularsView {
    void onAlert(int i, String str);

    void onFailure(int i, String str);

    void onSuccess(int i, String str);
}
