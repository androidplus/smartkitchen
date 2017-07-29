package com.smart.kitchen.smartkitchen.entitys;

public class DeliveryPersonnel {
    private String id;
    private String img_url;
    private String jie_time;
    private String name;
    private String orderid;
    private String phone;
    private String status;

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getOrderid() {
        return this.orderid;
    }

    public void setOrderid(String str) {
        this.orderid = str;
    }

    public String getJie_time() {
        return this.jie_time;
    }

    public void setJie_time(String str) {
        this.jie_time = str;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getImg_url() {
        return this.img_url;
    }

    public void setImg_url(String str) {
        this.img_url = str;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public String toString() {
        return "DeliveryPersonnel{id='" + this.id + '\'' + ", img_url='" + this.img_url + '\'' + ", name='" + this.name + '\'' + ", phone='" + this.phone + '\'' + ", status='" + this.status + '\'' + ", jie_time='" + this.jie_time + '\'' + ", orderid='" + this.orderid + '\'' + '}';
    }
}
