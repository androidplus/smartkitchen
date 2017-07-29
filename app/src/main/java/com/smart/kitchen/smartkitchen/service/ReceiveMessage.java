package com.smart.kitchen.smartkitchen.service;

import java.util.ArrayList;
import java.util.List;

public class ReceiveMessage {
    private static final String TAG = "ReceiveMessage";

    public static List<String> splite(String str) {
        String[] split = str.replaceAll("\\s*", "").replace("}{", "}-{").split("-");
        List<String> arrayList = new ArrayList();
        for (String add : split) {
            arrayList.add(add);
        }
        return arrayList;
    }
}
