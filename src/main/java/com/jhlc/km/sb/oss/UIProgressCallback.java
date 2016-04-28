package com.jhlc.km.sb.oss;

import android.util.Log;

import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;

/**
 * Created by Administrator on 2015/12/22 0022.
 */
public class UIProgressCallback<T> implements OSSProgressCallback<T> {
    private UIDispatcher UIDispatcher;
    private CallbackGroup callback = new CallbackGroup();

    public UIProgressCallback(UIDispatcher UIDispatcher) {
        this.UIDispatcher = UIDispatcher;
    }
    public UIProgressCallback<T> addCallback(Runnable r) {
        callback.add(r);
        return this;
    }

    public void onProgress(T request, long currentSize, long totalSize) {
        Log.d("UIProgressCallback", "Run");
        UIDispatcher.obtainMessage(UIDispatcher.UI_MESSAGE, callback).sendToTarget();
    }
}
