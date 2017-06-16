package com.smart.kitchen.smartkitchen.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.MemberTop;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;
import com.smart.kitchen.smartkitchen.entitys.VipInfo;
import com.smart.kitchen.smartkitchen.mvp.presenter.VipPayPresenter;
import com.smart.kitchen.smartkitchen.mvp.view.VipPayView;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils.onScanLinstener;
import com.smart.kitchen.smartkitchen.utils.ArithmeticUtils;
import com.smart.kitchen.smartkitchen.utils.CashierInputFilter;
import com.smart.kitchen.smartkitchen.utils.DoubleUtils;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import java.util.List;

public class VipPayFragment extends BaseFragment implements OnClickListener, VipPayView {
    private static final String TAG = "VipPayFragment";
    private ImageView ivStyleAlipay;
    private ImageView ivStyleChizhika;
    private ImageView ivStyleMoney;
    private ImageView ivStyleWx;
    private ImageView ivStyleYinlain;
    private TextView mBtn_cancle;
    private TextView mBtn_confirm;
    private TextView mExtraMoney;
    private EditText mIncomeMoney;
    private LinearLayout mLlStyleAlipay;
    private LinearLayout mLlStyleChizhika;
    private LinearLayout mLlStyleMoney;
    private LinearLayout mLlStyleWx;
    private LinearLayout mLlStyleYinlain;
    private EditText mRestMoney;
    private LinearLayout mSelectLL;
    private TextView mSelectText;
    private EditText mTotalMoney;
    private MemberTop memberTop;
    private VipPayPresenter payPresenter;
    private List<UserInfo> salesList;
    private int stylePay = -1;
    private UserInfo userInfo;
    private VipInfo vipInfo;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.dialog_vip_pay, null);
    }

    public void setVipInfo(VipInfo vipInfo) {
        this.vipInfo = vipInfo;
    }

    protected void initView(View view) {
        this.mTotalMoney = (EditText) view.findViewById(R.id.totalMoney);
        this.mExtraMoney = (TextView) view.findViewById(R.id.extraMoney);
        this.mIncomeMoney = (EditText) view.findViewById(R.id.incomeMoney);
        this.mRestMoney = (EditText) view.findViewById(R.id.restMoney);
        this.mBtn_confirm = (TextView) view.findViewById(R.id.btn_confirm);
        this.mBtn_cancle = (TextView) view.findViewById(R.id.btn_cancle);
        this.mSelectLL = (LinearLayout) view.findViewById(R.id.select_ll);
        this.mSelectText = (TextView) view.findViewById(R.id.select_text);
        this.mLlStyleMoney = (LinearLayout) view.findViewById(R.id.ll_style_money);
        this.mLlStyleAlipay = (LinearLayout) view.findViewById(R.id.ll_style_alipay);
        this.mLlStyleWx = (LinearLayout) view.findViewById(R.id.ll_style_wx);
        this.mLlStyleChizhika = (LinearLayout) view.findViewById(R.id.ll_style_chizhika);
        this.mLlStyleYinlain = (LinearLayout) view.findViewById(R.id.ll_style_yinlain);
        this.ivStyleMoney = (ImageView) view.findViewById(R.id.iv_style_money);
        this.ivStyleAlipay = (ImageView) view.findViewById(R.id.iv_style_alipay);
        this.ivStyleWx = (ImageView) view.findViewById(R.id.iv_style_wx);
        this.ivStyleChizhika = (ImageView) view.findViewById(R.id.iv_style_chizhika);
        this.ivStyleYinlain = (ImageView) view.findViewById(R.id.iv_style_yinlain);
        setInputEdit();
        this.mIncomeMoney.addTextChangedListener(new TextWatcher() {
            Double temp = Double.valueOf(VipPayFragment.this.getTotalMoney());
            Double tempS;

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (new DoubleUtils().pandun(editable.toString()) && editable.toString().length() > 0 && !".".equals(editable.toString().substring(0, 1))) {
                    this.tempS = Double.valueOf(editable.toString());
                    if (this.tempS.doubleValue() > this.temp.doubleValue()) {
                        VipPayFragment.this.mRestMoney.setText(ArithmeticUtils.sub(editable.toString(), VipPayFragment.this.getTotalMoney(), 2));
                    } else {
                        VipPayFragment.this.mRestMoney.setText("0");
                    }
                }
            }
        });
        this.memberTop = new MemberTop();
        this.mTotalMoney.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View view, boolean z) {
                if (!z) {
                    if (TextUtils.isEmpty(VipPayFragment.this.getTotalMoney()) || Double.valueOf(VipPayFragment.this.getTotalMoney()).doubleValue() <= 0.0d) {
                        VipPayFragment.this.mTotalMoney.setText("0");
                    } else {
                        VipPayFragment.this.payPresenter.memberTop(SPUtils.getUserinfo(VipPayFragment.this.context, SPUtils.STORE_ID), Double.valueOf(VipPayFragment.this.getTotalMoney()));
                    }
                }
            }
        });
        this.mTotalMoney.addTextChangedListener(new TextWatcher() {
            String sTemp;

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                this.sTemp = charSequence.toString();
            }

            public void afterTextChanged(Editable editable) {
                if (new DoubleUtils().pandun(editable.toString()) && editable.toString().length() > 1) {
                    if ("0".equals(this.sTemp.substring(0, 1)) && "0".equals(this.sTemp.substring(1, 2))) {
                        VipPayFragment.this.mIncomeMoney.setText(editable.toString().substring(1, editable.length()));
                    }
                    if (!".".equals(this.sTemp.substring(0, 1)) && !".".equals(this.sTemp.substring(1, 2)) && Double.valueOf(this.sTemp.substring(0, 2)).doubleValue() < 10.0d) {
                        VipPayFragment.this.mIncomeMoney.setText(editable.toString().substring(1, editable.length()));
                    }
                }
            }
        });
        this.dialogUtils.setScanListener(new onScanLinstener() {
            public void scan(String str, String str2) {
                LogUtils.e(VipPayFragment.TAG, "scan: " + str);
                if ("1".equals(str2)) {
                    if (VipPayFragment.this.userInfo == null) {
                        VipPayFragment.this.payPresenter.alipayScanCode(str, Double.valueOf(VipPayFragment.this.getTotalMoney()), SPUtils.getUserinfo(VipPayFragment.this.context, SPUtils.STORE_ID), VipPayFragment.this.vipInfo.getId(), VipPayFragment.this.memberTop.getActiveid(), null);
                        return;
                    }
                    VipPayFragment.this.payPresenter.alipayScanCode(str, Double.valueOf(VipPayFragment.this.getTotalMoney()), SPUtils.getUserinfo(VipPayFragment.this.context, SPUtils.STORE_ID), VipPayFragment.this.vipInfo.getId(), VipPayFragment.this.memberTop.getActiveid(), VipPayFragment.this.userInfo.getUserid());
                } else if (!"2".equals(str2)) {
                } else {
                    if (VipPayFragment.this.userInfo == null) {
                        VipPayFragment.this.payPresenter.wxScanCode(str, Double.valueOf(VipPayFragment.this.getTotalMoney()), SPUtils.getUserinfo(VipPayFragment.this.context, SPUtils.STORE_ID), VipPayFragment.this.vipInfo.getId(), VipPayFragment.this.memberTop.getActiveid(), null);
                        return;
                    }
                    VipPayFragment.this.payPresenter.wxScanCode(str, Double.valueOf(VipPayFragment.this.getTotalMoney()), SPUtils.getUserinfo(VipPayFragment.this.context, SPUtils.STORE_ID), VipPayFragment.this.vipInfo.getId(), VipPayFragment.this.memberTop.getActiveid(), VipPayFragment.this.userInfo.getUserid());
                }
            }
        });
    }

    protected void initEvent() {
        this.payPresenter = new VipPayPresenter(this.context, this, this.progressDialog);
        this.payPresenter.querySales(SPUtils.getUserinfo(this.context, SPUtils.STORE_ID));
        this.mBtn_confirm.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!new DoubleUtils().pandun(VipPayFragment.this.getIncomeMoney())) {
                    Toasts.show(VipPayFragment.this.context, "请输入正确的金额");
                } else if (TextUtils.isEmpty(VipPayFragment.this.getIncomeMoney())) {
                    Toasts.show(VipPayFragment.this.context, "请输入支付金额");
                } else if (!TextUtils.isEmpty(VipPayFragment.this.getTotalMoney()) && Double.valueOf(VipPayFragment.this.getTotalMoney()).doubleValue() > 0.0d && ArithmeticUtils.compare(VipPayFragment.this.getIncomeMoney(), VipPayFragment.this.getTotalMoney()) != -1) {
                    if (VipPayFragment.this.stylePay == -1) {
                        Toasts.show(VipPayFragment.this.context, "请选择支付方式");
                    } else {
                        VipPayFragment.this.payStyle(VipPayFragment.this.stylePay);
                    }
                }
            }
        });
        this.mSelectLL.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VipPayFragment.this.dialogUtils.showDaoGou(view, VipPayFragment.this.salesList, new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                        VipPayFragment.this.mSelectText.setText(((UserInfo) VipPayFragment.this.salesList.get(i)).getRealname());
                        VipPayFragment.this.userInfo = (UserInfo) VipPayFragment.this.salesList.get(i);
                    }
                });
            }
        });
        this.mBtn_cancle.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                VipPayFragment.this.getActivity().finish();
            }
        });
        this.mLlStyleMoney.setOnClickListener(this);
        this.mLlStyleAlipay.setOnClickListener(this);
        this.mLlStyleWx.setOnClickListener(this);
        this.mLlStyleChizhika.setOnClickListener(this);
        this.mLlStyleYinlain.setOnClickListener(this);
    }

    protected void initData() {
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_style_money:
                changeBackground(0);
                return;
            case R.id.ll_style_alipay:
                changeBackground(1);
                return;
            case R.id.ll_style_wx:
                changeBackground(2);
                return;
            case R.id.ll_style_chizhika:
                changeBackground(3);
                return;
            case R.id.ll_style_yinlain:
                changeBackground(4);
                return;
            default:
                return;
        }
    }

    public void onPaySucess(int i, String str) {
        if (i == 0) {
            this.memberTop = (MemberTop) JSON.parseObject(str, new TypeReference<MemberTop>() {
            }, new Feature[0]);
            this.mExtraMoney.setVisibility(0);
            this.mExtraMoney.setText(this.memberTop.getText());
        } else if (i == 1) {
            this.salesList = (List) JSON.parseObject(str, new TypeReference<List<UserInfo>>() {
            }, new Feature[0]);
        } else if (i == 4 || i == 5 || i == 6) {
            this.dialogUtils.showConfirm(this.mBtn_confirm, "会员充值成功，是否立即点餐", new String[]{"点餐", "取消"}, new DialogUtils.OnClickListener() {
                public void onClick(Object obj) {
                    Intent intent = new Intent();
                    intent.putExtra("vipinfo", VipPayFragment.this.vipInfo);
                    VipPayFragment.this.getActivity().setResult(-1, intent);
                    VipPayFragment.this.getActivity().finish();
                }
            }, new DialogUtils.OnClickListener() {
                public void onClick(Object obj) {
                    VipPayFragment.this.getActivity().finish();
                }
            });
        } else if (i != 2 && i == 3) {
        }
    }

    public void onFails(int i, String str) {
        if (i != 0) {
            Toasts.show(this.context, "提交失败,请重新提交");
        }
    }

    public String getTotalMoney() {
        if (TextUtils.isEmpty(this.mTotalMoney.getText().toString().trim())) {
            return "0";
        }
        return this.mTotalMoney.getText().toString().trim();
    }

    public String getIncomeMoney() {
        return this.mIncomeMoney.getText().toString().trim();
    }

    public String getRestMoney() {
        return this.mRestMoney.getText().toString().trim();
    }

    public void setInputEdit() {
        InputFilter[] inputFilterArr = new InputFilter[]{new CashierInputFilter()};
        this.mIncomeMoney.setFilters(inputFilterArr);
        this.mTotalMoney.setFilters(inputFilterArr);
    }

    public int getPayStyle() {
        return this.stylePay;
    }

    public void changeBackground(int i) {
        this.stylePay = i;
        this.ivStyleMoney.setBackgroundResource(R.drawable.corner_white);
        this.ivStyleAlipay.setBackgroundResource(R.drawable.corner_white);
        this.ivStyleWx.setBackgroundResource(R.drawable.corner_white);
        this.ivStyleChizhika.setBackgroundResource(R.drawable.corner_white);
        this.ivStyleYinlain.setBackgroundResource(R.drawable.corner_white);
        switch (this.stylePay) {
            case 0:
                this.ivStyleMoney.setBackgroundResource(R.drawable.corner_blue);
                return;
            case 1:
                this.ivStyleAlipay.setBackgroundResource(R.drawable.corner_blue);
                return;
            case 2:
                this.ivStyleWx.setBackgroundResource(R.drawable.corner_blue);
                return;
            case 3:
                this.ivStyleChizhika.setBackgroundResource(R.drawable.corner_blue);
                return;
            case 4:
                this.ivStyleYinlain.setBackgroundResource(R.drawable.corner_blue);
                return;
            default:
                return;
        }
    }

    private void payStyle(int i) {
        switch (i) {
            case 0:
                if (this.userInfo == null) {
                    this.payPresenter.cashMoney(Double.valueOf(getTotalMoney()), SPUtils.getUserinfo(this.context, SPUtils.STORE_ID), this.vipInfo.getId(), this.memberTop.getActiveid(), null);
                    return;
                } else {
                    this.payPresenter.cashMoney(Double.valueOf(getTotalMoney()), SPUtils.getUserinfo(this.context, SPUtils.STORE_ID), this.vipInfo.getId(), this.memberTop.getActiveid(), this.userInfo.getUserid());
                    return;
                }
            case 1:
                this.dialogUtils.showScanVIP(this.mBtn_confirm, "1");
                return;
            case 2:
                this.dialogUtils.showScanVIP(this.mBtn_confirm, "2");
                return;
            default:
                return;
        }
    }
}
