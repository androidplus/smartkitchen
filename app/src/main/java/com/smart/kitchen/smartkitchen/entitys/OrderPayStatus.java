package com.smart.kitchen.smartkitchen.entitys;

public class OrderPayStatus {
    private Double payedmoney;
    private Double resetmoney;
    private String totalmoney;

    public String getTotalmoney() {
        return this.totalmoney;
    }

    public void setTotalmoney(String str) {
        this.totalmoney = str;
    }

    public Double getPayedmoney() {
        return this.payedmoney;
    }

    public void setPayedmoney(Double d) {
        this.payedmoney = d;
    }

    public Double getResetmoney() {
        return this.resetmoney;
    }

    public void setResetmoney(Double d) {
        this.resetmoney = d;
    }

    public String toString() {
        return "OrderPayStatus{totalmoney='" + this.totalmoney + '\'' + ", payedmoney=" + this.payedmoney + ", resetmoney=" + this.resetmoney + '}';
    }
}
