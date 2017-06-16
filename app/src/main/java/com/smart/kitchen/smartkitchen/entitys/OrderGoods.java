package com.smart.kitchen.smartkitchen.entitys;

public class OrderGoods {
    private int count;
    private Goods goods;
    private GoodSize goodsize;
    private GoodTaste goodtaste;
    private String mark;
    private String orderDetailId;
    private Double price;
    private int status;

    public String getOrderDetailId() {
        return this.orderDetailId;
    }

    public void setOrderDetailId(String str) {
        this.orderDetailId = str;
    }

    public Goods getGoods() {
        return this.goods;
    }

    public GoodSize getGoodsize() {
        return this.goodsize;
    }

    public void setGoodsize(GoodSize goodSize) {
        this.goodsize = goodSize;
    }

    public GoodTaste getGoodtaste() {
        return this.goodtaste;
    }

    public void setGoodtaste(GoodTaste goodTaste) {
        this.goodtaste = goodTaste;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int i) {
        this.count = i;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public String getMark() {
        return this.mark;
    }

    public void setMark(String str) {
        this.mark = str;
    }

    public String toString() {
        return "OrderGoods{goods=" + this.goods + ", goodsize=" + this.goodsize + ", goodtaste=" + this.goodtaste + ", count=" + this.count + ", status=" + this.status + '}';
    }
}
