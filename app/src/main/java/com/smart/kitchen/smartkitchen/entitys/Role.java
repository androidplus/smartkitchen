package com.smart.kitchen.smartkitchen.entitys;

public class Role {
    private Integer flag;
    private Long id;
    private String rolename;

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public String getRolename() {
        return this.rolename;
    }

    public void setRolename(String str) {
        this.rolename = str;
    }

    public Integer getFlag() {
        return this.flag;
    }

    public void setFlag(Integer num) {
        this.flag = num;
    }

    public String toString() {
        return "Role{id=" + this.id + ", rolename='" + this.rolename + '\'' + ", flag=" + this.flag + '}';
    }
}
