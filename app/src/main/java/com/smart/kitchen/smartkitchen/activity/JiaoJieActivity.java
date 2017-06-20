package com.smart.kitchen.smartkitchen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.BaseFragmentActivity;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.JiaoJieInfo;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;
import com.smart.kitchen.smartkitchen.mvp.presenter.JiaoJiePresenter;
import com.smart.kitchen.smartkitchen.mvp.view.JiaoJieView;
import com.smart.kitchen.smartkitchen.utils.AuthorityUtil;
import com.smart.kitchen.smartkitchen.utils.CalendarUtils;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;

public class JiaoJieActivity extends BaseFragmentActivity implements JiaoJieView {
    private EditText etExtra;
    private JiaoJieInfo jiaoJieInfo;
    private JiaoJiePresenter presenter;
    private TextView tvGuadanTotalCount;
    private TextView tvJiaojie;
    private TextView tvJiaojieQuit;
    private TextView tvOrderCount;
    private TextView tvOrderTotalMoney;
    private TextView tvRealMoney;
    private TextView tvSaleCount;
    private TextView tvTime;
    private TextView tvVipPay;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_jiao_jie);
        FinishActivity.add(this);
    }

    @Override
    protected void initView() {
        this.tvTime = (TextView) findViewById(R.id.tv_time);
        this.tvOrderCount = (TextView) findViewById(R.id.tv_order_count);
        this.tvOrderTotalMoney = (TextView) findViewById(R.id.tv_order_total_money);
        this.tvGuadanTotalCount = (TextView) findViewById(R.id.tv_guadan_total_count);
        this.tvVipPay = (TextView) findViewById(R.id.tv_vip_pay);
        this.tvSaleCount = (TextView) findViewById(R.id.tv_sale_count);
        this.tvRealMoney = (TextView) findViewById(R.id.tv_real_money);
        this.tvJiaojie = (TextView) findViewById(R.id.tv_jiaojie);
        this.etExtra = (EditText) findViewById(R.id.et_extra);
        this.tvJiaojieQuit = (TextView) findViewById(R.id.tv_jiaojie_quit);
    }

    @Override
    protected void initEvent() {
        this.presenter = new JiaoJiePresenter(this, this);
        this.tvJiaojie.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AuthorityUtil.getInstance().getRoleFlag() != 2) {
                    Toasts.show(JiaoJieActivity.this.context, "您没有权限");
                } else {
                    JiaoJieActivity.this.presenter.updateJiaoJie();
                }
            }
        });
        this.tvJiaojieQuit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SPUtils.setUserinfo(JiaoJieActivity.this.context, SPUtils.NEED_JIAOJIE, Boolean.valueOf(true));
                SPUtils.setUserinfo(JiaoJieActivity.this.context, SPUtils.IS_LOGIN, Boolean.valueOf(false));
                JiaoJieActivity.this.startActivity(new Intent(JiaoJieActivity.this.context, LoginActivity.class));
                AuthorityUtil.setInstance();
                JiaoJieActivity.this.finish();
            }
        });
    }

    private void complete() {
        SPUtils.setUserinfo(this.context, SPUtils.NEED_JIAOJIE, Boolean.valueOf(false));
        startActivity(new Intent(this.context, MainActivity.class));
        finish();
    }

    @Override
    protected void initData() {
        this.tvTime.setText(CalendarUtils.getNowFullTime());
        this.presenter.showInfoById();
    }

    @Override
    public void onSuccess(String str) {
        complete();
    }

    @Override
    public void onFail() {
    }

    @Override
    public void showInfo(JiaoJieInfo jiaoJieInfo) {
        this.jiaoJieInfo = jiaoJieInfo;
        this.tvOrderCount.setText("" + jiaoJieInfo.getOrdercount());
        this.tvOrderTotalMoney.setText("" + jiaoJieInfo.getTotalmoney());
        this.tvGuadanTotalCount.setText("" + jiaoJieInfo.getNonpayedorder());
        this.tvVipPay.setText("" + jiaoJieInfo.getCrmmoney());
        this.tvSaleCount.setText("" + jiaoJieInfo.getOrdercount());
        if (jiaoJieInfo.getMoney() == null) {
            this.tvRealMoney.setText("0");
        } else {
            this.tvRealMoney.setText("" + jiaoJieInfo.getMoney());
        }
    }

    @Override
    public JiaoJieInfo getJiaoJieInfo() {
        String trim = this.etExtra.getText().toString().trim();
        if (!Contants.isEmpty(trim)) {
            this.jiaoJieInfo.setMark(trim);
        }
        this.jiaoJieInfo.setJieid(Long.valueOf(Long.parseLong(this.presenter.getNowUserInfo().getUserid())));
        return this.jiaoJieInfo;
    }

    @Override
    public UserInfo getUserInfo() {
        return this.presenter.getBeforeUserInfo();
    }
}
