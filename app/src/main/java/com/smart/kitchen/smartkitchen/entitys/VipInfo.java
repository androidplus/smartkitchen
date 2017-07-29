package com.smart.kitchen.smartkitchen.entitys;

import java.io.Serializable;

public class VipInfo implements Serializable {
    private int age;
    private String birth;
    private String displayname;
    private String email;
    private String extras;
    private int gender;
    private String id;
    private Double jf;
    private String mark;
    private Double money;
    private String phone;
    private String username;

    public Double getJf() {
        return this.jf;
    }

    public void setJf(Double d) {
        this.jf = d;
    }

    public Double getMoney() {
        return this.money;
    }

    public void setMoney(Double d) {
        this.money = d;
    }

    public String getExtras() {
        return this.extras;
    }

    public String getMark() {
        return this.mark;
    }

    public void setMark(String str) {
        this.mark = str;
    }

    public void setExtras(String str) {
        this.extras = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String str) {
        this.username = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getDisplayname() {
        return this.displayname;
    }

    public void setDisplayname(String str) {
        this.displayname = str;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public String getBirth() {
        return this.birth;
    }

    public void setBirth(String str) {
        this.birth = str;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int i) {
        this.age = i;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int i) {
        this.gender = i;
    }
}
