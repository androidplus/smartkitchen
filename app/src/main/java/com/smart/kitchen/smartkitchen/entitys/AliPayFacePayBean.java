package com.smart.kitchen.smartkitchen.entitys;

public class AliPayFacePayBean {
    private String buyer_pay_amount;
    private String code;
    private String invoice_amount;
    private String msg;
    private String out_trade_no;
    private String point_amount;
    private String receipt_amount;
    private String sub_code;
    private String sub_msg;

    public String getCode() {
        return this.code;
    }

    public void setCode(String str) {
        this.code = str;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public String getSub_code() {
        return this.sub_code;
    }

    public void setSub_code(String str) {
        this.sub_code = str;
    }

    public String getSub_msg() {
        return this.sub_msg;
    }

    public void setSub_msg(String str) {
        this.sub_msg = str;
    }

    public String getBuyer_pay_amount() {
        return this.buyer_pay_amount;
    }

    public void setBuyer_pay_amount(String str) {
        this.buyer_pay_amount = str;
    }

    public String getInvoice_amount() {
        return this.invoice_amount;
    }

    public void setInvoice_amount(String str) {
        this.invoice_amount = str;
    }

    public String getOut_trade_no() {
        return this.out_trade_no;
    }

    public void setOut_trade_no(String str) {
        this.out_trade_no = str;
    }

    public String getPoint_amount() {
        return this.point_amount;
    }

    public void setPoint_amount(String str) {
        this.point_amount = str;
    }

    public String getReceipt_amount() {
        return this.receipt_amount;
    }

    public void setReceipt_amount(String str) {
        this.receipt_amount = str;
    }
}
