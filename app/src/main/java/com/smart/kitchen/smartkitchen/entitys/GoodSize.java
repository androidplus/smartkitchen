package com.smart.kitchen.smartkitchen.entitys;

public class GoodSize {
    private Integer count;
    private String goodsid;
    private Long id;
    private Double sale_price;
    private String spec_name;

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer num) {
        this.count = num;
    }

    public GoodSize() {
    }

    public GoodSize(Long l, String str, Double d, String str2) {
        this.id = l;
        this.spec_name = str;
        this.sale_price = d;
        this.goodsid = str2;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public String getSpec_name() {
        return this.spec_name;
    }

    public void setSpec_name(String str) {
        this.spec_name = str;
    }

    public Double getSale_price() {
        return this.sale_price;
    }

    public void setSale_price(Double d) {
        this.sale_price = d;
    }

    public String getGoodsid() {
        return this.goodsid;
    }

    public void setGoodsid(String str) {
        this.goodsid = str;
    }

    public String toString() {
        return "GoodSize{id=" + this.id + ", spec_name='" + this.spec_name + '\'' + ", sale_price=" + this.sale_price + ", goodsid='" + this.goodsid + '\'' + '}';
    }
}
