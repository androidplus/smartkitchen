package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.model.OrderLeftModelImpl;
import com.smart.kitchen.smartkitchen.mvp.view.OrderLeftView;
import java.util.List;

public class OrderLeftPrestener implements OnResultListener {
    private Context context;
    private OrderLeftModelImpl orderLeftModel = new OrderLeftModelImpl();
    private OrderLeftView orderLeftView;
    private ProgressDialog progressDialog;

    public OrderLeftPrestener(OrderLeftView orderLeftView, Context context, ProgressDialog progressDialog) {
        this.orderLeftView = orderLeftView;
        this.context = context;
        this.progressDialog = progressDialog;
    }

    public void getFromDB() {
        this.orderLeftView.showOrderList(this.orderLeftModel.getFromDB());
    }

    public void getFromNet(String str, String str2, int i) {
        this.orderLeftModel.getFromNet(0, this.context, this, null, str, str2, i);
    }

    public void onCancellation(String str, String str2, String str3) {
        this.orderLeftModel.onCancellation(1, this.context, this, this.progressDialog, str, str2, str3);
    }

    public void salesReturn(String str, String str2, int i, String str3, String str4, String str5, String str6) {
        this.orderLeftModel.salesReturn(2, this.context, this, this.progressDialog, str, str2, i, str3, str4, str5, str6);
    }

    public void updateorderGoods(String str, String str2) {
        this.orderLeftModel.updateorderGoods(3, this.context, this, this.progressDialog, str, str2);
    }

    public void finishOrders(String str, String str2) {
        this.orderLeftModel.finishOrders(4, this.context, this, this.progressDialog, str, str2);
    }

    public void updateStatus(String str, int i, String str2) {
        this.orderLeftModel.updateStatus(5, this.context, this, this.progressDialog, str, i, str2);
    }

    public void confirmRefund(String str) {
        this.orderLeftModel.confirmRefund(6, this.context, this, this.progressDialog, str);
    }

    public void rejectRefund(String str) {
        this.orderLeftModel.rejectRefund(7, this.context, this, this.progressDialog, str);
    }

    public void onSuccess(int i, String str) {
        switch (i) {
            case 0:
                List asyncJson = this.orderLeftModel.asyncJson(str);
                this.orderLeftModel.saveOrder(asyncJson);
                this.orderLeftView.onSuccess(asyncJson);
                return;
            case 1:
                this.orderLeftView.inform("onSuccess");
                return;
            case 2:
                this.orderLeftView.inform("onSuccessSalesReturn");
                return;
            case 3:
                this.orderLeftView.inform("onSuccessUpdateorderGoods");
                return;
            case 4:
                this.orderLeftView.inform("onSuccessFinishOrders");
                return;
            case 5:
                this.orderLeftView.inform("onSuccessWaiMaiStatus");
                return;
            case 6:
                this.orderLeftView.inform("onSuccessconfirmRefund");
                return;
            case 7:
                this.orderLeftView.inform("onSuccessrejectRefund");
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
            case 2:
                this.orderLeftView.inform("onAlertSalesReturn");
                return;
            case 3:
                this.orderLeftView.inform("onAlertUpdateorderGoods");
                return;
            case 4:
                this.orderLeftView.inform("onAlertFinishOrders");
                return;
            case 5:
                this.orderLeftView.inform("onAlertWaiMaiStatus");
                return;
            case 6:
                this.orderLeftView.inform("onAlertconfirmRefund");
                return;
            case 7:
                this.orderLeftView.inform("onAlertrejectRefund");
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
            case 2:
                this.orderLeftView.inform("onFailureSalesReturn");
                return;
            case 3:
                this.orderLeftView.inform("onFailureUpdateorderGoods");
                return;
            case 4:
                this.orderLeftView.inform("onFailureFinishOrders");
                return;
            case 5:
                this.orderLeftView.inform("onFailureWaiMaiStatus");
                return;
            case 6:
                this.orderLeftView.inform("onFailureconfirmRefund");
                return;
            case 7:
                this.orderLeftView.inform("onFailurerejectRefund");
                return;
            default:
                return;
        }
    }
}
