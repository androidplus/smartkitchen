package com.smart.kitchen.smartkitchen.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;

public abstract class ABaseFragmentPager<T> extends FragmentPagerAdapter {
    private List<T> data;

    public abstract Fragment getItem(int i, T t);

    public ABaseFragmentPager(List<T> list, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.data = list;
    }

    public int getCount() {
        if (this.data != null) {
            return this.data.size();
        }
        return 0;
    }

    public Fragment getItem(int i) {
        return getItem(i, this.data.get(i));
    }

    public int getItemPosition(Object obj) {
        return -2;
    }

    public List<T> getData() {
        return this.data;
    }
}
