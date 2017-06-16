package com.smart.kitchen.smartkitchen.fragments;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import com.smart.kitchen.smartkitchen.BaseFragment;
import com.smart.kitchen.smartkitchen.MyApplication;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.print.dialog.UsbDeviceChooseDialog;
import com.smart.kitchen.smartkitchen.print.receiver.UsbDeviceReceiver;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import com.smart.kitchen.smartkitchen.view.SettingSwitchButton;
import com.smart.kitchen.smartkitchen.view.SettingSwitchButton.OnChangeListener;
import driver.BitmapConvertUtil;
import driver.HsBluetoothPrintDriver;
import driver.HsUsbPrintDriver;
import driver.HsWifiPrintDriver;

public class SettingPrintFragment extends BaseFragment {
    private static final int ALBUM_IMAGE_ACTIVITY_REQUEST_CODE = 209;
    private static String[] mFontArrayString = null;
    private final String TAG = getClass().getSimpleName();
    private EditText editGuandanPrintNum;
    private EditText editPrintNum;
    private EditText editPrintTimeKitchen;
    private EditText etConnectK;
    private EditText etPortK;
    private Uri imageUri;
    private ImageView imgLogo;
    private ImageView imgLogoContent;
    private TextView llConnectSpinner;
    private ArrayAdapter<String> mArrayAdapter;
    private Bitmap mBitmap;
    private Context mContext;
    Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 0:
                    SPUtils.setUserinfo(SettingPrintFragment.this.mContext, SPUtils.PRINT_KITCHEN_IP, SettingPrintFragment.this.etConnectK.getText().toString().trim());
                    SPUtils.setUserinfo(SettingPrintFragment.this.mContext, SPUtils.PRINT_KITCHEN_PORT, SettingPrintFragment.this.etPortK.getText().toString().trim());
                    SettingPrintFragment.this.tvConnectOk.setText("连接成功");
                    return;
                case 1:
                    SettingPrintFragment.this.tvConnectOk.setText("连接失败");
                    return;
                default:
                    return;
            }
        }
    };
    private int mKitchentype = 1;
    private int mPapertype = 0;
    private UsbDevice mUsbDevice;
    private UsbManager mUsbManager;
    private boolean mUsbRegistered = false;
    private RadioGroup rgPrintImage;
    private RadioGroup rgPrintKitchen;
    private SettingSwitchButton sbKitchenPrint;
    private SettingSwitchButton sbMerge;
    private SettingSwitchButton sbPrintLogo;
    private String selectLogoUri = "";
    private FrameLayout settingKitchenPrint;
    private FrameLayout settingNormalPrint;
    private Spinner settingSpinner;
    private TextView tvConnectK;
    private TextView tvConnectOk;
    private TextView tvHeatSensitiveConnect;
    private TextView tvKitchenPrint;
    private TextView tvNormalPrint;
    private TextView tvSelectLogo;

    public static SettingPrintFragment newInstance() {
        SettingPrintFragment settingPrintFragment = new SettingPrintFragment();
        settingPrintFragment.setArguments(new Bundle());
        return settingPrintFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = getActivity();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_setting_print, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        initView(view);
    }

    protected void initEvent() {
        this.sbKitchenPrint.setOnChangeListener(new OnChangeListener() {
            public void onChange(boolean z) {
                SPUtils.setUserinfo(SettingPrintFragment.this.context, SPUtils.SETTING_PRINT_KITCHEN, Boolean.valueOf(z));
            }
        });
        this.sbMerge.setOnChangeListener(new OnChangeListener() {
            public void onChange(boolean z) {
                SPUtils.setUserinfo(SettingPrintFragment.this.context, SPUtils.SETTING_PRINT_MERGE, Boolean.valueOf(z));
            }
        });
        this.sbPrintLogo.setOnChangeListener(new OnChangeListener() {
            public void onChange(boolean z) {
                SPUtils.setUserinfo(SettingPrintFragment.this.context, SPUtils.SETTING_PRINT_LOGO, Boolean.valueOf(z));
            }
        });
    }

    protected void initData() {
        if (-1 != SPUtils.getUserinfos(this.mContext, SPUtils.SETTING_PRINT_PAPERSTYLE)) {
            this.mPapertype = SPUtils.getUserinfos(this.mContext, SPUtils.SETTING_PRINT_PAPERSTYLE);
        }
        if (-1 != SPUtils.getUserinfos(this.mContext, SPUtils.SETTING_PRINT_KITCHENSTYLE)) {
            this.mKitchentype = SPUtils.getUserinfos(this.mContext, SPUtils.SETTING_PRINT_KITCHENSTYLE);
        }
        if (SPUtils.getUserinfos2(this.context, SPUtils.SETTING_PRINT_KITCHEN)) {
            this.sbKitchenPrint.setText(SPUtils.getUserinfos2(this.context, SPUtils.SETTING_PRINT_KITCHEN));
            this.sbKitchenPrint.startAnim();
        }
        if (SPUtils.getUserinfos2(this.context, SPUtils.SETTING_PRINT_LOGO)) {
            this.sbPrintLogo.setText(SPUtils.getUserinfos2(this.context, SPUtils.SETTING_PRINT_LOGO));
            this.sbPrintLogo.startAnim();
        }
        if (SPUtils.getUserinfos2(this.context, SPUtils.SETTING_PRINT_MERGE)) {
            this.sbMerge.setText(SPUtils.getUserinfos2(this.context, SPUtils.SETTING_PRINT_MERGE));
            this.sbMerge.startAnim();
        }
        this.selectLogoUri = SPUtils.getUserinfo(this.context, SPUtils.SETTING_SELECT_LOGO);
        if (this.selectLogoUri != null) {
            this.imgLogoContent.setVisibility(8);
            showImage(Uri.parse(this.selectLogoUri));
        }
        if (SPUtils.getUserinfo(this.mContext, SPUtils.PRINT_DELAY_TIME) != null) {
            this.editPrintTimeKitchen.setText(SPUtils.getUserinfo(this.mContext, SPUtils.PRINT_DELAY_TIME));
        }
        if (SPUtils.getUserinfo(this.context, SPUtils.PRINT_KITCHEN_PORT) != null) {
            this.etPortK.setText(SPUtils.getUserinfo(this.context, SPUtils.PRINT_KITCHEN_PORT));
        }
        if (TextUtils.isEmpty(this.etPortK.getText().toString().trim())) {
            SPUtils.setUserinfo(this.mContext, SPUtils.PRINT_KITCHEN_PORT, "9100");
        } else {
            SPUtils.setUserinfo(this.mContext, SPUtils.PRINT_KITCHEN_PORT, this.etPortK.getText().toString().trim());
        }
        if (SPUtils.getUserinfo(this.context, SPUtils.PRINT_KITCHEN_IP) != null) {
            this.etConnectK.setText(SPUtils.getUserinfo(this.context, SPUtils.PRINT_KITCHEN_IP));
        }
        if (TextUtils.isEmpty(this.etConnectK.getText().toString().trim())) {
            SPUtils.setUserinfo(this.mContext, SPUtils.PRINT_KITCHEN_IP, "");
        } else {
            SPUtils.setUserinfo(this.mContext, SPUtils.PRINT_KITCHEN_IP, this.etConnectK.getText().toString().trim());
        }
        switch (this.mPapertype) {
            case 0:
                this.rgPrintImage.check(R.id.rg_print_image_58);
                SPUtils.setUserinfo(this.mContext, SPUtils.SETTING_PRINT_PAPERSTYLE, Integer.valueOf(0));
                break;
            case 1:
                this.rgPrintImage.check(R.id.rg_print_image_80);
                SPUtils.setUserinfo(this.mContext, SPUtils.SETTING_PRINT_PAPERSTYLE, Integer.valueOf(1));
                break;
        }
        switch (this.mKitchentype) {
            case 0:
                this.rgPrintKitchen.check(R.id.rg_print_kitchen_58);
                SPUtils.setUserinfo(this.mContext, SPUtils.SETTING_PRINT_KITCHENSTYLE, Integer.valueOf(0));
                return;
            case 1:
                this.rgPrintKitchen.check(R.id.rg_print_kitchen_80);
                SPUtils.setUserinfo(this.mContext, SPUtils.SETTING_PRINT_KITCHENSTYLE, Integer.valueOf(1));
                return;
            default:
                return;
        }
    }

    protected void initView(View view) {
        this.tvNormalPrint = (TextView) view.findViewById(R.id.tv_normal_print);
        this.tvKitchenPrint = (TextView) view.findViewById(R.id.tv_kitchen_print);
        this.settingNormalPrint = (FrameLayout) view.findViewById(R.id.setting_normal_print);
        this.settingKitchenPrint = (FrameLayout) view.findViewById(R.id.setting_kitchen_print);
        this.tvConnectOk = (TextView) view.findViewById(R.id.tv_connect_ok);
        this.etPortK = (EditText) view.findViewById(R.id.et_port_k);
        this.llConnectSpinner = (TextView) view.findViewById(R.id.heat_sensitive_setting_connect_spinner);
        this.llConnectSpinner.setText(R.string.choose_usb_device);
        this.tvHeatSensitiveConnect = (TextView) view.findViewById(R.id.tv_heat_sensitive_connect);
        this.etConnectK = (EditText) view.findViewById(R.id.et_connect_k);
        this.tvConnectK = (TextView) view.findViewById(R.id.tv_connect_k);
        this.tvSelectLogo = (TextView) view.findViewById(R.id.tv_select_logo);
        this.imgLogoContent = (ImageView) view.findViewById(R.id.img_logo_content);
        this.imgLogo = (ImageView) view.findViewById(R.id.img_logo);
        this.sbPrintLogo = (SettingSwitchButton) view.findViewById(R.id.sb_print_logo);
        this.sbKitchenPrint = (SettingSwitchButton) view.findViewById(R.id.sb_kitchen_print);
        this.sbMerge = (SettingSwitchButton) view.findViewById(R.id.sb_merge);
        this.editPrintTimeKitchen = (EditText) view.findViewById(R.id.edit_print_time_kitchen);
        if (TextUtils.isEmpty(this.editPrintTimeKitchen.getText().toString().trim())) {
            SPUtils.setUserinfo(this.mContext, SPUtils.PRINT_DELAY_TIME, "500");
        } else {
            SPUtils.setUserinfo(this.mContext, SPUtils.PRINT_DELAY_TIME, this.editPrintTimeKitchen.getText().toString().trim());
        }
        this.settingSpinner = (Spinner) view.findViewById(R.id.spinner_print_font);
        mFontArrayString = new String[]{getResources().getString(R.string.font_small), getResources().getString(R.string.font_big)};
        this.mArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.setting_spinner, mFontArrayString) {
            public View getDropDownView(int i, View view, ViewGroup viewGroup) {
                if (view == null) {
                    view = SettingPrintFragment.this.getActivity().getLayoutInflater().inflate(R.layout.setting_spinner_item, viewGroup, false);
                }
                ((TextView) view.findViewById(R.id.spinner_textView)).setText((CharSequence) getItem(i));
                return view;
            }
        };
        this.settingSpinner.setAdapter(this.mArrayAdapter);
        this.settingSpinner.setSelection(0, false);
        this.settingSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                SPUtils.setUserinfo(SettingPrintFragment.this.context, SPUtils.PRINT_FONT, String.valueOf(i));
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        if (SPUtils.getUserinfo(this.context, SPUtils.PRINT_FONT) != null) {
            this.settingSpinner.setSelection(Integer.valueOf(SPUtils.getUserinfo(this.context, SPUtils.PRINT_FONT)).intValue());
        }
        this.rgPrintImage = (RadioGroup) view.findViewById(R.id.rg_print_image);
        this.rgPrintKitchen = (RadioGroup) view.findViewById(R.id.rg_print_kitchen);
        this.editPrintNum = (EditText) view.findViewById(R.id.edit_print_num);
        if (SPUtils.getUserinfo(this.context, SPUtils.PRINT_NUM) != null) {
            this.editPrintNum.setText(SPUtils.getUserinfo(this.context, SPUtils.PRINT_NUM));
        }
        if (TextUtils.isEmpty(this.editPrintNum.getText().toString().trim())) {
            SPUtils.setUserinfo(this.mContext, SPUtils.PRINT_NUM, "1");
        } else {
            SPUtils.setUserinfo(this.mContext, SPUtils.PRINT_NUM, this.editPrintNum.getText().toString().trim());
        }
        this.editGuandanPrintNum = (EditText) view.findViewById(R.id.edit_guadan_print_num);
        if (SPUtils.getUserinfo(this.context, SPUtils.GUANDAN_PRINT_NUM) != null) {
            this.editGuandanPrintNum.setText(SPUtils.getUserinfo(this.context, SPUtils.GUANDAN_PRINT_NUM));
        }
        if (TextUtils.isEmpty(this.editGuandanPrintNum.getText().toString().trim())) {
            SPUtils.setUserinfo(this.mContext, SPUtils.GUANDAN_PRINT_NUM, "1");
        } else {
            SPUtils.setUserinfo(this.mContext, SPUtils.GUANDAN_PRINT_NUM, this.editGuandanPrintNum.getText().toString().trim());
        }
        setListener();
    }

    private void reset() {
        this.tvNormalPrint.setTextColor(this.mContext.getResources().getColor(R.color.black));
        this.tvNormalPrint.setBackground(this.mContext.getResources().getDrawable(R.color.white));
        this.tvKitchenPrint.setTextColor(this.mContext.getResources().getColor(R.color.black));
        this.tvKitchenPrint.setBackground(this.mContext.getResources().getDrawable(R.color.white));
    }

    private void setListener() {
        this.tvKitchenPrint.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SettingPrintFragment.this.reset();
                SettingPrintFragment.this.tvKitchenPrint.setTextColor(SettingPrintFragment.this.mContext.getResources().getColor(R.color.white));
                SettingPrintFragment.this.tvKitchenPrint.setBackground(SettingPrintFragment.this.mContext.getResources().getDrawable(R.mipmap.tag_dock));
                SettingPrintFragment.this.settingKitchenPrint.setVisibility(0);
                SettingPrintFragment.this.settingNormalPrint.setVisibility(8);
            }
        });
        this.tvNormalPrint.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SettingPrintFragment.this.reset();
                SettingPrintFragment.this.tvNormalPrint.setTextColor(SettingPrintFragment.this.mContext.getResources().getColor(R.color.white));
                SettingPrintFragment.this.tvNormalPrint.setBackground(SettingPrintFragment.this.mContext.getResources().getDrawable(R.mipmap.tag_dock));
                SettingPrintFragment.this.settingNormalPrint.setVisibility(0);
                SettingPrintFragment.this.settingKitchenPrint.setVisibility(8);
            }
        });
        this.llConnectSpinner.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SettingPrintFragment.this.showUSBDeviceChooseDialog();
            }
        });
        this.tvHeatSensitiveConnect.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (MyApplication.getConnState() != 16) {
                    SettingPrintFragment.this.disconnect();
                    SettingPrintFragment.this.connect();
                    return;
                }
                SettingPrintFragment.this.connect();
            }
        });
        this.tvConnectK.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(SettingPrintFragment.this.etConnectK.getText().toString().trim()) || TextUtils.isEmpty(SettingPrintFragment.this.etPortK.getText().toString().trim())) {
                    Toasts.show(SettingPrintFragment.this.context, "IP或者端口号不能为空");
                } else {
                    new Thread() {
                        public void run() {
                            if (HsWifiPrintDriver.getInstance().WIFISocket(SettingPrintFragment.this.etConnectK.getText().toString().trim(), Integer.parseInt(SettingPrintFragment.this.etPortK.getText().toString().trim()))) {
                                SettingPrintFragment.this.mHandler.sendEmptyMessage(0);
                            } else {
                                SettingPrintFragment.this.mHandler.sendEmptyMessage(1);
                            }
                        }
                    }.start();
                }
            }
        });
        this.tvSelectLogo.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                SettingPrintFragment.this.openAlbum();
            }
        });
        this.rgPrintImage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rg_print_image_58:
                        SettingPrintFragment.this.mPapertype = 0;
                        break;
                    case R.id.rg_print_image_80:
                        SettingPrintFragment.this.mPapertype = 1;
                        break;
                }
                SPUtils.setUserinfo(SettingPrintFragment.this.mContext, SPUtils.SETTING_PRINT_PAPERSTYLE, Integer.valueOf(SettingPrintFragment.this.mPapertype));
            }
        });
        this.rgPrintKitchen.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rg_print_kitchen_58:
                        SettingPrintFragment.this.mKitchentype = 0;
                        break;
                    case R.id.rg_print_kitchen_80:
                        SettingPrintFragment.this.mKitchentype = 1;
                        break;
                }
                SPUtils.setUserinfo(SettingPrintFragment.this.mContext, SPUtils.SETTING_PRINT_KITCHENSTYLE, Integer.valueOf(SettingPrintFragment.this.mKitchentype));
            }
        });
        this.editPrintNum.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    SPUtils.setUserinfo(SettingPrintFragment.this.mContext, SPUtils.PRINT_NUM, editable.toString());
                }
            }
        });
        this.editGuandanPrintNum.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    SPUtils.setUserinfo(SettingPrintFragment.this.mContext, SPUtils.GUANDAN_PRINT_NUM, editable.toString());
                }
            }
        });
        this.editPrintTimeKitchen.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable.toString())) {
                    SPUtils.setUserinfo(SettingPrintFragment.this.mContext, SPUtils.PRINT_DELAY_TIME, editable.toString());
                }
            }
        });
    }

    private void openAlbum() {
        startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), ALBUM_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        switch (i) {
            case ALBUM_IMAGE_ACTIVITY_REQUEST_CODE /*209*/:
                if (i2 == -1) {
                    LogUtils.d(this.TAG, "getData = " + intent.getData());
                    this.imageUri = intent.getData();
                    SPUtils.setUserinfo(this.context, SPUtils.SETTING_SELECT_LOGO, this.imageUri + "");
                    this.selectLogoUri = SPUtils.getUserinfo(this.context, SPUtils.SETTING_SELECT_LOGO);
                    showImage(this.imageUri);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void showImage(Uri uri) {
        this.imgLogoContent.setVisibility(8);
        this.imgLogo.setVisibility(0);
        if (this.mBitmap != null) {
            this.mBitmap.recycle();
            this.mBitmap = null;
            System.gc();
        }
        switch (this.mPapertype) {
            case 0:
                this.mBitmap = BitmapConvertUtil.decodeSampledBitmapFromUri(this.mContext, uri, 384, 4000);
                break;
            case 1:
                this.mBitmap = BitmapConvertUtil.decodeSampledBitmapFromUri(this.mContext, uri, 576, 4000);
                break;
        }
        LogUtils.d(this.TAG, "mBitmap getWidth = " + this.mBitmap.getWidth());
        LogUtils.d(this.TAG, "mBitmap getHeight = " + this.mBitmap.getHeight());
        this.imgLogo.setImageBitmap(this.mBitmap);
    }

    private void showUSBDeviceChooseDialog() {
        if (!this.mUsbRegistered) {
            this.mUsbRegistered = true;
        }
        final UsbDeviceChooseDialog usbDeviceChooseDialog = new UsbDeviceChooseDialog();
        usbDeviceChooseDialog.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                SettingPrintFragment.this.mUsbDevice = (UsbDevice) adapterView.getAdapter().getItem(i);
                SettingPrintFragment.this.llConnectSpinner.setText(SettingPrintFragment.this.getString(R.string.print_device) + SettingPrintFragment.this.mUsbDevice.getVendorId() + SettingPrintFragment.this.mUsbDevice.getProductId() + SettingPrintFragment.this.mUsbDevice.getDeviceId());
                usbDeviceChooseDialog.dismiss();
            }
        });
        usbDeviceChooseDialog.show(getActivity().getFragmentManager(), null);
    }

    private void connectUsb(UsbDevice usbDevice) {
        HsUsbPrintDriver.getInstance().connect(usbDevice);
    }

    private void disconnect() {
        switch (MyApplication.getConnState()) {
            case 17:
                HsBluetoothPrintDriver.getInstance().stop();
                return;
            case 18:
                HsUsbPrintDriver.getInstance().stop();
                return;
            case 19:
                HsWifiPrintDriver.getInstance().stop();
                return;
            default:
                return;
        }
    }

    private void connect() {
        if (TextUtils.isEmpty(this.llConnectSpinner.getText().toString())) {
            Toasts.show(this.mContext, (int) R.string.tip_choose_usb_device);
            return;
        }
        if (this.mUsbManager == null) {
            this.mUsbManager = (UsbManager) this.mContext.getSystemService("usb");
        }
        if (this.mUsbDevice == null) {
            return;
        }
        if (this.mUsbManager.hasPermission(this.mUsbDevice)) {
            connectUsb(this.mUsbDevice);
            return;
        }
        this.mUsbManager.requestPermission(this.mUsbDevice, PendingIntent.getBroadcast(this.mContext, 0, new Intent(UsbDeviceReceiver.ACTION_USB_PERMISSION), 0));
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
