package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.content.Context;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.model.OrderLeftModelImpl;
import com.smart.kitchen.smartkitchen.mvp.view.OrderLeftView;

public class OrderRightPresenter implements OnResultListener {
    private Context context;
    private OrderLeftModelImpl orderLeftModel;
    private OrderLeftView orderLeftView;

    public void onSuccess(int i, String str) {
    }

    public void onAlert(int i, String str) {
    }

    public void onFailure(int i, String str) {
    }
}
