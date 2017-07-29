package com.smart.kitchen.smartkitchen.mvp.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;

public class PayModelImpl {
    public void aliQrPay(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2, String str3, String str4) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
        requestParams.put("out_trade_no", str);
        requestParams.put("total_amount", str2);
        requestParams.put("subject", str3);
        requestParams.put("body", str4);
        HttpUtils.post(context, Contants.ALIPAY_QRPAY, requestParams, i, onResultListener, progressDialog);
    }

    public void aliPayFacePay(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
        requestParams.put("auth_code", str);
        requestParams.put("orderid", str2);
        requestParams.put("totalmoney", str3);
        requestParams.put("thismoney", str4);
        if (!TextUtils.isEmpty(str5)) {
            requestParams.put("jf", str5);
        }
        if (!TextUtils.isEmpty(str6)) {
            requestParams.put("vip_id", str6);
        }
        if (!TextUtils.isEmpty(str7)) {
            requestParams.put("coupon", str7);
        }
        if (!TextUtils.isEmpty(str8)) {
            requestParams.put("vipcoupon", str8);
        }
        if (Double.parseDouble(str9) > 0.0d) {
            requestParams.put("discount", str9);
        }
        requestParams.put("is_finish", str10);
        HttpUtils.post(context, Contants.ALIPAY_FACEPAY, requestParams, i, onResultListener, progressDialog);
    }

    public void wxPay(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, String str10) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
        requestParams.put("auth_code", str);
        requestParams.put("orderid", str2);
        requestParams.put("totalmoney", str3);
        requestParams.put("thismoney", str4);
        if (!TextUtils.isEmpty(str5)) {
            requestParams.put("jf", str5);
        }
        if (!TextUtils.isEmpty(str6)) {
            requestParams.put("vip_id", str6);
        }
        if (!TextUtils.isEmpty(str7)) {
            requestParams.put("coupon", str7);
        }
        if (!TextUtils.isEmpty(str8)) {
            requestParams.put("vipcoupon", str8);
        }
        if (Double.parseDouble(str9) > 0.0d) {
            requestParams.put("discount", str9);
        }
        requestParams.put("is_finish", str10);
        HttpUtils.post(context, Contants.WXPAY, requestParams, i, onResultListener, progressDialog);
    }

    public void wxQrPay(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2, String str3, String str4) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
        requestParams.put("body", str);
        requestParams.put("out_trade_no", str2);
        requestParams.put("oattach", str3);
        requestParams.put("total_fee", str4);
        HttpUtils.post(context, Contants.WXQRPAY, requestParams, i, onResultListener, progressDialog);
    }

    public void payByCash(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
        requestParams.put("orderid", str);
        requestParams.put("totalmoney", str2);
        requestParams.put("thismoney", str3);
        if (!TextUtils.isEmpty(str4)) {
            requestParams.put("jf", str4);
        }
        if (!TextUtils.isEmpty(str5)) {
            requestParams.put("vip_id", str5);
        }
        if (!TextUtils.isEmpty(str6)) {
            requestParams.put("coupon", str6);
        }
        if (!TextUtils.isEmpty(str7)) {
            requestParams.put("vipcoupon", str7);
        }
        if (Double.parseDouble(str8) > 0.0d) {
            requestParams.put("discount", str8);
        }
        requestParams.put("is_finish", str9);
        HttpUtils.post(context, Contants.PAYBYCASH, requestParams, i, onResultListener, progressDialog);
    }

    public void checkConpon(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("coupon_code", str);
        requestParams.put("orderprice", str2);
        HttpUtils.post(context, Contants.CHECKCONPON, requestParams, i, onResultListener, progressDialog);
    }

    public void checkConponSuccess(Context context, String str, String str2, EditText editText, EditText editText2) {
    }

    public void countPay(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2, String str3, String str4, int i2, Double d, Double d2) {
        RequestParams requestParams = new RequestParams();
        if (!TextUtils.isEmpty(str)) {
            requestParams.put("couponID", str);
        }
        requestParams.put("orderID", str2);
        requestParams.put("orderprice", str3);
        if (!TextUtils.isEmpty(str4)) {
            requestParams.put("userID", str4);
        }
        if (i2 > 0) {
            requestParams.put("pointsamount", i2);
        }
        if (i2 > 0) {
            requestParams.put("discountamount", (Object) d);
        }
        if (d2.doubleValue() > 0.0d) {
            requestParams.put("discount", (Object) d2);
        }
        HttpUtils.post(context, Contants.COUNTPAY, requestParams, i, onResultListener, progressDialog);
    }

    public void searchVip(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("mobile", str);
        requestParams.put("orderprice", str2);
        HttpUtils.post(context, Contants.GETVIPCONPON, requestParams, i, onResultListener, progressDialog);
    }

    public void getOrderPay(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderid", str);
        HttpUtils.post(context, Contants.ORDERPAY, requestParams, i, onResultListener, progressDialog, true);
    }

    public void getBuliderOrderQR(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2, String str3, String str4, String str5, String str6, String str7, int i2) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderid", str);
        if (!TextUtils.isEmpty(str2)) {
            requestParams.put("jf", str2);
        }
        if (!TextUtils.isEmpty(str3)) {
            requestParams.put("vip_id", str3);
        }
        if (!TextUtils.isEmpty(str4)) {
            requestParams.put("coupon", str4);
        }
        if (!TextUtils.isEmpty(str5)) {
            requestParams.put("vipcoupon", str5);
        }
        if (Double.parseDouble(str6) > 0.0d) {
            requestParams.put("discount", str6);
        }
        if (!TextUtils.isEmpty(str7)) {
            requestParams.put("moneys", str7);
        }
        requestParams.put("flag", i2);
        HttpUtils.post(context, Contants.BULIDERORDERQR, requestParams, i, onResultListener, progressDialog);
    }

    public void finishOrders(int i, Context context, OnResultListener onResultListener, ProgressDialog progressDialog, String str, String str2) {
        RequestParams requestParams = new RequestParams();
        requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(context, SPUtils.STORE_ID));
        requestParams.put("orderid", str);
        requestParams.put("waiterid", str2);
        HttpUtils.post(context, Contants.FINISHORDERS, requestParams, i, onResultListener, progressDialog, true);
    }
}
