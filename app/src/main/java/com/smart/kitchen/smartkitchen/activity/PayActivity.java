package com.smart.kitchen.smartkitchen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.BaseFragmentActivity;
import com.smart.kitchen.smartkitchen.BuildConfig;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.DiscountCouponAdapter;
import com.smart.kitchen.smartkitchen.entitys.Conpon;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.entitys.OrderPayStatus;
import com.smart.kitchen.smartkitchen.entitys.VipConpon;
import com.smart.kitchen.smartkitchen.mvp.presenter.PayPresenter;
import com.smart.kitchen.smartkitchen.mvp.view.PayView;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils.onCouponLinstener;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils.onScanLinstener;
import com.smart.kitchen.smartkitchen.popwindow.PayDialogUtils;
import com.smart.kitchen.smartkitchen.print.PrintUtil;
import com.smart.kitchen.smartkitchen.service.ClientSocketService;
import com.smart.kitchen.smartkitchen.utils.ArithmeticUtils;
import com.smart.kitchen.smartkitchen.utils.CashierInputFilter;
import com.smart.kitchen.smartkitchen.utils.DiscountInputFilter;
import com.smart.kitchen.smartkitchen.utils.DoubleUtils;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import java.util.ArrayList;
import java.util.List;

public class PayActivity extends BaseFragmentActivity implements OnClickListener, PayView {
    private static final String TAG = "PayActivity";
    public int NUMBER = 1;
    private boolean OrderPay = false;
    public String PayTypeTAG = "1";
    public CheckBox ckDiscount;
    private CheckBox ckFavorable;
    private CheckBox ckIntegral;
    public CheckBox ckPay;
    private CheckBox ckVip;
    private Conpon conpon;
    private List<Conpon> conponList;
    private boolean couponTag = false;
    private Double differMoney = Double.valueOf(0.0d);
    private DiscountCouponAdapter discountCouponAdapter;
    public List<View> discountList = new ArrayList();
    public EditText etAggregatePay;
    public EditText etChangePay;
    public EditText etCombinationPay1;
    public EditText etCombinationPay2;
    public EditText etDiscountPay;
    private EditText etFavorableNumber;
    public EditText etFavorableParticulars;
    private TextView etFavorableVip;
    public EditText etIncomePay;
    private EditText etVipInfo;
    private boolean favorableBoolean = false;
    private String favorableMoney = "";
    private boolean integralBoolean = false;
    public ImageView ivAlipayPay;
    public ImageView ivCashPay;
    public ImageView ivCombinationPay1;
    public ImageView ivCombinationPay2;
    public ImageView ivCuzhikaPay;
    public ImageView ivFavorableNumber;
    public ImageView ivFavorableSearch;
    public ImageView ivObligate1;
    public ImageView ivObligate2;
    public ImageView ivSaoYiSao;
    public ImageView ivUnionpayPay;
    private ImageView ivVipSearch;
    public ImageView ivWxpayPay;
    private String jfTemp = "";
    public List<View> listConpon = new ArrayList();
    public List<View> listDiscount = new ArrayList();
    private List<OrderGoods> listGoods;
    public List<View> listVip = new ArrayList();
    public LinearLayout llCombinationPay;
    public LinearLayout llDiscount;
    public LinearLayout llDiscountPay;
    public LinearLayout llFavorable;
    private LinearLayout llFavorableCk;
    public LinearLayout llFavorableSelect;
    private LinearLayout llPay;
    private LinearLayout llVip;
    private LinearLayout llZhekou;
    private ListView lvFavorableSelect;
    private int mPapertype = 0;
    public String money;
    private OrderInfo orderInfo;
    private PayDialogUtils payDialogUtils;
    public List<View> paylist = new ArrayList();
    private PayPresenter presenter;
    private PrintUtil printUtil = PrintUtil.getInstance();
    private RelativeLayout rlVipSearch;
    private boolean servingTag = true;
    private TextView tvCouponRecord;
    public TextView tvDiscount;
    public TextView tvDiscount1;
    public TextView tvDiscount10;
    public TextView tvDiscount2;
    public TextView tvDiscount3;
    public TextView tvDiscount4;
    public TextView tvDiscount5;
    public TextView tvDiscount6;
    public TextView tvDiscount7;
    public TextView tvDiscount8;
    public TextView tvDiscount9;
    public TextView tvFavorable;
    private TextView tvFavorableOk;
    public TextView tvFavorablePay;
    private TextView tvIntegral;
    private TextView tvMemberRecord;
    public TextView tvMoney;
    private TextView tvQuery;
    public TextView tvQuitPay;
    public TextView tvSettlePay;
    public TextView tvTipIsnot;
    private TextView tvTipIsnotVip;
    private TextView tvVip;
    private TextView tvdiscountRecord;
    private boolean useBoolean = false;
    private boolean userVip = false;
    private boolean vipBoolean = false;
    private VipConpon vipConpon;
    private Conpon vipSelete;
    private boolean vipTag = false;
    private String vipUserId = "";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_pay);
        FinishActivity.add(this);
    }

    @Override
    protected void initView() {
        this.tvFavorablePay = (TextView) findViewById(R.id.tv_favorable_pay);
        this.etAggregatePay = (EditText) findViewById(R.id.et_aggregate_pay);
        this.etIncomePay = (EditText) findViewById(R.id.et_income_pay);
        this.etChangePay = (EditText) findViewById(R.id.et_change_pay);
        this.ivCashPay = (ImageView) findViewById(R.id.iv_cash_pay);
        this.ivAlipayPay = (ImageView) findViewById(R.id.iv_alipay_pay);
        this.ivWxpayPay = (ImageView) findViewById(R.id.iv_wxpay_pay);
        this.ivCuzhikaPay = (ImageView) findViewById(R.id.iv_cuzhika_pay);
        this.ivUnionpayPay = (ImageView) findViewById(R.id.iv_unionpay_pay);
        this.ivSaoYiSao = (ImageView) findViewById(R.id.iv_saoyisao_pay);
        this.ivObligate1 = (ImageView) findViewById(R.id.iv_obligate1_pay);
        this.ivObligate2 = (ImageView) findViewById(R.id.iv_obligate2_pay);
        this.llCombinationPay = (LinearLayout) findViewById(R.id.ll_combination_pay);
        this.llDiscountPay = (LinearLayout) findViewById(R.id.ll_zhekou_pay);
        this.tvDiscount = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_zhekou);
        this.tvFavorable = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_favorable);
        this.llFavorable = (LinearLayout) findViewById(R.id.include_pay).findViewById(R.id.ll_favorable);
        this.llDiscount = (LinearLayout) findViewById(R.id.include_pay).findViewById(R.id.ll_zhekou);
        this.etFavorableNumber = (EditText) findViewById(R.id.include_pay).findViewById(R.id.et_favorable_number);
        this.ivFavorableNumber = (ImageView) findViewById(R.id.include_pay).findViewById(R.id.iv_favorable_number);
        this.etFavorableParticulars = (EditText) findViewById(R.id.include_pay).findViewById(R.id.et_favorable_particulars);
        this.etDiscountPay = (EditText) findViewById(R.id.include_pay).findViewById(R.id.et_zhekou_pay);
        this.tvSettlePay = (TextView) findViewById(R.id.tv_settle_pay);
        this.tvQuitPay = (TextView) findViewById(R.id.tv_quit_pay);
        this.tvDiscount1 = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_zhekou1);
        this.tvDiscount2 = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_zhekou2);
        this.tvDiscount3 = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_zhekou3);
        this.tvDiscount4 = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_zhekou4);
        this.tvDiscount5 = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_zhekou5);
        this.tvDiscount6 = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_zhekou6);
        this.tvDiscount7 = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_zhekou7);
        this.tvDiscount8 = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_zhekou8);
        this.tvDiscount9 = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_zhekou9);
        this.tvDiscount10 = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_zhekou10);
        this.ckPay = (CheckBox) findViewById(R.id.ck_pay);
        this.ckDiscount = (CheckBox) findViewById(R.id.include_pay).findViewById(R.id.ck_zhekou);
        this.ivCombinationPay1 = (ImageView) findViewById(R.id.iv_combination_pay1);
        this.ivCombinationPay2 = (ImageView) findViewById(R.id.iv_combination_pay2);
        this.etCombinationPay1 = (EditText) findViewById(R.id.et_combination_pay1);
        this.etCombinationPay2 = (EditText) findViewById(R.id.et_combination_pay2);
        this.tvVip = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_vip);
        this.rlVipSearch = (RelativeLayout) findViewById(R.id.include_pay).findViewById(R.id.ll_vip_search);
        this.etVipInfo = (EditText) findViewById(R.id.include_pay).findViewById(R.id.et_vip_info);
        this.ivVipSearch = (ImageView) findViewById(R.id.include_pay).findViewById(R.id.iv_vip_search);
        this.etFavorableVip = (TextView) findViewById(R.id.include_pay).findViewById(R.id.et_favorable_vip);
        this.tvTipIsnotVip = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_favorable_show);
        this.tvdiscountRecord = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_discount_record);
        this.tvCouponRecord = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_coupon_record);
        this.tvMemberRecord = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_member_record);
        this.llFavorableSelect = (LinearLayout) findViewById(R.id.include_pay).findViewById(R.id.ll_favorable_select);
        this.tvTipIsnot = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_tip_isnot);
        this.lvFavorableSelect = (ListView) findViewById(R.id.include_pay).findViewById(R.id.lv_favorable_select);
        this.ckIntegral = (CheckBox) findViewById(R.id.include_pay).findViewById(R.id.ck_integral);
        this.tvIntegral = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_integral);
        this.tvFavorableOk = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_favorable_ok);
        this.ckFavorable = (CheckBox) findViewById(R.id.include_pay).findViewById(R.id.ck_favorable);
        this.ckVip = (CheckBox) findViewById(R.id.include_pay).findViewById(R.id.ck_vip);
        this.tvQuery = (TextView) findViewById(R.id.include_pay).findViewById(R.id.tv_query);
        this.ivFavorableSearch = (ImageView) findViewById(R.id.include_pay).findViewById(R.id.iv_favorable_search);
        this.llPay = (LinearLayout) findViewById(R.id.ll_pay);
        this.llZhekou = (LinearLayout) findViewById(R.id.include_pay).findViewById(R.id.ll_ck_zhekou);
        this.llFavorableCk = (LinearLayout) findViewById(R.id.include_pay).findViewById(R.id.ll_ck_favorable);
        this.llVip = (LinearLayout) findViewById(R.id.include_pay).findViewById(R.id.ll_ck_vip);
        this.tvMoney = (TextView) findViewById(R.id.tv_money);
        this.conponList = new ArrayList<>();
        this.discountCouponAdapter = new DiscountCouponAdapter(this.conponList, this.context);
        this.lvFavorableSelect.setAdapter(this.discountCouponAdapter);
        this.lvFavorableSelect.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                PayActivity.this.discountCouponAdapter.setSelect(i);
                PayActivity.this.tvTipIsnotVip.setText( PayActivity.this.conponList.get(i).getName());
                PayActivity.this.vipSelete = PayActivity.this.conponList.get(i);
                PayActivity.this.tvTipIsnotVip.setTextColor(PayActivity.this.getResources().getColor(R.color.black));
                PayActivity.this.etFavorableVip.setText("优惠信息 : " + PayActivity.this.conponList.get(i).getDetail());
                PayActivity.this.lvFavorableSelect.setVisibility(View.GONE);
            }
        });
        payDialogUtils = new PayDialogUtils(this);
    }

    @Override
    protected void initEvent() {
        this.presenter = new PayPresenter(this, this.context, this.progressDialog);
        this.orderInfo = (OrderInfo) getIntent().getSerializableExtra("ORD");
        this.money = String.valueOf(this.orderInfo.getTotalprice());
        this.presenter.getOrderPay(this.orderInfo.getOrderid());
    }

    @Override
    protected void initData() {
        if (-1 != SPUtils.getUserinfos(this.context, SPUtils.SETTING_PRINT_PAPERSTYLE)) {
            this.mPapertype = SPUtils.getUserinfos(this.context, SPUtils.SETTING_PRINT_PAPERSTYLE);
        }
        this.ivCashPay.setSelected(true);
        this.tvDiscount10.setSelected(true);
        this.etAggregatePay.setText(this.money);
        this.etIncomePay.setText(this.money);
        this.etDiscountPay.setText("10.0");
        this.paylist.add(this.ivCashPay);
        this.paylist.add(this.ivAlipayPay);
        this.paylist.add(this.ivWxpayPay);
        this.paylist.add(this.ivCuzhikaPay);
        this.paylist.add(this.ivUnionpayPay);
        this.paylist.add(this.ivSaoYiSao);
        this.paylist.add(this.ivObligate1);
        this.paylist.add(this.ivObligate2);
        this.discountList.add(this.tvDiscount1);
        this.discountList.add(this.tvDiscount2);
        this.discountList.add(this.tvDiscount3);
        this.discountList.add(this.tvDiscount4);
        this.discountList.add(this.tvDiscount5);
        this.discountList.add(this.tvDiscount6);
        this.discountList.add(this.tvDiscount7);
        this.discountList.add(this.tvDiscount8);
        this.discountList.add(this.tvDiscount9);
        this.discountList.add(this.tvDiscount10);
        this.listDiscount.addAll(this.discountList);
        this.listDiscount.add(this.tvFavorablePay);
        this.listDiscount.add(this.etDiscountPay);
        this.listConpon.add(this.tvFavorablePay);
        this.listConpon.add(this.ivFavorableNumber);
        this.listConpon.add(this.ivFavorableSearch);
        this.listVip.add(this.ivVipSearch);
        this.listVip.add(this.llFavorableSelect);
        this.listVip.add(this.tvFavorablePay);
        this.ivCashPay.setOnClickListener(this);
        this.ivAlipayPay.setOnClickListener(this);
        this.ivWxpayPay.setOnClickListener(this);
        this.ivCuzhikaPay.setOnClickListener(this);
        this.ivUnionpayPay.setOnClickListener(this);
        this.ivSaoYiSao.setOnClickListener(this);
        this.ivObligate1.setOnClickListener(this);
        this.ivObligate2.setOnClickListener(this);
        this.tvFavorablePay.setOnClickListener(this);
        this.tvDiscount1.setOnClickListener(this);
        this.tvDiscount2.setOnClickListener(this);
        this.tvDiscount3.setOnClickListener(this);
        this.tvDiscount4.setOnClickListener(this);
        this.tvDiscount5.setOnClickListener(this);
        this.tvDiscount6.setOnClickListener(this);
        this.tvDiscount7.setOnClickListener(this);
        this.tvDiscount8.setOnClickListener(this);
        this.tvDiscount9.setOnClickListener(this);
        this.tvDiscount10.setOnClickListener(this);
        this.llDiscountPay.setOnClickListener(this);
        this.tvDiscount.setOnClickListener(this);
        this.tvFavorable.setOnClickListener(this);
        this.etFavorableNumber.setOnClickListener(this);
        this.ivFavorableNumber.setOnClickListener(this);
        this.tvSettlePay.setOnClickListener(this);
        this.tvQuitPay.setOnClickListener(this);
        this.llDiscount.setOnClickListener(this);
        this.ckDiscount.setOnClickListener(this);
        this.rlVipSearch.setOnClickListener(this);
        this.ckVip.setOnClickListener(this);
        this.ckPay.setOnClickListener(this);
        this.ckFavorable.setOnClickListener(this);
        this.ivVipSearch.setOnClickListener(this);
        this.ivFavorableSearch.setOnClickListener(this);
        this.tvVip.setOnClickListener(this);
        this.llFavorableSelect.setOnClickListener(this);
        this.tvQuery.setOnClickListener(this);
        this.ckIntegral.setOnClickListener(this);
        this.ckIntegral.setEnabled(false);
        this.llPay.setOnClickListener(this);
        this.llZhekou.setOnClickListener(this);
        this.llFavorableCk.setOnClickListener(this);
        this.llVip.setOnClickListener(this);
        inputEdit();
        this.dialogUtils.setScanListener(new onScanLinstener() {
            public void scan(String str, String str2) {
                if ("2".equals(str2)) {
                    PayActivity.this.presenter.aliPayFacePay(str, PayActivity.this.orderInfo.getOrderid(), PayActivity.this.getAggregatePayMoney(), PayActivity.this.getPayMoney(), PayActivity.this.jfTemp, PayActivity.this.getVipId(), PayActivity.this.getCoupon(), PayActivity.this.getVipcoupon(), PayActivity.this.getDiscountPay() + "", PayActivity.this.getIsFinish());
                } else if ("3".equals(str2)) {
                    PayActivity.this.presenter.wxPay(str, PayActivity.this.orderInfo.getOrderid(), PayActivity.this.getAggregatePayMoney(), PayActivity.this.getPayMoney(), PayActivity.this.jfTemp, PayActivity.this.getVipId(), PayActivity.this.getCoupon(), PayActivity.this.getVipcoupon(), PayActivity.this.getDiscountPay() + "", PayActivity.this.getIsFinish());
                }
            }
        });
        this.etFavorableNumber.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == 67 && !TextUtils.isEmpty(PayActivity.this.etFavorableNumber.getText().toString().trim())) {
                    PayActivity.this.couponTag = false;
                }
                return false;
            }
        });
        this.etVipInfo.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == 67 && !TextUtils.isEmpty(PayActivity.this.etVipInfo.getText().toString().trim())) {
                    PayActivity.this.vipTag = false;
                    PayActivity.this.userVip = false;
                }
                return false;
            }
        });
        this.dialogUtils.setCouponLinstener(new onCouponLinstener() {
            public void onCoupon(String str) {
                PayActivity.this.etFavorableNumber.setText(str);
                PayActivity.this.etFavorableNumber.setSelection(PayActivity.this.etFavorableNumber.getText().length());
                PayActivity.this.presenter.checkConpon(str, PayActivity.this.money);
            }
        });
        this.etIncomePay.addTextChangedListener(new TextWatcher() {
            Double tempS;

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (new DoubleUtils().pandun(editable.toString()) && editable.toString().length() > 0 && !".".equals(editable.toString().substring(0, 1))) {
                    this.tempS = Double.valueOf(editable.toString());
                    if (this.tempS.doubleValue() > Double.valueOf(PayActivity.this.getAggregatePayMoney()).doubleValue()) {
                        PayActivity.this.etChangePay.setText(ArithmeticUtils.sub(editable.toString(), PayActivity.this.getAggregatePayMoney(), 2));
                    } else {
                        PayActivity.this.etChangePay.setText("0");
                    }
                }
            }
        });
        this.etCombinationPay2.addTextChangedListener(new TextWatcher() {
            Double temp;

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (new DoubleUtils().pandun(editable.toString()) && editable.toString().length() > 0 && !".".equals(editable.toString().substring(0, 1))) {
                    this.temp = Double.valueOf(editable.toString());
                    if (this.temp.doubleValue() > PayActivity.this.differMoney.doubleValue()) {
                        PayActivity.this.etChangePay.setText(ArithmeticUtils.sub(editable.toString(), String.valueOf(PayActivity.this.differMoney), 2));
                    } else {
                        PayActivity.this.etChangePay.setText("0");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_favorable_pay:
                if (!this.OrderPay) {
                    if (this.llDiscountPay.getVisibility() == View.GONE) {
                        this.llDiscountPay.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        this.llDiscountPay.setVisibility(View.GONE);
                        return;
                    }
                }
                return;
            case R.id.iv_cash_pay:
                selectedSet(String.valueOf(view.getId()), this.paylist);
                if (this.NUMBER == 1) {
                    this.PayTypeTAG = this.ivCashPay.getTag().toString();
                    setEdittextClick(this.NUMBER, false);
                    return;
                }
                setPays(this.ivCashPay.getTag().toString());
                setEdittextClick(this.NUMBER, false);
                return;
            case R.id.iv_alipay_pay:
                selectedSet(String.valueOf(view.getId()), this.paylist);
                if (this.NUMBER == 1) {
                    this.PayTypeTAG = this.ivAlipayPay.getTag().toString();
                    setEdittextClick(this.NUMBER, true);
                    return;
                }
                setPays(this.ivAlipayPay.getTag().toString());
                setEdittextClick(this.NUMBER, true);
                return;
            case R.id.iv_wxpay_pay:
                selectedSet(String.valueOf(view.getId()), this.paylist);
                if (this.NUMBER == 1) {
                    this.PayTypeTAG = this.ivWxpayPay.getTag().toString();
                    setEdittextClick(this.NUMBER, true);
                    return;
                }
                setPays(this.ivWxpayPay.getTag().toString());
                setEdittextClick(this.NUMBER, true);
                return;
            case R.id.tv_settle_pay:
                if (this.NUMBER == 1) {
                    if (!new DoubleUtils().pandun(getIncomePayMoney())) {
                        Toasts.show(this.context, "请输入正确的金额");
                        return;
                    }
                } else if (this.NUMBER == 2 && !new DoubleUtils().pandun(getEtCombinationPay2())) {
                    Toasts.show(this.context, "请输入正确的金额");
                    return;
                }
                if (!this.ckPay.isChecked()) {
                    if (!"6".equals(this.PayTypeTAG)) {
                        if (TextUtils.isEmpty(getIncomePayMoney())) {
                            Toasts.show(this.context, "请输入支付金额");
                            return;
                        } else if (ArithmeticUtils.compare(getIncomePayMoney(), getAggregatePayMoney()) == -1) {
                            return;
                        }
                    }
                    getMoney(Double.valueOf(getIncomePayMoney()), this.PayTypeTAG);
                    return;
                } else if (this.NUMBER == 1) {
                    if (!TextUtils.isEmpty(getIncomePayMoney())) {
                        if (ArithmeticUtils.compare(getIncomePayMoney(), getAggregatePayMoney()) == 1 || ArithmeticUtils.compare(getIncomePayMoney(), getAggregatePayMoney()) == 0) {
                            this.ckPay.setChecked(false);
                            return;
                        } else if (ArithmeticUtils.compare(getIncomePayMoney(), getAggregatePayMoney()) == -1) {
                            getMoney(Double.valueOf(getIncomePayMoney()), this.PayTypeTAG);
                            return;
                        } else {
                            return;
                        }
                    }
                    return;
                } else if (TextUtils.isEmpty(getEtCombinationPay2())) {
                    Toasts.showShort((Context) this, (CharSequence) "金额不对");
                    return;
                } else if (ArithmeticUtils.compare(String.valueOf(this.differMoney), getEtCombinationPay2()) == 0 || ArithmeticUtils.compare(String.valueOf(this.differMoney), getEtCombinationPay2()) == -1) {
                    getMoney(Double.valueOf(getEtCombinationPay2()), String.valueOf(this.ivCombinationPay2.getTag()));
                    return;
                } else {
                    return;
                }
            case R.id.tv_quit_pay:
                finish();
                return;
            case R.id.ll_pay:
                if (this.OrderPay) {
                    this.ckPay.setChecked(false);
                    return;
                } else if ("6".equals(this.PayTypeTAG)) {
                    this.ckPay.setChecked(false);
                    return;
                } else if (this.NUMBER == 2) {
                    this.ckPay.setChecked(true);
                    return;
                } else {
                    if (this.NUMBER == 1) {
                        this.etIncomePay.setEnabled(true);
                    }
                    if (this.ckPay.isChecked()) {
                        this.ckPay.setChecked(false);
                        return;
                    } else {
                        this.ckPay.setChecked(true);
                        return;
                    }
                }
            case R.id.ck_pay:
                if (this.OrderPay) {
                    this.ckPay.setChecked(false);
                    return;
                } else if ("6".equals(this.PayTypeTAG)) {
                    this.ckPay.setChecked(false);
                    return;
                } else if (this.NUMBER == 2) {
                    this.ckPay.setChecked(true);
                    return;
                } else if (this.NUMBER == 1) {
                    this.etIncomePay.setEnabled(true);
                    return;
                } else {
                    return;
                }
            case R.id.iv_cuzhika_pay:
                selectedSet(String.valueOf(view.getId()), this.paylist);
                if (this.NUMBER == 1) {
                    this.PayTypeTAG = this.ivCuzhikaPay.getTag().toString();
                    setEdittextClick(this.NUMBER, true);
                    return;
                }
                setPays(this.ivCuzhikaPay.getTag().toString());
                setEdittextClick(this.NUMBER, true);
                return;
            case R.id.iv_unionpay_pay:
                selectedSet(String.valueOf(view.getId()), this.paylist);
                if (this.NUMBER == 1) {
                    this.PayTypeTAG = this.ivUnionpayPay.getTag().toString();
                    setEdittextClick(this.NUMBER, true);
                    return;
                }
                setPays(this.ivUnionpayPay.getTag().toString());
                setEdittextClick(this.NUMBER, true);
                return;
            case R.id.iv_saoyisao_pay:
                if (this.OrderPay) {
                    Toasts.show(this.context, "第二次支付不能选择被扫");
                    return;
                } else if (!this.ckPay.isChecked()) {
                    selectedSet(String.valueOf(view.getId()), this.paylist);
                    if (this.NUMBER == 1) {
                        this.PayTypeTAG = this.ivSaoYiSao.getTag().toString();
                        return;
                    } else {
                        setPays(this.ivSaoYiSao.getTag().toString());
                        return;
                    }
                } else {
                    return;
                }
            case R.id.iv_obligate1_pay:
                selectedSet(String.valueOf(view.getId()), this.paylist);
                if (this.NUMBER == 1) {
                    this.PayTypeTAG = this.ivObligate1.getTag().toString();
                    return;
                } else {
                    setPays(this.ivObligate1.getTag().toString());
                    return;
                }
            case R.id.iv_obligate2_pay:
                selectedSet(String.valueOf(view.getId()), this.paylist);
                if (this.NUMBER == 1) {
                    this.PayTypeTAG = this.ivObligate2.getTag().toString();
                    return;
                } else {
                    setPays(this.ivObligate2.getTag().toString());
                    return;
                }
            case R.id.ll_zhekou_pay:
                if (this.lvFavorableSelect.getVisibility() == View.VISIBLE) {
                    this.lvFavorableSelect.setVisibility(View.GONE);
                    return;
                }
                return;
            case R.id.tv_zhekou:
                this.llDiscount.setVisibility(View.VISIBLE);
                this.tvDiscount.setBackgroundResource(R.drawable.corner_blue);
                this.llFavorable.setVisibility(View.GONE);
                this.tvFavorable.setBackgroundResource(R.drawable.corner_gray);
                this.rlVipSearch.setVisibility(View.GONE);
                this.tvVip.setBackgroundResource(R.drawable.corner_gray);
                return;
            case R.id.tv_favorable:
                this.llDiscount.setVisibility(View.GONE);
                this.tvDiscount.setBackgroundResource(R.drawable.corner_gray);
                this.llFavorable.setVisibility(View.VISIBLE);
                this.tvFavorable.setBackgroundResource(R.drawable.corner_blue);
                this.rlVipSearch.setVisibility(View.GONE);
                this.tvVip.setBackgroundResource(R.drawable.corner_gray);
                return;
            case R.id.iv_favorable_number:
                if (this.NUMBER != 2 && !this.ckPay.isChecked()) {
                    this.dialogUtils.scanAwait(this.ivFavorableNumber);
                    if (!TextUtils.isEmpty(this.etFavorableNumber.getText().toString().trim())) {
                        this.etFavorableNumber.setText("");
                    }
                    this.etFavorableNumber.requestFocus();
                    return;
                }
                return;
            case R.id.ll_favorable_select:
                if (this.lvFavorableSelect.getVisibility() == View.GONE) {
                    this.lvFavorableSelect.setVisibility(View.VISIBLE);
                    return;
                } else {
                    this.lvFavorableSelect.setVisibility(View.GONE);
                    return;
                }
            case R.id.tv_vip:
                this.llDiscount.setVisibility(View.GONE);
                this.llFavorable.setVisibility(View.GONE);
                this.rlVipSearch.setVisibility(View.VISIBLE);
                this.tvVip.setBackgroundResource(R.drawable.corner_blue);
                this.tvDiscount.setBackgroundResource(R.drawable.corner_gray);
                this.tvFavorable.setBackgroundResource(R.drawable.corner_gray);
                return;
            case R.id.iv_vip_search:
                if (this.NUMBER != 2 && !this.ckPay.isChecked()) {
                    if (TextUtils.isEmpty(this.etVipInfo.getText().toString().trim())) {
                        Toasts.show(this.context, "请输入会员号");
                        return;
                    } else {
                        this.presenter.searchVip(this.etVipInfo.getText().toString().trim(), this.money);
                        return;
                    }
                }
                return;
            case R.id.ck_integral:
                if (this.ckIntegral.isChecked()) {
                    this.jfTemp = this.vipConpon.getPointsamount();
                    return;
                } else {
                    this.jfTemp = "";
                    return;
                }
            case R.id.iv_favorable_search:
                if (this.NUMBER != 2 && !this.ckPay.isChecked() && !TextUtils.isEmpty(this.etFavorableNumber.getText().toString().trim())) {
                    this.presenter.checkConpon(this.etFavorableNumber.getText().toString().trim(), this.money);
                    return;
                }
                return;
            case R.id.tv_zhekou1:
                selectedSet(String.valueOf(view.getId()), this.discountList);
                this.etDiscountPay.setText(BuildConfig.VERSION_NAME);
                return;
            case R.id.tv_zhekou2:
                selectedSet(String.valueOf(view.getId()), this.discountList);
                this.etDiscountPay.setText("2.0");
                return;
            case R.id.tv_zhekou3:
                selectedSet(String.valueOf(view.getId()), this.discountList);
                this.etDiscountPay.setText("3.0");
                return;
            case R.id.tv_zhekou4:
                selectedSet(String.valueOf(view.getId()), this.discountList);
                this.etDiscountPay.setText("4.0");
                return;
            case R.id.tv_zhekou5:
                selectedSet(String.valueOf(view.getId()), this.discountList);
                this.etDiscountPay.setText("5.0");
                return;
            case R.id.tv_zhekou6:
                selectedSet(String.valueOf(view.getId()), this.discountList);
                this.etDiscountPay.setText("6.0");
                return;
            case R.id.tv_zhekou7:
                selectedSet(String.valueOf(view.getId()), this.discountList);
                this.etDiscountPay.setText("7.0");
                return;
            case R.id.tv_zhekou8:
                selectedSet(String.valueOf(view.getId()), this.discountList);
                this.etDiscountPay.setText("8.0");
                return;
            case R.id.tv_zhekou9:
                selectedSet(String.valueOf(view.getId()), this.discountList);
                this.etDiscountPay.setText("9.0");
                return;
            case R.id.tv_zhekou10:
                selectedSet(String.valueOf(view.getId()), this.discountList);
                this.etDiscountPay.setText("10.0");
                return;
            case R.id.ll_ck_zhekou:
                if (this.ckPay.isChecked()) {
                    this.ckDiscount.setChecked(this.integralBoolean);
                    return;
                }
                this.integralBoolean = this.ckDiscount.isChecked();
                if (this.NUMBER == 2) {
                    if (this.ckDiscount.isChecked()) {
                        this.ckDiscount.setChecked(true);
                        return;
                    } else {
                        this.ckDiscount.setChecked(false);
                        return;
                    }
                } else if (TextUtils.isEmpty(this.etDiscountPay.getText().toString().trim()) || Double.valueOf(this.etDiscountPay.getText().toString().trim()).doubleValue() <= 0.0d || ".".equals(this.etDiscountPay.getText().toString().trim().substring(0, 1))) {
                    this.ckDiscount.setChecked(false);
                    return;
                } else {
                    if (this.ckDiscount.isChecked()) {
                        this.ckDiscount.setChecked(false);
                    } else {
                        this.ckDiscount.setChecked(true);
                    }
                    if (this.ckDiscount.isChecked()) {
                        setClick(true, this.listDiscount);
                        this.etDiscountPay.setEnabled(false);
                        this.tvdiscountRecord.setVisibility(View.VISIBLE);
                    } else {
                        this.etAggregatePay.setText(this.money);
                        this.etIncomePay.setText(this.money);
                        setClick(false, this.listDiscount);
                        this.etDiscountPay.setEnabled(true);
                        this.tvdiscountRecord.setVisibility(View.INVISIBLE);
                    }
                    if (this.useBoolean) {
                        this.tvMoney.setVisibility(View.INVISIBLE);
                        setBalance();
                    }
                    this.useBoolean = false;
                    return;
                }
            case R.id.ck_zhekou:
                if (this.ckPay.isChecked()) {
                    this.ckDiscount.setChecked(this.integralBoolean);
                    return;
                }
                this.integralBoolean = this.ckDiscount.isChecked();
                if (this.NUMBER == 2) {
                    if (this.ckDiscount.isChecked()) {
                        this.ckDiscount.setChecked(true);
                        return;
                    } else {
                        this.ckDiscount.setChecked(false);
                        return;
                    }
                } else if (TextUtils.isEmpty(this.etDiscountPay.getText().toString().trim()) || Double.valueOf(this.etDiscountPay.getText().toString().trim()).doubleValue() <= 0.0d || ".".equals(this.etDiscountPay.getText().toString().trim().substring(0, 1))) {
                    this.ckDiscount.setChecked(false);
                    return;
                } else {
                    if (this.ckDiscount.isChecked()) {
                        setClick(true, this.listDiscount);
                        this.etDiscountPay.setEnabled(false);
                        this.tvdiscountRecord.setVisibility(View.VISIBLE);
                    } else {
                        this.etAggregatePay.setText(this.money);
                        this.etIncomePay.setText(this.money);
                        setClick(false, this.listDiscount);
                        this.etDiscountPay.setEnabled(true);
                        this.tvdiscountRecord.setVisibility(View.INVISIBLE);
                    }
                    if (this.useBoolean) {
                        this.tvMoney.setVisibility(View.INVISIBLE);
                        setBalance();
                    }
                    this.useBoolean = false;
                    return;
                }
            case R.id.ll_ck_favorable:
                if (this.ckPay.isChecked()) {
                    this.ckFavorable.setChecked(this.favorableBoolean);
                    return;
                }
                this.favorableBoolean = this.ckFavorable.isChecked();
                if (this.NUMBER == 2) {
                    if (this.ckFavorable.isChecked()) {
                        this.ckFavorable.setChecked(true);
                        return;
                    } else {
                        this.ckFavorable.setChecked(false);
                        return;
                    }
                } else if (this.couponTag) {
                    if (this.ckFavorable.isChecked()) {
                        this.ckFavorable.setChecked(false);
                    } else {
                        this.ckFavorable.setChecked(true);
                    }
                    if (this.ckFavorable.isChecked()) {
                        setClick(true, this.listConpon);
                        this.etFavorableNumber.setEnabled(false);
                        this.tvCouponRecord.setVisibility(View.VISIBLE);
                    } else {
                        this.etAggregatePay.setText(this.money);
                        this.etIncomePay.setText(this.money);
                        setClick(false, this.listConpon);
                        this.etFavorableNumber.setEnabled(true);
                        this.tvCouponRecord.setVisibility(View.INVISIBLE);
                    }
                    if (this.useBoolean) {
                        this.tvMoney.setVisibility(View.INVISIBLE);
                        setBalance();
                    }
                    this.useBoolean = false;
                    return;
                } else {
                    Toasts.show(this.context, "请先选择正确的优惠券");
                    this.ckFavorable.setChecked(false);
                    return;
                }
            case R.id.ck_favorable:
                if (this.ckPay.isChecked()) {
                    this.ckFavorable.setChecked(this.favorableBoolean);
                    return;
                }
                this.favorableBoolean = this.ckFavorable.isChecked();
                if (this.NUMBER == 2) {
                    if (this.ckFavorable.isChecked()) {
                        this.ckFavorable.setChecked(true);
                        return;
                    } else {
                        this.ckFavorable.setChecked(false);
                        return;
                    }
                } else if (this.couponTag) {
                    if (this.ckFavorable.isChecked()) {
                        setClick(true, this.listConpon);
                        this.etFavorableNumber.setEnabled(false);
                        this.tvCouponRecord.setVisibility(View.VISIBLE);
                    } else {
                        this.etAggregatePay.setText(this.money);
                        this.etIncomePay.setText(this.money);
                        setClick(false, this.listConpon);
                        this.etFavorableNumber.setEnabled(true);
                        this.tvCouponRecord.setVisibility(View.INVISIBLE);
                    }
                    if (this.useBoolean) {
                        this.tvMoney.setVisibility(View.INVISIBLE);
                        setBalance();
                    }
                    this.useBoolean = false;
                    return;
                } else {
                    Toasts.show(this.context, "请先选择正确的优惠券");
                    this.ckFavorable.setChecked(false);
                    return;
                }
            case R.id.ll_ck_vip:
                if (this.ckPay.isChecked()) {
                    this.ckVip.setChecked(this.vipBoolean);
                    return;
                }
                this.vipBoolean = this.ckVip.isChecked();
                if (this.NUMBER == 2) {
                    if (this.ckVip.isChecked()) {
                        this.ckVip.setChecked(true);
                        return;
                    } else {
                        this.ckVip.setChecked(false);
                        return;
                    }
                } else if (this.vipTag) {
                    if (this.ckVip.isChecked()) {
                        this.ckVip.setChecked(false);
                    } else {
                        this.ckVip.setChecked(true);
                    }
                    if (this.ckVip.isChecked()) {
                        setClick(true, this.listVip);
                        this.etVipInfo.setEnabled(false);
                        this.ckIntegral.setClickable(false);
                        this.tvMemberRecord.setVisibility(View.VISIBLE);
                    } else {
                        this.etAggregatePay.setText(this.money);
                        this.etIncomePay.setText(this.money);
                        setClick(false, this.listVip);
                        this.etVipInfo.setEnabled(true);
                        this.ckIntegral.setClickable(true);
                        this.tvMemberRecord.setVisibility(View.INVISIBLE);
                    }
                    if (this.useBoolean) {
                        this.tvMoney.setVisibility(View.INVISIBLE);
                        setBalance();
                    }
                    this.useBoolean = false;
                    return;
                } else {
                    Toasts.show(this.context, "请先选择优惠");
                    this.ckVip.setChecked(false);
                    return;
                }
            case R.id.ck_vip:
                if (this.ckPay.isChecked()) {
                    this.ckVip.setChecked(this.vipBoolean);
                    return;
                }
                this.vipBoolean = this.ckVip.isChecked();
                if (this.NUMBER == 2) {
                    if (this.ckVip.isChecked()) {
                        this.ckVip.setChecked(true);
                        return;
                    } else {
                        this.ckVip.setChecked(false);
                        return;
                    }
                } else if (this.vipTag) {
                    if (this.ckVip.isChecked()) {
                        setClick(true, this.listVip);
                        this.etVipInfo.setEnabled(false);
                        this.ckIntegral.setClickable(false);
                        this.tvMemberRecord.setVisibility(View.VISIBLE);
                    } else {
                        this.etAggregatePay.setText(this.money);
                        this.etIncomePay.setText(this.money);
                        setClick(false, this.listVip);
                        this.etVipInfo.setEnabled(true);
                        this.ckIntegral.setClickable(true);
                        this.tvMemberRecord.setVisibility(View.INVISIBLE);
                    }
                    if (this.useBoolean) {
                        this.tvMoney.setVisibility(View.INVISIBLE);
                        setBalance();
                    }
                    this.useBoolean = false;
                    return;
                } else {
                    Toasts.show(this.context, "请先选择优惠");
                    this.ckVip.setChecked(false);
                    return;
                }
            case R.id.tv_query:
                if (!this.ckPay.isChecked() && this.NUMBER != 2 && !this.OrderPay) {
                    if (this.ckDiscount.isChecked() || this.ckFavorable.isChecked() || this.ckVip.isChecked()) {
                        this.presenter.countPay(getCountID(), this.orderInfo.getOrderid(), this.money, getVipId(), getPointsamount().intValue(), getDiscountamount(), getDiscount());
                        return;
                    } else {
                        Toasts.show(this.context, "暂未选择优惠");
                        return;
                    }
                }
                return;
            default:
                return;
        }
    }

    private Double getDiscount() {
        if (this.ckDiscount.isChecked()) {
            return Double.valueOf(this.etDiscountPay.getText().toString().trim());
        }
        return Double.valueOf(-1.0d);
    }

    public int isUse() {
        if (this.ckDiscount.isChecked() || this.ckFavorable.isChecked() || this.ckVip.isChecked()) {
            return 1;
        }
        return 0;
    }

    private Double getDiscountamount() {
        if (this.userVip) {
            return Double.valueOf(this.vipConpon.getDiscountamount());
        }
        return Double.valueOf(0.0d);
    }

    private Integer getPointsamount() {
        if (this.ckIntegral.isChecked()) {
            return Integer.valueOf(this.vipConpon.getPointsamount());
        }
        return Integer.valueOf(0);
    }

    private String getCountID() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.ckFavorable.isChecked()) {
            stringBuffer.append(this.conpon.getId());
        }
        if (this.ckVip.isChecked() && this.vipSelete != null) {
            if (stringBuffer.length() > 0) {
                stringBuffer.append("," + this.vipSelete.getId());
            } else {
                stringBuffer.append(this.vipSelete.getId());
            }
        }
        return stringBuffer.toString();
    }

    private String getVipId() {
        return this.vipUserId;
    }

    private Double getDiscountPay() {
        if (!this.useBoolean) {
            return Double.valueOf(-1.0d);
        }
        if (this.ckDiscount.isChecked()) {
            return Double.valueOf(this.etDiscountPay.getText().toString().trim());
        }
        return Double.valueOf(-1.0d);
    }

    private String getCoupon() {
        List arrayList = new ArrayList();
        if (!this.useBoolean) {
            return "";
        }
        if (this.ckFavorable.isChecked()) {
            arrayList.add(this.conpon);
        }
        if (this.ckVip.isChecked() && this.vipSelete != null) {
            arrayList.add(this.vipSelete);
        }
        if (arrayList.size() > 0) {
            return JSON.toJSONString(arrayList);
        }
        return "";
    }

    private String getVipcoupon() {
        if (this.userVip) {
            return this.vipConpon.getDiscountamount();
        }
        return "";
    }

    private String getIsFinish() {
        if (!this.ckPay.isChecked()) {
            return "1";
        }
        if (this.NUMBER == 1) {
            return "0";
        }
        return "1";
    }

    private void setBalance() {
        if (this.ckPay.isChecked()) {
            if (this.NUMBER == 1) {
                if (new DoubleUtils().pandun(getIncomePayMoney()) && ArithmeticUtils.compare(getIncomePayMoney(), getAggregatePayMoney()) >= 0) {
                    this.etChangePay.setText(ArithmeticUtils.sub(getIncomePayMoney(), getAggregatePayMoney(), 2));
                }
            } else if (new DoubleUtils().pandun(getIncomePayMoney()) && ArithmeticUtils.compare(getEtCombinationPay2(), String.valueOf(this.differMoney)) >= 0) {
                this.etChangePay.setText(ArithmeticUtils.sub(getEtCombinationPay2(), String.valueOf(this.differMoney), 2));
            }
        } else if (new DoubleUtils().pandun(getIncomePayMoney()) && ArithmeticUtils.compare(getIncomePayMoney(), getAggregatePayMoney()) >= 0) {
            this.etChangePay.setText(ArithmeticUtils.sub(getIncomePayMoney(), getAggregatePayMoney(), 2));
        }
        this.tvQuery.setText("计算金额");
        this.tvQuery.setBackgroundResource(R.drawable.corner_gray);
    }

    private void combinationPay(String str) {
        if (!this.ckPay.isChecked()) {
            try {
                this.printUtil.delayPrint(this.context, "",  JSON.parseObject(str, new TypeReference<OrderInfo>() {
                }, new Feature[0]), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();
        } else if (this.NUMBER == 1) {
            this.etIncomePay.setVisibility(View.GONE);
            this.llCombinationPay.setVisibility(View.VISIBLE);
            for (int i = 0; i < this.paylist.size(); i++) {
                if (( this.paylist.get(i)).getTag().toString().equals(this.PayTypeTAG)) {
                    ivCombinationPay1.setImageDrawable(((ImageView)paylist.get(i)).getDrawable());
                    this.ivCombinationPay1.setTag(this.PayTypeTAG);
                }
            }
            differMoney = Double.valueOf(ArithmeticUtils.sub(getAggregatePayMoney(), getIncomePayMoney(), 2));
            this.etCombinationPay1.setText(getIncomePayMoney());
            this.etCombinationPay2.setText(String.valueOf(this.differMoney));
            this.etCombinationPay2.setEnabled(false);
            selectedSet(String.valueOf(this.ivAlipayPay.getId()), this.paylist);
            this.NUMBER = 2;
        } else {
            try {
                printUtil.delayPrint(context, "",  JSON.parseObject(str, new TypeReference<OrderInfo>() {
                }, new Feature[0]), 0);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            finish();
        }
    }

    private void getOffTheStocks() {
        this.listGoods = (List) JSON.parseObject(this.orderInfo.getGoodslist(), new TypeReference<List<OrderGoods>>() {
        }, new Feature[0]);
        for (int i = 0; i < this.listGoods.size(); i++) {
            if (( listGoods.get(i)).getStatus() == 0) {
                this.servingTag = false;
                return;
            }
        }
    }

    public void setPays(String str) {
        for (int i = 0; i < this.paylist.size(); i++) {
            if (str == ( paylist.get(i)).getTag().toString()) {
                this.ivCombinationPay2.setImageDrawable(((ImageView) this.paylist.get(i)).getDrawable());
                this.ivCombinationPay2.setTag(str);
            }
        }
    }

    public void selectedSet(String str, List<View> list) {
        for (int i = 0; i < list.size(); i++) {
            if (str.equals(String.valueOf(((View) list.get(i)).getId()))) {
                ((View) list.get(i)).setSelected(true);
            } else {
                ((View) list.get(i)).setSelected(false);
            }
        }
    }

    public void inputEdit() {
        this.etDiscountPay.setFilters(new InputFilter[]{new DiscountInputFilter()});
        InputFilter[] inputFilterArr = new InputFilter[]{new CashierInputFilter()};
        this.etIncomePay.setFilters(inputFilterArr);
        this.etCombinationPay2.setFilters(inputFilterArr);
    }

    public void setClick(boolean z, List<View> list) {
        int i;
        if (z) {
            for (i = 0; i < list.size(); i++) {
                ((View) list.get(i)).setOnClickListener(null);
            }
            return;
        }
        for (i = 0; i < list.size(); i++) {
            ((View) list.get(i)).setOnClickListener(this);
        }
    }

    public void setEdittextClick(int i, boolean z) {
        if (i == 1) {
            if (!z) {
                this.etIncomePay.setEnabled(true);
            } else if (this.ckPay.isChecked()) {
                this.etIncomePay.setEnabled(true);
            } else {
                this.etIncomePay.setEnabled(false);
                this.etIncomePay.setText(getAggregatePayMoney());
            }
        } else if (i != 2) {
        } else {
            if (z) {
                this.etCombinationPay2.setEnabled(false);
                this.etCombinationPay2.setText(this.differMoney + "");
                return;
            }
            this.etCombinationPay2.setEnabled(true);
        }
    }

    public String getIncomePayMoney() {
        return this.etIncomePay.getText().toString().trim();
    }

    public String getAggregatePayMoney() {
        return this.etAggregatePay.getText().toString().trim();
    }

    public String getEtCombinationPay2() {
        return this.etCombinationPay2.getText().toString().trim();
    }

    public String getPayMoney() {
        if (this.ckPay.isChecked()) {
            if (this.NUMBER == 1) {
                return getIncomePayMoney();
            }
            if (ArithmeticUtils.compare(String.valueOf(this.differMoney), getEtCombinationPay2()) == 0 || ArithmeticUtils.compare(getEtCombinationPay2(), String.valueOf(this.differMoney)) == 1) {
                return String.valueOf(this.differMoney);
            }
            return "";
        } else if (ArithmeticUtils.compare(getIncomePayMoney(), getAggregatePayMoney()) == 1 || ArithmeticUtils.compare(getIncomePayMoney(), getAggregatePayMoney()) == 0) {
            return getAggregatePayMoney();
        } else {
            return "";
        }
    }

    public void getMoney(Double d, String str) {
        Integer obj = -1;
        switch (str.hashCode()) {
            case 49:
                if (str.equals("1")) {
                    obj = 0;
                    break;
                }
                break;
            case 50:
                if (str.equals("2")) {
                    obj = 1;
                    break;
                }
                break;
            case 51:
                if (str.equals("3")) {
                    obj = 2;
                    break;
                }
                break;
            case 52:
                if (str.equals("4")) {
                    obj = 3;
                    break;
                }
                break;
            case 53:
                if (str.equals("5")) {
                    obj = 4;
                    break;
                }
                break;
            case 54:
                if (str.equals("6")) {
                    obj = 5;
                    break;
                }
                break;
            case 55:
                if (str.equals("7")) {
                    obj = 6;
                    break;
                }
                break;
            case 56:
                if (str.equals("8")) {
                    obj = 7;
                    break;
                }
                break;
        }
        switch (obj) {
            case 0:
                this.printUtil.cashBoxTest(this.context);
                this.dialogUtils.gatheringMoney(this.tvSettlePay, new DialogUtils.OnClickListener() {
                    public void onClick(Object obj) {
                        PayActivity.this.presenter.payByCash(PayActivity.this.orderInfo.getOrderid(), PayActivity.this.getAggregatePayMoney(), PayActivity.this.getPayMoney(), PayActivity.this.jfTemp, PayActivity.this.getVipId(), PayActivity.this.getCoupon(), PayActivity.this.getVipcoupon(), PayActivity.this.getDiscount() + "", PayActivity.this.getIsFinish());
                    }
                });
                return;
            case 1:
                this.dialogUtils.showScanVIP(this.etIncomePay, "2");
                return;
            case 2:
                this.dialogUtils.showScanVIP(this.etIncomePay, "3");
                return;
            case 3:
                Toasts.show(this.context, "暂未接入此功能");
                return;
            case 4:
                Toasts.show(this.context, "暂未接入此功能");
                return;
            case 5:
                this.presenter.getBuliderOrderQR(this.orderInfo.getOrderid(), this.jfTemp, getVipId(), getCoupon(), getVipcoupon(), getDiscount() + "", getAggregatePayMoney(), isUse());
                return;
            case 6:
                Toasts.show(this.context, "暂未接入此功能");
                return;
            case 7:
                Toasts.show(this.context, "暂未接入此功能");
                return;
            default:
                return;
        }
    }

    public void inform(String str, String str2) {
        if ("orderPayOnSuccess".equals(str)) {
            if (!(TextUtils.isEmpty(str2) || str2 == null)) {
                OrderPayStatus orderPayStatus = JSON.parseObject(str2, new TypeReference<OrderPayStatus>() {
                }, new Feature[0]);
                this.etAggregatePay.setText(ArithmeticUtils.sub(this.money, String.valueOf(orderPayStatus.getPayedmoney()), 2));
                this.etIncomePay.setText(ArithmeticUtils.sub(this.money, String.valueOf(orderPayStatus.getPayedmoney()), 2));
                this.OrderPay = true;
                this.tvMoney.setVisibility(View.VISIBLE);
                this.tvMoney.setText("此订单为第二次支付");
            }
        } else if ("orderPayOnAlert".equals(str)) {
            Toasts.show(this.context, str2);
        } else if ("orderPayOnFailure".equals(str)) {
            Toasts.show(this.context, str2);
        }
        if ("aliPayFacePayOnSuccess".equals(str)) {
            Toasts.show(this.context, "支付宝支付成功");
            combinationPay(str2);
        } else if ("aliPayFacePayOnAlert".equals(str) || "aliPayFacePayOnFailure".equals(str)) {
            Toasts.show(this.context, "支付宝支付失败" + str2);
        }
        if ("wxPayOnSuccess".equals(str)) {
            Toasts.show(this.context, "微信支付成功");
            combinationPay(str2);
        } else if ("wxPayOnAlert".equals(str) || "wxPayOnFailure".equals(str)) {
            Toasts.show(this.context, "微信支付失败" + str2);
        }
        if ("payByCashOnSuccess".equals(str)) {
            combinationPay(str2);
        } else if ("payByCashOnAlert".equals(str)) {
            Toasts.show(this.context, "网络请求失败,请重新点击");
        } else if ("payByCashOnFailure".equals(str)) {
            Toasts.show(this.context, "网络请求失败,请重新点击");
        }
        if ("searchVipOnSuccess".equals(str)) {
            if ("null".equals(str2) || TextUtils.isEmpty(str2)) {
                this.llFavorableSelect.setVisibility(View.GONE);
                this.tvTipIsnot.setVisibility(View.VISIBLE);
                this.etFavorableVip.setText("优惠信息 :");
                this.tvTipIsnotVip.setText("请选择优惠");
                this.vipTag = false;
                if (!TextUtils.isEmpty(this.vipUserId)) {
                    this.vipUserId = "";
                }
                this.userVip = false;
                Toasts.show(this.context, "查询成功,不是会员");
            } else {
                this.tvTipIsnot.setVisibility(View.GONE);
                this.llFavorableSelect.setVisibility(View.VISIBLE);
                this.tvTipIsnotVip.setText("请选择优惠");
                this.tvTipIsnotVip.setTextColor(getResources().getColor(R.color.gray));
                this.vipTag = true;
                this.userVip = true;
                this.conponList.clear();
                this.vipConpon =  JSON.parseObject(str2, new TypeReference<VipConpon>() {
                }, new Feature[0]);
                this.vipUserId = this.vipConpon.getUserID();
                this.conponList.addAll(this.vipConpon.getCoupons());
                this.discountCouponAdapter.notifyDataSetChanged();
                if (Integer.valueOf(this.vipConpon.getPointsamount()).intValue() <= 0) {
                    this.tvIntegral.setText("没有可用积分");
                    this.ckIntegral.setEnabled(false);
                } else {
                    tvIntegral.setText("可用" + vipConpon.getPointsamount() + "积分,是否使用积分");
                    ckIntegral.setEnabled(true);
                }
                if (this.conponList.size() > 0) {
                    this.tvTipIsnotVip.setText(( conponList.get(0)).getName());
                    this.etFavorableVip.setText("优惠信息 :" + ( conponList.get(0)).getName() + "," + (conponList.get(0)).getDetail());
                    this.vipSelete =  conponList.get(0);
                } else if (Integer.valueOf(this.vipConpon.getPointsamount()).intValue() <= 0) {
                    this.vipTag = false;
                }
            }
        } else if ("searchVipOnAlert".equals(str)) {
            Toasts.show(this.context, "网络错误,请重新请求");
            this.vipTag = false;
            if (!TextUtils.isEmpty(this.vipUserId)) {
                this.vipUserId = "";
            }
            this.userVip = false;
        } else if ("searchVipOnFailure".equals(str)) {
            Toasts.show(this.context, "网络错误,请重新请求");
            this.vipTag = false;
            if (!TextUtils.isEmpty(this.vipUserId)) {
                this.vipUserId = "";
            }
            this.userVip = false;
        }
        if ("checkConponOnSuccess".equals(str)) {
            if (TextUtils.isEmpty(str2)) {
                this.tvFavorableOk.setVisibility(View.VISIBLE);
                this.tvFavorableOk.setText("该优惠券不可用,请重新选择");
                this.couponTag = false;
                this.etFavorableParticulars.setText("优惠信息 :");
            } else {
                this.tvFavorableOk.setVisibility(View.INVISIBLE);
                this.tvFavorableOk.setText("该优惠券可用");
                this.couponTag = true;
                this.conpon = JSON.parseObject(str2, new TypeReference<Conpon>() {
                }, new Feature[0]);
                this.etFavorableParticulars.setText("优惠信息 :" + this.conpon.getName() + "," + this.conpon.getDetail());
            }
        } else if ("checkConponOnAlert".equals(str)) {
            this.tvFavorableOk.setVisibility(View.INVISIBLE);
            Toasts.show(this.context, "该优惠券不可用,请重新选择");
            this.tvFavorableOk.setText("该优惠券不可用");
            this.etFavorableParticulars.setText("优惠信息 :");
            this.couponTag = false;
        } else if ("checkConponOnFailure".equals(str)) {
            this.tvFavorableOk.setVisibility(View.INVISIBLE);
            this.couponTag = false;
            Toasts.show(this.context, "网络请求失败,请重新请求");
            this.tvFavorableOk.setText("查询失败,请重新查询");
            this.etFavorableParticulars.setText("优惠信息 :");
        }
        if ("countPayOnSuccess".equals(str)) {
            if (TextUtils.isEmpty(str2) || str2 == null) {
                Toasts.show(this.context, "计算失败,请重新点击查询");
                this.tvQuery.setText("计算金额");
                this.tvQuery.setBackgroundResource(R.drawable.corner_gray);
                this.tvMoney.setVisibility(View.GONE);
                this.etAggregatePay.setText(this.money);
                this.etIncomePay.setText(this.money);
                this.favorableMoney = "";
                this.useBoolean = false;
            } else {
                this.etAggregatePay.setText(str2);
                this.etIncomePay.setText(str2);
                this.tvQuery.setText("计算成功");
                this.tvQuery.setBackgroundResource(R.drawable.corner_red);
                this.tvMoney.setVisibility(View.VISIBLE);
                this.tvMoney.setText("原金额 :" + this.money);
                this.favorableMoney = str2;
                this.useBoolean = true;
            }
        } else if ("countPayOnAlert".equals(str)) {
            Toasts.show(this.context, "请求失败,请重新点击查询");
            this.tvQuery.setText("计算金额");
            this.tvQuery.setBackgroundResource(R.drawable.corner_gray);
            this.tvMoney.setVisibility(View.GONE);
            this.etAggregatePay.setText(this.money);
            this.etIncomePay.setText(this.money);
            this.favorableMoney = "";
            this.useBoolean = false;
        } else if ("countPayOnFailure".equals(str)) {
            Toasts.show(this.context, "请求失败,请重新点击查询");
            this.tvQuery.setText("计算金额");
            this.tvQuery.setBackgroundResource(R.drawable.corner_gray);
            this.tvMoney.setVisibility(View.GONE);
            this.etAggregatePay.setText(this.money);
            this.etIncomePay.setText(this.money);
            this.favorableMoney = "";
            this.useBoolean = false;
        }
        if ("BuliderOrderQROnSuccess".equals(str)) {
            this.printUtil.printQRcode(this.context, str2);
            this.payDialogUtils.showQR(this.etAggregatePay, str2);
        } else if ("BuliderOrderQROnAlert".equals(str)) {
            Toasts.show(this.context, "请求失败,请重新点击");
        } else if ("BuliderOrderQROnFailure".equals(str)) {
            Toasts.show(this.context, "请求失败,请重新点击");
        }
    }

    public void paySuccessNotify(String str, long j) {
        super.paySuccessNotify(str, j);
        if (str.equals(this.orderInfo.getOrderid())) {
            this.payDialogUtils.dismiss();
            Intent intent = new Intent(this.context, ClientSocketService.class);
            intent.putExtra("orderId", j);
            startService(intent);
            this.dialogUtils.showTips(this.etAggregatePay, "订单" + str + "支付成功！");
        }
    }
}
