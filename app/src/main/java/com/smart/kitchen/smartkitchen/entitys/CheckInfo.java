package com.smart.kitchen.smartkitchen.entitys;

public class CheckInfo {
    private String changetime;
    private Integer changevalue;
    private Integer enable;
    private Long goodsid;
    private Long id;
    private String mark;
    private String operator;
    private Integer stocks;

    public CheckInfo() {
    }

    public CheckInfo(Long l) {
        this.id = l;
    }

    public CheckInfo(Long l, Long l2, Integer num, String str, String str2, Integer num2, String str3, Integer num3) {
        this.id = l;
        this.goodsid = l2;
        this.stocks = num;
        this.operator = str;
        this.changetime = str2;
        this.changevalue = num2;
        this.mark = str3;
        this.enable = num3;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public Long getGoodsid() {
        return this.goodsid;
    }

    public void setGoodsid(Long l) {
        this.goodsid = l;
    }

    public Integer getStocks() {
        return this.stocks;
    }

    public void setStocks(Integer num) {
        this.stocks = num;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String str) {
        this.operator = str;
    }

    public String getChangetime() {
        return this.changetime;
    }

    public void setChangetime(String str) {
        this.changetime = str;
    }

    public Integer getChangevalue() {
        return this.changevalue;
    }

    public void setChangevalue(Integer num) {
        this.changevalue = num;
    }

    public String getMark() {
        return this.mark;
    }

    public void setMark(String str) {
        this.mark = str;
    }

    public Integer getEnable() {
        return this.enable;
    }

    public void setEnable(Integer num) {
        this.enable = num;
    }
}
