package driver;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.net.Uri;
import android.support.v4.media.TransportMediator;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import com.alibaba.fastjson.asm.Opcodes;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpStatus;

public class BitmapConvertUtil {
    private static int[][] Floyd16x16 = new int[][]{new int[]{0, 128, 32, Opcodes.IF_ICMPNE, 8, 136, 40, Opcodes.JSR, 2, TransportMediator.KEYCODE_MEDIA_RECORD, 34, Opcodes.IF_ICMPGE, 10, 138, 42, 170}, new int[]{Opcodes.CHECKCAST, 64, 224, 96, HttpStatus.SC_OK, 72, 232, 104, 194, 66, 226, 98, HttpStatus.SC_ACCEPTED, 74, 234, 106}, new int[]{48, Opcodes.ARETURN, 16, 144, 56, Opcodes.INVOKESTATIC, 24, Opcodes.DCMPG, 50, Opcodes.GETSTATIC, 18, Opcodes.I2C, 58, 186, 26, Opcodes.IFNE}, new int[]{240, 112, 208, 80, 248, 120, 216, 88, 242, 114, 210, 82, 250, 122, 218, 90}, new int[]{12, 140, 44, Opcodes.IRETURN, 4, Opcodes.IINC, 36, Opcodes.IF_ICMPLE, 14, 142, 46, 174, 6, 134, 38, Opcodes.IF_ACMPNE}, new int[]{HttpStatus.SC_NO_CONTENT, 76, 236, 108, 196, 68, 228, 100, HttpStatus.SC_PARTIAL_CONTENT, 78, 238, 110, Opcodes.IFNULL, 70, 230, 102}, new int[]{60, 188, 28, Opcodes.IFGE, 52, Opcodes.GETFIELD, 20, Opcodes.LCMP, 62, 190, 30, Opcodes.IFLE, 54, Opcodes.INVOKEVIRTUAL, 22, Opcodes.FCMPG}, new int[]{252, 124, 220, 92, 244, 116, 212, 84, 254, 126, 222, 94, 246, 118, 214, 86}, new int[]{3, 131, 35, Opcodes.IF_ICMPGT, 11, 139, 43, 171, 1, 129, 33, Opcodes.IF_ICMPLT, 9, 137, 41, Opcodes.RET}, new int[]{195, 67, 227, 99, HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION, 75, 235, 107, Opcodes.INSTANCEOF, 65, 225, 97, HttpStatus.SC_CREATED, 73, 233, 105}, new int[]{51, Opcodes.PUTSTATIC, 19, Opcodes.I2S, 59, Opcodes.NEW, 27, Opcodes.IFLT, 49, Opcodes.RETURN, 17, Opcodes.I2B, 57, Opcodes.INVOKEINTERFACE, 25, Opcodes.IFEQ}, new int[]{243, 115, 211, 83, 251, 123, 219, 91, 241, 113, 209, 81, 249, 121, 217, 89}, new int[]{15, 143, 47, 175, 7, 135, 39, Opcodes.GOTO, 13, 141, 45, 173, 5, 133, 37, Opcodes.IF_ACMPEQ}, new int[]{HttpStatus.SC_MULTI_STATUS, 79, 239, 111, Opcodes.IFNONNULL, 71, 231, 103, HttpStatus.SC_RESET_CONTENT, 77, 237, 109, 197, 69, 229, 101}, new int[]{63, 191, 31, Opcodes.IF_ICMPEQ, 55, Opcodes.INVOKESPECIAL, 23, Opcodes.DCMPL, 61, 189, 29, Opcodes.IFGT, 53, Opcodes.PUTFIELD, 21, Opcodes.FCMPL}, new int[]{254, TransportMediator.KEYCODE_MEDIA_PAUSE, 223, 95, 247, 119, 215, 87, 253, 125, 221, 93, 245, 117, 213, 85}};
    private static final String TAG = "BitmapConvertUtil";

