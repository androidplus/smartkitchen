package com.smart.kitchen.smartkitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.BaseFragmentActivity;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;
import com.smart.kitchen.smartkitchen.mvp.presenter.LoginPresenter;
import com.smart.kitchen.smartkitchen.mvp.view.LoginView;
import com.smart.kitchen.smartkitchen.service.UpdateAppService;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.DeviceUtils;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;
import com.smart.kitchen.smartkitchen.utils.Permission;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;

public class LoginActivity extends BaseFragmentActivity implements LoginView {
    private static final String TAG = "LoginActivity";
    private EditText etPwd;
    private EditText etUser;
    private boolean isFromMain = false;
    private LinearLayout llUser;
    private Button loginBtn;
    private LoginPresenter presenter;
    private TextView tips;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_login);
        FinishActivity.add(this);
        Permission.isHaveRootActivity(this, new String[]{"android.permission.READ_PHONE_STATE"}, 0, this);
    }

    protected void initView() {
        this.etUser = (EditText) findViewById(R.id.et_user);
        this.etPwd = (EditText) findViewById(R.id.et_pwd);
        this.llUser = (LinearLayout) findViewById(R.id.ll_user);
        this.loginBtn = (Button) findViewById(R.id.login_btn);
        this.tips = (TextView) findViewById(R.id.tips);
        if (!UpdateAppService.IS_OPEN) {
            startService(new Intent(this, UpdateAppService.class));
        }
    }

    protected void initEvent() {
        this.presenter = new LoginPresenter(this, this.context);
        this.isFromMain = getIntent().getBooleanExtra("is_main", false);
    }

    public void onBackPressed() {
        if (this.isFromMain) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }
    }

    protected void initData() {
        if (SPUtils.isLogin(this.context)) {
            this.presenter.setUserName();
            this.llUser.setVisibility(View.GONE);
            this.loginBtn.setText("确定");
        }
    }

    public void onLogin(View view) {
        if (Contants.isEmpty(getUserName()) || Contants.isEmpty(getUserPwd())) {
            setErrorTips("账号和密码不能为空");
        } else {
            this.presenter.login(this.progressDialog);
        }
    }

    private void goMain(UserInfo userInfo) {
        if (this.isFromMain) {
            finish();
        } else {
            check(userInfo);
        }
    }

    private void saveUserInfo(UserInfo userInfo, boolean z) {
        SPUtils.setUserinfo(this.context, SPUtils.STORE_ID, "" + userInfo.getOnwerid());
        this.presenter.getStoreInfo("" + userInfo.getOnwerid());
        if (z) {
            this.presenter.add(userInfo);
            SPUtils.setUserinfo(this.context, SPUtils.IS_LOGIN, Boolean.valueOf(true));
            return;
        }
        this.presenter.update(userInfo);
    }

    protected void initBroadCast() {
    }

    public void onLoginFail(String str) {
        setErrorTips(str);
    }

    public void onLoginSuccess(UserInfo userInfo) {
        goMain(userInfo);
    }

    private void check(UserInfo userInfo) {
        if (Contants.isEmpty(SPUtils.getUserinfo(context, SPUtils.STORE_ID)) ||
                (userInfo.getOnwerid() + "").equals(SPUtils.getUserinfo(context, SPUtils.STORE_ID))) {
            Intent intent;
            if (SPUtils.isLogin(context)) {
                saveUserInfo(userInfo, false);
                if (SPUtils.isNeedJiaojie(this.context)) {
                    intent = new Intent(this.context, JiaoJieActivity.class);
                } else {
                    intent = new Intent(this.context, MainActivity.class);
                }
            } else {
                saveUserInfo(userInfo, true);
                SPUtils.setUserinfo(this.context, SPUtils.LOGIN_MMS, DeviceUtils.getstr(this.context));
                if (SPUtils.isNeedJiaojie(this.context)) {
                    intent = new Intent(this.context, JiaoJieActivity.class);
                } else {
                    intent = new Intent(this.context, MainActivity.class);
                }
            }
            startActivity(intent);
            finish();
            return;
        }
        Toasts.show(this.context, "该账号非本店账号");
    }

    public void setUserName(String str) {
        this.etUser.setText(str);
    }

    public String getUserName() {
        return this.etUser.getText().toString().trim();
    }

    public String getUserPwd() {
        return this.etPwd.getText().toString().trim();
    }

    public void setErrorTips(String str) {
        this.tips.setText(str);
    }
}
