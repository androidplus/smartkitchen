package com.smart.kitchen.smartkitchen.entitys;

public class TableNumber {
    private String area_name;
    private Integer eating_count;
    private Long id;
    private Long pid;
    private String table_name;
    private Integer table_person;
    private Integer table_type_count;

    public TableNumber() {
    }

    public TableNumber(Long l) {
        this.id = l;
    }

    public TableNumber(Long l, String str, String str2, Long l2, Integer num, Integer num2, Integer num3) {
        this.id = l;
        this.area_name = str;
        this.table_name = str2;
        this.pid = l2;
        this.table_person = num;
        this.eating_count = num2;
        this.table_type_count = num3;
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

    public String getTable_name() {
        return this.table_name;
    }

    public void setTable_name(String str) {
        this.table_name = str;
    }

    public Long getPid() {
        return this.pid;
    }

    public void setPid(Long l) {
        this.pid = l;
    }

    public Integer getTable_person() {
        return this.table_person;
    }

    public void setTable_person(Integer num) {
        this.table_person = num;
    }

    public Integer getEating_count() {
        return this.eating_count;
    }

    public void setEating_count(Integer num) {
        this.eating_count = num;
    }

    public Integer getTable_type_count() {
        return this.table_type_count;
    }

    public void setTable_type_count(Integer num) {
        this.table_type_count = num;
    }
}
