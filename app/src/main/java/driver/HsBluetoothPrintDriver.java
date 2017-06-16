package driver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;
import org.apache.http.HttpStatus;

public class HsBluetoothPrintDriver implements Contants {
    private static final int STATE_CONNECTED = 3;
    private static final int STATE_CONNECTING = 2;
    private static final int STATE_LISTEN = 1;
    private static final int STATE_NONE = 0;
    private static HsBluetoothPrintDriver mBluetoothPrintDriver;
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private final String NAME = "BluetoothChatService";
    private final String TAG = "HsBluetoothPrintDriver";
    byte buf;
    byte buf2;
    private AcceptThread mAcceptThread;
    private BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    private String mCharsetName = "GBK";
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private Context mContext;
    private DefineNVLogoTask mDefineNVLogoTask;
    private Handler mHandler;
    private PrintImageTask mPrintImageTask;
    private int mState = 0;
    private BluetoothSocket pBluetoothSocket;

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket bluetoothServerSocket = null;
            try {
                bluetoothServerSocket = HsBluetoothPrintDriver.this.mAdapter.listenUsingRfcommWithServiceRecord("BluetoothChatService", HsBluetoothPrintDriver.this.MY_UUID);
            } catch (Throwable e) {
                Log.e("HsBluetoothPrintDriver", "listen() failed", e);
            }
            this.mmServerSocket = bluetoothServerSocket;
        }

        public void run() {
            setName("AcceptThread");
            while (HsBluetoothPrintDriver.this.mState != 3) {
                try {
                    BluetoothSocket accept = this.mmServerSocket.accept();
                    if (accept != null) {
                        synchronized (HsBluetoothPrintDriver.this) {
                            switch (HsBluetoothPrintDriver.this.mState) {
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
                                    HsBluetoothPrintDriver.this.connected(accept, accept.getRemoteDevice());
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
            this.mmDevice = bluetoothDevice;
            BluetoothSocket bluetoothSocket = null;
            try {
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(HsBluetoothPrintDriver.this.MY_UUID);
            } catch (Exception e) {
            }
            this.mmSocket = bluetoothSocket;
        }

        public void run() {
            setName("ConnectThread");
            HsBluetoothPrintDriver.this.mAdapter.cancelDiscovery();
            try {
                this.mmSocket.connect();
                synchronized (HsBluetoothPrintDriver.this) {
                    HsBluetoothPrintDriver.this.mConnectThread = null;
                }
                HsBluetoothPrintDriver.this.connected(this.mmSocket, this.mmDevice);
            } catch (IOException e) {
                HsBluetoothPrintDriver.this.sendMessageToMainThread(33);
                try {
                    this.mmSocket.close();
                } catch (IOException e2) {
                }
                HsBluetoothPrintDriver.this.start();
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
        final /* synthetic */ HsBluetoothPrintDriver this$0;

        public ConnectedThread(HsBluetoothPrintDriver hsBluetoothPrintDriver, BluetoothSocket bluetoothSocket) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            this.this$0 = hsBluetoothPrintDriver;
            this.mmSocket = bluetoothSocket;
            hsBluetoothPrintDriver.pBluetoothSocket = bluetoothSocket;
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

        public void read() {
            try {
                this.this$0.buf = (byte) 0;
                for (int i = 0; i < 120; i++) {
                    Log.e("cur", "cur=" + i);
                    sleep(50);
                    if (this.mmInStream.available() != 0) {
                        this.this$0.buf = (byte) this.mmInStream.read();
                        Log.e("buf", "buf=" + this.this$0.buf);
                        break;
                    }
                }
                Log.e("buf", "buf111=" + this.this$0.buf);
            } catch (IOException e) {
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }

        public void read(int i, Handler handler) {
            try {
                this.this$0.buf = (byte) 0;
                for (int i2 = 0; i2 < HttpStatus.SC_OK; i2++) {
                    Log.e("buf", "buf=" + i2);
                    sleep(50);
                    if (this.mmInStream.available() != 0) {
                        this.this$0.buf = (byte) this.mmInStream.read();
                        Log.e("buf", "buf=" + this.this$0.buf);
                        break;
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putInt("flag", i);
                bundle.putInt("state", this.this$0.buf);
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);
            } catch (IOException e) {
            } catch (InterruptedException e2) {
                e2.printStackTrace();
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

    private class DefineNVLogoTask extends AsyncTask<Void, Void, Void> {
        private String[] logolist;
        private int type;

        public DefineNVLogoTask(String[] strArr, int i) {
            this.logolist = strArr;
            this.type = i;
        }

        protected Void doInBackground(Void... voidArr) {
            HsBluetoothPrintDriver.this.BT_Write(new byte[]{(byte) 28, (byte) 113, (byte) this.logolist.length});
            for (int i = 0; i < this.logolist.length; i++) {
                Bitmap returnBitmap = BitmapConvertUtil.returnBitmap(this.logolist[i]);
                if (returnBitmap == null) {
                    Log.e("logolist============", this.logolist[i]);
                    break;
                }
                byte[] nVLogoByte = BitmapConvertUtil.getNVLogoByte(BitmapConvertUtil.decodeSampledBitmapFromBitmap(returnBitmap, this.type == 0 ? 384 : 576, 4000));
                HsBluetoothPrintDriver.this.BT_Write(new byte[]{(byte) (((returnBitmap.getWidth() + 7) / 8) % 256), (byte) (((returnBitmap.getWidth() + 7) / 8) / 256), (byte) (((returnBitmap.getHeight() + 7) / 8) % 256), (byte) (((returnBitmap.getHeight() + 7) / 8) / 256)});
                HsBluetoothPrintDriver.this.BT_Write(nVLogoByte);
                Log.e("HsBluetoothPrintDriver", "define nvlogo finished ============" + this.logolist[i]);
            }
            return null;
        }
    }

    private class PrintImageTask extends AsyncTask<Void, Void, Void> {
        private Bitmap bitmap;
        private int type;

        public PrintImageTask(Bitmap bitmap, int i) {
            this.bitmap = bitmap;
            this.type = i;
        }

        protected Void doInBackground(Void... voidArr) {
            Bitmap decodeSampledBitmapFromBitmap = BitmapConvertUtil.decodeSampledBitmapFromBitmap(this.bitmap, this.type == 0 ? 384 : 576, 4000);
            byte width = (byte) (((decodeSampledBitmapFromBitmap.getWidth() + 7) / 8) % 256);
            byte width2 = (byte) (((decodeSampledBitmapFromBitmap.getWidth() + 7) / 8) / 256);
            int height = ((decodeSampledBitmapFromBitmap.getHeight() + 30) - 1) / 30;
            int width3 = (decodeSampledBitmapFromBitmap.getWidth() + 7) / 8;
            byte[] convert = BitmapConvertUtil.convert(decodeSampledBitmapFromBitmap);
            for (int i = 0; i < height; i++) {
                byte b;
                if (i != height - 1) {
                    b = (byte) 30;
                    HsBluetoothPrintDriver.this.BT_Write(new byte[]{(byte) 29, (byte) 118, (byte) 48, (byte) 0, width, width2, b, (byte) 0});
                    HsBluetoothPrintDriver.this.BT_Write(Arrays.copyOfRange(convert, (width3 * i) * 30, ((width3 * 30) * (i + 1)) - 1));
                    HsBluetoothPrintDriver.this.BT_Write(new byte[]{(byte) 10});
                } else {
                    b = (byte) (decodeSampledBitmapFromBitmap.getHeight() % 30);
                    HsBluetoothPrintDriver.this.BT_Write(new byte[]{(byte) 29, (byte) 118, (byte) 48, (byte) 0, width, width2, b, (byte) 0});
                    HsBluetoothPrintDriver.this.BT_Write(Arrays.copyOfRange(convert, (width3 * i) * 30, convert.length));
                    HsBluetoothPrintDriver.this.BT_Write(new byte[]{(byte) 10});
                }
            }
            return null;
        }
    }

    public class ReadStateTask extends AsyncTask<Void, Void, Byte> {
        private int handlerSign;
        private Handler mhandler;
        private InputStream minputStream = null;

        public ReadStateTask(int i, Handler handler) {
            this.handlerSign = i;
            this.mhandler = handler;
            try {
                this.minputStream = HsBluetoothPrintDriver.this.pBluetoothSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected Byte doInBackground(Void... voidArr) {
            HsBluetoothPrintDriver.this.buf = (byte) 0;
            for (int i = 0; i < HttpStatus.SC_OK; i++) {
                Log.d("doInBackground------", i + "");
                try {
                    Thread.currentThread();
                    Thread.sleep(50);
                    if (this.minputStream.available() != 0) {
                        HsBluetoothPrintDriver.this.buf = (byte) this.minputStream.read();
                        int i2 = HsBluetoothPrintDriver.this.buf & 255;
                        if (i2 == 128) {
                            Log.d("bb------", i2 + "");
                            break;
                        }
                    } else {
                        continue;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            return Byte.valueOf(HsBluetoothPrintDriver.this.buf);
        }

        protected void onPostExecute(Byte b) {
            super.onPostExecute(b);
            Log.d("byt------", b + "");
            Bundle bundle = new Bundle();
            bundle.putInt("flag", this.handlerSign);
            bundle.putInt("state", b.byteValue());
            Message message = new Message();
            message.setData(bundle);
            this.mhandler.sendMessage(message);
        }
    }

    private HsBluetoothPrintDriver() {
    }

    public static HsBluetoothPrintDriver getInstance() {
        if (mBluetoothPrintDriver == null) {
            mBluetoothPrintDriver = new HsBluetoothPrintDriver();
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
        throw new UnsupportedOperationException("Method not decompiled: driver.HsBluetoothPrintDriver.write2(byte[]):void");
    }

    public void setCharsetName(String str) {
        if (str != null) {
            this.mCharsetName = str;
        }
    }

    public void BT_Write(String str) {
        byte[] bArr = null;
        if (this.mState == 3) {
            ConnectedThread connectedThread = this.mConnectedThread;
            try {
                bArr = str.getBytes(this.mCharsetName);
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
                try {
                    bArr = str.getBytes(this.mCharsetName);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            connectedThread.write(bArr);
        }
    }

    public void BT_Write(byte[] bArr) {
        if (this.mState == 3) {
            this.mConnectedThread.write(bArr);
        }
    }

    public byte BT_read() {
        if (this.mState != 3) {
            this.mConnectedThread.read();
        } else {
            this.mConnectedThread.read();
        }
        return this.buf;
    }

    public void BT_read(int i, Handler handler) {
        if (this.mState != 3) {
            this.mConnectedThread.read(i, handler);
        } else {
            this.mConnectedThread.read(i, handler);
        }
    }

    public void BT_Write(byte[] bArr, int i) {
        if (this.mState == 3) {
            this.mConnectedThread.write(bArr, i);
        }
    }

    public boolean IsNoConnection() {
        return this.mState != 3;
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

    public void SetDefaultSetting() {
        BT_Write(new byte[]{(byte) 27, (byte) 33, (byte) 0});
    }

    public void Begin() {
        WakeUpPritner();
        InitPrinter();
    }

    public void LF() {
        BT_Write(new byte[]{(byte) 13});
    }

    public void CR() {
        BT_Write(new byte[]{(byte) 10});
    }

    public void SelftestPrint() {
        BT_Write(new byte[]{(byte) 18, (byte) 84}, 2);
    }

    public void Beep(byte b, byte b2) {
        BT_Write(new byte[]{(byte) 27, (byte) 66, b, b2}, 4);
    }

    public byte StatusInquiry(byte b) {
        byte[] bArr = new byte[]{(byte) 16, (byte) 4, (byte) 1};
        bArr[2] = b;
        BT_Write(bArr, 3);
        return BT_read();
    }

    public byte StatusInquiryFinish() {
        BT_Write(new byte[]{(byte) 31, (byte) 97, (byte) 1}, 3);
        return BT_read();
    }

    public void StatusInquiryFinish(int i, Handler handler) {
        if (this.mState == 3) {
            Bundle bundle = new Bundle();
            bundle.putInt("flag", i);
            bundle.putInt("state", 0);
            Message message = new Message();
            message.setData(bundle);
            handler.sendMessage(message);
            SystemClock.sleep(1500);
            BT_Write(new byte[]{(byte) 31, (byte) 97, (byte) 1}, 3);
            new ReadStateTask(i, handler).execute(new Void[0]);
        }
    }

    public void SetRightSpacing(byte b) {
        BT_Write(new byte[]{(byte) 27, (byte) 32, b});
    }

    public void SetAbsolutePrintPosition(byte b, byte b2) {
        BT_Write(new byte[]{(byte) 27, (byte) 36, b, b2});
    }

    public void SetRelativePrintPosition(byte b, byte b2) {
        BT_Write(new byte[]{(byte) 27, (byte) 92, b, b2});
    }

    public void SetDefaultLineSpacing() {
        BT_Write(new byte[]{(byte) 27, (byte) 50});
    }

    public void SetLineSpacing(byte b) {
        BT_Write(new byte[]{(byte) 27, (byte) 51, b});
    }

    public void SetLeftStartSpacing(byte b, byte b2) {
        BT_Write(new byte[]{(byte) 29, (byte) 76, b, b2});
    }

    public void SetAreaWidth(byte b, byte b2) {
        BT_Write(new byte[]{(byte) 29, (byte) 87, b, b2});
    }

    public void SetCharacterPrintMode(byte b) {
        BT_Write(new byte[]{(byte) 27, (byte) 33, b});
    }

    public void SetUnderline(byte b) {
        BT_Write(new byte[]{(byte) 27, (byte) 45, b});
    }

    public void SetBold(byte b) {
        BT_Write(new byte[]{(byte) 27, (byte) 69, b});
    }

    public void SetCharacterFont(byte b) {
        BT_Write(new byte[]{(byte) 27, (byte) 77, b});
    }

    public void SetRotate(byte b) {
        BT_Write(new byte[]{(byte) 27, (byte) 86, b});
    }

    public void SetPrintRotate(byte b) {
        BT_Write(new byte[]{(byte) 31, (byte) 106, b});
    }

    public void SetAlignMode(byte b) {
        BT_Write(new byte[]{(byte) 27, (byte) 97, b});
    }

    public void SetInvertPrint(byte b) {
        BT_Write(new byte[]{(byte) 27, (byte) 123, b});
    }

    public void SetFontEnlarge(byte b) {
        BT_Write(new byte[]{(byte) 29, (byte) 33, b});
    }

    public void SetBlackReversePrint(byte b) {
        BT_Write(new byte[]{(byte) 29, (byte) 66, b});
    }

    public void SetChineseCharacterMode(byte b) {
        BT_Write(new byte[]{(byte) 28, (byte) 33, b});
    }

    public void SelChineseCodepage() {
        BT_Write(new byte[]{(byte) 28, (byte) 38});
    }

    public void CancelChineseCodepage() {
        BT_Write(new byte[]{(byte) 28, (byte) 46});
    }

    public void SetChineseUnderline(byte b) {
        BT_Write(new byte[]{(byte) 28, (byte) 45, b});
    }

    public void OpenDrawer(byte b, byte b2, byte b3) {
        BT_Write(new byte[]{(byte) 27, (byte) 112, b, b2, b3});
    }

    public void CutPaper() {
        BT_Write(new byte[]{(byte) 27, (byte) 105});
    }

    public void PartialCutPaper() {
        BT_Write(new byte[]{(byte) 27, (byte) 109});
    }

    public void FeedAndCutPaper(byte b) {
        BT_Write(new byte[]{(byte) 29, (byte) 86, b});
    }

    public void FeedAndCutPaper(byte b, byte b2) {
        BT_Write(new byte[]{(byte) 29, (byte) 86, b, b2});
    }

    public void SetHRIPosition(byte b) {
        BT_Write(new byte[]{(byte) 29, (byte) 72, b});
    }

    public void SetCodeMarginLeft(byte b, byte b2) {
        BT_Write(new byte[]{(byte) 29, (byte) 120, b, b2});
    }

    public void AddCodePrint(BarcodeType barcodeType, String str) {
        switch (barcodeType) {
            case UPC_A:
                UPCA(str);
                return;
            case UPC_E:
                UPCE(str);
                return;
            case EAN13:
                EAN13(str);
                return;
            case EAN8:
                EAN8(str);
                return;
            case CODE39:
                CODE39(str);
                return;
            case ITF:
                ITF(str);
                return;
            case CODABAR:
                CODEBAR(str);
                return;
            case CODE93:
                CODE93(str);
                return;
            case CODE128:
                Code128(str);
                return;
            case QR_CODE:
                CODE_QR_CODE(str);
                return;
            default:
                return;
        }
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

    public void UPCA(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length + 4)];
        bArr[0] = (byte) 29;
        bArr[1] = (byte) 107;
        bArr[2] = (byte) 0;
        int i = 0;
        while (i < length) {
            if (str.charAt(i) <= '9' && str.charAt(i) >= '0') {
                i++;
            } else {
                return;
            }
        }
        if (length <= 30) {
            i = 3;
            int i2 = 0;
            while (i2 < length) {
                int i3 = i + 1;
                bArr[i] = (byte) str.charAt(i2);
                i2++;
                i = i3;
            }
            i2 = i + 1;
            bArr[i] = (byte) 0;
            BT_Write(bArr);
        }
    }

    public void UPCE(String str) {
        int i = 0;
        int length = str.length();
        byte[] bArr = new byte[(length + 4)];
        bArr[0] = (byte) 29;
        bArr[1] = (byte) 107;
        int i2 = 3;
        bArr[2] = (byte) 1;
        int i3 = 0;
        while (i3 < length) {
            if (str.charAt(i3) <= '9' && str.charAt(i3) >= '0') {
                i3++;
            } else {
                return;
            }
        }
        if (length <= 30) {
            while (i < length) {
                i3 = i2 + 1;
                bArr[i2] = (byte) str.charAt(i);
                i++;
                i2 = i3;
            }
            BT_Write(bArr);
        }
    }

    public void EAN13(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length + 4)];
        bArr[0] = (byte) 29;
        bArr[1] = (byte) 107;
        bArr[2] = (byte) 2;
        int i = 0;
        while (i < length) {
            if (str.charAt(i) <= '9' && str.charAt(i) >= '0') {
                i++;
            } else {
                return;
            }
        }
        if (length <= 30) {
            i = 3;
            int i2 = 0;
            while (i2 < length) {
                int i3 = i + 1;
                bArr[i] = (byte) str.charAt(i2);
                i2++;
                i = i3;
            }
            i2 = i + 1;
            bArr[i] = (byte) 0;
            BT_Write(bArr);
        }
    }

    public void EAN8(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length + 4)];
        bArr[0] = (byte) 29;
        bArr[1] = (byte) 107;
        bArr[2] = (byte) 3;
        int i = 0;
        while (i < length) {
            if (str.charAt(i) <= '9' && str.charAt(i) >= '0') {
                i++;
            } else {
                return;
            }
        }
        if (length <= 30) {
            i = 3;
            int i2 = 0;
            while (i2 < length) {
                int i3 = i + 1;
                bArr[i] = (byte) str.charAt(i2);
                i2++;
                i = i3;
            }
            i2 = i + 1;
            bArr[i] = (byte) 0;
            BT_Write(bArr);
        }
    }

    public void CODE39(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length + 4)];
        bArr[0] = (byte) 29;
        bArr[1] = (byte) 107;
        bArr[2] = (byte) 4;
        int i = 0;
        while (i < length) {
            if (str.charAt(i) <= '' && str.charAt(i) >= ' ') {
                i++;
            } else {
                return;
            }
        }
        if (length <= 30) {
            i = 3;
            int i2 = 0;
            while (i2 < length) {
                int i3 = i + 1;
                bArr[i] = (byte) str.charAt(i2);
                i2++;
                i = i3;
            }
            i2 = i + 1;
            bArr[i] = (byte) 0;
            BT_Write(bArr);
        }
    }

    public void ITF(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length + 4)];
        bArr[0] = (byte) 29;
        bArr[1] = (byte) 107;
        bArr[2] = (byte) 5;
        int i = 0;
        while (i < length) {
            if (str.charAt(i) <= '9' && str.charAt(i) >= '0') {
                i++;
            } else {
                return;
            }
        }
        if (length <= 30) {
            i = 3;
            int i2 = 0;
            while (i2 < length) {
                int i3 = i + 1;
                bArr[i] = (byte) str.charAt(i2);
                i2++;
                i = i3;
            }
            i2 = i + 1;
            bArr[i] = (byte) 0;
            BT_Write(bArr);
        }
    }

    public void CODEBAR(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length + 4)];
        bArr[0] = (byte) 29;
        bArr[1] = (byte) 107;
        bArr[2] = (byte) 6;
        int i = 0;
        while (i < length) {
            if (str.charAt(i) <= '' && str.charAt(i) >= ' ') {
                i++;
            } else {
                return;
            }
        }
        if (length <= 30) {
            i = 3;
            int i2 = 0;
            while (i2 < length) {
                int i3 = i + 1;
                bArr[i] = (byte) str.charAt(i2);
                i2++;
                i = i3;
            }
            i2 = i + 1;
            bArr[i] = (byte) 0;
            BT_Write(bArr);
        }
    }

    public void CODE93(String str) {
        int i = 0;
        int length = str.length();
        byte[] bArr = new byte[(length + 3)];
        bArr[0] = (byte) 29;
        bArr[1] = (byte) 107;
        int i2 = 3;
        bArr[2] = (byte) 7;
        int i3 = 0;
        while (i3 < length) {
            if (str.charAt(i3) <= '' && str.charAt(i3) >= ' ') {
                i3++;
            } else {
                return;
            }
        }
        if (length <= 30) {
            while (i < length) {
                i3 = i2 + 1;
                bArr[i2] = (byte) str.charAt(i);
                i++;
                i2 = i3;
            }
            BT_Write(bArr);
        }
    }

    private void Code128(String str) {
        int i = 0;
        byte[] bArr = new byte[(str.length() + 4)];
        bArr[0] = (byte) 29;
        bArr[1] = (byte) 107;
        bArr[2] = (byte) 73;
        int i2 = 4;
        bArr[3] = (byte) str.length();
        while (i < str.length()) {
            int i3 = i2 + 1;
            bArr[i2] = (byte) str.charAt(i);
            i++;
            i2 = i3;
        }
        BT_Write(bArr);
    }

    public void Code128_B(String str) {
        int length = str.length();
        byte[] bArr = new byte[1024];
        bArr[0] = (byte) 29;
        bArr[1] = (byte) 107;
        bArr[2] = (byte) 73;
        bArr[4] = (byte) 123;
        bArr[5] = (byte) 66;
        int i = 0;
        while (i < length) {
            if (str.charAt(i) <= '' && str.charAt(i) >= ' ') {
                i++;
            } else {
                return;
            }
        }
        if (length <= 42) {
            int i2;
            int i3 = 0;
            int i4 = 6;
            int i5 = 0;
            while (i3 < length) {
                i = i4 + 1;
                bArr[i4] = (byte) str.charAt(i3);
                if (str.charAt(i3) == '{') {
                    i2 = i + 1;
                    bArr[i] = (byte) str.charAt(i3);
                    i = i5 + 1;
                } else {
                    i2 = i;
                    i = i5;
                }
                i3++;
                i4 = i2;
                i5 = i;
            }
            i = 1;
            int i6 = 104;
            i2 = 0;
            while (i2 < length) {
                i2++;
                i6 = (i * (str.charAt(i2) - 32)) + i6;
                i++;
            }
            i2 = i6 % 103;
            if (i2 >= 0 && i2 <= 95) {
                i = i4 + 1;
                bArr[i4] = (byte) (i2 + 32);
                bArr[3] = (byte) ((length + 3) + i5);
            } else if (i2 == 96) {
                i2 = i4 + 1;
                bArr[i4] = (byte) 123;
                i = i2 + 1;
                bArr[i2] = (byte) 51;
                bArr[3] = (byte) ((length + 4) + i5);
            } else if (i2 == 97) {
                i2 = i4 + 1;
                bArr[i4] = (byte) 123;
                i = i2 + 1;
                bArr[i2] = (byte) 50;
                bArr[3] = (byte) ((length + 4) + i5);
            } else if (i2 == 98) {
                i2 = i4 + 1;
                bArr[i4] = (byte) 123;
                i = i2 + 1;
                bArr[i2] = (byte) 83;
                bArr[3] = (byte) ((length + 4) + i5);
            } else if (i2 == 99) {
                i2 = i4 + 1;
                bArr[i4] = (byte) 123;
                i = i2 + 1;
                bArr[i2] = (byte) 67;
                bArr[3] = (byte) ((length + 4) + i5);
            } else if (i2 == 100) {
                i2 = i4 + 1;
                bArr[i4] = (byte) 123;
                i = i2 + 1;
                bArr[i2] = (byte) 52;
                bArr[3] = (byte) ((length + 4) + i5);
            } else if (i2 == 101) {
                i2 = i4 + 1;
                bArr[i4] = (byte) 123;
                i = i2 + 1;
                bArr[i2] = (byte) 65;
                bArr[3] = (byte) ((length + 4) + i5);
            } else if (i2 == 102) {
                i2 = i4 + 1;
                bArr[i4] = (byte) 123;
                i = i2 + 1;
                bArr[i2] = (byte) 49;
                bArr[3] = (byte) ((length + 4) + i5);
            }
            BT_Write(bArr);
        }
    }

    private void CODE_QR_CODE(String str) {
        BT_Write(new byte[]{(byte) 29, (byte) 40, (byte) 107, (byte) 3, (byte) 0, (byte) 49, (byte) 67, (byte) 7});
        BT_Write(new byte[]{(byte) 29, (byte) 40, (byte) 107, (byte) 3, (byte) 0, (byte) 49, (byte) 69, (byte) 48});
        byte[] bytes = str.getBytes();
        byte length = (byte) ((bytes.length + 3) % 256);
        byte length2 = (byte) ((bytes.length + 3) / 256);
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(bytes.length + 8);
        allocateDirect.put(new byte[]{(byte) 29, (byte) 40, (byte) 107, length, length2, (byte) 49, (byte) 80, (byte) 48}, 0, 8);
        allocateDirect.put(bytes, 0, bytes.length);
        BT_Write(allocateDirect.array());
        BT_Write(new byte[]{(byte) 27, (byte) 97, (byte) 0});
        BT_Write(new byte[]{(byte) 27, (byte) 97, (byte) 1});
        BT_Write(new byte[]{(byte) 29, (byte) 40, (byte) 107, (byte) 3, (byte) 0, (byte) 49, (byte) 81, (byte) 48});
    }

    public void printString(String str) {
        try {
            BT_Write(str.getBytes(this.mCharsetName));
            BT_Write(new byte[]{(byte) 10});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printParameterSet(byte[] bArr) {
        BT_Write(bArr);
    }

    public void printByteData(byte[] bArr) {
        BT_Write(bArr);
        BT_Write(new byte[]{(byte) 10});
    }

    public boolean defineNVLogo(String[] strArr, int i) {
        if (this.mDefineNVLogoTask != null && this.mDefineNVLogoTask.getStatus() == Status.RUNNING) {
            return false;
        }
        if (this.mPrintImageTask != null && this.mPrintImageTask.getStatus() == Status.RUNNING) {
            return false;
        }
        BT_Write(new byte[]{(byte) 28, (byte) 113, (byte) strArr.length});
        for (int i2 = 0; i2 < strArr.length; i2++) {
            Bitmap returnBitmap = BitmapConvertUtil.returnBitmap(strArr[i2]);
            if (returnBitmap == null) {
                Log.e("logolist============", strArr[i2]);
                return false;
            }
            byte[] nVLogoByte = BitmapConvertUtil.getNVLogoByte(BitmapConvertUtil.decodeSampledBitmapFromBitmap(returnBitmap, i == 0 ? 384 : 576, 4000));
            BT_Write(new byte[]{(byte) (((returnBitmap.getWidth() + 7) / 8) % 256), (byte) (((returnBitmap.getWidth() + 7) / 8) / 256), (byte) (((returnBitmap.getHeight() + 7) / 8) % 256), (byte) (((returnBitmap.getHeight() + 7) / 8) / 256)});
            BT_Write(nVLogoByte);
            Log.e("HsBluetoothPrintDriver", "define nvlogo finished ============" + strArr[i2]);
        }
        Log.d("HsBluetoothPrintDriver", "defineNVLogo ----------");
        return true;
    }

    public void printNVLogo(byte b, int i) {
        BT_Write(new byte[]{(byte) 28, (byte) 112, b, (byte) 0});
    }

    public boolean printImage(Bitmap bitmap, int i) {
        if (this.mPrintImageTask != null && this.mPrintImageTask.getStatus() == Status.RUNNING) {
            return false;
        }
        if (this.mDefineNVLogoTask != null && this.mDefineNVLogoTask.getStatus() == Status.RUNNING) {
            return false;
        }
        this.mPrintImageTask = new PrintImageTask(bitmap, i);
        this.mPrintImageTask.execute(new Void[0]);
        return true;
    }
}
