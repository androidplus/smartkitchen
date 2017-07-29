package com.smart.kitchen.smartkitchen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.SearchActivity;
import com.smart.kitchen.smartkitchen.entitys.VipInfo;

public class VIPInfoFragment extends BaseFragment {
    private static final String TAG = "VIPInfoFragment";
    private boolean isShowed = false;
    private TextView mAccount;
    private TextView mAdd;
    private TextView mAddress;
    private TextView mBirth;
    private TextView mCancle;
    private ImageView mIvClose;
    private TextView mName;
    private TextView mPay;
    private TextView mPhone;
    private SearchActivity sear;
    private VipInfo vipInfo;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.isShowed = false;
        return layoutInflater.inflate(R.layout.dialog_vip_info, null);
    }

    public void showVipInfo(VipInfo vipInfo) {
        this.vipInfo = vipInfo;
        if (this.isShowed) {
            initData();
        }
    }

    protected void initView(View view) {
        this.mName = (TextView) view.findViewById(R.id.name);
        this.mAccount = (TextView) view.findViewById(R.id.account);
        this.mPhone = (TextView) view.findViewById(R.id.phone);
        this.mBirth = (TextView) view.findViewById(R.id.birth);
        this.mAddress = (TextView) view.findViewById(R.id.address);
        this.mPay = (TextView) view.findViewById(R.id.pay);
        this.mAdd = (TextView) view.findViewById(R.id.add);
        this.mCancle = (TextView) view.findViewById(R.id.cancle);
        this.isShowed = true;
    }

    protected void initEvent() {
        this.sear = (SearchActivity) getActivity();
        this.mPay.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VIPInfoFragment.this.sear.showVipPayFragment(VIPInfoFragment.this.vipInfo);
            }
        });
        this.mAdd.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("vipinfo", VIPInfoFragment.this.vipInfo);
                VIPInfoFragment.this.getActivity().setResult(-1, intent);
                VIPInfoFragment.this.getActivity().finish();
            }
        });
        this.mCancle.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VIPInfoFragment.this.sear.hide();
            }
        });
    }

    protected void initData() {
        this.mName.setText(this.vipInfo.getDisplayname());
        this.mAccount.setText(this.vipInfo.getUsername());
        this.mPhone.setText(this.vipInfo.getPhone());
        this.mBirth.setText(this.vipInfo.getBirth());
    }
}
