package com.smart.kitchen.smartkitchen;

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
import android.support.v4.app.FragmentActivity;
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

public abstract class BaseFragmentActivity extends FragmentActivity implements IRoot {
    private static final String TAG = "BaseFragmentActivity";
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
                BaseFragmentActivity.this.receiverMessagesMain(stringExtra);
                return;
            }
            LogUtils.e(TAG, "onReceive->paysuccess: " + stringExtra2);
            BaseFragmentActivity.this.paySuccessNotify(stringExtra2, intent.getLongExtra("orderId", -1));
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

    public void setContentView(int i) {
        super.setContentView(i);
        initView();
        initBroadCast();
        this.dialogUtils = new DialogUtils(this);
        initEvent();
        initData();
    }

    protected void setScreen() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            View currentFocus = getCurrentFocus();
            if (AutoSoftUtils.isShouldHideInput(currentFocus, motionEvent)) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(currentFocus.getWindowToken(),InputMethodManager.RESULT_UNCHANGED_SHOWN);
                }
            }
            return super.dispatchTouchEvent(motionEvent);
        } else if (getWindow().superDispatchTouchEvent(motionEvent)) {
            return true;
        } else {
            return onTouchEvent(motionEvent);
        }
    }

    public void toFinish(View view) {
        finish();
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

    protected void initProgressDialog() {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(this);
            this.progressDialog.setMessage(getStrings(R.string.loading));
            this.progressDialog.setCancelable(false);
        }
    }

    protected String getStrings(int resId) {
        return Contants.getString(this.context, resId);
    }

    @Override
    public void haveRoot(int requestCode) {
        onRequestPermissionsSuccess(requestCode);
    }

    public void receiverMessagesMain(String msg) {
        LogUtils.e(TAG, "receiverMessagesMain: " + msg);
        SoundUtils.messageNew(msg);
        receiverMessages(msg);
    }

    public void receiverMessages(String str) {
    }

    public void paySuccessNotify(String str, long j) {
    }

    protected void onRequestPermissionsSuccess(int i) {
    }

    protected void onRequestPermissionsFail(int i) {
        Permission.showMissingPermissionDialog(this.context);
    }

    protected void initBroadCast() {
        this.intentFilter = new IntentFilter();
        this.intentFilter.addAction(BaseActivity.MSG_BROCARSTRECEIVER);
        this.socketReceiver = new SocketReceiver();
        registerReceiver(this.socketReceiver, this.intentFilter);
        startService(new Intent(this.context, ClientSocketService.class));
    }

    private void initUsbBroadCast() {
        if (this.usbDeviceReceiver == null) {
            this.usbDeviceReceiver = new UsbDeviceReceiver(new CallBack() {
                public void onPermissionGranted(UsbDevice usbDevice) {
                    LogUtils.e(BaseFragmentActivity.TAG, "BaseFragmentActivity       onPermissionGranted");
                    if (usbDevice.getDeviceClass() == 0 && usbDevice.getInterfaceCount() > 0 && usbDevice.getInterface(0).getInterfaceClass() == 7) {
                        BaseFragmentActivity.this.connectUsb(usbDevice);
                    }
                }

                public void onDeviceAttached(UsbDevice usbDevice) {
                    LogUtils.e(BaseFragmentActivity.TAG, "BaseFragmentActivity       onDeviceAttached");
                    if (usbDevice.getDeviceClass() == 0 && usbDevice.getInterfaceCount() > 0 && usbDevice.getInterface(0).getInterfaceClass() == 7) {
                        BaseFragmentActivity.this.UsbConnect();
                    }
                }

                public void onDeviceDetached(UsbDevice usbDevice) {
                    LogUtils.e(BaseFragmentActivity.TAG, "BaseFragmentActivity       onDeviceDetached");
                    if (usbDevice.getDeviceClass() == 0 && usbDevice.getInterfaceCount() > 0 && usbDevice.getInterface(0).getInterfaceClass() == 7) {
                        HsUsbPrintDriver.getInstance().stop();
                    }
                }
            });
            this.usbIntentFilter = new IntentFilter();
            this.usbIntentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
            this.usbIntentFilter.addAction(UsbDeviceReceiver.ACTION_USB_PERMISSION);
            this.usbIntentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
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
        this.mUsbManager.requestPermission(this.mUsbDevice, PendingIntent.getBroadcast(this.context, 0, new Intent(UsbDeviceReceiver.ACTION_USB_PERMISSION), 0));
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
