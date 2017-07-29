package com.smart.kitchen.smartkitchen.print.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.WifiDeviceAdapter;
import com.smart.kitchen.smartkitchen.print.interfaces.CustomDialogInterface.onPositiveClickListener;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;

public class WifiDeviceChooseDialog extends DialogFragment {
    public static final String BUNDLE_KEY_ADDRESS = "address";
    private final String EXTRA_KEY_DEVICE_IP = "deviceIp";
    private final String EXTRA_KEY_DEVICE_PORT = "devicePort";
    private final String TAG = getClass().getSimpleName();
    private EditText etIp;
    private EditText etPort;
    private WifiDeviceAdapter foundDeviceAdapter;
    private List<String> foundDeviceList;
    private LinearLayout llRadioChoose;
    private LinearLayout llRadioEdit;
    private ListView lvFoundDevices;
    private String mAddress;
    private Context mContext;
    private AlertDialog mDialog;
    private FoundHandler mFoundHandler;
    private HandlerThread mFoundThread;
    private onPositiveClickListener mListener;
    private Handler mSearchHandler;
    private boolean mSearchInited = false;
    private HandlerThread mSearchThread;
    private boolean mSearching = false;
    private IntentFilter mWifiIntentFilter;
    private WifiDeviceReceiver mWifiReceiver;
    private ProgressBar progressBar;
    private TextView tvClickToChoose;
    private TextView tvFoundDeviceCancle;
    private TextView tvFoundDeviceConfirm;
    private TextView tvFoundDeviceEmpty;

    private class FoundHandler extends Handler {
        private DatagramSocket ds;

