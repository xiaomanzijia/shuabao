package com.jhlc.km.sb;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.jhlc.km.sb.common.LocationService;
import com.jhlc.km.sb.constants.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * Created by licheng on 21/3/16.
 */
public class SbApplication extends Application {

    private static Context mContext;
    public LocationService locationService;
    public Vibrator mVibrator;
    public IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        //Fresco
        Fresco.initialize(this);
        //用于调试
        Stetho.initializeWithDefaults(this);
        mContext = getApplicationContext();

        //百度地图初始化
        locationService = new LocationService(this);
        mVibrator =(Vibrator)this.getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(this);

        //将应用id注册到微信
        api = WXAPIFactory.createWXAPI(this, Constants.WeChat_APP_ID,true);
        api.registerApp(Constants.WeChat_APP_ID);
    }


    public static Context getContext() {
        return mContext;
    }

}
