package com.smart.kitchen.smartkitchen.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.R;
import java.util.List;

public class BluetoothDeviceAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<BluetoothDevice> mList;

    private class ViewHolder {
        TextView tvName;

        private ViewHolder() {
        }
    }

    public BluetoothDeviceAdapter(Context context, List<BluetoothDevice> list) {
        this.mContext = context;
        this.mList = list;
        mInflater = LayoutInflater.from(this.mContext);
    }

    public int getCount() {
        return this.mList.size();
    }

    public Object getItem(int i) {
        return this.mList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = this.mInflater.inflate(R.layout.bluetooth_device_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) view.findViewById(R.id.tv_bluetooth_device_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        BluetoothDevice bluetoothDevice = mList.get(i);
        if (TextUtils.isEmpty(bluetoothDevice.getName())) {
            viewHolder.tvName.setText(bluetoothDevice.getAddress());
        } else {
            viewHolder.tvName.setText(bluetoothDevice.getName());
        }
        return view;
    }
}
