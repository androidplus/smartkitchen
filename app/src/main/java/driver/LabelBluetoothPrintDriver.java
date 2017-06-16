package driver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.vilyever.socketclient.util.SocketSplitter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class LabelBluetoothPrintDriver implements Contants {
    public static final int CODE11 = 9;
    public static final int CODE39 = 4;
    public static final int CODE93 = 7;
    public static final int CODEBAR = 6;
    public static final int Code128_B = 8;
    public static final int EAN13 = 2;
    public static final int EAN8 = 3;
    public static final int ITF = 5;
    public static final int MSI = 10;
    private static final int STATE_CONNECTED = 3;
    private static final int STATE_CONNECTING = 2;
    private static final int STATE_LISTEN = 1;
    private static final int STATE_NONE = 0;
    public static final int UPCA = 0;
    public static final int UPCE = 1;
    private static LabelBluetoothPrintDriver mBluetoothPrintDriver;
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final String NAME = "BluetoothChatService";
    private final String TAG = "LbBluetoothPrintDriver";
    private AcceptThread mAcceptThread;
    private final BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private Handler mHandler;
    private int mState = 0;

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket bluetoothServerSocket;
            try {
                bluetoothServerSocket = (BluetoothServerSocket) LabelBluetoothPrintDriver.this.mAdapter.getClass().getMethod("listenUsingRfcommOn", new Class[]{Integer.TYPE}).invoke(LabelBluetoothPrintDriver.this.mAdapter, new Object[]{Integer.valueOf(28)});
            } catch (Exception e) {
                bluetoothServerSocket = null;
            }
            this.mmServerSocket = bluetoothServerSocket;
        }

        public void run() {
            setName("AcceptThread");
            while (LabelBluetoothPrintDriver.this.mState != 3) {
                try {
                    BluetoothSocket accept = this.mmServerSocket.accept();
                    if (accept != null) {
                        synchronized (LabelBluetoothPrintDriver.this) {
                            switch (LabelBluetoothPrintDriver.this.mState) {
                                case 0:
                                case 3:
                                    try {
                                        accept.close();
                                        break;
                                    } catch (IOException e) {
                                        break;
                                    }
                                case 1:
                                case 2:
                                    LabelBluetoothPrintDriver.this.connected(accept, accept.getRemoteDevice());
                                    break;
                            }
                        }
                    }
                } catch (IOException e2) {
                    return;
                }
            }
            return;
        }

        public void cancel() {
            try {
                this.mmServerSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothDevice mmDevice;
        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice bluetoothDevice) {
            BluetoothSocket bluetoothSocket;
            this.mmDevice = bluetoothDevice;
            try {
                bluetoothSocket = (BluetoothSocket) bluetoothDevice.getClass().getMethod("createRfcommSocket", new Class[]{Integer.TYPE}).invoke(bluetoothDevice, new Object[]{Integer.valueOf(1)});
            } catch (Exception e) {
                bluetoothSocket = null;
            }
            this.mmSocket = bluetoothSocket;
        }

        public void run() {
            setName("ConnectThread");
            LabelBluetoothPrintDriver.this.mAdapter.cancelDiscovery();
            try {
                this.mmSocket.connect();
                synchronized (LabelBluetoothPrintDriver.this) {
                    LabelBluetoothPrintDriver.this.mConnectThread = null;
                }
                LabelBluetoothPrintDriver.this.connected(this.mmSocket, this.mmDevice);
            } catch (IOException e) {
                LabelBluetoothPrintDriver.this.sendMessageToMainThread(33);
                try {
                    this.mmSocket.close();
                } catch (IOException e2) {
                }
                LabelBluetoothPrintDriver.this.start();
            }
        }

        public void cancel() {
            try {
                this.mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final BluetoothSocket mmSocket;
        final /* synthetic */ LabelBluetoothPrintDriver this$0;

        public ConnectedThread(LabelBluetoothPrintDriver labelBluetoothPrintDriver, BluetoothSocket bluetoothSocket) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            this.this$0 = labelBluetoothPrintDriver;
            this.mmSocket = bluetoothSocket;
            try {
                inputStream = bluetoothSocket.getInputStream();
                try {
                    outputStream = bluetoothSocket.getOutputStream();
                } catch (IOException e) {
                }
            } catch (IOException e2) {
                Object obj = outputStream;
            }
            this.mmInStream = inputStream;
            this.mmOutStream = outputStream;
        }

        public void run() {
            byte[] bArr = new byte[1024];
            while (true) {
                try {
                    if (this.mmInStream.available() != 0) {
                        for (int i = 0; i < 3; i++) {
                            bArr[i] = (byte) this.mmInStream.read();
                        }
                    }
                } catch (IOException e) {
                    return;
                }
            }
        }

        public void write(byte[] bArr) {
            try {
                this.mmOutStream.write(bArr);
            } catch (IOException e) {
            }
        }

        public void write(byte[] bArr, int i) {
            int i2 = 0;
            while (i2 < i) {
                try {
                    this.mmOutStream.write(bArr[i2]);
                    i2++;
                } catch (IOException e) {
                    return;
                }
            }
        }

        public void cancel() {
            try {
                this.mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private LabelBluetoothPrintDriver() {
    }

    public static LabelBluetoothPrintDriver getInstance() {
        if (mBluetoothPrintDriver == null) {
            mBluetoothPrintDriver = new LabelBluetoothPrintDriver();
        }
        return mBluetoothPrintDriver;
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    private void sendMessageToMainThread(int i) {
        if (this.mHandler != null) {
            Message obtainMessage = this.mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putInt("flag", i);
            obtainMessage.setData(bundle);
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    private void sendMessageToMainThread(int i, int i2) {
        if (this.mHandler != null) {
            Message obtainMessage = this.mHandler.obtainMessage();
            Bundle bundle = new Bundle();
            bundle.putInt("flag", i);
            bundle.putInt("state", i2);
            obtainMessage.setData(bundle);
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    public synchronized int getState() {
        return this.mState;
    }

    private synchronized void setState(int i) {
        this.mState = i;
        switch (this.mState) {
            case 0:
                sendMessageToMainThread(32, 16);
                break;
            case 3:
                sendMessageToMainThread(32, 17);
                break;
        }
    }

    public synchronized void start() {
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }
        if (this.mAcceptThread == null) {
            this.mAcceptThread = new AcceptThread();
            this.mAcceptThread.start();
        }
        setState(1);
    }

    public synchronized void connect(BluetoothDevice bluetoothDevice) {
        if (this.mState == 2 && this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }
        this.mConnectThread = new ConnectThread(bluetoothDevice);
        this.mConnectThread.start();
        setState(2);
    }

    private synchronized void connected(BluetoothSocket bluetoothSocket, BluetoothDevice bluetoothDevice) {
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }
        if (this.mAcceptThread != null) {
            this.mAcceptThread.cancel();
            this.mAcceptThread = null;
        }
        this.mConnectedThread = new ConnectedThread(this, bluetoothSocket);
        this.mConnectedThread.start();
        sendMessageToMainThread(34);
        setState(3);
    }

    public synchronized void stop() {
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }
        if (this.mAcceptThread != null) {
            this.mAcceptThread.cancel();
            this.mAcceptThread = null;
        }
        setState(0);
    }

    public void write(byte[] bArr) {
        synchronized (this) {
            if (this.mState != 3) {
                return;
            }
            ConnectedThread connectedThread = this.mConnectedThread;
            connectedThread.write(bArr);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void write2(byte[] r5) throws IOException {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = r4.mState;	 Catch:{ all -> 0x001b }
        r1 = 3;
        if (r0 == r1) goto L_0x0008;
    L_0x0006:
        monitor-exit(r4);	 Catch:{ all -> 0x001b }
    L_0x0007:
        return;
    L_0x0008:
        r1 = r4.mConnectedThread;	 Catch:{ all -> 0x001b }
        monitor-exit(r4);	 Catch:{ all -> 0x001b }
        r0 = 0;
    L_0x000c:
        r2 = r5.length;
        if (r0 >= r2) goto L_0x0007;
    L_0x000f:
        r2 = r1.mmOutStream;
        r3 = r5[r0];
        r2.write(r3);
        r0 = r0 + 1;
        goto L_0x000c;
    L_0x001b:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x001b }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: driver.LabelBluetoothPrintDriver.write2(byte[]):void");
    }

    public void BT_Write(String str) {
        byte[] bArr = null;
        if (this.mState == 3) {
            ConnectedThread connectedThread = this.mConnectedThread;
            try {
                bArr = str.getBytes("GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            connectedThread.write(bArr);
        }
    }

    public void BT_Write(String str, boolean z) {
        byte[] bArr = null;
        if (this.mState == 3) {
            ConnectedThread connectedThread = this.mConnectedThread;
            if (z) {
                try {
                    bArr = str.getBytes("GBK");
                } catch (UnsupportedEncodingException e) {
                }
            } else {
                bArr = str.getBytes();
            }
            connectedThread.write(bArr);
        }
    }

    public void BT_Write(byte[] bArr) {
        if (this.mState == 3) {
            this.mConnectedThread.write(bArr);
        }
    }

    public void BT_Write(byte[] bArr, int i) {
        if (this.mState == 3) {
            this.mConnectedThread.write(bArr, i);
        }
    }

    public boolean IsNoConnection() {
        if (this.mState != 3) {
            return true;
        }
        return false;
    }

    public boolean InitPrinter() {
        byte[] bArr = new byte[]{(byte) 27, (byte) 64};
        if (this.mState != 3) {
            return false;
        }
        BT_Write(bArr);
        return true;
    }

    public void WakeUpPritner() {
        try {
            BT_Write(new byte[]{(byte) 0, (byte) 0, (byte) 0});
            Thread.sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Begin() {
        WakeUpPritner();
        InitPrinter();
    }

    public void SetPrintRotate(byte b) {
        BT_Write(new byte[]{(byte) 31, (byte) 106, b});
    }

    public void SetSize(String str, String str2) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "SIZE";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = " ";
        strArr[4] = "mm";
        strArr[5] = ",";
        strArr[6] = str2;
        strArr[7] = " ";
        strArr[8] = "mm";
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void SetGAP(String str, String str2) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "GAP";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = " ";
        strArr[4] = "mm";
        strArr[5] = ",";
        strArr[6] = str2;
        strArr[7] = " ";
        strArr[8] = "mm";
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void SetOFFSET(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "OFFSET";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = " ";
        strArr[4] = "mm";
        strArr[5] = SocketSplitter.Splitter;
        while (i < 6) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void SetSPEED(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "SPEED";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void SetDENSITY(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "DENSITY";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void SetDIRECTION(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "DIRECTION";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void SetREFERENCE(String str, String str2) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "REFERENCE";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = SocketSplitter.Splitter;
        while (i < 6) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void SetSHIFT(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "SHIFT";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void SetCODEPAGE(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "CODEPAGE";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void SetCLS() {
        BT_Write("CLS\r\n".getBytes());
    }

    public void SetFEED(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "FEED";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void SetBACKFEED(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "BACKFEED";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void SetFORMFEED() {
        BT_Write("FORMFEED\r\n".getBytes());
    }

    public void SetHOME() {
        BT_Write("HOME\r\n".getBytes());
    }

    public void SetPRINT(String str, String str2) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "PRINT";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = SocketSplitter.Splitter;
        while (i < 6) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void SetSOUND(String str, String str2) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "SOUND";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = SocketSplitter.Splitter;
        while (i < 6) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void SetLIMITFEED(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "LIMITFEED";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = " ";
        strArr[4] = "mm";
        strArr[5] = SocketSplitter.Splitter;
        while (i < 6) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void setDhcp(boolean z) {
        byte[] bArr = new byte[4];
        bArr[0] = (byte) 31;
        bArr[1] = (byte) 98;
        bArr[2] = (byte) 68;
        if (z) {
            bArr[3] = (byte) 1;
        } else {
            bArr[3] = (byte) 0;
        }
        BT_Write(bArr);
    }

    public void setStaticIp(String paramString1, String paramString2, String paramString3){
        String[]ss1 = paramString1.split("\\.");
        String[]ss2 = paramString2.split("\\.");
        String[]ss3 = paramString3.split("\\.");
        BT_Write(new byte[] { 31, 105, (byte)Short.parseShort(ss1[0]), (byte)Short.parseShort(ss1[1]), (byte)Short.parseShort(ss1[2]), (byte)Short.parseShort(ss1[3]), 31, 37, 0, (byte)Short.parseShort(ss2[0]), (byte)Short.parseShort(ss2[0]), (byte)Short.parseShort(ss2[0]), (byte)Short.parseShort(ss2[0]), 31, 37, 1, (byte)Short.parseShort(ss3[0]), (byte)Short.parseShort(ss3[0]), (byte)Short.parseShort(ss3[0]), (byte)Short.parseShort(ss3[0]) });
    }


    private void setOpen(String str, byte b) {
        byte[] bArr = new byte[(((str.length() + 3) + 3) + 1)];
        bArr[0] = (byte) 31;
        bArr[1] = (byte) 119;
        bArr[2] = (byte) str.length();
        int i = 3;
        int i2 = 0;
        while (i2 < str.length()) {
            int i3 = i + 1;
            bArr[i] = (byte) str.charAt(i2);
            i2++;
            i = i3;
        }
        i2 = i + 1;
        bArr[i] = (byte) 0;
        i = i2 + 1;
        bArr[i2] = (byte) 0;
        i2 = i + 1;
        bArr[i] = (byte) 0;
        if (b == (byte) 0) {
            i = i2 + 1;
            bArr[i2] = (byte) 0;
        } else {
            int i4 = i2 + 1;
            bArr[i2] = (byte) 1;
        }
        BT_Write(bArr);
    }

    private void setWPA(String str, String str2, byte b, byte b2, byte b3) {
        byte[] bArr = new byte[((((str.length() + 3) + 3) + str2.length()) + 1)];
        bArr[0] = (byte) 31;
        bArr[1] = (byte) 119;
        bArr[2] = (byte) str.length();
        int i = 0;
        int i2 = 3;
        while (i < str.length()) {
            int i3 = i2 + 1;
            bArr[i2] = (byte) str.charAt(i);
            i++;
            i2 = i3;
        }
        if (b2 == (byte) 0) {
            i = i2 + 1;
            bArr[i2] = (byte) 2;
            i2 = i;
        } else {
            i = i2 + 1;
            bArr[i2] = (byte) 3;
            i2 = i;
        }
        if (b3 == (byte) 0) {
            i = i2 + 1;
            bArr[i2] = (byte) 1;
        } else {
            i = i2 + 1;
            bArr[i2] = (byte) 0;
        }
        i2 = i + 1;
        bArr[i] = (byte) str2.length();
        i = 0;
        while (i < str2.length()) {
            int i4 = i2 + 1;
            bArr[i2] = (byte) str2.charAt(i);
            i++;
            i2 = i4;
        }
        if (b == (byte) 0) {
            i = i2 + 1;
            bArr[i2] = (byte) 0;
        } else {
            i = i2 + 1;
            bArr[i2] = (byte) 1;
        }
        BT_Write(bArr);
    }

    private void setWEP(String str, String str2, byte b, byte b2) {
        byte[] bArr = new byte[((((str.length() + 3) + 3) + str2.length()) + 1)];
        bArr[0] = (byte) 31;
        bArr[1] = (byte) 119;
        bArr[2] = (byte) str.length();
        int i = 3;
        int i2 = 0;
        while (i2 < str.length()) {
            int i3 = i + 1;
            bArr[i] = (byte) str.charAt(i2);
            i2++;
            i = i3;
        }
        int i3;
        if (b2 == (byte) 0) {
            i3 = i + 1;
            bArr[i] = (byte) 0;
            i2 = i3 + 1;
            bArr[i3] = (byte) 1;
        } else {
            i3 = i + 1;
            bArr[i] = (byte) 1;
            i2 = i3 + 1;
            bArr[i3] = (byte) 0;
        }
        i = i2 + 1;
        bArr[i2] = (byte) str2.length();
        i2 = 0;
        while (i2 < str2.length()) {
            i3 = i + 1;
            bArr[i] = (byte) str2.charAt(i2);
            i2++;
            i = i3;
        }
        if (b == (byte) 0) {
            i2 = i + 1;
            bArr[i] = (byte) 0;
        } else {
            i2 = i + 1;
            bArr[i] = (byte) 1;
        }
        BT_Write(bArr);
    }

    public void setWifiParam(String str, String str2, byte b, byte b2, byte b3, byte b4, byte b5) {
        if (str != null && str.length() >= 0) {
            if (b == (byte) 0) {
                try {
                    setOpen(str, b5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if ((byte) 1 == b) {
                setWPA(str, str2, b5, b2, b3);
            } else {
                setWEP(str, str2, b5, b4);
            }
        }
    }

    public void SelftestPrint() {
        BT_Write("SELFTEST\r\n".getBytes());
    }

    public void SetBAR(String str, String str2, String str3, String str4) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "BAR";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = str3;
        strArr[7] = ",";
        strArr[8] = str4;
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void drawBox(String str, String str2, String str3, String str4, String str5) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "BOX";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = str3;
        strArr[7] = ",";
        strArr[8] = str4;
        strArr[9] = ",";
        strArr[10] = str5;
        strArr[11] = SocketSplitter.Splitter;
        while (i < 12) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void drawBitMap(String str, String str2, String str3, String str4, String str5, byte[] bArr) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "BITMAP";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = str3;
        strArr[7] = ",";
        strArr[8] = str4;
        strArr[9] = ",";
        strArr[10] = str5;
        strArr[11] = ",";
        while (i < 12) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
        BT_Write(bArr);
        BT_Write(SocketSplitter.Splitter);
    }

    public void PutBmp(String str, String str2, String str3) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "PUTBMP";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = "\"";
        strArr[7] = str3;
        strArr[8] = "\"";
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void PutPcx(String str, String str2, String str3) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "PUTPCX";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = "\"";
        strArr[7] = str3;
        strArr[8] = "\"";
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void ClearData(String str, String str2, String str3, String str4) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "ERASE";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = str3;
        strArr[7] = ",";
        strArr[8] = str4;
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void SetReverse(String str, String str2, String str3, String str4) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "REVERSE";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = str3;
        strArr[7] = ",";
        strArr[8] = str4;
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void PrintText(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "TEXT";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = "\"";
        strArr[7] = str3;
        strArr[8] = "\"";
        strArr[9] = ",";
        strArr[10] = str4;
        strArr[11] = ",";
        strArr[12] = str5;
        strArr[13] = ",";
        strArr[14] = str6;
        strArr[15] = ",";
        strArr[16] = "\"";
        strArr[17] = str7.replaceAll(" ", "\b");
        strArr[18] = "\"";
        strArr[19] = SocketSplitter.Splitter;
        while (i < 20) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void loadProFile(String str) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "DOWNLOAD";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = SocketSplitter.Splitter;
        while (i < 4) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void loadDateFile(String str, String str2, String str3) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "DOWNLOAD";
        strArr[1] = " ";
        strArr[2] = "\"";
        strArr[3] = str;
        strArr[4] = "\"";
        strArr[5] = ",";
        strArr[6] = str2;
        strArr[7] = ",";
        strArr[8] = str3;
        strArr[9] = SocketSplitter.Splitter;
        while (i < 10) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void endPro() {
        BT_Write("EOP\r\n".getBytes());
    }

    public void setCounter(String str, String str2) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "SET COUNTER";
        strArr[1] = " ";
        strArr[2] = "@";
        strArr[3] = str;
        strArr[4] = ",";
        strArr[5] = str2;
        strArr[6] = SocketSplitter.Splitter;
        while (i < 7) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }

    public void CodePrint(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, String str9) {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer();
        String[] strArr = new String[1024];
        strArr[0] = "BARCODE";
        strArr[1] = " ";
        strArr[2] = str;
        strArr[3] = ",";
        strArr[4] = str2;
        strArr[5] = ",";
        strArr[6] = "\"";
        strArr[7] = str3;
        strArr[8] = "\"";
        strArr[9] = ",";
        strArr[10] = str4;
        strArr[11] = ",";
        strArr[12] = str5;
        strArr[13] = ",";
        strArr[14] = str6;
        strArr[15] = ",";
        strArr[16] = str7;
        strArr[17] = ",";
        strArr[18] = str8;
        strArr[19] = ",";
        strArr[20] = "\"";
        strArr[21] = str9;
        strArr[22] = "\"";
        strArr[23] = SocketSplitter.Splitter;
        while (i < 24) {
            stringBuffer.append(strArr[i]);
            i++;
        }
        BT_Write(stringBuffer.toString());
    }
}
