package com.smart.kitchen.smartkitchen.entitys;

public class JiaoJieInfo {
    private Double crmmoney;
    private Long id;
    private Long jiaoid;
    private Long jieid;
    private String mark;
    private Double money;
    private int nonpayedorder;
    private int ordercount;
    private Double restmoney;
    private int saleorder;
    private Double totalmoney;
    private String workofftime;
    private String workontime;

    public int getSaleorder() {
        return this.saleorder;
    }

    public void setSaleorder(int i) {
        this.saleorder = i;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public Long getJiaoid() {
        return this.jiaoid;
    }

    public void setJiaoid(Long l) {
        this.jiaoid = l;
    }

    public Long getJieid() {
        return this.jieid;
    }

    public void setJieid(Long l) {
        this.jieid = l;
    }

    public Double getMoney() {
        return this.money;
    }

    public void setMoney(Double d) {
        this.money = d;
    }

    public Double getCrmmoney() {
        return this.crmmoney;
    }

    public void setCrmmoney(Double d) {
        this.crmmoney = d;
    }

    public Double getTotalmoney() {
        return this.totalmoney;
    }

    public void setTotalmoney(Double d) {
        this.totalmoney = d;
    }

    public int getOrdercount() {
        return this.ordercount;
    }

    public void setOrdercount(int i) {
        this.ordercount = i;
    }

    public int getNonpayedorder() {
        return this.nonpayedorder;
    }

    public void setNonpayedorder(int i) {
        this.nonpayedorder = i;
    }

    public String getWorkontime() {
        return this.workontime;
    }

    public void setWorkontime(String str) {
        this.workontime = str;
    }

    public String getWorkofftime() {
        return this.workofftime;
    }

    public void setWorkofftime(String str) {
        this.workofftime = str;
    }

    public Double getRestmoney() {
        return this.restmoney;
    }

    public void setRestmoney(Double d) {
        this.restmoney = d;
    }

    public String getMark() {
        return this.mark;
    }

    public void setMark(String str) {
        this.mark = str;
    }
}
