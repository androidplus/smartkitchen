package com.smart.kitchen.smartkitchen.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import java.util.ArrayList;
import java.util.List;

public class StringTableNumber {
    public String getGsonTableNum(List<TableNumber> list) {
        return JSON.toJSONString(list);
    }

    public List<TableNumber> getGsonTableNum(String str) {
        return (List) JSON.parseObject(str, new TypeReference<List<TableNumber>>() {
        }, new Feature[0]);
    }

    public List<String> getStringTableName(List<TableNumber> list) {
        List<String> arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            arrayList.add(((TableNumber) list.get(i)).getArea_name() + "区" + ((TableNumber) list.get(i)).getTable_name() + "桌");
        }
        return arrayList;
    }

    public List<String> getListTable(String str) {
        return getStringTableName(getGsonTableNum(str));
    }
}
