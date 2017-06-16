package com.smart.kitchen.smartkitchen.entitys;

public class StoreInfo {
    private String address;
    private String company;
    private String contact;
    private String dining_name;
    private long id;
    private String logo;
    private Long parent_id;
    private Double petty_cash;
    private String phone;
    private String status;

    public StoreInfo() {
    }

    public StoreInfo(long j, Long l, String str, String str2, String str3, String str4, String str5, String str6, String str7, Double d) {
        this.id = j;
        this.parent_id = l;
        this.dining_name = str;
        this.phone = str2;
        this.company = str3;
        this.contact = str4;
        this.address = str5;
        this.status = str6;
        this.logo = str7;
        this.petty_cash = d;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
    }

    public Long getParent_id() {
        return this.parent_id;
    }

    public void setParent_id(Long l) {
        this.parent_id = l;
    }

    public String getDining_name() {
        return this.dining_name;
    }

    public void setDining_name(String str) {
        this.dining_name = str;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public String getCompany() {
        return this.company;
    }

    public void setCompany(String str) {
        this.company = str;
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String str) {
        this.contact = str;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String str) {
        this.logo = str;
    }

    public Double getPetty_cash() {
        return this.petty_cash;
    }

    public void setPetty_cash(Double d) {
        this.petty_cash = d;
    }
}
