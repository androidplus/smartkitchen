package com.smart.kitchen.smartkitchen.utils;

import android.content.Context;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.smart.kitchen.smartkitchen.R;
import com.smart.kitchen.smartkitchen.entitys.MessageCenter;

public class SoundUtils {
    private static final String TAG = "SoundUtils";
    private static Context context;
    static Handler haldler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            SoundUtils.streamId = SoundUtils.sp.play(SoundUtils.souceId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    };
    private static SoundUtils instance = null;
    private static int souceId;
    private static SoundPool sp;
    private static int streamId;

    public static synchronized SoundUtils getInstance(Context context) {
        SoundUtils soundUtils;
        synchronized (SoundUtils.class) {
            if (instance == null) {
                instance = new SoundUtils(context);
            }
            soundUtils = instance;
        }
        return soundUtils;
    }

    public void init() {
        initSound();
    }

    private SoundUtils(Context context) {
        this.context = context;
    }

    private void initSound() {
        sp = new SoundPool(1, 1, 5);
        souceId = sp.load(context, R.raw.tips, 1);
    }

    public static void playSound() {
        LogUtils.e(TAG, "playSound: ");
        haldler.sendEmptyMessage(0);
    }

    public static void pauseSound() {
        sp.stop(souceId);
    }

    public static void stop() {
        if (sp != null) {
            pauseSound();
            sp = null;
        }
    }

    public static void messageNew(String str) {
        playSound();
        if (!Contants.isEmpty(str)) {
            int intValue = ((MessageCenter) JSON.parseObject(str, new TypeReference<MessageCenter>() {
            }, new Feature[0])).getMsg_type().intValue();
            if (intValue == 0 || intValue == 1 || intValue == 3 || intValue == 4) {
                if (!SPUtils.getUserinfos2(context, SPUtils.SETTING_TAKEOUTNEW)) {
                }
            } else if (intValue == 5 && SPUtils.getUserinfos2(context, SPUtils.SETTING_PAYMENTSUCCEED)) {
                playSound();
            }
        }
    }
}
