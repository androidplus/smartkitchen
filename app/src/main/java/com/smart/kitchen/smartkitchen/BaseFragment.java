package com.smart.kitchen.smartkitchen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.utils.Contants;

public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseActivity";
    protected Activity activity;
    protected Context context;
    protected DialogUtils dialogUtils;
    protected ProgressDialog progressDialog;

    protected abstract void initData();

    protected abstract void initEvent();

    protected abstract void initView(View view);

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.activity = getActivity();
        this.context = getContext();
        this.dialogUtils = new DialogUtils(getActivity());
        initView(view);
        initProgressDialog();
        initEvent();
        initData();
    }

    protected String getStrings(int resId) {
        return Contants.getString(this.context, resId);
    }

    protected void initProgressDialog() {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this.context);
            this.progressDialog.setMessage(getStrings(R.string.loading));
            this.progressDialog.setCancelable(false);
        }
    }
}
