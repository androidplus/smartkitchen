package com.smart.kitchen.smartkitchen;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.internal.view.SupportMenu;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import com.smart.kitchen.smartkitchen.print.observable.ConnResultObservable;
import com.smart.kitchen.smartkitchen.print.observable.ConnStateObservable;
import com.smart.kitchen.smartkitchen.utils.CrashHandler;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.SoundUtils;
import driver.HsBluetoothPrintDriver;
import driver.HsUsbPrintDriver;
import driver.HsWifiPrintDriver;
import driver.LabelBluetoothPrintDriver;
import driver.LabelUsbPrintDriver;
import driver.LabelWifiPrintDriver;
import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {
    public static final int MODE_HS = 0;
    private static final int PRINTFINISHHANDLER_FLAG = 7174;
    private static final String TAG = "BaseApplication";
    private static List<Activity> activitys = new LinkedList();
    private static SpannableString connStateConnectedString;
    private static SpannableString connStateUnConnectedString;
    private static ForegroundColorSpan connectedColorSpan;
    private static String connectedStr;
    private static MyApplication instances;
    public static boolean isPrintFinish = true;
    public static String labelCopies = "1";
    public static String labelDensity = "0";
    public static String labelDirection = "0";
    public static String labelGap = "3";
    public static String labelHeight = "15";
    public static String labelSizeStr = "30 * 15";
    public static String labelSpeed = "2";
    public static String labelWidth = "30";
    private static ConnResultObservable mConnResultObservable;
    private static int mConnState = 16;
    private static ConnStateObservable mConnStateObservable;
    public static int mode = 0;
    private static PrintFinishHandler printFinishHandler;
    private static ForegroundColorSpan unConnectedColorSpan;
    private static String unConnectedStr;

    private class ConnStateHandler extends Handler {
        private ConnStateHandler() {
        }

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle data = message.getData();
            switch (data.getInt("flag")) {
                case 32:
                    MyApplication.this.setConnState(data.getInt("state"));
                    return;
                case 33:
                    MyApplication.mConnResultObservable.setChanged();
                    MyApplication.mConnResultObservable.notifyObservers(33);
                    return;
                case 34:
                    MyApplication.mConnResultObservable.setChanged();
                    MyApplication.mConnResultObservable.notifyObservers(34);
                    return;
                default:
                    return;
            }
        }
    }

    private class PrintFinishHandler extends Handler {
        private PrintFinishHandler() {
        }

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle data = message.getData();
            switch (data.getInt("flag")) {
                case MyApplication.PRINTFINISHHANDLER_FLAG /*7174*/:
                    int state = data.getInt("state", 0) & 0xff;
                    LogUtils.d("inquiry_status------", state + "");
                    if (state == 128) {
                        MyApplication.isPrintFinish = true;
                        return;
                    } else {
                        MyApplication.isPrintFinish = false;
                        return;
                    }
                default:
                    return;
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SoundUtils.getInstance(this).init();
        CrashHandler.getInstance().init(this);
        LogUtils.setDebug(false);
        instances = this;
        mConnStateObservable = ConnStateObservable.getInstance();
        mConnResultObservable = ConnResultObservable.getInstance();
        initDriver();
    }

    private void initDriver() {
        UsbManager usbManager = (UsbManager) instances.getSystemService(USB_SERVICE);
        HsUsbPrintDriver.getInstance().setUsbManager(usbManager);
        LabelUsbPrintDriver.getInstance().setUsbManager(usbManager);
        Handler connStateHandler = new ConnStateHandler();
        HsBluetoothPrintDriver.getInstance().setHandler(connStateHandler);
        HsUsbPrintDriver.getInstance().setHandler(connStateHandler);
        HsWifiPrintDriver.getInstance().setHandler(connStateHandler);
        LabelBluetoothPrintDriver.getInstance().setHandler(connStateHandler);
        LabelUsbPrintDriver.getInstance().setHandler(connStateHandler);
        LabelWifiPrintDriver.getInstance().setHandler(connStateHandler);
        printFinishHandler = new PrintFinishHandler();
    }

    public static MyApplication getApp() {
        return instances;
    }

    public static int getConnState() {
        return mConnState;
    }

    private void setConnState(int connState) {
        if (mConnState != connState) {
            mConnState = connState;
            mConnStateObservable.setChanged();
            mConnStateObservable.notifyObservers(getConnStateString());
        }
    }

    public static CharSequence getConnStateString() {
        if (mConnState == 16) {
            unConnectedStr = instances.getResources().getString(R.string.unconnected);
            connStateUnConnectedString = new SpannableString(unConnectedStr);
            if (unConnectedColorSpan == null) {
                unConnectedColorSpan = new ForegroundColorSpan(SupportMenu.CATEGORY_MASK);
            }
            connStateUnConnectedString.setSpan(unConnectedColorSpan, 0, unConnectedStr.length(), 18);
            return connStateUnConnectedString;
        }
        connectedStr = instances.getResources().getString(R.string.connected);
        connStateConnectedString = new SpannableString(connectedStr);
        if (connectedColorSpan == null) {
            connectedColorSpan = new ForegroundColorSpan(Color.GREEN);
        }
        connStateConnectedString.setSpan(connectedColorSpan, 0, connectedStr.length(), 18);
        return connStateConnectedString;
    }

    public static void addActivity(Activity activity) {
        LogUtils.v(TAG, "activity add");
        if (activitys == null || activitys.size() <= 0) {
            activitys.add(activity);
        } else if (!activitys.contains(activity)) {
            activitys.add(activity);
        }
    }

    public static void removeActivity(Activity activity) {
        activitys.remove(activity);
    }
}
