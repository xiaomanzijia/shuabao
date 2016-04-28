package com.jhlc.km.sb.oss;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


/**
 * Created by OSS on 2015/12/21 0021.
 */
public class UIDispatcher extends Handler{
    public final static int UI_MESSAGE = 1;

    public UIDispatcher(Looper looper) {
        super(looper);
    }

    public void handleMessage(Message msg) {
        if (msg.what == UI_MESSAGE) {
            Log.d("Recive", "UIMessage");
            Runnable c = (Runnable) msg.obj;
            c.run();
        }
        else {
            Log.w("ReciveMessage", String.valueOf(msg.what));
        }
    }
}
