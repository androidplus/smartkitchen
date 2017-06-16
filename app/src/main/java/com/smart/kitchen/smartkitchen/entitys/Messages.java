package com.smart.kitchen.smartkitchen.entitys;

import java.io.Serializable;

public class Messages implements Serializable {
    private Object data;
    private int flag;
    private String msg;

    public Messages() {
    }

    public Messages(int i, String str, Object obj) {
        this.flag = i;
        this.msg = str;
        this.data = obj;
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int i) {
        this.flag = i;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String str) {
        this.msg = str;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object obj) {
        this.data = obj;
    }
}
