package com.smart.kitchen.smartkitchen.print.interfaces;

import android.content.DialogInterface;

public interface CustomDialogInterface {

    public interface NoticeDialogListener {
        void onDialogNegativeClick(DialogInterface dialogInterface);

        void onDialogPositiveClick(DialogInterface dialogInterface);
    }

    public interface onItemClickListener {
        void onItemClick(String str);
    }

    public interface onPositiveClickListener {
        void onDialogPositiveClick(String str);
    }
}
