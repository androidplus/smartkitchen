package com.smart.kitchen.smartkitchen.mvp.view;

import com.smart.kitchen.smartkitchen.entitys.VipInfo;

public interface SearchView {
    String getInput();

    void onFial(String str);

    void onSuccess(VipInfo vipInfo);
}
