package com.smart.kitchen.smartkitchen.mvp.model;

import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import java.util.List;

public interface OrderRightModel {
    List<OrderInfo> getFromDB();

    void saveOrder(List<OrderInfo> list);
}
