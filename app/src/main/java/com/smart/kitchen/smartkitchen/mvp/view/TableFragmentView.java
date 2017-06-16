package com.smart.kitchen.smartkitchen.mvp.view;

import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import java.util.List;

public interface TableFragmentView {
    void ShowTableNumber(List<TableNumber> list);

    long getTableAreaId();

    void onFail();

    void onSuccess(List<TableNumber> list);
}
