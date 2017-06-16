package com.smart.kitchen.smartkitchen.entitys;

public class Conpon {
    private Double amount;
    private String coupon_code;
    private String date;
    private String detail;
    private String id;
    private String name;
    private int type;
    private String userID;

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String str) {
        this.userID = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getCoupon_code() {
        return this.coupon_code;
    }

    public void setCoupon_code(String str) {
        this.coupon_code = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String str) {
        this.detail = str;
    }

    public Double getAmount() {
        return this.amount;
    }

    public void setAmount(Double d) {
        this.amount = d;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String str) {
        this.date = str;
    }

    public String toString() {
        return "Conpon{userID='" + this.userID + '\'' + ", id='" + this.id + '\'' + ", coupon_code='" + this.coupon_code + '\'' + ", type=" + this.type + ", name='" + this.name + '\'' + ", detail='" + this.detail + '\'' + ", amount=" + this.amount + ", date='" + this.date + '\'' + '}';
    }
}
