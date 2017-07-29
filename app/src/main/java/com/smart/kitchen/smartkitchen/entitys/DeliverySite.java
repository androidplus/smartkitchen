package com.smart.kitchen.smartkitchen.entitys;

public class DeliverySite {
    private String address;
    private String city;
    private String latitude;
    private String longitude;
    private String mark;
    private String province;
    private String receiver;
    private String receiverphone;

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String str) {
        this.latitude = str;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String str) {
        this.longitude = str;
    }

    public String getProvince() {
        return this.province;
    }

    public void setProvince(String str) {
        this.province = str;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String str) {
        this.city = str;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public String getReceiver() {
        return this.receiver;
    }

    public void setReceiver(String str) {
        this.receiver = str;
    }

    public String getReceiverphone() {
        return this.receiverphone;
    }

    public void setReceiverphone(String str) {
        this.receiverphone = str;
    }

    public String getMark() {
        return this.mark;
    }

    public void setMark(String str) {
        this.mark = str;
    }

    public String toString() {
        return "DeliverySite{latitude='" + this.latitude + '\'' + ", longitude='" + this.longitude + '\'' + ", province='" + this.province + '\'' + ", city='" + this.city + '\'' + ", address='" + this.address + '\'' + ", receiver='" + this.receiver + '\'' + ", receiverphone='" + this.receiverphone + '\'' + ", mark='" + this.mark + '\'' + '}';
    }
}
