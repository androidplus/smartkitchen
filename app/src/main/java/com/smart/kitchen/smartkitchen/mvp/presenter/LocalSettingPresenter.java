package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.content.Context;
import com.smart.kitchen.smartkitchen.mvp.model.LocalSettingModelImpl;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.view.LocalSettingView;

public class LocalSettingPresenter implements OnResultListener {
    private Context context;
    private LocalSettingModelImpl localSettingModel = new LocalSettingModelImpl();
    private LocalSettingView localSettingView;

    public LocalSettingPresenter(Context context, LocalSettingView localSettingView) {
        this.context = context;
        this.localSettingView = localSettingView;
    }

    public void getTableAreaFromNET() {
        this.localSettingModel.getTableAreaFromNET(0, this.context, this, null);
    }

    public void onSuccess(int i, String str) {
        switch (i) {
            case 0:
                this.localSettingView.ShowTableArea(this.localSettingModel.asyncJson(str));
                return;
            default:
                return;
        }
    }

    public void onAlert(int i, String str) {
    }

    public void onFailure(int i, String str) {
    }
}
