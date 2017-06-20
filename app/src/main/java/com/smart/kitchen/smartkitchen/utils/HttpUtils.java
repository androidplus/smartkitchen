package com.smart.kitchen.smartkitchen.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.Messages;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import java.io.File;
import org.apache.http.Header;

public class HttpUtils {
    public static final String STATUS_NETWORK = "network_available";
    private static final String TAG = "HttpUtils";
    private static AsyncHttpClient client = new AsyncHttpClient();
    private static boolean isSuccess = false;

    static {
        client.setTimeout(11000);
    }

    public static void get(String url, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        client.get(url, asyncHttpResponseHandler);
    }

    public static void get(String url, RequestParams requestParams, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        client.get(url, requestParams, (ResponseHandlerInterface) asyncHttpResponseHandler);
    }

    public static void get(String url, JsonHttpResponseHandler jsonHttpResponseHandler) {
        client.get(url, jsonHttpResponseHandler);
    }

    public static void get(String url, RequestParams requestParams, JsonHttpResponseHandler jsonHttpResponseHandler) {
        client.get(url, requestParams, (ResponseHandlerInterface) jsonHttpResponseHandler);
    }

    public static void get(String url, BinaryHttpResponseHandler binaryHttpResponseHandler) {
        client.get(url, binaryHttpResponseHandler);
    }

    public static void post(Context context, String url, RequestParams requestParams, boolean z) {
        post(context, url, requestParams, null, z);
    }

    public static void post(Context context, String url, RequestParams requestParams) {
        post(context, url, requestParams, null, false);
    }

    public static void post(Context context, String url, RequestParams requestParams, OnResultListener onResultListener, boolean z) {
        post(context, url, requestParams, 0, onResultListener, null, z);
    }

    public static void post(Context context, String url, RequestParams requestParams, OnResultListener onResultListener) {
        post(context, url, requestParams, 0, onResultListener, null, false);
    }

    public static void post(Context context, String url, RequestParams requestParams, OnResultListener onResultListener, ProgressDialog progressDialog, boolean z) {
        post(context, url, requestParams, 0, onResultListener, progressDialog, z);
    }

    public static void post(Context context, String url, RequestParams requestParams, OnResultListener onResultListener, ProgressDialog progressDialog) {
        post(context, url, requestParams, 0, onResultListener, progressDialog, false);
    }

    public static void post(Context context, String url, RequestParams requestParams, int i, OnResultListener onResultListener, boolean z) {
        post(context, url, requestParams, i, onResultListener, null, z);
    }

    public static void post(Context context, String url, RequestParams requestParams, int i, OnResultListener onResultListener) {
        post(context, url, requestParams, i, onResultListener, null, false);
    }

    public static void post(Context context, String url, RequestParams requestParams, int i, OnResultListener onResultListener, ProgressDialog progressDialog, boolean z) {
        post(context, url, requestParams, i, onResultListener, progressDialog, null, z);
    }

    public static void post(Context context, String url, RequestParams requestParams, int i, OnResultListener onResultListener, ProgressDialog progressDialog) {
        post(context, url, requestParams, i, onResultListener, progressDialog, null, false);
    }

    public static void post(Context context, String url, RequestParams requestParams, int i, OnResultListener onResultListener, ProgressDialog progressDialog, View view, boolean z) {
        if (NetUtils.isConnected(context)) {
            LogUtils.e(TAG, "post: " + url + "?" + requestParams.toString());
            if (view != null) {
                view.setEnabled(false);
            }
            if (!(progressDialog == null || progressDialog.isShowing())) {
                progressDialog.show();
            }
            final View view2 = view;
            final ProgressDialog progressDialog2 = progressDialog;
            final boolean z2 = z;
            final Context context2 = context;
            final String str2 = url;
            final RequestParams requestParams2 = requestParams;
            final int i2 = i;
            final OnResultListener onResultListener2 = onResultListener;
            client.post(url, requestParams, new TextHttpResponseHandler() {

                @Override
                public void onFailure(int i, Header[] headerArr, String str, Throwable th) {
                    if (view2 != null) {
                        view2.setEnabled(true);
                    }
                    if (progressDialog2 != null && progressDialog2.isShowing()) {
                        progressDialog2.dismiss();
                    }
                    if (str != null) {
                        LogUtils.e(HttpUtils.TAG, "onFailure: " + str);
                    }
                    if (z2) {
                        HttpUtils.post(context2, str2, requestParams2, i2, onResultListener2, progressDialog2, view2, z2);
                    } else if (onResultListener2 != null) {
                        onResultListener2.onFailure(i2, "加载失败≡(▔﹏▔)≡");
                    }
                }

                @Override
                public void onSuccess(int i, Header[] headerArr, String str) {
                    if (progressDialog2 != null && progressDialog2.isShowing()) {
                        progressDialog2.dismiss();
                    }
                    if (view2 != null) {
                        view2.setEnabled(true);
                    }
                    LogUtils.e(HttpUtils.TAG, "onSuccess: " + str);
                    Messages messages = JsonUtils.getMessages(str);
                    if (messages.getFlag() == 0) {
                        if (onResultListener2 != null) {
                            onResultListener2.onSuccess(i2, JsonUtils.JsonParse(messages));
                        }
                    } else if (onResultListener2 != null) {
                        onResultListener2.onAlert(i2, messages.getMsg());
                    }
                }
            });
            return;
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (view != null) {
            view.setEnabled(true);
        }
        Toasts.show(context, Contants.getString(context, R.string.net_error), 1000);
        onResultListener.onFailure(i, Contants.getString(context, R.string.net_error));
    }

    public static void calcle(Context context) {
        client.cancelRequests(context, true);
    }

    public static boolean uploadFile(final Context context, String str, String url, String name, final ProgressDialog progressDialog) throws Exception {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ce.docx");
        if (file.exists()) {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            RequestParams requestParams = new RequestParams();
            requestParams.put("uploadFile", file);
            requestParams.put("name", name);
            progressDialog.setMax(Integer.parseInt(file.length() + ""));
            asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {

                public void onProgress(long j, long j2) {
                    progressDialog.setProgress((int) (((((double) j) * 1.0d) / ((double) j2)) * 100.0d));
                    super.onProgress(j, j2);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "上传成功", Toast.LENGTH_LONG).show();
                    HttpUtils.isSuccess = true;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "上传失败", Toast.LENGTH_LONG).show();
                    HttpUtils.isSuccess = false;
                }

                public void onRetry(int i) {
                    super.onRetry(i);
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(context, "文件不存在", 1).show();
        }
        return isSuccess;
    }
}
