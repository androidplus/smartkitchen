package com.smart.kitchen.smartkitchen.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.entitys.Role;

public class AuthorityUtil {
    private static AuthorityUtil instance = null;
    String authorityStr = new UserInfoDaoManager().getNowUserInfo().getAuthority();
    public String reg;
    String roleStr = new UserInfoDaoManager().getNowUserInfo().getRole();

    public static AuthorityUtil getInstance() {
        if (instance == null) {
            instance = new AuthorityUtil();
        }
        return instance;
    }

    public static void setInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    public int getRoleFlag() {
        return ((Role) JSON.parseObject(this.roleStr, new TypeReference<Role>() {
        }, new Feature[0])).getFlag().intValue();
    }

    public boolean permitCollectMoney() {
        this.reg = ".*1.*";
        if (this.authorityStr.matches(this.reg)) {
            return true;
        }
        return false;
    }

    public boolean permitOrder() {
        this.reg = ".*2.*";
        if (this.authorityStr.matches(this.reg)) {
            return true;
        }
        return false;
    }

    public boolean permitPrint() {
        this.reg = ".*3.*";
        if (this.authorityStr.matches(this.reg)) {
            return true;
        }
        return false;
    }

    public boolean permitCheck() {
        this.reg = ".*4.*";
        if (this.authorityStr.matches(this.reg)) {
            return true;
        }
        return false;
    }

    public boolean permitPCLogin() {
        this.reg = ".*5.*";
        if (this.authorityStr.matches(this.reg)) {
            return true;
        }
        return false;
    }

    public boolean permitWarning() {
        this.reg = ".*6.*";
        if (this.authorityStr.matches(this.reg)) {
            return true;
        }
        return false;
    }

    public boolean permitAddVip() {
        this.reg = ".*7.*";
        if (this.authorityStr.matches(this.reg)) {
            return true;
        }
        return false;
    }

    public boolean permitBreakOrder() {
        this.reg = ".*8.*";
        if (this.authorityStr.matches(this.reg)) {
            return true;
        }
        return false;
    }

    public boolean permitBreakage() {
        this.reg = ".*9.*";
        if (this.authorityStr.matches(this.reg)) {
            return true;
        }
        return false;
    }
}
