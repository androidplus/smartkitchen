package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.content.Context;
import com.smart.kitchen.smartkitchen.mvp.model.BreakageModelImpl;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.view.BreakageView;
import java.util.List;

public class BreakagePresenter implements OnResultListener {
    private BreakageModelImpl breakageModel = new BreakageModelImpl();
    private BreakageView breakageView;
    private Context context;

    public BreakagePresenter(Context context, BreakageView breakageView) {
        this.context = context;
        this.breakageView = breakageView;
    }

    public void getGoodsListFromDB() {
        this.breakageView.ShowFoodType(this.breakageModel.getFoodTypeFromDB());
    }

    public void getGoodsListFromNET() {
        this.breakageModel.getFoodTypeFromNET(0, this.context, this, null);
    }

    public void breakage(String str, double d, String str2, String str3) {
        this.breakageModel.breakage(1, this.context, this, null, str, d, str2, str3);
    }

    public void onSuccess(int i, String str) {
        switch (i) {
            case 0:
                List asyncJson = this.breakageModel.asyncJson(str);
                this.breakageModel.saveFoodType(asyncJson);
                this.breakageView.onSuccess(asyncJson);
                return;
            case 1:
                this.breakageView.inFrom("onSuccess");
                return;
            default:
                return;
        }
    }

    public void onAlert(int i, String str) {
        switch (i) {
            case 1:
                this.breakageView.inFrom("onAlert");
                return;
            default:
                return;
        }
    }

    public void onFailure(int i, String str) {
        switch (i) {
            case 1:
                this.breakageView.inFrom("onFailure");
                return;
            default:
                return;
        }
    }
}
