package com.smart.kitchen.smartkitchen.service;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.loopj.android.http.RequestParams;
import com.smart.kitchen.smartkitchen.BaseActivity;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.db.daoutils.MessageCenterDaoManager;
import com.smart.kitchen.smartkitchen.db.daoutils.UserInfoDaoManager;
import com.smart.kitchen.smartkitchen.entitys.MessageCenter;
import com.smart.kitchen.smartkitchen.entitys.OrderInfo;
import com.smart.kitchen.smartkitchen.entitys.TableNumber;
import com.smart.kitchen.smartkitchen.mvp.model.OnResultListener;
import com.smart.kitchen.smartkitchen.mvp.presenter.TablePresenter;
import com.smart.kitchen.smartkitchen.print.PrintUtil;
import com.smart.kitchen.smartkitchen.utils.CalendarUtils;
import com.smart.kitchen.smartkitchen.utils.Contants;
import com.smart.kitchen.smartkitchen.utils.DeviceUtils;
import com.smart.kitchen.smartkitchen.utils.FinishActivity;
import com.smart.kitchen.smartkitchen.utils.HttpUtils;
import com.smart.kitchen.smartkitchen.utils.LogUtils;
import com.smart.kitchen.smartkitchen.utils.ReceiveMsgAuthFilter;
import com.smart.kitchen.smartkitchen.utils.SPUtils;
import com.vilyever.socketclient.SocketClient;
import com.vilyever.socketclient.SocketClient.SocketDelegate;
import com.vilyever.socketclient.SocketResponsePacket;
import java.util.ArrayList;
import java.util.List;

