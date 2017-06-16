package com.smart.kitchen.smartkitchen.entitys;

public class LeftMenu {
    private String tag;
    private String title;
    private int title_icon;
    private int title_icon_ckecked;

    public LeftMenu() {
    }

    public LeftMenu(int i, int i2, String str, String str2) {
        this.title_icon = i;
        this.title_icon_ckecked = i2;
        this.title = str;
        this.tag = str2;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String str) {
        this.tag = str;
    }

    public int getTitle_icon_ckecked() {
        return this.title_icon_ckecked;
    }

    public void setTitle_icon_ckecked(int i) {
        this.title_icon_ckecked = i;
    }

    public int getTitle_icon() {
        return this.title_icon;
    }

    public void setTitle_icon(int i) {
        this.title_icon = i;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }
}
