package com.smart.kitchen.smartkitchen;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.smart.kitchen.smartkitchen.popwindow.DialogUtils;
import com.smart.kitchen.smartkitchen.print.receiver.UsbDeviceReceiver;
import com.smart.kitchen.smartkitchen.print.receiver.UsbDeviceReceiver.CallBack;
import com.smart.kitchen.smartkitchen.service.ClientSocketService;
import com.smart.kitchen.smartkitchen.utils.AutoSoftUtils;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.Permission;
import com.smart.kitchen.smartkitchen.utils.Permission.IRoot;
import com.smart.kitchen.smartkitchen.utils.SoundUtils;
import driver.HsUsbPrintDriver;

public abstract class BaseActivity extends Activity implements IRoot {
    public static final String MSG_BROCARSTRECEIVER = "com.socket.msg";
    private static final String TAG = "BaseActivity";
    protected Context context;
    protected DialogUtils dialogUtils;
    private IntentFilter intentFilter;
    private UsbDevice mUsbDevice;
    private UsbManager mUsbManager;
    protected ProgressDialog progressDialog;
    private SocketReceiver socketReceiver;
    private UsbDeviceReceiver usbDeviceReceiver;
    private IntentFilter usbIntentFilter;

    class SocketReceiver extends BroadcastReceiver {
        private static final String TAG = "socketReceiver";

        SocketReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra("msg");
            String stringExtra2 = intent.getStringExtra("paysuccess");
            if (Contants.isEmpty(stringExtra2)) {
                LogUtils.e(TAG, "onReceive->msg: " + stringExtra);
                BaseActivity.this.receiverMessagesMain(stringExtra);
                return;
            }
            LogUtils.e(TAG, "onReceive->paysuccess: " + stringExtra2);
            BaseActivity.this.paySuccessNotify(stringExtra2);
        }
    }

    protected abstract void initView();

    protected abstract void initEvent();

    protected abstract void initData();

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.context = this;
        setScreen();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initView();
        initBroadCast();
        this.dialogUtils = new DialogUtils(this);
        initProgressDialog();
        initEvent();
        initData();
    }

    protected void setScreen() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    protected String getStrings(int resId) {
        return Contants.getString(this.context, resId);
    }

    public void toFinish(View view) {
        finish();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            View currentFocus = getCurrentFocus();
            if (AutoSoftUtils.isShouldHideInput(currentFocus, motionEvent)) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                }
            }
            return super.dispatchTouchEvent(motionEvent);
        } else if (getWindow().superDispatchTouchEvent(motionEvent)) {
            return true;
        } else {
            return onTouchEvent(motionEvent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length <= 0 || grantResults[0] != 0) {
            onRequestPermissionsFail(requestCode);
        } else {
            haveRoot(requestCode);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void haveRoot(int requestCode) {
        onRequestPermissionsSuccess(requestCode);
    }

    protected void onRequestPermissionsSuccess(int requestCode) {
    }

    protected void onRequestPermissionsFail(int requestCode) {
        Permission.showMissingPermissionDialog(this.context);
    }

    public String[] copyArray(String[] strArr, String[] strArr2) {
        String[] strArr3 = new String[(strArr.length + strArr2.length)];
        for (int i = 0; i < 2; i++) {
            int i2;
            if (i == 0) {
                for (i2 = 0; i2 < strArr.length; i2++) {
                    strArr3[i2] = strArr[i2];
                }
            } else if (i == 1) {
                int i3 = 0;
                for (i2 = strArr.length; i2 < strArr.length + strArr2.length; i2++) {
                    strArr3[i2] = strArr2[i3];
                    i3++;
                }
            }
        }
        return strArr3;
    }

    protected void initProgressDialog() {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this);
            this.progressDialog.setMessage(getStrings(R.string.loading));
            this.progressDialog.setCancelable(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void receiverMessagesMain(String str) {
        SoundUtils.messageNew(str);
        receiverMessages(str);
    }

    public void receiverMessages(String str) {
    }

    public void paySuccessNotify(String str) {
    }

    protected void initBroadCast() {
        this.intentFilter = new IntentFilter();
        this.intentFilter.addAction(MSG_BROCARSTRECEIVER);
        this.socketReceiver = new SocketReceiver();
        registerReceiver(this.socketReceiver, this.intentFilter);
        startService(new Intent(this.context, ClientSocketService.class));
    }

    private void initUsbBroadCast() {
        if (this.usbDeviceReceiver == null) {
            this.usbIntentFilter = new IntentFilter();
            this.usbIntentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
            this.usbIntentFilter.addAction(UsbDeviceReceiver.ACTION_USB_PERMISSION);
            this.usbIntentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
            this.usbDeviceReceiver = new UsbDeviceReceiver(new CallBack() {
                public void onPermissionGranted(UsbDevice usbDevice) {
                    if (usbDevice.getDeviceClass() == 0 && usbDevice.getInterfaceCount() > 0 && usbDevice.getInterface(0).getInterfaceClass() == 7) {
                        BaseActivity.this.connectUsb(usbDevice);
                    }
                }

                public void onDeviceAttached(UsbDevice usbDevice) {
                    if (usbDevice.getDeviceClass() == 0 && usbDevice.getInterfaceCount() > 0 && usbDevice.getInterface(0).getInterfaceClass() == 7) {
                        BaseActivity.this.UsbConnect();
                    }
                }

                public void onDeviceDetached(UsbDevice usbDevice) {
                    if (usbDevice.getDeviceClass() == 0 && usbDevice.getInterfaceCount() > 0 && usbDevice.getInterface(0).getInterfaceClass() == 7) {
                        HsUsbPrintDriver.getInstance().stop();
                    }
                }
            });
            registerReceiver(this.usbDeviceReceiver, this.usbIntentFilter);
        }
    }

    private void UsbConnect() {
        if (MyApplication.getConnState() != 16) {
            if (MyApplication.getConnState() == 18) {
                HsUsbPrintDriver.getInstance().stop();
            }
            connect();
            return;
        }
        connect();
    }

    private void connect() {
        if (this.mUsbManager == null) {
            this.mUsbManager = (UsbManager) this.context.getSystemService(USB_SERVICE);
        }
        for (UsbDevice usbDevice : this.mUsbManager.getDeviceList().values()) {
            if (usbDevice.getDeviceClass() == 0 && usbDevice.getInterface(0).getInterfaceClass() == 7) {
                this.mUsbDevice = usbDevice;
            }
        }
        if (this.mUsbDevice == null) {
            return;
        }
        if (this.mUsbManager.hasPermission(this.mUsbDevice)) {
            connectUsb(this.mUsbDevice);
            return;
        }
        Context context = this.context;
        UsbDeviceReceiver usbDeviceReceiver = this.usbDeviceReceiver;
        this.mUsbManager.requestPermission(this.mUsbDevice, PendingIntent.getBroadcast(context, 0, new Intent(UsbDeviceReceiver.ACTION_USB_PERMISSION), 0));
    }

    private void connectUsb(UsbDevice usbDevice) {
        HsUsbPrintDriver.getInstance().connect(usbDevice);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.socketReceiver != null) {
            unregisterReceiver(this.socketReceiver);
        }
        if (this.usbDeviceReceiver != null) {
            unregisterReceiver(this.usbDeviceReceiver);
        }
    }
}
