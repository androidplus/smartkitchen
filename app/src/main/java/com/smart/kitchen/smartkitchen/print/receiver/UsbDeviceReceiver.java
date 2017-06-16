package com.smart.kitchen.smartkitchen.print.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import com.smart.kitchen.smartkitchen.utils.LogUtils;

public class UsbDeviceReceiver extends BroadcastReceiver {
    public static final String ACTION_USB_PERMISSION = "com.hang.usb.action.USB_PERMISSION";
    private final String TAG = getClass().getSimpleName();
    private CallBack mCallBack;

    public interface CallBack {
        void onDeviceAttached(UsbDevice usbDevice);

        void onDeviceDetached(UsbDevice usbDevice);

        void onPermissionGranted(UsbDevice usbDevice);
    }

    private UsbDeviceReceiver() {
    }

    public UsbDeviceReceiver(CallBack callBack) {
        this.mCallBack = callBack;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtils.d(this.TAG, "action = " + action);
        UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra("device");
        if (usbDevice != null) {
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    LogUtils.d(this.TAG, "UsbManager EXTRA_PERMISSION_GRANTED booleanExtra = " + intent.getBooleanExtra("permission", false));
                    if (intent.getBooleanExtra("permission", false)) {
                        this.mCallBack.onPermissionGranted(usbDevice);
                    } else {
                        LogUtils.d(this.TAG, "permission denied for device " + usbDevice);
                    }
                }
            } else if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
                this.mCallBack.onDeviceAttached(usbDevice);
            } else if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                this.mCallBack.onDeviceDetached(usbDevice);
            }
        }
    }
}