    private BitmapConvertUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static Bitmap convertToBlackWhite(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int i2 = 0; i2 < width; i2++) {
                int i3 = iArr[(width * i) + i2];
                i3 = (int) (((((double) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i3) >> 8)) * 0.59d) + (((double) ((16711680 & i3) >> 16)) * 0.3d)) + (((double) (i3 & 255)) * 0.11d));
                iArr[(width * i) + i2] = i3 | (((i3 << 16) | ViewCompat.MEASURED_STATE_MASK) | (i3 << 8));
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return createBitmap;
    }

    public static Bitmap convert2GreyImg(Bitmap bitmap) {
        int i;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        byte[] bArr = new byte[(width * height)];
        for (i = 0; i < iArr.length; i++) {
            bArr[i] = (byte) iArr[i];
        }
        for (int i2 = 0; i2 < height; i2++) {
            for (i = 0; i < width; i++) {
                int i3 = iArr[(width * i2) + i];
                i3 = (int) (((((double) ((float) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i3) >> 8))) * 0.59d) + (((double) ((float) ((16711680 & i3) >> 16))) * 0.3d)) + (((double) ((float) (i3 & 255))) * 0.11d));
                iArr[(width * i2) + i] = (i3 | (((i3 << 16) | ViewCompat.MEASURED_STATE_MASK) | (i3 << 8))) ^ -1;
            }
        }
        for (i = 0; i < iArr.length; i++) {
            bArr[i] = (byte) iArr[i];
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.RGB_565);
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return createBitmap;
    }

    public static Bitmap bitmap2Gray(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public static byte[] getNVLogoByte(Bitmap bitmap) {
        int i;
        int i2;
        int i3;
        int i4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.e("logolist============", " " + width + " " + height);
        int[] iArr = new int[(width * height)];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        for (i = 0; i < height; i++) {
            for (i2 = 0; i2 < width; i2++) {
                i3 = iArr[(width * i) + i2];
                i4 = (width * i) + i2;
                iArr[i4] = (int) (((((double) ((float) ((MotionEventCompat.ACTION_POINTER_INDEX_MASK & i3) >> 8))) * 0.59d) + (((double) ((float) ((16711680 & i3) >> 16))) * 0.3d)) + (((double) ((float) (i3 & 255))) * 0.11d));
            }
        }
        for (i = 0; i < height; i++) {
            for (i2 = 0; i2 < width; i2++) {
                if ((iArr[(i * width) + i2] & 255) > Floyd16x16[i2 & 15][i & 15]) {
                    iArr[(i * width) + i2] = 0;
                } else {
                    iArr[(i * width) + i2] = 1;
                }
            }
        }
        i4 = ((height + 7) / 8) * 8;
        height = ((width + 7) / 8) * 8;
        byte[] bArr = new byte[((i4 * height) / 8)];
        byte[] bArr2 = new byte[]{Byte.MIN_VALUE, (byte) 64, (byte) 32, (byte) 16, (byte) 8, (byte) 4, (byte) 2, (byte) 1};
        i3 = 0;
        while (i3 < height) {
            i = 0;
            while (i < i4) {
                try {
                    int width2;
                    if (i + 8 < bitmap.getHeight()) {
                        i2 = 0;
                        while (i2 < 8) {
                            width2 = ((i + i2) * bitmap.getWidth()) + i3;
                            int i5 = ((i3 * i4) / 8) + (i / 8);
                            if (i3 < width && i + i2 < bitmap.getHeight() && iArr[width2] > 0) {
                                bArr[i5] = (byte) (bArr[i5] | bArr2[i2]);
                            }
                            i2++;
                        }
                    } else {
                        i2 = 0;
                        while (i2 < 8 - (i4 - bitmap.getHeight())) {
                            if (i3 < width && i + i2 < bitmap.getHeight() && iArr[((i + i2) * bitmap.getWidth()) + i3] > 0) {
                                width2 = ((i3 * i4) / 8) + (i / 8);
                                bArr[width2] = (byte) (bArr[width2] | bArr2[i2]);
                            }
                            i2++;
                        }
                    }
                    i += 8;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            i3++;
        }
        Log.e("getNVLogoByte finish", "");
        return bArr;
    }

    public static byte[] convert(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        int i = ((width - 1) / 8) + 1;
        byte[] bArr = new byte[(i * height)];
        for (int i2 = 0; i2 < height; i2++) {
            for (int i3 = 0; i3 < width; i3++) {
                int i4 = (i * i2) + (i3 / 8);
                int i5 = 7 - (i3 % 8);
                if ((iArr[(width * i2) + i3] & 255) < Floyd16x16[i2 & 15][i3 & 15]) {
                    bArr[i4] = (byte) (bArr[i4] | (1 << i5));
                }
            }
        }
        return bArr;
    }

    public static byte[] convert2(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        byte b = (byte) (255 >> (width % 8));
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        int i = ((width - 1) / 8) + 1;
        byte[] bArr = new byte[(i * height)];
        for (int i2 = 0; i2 < height; i2++) {
            int i3;
            for (i3 = 0; i3 < width; i3++) {
                int i4 = (i * i2) + (i3 / 8);
                int i5 = 7 - (i3 % 8);
                if ((iArr[(width * i2) + i3] & 255) > Floyd16x16[i2 & 15][i3 & 15]) {
                    bArr[i4] = (byte) (bArr[i4] | (1 << i5));
                }
            }
            i3 = ((i2 + 1) * i) - 1;
            bArr[i3] = (byte) (bArr[i3] | b);
        }
        return bArr;
    }

    public static int calculateOutsideInSampleSize(Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = 1;
        if (i3 > i2 || i4 > i) {
            i3 /= 2;
            i4 /= 2;
            while (i3 / i5 > i2 && i4 / i5 > i) {
                i5 *= 2;
            }
        }
        return i5;
    }

    public static int calculateOutsideInSampleSize(Bitmap bitmap, int i, int i2) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int i3 = 1;
        if (height > i2 || width > i) {
            height /= 2;
            width /= 2;
            while (height / i3 > i2 && width / i3 > i) {
                i3 *= 2;
            }
        }
        return i3;
    }

    public static Bitmap returnBitmap(String str) {
        File file;
        Bitmap decodeStream;
        IOException e;
        Exception e2;
        try {
            file = new File(str);
        } catch (Exception e3) {
            e3.printStackTrace();
            file = null;
        }
        try {
            InputStream fileInputStream = new FileInputStream(file);
            decodeStream = BitmapFactory.decodeStream(fileInputStream);
            try {
                fileInputStream.close();
            } catch (IOException e4) {
                e = e4;
                e.printStackTrace();
                return decodeStream;
            } catch (Exception e5) {
                e2 = e5;
                e2.printStackTrace();
                return decodeStream;
            }
        } catch (IOException e6) {
            IOException iOException = e6;
            decodeStream = null;
            e = iOException;
            e.printStackTrace();
            return decodeStream;
        } catch (Exception e32) {
            Exception exception = e32;
            decodeStream = null;
            e2 = exception;
            e2.printStackTrace();
            return decodeStream;
        }
        return decodeStream;
    }

    public static int calculateInsideInSampleSize(Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        if (i4 > i && i4 / i > i3 / i2) {
            return i4 / i;
        }
        if (i3 > i2) {
            return i3 / i2;
        }
        return 1;
    }

    public static int calculateInsideInSampleSize(Bitmap bitmap, int i, int i2) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        if (width > i && width / i > height / i2) {
            return width / i;
        }
        if (height > i2) {
            return height / i2;
        }
        return 1;
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream inputStream, int i, int i2) {
        InputStream bufferedInputStream = new BufferedInputStream(inputStream);
        bufferedInputStream.mark(0);
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(bufferedInputStream, null, options);
        options.inSampleSize = calculateInsideInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        try {
            bufferedInputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(bufferedInputStream, null, options);
    }

    public static Bitmap decodeSampledBitmapFromUri(Context context, Uri uri, int i, int i2) {
        String filePath = getFilePath(context, uri);
        if (filePath == null) {
            return null;
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInsideInSampleSize(options, i, i2);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap, int i) {
        float width;
        if (bitmap.getWidth() > i) {
            width = ((float) i) / ((float) bitmap.getWidth());
        } else {
            width = 1.0f;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(width, width);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap, int i, int i2) {
        float width;
        if (bitmap.getWidth() > i || bitmap.getHeight() > i2) {
            width = ((float) i) / ((float) bitmap.getWidth());
            float height = ((float) i2) / ((float) bitmap.getHeight());
            if (width >= height) {
                width = height;
            }
        } else {
            width = 1.0f;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(width, width);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources resources, int i, int i2, int i3) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, i, options);
        options.inSampleSize = calculateInsideInSampleSize(options, i2, i3);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, i, options);
    }

    public static byte[] encodeBitmapToPixelsByteArray(Bitmap bitmap) {
        int i = 0;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        byte[] bArr = new byte[(width * height)];
        while (i < bArr.length) {
            bArr[i] = (byte) iArr[i];
            i++;
        }
        return bArr;
    }

    public static Bitmap scale(Bitmap bitmap, float f, float f2) {
        Matrix matrix = new Matrix();
        matrix.postScale(f, f2);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap scaleToRequiredWidth(Bitmap bitmap, int i) {
        Matrix matrix = new Matrix();
        matrix.postScale((((float) i) * 1.0f) / ((float) bitmap.getWidth()), (((float) i) * 1.0f) / ((float) bitmap.getWidth()));
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap createBitmapFromPicture(Picture picture) {
        Bitmap createBitmap = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Config.ARGB_8888);
        picture.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    private static String getFilePath(Context context, Uri uri) {
        String str = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Cursor query = context.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            if (query == null) {
                return null;
            }
            try {
                if (query.moveToNext()) {
                    str = query.getString(query.getColumnIndex("_data"));
                }
                query.close();
            } catch (Throwable th) {
                query.close();
            }
        }
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return str;
    }
}
