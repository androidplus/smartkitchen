package com.smart.kitchen.smartkitchen.mvp.view;

import com.smart.kitchen.smartkitchen.entitys.TableArea;
import java.util.List;

public interface TableView {
    void ShowTableArea(List<TableArea> list);

    void isSubmitIndent(String str);

    void onFail();

    void onSuccess(List<TableArea> list);
}
