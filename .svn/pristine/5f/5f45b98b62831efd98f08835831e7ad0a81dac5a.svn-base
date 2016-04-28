package com.jhlc.km.sb.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jhlc.km.sb.constants.Constants;

/**
 * Created by licheng on 30/3/16.
 */
public class DataRefreshReceiver extends BroadcastReceiver {

    private final static String TAG = "DataRefreshReceiver";

    private RefreshListener refreshListener;

    private static DataRefreshReceiver refreshReceiver;

    private Context mContext;

    public DataRefreshReceiver(Context mContext,RefreshListener listener) {
        this.mContext = mContext;
        this.refreshListener = listener;
    }

    public DataRefreshReceiver(Context mContext) {
        this.mContext = mContext;
    }

    public static synchronized DataRefreshReceiver getInstance(Context context) {
        if (refreshReceiver == null) {
            refreshReceiver = new DataRefreshReceiver(context);
        }
        return refreshReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean ischanged = intent.getBooleanExtra(Constants.BROADCASTRECEIVER_DATA_REFRESH_TRUE,false);
        Log.i(TAG,ischanged+"");
        if(refreshListener != null){
            refreshListener.refresh(ischanged);
        }else {
            Log.i(TAG,"listener is null");
        }
    }

    public interface RefreshListener{
        void refresh(Boolean isChanged);
    }

    public RefreshListener getRefreshListener() {
        return refreshListener;
    }

    public void setRefreshListener(RefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }
}
