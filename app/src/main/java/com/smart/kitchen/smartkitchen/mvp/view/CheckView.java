package com.smart.kitchen.smartkitchen.mvp.view;

import com.smart.kitchen.smartkitchen.entitys.FoodType;
import java.util.List;

public interface CheckView {
    void ShowFoodType(List<FoodType> list);

    void inFrom(String str);

    void onFail();

    void onSuccess(List<FoodType> list);
}
