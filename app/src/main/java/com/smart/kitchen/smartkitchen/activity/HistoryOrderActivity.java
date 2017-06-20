package com.smart.kitchen.smartkitchen.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.smart.kitchen.smartkitchen.BaseFragmentActivity;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.fragments.HistoryOrderLeftFragment;
import com.smart.kitchen.smartkitchen.fragments.HistoryOrderLeftFragment.OnDataTransmissionListener;
import com.smart.kitchen.smartkitchen.fragments.HistoryOrderRightFragment1;
import com.smart.kitchen.smartkitchen.fragments.HistoryOrderRightFragment1.OnChangeListener;
import com.smart.kitchen.smartkitchen.fragments.HistoryOrderRightFragment2;
import com.smart.kitchen.smartkitchen.fragments.HistoryOrderRightFragment2.OnListenerOnChange;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;

public class HistoryOrderActivity extends BaseFragmentActivity {
    private HistoryOrderLeftFragment historyOrderLeftFragment;
    private HistoryOrderRightFragment1 historyOrderRightFragment1;
    private HistoryOrderRightFragment2 historyOrderRightFragment2;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_order);
        FinishActivity.add(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLeftChange();
    }

    public void onLeftChange() {
        this.historyOrderLeftFragment.change();
    }

    @Override
    protected void initView() {
        showLeft();
        showRight1();
    }

    @Override
    protected void initEvent() {
        this.historyOrderLeftFragment.setOnDataTransmissionListener(new OnDataTransmissionListener() {
            public void dataTransmission(String str, int i) {
                if (i == 0) {
                    HistoryOrderActivity.this.historyOrderRightFragment1.setData(str);
                } else if (HistoryOrderActivity.this.historyOrderRightFragment2 != null) {
                    HistoryOrderActivity.this.historyOrderRightFragment2.setData(str);
                }
            }
        });
    }

    @Override
    protected void initData() {
    }

    public void showLeft() {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        if (this.historyOrderLeftFragment == null) {
            this.historyOrderLeftFragment = new HistoryOrderLeftFragment();
            beginTransaction.add((int) R.id.left_frag, this.historyOrderLeftFragment);
        } else {
            beginTransaction.show(this.historyOrderLeftFragment);
        }
        beginTransaction.commit();
    }

    public void showRight1() {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(beginTransaction);
        if (this.historyOrderRightFragment1 == null) {
            this.historyOrderRightFragment1 = new HistoryOrderRightFragment1();
            beginTransaction.add((int) R.id.right_frag, this.historyOrderRightFragment1);
            this.historyOrderRightFragment1.onChangeListener(new OnChangeListener() {
                public void onChange(String str) {
                    if ("onChange".equals(str)) {
                        HistoryOrderActivity.this.onLeftChange();
                    }
                }
            });
        } else {
            beginTransaction.show(this.historyOrderRightFragment1);
        }
        beginTransaction.commit();
    }

    public void showRight2() {
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        hideFragment(beginTransaction);
        if (this.historyOrderRightFragment2 == null) {
            this.historyOrderRightFragment2 = new HistoryOrderRightFragment2();
            beginTransaction.add((int) R.id.right_frag, this.historyOrderRightFragment2);
            this.historyOrderRightFragment2.onListenerOnChange(new OnListenerOnChange() {
                public void onChange(String str) {
                    if ("onChange".equals(str)) {
                        HistoryOrderActivity.this.onLeftChange();
                    }
                }
            });
        } else {
            beginTransaction.show(this.historyOrderRightFragment2);
        }
        beginTransaction.commit();
    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (this.historyOrderRightFragment2 != null) {
            fragmentTransaction.hide(this.historyOrderRightFragment2);
        }
        if (this.historyOrderRightFragment1 != null) {
            fragmentTransaction.hide(this.historyOrderRightFragment1);
        }
    }
}
