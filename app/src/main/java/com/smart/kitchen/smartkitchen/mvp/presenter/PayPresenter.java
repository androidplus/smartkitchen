package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.model.PayModelImpl;
import com.smart.kitchen.smartkitchen.mvp.view.PayView;

public class PayPresenter implements OnResultListener {
    private Context context;
    private PayModelImpl payModel = new PayModelImpl();
    private PayView payView;
    private ProgressDialog progressDialog;

    public PayPresenter(PayView payView, Context context, ProgressDialog progressDialog) {
        this.payView = payView;
        this.context = context;
        this.progressDialog = progressDialog;
    }

    public void aliQrPay(String str, String str2, String str3, String str4) {
    }

    public void wxQrPay(String str, String str2, String str3, String str4) {
    }

    public void aliPayFacePay(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10) {
        this.payModel.aliPayFacePay(1, this.context, this, this.progressDialog, str, str2, str3, str4, str5, str6, str7, str8, str9, str10);
    }

    public void wxPay(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10) {
        this.payModel.wxPay(2, this.context, this, this.progressDialog, str, str2, str3, str4, str5, str6, str7, str8, str9, str10);
    }

    public void payByCash(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        this.payModel.payByCash(4, this.context, this, this.progressDialog, str, str2, str3, str4, str5, str6, str7, str8, str9);
    }

    public void checkConpon(String str, String str2) {
        this.payModel.checkConpon(5, this.context, this, this.progressDialog, str, str2);
    }

    public void countPay(String str, String str2, String str3, String str4, int i, Double d, Double d2) {
        this.payModel.countPay(6, this.context, this, this.progressDialog, str, str2, str3, str4, i, d, d2);
    }

    public void searchVip(String str, String str2) {
        this.payModel.searchVip(7, this.context, this, this.progressDialog, str, str2);
    }

    public void getOrderPay(String str) {
        this.payModel.getOrderPay(9, this.context, this, this.progressDialog, str);
    }

    public void getBuliderOrderQR(String str, String str2, String str3, String str4, String str5, String str6, String str7, int i) {
        this.payModel.getBuliderOrderQR(10, this.context, this, this.progressDialog, str, str2, str3, str4, str5, str6, str7, i);
    }

    public void finishOrders(String str, String str2) {
        this.payModel.finishOrders(11, this.context, this, this.progressDialog, str, str2);
    }

    public void onSuccess(int i, String str) {
        switch (i) {
            case 0:
                this.payView.inform("aliQrPayOnSuccess", str);
                return;
            case 1:
                this.payView.inform("aliPayFacePayOnSuccess", str);
                return;
            case 2:
                this.payView.inform("wxPayOnSuccess", str);
                return;
            case 3:
                this.payView.inform("wxQrPayOnSuccess", str);
                return;
            case 4:
                this.payView.inform("payByCashOnSuccess", str);
                return;
            case 5:
                this.payView.inform("checkConponOnSuccess", str);
                return;
            case 6:
                this.payView.inform("countPayOnSuccess", str);
                return;
            case 7:
                this.payView.inform("searchVipOnSuccess", str);
                return;
            case 9:
                this.payView.inform("orderPayOnSuccess", str);
                return;
            case 10:
                this.payView.inform("BuliderOrderQROnSuccess", str);
                return;
            case 11:
                this.payView.inform("finishOrdersOnSuccess", str);
                return;
            default:
                return;
        }
    }

    public void onAlert(int i, String str) {
        switch (i) {
            case 0:
                this.payView.inform("aliQrPayOnAlert", str);
                return;
            case 1:
                this.payView.inform("aliPayFacePayOnAlert", str);
                return;
            case 2:
                this.payView.inform("wxPayOnAlert", str);
                return;
            case 3:
                this.payView.inform("wxQrPayOnAlert", str);
                return;
            case 4:
                this.payView.inform("payByCashOnAlert", str);
                return;
            case 5:
                this.payView.inform("checkConponOnAlert", str);
                return;
            case 6:
                this.payView.inform("countPayOnAlert", str);
                return;
            case 7:
                this.payView.inform("searchVipOnAlert", str);
                return;
            case 9:
                this.payView.inform("orderPayOnAlert", str);
                return;
            case 10:
                this.payView.inform("BuliderOrderQROnAlert", str);
                return;
            case 11:
                this.payView.inform("finishOrdersOnAlert", str);
                return;
            default:
                return;
        }
    }

    public void onFailure(int i, String str) {
        switch (i) {
            case 0:
                this.payView.inform("aliQrPayOnFailure", str);
                return;
            case 1:
                this.payView.inform("aliPayFacePayOnFailure", str);
                return;
            case 2:
                this.payView.inform("wxPayOnFailure", str);
                return;
            case 3:
                this.payView.inform("wxQrPayOnFailure", str);
                return;
            case 4:
                this.payView.inform("payByCashOnFailure", str);
                return;
            case 5:
                this.payView.inform("checkConponOnFailure", str);
                return;
            case 6:
                this.payView.inform("countPayOnFailure", str);
                return;
            case 7:
                this.payView.inform("searchVipOnFailure", str);
                return;
            case 9:
                this.payView.inform("orderPayOnFailure", str);
                return;
            case 10:
                this.payView.inform("BuliderOrderQROnFailure", str);
                return;
            case 11:
                this.payView.inform("finishOrdersOnFailure", str);
                return;
            default:
                return;
        }
    }
}
