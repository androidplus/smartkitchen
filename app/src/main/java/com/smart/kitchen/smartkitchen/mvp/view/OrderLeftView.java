package com.smart.kitchen.smartkitchen.mvp.view;

import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import java.util.List;

public interface OrderLeftView {
    void inform(String str);

    void onFial(String str);

    void onSuccess(List<OrderInfo> list);

    void showOrderList(List<OrderInfo> list);
}
