package com.smart.kitchen.smartkitchen.entitys;

public class GoodTaste {
    private String goodsid;
    private Long id;
    private String tastename;

    public GoodTaste() {
    }

    public GoodTaste(Long l) {
        this.id = l;
    }

    public GoodTaste(Long l, String str, String str2) {
        this.id = l;
        this.tastename = str;
        this.goodsid = str2;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public String getTastename() {
        return this.tastename;
    }

    public void setTastename(String str) {
        this.tastename = str;
    }

    public String getGoodsid() {
        return this.goodsid;
    }

    public void setGoodsid(String str) {
        this.goodsid = str;
    }

    public String toString() {
        return "GoodTaste{id=" + this.id + ", tastename='" + this.tastename + '\'' + ", goodsid='" + this.goodsid + '\'' + '}';
    }
}
