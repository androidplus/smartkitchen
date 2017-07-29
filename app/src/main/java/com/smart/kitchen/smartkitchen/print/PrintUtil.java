package com.smart.kitchen.smartkitchen.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.google.zxing.common.StringUtils;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.entitys.StoreInfo;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import com.smart.kitchen.smartkitchen.utils.ArithmeticUtils;
import com.smart.kitchen.smartkitchen.utils.BitmapUtils;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import driver.BitmapConvertUtil;
import driver.HsUsbPrintDriver;
import driver.HsWifiPrintDriver;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.HttpStatus;

public class PrintUtil {
    private static final int LEFT_LENGTH = 16;
    private static final int LEFT_TEXT_MAX_LENGTH = 6;
    private static final int RIGHT_LENGTH = 16;
    private static final String SEPARATOR = "$";
    private static final String TAG = "PrintUtil";
    private static PrintUtil instance = null;
    private static StringBuffer sb = new StringBuffer();
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    private UsbManager mUsbManager;

    public static synchronized PrintUtil getInstance() {
        PrintUtil printUtil;
        synchronized (PrintUtil.class) {
            if (instance == null) {
                instance = new PrintUtil();
            }
            printUtil = instance;
        }
        return printUtil;
    }

    private int getMaxLength(Object[] objArr) {
        int length = objArr.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int bytesLength = getBytesLength(objArr[i].toString());
            if (bytesLength <= i2) {
                bytesLength = i2;
            }
            i++;
            i2 = bytesLength;
        }
        return i2;
    }

    private int getBytesLength(String str) {
        return str.getBytes(Charset.forName(StringUtils.GB2312)).length;
    }

    private String printMenuMSG(LinkedHashMap<String, LinkedList<String>> linkedHashMap, int i, int i2) {
        String str;
        int i3;
        sb.delete(0, sb.length());
        List arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        for (List<String> it : linkedHashMap.values()) {
            for (String str2 : it) {
                if (str2.contains(SEPARATOR)) {
                    String[] split = str2.split("[$]");
                    if (!(split == null || split.length == 0)) {
                        arrayList.add(split[0]);
                        arrayList2.add(split[2]);
                    }
                }
            }
        }
        String str3 = "名称*规格 ";
        String str22 = "数量";
        if (i2 == 2) {
            str = "数量*口味";
        } else {
            str = str22;
        }
        str22 = " ";
        if (i2 == 1) {
            str22 = " 单价\n";
        } else {
            str22 = " 备注\n";
        }
        int maxLength = getMaxLength(arrayList.toArray());
        int maxLength2 = getMaxLength(arrayList2.toArray());
        if (maxLength2 < getBytesLength(str22)) {
            maxLength2 = getBytesLength(str22);
        }
        int bytesLength = (maxLength - getBytesLength(str3)) / 2;
        for (i3 = 0; i3 < bytesLength; i3++) {
            sb.append(" ");
        }
        sb.append(str3);
        int bytesLength2 = (((i - maxLength) - maxLength2) - getBytesLength(str)) / 2;
        for (i3 = 0; i3 < bytesLength2 + bytesLength; i3++) {
            sb.append(" ");
        }
        sb.append(str);
        i3 = (maxLength2 - getBytesLength(str22)) / 2;
        for (maxLength2 = 0; maxLength2 < bytesLength2 + i3; maxLength2++) {
            sb.append(" ");
        }
        sb.append(str22);
        for (Entry entry : linkedHashMap.entrySet()) {
            if (!"".equals(entry.getKey())) {
                sb.append(((String) entry.getKey()) + "\n");
            }
            Iterator it2 = ((LinkedList) entry.getValue()).iterator();
            while (it2.hasNext()) {
                str22 = (String) it2.next();
                if (str22.contains(SEPARATOR)) {
                    String[] split2 = str22.split("[$]");
                    if (!(split2 == null || split2.length == 0)) {
                        int i4;
                        sb.append(split2[0]);
                        for (i4 = 0; i4 < (((maxLength - getBytesLength(split2[0])) + bytesLength2) + (getBytesLength(str) / 2)) - 1; i4++) {
                            sb.append(" ");
                        }
                        sb.append(split2[1]);
                        for (i4 = 0; i4 < ((((getBytesLength(str) / 2) + bytesLength2) + 1) - getBytesLength(split2[1])) + i3; i4++) {
                            sb.append(" ");
                        }
                        sb.append(split2[2] + "\n");
                    }
                } else {
                    for (maxLength2 = 0; maxLength2 < (i / getBytesLength(str22)) - getBytesLength("\n"); maxLength2++) {
                        sb.append(str22);
                    }
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }

    public LinkedHashMap<String, LinkedList<String>> getMap(List<OrderGoods> list, int i) {
        LinkedHashMap<String, LinkedList<String>> linkedHashMap = new LinkedHashMap();
        LinkedList linkedList = new LinkedList();
        int i2 = 0;
        while (i2 < list.size()) {
            if (((OrderGoods) list.get(i2)).getStatus() != 4) {
                if (i == 1) {
                    linkedList.add(((OrderGoods) list.get(i2)).getGoods().getName() + "*" + ((OrderGoods) list.get(i2)).getGoodsize().getSpec_name() + " $ " + ((OrderGoods) list.get(i2)).getCount() + "" + " $ " + ((OrderGoods) list.get(i2)).getGoodsize().getSale_price());
                } else if (((OrderGoods) list.get(i2)).getMark() == null || TextUtils.isEmpty(((OrderGoods) list.get(i2)).getMark())) {
                    if (((OrderGoods) list.get(i2)).getGoodsize() != null) {
                        if (((OrderGoods) list.get(i2)).getGoodtaste() != null) {
                            linkedList.add(((OrderGoods) list.get(i2)).getGoods().getName() + "*" + ((OrderGoods) list.get(i2)).getGoodsize().getSpec_name() + " $ " + ((OrderGoods) list.get(i2)).getCount() + "*" + ((OrderGoods) list.get(i2)).getGoodtaste().getTastename() + " $ " + "无");
                        } else {
                            linkedList.add(((OrderGoods) list.get(i2)).getGoods().getName() + "*" + ((OrderGoods) list.get(i2)).getGoodsize().getSpec_name() + " $ " + ((OrderGoods) list.get(i2)).getCount() + "*无" + " $ " + "无");
                        }
                    }
                } else if (((OrderGoods) list.get(i2)).getGoodtaste() != null) {
                    linkedList.add(((OrderGoods) list.get(i2)).getGoods().getName() + "*" + ((OrderGoods) list.get(i2)).getGoodsize().getSpec_name() + " $ " + ((OrderGoods) list.get(i2)).getCount() + "*" + ((OrderGoods) list.get(i2)).getGoodtaste().getTastename() + " $ " + ((OrderGoods) list.get(i2)).getMark());
                } else {
                    linkedList.add(((OrderGoods) list.get(i2)).getGoods().getName() + "*" + ((OrderGoods) list.get(i2)).getGoodsize().getSpec_name() + " $ " + ((OrderGoods) list.get(i2)).getCount() + "*无" + " $ " + ((OrderGoods) list.get(i2)).getMark());
                }
            }
            i2++;
        }
        linkedHashMap.put("", linkedList);
        return linkedHashMap;
    }

    private String printThreeData(String str, String str2, String str3, int i) {
        int i2 = 0;
        StringBuilder stringBuilder = new StringBuilder();
        if (i == 1 && str.length() > 6) {
            str = str.substring(0, 6) + "..";
        }
        int bytesLength = getBytesLength(str);
        int bytesLength2 = getBytesLength(str2);
        int bytesLength3 = getBytesLength(str3);
        stringBuilder.append(str);
        int i3 = (16 - bytesLength) - (bytesLength2 / 2);
        for (bytesLength = 0; bytesLength < i3; bytesLength++) {
            stringBuilder.append(" ");
        }
        stringBuilder.append(str2);
        bytesLength = (16 - (bytesLength2 / 2)) - bytesLength3;
        while (i2 < bytesLength) {
            stringBuilder.append(" ");
            i2++;
        }
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length()).append(str3);
        return stringBuilder.toString();
    }

    private String printTwoData(String str, String str2, int i) {
        StringBuilder stringBuilder = new StringBuilder();
        int bytesLength = getBytesLength(str);
        int bytesLength2 = getBytesLength(str2);
        stringBuilder.append(str);
        bytesLength2 = (i - bytesLength) - bytesLength2;
        for (bytesLength = 0; bytesLength < bytesLength2; bytesLength++) {
            stringBuilder.append(" ");
        }
        stringBuilder.append(str2);
        return stringBuilder.toString();
    }

    private void hsPrePrint(Context context, OrderInfo orderInfo) {
        String str;
        int userinfos;
        StoreInfo storeInfo = (StoreInfo) JSON.parseObject(SPUtils.getUserinfo(context, SPUtils.STORE_INFO), new TypeReference<StoreInfo>() {
        }, new Feature[0]);
        List list = (List) JSON.parseObject(orderInfo.getGoodslist(), new TypeReference<List<OrderGoods>>() {
        }, new Feature[0]);
        List list2 = (List) JSON.parseObject(orderInfo.getTablenumber(), new TypeReference<List<TableNumber>>() {
        }, new Feature[0]);
        String format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        String str2 = "0";
        if (orderInfo.getConponMoney() != null) {
            str = orderInfo.getConponMoney() + "";
        } else {
            str = str2;
        }
        if (SPUtils.getUserinfos(context, SPUtils.SETTING_PRINT_PAPERSTYLE) != -1) {
            userinfos = SPUtils.getUserinfos(context, SPUtils.SETTING_PRINT_PAPERSTYLE);
        } else {
            userinfos = 0;
        }
        HsUsbPrintDriver instance = HsUsbPrintDriver.getInstance();
        instance.Begin();
        instance.SetDefaultSetting();
        instance.SetAlignMode((byte) 1);
        instance.SetCharacterPrintMode((byte) 16);
        instance.SelChineseCodepage();
        instance.SetChineseCharacterMode((byte) 12);
        if (list2 != null && list2.size() > 0) {
            for (int i = 0; i < list2.size(); i++) {
                instance.USB_Write(((TableNumber) list2.get(i)).getArea_name() + "区" + ((TableNumber) list2.get(i)).getTable_name() + "桌");
                instance.LF();
                instance.CR();
            }
        }
        instance.SetDefaultSetting();
        instance.SetAlignMode((byte) 0);
        String orderfrom = orderInfo.getOrderfrom();
        if ("tangchi".equals(orderfrom)) {
            instance.USB_Write("订单类型 :堂吃\n");
        } else if ("wxwaimai".equals(orderfrom)) {
            instance.USB_Write("订单类型 :微信外卖\n");
        } else if ("wxtangchi".equals(orderfrom)) {
            instance.USB_Write("订单类型 :微信堂吃\n");
        } else if ("meituan".equals(orderfrom)) {
            instance.USB_Write("订单类型 :美团外卖\n");
        } else if ("eleme".equals(orderfrom)) {
            instance.USB_Write("订单类型 :饿了么外卖\n");
        } else if ("baidu".equals(orderfrom)) {
            instance.USB_Write("订单类型 :百度外卖\n");
        }
        instance.USB_Write("订单编号 :" + orderInfo.getOrderid() + "\n");
        instance.USB_Write("点菜时间 :" + orderInfo.getOrderdate() + "\n");
        instance.USB_Write("打印时间 :" + format + "\n");
        switch (userinfos) {
            case 0:
                if (orderInfo.getUsersnum() == null) {
                    instance.USB_Write(printTwoData("收款人员 :" + new UserInfoDaoManager().getNowUserInfo().getUserid(), "就餐人数 :0 人\n", 32));
                } else {
                    instance.USB_Write(printTwoData("收款人员 :" + new UserInfoDaoManager().getNowUserInfo().getUserid(), "就餐人数 :" + orderInfo.getUsersnum() + " 人" + "\n", 32));
                }
                instance.USB_Write("--------------------------------\n");
                instance.USB_Write(printThreeData("名称*规格", "数量", "单价\n", 1));
                for (int i2 = 0; i2 < list.size(); i2++) {
                    if (((OrderGoods) list.get(i2)).getStatus() != 4) {
                        String str3 = ((OrderGoods) list.get(i2)).getGoods().getName() + "*" + ((OrderGoods) list.get(i2)).getGoodsize().getSpec_name();
                        String str4 = ((OrderGoods) list.get(i2)).getCount() + "";
                        orderfrom = "";
                        if (((OrderGoods) list.get(i2)).getGoodsize() == null) {
                            orderfrom = ((OrderGoods) list.get(i2)).getGoods().getMoney() + "";
                        } else {
                            orderfrom = ((OrderGoods) list.get(i2)).getGoodsize().getSale_price() + "";
                        }
                        instance.USB_Write(printThreeData(str3, str4, orderfrom + "\n", 1));
                    }
                }
                instance.USB_Write("--------------------------------\n");
                if (orderInfo.getMark() != null) {
                    instance.USB_Write(printTwoData("备注：", orderInfo.getMark(), 32));
                } else {
                    instance.USB_Write(printTwoData("备注：", " ", 32));
                }
                instance.USB_Write("--------------------------------\n");
                instance.USB_Write(printTwoData("合计", orderInfo.getTotalprice() + "\n", 32));
                instance.USB_Write(printTwoData("优惠", str + "\n", 32));
                instance.USB_Write(printTwoData("应收", ArithmeticUtils.sub(orderInfo.getTotalprice() + "", str, 2) + "\n", 32));
                break;
            case 1:
                if (orderInfo.getUsersnum() == null) {
                    instance.USB_Write(printTwoData("收款人员 :" + new UserInfoDaoManager().getNowUserInfo().getUserid(), "就餐人数 :0 人\n", 38));
                } else {
                    instance.USB_Write(printTwoData("收款人员 :" + new UserInfoDaoManager().getNowUserInfo().getUserid(), "就餐人数 :" + orderInfo.getUsersnum() + " 人" + "\n", 38));
                }
                instance.USB_Write("--------------------------------\n");
                instance.USB_Write(printMenuMSG(getMap(list, 1), 38, 1));
                instance.USB_Write("--------------------------------\n");
                if (orderInfo.getMark() != null) {
                    instance.USB_Write(printTwoData("备注：", orderInfo.getMark(), 38));
                } else {
                    instance.USB_Write(printTwoData("备注：", " ", 38));
                }
                instance.USB_Write("--------------------------------\n");
                instance.USB_Write(printTwoData("合计", orderInfo.getTotalprice() + "\n", 38));
                instance.USB_Write(printTwoData("优惠", str + "\n", 38));
                instance.USB_Write(printTwoData("应收", ArithmeticUtils.sub(orderInfo.getTotalprice() + "", str, 2) + "\n", 38));
                break;
        }
        instance.USB_Write("\n\n");
        instance.SetAlignMode((byte) 1);
        instance.USB_Write("谢谢惠顾\n");
        instance.SetAlignMode((byte) 0);
        instance.USB_Write("电话 :" + storeInfo.getPhone() + "\n");
        instance.USB_Write("地址 :" + storeInfo.getAddress() + "\n");
        instance.SetAlignMode((byte) 1);
        instance.USB_Write("欢迎光临--" + storeInfo.getDining_name() + "\n");
        instance.USB_Write("\n");
        if (SPUtils.getUserinfos2(context, SPUtils.SETTING_PRINT_LOGO)) {
            printLogo(context, instance);
            return;
        }
        instance.LF();
        instance.CR();
        instance.LF();
        instance.CR();
        instance.LF();
        instance.CR();
    }

    private void hsUnPayPrint(Context context, OrderInfo orderInfo) {
        StoreInfo storeInfo = (StoreInfo) JSON.parseObject(SPUtils.getUserinfo(context, SPUtils.STORE_INFO), new TypeReference<StoreInfo>() {
        }, new Feature[0]);
        List list = (List) JSON.parseObject(orderInfo.getGoodslist(), new TypeReference<List<OrderGoods>>() {
        }, new Feature[0]);
        List list2 = (List) JSON.parseObject(orderInfo.getTablenumber(), new TypeReference<List<TableNumber>>() {
        }, new Feature[0]);
        String format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        if (SPUtils.getUserinfos(context, SPUtils.SETTING_PRINT_PAPERSTYLE) != -1) {
            int userinfos = SPUtils.getUserinfos(context, SPUtils.SETTING_PRINT_PAPERSTYLE);
        } else {
            byte b = (byte) 0;
        }
        HsUsbPrintDriver instance = HsUsbPrintDriver.getInstance();
        instance.Begin();
        instance.SetDefaultSetting();
        instance.SetAlignMode((byte) 1);
        instance.SetCharacterPrintMode((byte) 16);
        instance.SelChineseCodepage();
        instance.SetChineseCharacterMode((byte) 12);
        if (list2 != null && list2.size() > 0) {
            for (int i = 0; i < list2.size(); i++) {
                instance.USB_Write(((TableNumber) list2.get(i)).getArea_name() + "区" + ((TableNumber) list2.get(i)).getTable_name() + "桌");
                instance.LF();
                instance.CR();
            }
        }
        instance.SetDefaultSetting();
        instance.SetAlignMode((byte) 0);
        String orderfrom = orderInfo.getOrderfrom();
        if ("tangchi".equals(orderfrom)) {
            instance.USB_Write("订单类型 :堂吃\n");
        } else if ("wxwaimai".equals(orderfrom)) {
            instance.USB_Write("订单类型 :微信外卖\n");
        } else if ("wxtangchi".equals(orderfrom)) {
            instance.USB_Write("订单类型 :微信堂吃\n");
        } else if ("meituan".equals(orderfrom)) {
            instance.USB_Write("订单类型 :美团外卖\n");
        } else if ("eleme".equals(orderfrom)) {
            instance.USB_Write("订单类型 :饿了么外卖\n");
        } else if ("baidu".equals(orderfrom)) {
            instance.USB_Write("订单类型 :百度外卖\n");
        }
        instance.USB_Write("订单编号 :" + orderInfo.getOrderid() + "\n");
        instance.USB_Write("点菜时间 :" + orderInfo.getOrderdate() + "\n");
        instance.USB_Write("打印时间 :" + format + "\n");
        int userinfos =0;
        switch (userinfos) {
            case 0:
                if (orderInfo.getUsersnum() == null) {
                    instance.USB_Write(printTwoData("收款人员 :" + new UserInfoDaoManager().getNowUserInfo().getUserid(), "就餐人数 :0 人\n", 32));
                } else {
                    instance.USB_Write(printTwoData("收款人员 :" + new UserInfoDaoManager().getNowUserInfo().getUserid(), "就餐人数 :" + orderInfo.getUsersnum() + " 人" + "\n", 32));
                }
                instance.USB_Write("--------------------------------\n");
                instance.USB_Write(printThreeData("名称*规格", "数量", "单价\n", 1));
                for (int i2 = 0; i2 < list.size(); i2++) {
                    if (((OrderGoods) list.get(i2)).getStatus() != 4) {
                        String str = ((OrderGoods) list.get(i2)).getGoods().getName() + "*" + ((OrderGoods) list.get(i2)).getGoodsize().getSpec_name();
                        String str2 = ((OrderGoods) list.get(i2)).getCount() + "";
                        orderfrom = "";
                        if (((OrderGoods) list.get(i2)).getGoodsize() == null) {
                            orderfrom = ((OrderGoods) list.get(i2)).getGoods().getMoney() + "";
                        } else {
                            orderfrom = ((OrderGoods) list.get(i2)).getGoodsize().getSale_price() + "";
                        }
                        instance.USB_Write(printThreeData(str, str2, orderfrom + "\n", 1));
                    }
                }
                instance.USB_Write("--------------------------------\n");
                if (orderInfo.getMark() != null) {
                    instance.USB_Write(printTwoData("备注：", orderInfo.getMark(), 32));
                } else {
                    instance.USB_Write(printTwoData("备注：", " ", 32));
                }
                instance.USB_Write("--------------------------------\n");
                instance.USB_Write(printTwoData("合计", orderInfo.getTotalprice() + "\n", 32));
                instance.USB_Write(printTwoData("优惠", "0\n", 32));
                instance.USB_Write(printTwoData("已收", "0\n", 32));
                break;
            case 1:
                if (orderInfo.getUsersnum() == null) {
                    instance.USB_Write(printTwoData("收款人员 :" + new UserInfoDaoManager().getNowUserInfo().getUserid(), "就餐人数 :0 人\n", 38));
                } else {
                    instance.USB_Write(printTwoData("收款人员 :" + new UserInfoDaoManager().getNowUserInfo().getUserid(), "就餐人数 :" + orderInfo.getUsersnum() + " 人" + "\n", 38));
                }
                instance.USB_Write("--------------------------------\n");
                instance.USB_Write(printMenuMSG(getMap(list, 1), 38, 1));
                instance.USB_Write("--------------------------------\n");
                if (orderInfo.getMark() != null) {
                    instance.USB_Write(printTwoData("备注：", orderInfo.getMark(), 38));
                } else {
                    instance.USB_Write(printTwoData("备注：", " ", 38));
                }
                instance.USB_Write("--------------------------------\n");
                instance.USB_Write(printTwoData("合计", orderInfo.getTotalprice() + "\n", 38));
                instance.USB_Write(printTwoData("优惠", "0\n", 38));
                instance.USB_Write(printTwoData("已收", "0\n", 38));
                break;
        }
        instance.USB_Write("\n\n");
        instance.SetAlignMode((byte) 1);
        instance.USB_Write("谢谢惠顾\n");
        instance.SetAlignMode((byte) 0);
        instance.USB_Write("电话 :" + storeInfo.getPhone() + "\n");
        instance.USB_Write("地址 :" + storeInfo.getAddress() + "\n");
        instance.SetAlignMode((byte) 1);
        instance.USB_Write("欢迎光临--" + storeInfo.getDining_name() + "\n");
        instance.USB_Write("\n");
        if (SPUtils.getUserinfos2(context, SPUtils.SETTING_PRINT_LOGO)) {
            printLogo(context, instance);
            return;
        }
        instance.LF();
        instance.CR();
        instance.LF();
        instance.CR();
        instance.LF();
        instance.CR();
    }

    public void printQRcode(Context context, String str) {
        Bitmap bitmap = null;
        int i = 0;
        if (SPUtils.getUserinfos(context, SPUtils.SETTING_PRINT_PAPERSTYLE) != -1) {
            i = SPUtils.getUserinfos(context, SPUtils.SETTING_PRINT_PAPERSTYLE);
        }
        switch (i) {
            case 0:
                bitmap = BitmapUtils.createQRCoderBitmap(str, 384, HttpStatus.SC_INTERNAL_SERVER_ERROR);
                break;
            case 1:
                bitmap = BitmapUtils.createQRCoderBitmap(str, 624, HttpStatus.SC_INTERNAL_SERVER_ERROR);
                break;
        }
        HsUsbPrintDriver instance = HsUsbPrintDriver.getInstance();
        instance.Begin();
        instance.SetDefaultSetting();
        instance.SetAlignMode((byte) 1);
        instance.USB_Write("扫一扫--即可完成支付");
        instance.printImage(bitmap, i);
        instance.USB_Write("\n\n\n\n\n");
    }

    public void printLogo(Context context, final HsUsbPrintDriver hsUsbPrintDriver) {
        Bitmap bitmap = null;
        int i = 0;
        if (SPUtils.getUserinfos(context, SPUtils.SETTING_PRINT_PAPERSTYLE) != -1) {
            i = SPUtils.getUserinfos(context, SPUtils.SETTING_PRINT_PAPERSTYLE);
        }
        String str = "";
        if (SPUtils.getUserinfo(context, SPUtils.SETTING_SELECT_LOGO) != null) {
            str = SPUtils.getUserinfo(context, SPUtils.SETTING_SELECT_LOGO);
            switch (i) {
                case 0:
                    bitmap = BitmapConvertUtil.decodeSampledBitmapFromUri(context, Uri.parse(str), 384, 300);
                    break;
                case 1:
                    bitmap = BitmapConvertUtil.decodeSampledBitmapFromUri(context, Uri.parse(str), 576, 300);
                    break;
            }
            hsUsbPrintDriver.SetAlignMode((byte) 1);
            hsUsbPrintDriver.printImage(bitmap, i);
            this.executorService.execute(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    hsUsbPrintDriver.LF();
                    hsUsbPrintDriver.CR();
                    hsUsbPrintDriver.LF();
                    hsUsbPrintDriver.CR();
                    hsUsbPrintDriver.LF();
                    hsUsbPrintDriver.CR();
                }
            });
            return;
        }
        hsUsbPrintDriver.LF();
        hsUsbPrintDriver.CR();
        hsUsbPrintDriver.LF();
        hsUsbPrintDriver.CR();
        hsUsbPrintDriver.LF();
        hsUsbPrintDriver.CR();
    }

    private void kitchenPrint(final Context context, final OrderInfo orderInfo) {
        int parseInt;
        int userinfos;
        LogUtils.e(TAG, "厨房打印" + orderInfo.toString());
        List list = (List) JSON.parseObject(orderInfo.getGoodslist(), new TypeReference<List<OrderGoods>>() {
        }, new Feature[0]);
        List list2 = (List) JSON.parseObject(orderInfo.getTablenumber(), new TypeReference<List<TableNumber>>() {
        }, new Feature[0]);
        String format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        if (SPUtils.getUserinfo(context, SPUtils.PRINT_FONT) != null) {
            parseInt = Integer.parseInt(SPUtils.getUserinfo(context, SPUtils.PRINT_FONT));
        } else {
            parseInt = 0;
        }
        if (SPUtils.getUserinfos(context, SPUtils.SETTING_PRINT_KITCHENSTYLE) != -1) {
            userinfos = SPUtils.getUserinfos(context, SPUtils.SETTING_PRINT_KITCHENSTYLE);
        } else {
            userinfos = 1;
        }
        String userinfo = SPUtils.getUserinfo(context, SPUtils.PRINT_KITCHEN_IP);
        int parseInt2 = Integer.parseInt(SPUtils.getUserinfo(context, SPUtils.PRINT_KITCHEN_PORT));
        HsWifiPrintDriver instance = HsWifiPrintDriver.getInstance();
        instance.WIFISocket(userinfo, parseInt2);
        instance.Begin();
        instance.SetDefaultSetting();
        instance.SetAlignMode((byte) 1);
        instance.SetCharacterPrintMode((byte) 16);
        instance.SelChineseCodepage();
        instance.SetChineseCharacterMode((byte) 12);
        if (list2 != null && list2.size() > 0) {
            for (parseInt2 = 0; parseInt2 < list2.size(); parseInt2++) {
                instance.WIFI_Write(((TableNumber) list2.get(parseInt2)).getArea_name() + "区" + ((TableNumber) list2.get(parseInt2)).getTable_name() + "桌");
                instance.LF();
                instance.CR();
            }
        }
        if (parseInt == 0) {
            instance.SetDefaultSetting();
        }
        instance.SetAlignMode((byte) 0);
        String orderfrom = orderInfo.getOrderfrom();
        if ("tangchi".equals(orderfrom)) {
            instance.WIFI_Write("订单类型 :堂吃\n");
        } else if ("wxwaimai".equals(orderfrom)) {
            instance.WIFI_Write("订单类型 :微信外卖\n");
        } else if ("wxtangchi".equals(orderfrom)) {
            instance.WIFI_Write("订单类型 :微信堂吃\n");
        } else if ("meituan".equals(orderfrom)) {
            instance.WIFI_Write("订单类型 :美团外卖\n");
        } else if ("eleme".equals(orderfrom)) {
            instance.WIFI_Write("订单类型 :饿了么外卖\n");
        } else if ("baidu".equals(orderfrom)) {
            instance.WIFI_Write("订单类型 :百度外卖\n");
        }
        instance.WIFI_Write("订单编号 :" + orderInfo.getOrderid() + "\n");
        instance.WIFI_Write("点菜时间 :" + orderInfo.getOrderdate() + "\n");
        instance.WIFI_Write("打印时间 :" + format + "\n");
        switch (userinfos) {
            case 0:
                if (orderInfo.getUsersnum() == null) {
                    instance.WIFI_Write(printTwoData("就餐人数", "0 人\n", 32));
                } else {
                    instance.WIFI_Write(printTwoData("就餐人数", orderInfo.getUsersnum() + " 人" + "\n", 32));
                }
                instance.WIFI_Write("-------------------------------------------\n");
                instance.WIFI_Write(printThreeData("名称*规格", "数量*口味", "备注\n", 1));
                for (int i = 0; i < list.size(); i++) {
                    if (((OrderGoods) list.get(i)).getStatus() != 4) {
                        String str = ((OrderGoods) list.get(i)).getGoods().getName() + "*" + ((OrderGoods) list.get(i)).getGoodsize().getSpec_name();
                        String str2 = ((OrderGoods) list.get(i)).getCount() + "";
                        String mark = ((OrderGoods) list.get(i)).getMark();
                        if (((OrderGoods) list.get(i)).getGoodtaste() != null) {
                            instance.WIFI_Write(printThreeData(str, str2 + "*" + ((OrderGoods) list.get(i)).getGoodtaste().getTastename(), mark + "\n", 2));
                        } else {
                            instance.WIFI_Write(printThreeData(str, str2 + "*无", mark + "\n", 2));
                        }
                    }
                }
                break;
            case 1:
                if (orderInfo.getUsersnum() == null) {
                    instance.WIFI_Write(printTwoData("就餐人数", "0 人\n", 38));
                } else {
                    instance.WIFI_Write(printTwoData("就餐人数", orderInfo.getUsersnum() + " 人" + "\n", 38));
                }
                instance.WIFI_Write("-------------------------------------------\n");
                instance.WIFI_Write(printMenuMSG(getMap(list, 2), 38, 2));
                break;
        }
        instance.WIFI_Write("-------------------------------------------\n");
        if (orderInfo.getMark() == null) {
            instance.WIFI_Write("备注 :\n");
        } else {
            instance.WIFI_Write("备注 :" + orderInfo.getMark() + "\n");
        }
        instance.WIFI_Write("-------------------------------------------\n");
        instance.WIFI_Write("\n\n\n\n\n\n");
        instance.CutPaper();
        instance.StatusInquiryFinish(3, new Handler(context.getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                Bundle data = message.getData();
                int i = data.getInt("flag");
                int i2 = data.getInt("state", -1);
                switch (i) {
                    case 3:
                        i2 &= 255;
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("id", orderInfo.getId() + "");
                        HttpUtils.post(context, Contants.UPDATE_POS, requestParams);
                        return;
                    default:
                        return;
                }
            }
        });
    }

    public void cashBoxTest(Context context) {
        HsUsbPrintDriver instance = HsUsbPrintDriver.getInstance();
        instance.Begin();
        instance.SetDefaultSetting();
        instance.OpenDrawer((byte) 0, (byte) 5, (byte) 0);
    }

    public void connectUSB(Context context) {
        if (this.mUsbManager == null) {
            this.mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        }
        for (final UsbDevice usbDevice : this.mUsbManager.getDeviceList().values()) {
            if (usbDevice.getDeviceClass() == 0 && usbDevice.getInterface(0).getInterfaceClass() == 7 && usbDevice != null) {
                if (this.mUsbManager.hasPermission(usbDevice)) {
                    connectUsb(usbDevice);
                } else {
                    this.mUsbManager.requestPermission(usbDevice, null);
                    this.executorService.execute(new Runnable() {
                        public void run() {
                            try {
                                Thread.sleep(3000);
                                if (PrintUtil.this.mUsbManager.hasPermission(usbDevice)) {
                                    PrintUtil.this.connectUsb(usbDevice);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }

    private void connectUsb(UsbDevice usbDevice) {
        HsUsbPrintDriver instance = HsUsbPrintDriver.getInstance();
        if (instance != null) {
            instance.setUsbManager(this.mUsbManager);
            instance.connect(usbDevice);
        }
    }

    public void delayPrint(Context context, String str, OrderInfo orderInfo, int i) throws Exception {
        connectUSB(context);
        final String userinfo = SPUtils.getUserinfo(context, SPUtils.PRINT_DELAY_TIME);
        if (!TextUtils.isEmpty(userinfo) && userinfo != null) {
            final int i2 = i;
            final OrderInfo orderInfo2 = orderInfo;
            final Context context2 = context;
            final String str2 = str;
            this.executorService.execute(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(Long.parseLong(userinfo));
                        if (i2 == 0) {
                            if (orderInfo2.getPay_status().intValue() == 0) {
                                PrintUtil.this.hsUnPayPrint(context2, orderInfo2);
                            } else {
                                PrintUtil.this.hsPrePrint(context2, orderInfo2);
                            }
                        } else if (i2 == 1) {
                            PrintUtil.this.kitchenPrint(context2, orderInfo2);
                            if (!"wxwaimai".equals(orderInfo2.getOrderfrom()) && !"meituan".equals(orderInfo2.getOrderfrom()) && !"eleme".equals(orderInfo2.getOrderfrom()) && !"baidu".equals(orderInfo2.getOrderfrom())) {
                                return;
                            }
                            if (orderInfo2.getPay_status().intValue() == 0) {
                                PrintUtil.this.hsUnPayPrint(context2, orderInfo2);
                            } else {
                                PrintUtil.this.hsPrePrint(context2, orderInfo2);
                            }
                        } else if (i2 == 2) {
                            PrintUtil.this.printQRcode(context2, str2);
                        } else if (i2 != 3) {
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (i == 0) {
            if (orderInfo.getPay_status().intValue() == 0) {
                hsUnPayPrint(context, orderInfo);
            } else {
                hsPrePrint(context, orderInfo);
            }
        } else if (i == 1) {
            kitchenPrint(context, orderInfo);
            if (!"wxwaimai".equals(orderInfo.getOrderfrom()) && !"meituan".equals(orderInfo.getOrderfrom()) && !"eleme".equals(orderInfo.getOrderfrom()) && !"baidu".equals(orderInfo.getOrderfrom())) {
                return;
            }
            if (orderInfo.getPay_status().intValue() == 0) {
                hsUnPayPrint(context, orderInfo);
            } else {
                hsPrePrint(context, orderInfo);
            }
        } else if (i == 2) {
            printQRcode(context, str);
        } else if (i != 3) {
        }
    }

    public void stopExecutor() {
        if (this.executorService != null) {
            this.executorService.shutdown();
        }
    }
}
