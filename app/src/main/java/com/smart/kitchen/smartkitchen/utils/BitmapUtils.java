package com.smart.kitchen.smartkitchen.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.util.Base64;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class BitmapUtils {
    public static Bitmap getImageThumbnail(String str, int i, int i2) {
        int i3 = 1;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inJustDecodeBounds = false;
        int i4 = options.outWidth / i;
        int i5 = options.outHeight / i2;
        if (i4 >= i5) {
            i4 = i5;
        }
        if (i4 > 0) {
            i3 = i4;
        }
        options.inSampleSize = i3;
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(str, options), i, i2, 2);
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        IOException e;
        Throwable th;
        String str = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        if (bitmap != null) {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    bitmap.compress(CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byteArrayOutputStream.flush();
                    byteArrayOutputStream.close();
                    str = "data:image/jpeg;base64," + Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
                } catch (IOException e2) {
                    e = e2;
                    try {
                        e.printStackTrace();
                        if (byteArrayOutputStream != null) {
                            try {
                                byteArrayOutputStream.flush();
                                byteArrayOutputStream.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        return str;
                    } catch (Throwable th2) {
                        th = th2;
                        if (byteArrayOutputStream != null) {
                            try {
                                byteArrayOutputStream.flush();
                                byteArrayOutputStream.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                            }
                        }
                        throw th;
                    }
                }
            } catch (IOException e4) {

                return str;
            } catch (Throwable th3) {

            }
        }
        Object obj = str;
        if (byteArrayOutputStream != null) {
            try {
                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();
            } catch (IOException e322) {
                e322.printStackTrace();
            }

        }
        return str;
    }

    public static Bitmap base64ToBitmap(String str) {
        byte[] decode = Base64.decode(str, 0);
        return BitmapFactory.decodeByteArray(decode, 0, decode.length);
    }

    public static Bitmap getBitmap(Uri uri, Activity activity) {
        try {
            return BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createQRCoderBitmap(String str, int i, int i2) {
        if (str != null) {
            try {
                if (!str.isEmpty()) {
                    Map hashtable = new Hashtable();
                    hashtable.put(EncodeHintType.CHARACTER_SET, "utf-8");
                    BitMatrix encode = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, i, i2, hashtable);
                    int[] iArr = new int[(i * i2)];
                    for (int i3 = 0; i3 < i2; i3++) {
                        for (int i4 = 0; i4 < i; i4++) {
                            if (encode.get(i4, i3)) {
                                iArr[(i3 * i) + i4] = ViewCompat.MEASURED_STATE_MASK;
                            } else {
                                iArr[(i3 * i) + i4] = -1;
                            }
                        }
                    }
                    Bitmap createBitmap = Bitmap.createBitmap(i, i2, Config.ARGB_8888);
                    createBitmap.setPixels(iArr, 0, i, 0, 0, i, i2);
                    return createBitmap;
                }
            } catch (WriterException e) {
                LogUtils.i("qrCode", "生成二维码错误" + e.getMessage());
                return null;
            }
        }
        return null;
    }
}
