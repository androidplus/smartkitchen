package com.smart.kitchen.smartkitchen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.SearchActivity;
import com.smart.kitchen.smartkitchen.entitys.VipInfo;
import com.smart.kitchen.smartkitchen.mvp.presenter.AddVipPresenter;
import com.smart.kitchen.smartkitchen.mvp.view.AddVipView;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.Toasts;

public class AddVipFragment extends BaseFragment implements AddVipView {
    private DialogUtils dialogUtils;
    private TextView mBtnCancle;
    private TextView mBtnConfirm;
    private EditText mVipBirth;
    private EditText mVipCard;
    private EditText mVipExtras;
    private EditText mVipName;
    private EditText mVipPhone;
    private EditText mVipSite;
    private AddVipPresenter presenter;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.dialog_vip_add, null);
    }

    protected void initView(View view) {
        this.mVipCard = (EditText) view.findViewById(R.id.vip_card);
        this.mVipName = (EditText) view.findViewById(R.id.vip_name);
        this.mVipPhone = (EditText) view.findViewById(R.id.vip_phone);
        this.mVipBirth = (EditText) view.findViewById(R.id.vip_birth);
        this.mVipExtras = (EditText) view.findViewById(R.id.vip_extras);
        this.mBtnConfirm = (TextView) view.findViewById(R.id.btn_confirm);
        this.mBtnCancle = (TextView) view.findViewById(R.id.btn_cancle);
        this.mVipSite = (EditText) view.findViewById(R.id.vip_site);
    }

    protected void initEvent() {
        this.presenter = new AddVipPresenter(this, this.context);
        this.dialogUtils = new DialogUtils(getActivity());
        this.mBtnConfirm.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AddVipFragment.this.getVipInfo() != null) {
                    AddVipFragment.this.presenter.add();
                }
            }
        });
        this.mBtnCancle.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AddVipFragment.this.getActivity().finish();
            }
        });
    }

    protected void initData() {
    }

    public VipInfo getVipInfo() {
        if (Contants.isEmpty(getVipCard())) {
            Toasts.showShort(this.context, (CharSequence) "账号不能为空");
            return null;
        } else if (Contants.isEmpty(getVipName())) {
            Toasts.showShort(this.context, (CharSequence) "姓名不能为空");
            return null;
        } else if (Contants.isMobileNO(getVipPhone())) {
            VipInfo vipInfo = new VipInfo();
            vipInfo.setUsername(getVipCard());
            vipInfo.setDisplayname(getVipName());
            vipInfo.setPhone(getVipPhone());
            vipInfo.setBirth(getVipBirth());
            vipInfo.setExtras(getVipExtras());
            return vipInfo;
        } else {
            Toasts.showShort(this.context, (CharSequence) "手机号不能为空");
            return null;
        }
    }

    public void onSuccess(final VipInfo vipInfo) {
        this.dialogUtils.showConfirm(this.mBtnConfirm, "会员添加成功，是否立即充值？", new String[]{"充值", "取消"}, new DialogUtils.OnClickListener() {
            public void onClick(Object obj) {
                ((SearchActivity) AddVipFragment.this.getActivity()).showVipPayFragment(vipInfo);
            }
        }, new DialogUtils.OnClickListener() {
            public void onClick(Object obj) {
                AddVipFragment.this.getActivity().finish();
            }
        });
    }

    public void onFail() {
    }

    public String getVipCard() {
        return this.mVipCard.getText().toString().trim();
    }

    public String getVipName() {
        return this.mVipName.getText().toString().trim();
    }

    public String getVipPhone() {
        return this.mVipPhone.getText().toString().trim();
    }

    public String getVipBirth() {
        return this.mVipBirth.getText().toString().trim();
    }

    public String getVipExtras() {
        return this.mVipExtras.getText().toString().trim();
    }

    public String getVipSite() {
        return this.mVipSite.getText().toString().trim();
    }
}
