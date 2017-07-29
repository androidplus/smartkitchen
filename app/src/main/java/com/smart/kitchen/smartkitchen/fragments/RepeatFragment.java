package com.smart.kitchen.smartkitchen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.LoginActivity;
import com.smart.kitchen.smartkitchen.activity.MainActivity;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.entitys.JiaoJieInfo;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;
import com.smart.kitchen.smartkitchen.mvp.presenter.JiaoJiePresenter;
import com.smart.kitchen.smartkitchen.mvp.view.JiaoJieView;
import com.smart.kitchen.smartkitchen.service.ClientSocketService;
import com.smart.kitchen.smartkitchen.utils.AuthorityUtil;
import com.smart.kitchen.smartkitchen.utils.CalendarUtils;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;

public class RepeatFragment extends BaseFragment implements JiaoJieView {
    private static final String TAG = "RepeatFragment";
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

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.dialog_jiaojie, null);
    }

    protected void initView(View view) {
        this.llContainer = (LinearLayout) view.findViewById(R.id.ll_container_jiaojie);
        this.llContainer.setBackgroundColor(this.context.getResources().getColor(R.color.white));
        this.tvTime = (TextView) view.findViewById(R.id.tv_time);
        this.tvOrderCount = (TextView) view.findViewById(R.id.tv_order_count);
        this.tvOrderTotalMoney = (TextView) view.findViewById(R.id.tv_order_total_money);
        this.tvGuadanTotalCount = (TextView) view.findViewById(R.id.tv_guadan_total_count);
        this.tvVipPay = (TextView) view.findViewById(R.id.tv_vip_pay);
        this.tvSaleCount = (TextView) view.findViewById(R.id.tv_sale_count);
        this.tvRealMoney = (TextView) view.findViewById(R.id.tv_real_money);
        this.etExtra = (EditText) view.findViewById(R.id.et_extra);
        this.tvJiaojie = (TextView) view.findViewById(R.id.tv_jiaojie);
        this.tvQuit = (TextView) view.findViewById(R.id.tv_jiaojie_quit);
    }

    protected void initEvent() {
        this.presenter = new JiaoJiePresenter(this, this.context);
        this.tvJiaojie.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new UserInfoDaoManager().offWorkUserInfo();
                RepeatFragment.this.presenter.doJiaoJie();
            }
        });
        this.tvQuit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ((MainActivity) RepeatFragment.this.getActivity()).fm();
            }
        });
    }

    protected void initData() {
    }

    public void onResume() {
        super.onResume();
        String nowFullTime = CalendarUtils.getNowFullTime();
        this.tvTime.setText(nowFullTime);
        this.presenter.showInfo(nowFullTime);
    }

    public void onSuccess(String str) {
        SPUtils.setUserinfo(this.context, SPUtils.NEED_JIAOJIE, Boolean.valueOf(true));
        SPUtils.setUserinfo(this.context, SPUtils.ID_JIAOJIE, str);
        SPUtils.setUserinfo(this.context, SPUtils.IS_LOGIN, Boolean.valueOf(false));
        SPUtils.remove(this.context, SPUtils.SETTING_AREA);
        this.context.stopService(new Intent(this.context, ClientSocketService.class));
        Intent intent = new Intent(this.context, LoginActivity.class);
        AuthorityUtil.setInstance();
        startActivity(intent);
        getActivity().finish();
    }

    public void onFail() {
    }

    public void showInfo(JiaoJieInfo jiaoJieInfo) {
        this.jiaoJieInfo = jiaoJieInfo;
        this.tvOrderCount.setText("" + jiaoJieInfo.getNonpayedorder());
        this.tvOrderTotalMoney.setText("" + jiaoJieInfo.getTotalmoney());
        this.tvGuadanTotalCount.setText("" + jiaoJieInfo.getNonpayedorder());
        this.tvVipPay.setText("" + jiaoJieInfo.getCrmmoney());
        this.tvSaleCount.setText("" + jiaoJieInfo.getOrdercount());
        if (jiaoJieInfo.getRestmoney() == null) {
            this.tvRealMoney.setText("0");
        } else {
            this.tvRealMoney.setText("" + jiaoJieInfo.getRestmoney());
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
