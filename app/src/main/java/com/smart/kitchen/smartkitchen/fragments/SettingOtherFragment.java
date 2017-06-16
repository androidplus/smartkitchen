package com.smart.kitchen.smartkitchen.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.SelectAreaAdapter;
import com.smart.kitchen.smartkitchen.entitys.TableArea;
import com.smart.kitchen.smartkitchen.utils.AppUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import com.smart.kitchen.smartkitchen.view.MyListView;
import com.smart.kitchen.smartkitchen.view.SettingSwitchButton;
import com.smart.kitchen.smartkitchen.view.SettingSwitchButton.OnChangeListener;
import java.util.ArrayList;
import java.util.List;

public class SettingOtherFragment extends BaseFragment {
    private List<TableArea> areaList;
    private boolean autopay;
    private boolean cancel;
    private boolean count;
    private EditText etNewOrderRefresh;
    private boolean haveOrder;
    private LinearLayout llSelect;
    private LinearLayout llShow;
    private MyListView lv;
    private boolean pay;
    private boolean payment;
    private SettingSwitchButton sbNotificationAutoPay;
    private SettingSwitchButton sbNotificationCancel;
    private SettingSwitchButton sbNotificationCount;
    private SettingSwitchButton sbNotificationHaveOrder;
    private SettingSwitchButton sbNotificationPay;
    private SettingSwitchButton sbNotificationWaimai;
    private SettingSwitchButton sbNotificationWarring;
    private SettingSwitchButton sbPaymentSucceed;
    private SettingSwitchButton sbTakeoutNew;
    private SelectAreaAdapter selectAreaAdapter;
    private boolean takeoutNew;
    private boolean takeoutreceiving;
    private TextView tvAreaShow;
    private TextView tvVersion;
    private boolean warning;

