package com.smart.kitchen.smartkitchen.entitys;

public class SongCanInfo {
    private int id;
    private String jie_time;
    private String name;
    private String phone;
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public String getJie_time() {
        return this.jie_time;
    }

    public void setJie_time(String str) {
        this.jie_time = str;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String str) {
        this.phone = str;
    }
}
