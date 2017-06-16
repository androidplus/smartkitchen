package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.mvp.model.HistoryLeftModelImpl;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.view.HistoryOrderView;
import java.util.List;

public class HistoryLeftPrestener implements OnResultListener {
    private Context context;
    private HistoryLeftModelImpl historyLeftModel = new HistoryLeftModelImpl();
    private HistoryOrderView orderLeftView;
    private ProgressDialog progressDialog;

    public HistoryLeftPrestener(HistoryOrderView historyOrderView, Context context, ProgressDialog progressDialog) {
        this.orderLeftView = historyOrderView;
        this.context = context;
        this.progressDialog = progressDialog;
    }

    public void getFromDB() {
        this.orderLeftView.showOrderList(this.historyLeftModel.getFromDB());
    }

    public void getFromNet(String str, String str2, int i) {
        this.historyLeftModel.getFromNet(0, this.context, this, null, str, str2, i);
    }

    public void salesReturnOrder(String str, String str2) {
        this.historyLeftModel.salesReturnOrder(1, this.context, this, null, str, str2);
    }

    public void onSuccess(int i, String str) {
        switch (i) {
            case 0:
                List asyncJson = this.historyLeftModel.asyncJson(str);
                this.historyLeftModel.saveOrder(asyncJson);
                this.orderLeftView.onSuccess(asyncJson);
                return;
            case 1:
                this.orderLeftView.inform("onSuccess");
                return;
            default:
                return;
        }
    }

    public void onAlert(int i, String str) {
        switch (i) {
            case 1:
                this.orderLeftView.inform("onAlert");
                return;
            default:
                return;
        }
    }

    public void onFailure(int i, String str) {
        switch (i) {
            case 0:
                this.orderLeftView.onFial(str);
                return;
            case 1:
                this.orderLeftView.inform("onFailure");
                return;
            default:
                return;
        }
    }
}
