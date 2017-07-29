package com.smart.kitchen.smartkitchen.entitys;

public class UserInfo {
    private String account;
    private String authority;
    private Long id;
    private Long onwerid;
    private String password;
    private String phone;
    private String realname;
    private String role;
    private String userid;
    private String workofftime;
    private String workontime;

    public UserInfo() {
    }

    public UserInfo(Long l) {
        this.id = l;
    }

    public UserInfo(Long l, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9, Long l2) {
        this.id = l;
        this.userid = str;
        this.account = str2;
        this.password = str3;
        this.realname = str4;
        this.phone = str5;
        this.role = str6;
        this.workontime = str7;
        this.workofftime = str8;
        this.authority = str9;
        this.onwerid = l2;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String str) {
        this.userid = str;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String str) {
        this.account = str;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String str) {
        this.password = str;
    }

    public String getRealname() {
        return this.realname;
    }

    public void setRealname(String str) {
        this.realname = str;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String str) {
        this.role = str;
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

    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String str) {
        this.authority = str;
    }

    public Long getOnwerid() {
        return this.onwerid;
    }

    public void setOnwerid(Long l) {
        this.onwerid = l;
    }

    public String toString() {
        return "UserInfo{id=" + this.id + ", userid='" + this.userid + '\'' + ", account='" + this.account + '\'' + ", password='" + this.password + '\'' + ", realname='" + this.realname + '\'' + ", phone='" + this.phone + '\'' + ", role='" + this.role + '\'' + ", workontime='" + this.workontime + '\'' + ", workofftime='" + this.workofftime + '\'' + ", authority='" + this.authority + '\'' + ", onwerid=" + this.onwerid + '}';
    }
}