    public static SettingOtherFragment newInstance() {
        SettingOtherFragment settingOtherFragment = new SettingOtherFragment();
        settingOtherFragment.setArguments(new Bundle());
        return settingOtherFragment;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_setting_other, viewGroup, false);
    }

    protected void initView(View view) {
        this.sbNotificationHaveOrder = (SettingSwitchButton) view.findViewById(R.id.sb_notification_have_order);
        this.sbNotificationPay = (SettingSwitchButton) view.findViewById(R.id.sb_notification_pay);
        this.sbNotificationWarring = (SettingSwitchButton) view.findViewById(R.id.sb_notification_warring);
        this.sbNotificationCancel = (SettingSwitchButton) view.findViewById(R.id.sb_notification_cancel);
        this.sbNotificationCount = (SettingSwitchButton) view.findViewById(R.id.sb_notification_count);
        this.sbNotificationAutoPay = (SettingSwitchButton) view.findViewById(R.id.sb_notification_auto_pay);
        this.sbTakeoutNew = (SettingSwitchButton) view.findViewById(R.id.sb_takeout_new);
        this.sbPaymentSucceed = (SettingSwitchButton) view.findViewById(R.id.sb_payment_succeed);
        this.sbNotificationWaimai = (SettingSwitchButton) view.findViewById(R.id.sb_notification_waimai);
        this.tvVersion = (TextView) view.findViewById(R.id.tv_version);
        this.etNewOrderRefresh = (EditText) view.findViewById(R.id.et_new_order_refresh);
        this.tvAreaShow = (TextView) view.findViewById(R.id.tv_favorable_show);
        this.llShow = (LinearLayout) view.findViewById(R.id.ll_favorable);
        this.llSelect = (LinearLayout) view.findViewById(R.id.ll_favorable_select);
        this.lv = (MyListView) view.findViewById(R.id.lv_favorable_select);
    }

    protected void initData() {
        this.haveOrder = SPUtils.getUserinfos2(this.context, SPUtils.SETTING_NOTIFICATIONHAVEORDER);
        this.pay = SPUtils.getUserinfos2(this.context, SPUtils.SETTING_NOTIFICATIONPAY);
        this.warning = SPUtils.getUserinfos2(this.context, SPUtils.SETTING_NOTIFICATIONWARRING);
        this.cancel = SPUtils.getUserinfos2(this.context, SPUtils.SETTING_NOTIFICATIONCANCEL);
        this.count = SPUtils.getUserinfos2(this.context, SPUtils.SETTING_NOTIFICATIONCOUNT);
        this.autopay = SPUtils.getUserinfos2(this.context, SPUtils.SETTING_NOTIFICATIONAUTOPAY);
        this.takeoutNew = SPUtils.getUserinfos2(this.context, SPUtils.SETTING_TAKEOUTNEW);
        this.payment = SPUtils.getUserinfos2(this.context, SPUtils.SETTING_PAYMENTSUCCEED);
        this.takeoutreceiving = SPUtils.getUserinfos2(this.context, SPUtils.TAKEOUT_RECEIVING);
        this.sbNotificationHaveOrder.setText(this.haveOrder);
        this.sbNotificationPay.setText(this.pay);
        this.sbNotificationWarring.setText(this.warning);
        this.sbNotificationCancel.setText(this.cancel);
        this.sbNotificationCount.setText(this.count);
        this.sbNotificationAutoPay.setText(this.autopay);
        this.sbTakeoutNew.setText(this.takeoutNew);
        this.sbPaymentSucceed.setText(this.payment);
        this.sbNotificationWaimai.setText(this.takeoutreceiving);
        setAnimal(this.sbNotificationHaveOrder, this.haveOrder);
        setAnimal(this.sbNotificationPay, this.pay);
        setAnimal(this.sbNotificationWarring, this.warning);
        setAnimal(this.sbNotificationCancel, this.cancel);
        setAnimal(this.sbNotificationCount, this.count);
        setAnimal(this.sbNotificationAutoPay, this.autopay);
        setAnimal(this.sbTakeoutNew, this.takeoutNew);
        setAnimal(this.sbPaymentSucceed, this.payment);
        setAnimal(this.sbNotificationWaimai, this.takeoutreceiving);
        this.tvVersion.setText("版本号：" + AppUtils.getVersionName(this.context));
        if (!TextUtils.isEmpty(SPUtils.getUserinfo(this.context, SPUtils.SETTING_AREA))) {
            this.tvAreaShow.setText(SPUtils.getUserinfo(this.context, SPUtils.SETTING_AREA) + "");
        }
        if (TextUtils.isEmpty(SPUtils.getUserinfo(this.context, SPUtils.SETTING_NEW_ORDER_REFRESH_TIME))) {
            this.etNewOrderRefresh.setText("0");
        } else {
            this.etNewOrderRefresh.setText(SPUtils.getUserinfo(this.context, SPUtils.SETTING_NEW_ORDER_REFRESH_TIME) + "");
        }
        this.llSelect.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (SettingOtherFragment.this.llShow.getVisibility() == 8) {
                    SettingOtherFragment.this.llShow.setVisibility(0);
                } else {
                    SettingOtherFragment.this.llShow.setVisibility(8);
                }
            }
        });
        this.lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                SettingOtherFragment.this.tvAreaShow.setText(((TableArea) SettingOtherFragment.this.areaList.get(i)).getArea_name());
                SettingOtherFragment.this.tvAreaShow.setTextColor(SettingOtherFragment.this.getResources().getColor(R.color.black));
                SettingOtherFragment.this.llShow.setVisibility(4);
                SPUtils.setUserinfo(SettingOtherFragment.this.context, SPUtils.SETTING_AREA, ((TableArea) SettingOtherFragment.this.areaList.get(i)).getId() + "");
            }
        });
    }

    private void setAnimal(SettingSwitchButton settingSwitchButton, boolean z) {
        if (z) {
            settingSwitchButton.startAnim();
        }
    }

    protected void initEvent() {
        this.sbNotificationHaveOrder.setOnChangeListener(new OnChangeListener() {
            public void onChange(boolean z) {
                SPUtils.setUserinfo(SettingOtherFragment.this.context, SPUtils.SETTING_NOTIFICATIONHAVEORDER, Boolean.valueOf(z));
            }
        });
        this.sbNotificationPay.setOnChangeListener(new OnChangeListener() {
            public void onChange(boolean z) {
                SPUtils.setUserinfo(SettingOtherFragment.this.context, SPUtils.SETTING_NOTIFICATIONPAY, Boolean.valueOf(z));
            }
        });
        this.sbNotificationWarring.setOnChangeListener(new OnChangeListener() {
            public void onChange(boolean z) {
                SPUtils.setUserinfo(SettingOtherFragment.this.context, SPUtils.SETTING_NOTIFICATIONWARRING, Boolean.valueOf(z));
            }
        });
        this.sbNotificationCancel.setOnChangeListener(new OnChangeListener() {
            public void onChange(boolean z) {
                SPUtils.setUserinfo(SettingOtherFragment.this.context, SPUtils.SETTING_NOTIFICATIONCANCEL, Boolean.valueOf(z));
            }
        });
        this.sbNotificationCount.setOnChangeListener(new OnChangeListener() {
            public void onChange(boolean z) {
                SPUtils.setUserinfo(SettingOtherFragment.this.context, SPUtils.SETTING_NOTIFICATIONCOUNT, Boolean.valueOf(z));
            }
        });
        this.sbNotificationAutoPay.setOnChangeListener(new OnChangeListener() {
            public void onChange(boolean z) {
                SPUtils.setUserinfo(SettingOtherFragment.this.context, SPUtils.SETTING_NOTIFICATIONAUTOPAY, Boolean.valueOf(z));
            }
        });
        this.sbTakeoutNew.setOnChangeListener(new OnChangeListener() {
            public void onChange(boolean z) {
                SPUtils.setUserinfo(SettingOtherFragment.this.context, SPUtils.SETTING_TAKEOUTNEW, Boolean.valueOf(z));
            }
        });
        this.sbPaymentSucceed.setOnChangeListener(new OnChangeListener() {
            public void onChange(boolean z) {
                SPUtils.setUserinfo(SettingOtherFragment.this.context, SPUtils.SETTING_PAYMENTSUCCEED, Boolean.valueOf(z));
            }
        });
        this.sbNotificationWaimai.setOnChangeListener(new OnChangeListener() {
            public void onChange(boolean z) {
                SPUtils.setUserinfo(SettingOtherFragment.this.context, SPUtils.TAKEOUT_RECEIVING, Boolean.valueOf(z));
            }
        });
        this.etNewOrderRefresh.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                SPUtils.setUserinfo(SettingOtherFragment.this.context, SPUtils.SETTING_NEW_ORDER_REFRESH_TIME, editable.toString());
            }
        });
    }

    public void upDateArea(List<TableArea> list) {
        if (list != null) {
            if (this.areaList == null) {
                this.areaList = new ArrayList();
            }
            this.areaList.clear();
            this.areaList.addAll(list);
            if (this.selectAreaAdapter == null) {
                this.selectAreaAdapter = new SelectAreaAdapter(this.context, this.areaList);
                this.lv.setAdapter(this.selectAreaAdapter);
            } else {
                this.selectAreaAdapter.notifyDataSetChanged();
            }
            if (!TextUtils.isEmpty(SPUtils.getUserinfo(this.context, SPUtils.SETTING_AREA)) && this.areaList != null) {
                for (int i = 0; i < this.areaList.size(); i++) {
                    if (String.valueOf(((TableArea) this.areaList.get(i)).getId()).equals(SPUtils.getUserinfo(this.context, SPUtils.SETTING_AREA))) {
                        this.tvAreaShow.setText(((TableArea) this.areaList.get(i)).getArea_name());
                        return;
                    }
                }
            }
        }
    }
}
