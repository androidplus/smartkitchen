package com.smart.kitchen.smartkitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.smart.kitchen.smartkitchen.BaseActivity;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.entitys.JiaoJieInfo;
import com.smart.kitchen.smartkitchen.entitys.TestEneitys;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;
import com.smart.kitchen.smartkitchen.mvp.presenter.JiaoJiePresenter;
import com.smart.kitchen.smartkitchen.mvp.view.JiaoJieView;
import com.smart.kitchen.smartkitchen.service.ClientSocketService;
import com.smart.kitchen.smartkitchen.utils.CalendarUtils;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;

public class RepeatActivity extends BaseActivity implements JiaoJieView {
    private static final String TAG = "RepeatActivity";
    private EditText etExtra;
    private JiaoJieInfo jiaoJieInfo;
    private LinearLayout llContainer;
    private JiaoJiePresenter presenter;
    private TextView tvGuadanTotalCount;
    private TextView tvJiaojie;
    private TextView tvOrderCount;
    private TextView tvOrderTotalMoney;
    private TextView tvQuit;
    private TextView tvRealMoney;
    private TextView tvSaleCount;
    private TextView tvTime;
    private TextView tvVipPay;

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_repeat);
        FinishActivity.add(this);
    }

    protected void initView() {
        this.llContainer = (LinearLayout) findViewById(R.id.ll_container_jiaojie);
        this.llContainer.setBackgroundColor(this.context.getResources().getColor(R.color.white));
        this.tvTime = (TextView) findViewById(R.id.tv_time);
        this.tvOrderCount = (TextView) findViewById(R.id.tv_order_count);
        this.tvOrderTotalMoney = (TextView) findViewById(R.id.tv_order_total_money);
        this.tvGuadanTotalCount = (TextView) findViewById(R.id.tv_guadan_total_count);
        this.tvVipPay = (TextView) findViewById(R.id.tv_vip_pay);
        this.tvSaleCount = (TextView) findViewById(R.id.tv_sale_count);
        this.tvRealMoney = (TextView) findViewById(R.id.tv_real_money);
        this.etExtra = (EditText) findViewById(R.id.et_extra);
        this.tvJiaojie = (TextView) findViewById(R.id.tv_jiaojie);
        this.tvQuit = (TextView) findViewById(R.id.tv_jiaojie_quit);
    }

    protected void initEvent() {
        this.presenter = new JiaoJiePresenter(this, this.context);
        this.tvJiaojie.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new UserInfoDaoManager().offWorkUserInfo();
                RepeatActivity.this.presenter.doJiaoJie();
            }
        });
        this.tvQuit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                RepeatActivity.this.finish();
            }
        });
    }

    protected void initData() {
        String nowFullTime = CalendarUtils.getNowFullTime();
        this.tvTime.setText(nowFullTime);
        this.presenter.showInfo(nowFullTime);
    }

    public void onSuccess(String str) {
        SPUtils.setUserinfo(this.context, SPUtils.NEED_JIAOJIE, Boolean.valueOf(true));
        SPUtils.setUserinfo(this.context, SPUtils.ID_JIAOJIE, str);
        SPUtils.setUserinfo(this.context, SPUtils.IS_LOGIN, Boolean.valueOf(false));
        this.context.stopService(new Intent(this.context, ClientSocketService.class));
        startActivity(new Intent(this.context, LoginActivity.class));
        FinishActivity.finish();
        FinishActivity.clear();
        TestEneitys.setEmpty();
        finish();
    }

    public void onFail() {
    }

    public void showInfo(JiaoJieInfo jiaoJieInfo) {
        this.jiaoJieInfo = jiaoJieInfo;
        this.tvOrderCount.setText("" + jiaoJieInfo.getOrdercount());
        this.tvOrderTotalMoney.setText("" + jiaoJieInfo.getTotalmoney());
        this.tvGuadanTotalCount.setText("" + jiaoJieInfo.getNonpayedorder());
        this.tvVipPay.setText("" + jiaoJieInfo.getCrmmoney());
        this.tvSaleCount.setText("" + jiaoJieInfo.getSaleorder());
        if (jiaoJieInfo.getMoney() == null) {
            this.tvRealMoney.setText("0");
        } else {
            this.tvRealMoney.setText("" + jiaoJieInfo.getMoney());
        }
    }

    public JiaoJieInfo getJiaoJieInfo() {
        String trim = this.etExtra.getText().toString().trim();
        if (!Contants.isEmpty(trim)) {
            this.jiaoJieInfo.setMark(trim);
        }
        UserInfo nowUserInfo = this.presenter.getNowUserInfo();
        this.jiaoJieInfo.setJiaoid(Long.valueOf(Long.parseLong(nowUserInfo.getUserid())));
        this.jiaoJieInfo.setWorkontime(nowUserInfo.getWorkontime());
        this.jiaoJieInfo.setWorkofftime(nowUserInfo.getWorkofftime());
        return this.jiaoJieInfo;
    }

    public UserInfo getUserInfo() {
        UserInfo nowUserInfo = this.presenter.getNowUserInfo();
        LogUtils.e(TAG, "getUserInfo: " + JSON.toJSONString(nowUserInfo));
        return nowUserInfo;
    }
}
