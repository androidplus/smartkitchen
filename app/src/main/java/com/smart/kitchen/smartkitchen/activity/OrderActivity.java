package com.smart.kitchen.smartkitchen.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import com.smart.kitchen.smartkitchen.BaseFragmentActivity;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.fragments.OrderLeftFragment;
import com.smart.kitchen.smartkitchen.fragments.OrderLeftFragment.OnDataTransmissionListener;
import com.smart.kitchen.smartkitchen.fragments.OrderRightFragment1;
import com.smart.kitchen.smartkitchen.fragments.OrderRightFragment1.OnListenerOnChange;
import com.smart.kitchen.smartkitchen.fragments.OrderRightFragment2;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;

public class OrderActivity extends BaseFragmentActivity {
    private static final String TAG = "OrderActivity";
    private OrderLeftFragment orderLeftFragment;
    private OrderRightFragment1 orderRightFragment1;
    private OrderRightFragment2 orderRightFragment2;
    private String temp = "";

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_order);
        FinishActivity.add(this);
    }

    protected void onResume() {
        super.onResume();
        onLeftChange();
    }

    public void onLeftChange() {
        this.orderLeftFragment.change();
    }

    protected void initView() {
        showLeft();
        showRight1();
    }

    protected void initEvent() {
        this.orderLeftFragment.setOnDataTransmissionListener(new OnDataTransmissionListener() {
            public void dataTransmission(String str, int i) {
                if ("notify".equals(str)) {
                    if (i == 0) {
                        OrderActivity.this.orderRightFragment1.notifyFragment();
                    } else {
                        OrderActivity.this.orderRightFragment2.notifyFragment();
                    }
                } else if (i == 0) {
                    OrderActivity.this.orderRightFragment1.setData(str);
                } else if (OrderActivity.this.orderRightFragment2 != null) {
                    OrderActivity.this.orderRightFragment2.setData(str);
                } else {
                    OrderActivity.this.temp = str;
                }
            }
        });
    }

    protected void initData() {
    }

    public void showLeft() {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        if (this.orderLeftFragment == null) {
            this.orderLeftFragment = new OrderLeftFragment();
            beginTransaction.add((int) R.id.left_frag, this.orderLeftFragment);
        } else {
            beginTransaction.show(this.orderLeftFragment);
        }
        beginTransaction.commit();
    }

    public void showRight1() {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(beginTransaction);
        if (this.orderRightFragment1 == null) {
            this.orderRightFragment1 = new OrderRightFragment1();
            beginTransaction.add((int) R.id.right_frag, this.orderRightFragment1);
            this.orderRightFragment1.onListenerOnChange(new OnListenerOnChange() {
                public void onChange(String str) {
                    if ("onChange".equals(str)) {
                        OrderActivity.this.onLeftChange();
                    }
                }
            });
        } else {
            beginTransaction.show(this.orderRightFragment1);
        }
        beginTransaction.commit();
    }

    public void showRight2() {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(beginTransaction);
        if (this.orderRightFragment2 == null) {
            this.orderRightFragment2 = new OrderRightFragment2();
            beginTransaction.add((int) R.id.right_frag, this.orderRightFragment2);
            this.orderRightFragment2.onListenerOnChange(new OrderRightFragment2.OnListenerOnChange() {
                public void onChange(String str) {
                    if ("onChange".equals(str)) {
                        OrderActivity.this.onLeftChange();
                    }
                }
            });
        } else {
            beginTransaction.show(this.orderRightFragment2);
        }
        beginTransaction.commit();
        if (!TextUtils.isEmpty(this.temp)) {
            this.orderRightFragment2.setData(this.temp);
            this.temp = "";
        }
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (this.orderRightFragment2 != null) {
            fragmentTransaction.hide(this.orderRightFragment2);
        }
        if (this.orderRightFragment1 != null) {
            fragmentTransaction.hide(this.orderRightFragment1);
        }
    }
}
