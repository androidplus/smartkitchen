package com.smart.kitchen.smartkitchen.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SDCardUtils {
    public static boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String getSDCardRootDir() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static long getSDCardTotalSize() {
        if (!isSDCardMounted()) {
            return 0;
        }
        StatFs statFs = new StatFs(getSDCardRootDir());
        return (long) (((statFs.getBlockCount() * statFs.getBlockSize()) / 1024) / 1024);
    }

    public static long getSDCardAvailableSize() {
        if (!isSDCardMounted()) {
            return 0;
        }
        StatFs statFs = new StatFs(getSDCardRootDir());
        return (long) (((statFs.getBlockSize() * statFs.getAvailableBlocks()) / 1024) / 1024);
    }

    public static boolean saveFileToPublicDirectory(byte[] bArr, String str, String str2) {
    return false;
    }

    public static boolean saveFileToExternalFileDir(Context context, byte[] bArr, String str, String str2) {
        return false;
    }

    public static boolean saveFileToExternalCacheDir(Context context, byte[] bArr, String str) {
        return false;
    }

    public static byte[] loadDataFromSDCard(String str) {
        return null;
    }

    public static String getSDCardCacheDir(Context context) {
        return context.getExternalCacheDir().getPath();
    }
}
