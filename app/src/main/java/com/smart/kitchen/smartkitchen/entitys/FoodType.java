package com.smart.kitchen.smartkitchen.entitys;

public class FoodType {
    private String catalog;
    private Long catalogid;
    private String delete_time;
    private Long id;

    public FoodType() {
    }

    public FoodType(Long l) {
        this.id = l;
    }

    public FoodType(Long l, String str, Long l2, String str2) {
        this.id = l;
        this.catalog = str;
        this.catalogid = l2;
        this.delete_time = str2;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public String getCatalog() {
        return this.catalog;
    }

    public void setCatalog(String str) {
        this.catalog = str;
    }

    public Long getCatalogid() {
        return this.catalogid;
    }

    public void setCatalogid(Long l) {
        this.catalogid = l;
    }

    public String getDelete_time() {
        return this.delete_time;
    }

    public void setDelete_time(String str) {
        this.delete_time = str;
    }
}
