package com.smart.kitchen.smartkitchen.entitys;

public class AliPayQrPayBean {
    private String code;
    private String msg;
    private String out_trade_no;
    private String qr_code;

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

    public String getOut_trade_no() {
        return this.out_trade_no;
    }

    public void setOut_trade_no(String str) {
        this.out_trade_no = str;
    }

    public String getQr_code() {
        return this.qr_code;
    }

    public void setQr_code(String str) {
        this.qr_code = str;
    }
}
