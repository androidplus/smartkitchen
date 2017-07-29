package com.smart.kitchen.smartkitchen.mvp.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import com.smart.kitchen.smartkitchen.mvp.model.NewsParticularsModelImpl;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.view.NewsParticularsView;

public class NewsParticularsPresenter implements OnResultListener {
    private Context context;
    private NewsParticularsModelImpl newsParticularsModel = new NewsParticularsModelImpl();
    private NewsParticularsView newsParticularsView;
    private ProgressDialog progressDialog;

    public NewsParticularsPresenter(Context context, NewsParticularsView newsParticularsView, ProgressDialog progressDialog) {
        this.context = context;
        this.newsParticularsView = newsParticularsView;
        this.progressDialog = progressDialog;
    }

    public void takeOut(String str) {
        this.newsParticularsModel.takeOut(0, this.context, this, this.progressDialog, str);
    }

    public void updateStatus(String str, int i, String str2) {
        this.newsParticularsModel.updateStatus(1, this.context, this, this.progressDialog, str, i, str2);
    }

    public void confirmRefund(String str) {
        this.newsParticularsModel.confirmRefund(6, this.context, this, this.progressDialog, str);
    }

    public void rejectRefund(String str) {
        this.newsParticularsModel.rejectRefund(7, this.context, this, this.progressDialog, str);
    }

    public void onSuccess(int i, String str) {
        switch (i) {
            case 6:
                this.newsParticularsView.onSuccess(i, str);
                return;
            case 7:
                this.newsParticularsView.onSuccess(i, str);
                return;
            default:
                this.newsParticularsView.onSuccess(i, str);
                return;
        }
    }

    public void onAlert(int i, String str) {
        switch (i) {
            case 6:
                this.newsParticularsView.onSuccess(i, str);
                return;
            case 7:
                this.newsParticularsView.onSuccess(i, str);
                return;
            default:
                this.newsParticularsView.onAlert(i, str);
                return;
        }
    }

    public void onFailure(int i, String str) {
        switch (i) {
            case 6:
                this.newsParticularsView.onSuccess(i, str);
                return;
            case 7:
                this.newsParticularsView.onSuccess(i, str);
                return;
            default:
                this.newsParticularsView.onFailure(i, str);
                return;
        }
    }
}
