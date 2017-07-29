package com.smart.kitchen.smartkitchen.mvp.view;

import com.smart.kitchen.smartkitchen.entitys.VipInfo;

public interface AddVipView {
    String getVipBirth();

    String getVipCard();

    String getVipExtras();

    VipInfo getVipInfo();

    String getVipName();

    String getVipPhone();

    void onFail();

    void onSuccess(VipInfo vipInfo);
}
