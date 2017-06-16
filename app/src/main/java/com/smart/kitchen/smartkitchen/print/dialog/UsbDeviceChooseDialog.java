package com.smart.kitchen.smartkitchen.print.dialog;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.UsbDeviceAdapter;
import com.smart.kitchen.smartkitchen.print.receiver.UsbDeviceReceiver;
import com.smart.kitchen.smartkitchen.print.receiver.UsbDeviceReceiver.CallBack;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UsbDeviceChooseDialog extends DialogFragment {
    private final String TAG = getClass().getSimpleName();
    private ListView lvContent;
    private UsbDeviceAdapter mAdapter;
    private Context mContext;
    private List<UsbDevice> mList;
    private OnItemClickListener mListener;
    private OnClickListener mOnClickListener;
    private UsbManager mUsbManager;
    private UsbDeviceReceiver mUsbReceiver;
    private TextView tvCancle;
    private TextView tvConfirm;
    private TextView tvEmpty;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.dialog_choose_usb_device, null);
        this.tvConfirm = (TextView) inflate.findViewById(R.id.tv_dialog_choose_usb_device_sure);
        this.tvCancle = (TextView) inflate.findViewById(R.id.tv_dialog_choose_usb_device_cancle);
        initView(inflate);
        setListener();
        initData();
        setAdapter();
        registerUsbReceiver();
        Builder builder = new Builder(this.mContext);
        builder.setView(inflate).setCancelable(true).setNegativeButton(null, null);
        return builder.create();
    }

    private void registerUsbReceiver() {
        if (this.mUsbReceiver == null) {
            this.mUsbReceiver = new UsbDeviceReceiver(new CallBack() {
                public void onPermissionGranted(UsbDevice usbDevice) {
                    if (usbDevice.getDeviceClass() == 0 && usbDevice.getInterfaceCount() > 0 && usbDevice.getInterface(0).getInterfaceClass() != 7) {
                    }
                }

                public void onDeviceAttached(UsbDevice usbDevice) {
                    if (usbDevice.getDeviceClass() == 0 && usbDevice.getInterfaceCount() > 0 && usbDevice.getInterface(0).getInterfaceClass() == 7) {
                        UsbDeviceChooseDialog.this.mList.add(usbDevice);
                        UsbDeviceChooseDialog.this.mAdapter.notifyDataSetChanged();
                        UsbDeviceChooseDialog.this.tvEmpty.setVisibility(View.GONE);
                    }
                }

                public void onDeviceDetached(UsbDevice usbDevice) {
                    if (usbDevice.getDeviceClass() == 0 && usbDevice.getInterfaceCount() > 0 && usbDevice.getInterface(0).getInterfaceClass() == 7) {
                        UsbDeviceChooseDialog.this.mList.remove(usbDevice);
                        UsbDeviceChooseDialog.this.mAdapter.notifyDataSetChanged();
                        if (UsbDeviceChooseDialog.this.mList.size() == 0) {
                            UsbDeviceChooseDialog.this.tvEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
            intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
            intentFilter.addAction(UsbDeviceReceiver.ACTION_USB_PERMISSION);
            this.mContext.registerReceiver(this.mUsbReceiver, intentFilter);
        }
    }

    private void initView(View view) {
        this.lvContent = (ListView) view.findViewById(R.id.lv_dialog_choose_usb_device);
        this.tvEmpty = (TextView) view.findViewById(R.id.tv_dialog_choose_usb_device_empty);
    }

    private void setListener() {
        this.lvContent.setOnItemClickListener(this.mListener);
        this.tvConfirm.setOnClickListener(this.mOnClickListener);
        this.tvCancle.setOnClickListener(this.mOnClickListener);
    }

    private void initData() {
        this.mList = new ArrayList();
        this.mUsbManager = (UsbManager) this.mContext.getSystemService(Context.USB_SERVICE);
        HashMap<String,UsbDevice> deviceList = this.mUsbManager.getDeviceList();
        LogUtils.d(this.TAG, "deviceList size = " + deviceList.size());
        for (UsbDevice usbDevice : deviceList.values()) {
            if (usbDevice.getDeviceClass() == 0) {
                UsbInterface usbInterface = usbDevice.getInterface(0);
                if (usbInterface.getInterfaceClass() == 7) {
                    this.mList.add(usbDevice);
                } else if (usbInterface.getInterfaceClass() == 8) {
                }
            }
        }
        if (this.mList.size() == 0) {
            this.tvEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter() {
        this.mAdapter = new UsbDeviceAdapter(this.mContext, this.mList);
        this.lvContent.setAdapter(this.mAdapter);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        LogUtils.v(this.TAG, "onDismiss");
        if (this.mUsbReceiver != null) {
            this.mContext.unregisterReceiver(this.mUsbReceiver);
        }
    }
}
