package com.smart.kitchen.smartkitchen.mvp.view;

import com.smart.kitchen.smartkitchen.entitys.FoodType;
import java.util.List;

public interface MainView {
    void ShowFoodType(List<FoodType> list);

    void isSubmitIndent(String str, String str2);

    void onFail();

    void onSuccess(List<FoodType> list);
}
