package com.smart.kitchen.smartkitchen.popwindow;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.utils.BitmapUtils;
import org.apache.http.HttpStatus;

public class PayDialogUtils {
    private Activity context;
    private ImageView iv_qr;
    private PopupWindow popWindow;

    public PayDialogUtils(Activity activity) {
        this.context = activity;
    }

    public void showQR(View view, String str) {
        Bitmap createQRCoderBitmap = BitmapUtils.createQRCoderBitmap(str, HttpStatus.SC_INTERNAL_SERVER_ERROR, HttpStatus.SC_INTERNAL_SERVER_ERROR);
        if (this.popWindow == null) {
            View inflate = LayoutInflater.from(this.context).inflate(R.layout.dialog_qr, null, false);
            this.iv_qr = (ImageView) inflate.findViewById(R.id.iv_qr);
            this.popWindow = new PopupWindow(inflate, -2, -2, true);
            this.popWindow.setTouchInterceptor(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            this.popWindow.setBackgroundDrawable(new ColorDrawable(0));
            backgroundAlpha(this.context, 0.5f);
            this.popWindow.setOnDismissListener(new OnDismissListener() {
                public void onDismiss() {
                    PayDialogUtils.this.backgroundAlpha(PayDialogUtils.this.context, 1.0f);
                }
            });
        }
        this.iv_qr.setImageBitmap(createQRCoderBitmap);
        if (!this.popWindow.isShowing()) {
            this.popWindow.showAtLocation(view, 17, 0, 0);
        }
    }

    public void dismiss() {
        if (this.popWindow != null) {
            this.popWindow.dismiss();
        }
    }

    public void backgroundAlpha(Activity activity, float f) {
        LayoutParams attributes = activity.getWindow().getAttributes();
        attributes.alpha = f;
        activity.getWindow().addFlags(2);
        activity.getWindow().setAttributes(attributes);
    }
}
