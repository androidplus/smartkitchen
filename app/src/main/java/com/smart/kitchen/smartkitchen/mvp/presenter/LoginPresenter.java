package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;
import com.smart.kitchen.smartkitchen.mvp.model.LoginModelImpl;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.view.LoginView;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.SPUtils;

public class LoginPresenter implements OnResultListener {
    private static final String TAG = "LoginPresenter";
    Context context;
    LoginModelImpl httpsModel = new LoginModelImpl();
    LoginView loginView;

    public LoginPresenter(LoginView loginView, Context context) {
        this.loginView = loginView;
        this.context = context;
    }

    public void login(ProgressDialog progressDialog) {
        this.httpsModel.login(1, this.context, this.loginView.getUserName(), this.loginView.getUserPwd(), this, progressDialog);
    }

    public void getStoreInfo(String str) {
        this.httpsModel.getStoreInfo(Contants.GET_STORE_INFO_CODE, this.context, str, this, null);
    }

    public void add(UserInfo userInfo) {
        this.httpsModel.add(userInfo);
    }

    public void update(UserInfo userInfo) {
        this.httpsModel.update(userInfo);
    }

    public void setUserName() {
        this.loginView.setUserName(this.httpsModel.getUserInfo().getPhone());
    }

    public void onSuccess(int i, String str) {
        switch (i) {
            case 1:
                this.loginView.onLoginSuccess(this.httpsModel.asyncJson(str));
                return;
            case Contants.GET_STORE_INFO_CODE /*999*/:
                SPUtils.setUserinfo(this.context, SPUtils.STORE_INFO, str);
                return;
            default:
                return;
        }
    }

    public void onAlert(int i, String str) {
        this.loginView.onLoginFail(str);
    }

    public void onFailure(int i, String str) {
    }
}
