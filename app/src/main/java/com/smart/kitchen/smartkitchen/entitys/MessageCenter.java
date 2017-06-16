package com.smart.kitchen.smartkitchen.entitys;

import java.io.Serializable;

public class MessageCenter implements Serializable {
    private Integer flag;
    private Long id;
    private String msg_content;
    private Long msg_id;
    private Integer msg_type;
    private Integer status;
    private String times;

    public MessageCenter() {
    }

    public MessageCenter(Long l) {
        this.id = l;
    }

    public MessageCenter(Long l, Long l2, Integer num, Integer num2, String str, Integer num3, String str2) {
        this.id = l;
        this.msg_id = l2;
        this.msg_type = num;
        this.flag = num2;
        this.msg_content = str;
        this.status = num3;
        this.times = str2;
    }

    public String getTimes() {
        return this.times;
    }

    public void setTimes(String str) {
        this.times = str;
    }

    public Integer getFlag() {
        return this.flag;
    }

    public void setFlag(Integer num) {
        this.flag = num;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public Long getMsg_id() {
        return this.msg_id;
    }

    public void setMsg_id(Long l) {
        this.msg_id = l;
    }

    public Integer getMsg_type() {
        return this.msg_type;
    }

    public void setMsg_type(Integer num) {
        this.msg_type = num;
    }

    public String getMsg_content() {
        return this.msg_content;
    }

    public void setMsg_content(String str) {
        this.msg_content = str;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer num) {
        this.status = num;
    }

    public String toString() {
        return "MessageCenter{id=" + this.id + ", msg_id=" + this.msg_id + ", msg_type=" + this.msg_type + ", flag=" + this.flag + ", msg_content='" + this.msg_content + '\'' + ", stutas=" + this.status + '}';
    }
}
