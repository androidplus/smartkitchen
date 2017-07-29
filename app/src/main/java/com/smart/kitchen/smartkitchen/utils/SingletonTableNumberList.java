package com.smart.kitchen.smartkitchen.utils;

import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import java.util.ArrayList;
import java.util.List;

public class SingletonTableNumberList {
    private static SingletonTableNumberList instance = null;
    private List<TableNumber> selectList = new ArrayList();

    public static synchronized SingletonTableNumberList getInstance() {
        SingletonTableNumberList singletonTableNumberList;
        synchronized (SingletonTableNumberList.class) {
            if (instance == null) {
                instance = new SingletonTableNumberList();
            }
            singletonTableNumberList = instance;
        }
        return singletonTableNumberList;
    }

    public List<TableNumber> getSelectList() {
        return this.selectList;
    }

    public void clear() {
        this.selectList.clear();
    }
}
