package com.smart.kitchen.smartkitchen.service;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.smart.kitchen.smartkitchen.entitys.Messages;
import com.smart.kitchen.smartkitchen.utils.AppUtils;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.JsonUtils;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.SDCardUtils;
import com.smart.kitchen.smartkitchen.utils.Toasts;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.Header;

public class UpdateAppService extends Service {
    public static boolean IS_OPEN = false;
    private static final String TAG = "UpdateAppService";
    String apkname = "";
    AsyncHttpClient client = new AsyncHttpClient();
    BinaryHttpResponseHandler downloadNewVersionHandler = new BinaryHttpResponseHandler(this.mAllowedContentTypes) {
        public void onSuccess(int i, Header[] headerArr, byte[] bArr) {
            LogUtils.e(UpdateAppService.TAG, " statusCode=========" + i);
            LogUtils.e(UpdateAppService.TAG, " statusCode=========" + headerArr);
            LogUtils.e(UpdateAppService.TAG, " statusCode====binaryData len=====" + bArr.length);
            LogUtils.e(UpdateAppService.TAG, "onSuccess: ");
            if (bArr != null) {
                try {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    String str = file.getAbsolutePath() + "/" + UpdateAppService.this.apkname;
                    File file2 = new File(str);
                    if (!file2.exists()) {
                        file2.createNewFile();
                    }
                    OutputStream fileOutputStream = new FileOutputStream(file2);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                    bufferedOutputStream.write(bArr);
                    bufferedOutputStream.flush();
                    fileOutputStream.flush();
                    if (bufferedOutputStream != null) {
                        bufferedOutputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    UpdateAppService.this.updateVersion(str);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        }

        public void onFailure(int i, Header[] headerArr, byte[] bArr, Throwable th) {
            LogUtils.e(UpdateAppService.TAG, "onFailure: 下载" + th.toString());
            for (Header header : headerArr) {
                LogUtils.e(UpdateAppService.TAG, header.getName() + " / " + header.getValue());
            }
            UpdateAppService.this.client.cancelRequests(UpdateAppService.this.getApplicationContext(), true);
            UpdateAppService.this.stopSelf();
        }

        public void onStart() {
            super.onStart();
            LogUtils.e(UpdateAppService.TAG, "onStart: ");
        }

        public void onProgress(long j, long j2) {
            super.onProgress(j, j2);
            LogUtils.e(UpdateAppService.TAG, j + "onProgress: " + j2 + "," + ((int) ((100 * j) / j2)) + "%");
        }
    };
    private String[] mAllowedContentTypes = new String[]{"application/vnd.android.package-archive", "application/octet-stream", "image/jpeg", "image/png", "image/gif"};

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        IS_OPEN = true;
        super.onCreate();
        LogUtils.e(TAG, "onCreate: ");
        if (SDCardUtils.isSDCardMounted()) {
            LogUtils.e(TAG, "onCreate: request");
            RequestParams requestParams = new RequestParams();
            requestParams.put("version", AppUtils.getVersionCode(this));
            this.client.post(this, Contants.VERSION_UPDARE, requestParams, new TextHttpResponseHandler() {
                public void onFailure(int i, Header[] headerArr, String str, Throwable th) {
                    LogUtils.e(UpdateAppService.TAG, "onFailure: ");
                    UpdateAppService.this.stopSelf();
                }

                public void onSuccess(int i, Header[] headerArr, String str) {
                    String substring = str.substring(str.indexOf("{"));
                    LogUtils.e(UpdateAppService.TAG, "onSuccess: " + substring);
                    Messages messages = JsonUtils.getMessages(substring);
                    if (messages.getFlag() == 0) {
                        UpdateAppService.this.startDownload(UpdateAppService.this, (String) messages.getData());
                    } else {
                        UpdateAppService.this.stopSelf();
                    }
                }
            });
            return;
        }
        LogUtils.e(TAG, "onCreate: SD卡不可用");
        Toasts.show((Context) this, (CharSequence) "SD卡不可用", 1000);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return super.onStartCommand(intent, i, i2);
    }

    public void downloadNewVersion(String str) {
        LogUtils.e(TAG, "downloadNewVersion: ");
        this.client.get(str, this.downloadNewVersionHandler);
        this.apkname = str.substring(str.lastIndexOf("/") + 1);
    }

    public void updateVersion(String str) {
        this.client.cancelRequests((Context) this, true);
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(new File(str)), "application/vnd.android.package-archive");
        startActivity(intent);
        stopSelf();
    }

    public void startDownload(Context context, final String str) {
        LogUtils.e(TAG, "startDownload: " + str);
        AlertDialog create = new Builder(context).setMessage("有新版本，是否更新？").setCancelable(false).setPositiveButton("更新", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test");
                if (!file.exists()) {
                    file.mkdirs();
                }
                File file2 = new File(file.getAbsolutePath() + "/test.apk");
                if (!file2.exists()) {
                    file2.delete();
                }
                UpdateAppService.this.downloadNewVersion(str);
            }
        }).setNegativeButton("暂不更新", new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                UpdateAppService.this.stopSelf();
                dialogInterface.dismiss();
            }
        }).create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        create.getWindow().setType(2003);
        create.show();
    }

    public void onDestroy() {
        super.onDestroy();
        IS_OPEN = false;
    }
}
