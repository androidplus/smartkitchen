package com.smart.kitchen.smartkitchen.entitys;

import java.util.List;

public class VipConpon {
    private List<Conpon> coupons;
    private String discountamount;
    private String points;
    private String pointsamount;
    private String userID;

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String str) {
        this.userID = str;
    }

    public String getDiscountamount() {
        return this.discountamount;
    }

    public void setDiscountamount(String str) {
        this.discountamount = str;
    }

    public List<Conpon> getCoupons() {
        return this.coupons;
    }

    public void setCoupons(List<Conpon> list) {
        this.coupons = list;
    }

    public String getPoints() {
        return this.points;
    }

    public void setPoints(String str) {
        this.points = str;
    }

    public String getPointsamount() {
        return this.pointsamount;
    }

    public void setPointsamount(String str) {
        this.pointsamount = str;
    }

    public String toString() {
        return "VipConpon{userID='" + this.userID + '\'' + ", discountamount='" + this.discountamount + '\'' + ", points='" + this.points + '\'' + ", pointsamount='" + this.pointsamount + '\'' + ", coupons=" + this.coupons + '}';
    }
}
