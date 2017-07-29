package com.smart.kitchen.smartkitchen.entitys;

import java.util.List;

public class WxPayBean {
    private String appid;
    private List<?> attach;
    private String bank_type;
    private String cash_fee;
    private String fee_type;
    private String is_subscribe;
    private String mch_id;
    private String nonce_str;
    private String openid;
    private String out_trade_no;
    private String result_code;
    private String return_code;
    private String return_msg;
    private String sign;
    private String time_end;
    private String total_fee;
    private String trade_state;
    private String trade_type;
    private String transaction_id;

    public String getAppid() {
        return this.appid;
    }

    public void setAppid(String str) {
        this.appid = str;
    }

    public String getBank_type() {
        return this.bank_type;
    }

    public void setBank_type(String str) {
        this.bank_type = str;
    }

    public String getCash_fee() {
        return this.cash_fee;
    }

    public void setCash_fee(String str) {
        this.cash_fee = str;
    }

    public String getFee_type() {
        return this.fee_type;
    }

    public void setFee_type(String str) {
        this.fee_type = str;
    }

    public String getIs_subscribe() {
        return this.is_subscribe;
    }

    public void setIs_subscribe(String str) {
        this.is_subscribe = str;
    }

    public String getMch_id() {
        return this.mch_id;
    }

    public void setMch_id(String str) {
        this.mch_id = str;
    }

    public String getNonce_str() {
        return this.nonce_str;
    }

    public void setNonce_str(String str) {
        this.nonce_str = str;
    }

    public String getOpenid() {
        return this.openid;
    }

    public void setOpenid(String str) {
        this.openid = str;
    }

    public String getOut_trade_no() {
        return this.out_trade_no;
    }

    public void setOut_trade_no(String str) {
        this.out_trade_no = str;
    }

    public String getResult_code() {
        return this.result_code;
    }

    public void setResult_code(String str) {
        this.result_code = str;
    }

    public String getReturn_code() {
        return this.return_code;
    }

    public void setReturn_code(String str) {
        this.return_code = str;
    }

    public String getReturn_msg() {
        return this.return_msg;
    }

    public void setReturn_msg(String str) {
        this.return_msg = str;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String str) {
        this.sign = str;
    }

    public String getTime_end() {
        return this.time_end;
    }

    public void setTime_end(String str) {
        this.time_end = str;
    }

    public String getTotal_fee() {
        return this.total_fee;
    }

    public void setTotal_fee(String str) {
        this.total_fee = str;
    }

    public String getTrade_state() {
        return this.trade_state;
    }

    public void setTrade_state(String str) {
        this.trade_state = str;
    }

    public String getTrade_type() {
        return this.trade_type;
    }

    public void setTrade_type(String str) {
        this.trade_type = str;
    }

    public String getTransaction_id() {
        return this.transaction_id;
    }

    public void setTransaction_id(String str) {
        this.transaction_id = str;
    }

    public List<?> getAttach() {
        return this.attach;
    }

    public void setAttach(List<?> list) {
        this.attach = list;
    }
}
