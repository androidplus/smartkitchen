package com.smart.kitchen.smartkitchen.utils;

import com.smart.kitchen.smartkitchen.activity.MainActivity;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import java.util.Iterator;
import java.util.List;

public class ObjectEquals {
    public static boolean getOrder(List<OrderGoods> list, OrderGoods orderGoods) {
        if (list.size() <= 0) {
            return false;
        }
        int i = 0;
        boolean z = false;
        while (i < list.size()) {
            if (orderGoods.getGoods().getId().equals(((OrderGoods) list.get(i)).getGoods().getId()) && orderGoods.getGoodsize().getId().equals(((OrderGoods) list.get(i)).getGoodsize().getId())) {
                if (orderGoods.getGoodtaste() == null && ((OrderGoods) list.get(i)).getGoodtaste() == null) {
                    z = true;
                }
                if (!(orderGoods.getGoodtaste() == null || ((OrderGoods) list.get(i)).getGoodtaste() == null || !orderGoods.getGoodtaste().getId().equals(((OrderGoods) list.get(i)).getGoodtaste().getId()))) {
                    z = true;
                }
            }
            i++;
        }
        return z;
    }

    public static void addOrder(List<OrderGoods> list, OrderGoods orderGoods) {
        int i;
        OrderGoods orderGoods2;
        Object obj = null;
        if (list.size() > 0) {
            i = 0;
            while (i < list.size()) {
                if (orderGoods.getGoods().getId().equals(((OrderGoods) list.get(i)).getGoods().getId()) && orderGoods.getGoodsize().getId().equals(((OrderGoods) list.get(i)).getGoodsize().getId())) {
                    if (orderGoods.getGoodtaste() != null && ((OrderGoods) list.get(i)).getGoodtaste() == null) {
                        return;
                    }
                    if (orderGoods.getGoodtaste() != null || ((OrderGoods) list.get(i)).getGoodtaste() == null) {
                        if (orderGoods.getGoodtaste() == null && ((OrderGoods) list.get(i)).getGoodtaste() == null) {
                            orderGoods2 = (OrderGoods) list.get(i);
                            obj = 1;
                            break;
                        } else if (orderGoods.getGoodtaste().getId().equals(((OrderGoods) list.get(i)).getGoodtaste().getId())) {
                            orderGoods2 = (OrderGoods) list.get(i);
                            int i2 = 1;
                            break;
                        }
                    } else {
                        return;
                    }
                }
                i++;
            }
        }
        i = -1;
        orderGoods2 = null;
        if (obj != null) {
            orderGoods2.setCount(orderGoods2.getCount() + orderGoods.getCount());
            MainActivity.shoppingCarMap.set(i, orderGoods2);
            return;
        }
        MainActivity.shoppingCarMap_Key.add(orderGoods.getGoods().getId() + "");
        MainActivity.shoppingCarMap.add(orderGoods);
    }

    public static int getGoodsCount(List<OrderGoods> list, String str) {
        if (list.size() <= 0) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (String.valueOf(((OrderGoods) list.get(i2)).getGoods().getId()).equals(str)) {
                i += ((OrderGoods) list.get(i2)).getCount();
            }
        }
        return i;
    }

    public static void removeGoodsCount(OrderGoods orderGoods) {
        if (MainActivity.shoppingCarMap.size() > 0) {
            int i = 0;
            while (i < MainActivity.shoppingCarMap.size()) {
                if (orderGoods.getGoods().getId().equals(((OrderGoods) MainActivity.shoppingCarMap.get(i)).getGoods().getId()) && orderGoods.getGoodsize().getId().equals(((OrderGoods) MainActivity.shoppingCarMap.get(i)).getGoodsize().getId())) {
                    if (orderGoods.getGoodtaste() != null) {
                        if (((OrderGoods) MainActivity.shoppingCarMap.get(i)).getGoodtaste() == null) {
                            return;
                        }
                        if (orderGoods.getGoodtaste().getId().equals(((OrderGoods) MainActivity.shoppingCarMap.get(i)).getGoodtaste().getId())) {
                            ((OrderGoods) MainActivity.shoppingCarMap.get(i)).setCount(orderGoods.getCount());
                        }
                    } else if (((OrderGoods) MainActivity.shoppingCarMap.get(i)).getGoodtaste() == null) {
                        ((OrderGoods) MainActivity.shoppingCarMap.get(i)).setCount(orderGoods.getCount());
                    } else {
                        return;
                    }
                }
                i++;
            }
        }
    }

    public static void removeGoods(OrderGoods orderGoods) {
        Iterator it = MainActivity.shoppingCarMap.iterator();
        int i = 0;
        while (it.hasNext()) {
            OrderGoods orderGoods2 = (OrderGoods) it.next();
            i++;
            if (orderGoods.getGoods().getId().equals(orderGoods2.getGoods().getId()) && orderGoods.getGoodsize().getId().equals(orderGoods2.getGoodsize().getId())) {
                int i2;
                if (orderGoods.getGoodtaste() != null) {
                    if (orderGoods2.getGoodtaste() == null) {
                        return;
                    }
                    if (orderGoods.getGoodtaste().getId().equals(orderGoods2.getGoodtaste().getId())) {
                        it.remove();
                        MainActivity.shoppingCarMap_Key.remove(i - 1);
                        i2 = i - 1;
                        return;
                    }
                } else if (orderGoods2.getGoodtaste() == null) {
                    it.remove();
                    MainActivity.shoppingCarMap_Key.remove(i - 1);
                    i2 = i - 1;
                    return;
                } else {
                    return;
                }
            }
        }
    }
}
