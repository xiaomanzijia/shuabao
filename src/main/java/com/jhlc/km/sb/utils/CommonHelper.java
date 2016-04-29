package com.jhlc.km.sb.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.jhlc.km.sb.SbApplication;
import com.jhlc.km.sb.constants.Constants;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;

/**
 * Created by licheng on 29/4/16.
 */
public class CommonHelper {

    private Context mContext;

    public CommonHelper(Context mContext) {
        this.mContext = mContext;
    }

    //分享到微信
    public void shareWeChat(String id, String tresuereName, String tresureIndexImage, int WXScene){
        IWXAPI api = ((SbApplication)mContext.getApplicationContext()).api;

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = Constants.WEB_WECHAT_URL+id;

        WXMediaMessage msgWx = new WXMediaMessage(webpage);
        msgWx.title = tresuereName;
        msgWx.description = tresuereName;

        Bitmap thumb = CommomUtils.getBitmap(tresureIndexImage); //请求网络图片地址转换为bitmap
        msgWx.thumbData = CommomUtils.bmpToByteArray(thumb,true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msgWx;
        req.scene = WXScene; //分享到朋友圈

        api.sendReq(req);
    }

    private  String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
