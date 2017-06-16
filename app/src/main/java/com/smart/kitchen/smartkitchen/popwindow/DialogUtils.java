package com.smart.kitchen.smartkitchen.popwindow;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.activity.NewsParticularsActivity;
import com.smart.kitchen.smartkitchen.adapters.CheckDialogAdapter;
import com.smart.kitchen.smartkitchen.adapters.DaoGouAdapter;
import com.smart.kitchen.smartkitchen.adapters.MsgAdapter;
import com.smart.kitchen.smartkitchen.adapters.SelectAreaAdapter;
import com.smart.kitchen.smartkitchen.adapters.SpinnerTypeAdapter;
import com.smart.kitchen.smartkitchen.adapters.StandardAdapter;
import com.smart.kitchen.smartkitchen.db.daoutils.MessageCenterDaoManager;
import com.smart.kitchen.smartkitchen.entitys.FromType;
import com.smart.kitchen.smartkitchen.entitys.GoodSize;
import com.smart.kitchen.smartkitchen.entitys.GoodTaste;
import com.smart.kitchen.smartkitchen.entitys.MessageCenter;
import com.smart.kitchen.smartkitchen.entitys.OrderGoods;
import com.smart.kitchen.smartkitchen.entitys.TableArea;
import com.smart.kitchen.smartkitchen.entitys.UserInfo;
import com.smart.kitchen.smartkitchen.utils.CalendarUtils;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import com.smart.kitchen.smartkitchen.view.CircleView;
import com.smart.kitchen.smartkitchen.view.MyListView;
import com.vilyever.b.a;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DialogUtils {
    private static final String TAG = "DialogUtils";
    private int TIME = 1000;
    PopupWindow awaitPopWindow;
    private CheckListener checkListener;
    private Activity context;
    private onCouponLinstener couponLinstener;
    private DistoryPopuListener distoryPopu;
    int i = 0;
    private PropertyListener mListener;
    private PropertyListener2 mListener2;
    int num = 0;
    int property = 0;
    private onScanLinstener scanLinstener;
    private ScreenListener screenListener;
    int standard = 0;
    public PopupWindow submitPopWindow;

    public interface OnClickListener {
        void onClick(Object obj);
    }

    public interface onScanLinstener {
        void scan(String str, String str2);
    }

    public interface onCouponLinstener {
        void onCoupon(String str);
    }

    public interface DistoryPopuListener {
        void sendMessage(String str, String str2);
    }

    public interface PropertyListener {
        void dataTransmission(int i, int i2, int i3, String str);
    }

    public interface PropertyListener2 {
        void dataTransmission2(int i, int i2, int i3, String str);
    }

    public interface CheckListener {
        void property(Map<String, String> map);
    }

    public interface ScreenListener {
        void type(String str, String str2, String str3, String str4, String str5);
    }

    public DialogUtils(Activity activity) {
        this.context = activity;
    }

    public void backgroundAlpha(Activity activity, float f) {
        LayoutParams attributes = activity.getWindow().getAttributes();
        attributes.alpha = f;
        activity.getWindow().addFlags(2);
        activity.getWindow().setAttributes(attributes);
    }

    public void showBeiZhu(View view, Long l, List<OrderGoods> list, int i, List<GoodSize> list2, List<GoodTaste> list3, OnClickListener onClickListener) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_extra, null, false);
        CircleView circleView = (CircleView) inflate.findViewById(R.id.cv_extra_add);
        CircleView circleView2 = (CircleView) inflate.findViewById(R.id.cv_extra_delete);
        final TextView textView = (TextView) inflate.findViewById(R.id.tv_extra_number);
        GridView gridView = (GridView) inflate.findViewById(R.id.gv_extra_property);
        GridView gridView2 = (GridView) inflate.findViewById(R.id.gv_extra_standard);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_extra_ok);
        TextView textView3 = (TextView) inflate.findViewById(R.id.tv_extra_clear);
        final EditText editText = (EditText) inflate.findViewById(R.id.et_extra);
        if (list2.size() > 0) {
            this.property = 0;
            final StandardAdapter standardAdapter = new StandardAdapter(this.context, list2, 0);
            gridView.setAdapter(standardAdapter);
            final List<GoodSize> list4 = list2;
            gridView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    if (((GoodSize) list4.get(i)).getCount().intValue() != 0) {
                        standardAdapter.setSelectFoodType(i);
                        DialogUtils.this.property = i;
                    }
                }
            });
        }
        if (list3.size() > 0) {
            this.standard = 0;
            final StandardAdapter standardAdapter2 = new StandardAdapter(this.context, list3, 1);
            gridView2.setAdapter(standardAdapter2);
            gridView2.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    standardAdapter2.setSelectFoodType(i);
                    DialogUtils.this.standard = i;
                }
            });
        }
        circleView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                textView.setText(String.valueOf(Integer.valueOf(textView.getText().toString().trim()).intValue() + 1));
            }
        });
        circleView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int intValue = Integer.valueOf(textView.getText().toString().trim()).intValue();
                if (intValue != 1) {
                    textView.setText(String.valueOf(intValue - 1));
                }
            }
        });
        final PopupWindow popupWindow = new PopupWindow(inflate, -2, -2, true);
        final int i2 = i;
        final List<GoodSize> list5 = list2;
        final List<OrderGoods> list6 = list;
        final Long l2 = l;
        final OnClickListener onClickListener2 = onClickListener;
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int i = 0;
                if (i2 == 0) {
                    if (Integer.parseInt(textView.getText().toString().trim()) > ((GoodSize) list5.get(DialogUtils.this.property)).getCount().intValue()) {
                        Toasts.show(DialogUtils.this.context, "该规格库存不足");
                        return;
                    }
                    int i2 = 0;
                    while (i < list6.size()) {
                        if (l2.equals(((OrderGoods) list6.get(i)).getGoods().getId()) && ((GoodSize) list5.get(DialogUtils.this.property)).getId().equals(((OrderGoods) list6.get(i)).getGoodsize().getId())) {
                            i2 += ((OrderGoods) list6.get(i)).getCount();
                        }
                        i++;
                    }
                    if (i2 + Integer.parseInt(textView.getText().toString().trim()) > ((GoodSize) list5.get(DialogUtils.this.property)).getCount().intValue()) {
                        Toasts.show(DialogUtils.this.context, "该规格库存不足");
                        return;
                    }
                }
                if (onClickListener2 != null) {
                    onClickListener2.onClick(null);
                }
                if (DialogUtils.this.mListener != null) {
                    DialogUtils.this.mListener.dataTransmission(DialogUtils.this.property, DialogUtils.this.standard, Integer.valueOf(textView.getText().toString().trim()).intValue(), editText.getText().toString().trim());
                }
                popupWindow.dismiss();
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        popupWindow.setSoftInputMode(1);
        popupWindow.setSoftInputMode(16);
        popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void PropertyListener(PropertyListener propertyListener) {
        this.mListener = propertyListener;
    }

    public void showBeiZhu2(View view, final List<GoodSize> list, List<GoodTaste> list2, OnClickListener onClickListener) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_extra, null, false);
        CircleView circleView = (CircleView) inflate.findViewById(R.id.cv_extra_add);
        CircleView circleView2 = (CircleView) inflate.findViewById(R.id.cv_extra_delete);
        final TextView textView = (TextView) inflate.findViewById(R.id.tv_extra_number);
        GridView gridView = (GridView) inflate.findViewById(R.id.gv_extra_property);
        GridView gridView2 = (GridView) inflate.findViewById(R.id.gv_extra_standard);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_extra_ok);
        TextView textView3 = (TextView) inflate.findViewById(R.id.tv_extra_clear);
        final EditText editText = (EditText) inflate.findViewById(R.id.et_extra);
        if (list.size() > 0) {
            this.property = 0;
            final StandardAdapter standardAdapter = new StandardAdapter(this.context, list, 0);
            gridView.setAdapter(standardAdapter);
            gridView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    if (((GoodSize) list.get(i)).getCount().intValue() != 0) {
                        standardAdapter.setSelectFoodType(i);
                        DialogUtils.this.property = i;
                    }
                }
            });
        }
        if (list2.size() > 0) {
            this.standard = 0;
            final StandardAdapter standardAdapter2 = new StandardAdapter(this.context, list2, 1);
            gridView2.setAdapter(standardAdapter2);
            gridView2.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    standardAdapter2.setSelectFoodType(i);
                    DialogUtils.this.standard = i;
                }
            });
        }
        circleView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                textView.setText(String.valueOf(Integer.valueOf(textView.getText().toString().trim()).intValue() + 1));
            }
        });
        circleView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int intValue = Integer.valueOf(textView.getText().toString().trim()).intValue();
                if (intValue != 1) {
                    textView.setText(String.valueOf(intValue - 1));
                }
            }
        });
        final PopupWindow popupWindow = new PopupWindow(inflate, -2, -2, true);
        final OnClickListener onClickListener2 = onClickListener;
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (onClickListener2 != null) {
                    onClickListener2.onClick(null);
                }
                if (DialogUtils.this.mListener2 != null) {
                    DialogUtils.this.mListener2.dataTransmission2(DialogUtils.this.property, DialogUtils.this.standard, Integer.valueOf(textView.getText().toString().trim()).intValue(), editText.getText().toString().trim());
                }
                popupWindow.dismiss();
            }
        });
        textView3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        popupWindow.setSoftInputMode(1);
        popupWindow.setSoftInputMode(16);
        popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void PropertyListener2(PropertyListener2 propertyListener2) {
        this.mListener2 = propertyListener2;
    }

    public void showUpdataPerson(View view) {
        PopupWindow popupWindow = new PopupWindow(LayoutInflater.from(this.context).inflate(R.layout.dialog_updata_person_count, null, false), -2, -2, true);
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void showConfirm(View view, String str, String[] strArr, final OnClickListener onClickListener, final OnClickListener onClickListener2) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_vip_not_exist, null, false);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_first_button);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_second_button);
        ((TextView) inflate.findViewById(R.id.tv_title)).setText(str);
        textView.setText(strArr[0]);
        textView2.setText(strArr[1]);
        final PopupWindow popupWindow = new PopupWindow(inflate, -2, -2, true);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popupWindow.dismiss();
                if (onClickListener != null) {
                    onClickListener.onClick(null);
                }
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popupWindow.dismiss();
                if (onClickListener2 != null) {
                    onClickListener2.onClick(null);
                }
            }
        });
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void showTips(View view, String str) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_submit_success, null, false);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_submit_success);
        if (!Contants.isEmpty(str)) {
            textView.setText(str);
        }
        this.submitPopWindow = new PopupWindow(inflate, -2, -2, true);
        this.submitPopWindow.setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        textView.setText(str);
        this.submitPopWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        this.submitPopWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        this.submitPopWindow.showAtLocation(view, 17, 0, 0);
    }

    public void submitPopWindow() {
        this.submitPopWindow.dismiss();
    }

    public void showPay(View view, String str) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_pay, null, false);
        EditText editText = (EditText) inflate.findViewById(R.id.et_aggregate_pay);
        editText = (EditText) inflate.findViewById(R.id.et_income_pay);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_change);
        textView = (TextView) inflate.findViewById(R.id.tv_settle_pay);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_quit_pay);
        final PopupWindow popupWindow = new PopupWindow(inflate, -2, -2, true);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void showOrderInfo(View view) {
        PopupWindow popupWindow = new PopupWindow(LayoutInflater.from(this.context).inflate(R.layout.dialog_order_info, null, false), -2, -2, true);
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void showDistory2(View view, int i, OnClickListener onClickListener, final OnClickListener onClickListener2) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_distory_order, null, false);
        final EditText editText = (EditText) inflate.findViewById(R.id.et_distory_remark);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_distory_confirm);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_distory_cancel);
        final EditText editText2 = (EditText) inflate.findViewById(R.id.et_distory_num);
        editText.addTextChangedListener(new TextWatcher() {
            CharSequence tmp = "";

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                this.tmp = charSequence;
            }

            public void afterTextChanged(Editable editable) {
                if (this.tmp.length() > 0) {
                    String substring = this.tmp.toString().substring(0, 1);
                    if (this.tmp.length() >= 2 && substring.equals("0")) {
                        editable.delete(0, 1);
                        editText.setText(editable);
                        editText.setSelection(this.tmp.length());
                    }
                }
            }
        });
        final PopupWindow popupWindow = new PopupWindow(inflate, -1, -1, true);
        final OnClickListener onClickListener3 = onClickListener;
        final int i2 = i;
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (onClickListener3 != null) {
                    onClickListener3.onClick(null);
                }
                if (TextUtils.isEmpty(editText.getText().toString().trim()) || TextUtils.isEmpty(editText2.getText().toString().trim())) {
                    Toasts.show(DialogUtils.this.context, "备注或数量不能为空");
                } else if (i2 - Integer.valueOf(editText2.getText().toString().trim()).intValue() < 0 || 0 - Integer.valueOf(editText2.getText().toString().trim()).intValue() == 0) {
                    Toasts.show(DialogUtils.this.context, "请输入真正确的数量");
                } else {
                    if (DialogUtils.this.distoryPopu != null) {
                        DialogUtils.this.distoryPopu.sendMessage(editText.getText().toString().trim(), editText2.getText().toString().trim());
                    }
                    popupWindow.dismiss();
                }
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (onClickListener2 != null) {
                    onClickListener2.onClick(null);
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        popupWindow.setSoftInputMode(1);
        popupWindow.setSoftInputMode(16);
        popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void showDistory(View view, String str, final OnClickListener onClickListener, final OnClickListener onClickListener2) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_distory_order, null, false);
        final EditText editText = (EditText) inflate.findViewById(R.id.et_distory_remark);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.ll_num);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_distory_confirm);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_distory_cancel);
        ((TextView) inflate.findViewById(R.id.tv_title)).setText(str);
        linearLayout.setVisibility(View.GONE);
        final PopupWindow popupWindow = new PopupWindow(inflate, -1, -1, true);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(null);
                }
                if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                    Toasts.show(DialogUtils.this.context, "备注不能为空");
                    return;
                }
                if (DialogUtils.this.distoryPopu != null) {
                    DialogUtils.this.distoryPopu.sendMessage(editText.getText().toString().trim(), null);
                }
                popupWindow.dismiss();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (onClickListener2 != null) {
                    onClickListener2.onClick(null);
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        popupWindow.setSoftInputMode(1);
        popupWindow.setSoftInputMode(16);
        popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void setDistoryPopuMessage(DistoryPopuListener distoryPopuListener) {
        this.distoryPopu = distoryPopuListener;
    }

    public void showShaiXuan(View view, String str) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.shaixuan, null, false);
        LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.ll_start_date);
        LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(R.id.ll_start_time);
        LinearLayout linearLayout3 = (LinearLayout) inflate.findViewById(R.id.ll_end_date);
        LinearLayout linearLayout4 = (LinearLayout) inflate.findViewById(R.id.ll_end_time);
        LinearLayout linearLayout5 = (LinearLayout) inflate.findViewById(R.id.ll_type);
        final TextView textView = (TextView) inflate.findViewById(R.id.tv_start_date);
        final TextView textView2 = (TextView) inflate.findViewById(R.id.tv_start_time);
        final TextView textView3 = (TextView) inflate.findViewById(R.id.tv_end_date);
        final TextView textView4 = (TextView) inflate.findViewById(R.id.tv_end_time);
        TextView textView5 = (TextView) inflate.findViewById(R.id.tv_cancle);
        TextView textView6 = (TextView) inflate.findViewById(R.id.tv_ok);
        final TextView textView7 = (TextView) inflate.findViewById(R.id.tv_type);
        final ListView listView = (ListView) inflate.findViewById(R.id.listView);
        final SpinnerTypeAdapter spinnerTypeAdapter = new SpinnerTypeAdapter(this.context, FromType.getSimpleData(this.context, str));
        listView.setAdapter(spinnerTypeAdapter);
        textView7.setText(( FromType.getSimpleData(this.context, str).get(0)).getName());
        final String str2 = str;
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                spinnerTypeAdapter.setSelect(i);
                textView7.setText(( FromType.getSimpleData(DialogUtils.this.context, str2).get(i)).getName());
                listView.setVisibility(View.GONE);
            }
        });
        inflate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                listView.setVisibility(View.GONE);
            }
        });
        textView.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        textView3.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        linearLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new DatePickerDialog(DialogUtils.this.context, new OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        textView.setText(i + "-" + CalendarUtils.numToTwo(i2 + 1) + "-" + CalendarUtils.numToTwo(i3));
                    }
                }, CalendarUtils.getNowYear(), CalendarUtils.getNowMonth() - 1, CalendarUtils.getNowDate()).show();
            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new TimePickerDialog(DialogUtils.this.context, new OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        textView2.setText(CalendarUtils.numToTwo(i) + ":" + CalendarUtils.numToTwo(i2));
                    }
                }, CalendarUtils.getNowHour(), CalendarUtils.getNowMinute(), true).show();
            }
        });
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new DatePickerDialog(DialogUtils.this.context, new OnDateSetListener() {
                    public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                        textView3.setText(i + "-" + CalendarUtils.numToTwo(i2 + 1) + "-" + CalendarUtils.numToTwo(i3));
                    }
                }, CalendarUtils.getNowYear(), CalendarUtils.getNowMonth() - 1, CalendarUtils.getNowDate()).show();
            }
        });
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new TimePickerDialog(DialogUtils.this.context, new OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        textView4.setText(CalendarUtils.numToTwo(i) + ":" + CalendarUtils.numToTwo(i2));
                    }
                }, CalendarUtils.getNowHour(), CalendarUtils.getNowMinute(), true).show();
            }
        });
        linearLayout5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                listView.setVisibility(View.VISIBLE);
            }
        });
        final PopupWindow popupWindow = new PopupWindow(inflate, view.getWidth(), -2, true);
        textView5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        final TextView textView8 = textView7;
        textView6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!(TextUtils.isEmpty(textView2.getText().toString().trim()) && TextUtils.isEmpty(textView4.getText().toString().trim()))) {
                    if (TextUtils.isEmpty(textView.getText().toString().trim()) || TextUtils.isEmpty(textView2.getText().toString().trim()) || TextUtils.isEmpty(textView3.getText().toString().trim()) || TextUtils.isEmpty(textView4.getText().toString().trim())) {
                        Toasts.show(DialogUtils.this.context, "请输入完整时间");
                        return;
                    }
                    String str = textView.getText().toString().trim() + " " + textView2.getText().toString().trim();
                    String str2 = textView3.getText().toString().trim() + " " + textView4.getText().toString().trim();
                    DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Calendar instance = Calendar.getInstance();
                    Calendar instance2 = Calendar.getInstance();
                    try {
                        instance.setTime(simpleDateFormat.parse(str));
                        instance2.setTime(simpleDateFormat.parse(str2));
                        if (instance.compareTo(instance2) > 0) {
                            Toasts.show(DialogUtils.this.context, "时间输入有误,请重新输入");
                            return;
                        }
                    } catch (ParseException e) {
                        Toasts.show(DialogUtils.this.context, "格式不正确");
                        return;
                    }
                }
                if (DialogUtils.this.screenListener != null) {
                    DialogUtils.this.screenListener.type(textView8.getText().toString().trim(), textView.getText().toString().trim(), textView2.getText().toString().trim(), textView3.getText().toString().trim(), textView4.getText().toString().trim());
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.showAsDropDown(view, 0, 0);
    }

    public void screenListener(ScreenListener screenListener) {
        this.screenListener = screenListener;
    }

    public void showDaoGou(View view, List<UserInfo> list, final OnItemClickListener onItemClickListener) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.daogou, null, false);
        ListView listView = (ListView) inflate.findViewById(R.id.lV_daogou);
        final PopupWindow popupWindow = new PopupWindow(inflate, view.getWidth(), -2, true);
        final DaoGouAdapter daoGouAdapter = new DaoGouAdapter(list, this.context);
        listView.setAdapter(daoGouAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                daoGouAdapter.setSelect(i);
                popupWindow.dismiss();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(adapterView, view, i, j);
                }
            }
        });
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.showAsDropDown(view, 0, 0);
    }

    public void showWaimaiType(View view) {
        PopupWindow popupWindow = new PopupWindow(LayoutInflater.from(this.context).inflate(R.layout.waimai_type, null, false), view.getWidth(), -2, true);
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.showAsDropDown(view, 0, 0);
    }

    public void showMSG(View view, View view2, List<MessageCenter> list, final List<MessageCenter> list2, View.OnClickListener onClickListener) {
        final MessageCenterDaoManager messageCenterDaoManager = new MessageCenterDaoManager();
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.msg, null, false);
        ScrollView scrollView = (ScrollView) inflate.findViewById(R.id.scrollView);
        MyListView myListView = (MyListView) inflate.findViewById(R.id.listView1);
        MyListView myListView2 = (MyListView) inflate.findViewById(R.id.listView2);
        final PopupWindow popupWindow = new PopupWindow(inflate, view2.getWidth(), -2, true);
        final List<MessageCenter> list3 = list;
        final View.OnClickListener onClickListener2 = onClickListener;
        myListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                messageCenterDaoManager.updateStatus((MessageCenter) list3.get(i));
                popupWindow.dismiss();
                Intent intent = new Intent(DialogUtils.this.context, NewsParticularsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("MessageCenter", (Serializable) list3.get(i));
                intent.putExtras(bundle);
                DialogUtils.this.context.startActivity(intent);
                if (onClickListener2 != null) {
                    onClickListener2.onClick(view);
                }
            }
        });
        myListView2.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Intent intent = new Intent(DialogUtils.this.context, NewsParticularsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("MessageCenter", (Serializable) list2.get(i));
                intent.putExtras(bundle);
                DialogUtils.this.context.startActivity(intent);
            }
        });
        ListAdapter msgAdapter = new MsgAdapter(this.context, list, false);
        ListAdapter msgAdapter2 = new MsgAdapter(this.context, list2);
        myListView.setAdapter(msgAdapter);
        myListView2.setAdapter(msgAdapter2);
        scrollView.smoothScrollTo(0, 0);
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.showAsDropDown(view, 0, 0);
    }

    public void scanAwait(View view) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_loading, null, false);
        final EditText editText = (EditText) inflate.findViewById(R.id.et_loading);
        View findViewById = inflate.findViewById(R.id.view1);
        View findViewById2 = inflate.findViewById(R.id.view2);
        View findViewById3 = inflate.findViewById(R.id.view3);
        View findViewById4 = inflate.findViewById(R.id.view4);
        View findViewById5 = inflate.findViewById(R.id.view5);
        View findViewById6 = inflate.findViewById(R.id.view6);
        ((ImageView) inflate.findViewById(R.id.iv_close)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogUtils.this.awaitPopWindow.dismiss();
            }
        });
        final List arrayList = new ArrayList();
        arrayList.add(findViewById);
        arrayList.add(findViewById2);
        arrayList.add(findViewById3);
        arrayList.add(findViewById4);
        arrayList.add(findViewById5);
        arrayList.add(findViewById6);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try {
                    handler.postDelayed(this, (long) DialogUtils.this.TIME);
                    if (DialogUtils.this.i == 6) {
                        for (int i = 0; i < arrayList.size(); i++) {
                            ((View) arrayList.get(i)).setBackgroundResource(R.drawable.circle_gray_white);
                        }
                        DialogUtils.this.i = 0;
                    }
                    ((View) arrayList.get(DialogUtils.this.i)).setBackgroundResource(R.drawable.circle_red);
                    DialogUtils dialogUtils = DialogUtils.this;
                    dialogUtils.i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, (long) this.TIME);
        editText.setInputType(0);
        editText.requestFocus();
        editText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (66 != i || keyEvent.getAction() != 0) {
                    return false;
                }
                if (DialogUtils.this.couponLinstener != null) {
                    DialogUtils.this.couponLinstener.onCoupon(editText.getText().toString().trim());
                }
                editText.setText("");
                DialogUtils.this.awaitPopWindow.dismiss();
                return true;
            }
        });
        this.awaitPopWindow = new PopupWindow(inflate, -1, -1, true);
        this.awaitPopWindow.setOutsideTouchable(true);
        this.awaitPopWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        this.awaitPopWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        this.awaitPopWindow.showAtLocation(view, 17, 0, 0);
    }

    public void setCouponLinstener(onCouponLinstener com_smart_kitchen_smartkitchen_popwindow_DialogUtils_onCouponLinstener) {
        this.couponLinstener = com_smart_kitchen_smartkitchen_popwindow_DialogUtils_onCouponLinstener;
    }

    public void closeWait() {
        if (this.awaitPopWindow != null) {
            this.awaitPopWindow.dismiss();
        }
    }

    public void showCheck(View view, List<GoodSize> list, final OnClickListener onClickListener) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_check, null, false);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_extra_ok);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_extra_clear);
        ListView listView = (ListView) inflate.findViewById(R.id.lw_check);
        final CheckDialogAdapter checkDialogAdapter = new CheckDialogAdapter(this.context, list);
        listView.setAdapter(checkDialogAdapter);
        Log.e(TAG, "onClick: " + list.toString());
        final PopupWindow popupWindow = new PopupWindow(inflate, -1, -1, true);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(null);
                }
                if (DialogUtils.this.checkListener != null) {
                    Log.e(DialogUtils.TAG, "onClick: " + checkDialogAdapter.getNumMap().toString());
                    DialogUtils.this.checkListener.property(checkDialogAdapter.getNumMap());
                }
                popupWindow.dismiss();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        popupWindow.setSoftInputMode(1);
        popupWindow.setSoftInputMode(16);
        popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void propertyListener(CheckListener checkListener) {
        this.checkListener = checkListener;
    }

    public void showParticulars(View view, OrderGoods orderGoods) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_particulars, null, false);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.iv_close);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_particulars_standard);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_particulars_property);
        TextView textView3 = (TextView) inflate.findViewById(R.id.tv_particulars_number);
        TextView textView4 = (TextView) inflate.findViewById(R.id.tv_particulars_mark);
        if (orderGoods.getGoodsize().getSpec_name() != null) {
            textView.setText(orderGoods.getGoodsize().getSpec_name());
        } else {
            textView.setText("");
        }
        if (orderGoods.getGoodtaste() != null) {
            textView2.setText(orderGoods.getGoodtaste().getTastename());
        } else {
            textView2.setText("");
        }
        textView3.setText(orderGoods.getCount() + "");
        if (TextUtils.isEmpty(orderGoods.getMark()) || orderGoods.getMark() == null) {
            textView4.setText("");
        } else {
            textView4.setText(orderGoods.getMark());
        }
        final PopupWindow popupWindow = new PopupWindow(inflate, -2, -2, true);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void showScan(View view) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_loading, null, false);
        final EditText editText = (EditText) inflate.findViewById(R.id.et_loading);
        View findViewById = inflate.findViewById(R.id.view1);
        View findViewById2 = inflate.findViewById(R.id.view2);
        View findViewById3 = inflate.findViewById(R.id.view3);
        View findViewById4 = inflate.findViewById(R.id.view4);
        View findViewById5 = inflate.findViewById(R.id.view5);
        View findViewById6 = inflate.findViewById(R.id.view6);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.iv_close);
        final List arrayList = new ArrayList();
        arrayList.add(findViewById);
        arrayList.add(findViewById2);
        arrayList.add(findViewById3);
        arrayList.add(findViewById4);
        arrayList.add(findViewById5);
        arrayList.add(findViewById6);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try {
                    handler.postDelayed(this, (long) DialogUtils.this.TIME);
                    if (DialogUtils.this.i == 6) {
                        for (int i = 0; i < arrayList.size(); i++) {
                            ((View) arrayList.get(i)).setBackgroundResource(R.drawable.circle_gray_white);
                        }
                        DialogUtils.this.i = 0;
                    }
                    ((View) arrayList.get(DialogUtils.this.i)).setBackgroundResource(R.drawable.circle_red);
                    DialogUtils dialogUtils = DialogUtils.this;
                    dialogUtils.i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, (long) this.TIME);
        final PopupWindow popupWindow = new PopupWindow(inflate, -1, -1, true);
        popupWindow.setOutsideTouchable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        editText.setInputType(0);
        editText.requestFocus();
        editText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (66 != i || keyEvent.getAction() != 0) {
                    return false;
                }
                if (DialogUtils.this.couponLinstener != null) {
                    DialogUtils.this.couponLinstener.onCoupon(editText.getText().toString().trim());
                }
                editText.setText("");
                popupWindow.dismiss();
                return true;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void showScanVIP(View view, final String str) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_loading, null, false);
        final EditText editText = (EditText) inflate.findViewById(R.id.et_loading);
        View findViewById = inflate.findViewById(R.id.view1);
        View findViewById2 = inflate.findViewById(R.id.view2);
        View findViewById3 = inflate.findViewById(R.id.view3);
        View findViewById4 = inflate.findViewById(R.id.view4);
        View findViewById5 = inflate.findViewById(R.id.view5);
        View findViewById6 = inflate.findViewById(R.id.view6);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.iv_close);
        final List arrayList = new ArrayList();
        arrayList.add(findViewById);
        arrayList.add(findViewById2);
        arrayList.add(findViewById3);
        arrayList.add(findViewById4);
        arrayList.add(findViewById5);
        arrayList.add(findViewById6);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try {
                    handler.postDelayed(this, (long) DialogUtils.this.TIME);
                    if (DialogUtils.this.i == 6) {
                        for (int i = 0; i < arrayList.size(); i++) {
                            ((View) arrayList.get(i)).setBackgroundResource(R.drawable.circle_gray_white);
                        }
                        DialogUtils.this.i = 0;
                    }
                    ((View) arrayList.get(DialogUtils.this.i)).setBackgroundResource(R.drawable.circle_red);
                    DialogUtils dialogUtils = DialogUtils.this;
                    dialogUtils.i++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, (long) this.TIME);
        final PopupWindow popupWindow = new PopupWindow(inflate, -1, -1, true);
        popupWindow.setOutsideTouchable(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        editText.setInputType(0);
        editText.requestFocus();
        editText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (66 != i || keyEvent.getAction() != 0) {
                    return false;
                }
                if (Contants.isEmpty(editText.getText().toString())) {
                    LogUtils.e(DialogUtils.TAG, "onKey:null ");
                    return false;
                }
                LogUtils.e(DialogUtils.TAG, "onKey: " + DialogUtils.this.scanLinstener);
                if (DialogUtils.this.scanLinstener != null) {
                    DialogUtils.this.scanLinstener.scan(editText.getText().toString(), str);
                }
                editText.setText("");
                popupWindow.dismiss();
                return true;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void setScanListener(onScanLinstener com_smart_kitchen_smartkitchen_popwindow_DialogUtils_onScanLinstener) {
        this.scanLinstener = com_smart_kitchen_smartkitchen_popwindow_DialogUtils_onScanLinstener;
    }

    public void repastNumber(View view, final OnClickListener onClickListener, final OnClickListener onClickListener2) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_repastnumber, null, false);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_distory_confirm);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_distory_cancel);
        final EditText editText = (EditText) inflate.findViewById(R.id.et_distory_num);
        editText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                String obj = editable.toString();
                int length = editable.toString().length();
                if (length == 1 && obj.equals("0")) {
                    editable.clear();
                }
                if (length == 2 && Integer.valueOf(obj).intValue() < 10) {
                    editable.delete(0, 1);
                }
                if (editable.length() > 4) {
                    editable.delete(4, editText.getSelectionEnd());
                }
            }
        });
        final PopupWindow popupWindow = new PopupWindow(inflate, -2, -2, true);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(null);
                }
                if (DialogUtils.this.distoryPopu != null) {
                    DialogUtils.this.distoryPopu.sendMessage(null, editText.getText().toString().trim());
                }
                popupWindow.dismiss();
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (onClickListener2 != null) {
                    onClickListener2.onClick(null);
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        popupWindow.setSoftInputMode(1);
        popupWindow.setSoftInputMode(16);
        popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void gatheringMoney(View view, final OnClickListener onClickListener) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_gathering_money, null, false);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_first_button);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_second_button);
        final PopupWindow popupWindow = new PopupWindow(inflate, -1, -1, true);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popupWindow.dismiss();
                if (onClickListener != null) {
                    onClickListener.onClick(null);
                }
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        popupWindow.showAtLocation(view, 17, 0, 0);
    }

    public void showArea(View view, final List<TableArea> list) {
        View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_showarea, null, false);
        TextView textView = (TextView) inflate.findViewById(R.id.tv_distory_confirm);
        TextView textView2 = (TextView) inflate.findViewById(R.id.tv_distory_cancel);
        final LinearLayout linearLayout = (LinearLayout) inflate.findViewById(R.id.ll_favorable);
        ListView listView = (ListView) inflate.findViewById(R.id.lv_favorable_select);
        final TextView textView3 = (TextView) inflate.findViewById(R.id.tv_favorable_show);
        ((LinearLayout) inflate.findViewById(R.id.ll_favorable_select)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (linearLayout.getVisibility() == 4) {
                    linearLayout.setVisibility(0);
                } else {
                    linearLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
        listView.setAdapter(new SelectAreaAdapter(this.context, list));
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                textView3.setText(((TableArea) list.get(i)).getArea_name());
                textView3.setTextColor(a.a().getColor(R.color.black));
                linearLayout.setVisibility(View.INVISIBLE);
                SPUtils.setUserinfo(DialogUtils.this.context, SPUtils.SETTING_AREA, ((TableArea) list.get(i)).getId() + "");
            }
        });
        final PopupWindow popupWindow = new PopupWindow(inflate, -1, -1, true);
        textView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!TextUtils.isEmpty(textView3.getText().toString().trim())) {
                    popupWindow.dismiss();
                }
            }
        });
        textView2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        backgroundAlpha(this.context, 0.5f);
        popupWindow.setOnDismissListener(new OnDismissListener() {
            public void onDismiss() {
                DialogUtils.this.backgroundAlpha(DialogUtils.this.context, 1.0f);
            }
        });
        popupWindow.showAtLocation(view, 17, 0, 0);
    }
}
