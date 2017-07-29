package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.entitys.MessageCenter;
import com.smart.kitchen.smartkitchen.mvp.model.MainModelImpl;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.view.MainView;
import com.smart.kitchen.smartkitchen.utils.CalendarUtils;
import java.util.List;

public class MainPresenter implements OnResultListener {
    private Context context;
    private MainModelImpl mainModel;
    private MainView mainView;
    private long mms = 0;
    private ProgressDialog progressDialog;

    public MainPresenter(Context context, MainView mainView, ProgressDialog progressDialog) {
        this.context = context;
        this.mainView = mainView;
        this.mainModel = new MainModelImpl();
        this.progressDialog = progressDialog;
    }

    public void getGoodsListFromDB() {
        this.mainView.ShowFoodType(this.mainModel.getFoodTypeFromDB());
    }

    public List<MessageCenter> getMessaggeListNOReadFromDB() {
        return this.mainModel.getMessageCenterListNORead();
    }

    public List<MessageCenter> getMessaggeListReadedFromDB() {
        return this.mainModel.getMessageCenterListReaded();
    }

    public void getGoodsListFromNET() {
        this.mainModel.getFoodTypeFromNET(0, this.context, this, null);
    }

    public void getAreaListFromNET() {
        this.mainModel.getAreaFromNET(2, this.context, this, null);
    }

    public void submitIndent(String str, String str2) {
        long nowMms = CalendarUtils.getNowMms();
        if ((nowMms - this.mms) / 1000 >= 3) {
            this.mms = nowMms;
            this.mainModel.submitIndent(1, this.context, this, this.progressDialog, str, str2);
        }
    }

    public void search(String str) {
        this.mainModel.search(3, this.context, str, this);
    }

    @Override
    public void onSuccess(int i, String str) {
        switch (i) {
            case 0:
                List asyncJson = this.mainModel.asyncJson(str);
                this.mainModel.saveFoodType(asyncJson);
                this.mainView.onSuccess(asyncJson);
                return;
            case 1:
                this.mainView.isSubmitIndent("onSuccess", str);
                return;
            case 2:
                this.mainView.isSubmitIndent("AreaOnSuccess", str);
                return;
            case 3:
                this.mainView.isSubmitIndent("SearchOnSuccess", str);
                return;
            default:
                return;
        }
    }

    public void onAlert(int i, String str) {
        switch (i) {
            case 1:
                this.mainView.isSubmitIndent("onAlert", str);
                return;
            case 2:
                this.mainView.isSubmitIndent("AreaOnAlert", str);
                return;
            case 3:
                this.mainView.isSubmitIndent("SearchOnAlert", str);
                return;
            default:
                return;
        }
    }

    public void onFailure(int i, String str) {
        switch (i) {
            case 1:
                this.mainView.isSubmitIndent("onFailure", str);
                return;
            case 2:
                this.mainView.isSubmitIndent("AreaOnFailure", str);
                return;
            case 3:
                this.mainView.isSubmitIndent("SearchOnFailure", str);
                return;
            default:
                return;
        }
    }
}
