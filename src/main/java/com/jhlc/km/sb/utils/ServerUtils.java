package com.jhlc.km.sb.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.jhlc.km.sb.common.Des3;
import com.jhlc.km.sb.volley.RequestParams;

import java.util.Map;


/**
 * Created by licheng on 28/3/16.
 */
public class ServerUtils {


    //对请求字段做全文加密
    public static RequestParams getRequestParams(Map<String,String> param, String TAG){
        String registJson = new Gson().toJson(param);
        Log.i(TAG,registJson);
        String registJsonEnCode = null;
        try {
            registJsonEnCode = Des3.encode(registJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG,registJsonEnCode);
        RequestParams params = new RequestParams();
        params.put("s", registJsonEnCode);
        return params;
    }

//    //对返回字段做全文解密
//    public static String getResposeBody(Response reponse, String TAG){
//        String responseBody = reponse.body().toString();
//        Log.i(TAG,responseBody);
//        SModel sModel = new Gson().fromJson(responseBody,SModel.class);
//        String respose = null;
//        try {
//            respose = Des3.decode(sModel.getS());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return respose;
//    }
}
