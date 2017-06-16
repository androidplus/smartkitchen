package com.smart.kitchen.smartkitchen.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import com.smart.kitchen.smartkitchen.R;

public class Permission {
    public static final String[] CAMERA_ARR = new String[]{"android.permission.CAMERA"};
    public static final int CAMERA_CODE = 3;
    public static final String[] LOCATION_ARR = new String[]{"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
    public static final int LOCATION_CODE = 2;
    private static final String PACKAGE_URL_SCHEME = "package:";
    public static final String[] SD_ARR = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
    public static final int SD_CODE = 1;
    private static final String TAG = "Permission";
    public static final String[] WIFI_ARR = new String[]{"android.permission.ACCESS_WIFI_STATE", "android.permission.CHANGE_WIFI_STATE", "android.permission.CHANGE_WIFI_MULTICAST_STATE"};
    public static final int WIFI_ARR_CODE = 2;
    private static AlertDialog alertDialog;

    public interface IRoot {
        void haveRoot(int i);
    }

    public interface DialogClickListener {
        void isQuit();
    }

    public static void isHaveRootActivity(Activity activity, String[] strArr, int i, IRoot iRoot) {
        LogUtils.e(TAG, "isHaveRootActivity: ");
        if (lacksPermissions(activity, strArr)) {
            ActivityCompat.requestPermissions(activity, strArr, i);
        } else {
            iRoot.haveRoot(i);
        }
    }

    public static void isHaveRootFragment(Fragment fragment, String[] strArr, int i, IRoot iRoot) {
        LogUtils.e(TAG, "isHaveRootFragment: ");
        if (lacksPermissions(fragment.getContext(), strArr)) {
            fragment.requestPermissions(strArr, i);
        } else {
            iRoot.haveRoot(i);
        }
    }

    private static boolean lacksPermissions(Context context, String... strArr) {
        for (String lacksPermission : strArr) {
            if (lacksPermission(context, lacksPermission)) {
                return true;
            }
        }
        return false;
    }

    private static boolean lacksPermission(Context context, String str) {
        return ContextCompat.checkSelfPermission(context, str) == -1;
    }

    public static void showMissingPermissionDialog(final Context context) {
        if (alertDialog != null) {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
            alertDialog = null;
        }
        if (alertDialog == null) {
            AlertDialog.Builder aVar = new AlertDialog.Builder(context);
            aVar.setTitle( R.string.help);
            aVar.setMessage(R.string.string_help_text);
            aVar.setNegativeButton(R.string.cancel, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Permission.alertDialog.dismiss();
                    Permission.alertDialog = null;
                }
            });
            aVar.setPositiveButton((int) R.string.setting, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    Permission.alertDialog.dismiss();
                    Permission.alertDialog = null;
                    Permission.startAppSettings(context);
                }
            });
            alertDialog = aVar.create();
        }
    }

    private static void startAppSettings(Context context) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + context.getPackageName()));
        context.startActivity(intent);
    }
}