        public FoundHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            LogUtils.v(WifiDeviceChooseDialog.this.TAG, "FoundHandler thread name = " + Thread.currentThread().getName());
            byte[] bArr = new byte[1024];
            while (WifiDeviceChooseDialog.this.mSearching) {
                try {
                    synchronized (this) {
                        int i;
                        DatagramPacket datagramPacket = new DatagramPacket(bArr, bArr.length);
                        if (this.ds == null) {
                            this.ds = new DatagramSocket(6001);
                            this.ds.setSoTimeout(3000);
                        }
                        this.ds.receive(datagramPacket);
                        byte[] data = datagramPacket.getData();
                        for (byte b : data) {
                            LogUtils.d(WifiDeviceChooseDialog.this.TAG, "b = " + b);
                        }
                        LogUtils.d(WifiDeviceChooseDialog.this.TAG, "data = " + new String(data));
                        if (data.length > 7 && new String(data, 0, 7).equals("RTFOUND")) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (i = 0; i < 4; i++) {
                                stringBuilder.append(data[i + 13]);
                                stringBuilder.append(".");
                            }
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                            String stringBuilder2 = stringBuilder.toString();
                            int i2 = data[25] << (data[26] + 8);
                            Intent intent = new Intent();
                            intent.setAction("com.hang.wifi.action.found");
                            intent.putExtra("deviceIp", stringBuilder2);
                            intent.putExtra("devicePort", i2);
                            WifiDeviceChooseDialog.this.mContext.sendBroadcast(intent);
                        }
                    }
                } catch (IOException e) {
                    try {
                        LogUtils.d(WifiDeviceChooseDialog.this.TAG, "IOException e getMessage = " + e.getMessage());
                        Intent intent2 = new Intent();
                        intent2.setAction("com.hang.wifi.action.finish");
                        WifiDeviceChooseDialog.this.mContext.sendBroadcast(intent2);
                        e.printStackTrace();
                        return;
                    } finally {
                        if (this.ds != null) {
                            this.ds.close();
                            this.ds = null;
                        }
                    }
                }
            }
            if (this.ds != null) {
                this.ds.close();
                this.ds = null;
            }
        }

        public void close() {
            if (this.ds != null && !this.ds.isClosed()) {
                this.ds.close();
            }
        }
    }

    private class SearchHandler extends Handler {
        private DatagramSocket ds;

        public void handleMessage(Message r6) {
            /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1439)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1461)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
            /*
            r5 = this;
            r0 = com.smart.kitchen.smartkitchen.print.dialog.WifiDeviceChooseDialog.this;
            r0 = r0.TAG;
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "SearchHandler thread name = ";
            r1 = r1.append(r2);
            r2 = java.lang.Thread.currentThread();
            r2 = r2.getName();
            r1 = r1.append(r2);
            r1 = r1.toString();
            com.smart.kitchen.smartkitchen.utils.LogUtils.d(r0, r1);
            r0 = new java.net.DatagramSocket;	 Catch:{ IOException -> 0x0051, all -> 0x005f }
            r1 = 6000; // 0x1770 float:8.408E-42 double:2.9644E-320;	 Catch:{ IOException -> 0x0051, all -> 0x005f }
            r0.<init>(r1);	 Catch:{ IOException -> 0x0051, all -> 0x005f }
            r5.ds = r0;	 Catch:{ IOException -> 0x0051, all -> 0x005f }
            r0 = "255.255.255.255";	 Catch:{ IOException -> 0x0051, all -> 0x005f }
            r0 = java.net.InetAddress.getByName(r0);	 Catch:{ IOException -> 0x0051, all -> 0x005f }
            r1 = "RTSEARCH";	 Catch:{ IOException -> 0x0051, all -> 0x005f }
            r1 = r1.getBytes();	 Catch:{ IOException -> 0x0051, all -> 0x005f }
            r2 = new java.net.DatagramPacket;	 Catch:{ IOException -> 0x0051, all -> 0x005f }
            r3 = 8;	 Catch:{ IOException -> 0x0051, all -> 0x005f }
            r4 = 3000; // 0xbb8 float:4.204E-42 double:1.482E-320;	 Catch:{ IOException -> 0x0051, all -> 0x005f }
            r2.<init>(r1, r3, r0, r4);	 Catch:{ IOException -> 0x0051, all -> 0x005f }
            r0 = r5.ds;	 Catch:{ IOException -> 0x0051, all -> 0x005f }
            r0.send(r2);	 Catch:{ IOException -> 0x0051, all -> 0x005f }
            r0 = r5.ds;
            if (r0 == 0) goto L_0x0050;
        L_0x004b:
            r0 = r5.ds;
            r0.close();
        L_0x0050:
            return;
        L_0x0051:
            r0 = move-exception;
            r0.printStackTrace();	 Catch:{ IOException -> 0x0051, all -> 0x005f }
            r0 = r5.ds;
            if (r0 == 0) goto L_0x0050;
        L_0x0059:
            r0 = r5.ds;
            r0.close();
            goto L_0x0050;
        L_0x005f:
            r0 = move-exception;
            r1 = r5.ds;
            if (r1 == 0) goto L_0x0069;
        L_0x0064:
            r1 = r5.ds;
            r1.close();
        L_0x0069:
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.smart.kitchen.smartkitchen.print.dialog.WifiDeviceChooseDialog.SearchHandler.handleMessage(android.os.Message):void");
        }

        public SearchHandler(Looper looper) {
            super(looper);
        }
    }

    private class WifiDeviceReceiver extends BroadcastReceiver {
        private static final String ACTION_FINISH = "com.hang.wifi.action.finish";
        private static final String ACTION_FOUND = "com.hang.wifi.action.found";

        private WifiDeviceReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtils.v(WifiDeviceChooseDialog.this.TAG, "action = " + action);
            if (ACTION_FOUND.equals(action)) {
                WifiDeviceChooseDialog.this.foundDeviceList.add(intent.getStringExtra("deviceIp") + ":" + intent.getIntExtra("devicePort", -1));
                WifiDeviceChooseDialog.this.foundDeviceAdapter.notifyDataSetChanged();
            } else if (ACTION_FINISH.equals(action)) {
                WifiDeviceChooseDialog.this.mContext.unregisterReceiver(WifiDeviceChooseDialog.this.mWifiReceiver);
                WifiDeviceChooseDialog.this.mSearching = false;
                WifiDeviceChooseDialog.this.progressBar.setVisibility(View.GONE);
                WifiDeviceChooseDialog.this.tvClickToChoose.setEnabled(true);
                LogUtils.v(WifiDeviceChooseDialog.this.TAG, "foundDeviceList size = " + WifiDeviceChooseDialog.this.foundDeviceList.size());
                if (WifiDeviceChooseDialog.this.foundDeviceList.size() == 0) {
                    WifiDeviceChooseDialog.this.tvFoundDeviceEmpty.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mAddress = getArguments().getString(BUNDLE_KEY_ADDRESS, "");
    }

    public Dialog onCreateDialog(Bundle bundle) {
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.dialog_choose_wifi_device, null);
        initView(inflate);
        setListener();
        this.llRadioEdit.performClick();
        Builder builder = new Builder(this.mContext);
        builder.setView(inflate).setCancelable(true).setPositiveButton(null, null).setNegativeButton(null, null);
        this.mDialog = builder.create();
        this.mDialog.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                WifiDeviceChooseDialog.this.setDialogButtonListener();
            }
        });
        return this.mDialog;
    }

    private void initView(View view) {
        this.llRadioEdit = (LinearLayout) view.findViewById(R.id.ll_dialog_choose_wifi_device_radio_edit);
        this.llRadioChoose = (LinearLayout) view.findViewById(R.id.ll_dialog_choose_wifi_device_radio_choose);
        this.etIp = (EditText) view.findViewById(R.id.et_dialog_choose_wifi_device_ip);
        this.etPort = (EditText) view.findViewById(R.id.et_dialog_choose_wifi_device_port);
        this.tvClickToChoose = (TextView) view.findViewById(R.id.tv_dialog_choose_wifi_device_click_to_choose);
        this.progressBar = (ProgressBar) view.findViewById(R.id.pb_dialog_choose_wifi_device_progress_bar);
        this.lvFoundDevices = (ListView) view.findViewById(R.id.lv_dialog_choose_wifi_device_found_devices);
        this.tvFoundDeviceEmpty = (TextView) view.findViewById(R.id.tv_dialog_choose_wifi_device_found_devices_empty);
        this.tvFoundDeviceConfirm = (TextView) view.findViewById(R.id.tv_dialog_choose_wifi_device_sure);
        this.tvFoundDeviceCancle = (TextView) view.findViewById(R.id.tv_dialog_choose_wifi_device_cancle);
        this.lvFoundDevices.setChoiceMode(1);
        Log.d("log", this.mAddress);
        if (!TextUtils.isEmpty(this.mAddress)) {
            String[] split = this.mAddress.split(":");
            this.etIp.setText(split[0]);
            this.etPort.setText(split[1]);
        }
    }

    private void setListener() {
        this.llRadioEdit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                WifiDeviceChooseDialog.this.llRadioEdit.setSelected(true);
                WifiDeviceChooseDialog.this.llRadioChoose.setSelected(false);
                WifiDeviceChooseDialog.this.etIp.setEnabled(true);
                WifiDeviceChooseDialog.this.etPort.setEnabled(true);
                WifiDeviceChooseDialog.this.tvClickToChoose.setEnabled(false);
                WifiDeviceChooseDialog.this.lvFoundDevices.setEnabled(false);
                if (WifiDeviceChooseDialog.this.mSearching) {
                    WifiDeviceChooseDialog.this.mContext.unregisterReceiver(WifiDeviceChooseDialog.this.mWifiReceiver);
                    WifiDeviceChooseDialog.this.mSearching = false;
                    WifiDeviceChooseDialog.this.progressBar.setVisibility(View.GONE);
                }
            }
        });
        this.llRadioChoose.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                WifiDeviceChooseDialog.this.llRadioEdit.setSelected(false);
                WifiDeviceChooseDialog.this.llRadioChoose.setSelected(true);
                WifiDeviceChooseDialog.this.etIp.setEnabled(false);
                WifiDeviceChooseDialog.this.etPort.setEnabled(false);
                WifiDeviceChooseDialog.this.tvClickToChoose.setEnabled(true);
                WifiDeviceChooseDialog.this.lvFoundDevices.setEnabled(true);
            }
        });
        this.tvClickToChoose.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (WifiDeviceChooseDialog.this.mSearchInited) {
                    WifiDeviceChooseDialog.this.foundDeviceList.clear();
                    WifiDeviceChooseDialog.this.foundDeviceAdapter.notifyDataSetChanged();
                } else {
                    WifiDeviceChooseDialog.this.initSearch();
                    WifiDeviceChooseDialog.this.mSearchInited = true;
                }
                WifiDeviceChooseDialog.this.mSearching = true;
                WifiDeviceChooseDialog.this.tvFoundDeviceEmpty.setVisibility(View.GONE);
                WifiDeviceChooseDialog.this.progressBar.setVisibility(View.VISIBLE);
                WifiDeviceChooseDialog.this.tvClickToChoose.setEnabled(false);
                WifiDeviceChooseDialog.this.mContext.registerReceiver(WifiDeviceChooseDialog.this.mWifiReceiver, WifiDeviceChooseDialog.this.mWifiIntentFilter);
                WifiDeviceChooseDialog.this.mFoundHandler.sendMessage(WifiDeviceChooseDialog.this.mFoundHandler.obtainMessage());
                WifiDeviceChooseDialog.this.mSearchHandler.sendMessage(WifiDeviceChooseDialog.this.mSearchHandler.obtainMessage());
            }
        });
    }

    private void setDialogButtonListener() {
        this.mDialog.getButton(-1).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String trim;
                if (WifiDeviceChooseDialog.this.llRadioEdit.isSelected()) {
                    trim = WifiDeviceChooseDialog.this.etIp.getText().toString().trim();
                    if (TextUtils.isEmpty(trim)) {
                        Toasts.show(WifiDeviceChooseDialog.this.mContext, (int) R.string.tip_ip_input);
                        return;
                    }
                    String trim2 = WifiDeviceChooseDialog.this.etPort.getText().toString().trim();
                    if (TextUtils.isEmpty(trim2)) {
                        Toasts.show(WifiDeviceChooseDialog.this.mContext, (int) R.string.tip_port_input);
                        return;
                    } else if (trim.matches("(\\d{1,3}\\.){3}\\d{1,3}")) {
                        trim = trim + ":" + trim2;
                    } else {
                        Toasts.show(WifiDeviceChooseDialog.this.mContext, (int) R.string.tip_input_correct_ip);
                        return;
                    }
                } else if (WifiDeviceChooseDialog.this.foundDeviceList == null || WifiDeviceChooseDialog.this.foundDeviceList.size() == 0) {
                    WifiDeviceChooseDialog.this.mDialog.dismiss();
                    return;
                } else {
                    int checkedItemPosition = WifiDeviceChooseDialog.this.lvFoundDevices.getCheckedItemPosition();
                    if (checkedItemPosition == -1) {
                        Toasts.show(WifiDeviceChooseDialog.this.mContext, (int) R.string.tip_choose_address);
                        return;
                    }
                    trim = WifiDeviceChooseDialog.this.lvFoundDevices.getAdapter().getItem(checkedItemPosition).toString();
                }
                WifiDeviceChooseDialog.this.mListener.onDialogPositiveClick(trim);
                WifiDeviceChooseDialog.this.mDialog.dismiss();
            }
        });
        this.tvFoundDeviceConfirm.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                String trim;
                if (WifiDeviceChooseDialog.this.llRadioEdit.isSelected()) {
                    trim = WifiDeviceChooseDialog.this.etIp.getText().toString().trim();
                    if (TextUtils.isEmpty(trim)) {
                        Toasts.show(WifiDeviceChooseDialog.this.mContext, (int) R.string.tip_ip_input);
                        return;
                    }
                    String trim2 = WifiDeviceChooseDialog.this.etPort.getText().toString().trim();
                    if (TextUtils.isEmpty(trim2)) {
                        Toasts.show(WifiDeviceChooseDialog.this.mContext, (int) R.string.tip_port_input);
                        return;
                    } else if (trim.matches("(\\d{1,3}\\.){3}\\d{1,3}")) {
                        trim = trim + ":" + trim2;
                    } else {
                        Toasts.show(WifiDeviceChooseDialog.this.mContext, (int) R.string.tip_input_correct_ip);
                        return;
                    }
                } else if (WifiDeviceChooseDialog.this.foundDeviceList == null || WifiDeviceChooseDialog.this.foundDeviceList.size() == 0) {
                    WifiDeviceChooseDialog.this.mDialog.dismiss();
                    return;
                } else {
                    int checkedItemPosition = WifiDeviceChooseDialog.this.lvFoundDevices.getCheckedItemPosition();
                    if (checkedItemPosition == -1) {
                        Toasts.show(WifiDeviceChooseDialog.this.mContext, (int) R.string.tip_choose_address);
                        return;
                    }
                    trim = WifiDeviceChooseDialog.this.lvFoundDevices.getAdapter().getItem(checkedItemPosition).toString();
                }
                WifiDeviceChooseDialog.this.mListener.onDialogPositiveClick(trim);
                WifiDeviceChooseDialog.this.mDialog.dismiss();
            }
        });
        this.tvFoundDeviceCancle.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                WifiDeviceChooseDialog.this.mDialog.dismiss();
            }
        });
    }

    private void initSearch() {
        this.foundDeviceList = new ArrayList();
        this.foundDeviceAdapter = new WifiDeviceAdapter(this.mContext, this.foundDeviceList);
        this.lvFoundDevices.setAdapter(this.foundDeviceAdapter);
        this.mWifiReceiver = new WifiDeviceReceiver();
        this.mWifiIntentFilter = new IntentFilter();
        this.mWifiIntentFilter.addAction("com.hang.wifi.action.found");
        this.mWifiIntentFilter.addAction("com.hang.wifi.action.finish");
        this.mFoundThread = new HandlerThread("foundThread", 10);
        this.mSearchThread = new HandlerThread("searchThread", 10);
        this.mFoundThread.start();
        this.mSearchThread.start();
        this.mFoundHandler = new FoundHandler(this.mFoundThread.getLooper());
        this.mSearchHandler = new SearchHandler(this.mSearchThread.getLooper());
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        if (this.mSearching) {
            this.mContext.unregisterReceiver(this.mWifiReceiver);
        }
        if (this.mFoundHandler != null) {
            this.mFoundHandler.close();
        }
    }

    public void setOnClickListener(onPositiveClickListener com_smart_kitchen_smartkitchen_print_interfaces_CustomDialogInterface_onPositiveClickListener) {
        this.mListener = com_smart_kitchen_smartkitchen_print_interfaces_CustomDialogInterface_onPositiveClickListener;
    }
}
