package com.smart.kitchen.smartkitchen.print.dialog;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.adapters.BluetoothDeviceAdapter;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

public class BluetoothDeviceChooseDialog extends DialogFragment {
    private final String TAG = getClass().getSimpleName();
    private BluetoothDeviceAdapter foundDeviceAdapter;
    private List<BluetoothDevice> foundDeviceList;
    private ListView lvFoundDevices;
    private ListView lvPairedDevices;
    private BluetoothAdapter mBluetoothAdapter;
    private IntentFilter mBluetoothIntentFilter;
    private BroadcastReceiver mBluetoothReceiver;
    private Context mContext;
    private onDeviceItemClickListener mListener;
    private boolean mRegistered = false;
    private boolean mSearchInited = false;
    private BluetoothDeviceAdapter pairedDeviceAdapter;
    private List<BluetoothDevice> pairedDeviceList;
    private ProgressBar progressBar;
    private TextView tvFoundDeviceEmpty;
    private TextView tvPairedDeviceEmpty;
    private TextView tvSearchDevice;

    private class BluetoothDeviceReceiver extends BroadcastReceiver {
        private BluetoothDeviceReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            boolean z = true;
            String action = intent.getAction();
            LogUtils.d(BluetoothDeviceChooseDialog.this.TAG, "action = " + action);
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice bluetoothDevice = (BluetoothDevice) intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (VERSION.SDK_INT < 18 || bluetoothDevice.getType() == 1) {
                    String access$1400 = BluetoothDeviceChooseDialog.this.TAG;
                    StringBuilder append = new StringBuilder().append("!foundDeviceList.contains(device) = ");
                    if (BluetoothDeviceChooseDialog.this.foundDeviceList.contains(bluetoothDevice)) {
                        z = false;
                    }
                    LogUtils.d(access$1400, append.append(z).toString());
                    if (!BluetoothDeviceChooseDialog.this.foundDeviceList.contains(bluetoothDevice)) {
                        BluetoothDeviceChooseDialog.this.foundDeviceList.add(bluetoothDevice);
                        BluetoothDeviceChooseDialog.this.foundDeviceAdapter.notifyDataSetChanged();
                    }
                }
            } else if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                BluetoothDeviceChooseDialog.this.mBluetoothAdapter.cancelDiscovery();
                BluetoothDeviceChooseDialog.this.mContext.unregisterReceiver(BluetoothDeviceChooseDialog.this.mBluetoothReceiver);
                BluetoothDeviceChooseDialog.this.mRegistered = false;
                BluetoothDeviceChooseDialog.this.tvSearchDevice.setEnabled(true);
                BluetoothDeviceChooseDialog.this.progressBar.setVisibility(8);
                if (BluetoothDeviceChooseDialog.this.foundDeviceList.size() == 0) {
                    BluetoothDeviceChooseDialog.this.tvFoundDeviceEmpty.setVisibility(0);
                }
            }
        }
    }

    public interface onDeviceItemClickListener {
        void onDeviceItemClick(BluetoothDevice bluetoothDevice);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.dialog_choose_bluetooth_device, null);
        initView(inflate);
        setListener();
        initData();
        Builder builder = new Builder(this.mContext);
        builder.setView(inflate).setCancelable(true).setNegativeButton(null, null);
        return builder.create();
    }

    private void initView(View view) {
        this.lvPairedDevices = (ListView) view.findViewById(R.id.lv_dialog_choose_bluetooth_device_paired_devices);
        this.lvFoundDevices = (ListView) view.findViewById(R.id.lv_dialog_choose_bluetooth_device_found_devices);
        this.tvPairedDeviceEmpty = (TextView) view.findViewById(R.id.tv_dialog_choose_bluetooth_device_paired_devices_empty);
        this.tvFoundDeviceEmpty = (TextView) view.findViewById(R.id.tv_dialog_choose_bluetooth_device_found_devices_empty);
        this.tvSearchDevice = (TextView) view.findViewById(R.id.tv_dialog_choose_bluetooth_device_search_device);
        this.progressBar = (ProgressBar) view.findViewById(R.id.pb_dialog_choose_bluetooth_device_progress_bar);
    }

    private void setListener() {
        this.tvSearchDevice.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                BluetoothDeviceChooseDialog.this.tvSearchDevice.setEnabled(false);
                BluetoothDeviceChooseDialog.this.progressBar.setVisibility(0);
                BluetoothDeviceChooseDialog.this.tvFoundDeviceEmpty.setVisibility(8);
                if (BluetoothDeviceChooseDialog.this.mSearchInited) {
                    BluetoothDeviceChooseDialog.this.foundDeviceList.clear();
                    BluetoothDeviceChooseDialog.this.foundDeviceAdapter.notifyDataSetChanged();
                } else {
                    BluetoothDeviceChooseDialog.this.foundDeviceList = new ArrayList();
                    BluetoothDeviceChooseDialog.this.foundDeviceAdapter = new BluetoothDeviceAdapter(BluetoothDeviceChooseDialog.this.mContext, BluetoothDeviceChooseDialog.this.foundDeviceList);
                    BluetoothDeviceChooseDialog.this.lvFoundDevices.setAdapter(BluetoothDeviceChooseDialog.this.foundDeviceAdapter);
                    BluetoothDeviceChooseDialog.this.mBluetoothReceiver = new BluetoothDeviceReceiver();
                    BluetoothDeviceChooseDialog.this.mBluetoothIntentFilter = new IntentFilter();
                    BluetoothDeviceChooseDialog.this.mBluetoothIntentFilter.addAction("android.bluetooth.device.action.FOUND");
                    BluetoothDeviceChooseDialog.this.mBluetoothIntentFilter.addAction("android.bluetooth.adapter.action.DISCOVERY_FINISHED");
                    BluetoothDeviceChooseDialog.this.mSearchInited = true;
                }
                BluetoothDeviceChooseDialog.this.mContext.registerReceiver(BluetoothDeviceChooseDialog.this.mBluetoothReceiver, BluetoothDeviceChooseDialog.this.mBluetoothIntentFilter);
                BluetoothDeviceChooseDialog.this.mRegistered = true;
                BluetoothDeviceChooseDialog.this.mBluetoothAdapter.startDiscovery();
            }
        });
        this.lvPairedDevices.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                BluetoothDeviceChooseDialog.this.mBluetoothAdapter.cancelDiscovery();
                if (BluetoothDeviceChooseDialog.this.mRegistered) {
                    BluetoothDeviceChooseDialog.this.mContext.unregisterReceiver(BluetoothDeviceChooseDialog.this.mBluetoothReceiver);
                    BluetoothDeviceChooseDialog.this.mRegistered = false;
                }
                BluetoothDeviceChooseDialog.this.mListener.onDeviceItemClick((BluetoothDevice) adapterView.getAdapter().getItem(i));
                BluetoothDeviceChooseDialog.this.getDialog().dismiss();
            }
        });
        this.lvFoundDevices.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                BluetoothDeviceChooseDialog.this.mBluetoothAdapter.cancelDiscovery();
                if (BluetoothDeviceChooseDialog.this.mRegistered) {
                    BluetoothDeviceChooseDialog.this.mContext.unregisterReceiver(BluetoothDeviceChooseDialog.this.mBluetoothReceiver);
                    BluetoothDeviceChooseDialog.this.mRegistered = false;
                }
                BluetoothDeviceChooseDialog.this.mListener.onDeviceItemClick((BluetoothDevice) adapterView.getAdapter().getItem(i));
                BluetoothDeviceChooseDialog.this.getDialog().dismiss();
            }
        });
    }

    private void initData() {
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.pairedDeviceList = new ArrayList(this.mBluetoothAdapter.getBondedDevices());
        if (this.pairedDeviceList.size() == 0) {
            this.tvPairedDeviceEmpty.setVisibility(0);
        }
        this.pairedDeviceAdapter = new BluetoothDeviceAdapter(this.mContext, this.pairedDeviceList);
        this.lvPairedDevices.setAdapter(this.pairedDeviceAdapter);
    }

    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        this.mBluetoothAdapter.cancelDiscovery();
        if (this.mRegistered) {
            this.mContext.unregisterReceiver(this.mBluetoothReceiver);
        }
    }

    public void setOnDeviceItemClickListener(onDeviceItemClickListener com_smart_kitchen_smartkitchen_print_dialog_BluetoothDeviceChooseDialog_onDeviceItemClickListener) {
        this.mListener = com_smart_kitchen_smartkitchen_print_dialog_BluetoothDeviceChooseDialog_onDeviceItemClickListener;
    }
}
