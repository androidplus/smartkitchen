package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.model.VipPayModelImpl;
import com.smart.kitchen.smartkitchen.mvp.view.VipPayView;

public class VipPayPresenter implements OnResultListener {
    private Context context;
    private ProgressDialog progressDialog;
    private VipPayModelImpl vipPayModel = new VipPayModelImpl();
    private VipPayView vipPayView;

    public VipPayPresenter(Context context, VipPayView vipPayView, ProgressDialog progressDialog) {
        this.context = context;
        this.vipPayView = vipPayView;
        this.progressDialog = progressDialog;
    }

    public void memberTop(String str, Double d) {
        this.vipPayModel.memberTop(0, this.context, this, this.progressDialog, str, d);
    }

    public void querySales(String str) {
        this.vipPayModel.querySales(1, this.context, this, this.progressDialog, str);
    }

    public void aliPayFacePay(Double d, String str, String str2, String str3, String str4) {
        this.vipPayModel.aliPayFacePay(2, this.context, this, this.progressDialog, d, str, str2, str3, str4);
    }

    public void wxPayFacePay(Double d, String str, String str2, String str3, String str4) {
        this.vipPayModel.wxPayFacePay(3, this.context, this, this.progressDialog, d, str, str2, str3, str4);
    }

    public void wxScanCode(String str, Double d, String str2, String str3, String str4, String str5) {
        this.vipPayModel.wxScanCode(5, this.context, this, this.progressDialog, str, d, str2, str3, str4, str5);
    }

    public void alipayScanCode(String str, Double d, String str2, String str3, String str4, String str5) {
        this.vipPayModel.alipayScanCode(6, this.context, this, this.progressDialog, str, d, str2, str3, str4, str5);
    }

    public void cashMoney(Double d, String str, String str2, String str3, String str4) {
        this.vipPayModel.cashMoney(4, this.context, this, this.progressDialog, d, str, str2, str3, str4);
    }

    public void onSuccess(int i, String str) {
        this.vipPayView.onPaySucess(i, str);
    }

    public void onAlert(int i, String str) {
        this.vipPayView.onFails(i, str);
    }

    public void onFailure(int i, String str) {
        this.vipPayView.onFails(i, str);
    }
}