public class ClientSocketService extends Service implements SocketDelegate {
    private static final String TAG = "ClientSocketService";
    public static boolean is_running = false;
    private Context context;
    private MessageCenterDaoManager daoManager;
    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    for (String str : ReceiveMessage.splite((String) message.obj)) {
                        Message message2 = ClientSocketService.this.getMessage();
                        message2.what = 2;
                        message2.obj = str;
                        ClientSocketService.this.handler.sendMessage(message2);
                    }
                    return;
                case 2:
                    try {
                        ClientSocketService.this.doData((String) message.obj);
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                case 3:
                    ClientSocketService.this.getOrders((Long) message.obj, 1);
                    Log.e(ClientSocketService.TAG, "onSuccess: 自动打印");
                    return;
                case 4:
                    AlertDialog create = new Builder(ClientSocketService.this.context).setMessage(ClientSocketService.this.getString(R.string.out)).setCancelable(false).setPositiveButton(ClientSocketService.this.getString(R.string.confirm), new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            SPUtils.setUserinfo(ClientSocketService.this.context, SPUtils.IS_LOGIN, Boolean.valueOf(false));
                            SPUtils.remove(ClientSocketService.this.context, SPUtils.SETTING_AREA);
                            FinishActivity.finish();
                        }
                    }).create();
                    create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    create.getWindow().setType(2003);
                    create.show();
                    return;
                case 5:
                    ClientSocketService.this.takenourCenter = (MessageCenter) JSON.parseObject((String) message.obj, new TypeReference<MessageCenter>() {
                    }, new Feature[0]);
                    ClientSocketService.this.orderReceiving(ClientSocketService.this.takenourCenter.getMsg_id(), ClientSocketService.this.takenourCenter.getMsg_type().intValue());
                    return;
                default:
                    return;
            }
        }
    };
    private MessageCenter messageCenter;
    private long mms = 0;
    private long mms1 = 0;
    private PrintUtil printUtil = PrintUtil.getInstance();
    private ReceiveMsgAuthFilter receiveMsgAuthFilter;
    private ClientSockets socketClient;
    private TablePresenter tablePresenter;
    private MessageCenter takenourCenter;
    private List<Long> temp = new ArrayList();

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.context = this;
        is_running = true;
        this.daoManager = new MessageCenterDaoManager();
        this.tablePresenter = new TablePresenter(this.context, null, null);
        initSocket();
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (!(intent == null || intent.getLongExtra("orderId", -1) == -1)) {
            getOrders(Long.valueOf(intent.getLongExtra("orderId", -1)), 0);
        }
        return super.onStartCommand(intent, i, i2);
    }

    private void initSocket() {
        if (Contants.isEmpty(SPUtils.getUserinfo(this.context, SPUtils.STORE_ID))) {
            stopSelf();
            return;
        }
        this.socketClient = new ClientSockets();
        this.socketClient.registerConnectListener(this);
        this.socketClient.connect();
    }

    public void onConnected(SocketClient socketClient) {
        LogUtils.e(TAG, "socket连接成功: ");
    }

    public void onDisconnected(SocketClient socketClient) {
        if (is_running) {
            LogUtils.e(TAG, "正在重新连接socket: ");
            try {
                this.socketClient = null;
                initSocket();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onResponse(SocketClient socketClient, SocketResponsePacket socketResponsePacket) {
        String message = socketResponsePacket.getMessage();
        if (message != null) {
            Message message2 = getMessage();
            message2.what = 1;
            message2.obj = message;
            this.handler.sendMessage(message2);
        }
    }

    public void doData(String str) {
        int i = 0;
        MyMessages myMessages = (MyMessages) JSON.parseObject(str, new TypeReference<MyMessages>() {
        }, new Feature[0]);
        if (!myMessages.getType().equals("ping")) {
            LogUtils.e(TAG, "doData: " + str);
        }
        String type = myMessages.getType();
        int i2 = -1;
        switch (type.hashCode()) {
            case -1635650018:
                if (type.equals("changetable")) {
                    i2 = 4;
                    break;
                }
                break;
            case -1204616613:
                if (type.equals("paysuccess")) {
                    i2 = 3;
                    break;
                }
                break;
            case -554624859:
                if (type.equals("updatetable")) {
                    i2 = 5;
                    break;
                }
                break;
            case 108417:
                if (type.equals("msg")) {
                    i2 = 2;
                    break;
                }
                break;
            case 110414:
                if (type.equals("out")) {
                    i2 = 6;
                    break;
                }
                break;
            case 3237136:
                if (type.equals("init")) {
                    i2 = 1;
                    break;
                }
                break;
            case 3441010:
                if (type.equals("ping")) {
                    i2 = 0;
                    break;
                }
                break;
        }
        Intent intent;
        MessageCenter messageCenter;
        List list;
        switch (i2) {
            case 0:
                long nowMms = CalendarUtils.getNowMms();
                long j = (nowMms - this.mms) / 1000;
                if (j < 30) {
                    LogUtils.e(TAG, j + "s");
                    return;
                }
                this.mms = nowMms;
                RequestParams requestParams = new RequestParams();
                requestParams.put(SPUtils.STORE_ID, SPUtils.getUserinfo(this.context, SPUtils.STORE_ID));
                HttpUtils.post(this.context, Contants.SAEARCH_ORDERFROMPOS, requestParams);
                return;
            case 1:
                if (!Contants.isEmpty(myMessages.getClient_id())) {
                    RequestParams requestParams2 = new RequestParams();
                    requestParams2.put("client_id", myMessages.getClient_id());
                    requestParams2.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
                    requestParams2.put("device_id", DeviceUtils.getDeviceId(this.context));
                    requestParams2.put(SPUtils.STORE_ID, SPUtils.getUserinfo(this, SPUtils.STORE_ID));
                    HttpUtils.post(this.context, Contants.BIND_SOCKET, requestParams2, false);
                    return;
                }
                return;
            case 2:
                this.messageCenter = (MessageCenter) JSON.parseObject(myMessages.getData(), new TypeReference<MessageCenter>() {
                }, new Feature[0]);
                long nowMms2 = CalendarUtils.getNowMms();
                long j2 = (nowMms2 - this.mms1) / 1000;
                if (this.temp.indexOf(this.messageCenter.getMsg_id()) == -1 || this.messageCenter.getFlag().intValue() != 0 || j2 >= 30) {
                    Message message;
                    this.mms1 = nowMms2;
                    this.temp.add(this.messageCenter.getMsg_id());
                    if (SPUtils.getUserinfos2(this.context, SPUtils.SETTING_PRINT_KITCHEN) && this.messageCenter.getMsg_type().intValue() < 5 && this.messageCenter.getMsg_type().intValue() == 0 && this.messageCenter.getFlag().intValue() == 0) {
                        message = getMessage();
                        message.what = 3;
                        message.obj = this.messageCenter.getMsg_id();
                        this.handler.sendMessage(message);
                    }
                    if (SPUtils.getUserinfos2(this.context, SPUtils.TAKEOUT_RECEIVING) && ((this.messageCenter.getMsg_type().intValue() == 1 || this.messageCenter.getMsg_type().intValue() == 2 || this.messageCenter.getMsg_type().intValue() == 3 || this.messageCenter.getMsg_type().intValue() == 4) && this.messageCenter.getFlag().intValue() == 0)) {
                        message = getMessage();
                        message.what = 5;
                        message.obj = myMessages.getData();
                        this.handler.sendMessage(message);
                    }
                    this.receiveMsgAuthFilter = new ReceiveMsgAuthFilter(this.messageCenter, this.context);
                    if (this.receiveMsgAuthFilter.canSave()) {
                        this.daoManager.saveMsg(this.messageCenter);
                        intent = new Intent(BaseActivity.MSG_BROCARSTRECEIVER);
                        intent.putExtra("msg", myMessages.getData());
                        sendBroadcast(intent);
                        return;
                    }
                    return;
                }
                LogUtils.e(TAG, j2 + "s" + this.messageCenter.getMsg_id());
                return;
            case 3:
                messageCenter = (MessageCenter) JSON.parseObject(myMessages.getData(), new TypeReference<MessageCenter>() {
                }, new Feature[0]);
                intent = new Intent(BaseActivity.MSG_BROCARSTRECEIVER);
                intent.putExtra("paysuccess", messageCenter.getMsg_content());
                intent.putExtra("orderId", messageCenter.getMsg_id());
                sendBroadcast(intent);
                return;
            case 4:
                messageCenter = (MessageCenter) JSON.parseObject(myMessages.getData(), new TypeReference<MessageCenter>() {
                }, new Feature[0]);
                if (!TextUtils.isEmpty(messageCenter.getMsg_content())) {
                    list = (List) JSON.parseObject(messageCenter.getMsg_content(), new TypeReference<List<TableNumber>>() {
                    }, new Feature[0]);
                    List arrayList = new ArrayList();
                    for (int i3 = 0; i3 < list.size(); i3++) {
                        TableNumber tableNumber = this.tablePresenter.getTableNumber(((TableNumber) list.get(i3)).getId().longValue());
                        if (tableNumber != null) {
                            i2 = tableNumber.getTable_type_count().intValue() - ((TableNumber) list.get(i3)).getTable_type_count().intValue();
                            if (i2 <= 0) {
                                tableNumber.setTable_type_count(Integer.valueOf(0));
                                tableNumber.setTable_person(Integer.valueOf(0));
                            } else {
                                tableNumber.setTable_type_count(Integer.valueOf(i2));
                                tableNumber.setTable_person(Integer.valueOf(1));
                            }
                            arrayList.add(tableNumber);
                        }
                    }
                    this.tablePresenter.setTableNumber(arrayList);
                    return;
                }
                return;
            case 5:
                messageCenter = (MessageCenter) JSON.parseObject(myMessages.getData(), new TypeReference<MessageCenter>() {
                }, new Feature[0]);
                if (!TextUtils.isEmpty(messageCenter.getMsg_content()) && messageCenter.getMsg_id() != null && !String.valueOf(messageCenter.getMsg_id()).equals(new UserInfoDaoManager().getNowUserInfo().getUserid())) {
                    list = (List) JSON.parseObject(messageCenter.getMsg_content(), new TypeReference<List<TableNumber>>() {
                    }, new Feature[0]);
                    List arrayList2 = new ArrayList();
                    while (i < list.size()) {
                        TableNumber tableNumber2 = this.tablePresenter.getTableNumber(((TableNumber) list.get(i)).getId().longValue());
                        if (tableNumber2 != null) {
                            tableNumber2.setTable_type_count(Integer.valueOf(((TableNumber) list.get(i)).getTable_type_count().intValue() + tableNumber2.getTable_type_count().intValue()));
                            tableNumber2.setTable_person(Integer.valueOf(1));
                            arrayList2.add(tableNumber2);
                        }
                        i++;
                    }
                    this.tablePresenter.setTableNumber(arrayList2);
                    return;
                }
                return;
            case 6:
                if (!myMessages.getData().equals(DeviceUtils.getDeviceId(this.context))) {
                    LogUtils.e(TAG, "异地登陆");
                    is_running = false;
                    if (this.socketClient != null) {
                        this.socketClient.unregisterConnectListener(this);
                        this.socketClient.disconnect();
                        this.socketClient = null;
                    }
                    stopSelf();
                    Message message2 = getMessage();
                    message2.what = 4;
                    this.handler.sendMessage(message2);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public Message getMessage() {
        return new Message();
    }

    public void onDestroy() {
        super.onDestroy();
        is_running = false;
        if (this.socketClient != null) {
            this.socketClient.unregisterConnectListener(this);
            this.socketClient.disconnect();
            this.socketClient = null;
        }
    }

    public void orderReceiving(final Long l, int i) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderid", (Object) l);
        requestParams.put("staffid", new UserInfoDaoManager().getNowUserInfo().getUserid());
        requestParams.put("status", 1);
        if (i == 1) {
            requestParams.put("type", "wxwaimai");
        } else if (i == 2) {
            requestParams.put("type", "meituan");
        } else if (i == 3) {
            requestParams.put("type", "eleme");
        } else if (i == 4) {
            requestParams.put("type", "baidu");
        }
        HttpUtils.post(this.context, Contants.UPDATEWAIMAISTATUS, requestParams, new OnResultListener() {
            public void onSuccess(int i, String str) {
                ClientSocketService.this.getOrders(l, 1);
            }

            public void onAlert(int i, String str) {
            }

            public void onFailure(int i, String str) {
            }
        });
    }

    public void getOrders(Long l, final int i) {
        String str = Contants.ORDERFROMPOS;
        if (i == 0) {
            str = Contants.ORDERFROM;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderid", (Object) l);
        HttpUtils.post(this.context, str, requestParams, new OnResultListener() {
            public void onSuccess(int i, String str) {
                OrderInfo orderInfo = (OrderInfo) JSON.parseObject(str, new TypeReference<OrderInfo>() {
                }, new Feature[0]);
                if (!Contants.isEmpty(SPUtils.getUserinfo(ClientSocketService.this.context, SPUtils.PRINT_KITCHEN_IP)) && SPUtils.getUserinfos2(ClientSocketService.this.context, SPUtils.SETTING_PRINT_KITCHEN)) {
                    try {
                        Log.e(ClientSocketService.TAG, "onSuccess: 打印" + i);
                        ClientSocketService.this.printUtil.delayPrint(ClientSocketService.this.context, "", orderInfo, i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onAlert(int i, String str) {
            }

            public void onFailure(int i, String str) {
            }
        });
    }
}
