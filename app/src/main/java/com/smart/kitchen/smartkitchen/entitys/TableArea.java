package com.smart.kitchen.smartkitchen.entitys;

public class TableArea {
    private String area_name;
    private Long id;

    public TableArea() {
    }

    public TableArea(Long l) {
        this.id = l;
    }

    public TableArea(Long l, String str) {
        this.id = l;
        this.area_name = str;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public String getArea_name() {
        return this.area_name;
    }

    public void setArea_name(String str) {
        this.area_name = str;
    }
}
