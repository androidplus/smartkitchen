package com.smart.kitchen.smartkitchen.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.entitys.Goods;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import java.util.List;

public class OrderUtils {
    public Double getTotalmoney(String str) {
        List list = (List) JSON.parseObject(str, new TypeReference<List<OrderGoods>>() {
        }, new Feature[0]);
        Double valueOf = Double.valueOf(0.0d);
        for (int i = 0; i < list.size(); i++) {
            if (((OrderGoods) list.get(i)).getStatus() != 4) {
                valueOf = Double.valueOf(valueOf.doubleValue() + (((double) ((OrderGoods) list.get(i)).getCount()) * ((OrderGoods) list.get(i)).getGoods().getMoney().doubleValue()));
            }
        }
        return valueOf;
    }

    public Double getTotalmoney(List<OrderGoods> list) {
        Double valueOf = Double.valueOf(0.0d);
        for (int i = 0; i < list.size(); i++) {
            if (((OrderGoods) list.get(i)).getStatus() != 4) {
                valueOf = Double.valueOf(valueOf.doubleValue() + (((double) ((OrderGoods) list.get(i)).getCount()) * ((OrderGoods) list.get(i)).getGoods().getMoney().doubleValue()));
            }
        }
        return valueOf;
    }

    public Double getTotalmoneyGoods(List<Goods> list) {
        Double valueOf = Double.valueOf(0.0d);
        int i = 0;
        while (i < list.size()) {
            i++;
            valueOf = Double.valueOf(valueOf.doubleValue() + (((double) ((Goods) list.get(i)).getCount().intValue()) * ((Goods) list.get(i)).getMoney().doubleValue()));
        }
        return valueOf;
    }

    public int getCount(String str) {
        List list = (List) JSON.parseObject(str, new TypeReference<List<OrderGoods>>() {
        }, new Feature[0]);
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (((OrderGoods) list.get(i2)).getStatus() != 4) {
                i += ((OrderGoods) list.get(i2)).getCount();
            }
        }
        return i;
    }

    public int getCount(List<OrderGoods> list) {
        int i = 0;
        int size = list.size() - 1;
        while (size >= 0) {
            int count;
            OrderGoods orderGoods = (OrderGoods) list.get(size);
            if (((OrderGoods) list.get(size)).getStatus() != 4) {
                count = ((OrderGoods) list.get(size)).getCount() + i;
            } else {
                count = i;
            }
            size--;
            i = count;
        }
        return i;
    }
}
