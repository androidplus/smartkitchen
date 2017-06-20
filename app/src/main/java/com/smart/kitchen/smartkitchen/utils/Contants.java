package com.smart.kitchen.smartkitchen.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Contants {
    public static final String ADDORDERS = "http://shop.makalon.com.cn/api/v1.Orders/addOrders";
    public static final String ADD_TO = "http://shop.makalon.com.cn/api/v1.Orders/appendOrder";
    public static final String ADD_VIP = "http://shop.makalon.com.cn/api/v1.Users/addVip";
    public static final String ALIPAY_FACEPAY = "http://shop.makalon.com.cn/index.php/api/facepay/pay";
    public static final int ALIPAY_FACEPAY_CODE = 1;
    public static final String ALIPAY_QRPAY = "http://shop.makalon.com.cn/index.php/api/qrpay/pay";
    public static final int ALIPAY_QRPAY_CODE = 2;
    public static final String BACKGOODS = "http://shop.makalon.com.cn/api/v1.Orders/backGoods";
    public static final String BACKGOODS_TO = "http://shop.makalon.com.cn/api/v1.Orders/backGoodsTo";
    public static final String BACKORDERS = "http://shop.makalon.com.cn/api/v1.Orders/backOrders";
    public static final String BIND_SOCKET = "http://shop.makalon.com.cn/api/v1.Sockets/bind";
    public static final String BREAKAGE_TYPE = "http://shop.makalon.com.cn/api/v1.Reports/reportsGoods";
    public static final String BULIDERORDERQR = "http://shop.makalon.com.cn/api/v1.Qr/buliderOrderQR";
    public static final String CANCELLATION = "http://shop.makalon.com.cn/api/v1.Orders/orderDistory";
    public static final int CASH_CODE = 8;
    public static final String CHECKCONPON = "http://shop.makalon.com.cn/api/v1.Users/checkConpon";
    public static final int CHECK_CONPON_CODE = 6;
    public static final String CHECK_TYPE = "http://shop.makalon.com.cn/api/v1.Goods/updateGoodsInfo";
    public static final String CONFIRMREFUND = "http://shop.makalon.com.cn/api/v1.Orders/confirmRefund";
    public static final String COUNTPAY = "http://shop.makalon.com.cn/api/v1.Users/countPay";
    public static final String FINISHORDERS = "http://shop.makalon.com.cn/api/v1.Orders/finishOrders";
    public static final String GETJIAOBANINGO = "http://shop.makalon.com.cn/api/v1.Handover/getHandover";
    public static final String GETJIAOBANINGOBYIDBYID = "http://shop.makalon.com.cn/api/v1.Handover/getHandoverById";
    public static final int GETPAYCOUNT = 7;
    public static final String GETSTAFFALL = "http://shop.makalon.com.cn/api/v1.Staffs/getStaffAll";
    public static final String GETVIPCONPON = "http://shop.makalon.com.cn/api/v1.Users/getVipAmountByPhone";
    public static final String GET_MENU = "http://shop.makalon.com.cn/api/v1.Goods/getMenu";
    public static final String GET_MENU_TYPE = "http://shop.makalon.com.cn/api/v1.Goods/getMenuType";
    public static final String GET_ORDER_HISTORY = "http://shop.makalon.com.cn/api/v1.Orders/getOrderHistory";
    public static final String GET_ORDER_INFO = "http://shop.makalon.com.cn/api/v1.";
    public static final String GET_ORDER_LIST = "http://shop.makalon.com.cn/api/v1.Orders/getOrder";
    public static final String GET_STORE_INFO = "http://shop.makalon.com.cn/api/v1.Stores/getStoreInfo2";
    public static final int GET_STORE_INFO_CODE = 999;
    public static final String GET_TABLE_AREA = "http://shop.makalon.com.cn/api/v1.Tables/getArea";
    public static final String GET_TABLE_LSIT = "http://shop.makalon.com.cn/api/v1.Tables/getTable";
    public static final int GET_VIP_INFO = 5;
    public static final String JIAOBAN = "http://shop.makalon.com.cn/api/v1.Handover/jiaojieban";
    public static final String LOGIN = "http://shop.makalon.com.cn/api/v1.Staffs/login";
    public static final int LOGIN_CODE = 1;
    public static final String MEMBERTOP = "http://shop.makalon.com.cn/api/v1.Users/searchGift";
    public static final String ORDERFROM = "http://shop.makalon.com.cn/api/v1.Orders/getOrderOneByOrderIds";
    public static final String ORDERFROMPOS = "http://shop.makalon.com.cn/api/v1.Orders/getPosOrderOneByOrderIds";
    public static final String ORDERPAY = "http://shop.makalon.com.cn/api/v1.Pays/getOrderPay";
    public static final String PAYBYCASH = "http://shop.makalon.com.cn/api/v1.Cash/pay";
    public static final int QRCODE_HEIGTH = 300;
    public static final int QRCODE_WIDTH = 300;
    public static final String REJECTREFUND = "http://shop.makalon.com.cn/api/v1.Orders/rejectRefund";
    public static final String SAEARCH_ORDERFROMPOS = "http://shop.makalon.com.cn/api/v1.Orders/searchOrers";
    public static final int SALES_RETURN_STATUS_NUM = 4;
    public static final String SEARCH_VIP = "http://shop.makalon.com.cn/api/v1.Users/getVipInfo";
    private static final String SERVER_ADDRESS = "http://shop.makalon.com.cn/api/v1.";
    private static final String SERVER_ADDRESS_TEST = "http://shop.makalon.com.cn/";
    private static final String TAG = "Contants";
    public static final int UNFINISHED_STATUS_NUM = 0;
    public static final String UPDATEORDERGOODS = "http://shop.makalon.com.cn/api/v1.Orders/updateOrderGoods";
    public static final String UPDATEWAIMAISTATUS = "http://shop.makalon.com.cn/api/v1.Orders/updateWaiMaiStatus";
    public static final String UPDATE_JIAOBAN = "http://shop.makalon.com.cn/api/v1.Handover/updatejiaojieban";
    public static final String UPDATE_POS = "http://shop.makalon.com.cn/api/v1.Orders/updatePosOrderisPosed";
    public static final String VERSION_UPDARE = "http://shop.makalon.com.cn/api/v1.Versions/checkUpdate";
    public static final String VIPPAYCASH = "http://shop.makalon.com.cn/api/v1.Cash/vippay";
    public static final String VIPPAYFACEPAY = "http://shop.makalon.com.cn/api/Facepay/vippay";
    public static final String VIPPAYQRPAY = "http://shop.makalon.com.cn/api/v1.Qrpay/pay";
    public static final String VIPPAYQRPAYWX = "http://shop.makalon.com.cn/api/v1.Qrpaywx/pay";
    public static final String VIPPAYWEIXINPAY = "http://shop.makalon.com.cn/api/Weixinpay/vippay";
    public static final String WXPAY = "http://shop.makalon.com.cn/index.php/api/weixinpay/pay";
    public static final int WXPAY_CODE = 3;
    public static final String WXQRPAY = "http://shop.makalon.com.cn/index.php/api/wxnativepay/pay";
    public static final int WXQRPAY_CODE = 4;

    public static boolean isPic(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        if (str.toLowerCase().indexOf(".jpg") > 0 || str.toLowerCase().indexOf(".jpeg") > 0 || str.toLowerCase().indexOf(".png") > 0) {
            return true;
        }
        return false;
    }

    public static int getDimen(Context context, int i) {
        return (int) context.getResources().getDimension(i);
    }

    public static int getColor(Context context, int i) {
        return context.getResources().getColor(i);
    }

    public static Drawable getDrawable(Context context, int i) {
        return context.getResources().getDrawable(i);
    }

    public static String getString(Context context, int resId) {
        return context.getResources().getString(resId);
    }

    public static String getName(String str) {
        String str2 = "";
        if (str.indexOf(" ") > 0) {
            return str.substring(str.indexOf(" ") + 1);
        }
        return str2;
    }

    public static String getFirst(String str) {
        return str.substring(0, 1);
    }

    public static boolean isMobileNO(String str) {
        String str2 = "[1][345789]\\d{9}";
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.matches(str2);
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String formatDouble(String str) {
        return new DecimalFormat("###,##0.00").format(Double.valueOf(Double.parseDouble(str)));
    }

    public static boolean isEmail(String str) {
        if (str.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
            return true;
        }
        return false;
    }

    public static List<Integer> getArrayString(String str) {
        String[] split = str.split("-");
        List<Integer> arrayList = new ArrayList();
        for (String parseInt : split) {
            arrayList.add(Integer.valueOf(Integer.parseInt(parseInt)));
        }
        return arrayList;
    }

    public static List<Integer> getArrayStrings(String str, String str2) {
        String[] split = str.split(str2);
        List<Integer> arrayList = new ArrayList();
        for (String parseInt : split) {
            arrayList.add(Integer.valueOf(Integer.parseInt(parseInt)));
        }
        return arrayList;
    }

    public static int[] getMonth_Day(String str) {
        return new int[]{Integer.parseInt(str.substring(0, 4)), Integer.parseInt(str.substring(5, 7)), Integer.parseInt(str.substring(8, 10))};
    }

    public static int compare_date(String str, String str2) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        String str3 = str + " 00:00:00";
        String str4 = str2 + " 00:00:00";
        if (isEmpty(str2)) {
            str4 = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
        }
        try {
            instance.setTime(simpleDateFormat.parse(str3));
            instance2.setTime(simpleDateFormat.parse(str4));
        } catch (ParseException e) {
            System.err.println("格式不正确");
        }
        int compareTo = instance.compareTo(instance2);
        if (compareTo == 0) {
            return 0;
        }
        if (compareTo < 0) {
            return 1;
        }
        return -1;
    }

    public static int compare_time(String str, String str2) {
        String str3 = "2016-01-01 " + str + ":00";
        String str4 = "2016-01-01 " + str2 + ":00";
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar instance = Calendar.getInstance();
        Calendar instance2 = Calendar.getInstance();
        try {
            instance.setTime(simpleDateFormat.parse(str3));
            instance2.setTime(simpleDateFormat.parse(str4));
        } catch (ParseException e) {
            System.err.println("格式不正确");
        }
        int compareTo = instance.compareTo(instance2);
        if (compareTo == 0) {
            return 0;
        }
        if (compareTo < 0) {
            return 1;
        }
        return -1;
    }
}
