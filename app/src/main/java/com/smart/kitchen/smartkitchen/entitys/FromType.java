package com.smart.kitchen.smartkitchen.entitys;

import android.content.Context;
import com.smart.kitchen.smartkitchen.R;
import java.util.ArrayList;
import java.util.List;

public class FromType {
    private int id;
    private String name;

    public FromType() {
    }

    public FromType(String str) {
        this.name = str;
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

    public static List<FromType> getSimpleData(Context context, String str) {
        int i = 0;
        List<FromType> arrayList = new ArrayList();
        String[] strArr = new String[0];
        if ("order0".equals(str)) {
            strArr = context.getResources().getStringArray(R.array.tangchi_order);
        }
        if ("order1".equals(str)) {
            strArr = context.getResources().getStringArray(R.array.waimai_order);
        }
        if ("history0".equals(str)) {
            strArr = context.getResources().getStringArray(R.array.tangchi_history);
        }
        if ("history1".equals(str)) {
            strArr = context.getResources().getStringArray(R.array.waimai_history);
        }
        while (i < strArr.length) {
            arrayList.add(new FromType(strArr[i]));
            i++;
        }
        return arrayList;
    }
}
