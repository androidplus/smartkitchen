package com.smart.kitchen.smartkitchen.mvp.view;

import com.smart.kitchen.smartkitchen.entitys.Goods;
import java.util.List;

public interface GoodsView {
    void ShowGoods(List<Goods> list);

    long getMenuTypeId();

    void onFail();

    void onSuccess(List<Goods> list);
}
