package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.content.Context;
import com.smart.kitchen.smartkitchen.mvp.model.CheckModelImpl;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.view.CheckView;
import java.util.List;

public class CheckPresenter implements OnResultListener {
    private CheckModelImpl checkModel = new CheckModelImpl();
    private CheckView checkView;
    private Context context;

    public CheckPresenter(Context context, CheckView checkView) {
        this.context = context;
        this.checkView = checkView;
    }

    public void getGoodsListFromDB() {
        this.checkView.ShowFoodType(this.checkModel.getFoodTypeFromDB());
    }

    public void getGoodsListFromNET() {
        this.checkModel.getFoodTypeFromNET(0, this.context, this, null);
    }

    public void submitCheck(String str, String str2) {
        this.checkModel.submitCheck(1, this.context, this, null, str, str2);
    }

    public void onSuccess(int i, String str) {
        switch (i) {
            case 0:
                List asyncJson = this.checkModel.asyncJson(str);
                this.checkModel.saveFoodType(asyncJson);
                this.checkView.onSuccess(asyncJson);
                return;
            case 1:
                this.checkView.inFrom("onSuccess");
                return;
            default:
                return;
        }
    }

    public void onAlert(int i, String str) {
        switch (i) {
            case 1:
                this.checkView.inFrom("onAlert");
                return;
            default:
                return;
        }
    }

    public void onFailure(int i, String str) {
        switch (i) {
            case 1:
                this.checkView.inFrom("onFailure");
                return;
            default:
                return;
        }
    }
}
