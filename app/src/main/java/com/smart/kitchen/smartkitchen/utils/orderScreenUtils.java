package com.smart.kitchen.smartkitchen.utils;

import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class orderScreenUtils {
    public List<OrderInfo> notifyList(List<OrderInfo> list, String str, String str2, String str3) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        Calendar instance3 = Calendar.getInstance();
        try {
            instance.setTime(simpleDateFormat.parse(str2));
            instance2.setTime(simpleDateFormat.parse(str3));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<OrderInfo> arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            try {
                instance3.setTime(simpleDateFormat.parse(((OrderInfo) list.get(i)).getOrderdate().substring(0, 16)));
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
            OrderInfo orderInfo = (OrderInfo) list.get(i);
            if (instance3.compareTo(instance) >= 0 && instance3.compareTo(instance2) <= 0) {
                if ("全部".equals(str)) {
                    arrayList.add(orderInfo);
                } else if ("堂吃".equals(str)) {
                    if ("tangchi".equals(orderInfo.getOrderfrom())) {
                        arrayList.add(orderInfo);
                    }
                } else if ("自助点单".equals(str)) {
                    if ("wxtangchi".equals(orderInfo.getOrderfrom())) {
                        arrayList.add(orderInfo);
                    }
                } else if ("成交单".equals(str)) {
                    if ("3".equals(orderInfo.getOrderstatus())) {
                        arrayList.add(orderInfo);
                    }
                } else if ("退货单".equals(str)) {
                    if ("4".equals(orderInfo.getOrderstatus())) {
                        arrayList.add(orderInfo);
                    } else if ("tuihuo".equals(orderInfo.getOrderfrom())) {
                        arrayList.add(orderInfo);
                    }
                } else if ("饿了么".equals(str)) {
                    if ("eleme".equals(orderInfo.getOrderfrom())) {
                        arrayList.add(orderInfo);
                    }
                } else if ("百度外卖".equals(str)) {
                    if ("baidu".equals(orderInfo.getOrderfrom())) {
                        arrayList.add(orderInfo);
                    }
                } else if ("美团外卖".equals(str)) {
                    if ("meituan".equals(orderInfo.getOrderfrom())) {
                        arrayList.add(orderInfo);
                    }
                } else if ("自助外卖".equals(str) && "wxwaimai".equals(orderInfo.getOrderfrom())) {
                    arrayList.add(orderInfo);
                }
            }
        }
        return arrayList;
    }

    public List<OrderInfo> notifyList(List<OrderInfo> list, String str) {
        List<OrderInfo> arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            OrderInfo orderInfo = (OrderInfo) list.get(i);
            if ("全部".equals(str)) {
                arrayList.add(orderInfo);
            } else if ("堂吃".equals(str)) {
                if ("tangchi".equals(orderInfo.getOrderfrom())) {
                    arrayList.add(orderInfo);
                }
            } else if ("自助点单".equals(str)) {
                if ("wxtangchi".equals(orderInfo.getOrderfrom())) {
                    arrayList.add(orderInfo);
                }
            } else if ("成交单".equals(str)) {
                if ("3".equals(orderInfo.getOrderstatus())) {
                    arrayList.add(orderInfo);
                }
            } else if ("退货单".equals(str)) {
                if ("4".equals(orderInfo.getOrderstatus())) {
                    arrayList.add(orderInfo);
                } else if ("tuihuo".equals(orderInfo.getOrderfrom())) {
                    arrayList.add(orderInfo);
                }
            } else if ("饿了么".equals(str)) {
                if ("eleme".equals(orderInfo.getOrderfrom())) {
                    arrayList.add(orderInfo);
                }
            } else if ("百度外卖".equals(str)) {
                if ("baidu".equals(orderInfo.getOrderfrom())) {
                    arrayList.add(orderInfo);
                }
            } else if ("美团外卖".equals(str)) {
                if ("meituan".equals(orderInfo.getOrderfrom())) {
                    arrayList.add(orderInfo);
                }
            } else if ("自助外卖".equals(str) && "wxwaimai".equals(orderInfo.getOrderfrom())) {
                arrayList.add(orderInfo);
            }
        }
        return arrayList;
    }
}
