package com.smart.kitchen.smartkitchen.entitys;

public class Goods {
    private Integer count;
    private String delete_time;
    private String describe;
    private String goods_image_url;
    private String goods_size;
    private Long goods_type;
    private String goodsbarcode;
    private Long id;
    private int is_delete;
    private String is_warning_count;
    private Double money;
    private String name;
    private String orderid;
    private String taste;

    public Goods() {
    }

    public Goods(Long l) {
        this.id = l;
    }

    public Goods(Long l, String str, String str2, Long l2, String str3, String str4, String str5, String str6, String str7, String str8, Double d, Integer num, int i, String str9) {
        this.id = l;
        this.name = str;
        this.orderid = str2;
        this.goods_type = l2;
        this.goods_image_url = str3;
        this.is_warning_count = str4;
        this.goods_size = str5;
        this.taste = str6;
        this.describe = str7;
        this.goodsbarcode = str8;
        this.money = d;
        this.count = num;
        this.is_delete = i;
        this.delete_time = str9;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getOrderid() {
        return this.orderid;
    }

    public void setOrderid(String str) {
        this.orderid = str;
    }

    public Long getGoods_type() {
        return this.goods_type;
    }

    public void setGoods_type(Long l) {
        this.goods_type = l;
    }

    public String getGoods_image_url() {
        return this.goods_image_url;
    }

    public void setGoods_image_url(String str) {
        this.goods_image_url = str;
    }

    public String getIs_warning_count() {
        return this.is_warning_count;
    }

    public void setIs_warning_count(String str) {
        this.is_warning_count = str;
    }

    public String getGoods_size() {
        return this.goods_size;
    }

    public void setGoods_size(String str) {
        this.goods_size = str;
    }

    public String getTaste() {
        return this.taste;
    }

    public void setTaste(String str) {
        this.taste = str;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String str) {
        this.describe = str;
    }

    public String getGoodsbarcode() {
        return this.goodsbarcode;
    }

    public void setGoodsbarcode(String str) {
        this.goodsbarcode = str;
    }

    public Double getMoney() {
        return this.money;
    }

    public void setMoney(Double d) {
        this.money = d;
    }

    public Integer getCount() {
        return this.count;
    }

    public void setCount(Integer num) {
        this.count = num;
    }

    public int getIs_delete() {
        return this.is_delete;
    }

    public void setIs_delete(int i) {
        this.is_delete = i;
    }

    public String getDelete_time() {
        return this.delete_time;
    }

    public void setDelete_time(String str) {
        this.delete_time = str;
    }

    public String toString() {
        return "Goods{id=" + this.id + ", name='" + this.name + '\'' + ", orderid='" + this.orderid + '\'' + ", goods_type=" + this.goods_type + ", goods_image_url='" + this.goods_image_url + '\'' + ", is_warning_count='" + this.is_warning_count + '\'' + ", goods_size='" + this.goods_size + '\'' + ", taste='" + this.taste + '\'' + ", describe='" + this.describe + '\'' + ", goodsbarcode='" + this.goodsbarcode + '\'' + ", money=" + this.money + ", count=" + this.count + ", is_delete=" + this.is_delete + ", delete_time='" + this.delete_time + '\'' + '}';
    }
}
