package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.content.Context;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.model.SalesReturnModelImpl;
import com.smart.kitchen.smartkitchen.mvp.view.SalesReturnView;

public class SalesReturnPresenter implements OnResultListener {
    private Context context;
    private SalesReturnModelImpl salesReturnModel = new SalesReturnModelImpl();
    private SalesReturnView salesReturnView;

    public SalesReturnPresenter(Context context, SalesReturnView salesReturnView) {
        this.context = context;
        this.salesReturnView = salesReturnView;
    }

    public void salesReturnConsumer(String str, String str2, String str3, String str4, String str5, String str6) {
        this.salesReturnModel.salesReturnConsumer(0, this.context, this, null, str, str2, str3, str4, str5, str6);
    }

    public void onSuccess(int i, String str) {
        switch (i) {
            case 0:
                this.salesReturnView.onSuccess("salesReturnConsumer");
                return;
            default:
                return;
        }
    }

    public void onAlert(int i, String str) {
        switch (i) {
            case 0:
                this.salesReturnView.onAlert("salesReturnConsumer");
                return;
            default:
                return;
        }
    }

    public void onFailure(int i, String str) {
        switch (i) {
            case 0:
                this.salesReturnView.onFailure("salesReturnConsumer");
                return;
            default:
                return;
        }
    }
}
