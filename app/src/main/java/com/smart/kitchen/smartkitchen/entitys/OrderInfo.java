package com.smart.kitchen.smartkitchen.entitys;

import java.io.Serializable;

public class OrderInfo implements Serializable {
    private String conponContent;
    private Double conponMoney;
    private String goodslist;
    private Long id;
    private int is_need_refund;
    private Integer is_new;
    private int is_pay_his;
    private Integer is_qucan;
    private Integer is_yuyue;
    private String mark;
    private String orderdate;
    private String orderfrom;
    private String orderid;
    private String orderstatus;
    private Integer pay_status;
    private String pay_style;
    private int refund_opera;
    private int refund_res;
    private String song_starttime;
    private String songinfo;
    private String tablenumber;
    private String tablenumberid;
    private Double totalprice;
    private String userinfo;
    private Integer usersnum;
    private String waiterid;
    private String waitername;
    private String want_time;

    public int getIs_pay_his() {
        return this.is_pay_his;
    }

    public void setIs_pay_his(int i) {
        this.is_pay_his = i;
    }

    public OrderInfo() {
    }

    public OrderInfo(String str, String str2, String str3, String str4, Double d, String str5, String str6, String str7, Integer num, String str8, String str9, String str10, Integer num2, String str11, String str12, Integer num3, String str13, String str14, String str15, Integer num4, String str16, Double d2, int i, int i2, int i3, Long l, Integer num5) {
        this.orderid = str;
        this.orderfrom = str2;
        this.orderstatus = str3;
        this.orderdate = str4;
        this.totalprice = d;
        this.mark = str5;
        this.waiterid = str6;
        this.tablenumberid = str7;
        this.usersnum = num;
        this.tablenumber = str8;
        this.userinfo = str9;
        this.want_time = str10;
        this.is_yuyue = num2;
        this.songinfo = str11;
        this.song_starttime = str12;
        this.is_qucan = num3;
        this.pay_style = str13;
        this.goodslist = str14;
        this.waitername = str15;
        this.pay_status = num4;
        this.conponContent = str16;
        this.conponMoney = d2;
        this.refund_res = i;
        this.refund_opera = i2;
        this.is_need_refund = i3;
        this.id = l;
        this.is_new = num5;
    }

    public int getIs_need_refund() {
        return this.is_need_refund;
    }

    public void setIs_need_refund(int i) {
        this.is_need_refund = i;
    }

    public int getRefund_opera() {
        return this.refund_opera;
    }

    public void setRefund_opera(int i) {
        this.refund_opera = i;
    }

    public int getRefund_res() {
        return this.refund_res;
    }

    public void setRefund_res(int i) {
        this.refund_res = i;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public Integer getIs_new() {
        return this.is_new;
    }

    public void setIs_new(Integer num) {
        this.is_new = num;
    }

    public String getConponContent() {
        return this.conponContent;
    }

    public void setConponContent(String str) {
        this.conponContent = str;
    }

    public Double getConponMoney() {
        return this.conponMoney;
    }

    public void setConponMoney(Double d) {
        this.conponMoney = d;
    }

    public String getWaitername() {
        return this.waitername;
    }

    public void setWaitername(String str) {
        this.waitername = str;
    }

    public String getOrderid() {
        return this.orderid;
    }

    public void setOrderid(String str) {
        this.orderid = str;
    }

    public String getOrderfrom() {
        return this.orderfrom;
    }

    public void setOrderfrom(String str) {
        this.orderfrom = str;
    }

    public String getOrderstatus() {
        return this.orderstatus;
    }

    public void setOrderstatus(String str) {
        this.orderstatus = str;
    }

    public Double getTotalprice() {
        return this.totalprice;
    }

    public void setTotalprice(Double d) {
        this.totalprice = d;
    }

    public String getOrderdate() {
        return this.orderdate;
    }

    public void setOrderdate(String str) {
        this.orderdate = str;
    }

    public String getMark() {
        return this.mark;
    }

    public void setMark(String str) {
        this.mark = str;
    }

    public String getWaiterid() {
        return this.waiterid;
    }

    public void setWaiterid(String str) {
        this.waiterid = str;
    }

    public Integer getUsersnum() {
        return this.usersnum;
    }

    public void setUsersnum(Integer num) {
        this.usersnum = num;
    }

    public String getTablenumberid() {
        return this.tablenumberid;
    }

    public void setTablenumberid(String str) {
        this.tablenumberid = str;
    }

    public String getTablenumber() {
        return this.tablenumber;
    }

    public void setTablenumber(String str) {
        this.tablenumber = str;
    }

    public String getUserinfo() {
        return this.userinfo;
    }

    public void setUserinfo(String str) {
        this.userinfo = str;
    }

    public String getWant_time() {
        return this.want_time;
    }

    public void setWant_time(String str) {
        this.want_time = str;
    }

    public Integer getIs_yuyue() {
        return this.is_yuyue;
    }

    public void setIs_yuyue(Integer num) {
        this.is_yuyue = num;
    }

    public String getSonginfo() {
        return this.songinfo;
    }

    public void setSonginfo(String str) {
        this.songinfo = str;
    }

    public String getSong_starttime() {
        return this.song_starttime;
    }

    public void setSong_starttime(String str) {
        this.song_starttime = str;
    }

    public Integer getIs_qucan() {
        return this.is_qucan;
    }

    public void setIs_qucan(Integer num) {
        this.is_qucan = num;
    }

    public String getPay_style() {
        return this.pay_style;
    }

    public void setPay_style(String str) {
        this.pay_style = str;
    }

    public Integer getPay_status() {
        return this.pay_status;
    }

    public void setPay_status(Integer num) {
        this.pay_status = num;
    }

    public String getGoodslist() {
        return this.goodslist;
    }

    public void setGoodslist(String str) {
        this.goodslist = str;
    }

    public String toString() {
        return "OrderInfo{orderid='" + this.orderid + '\'' + ", orderfrom='" + this.orderfrom + '\'' + ", orderstatus='" + this.orderstatus + '\'' + ", totalprice=" + this.totalprice + ", orderdate='" + this.orderdate + '\'' + ", mark='" + this.mark + '\'' + ", waiterid='" + this.waiterid + '\'' + ", usersnum=" + this.usersnum + ", tablenumberid='" + this.tablenumberid + '\'' + ", tablenumber='" + this.tablenumber + '\'' + ", userinfo='" + this.userinfo + '\'' + ", want_time='" + this.want_time + '\'' + ", is_yuyue=" + this.is_yuyue + ", songinfo='" + this.songinfo + '\'' + ", song_starttime='" + this.song_starttime + '\'' + ", is_qucan=" + this.is_qucan + ", pay_style='" + this.pay_style + '\'' + ", goodslist='" + this.goodslist + '\'' + ", waitername='" + this.waitername + '\'' + ", pay_status=" + this.pay_status + ", conponContent='" + this.conponContent + '\'' + ", conponMoney=" + this.conponMoney + ", is_new=" + this.is_new + ", id=" + this.id + ", is_need_refund=" + this.is_need_refund + ", refund_opera=" + this.refund_opera + ", refund_res=" + this.refund_res + '}';
    }
}
