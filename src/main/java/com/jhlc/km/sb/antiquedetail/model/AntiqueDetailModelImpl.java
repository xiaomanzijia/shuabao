package com.jhlc.km.sb.antiquedetail.model;


import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jhlc.km.sb.common.Des3;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.model.AntiqueDetailServerModel;
import com.jhlc.km.sb.model.SModel;
import com.jhlc.km.sb.volley.IRequest;
import com.jhlc.km.sb.volley.RequestJsonListener;
import com.jhlc.km.sb.volley.RequestParams;

/**
 * Created by licheng on 31/3/16.
 */
public class AntiqueDetailModelImpl implements AntiqueDetailModel {

    private Context mContext;

    public AntiqueDetailModelImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void loadAntiqueDetail(String url, RequestParams params, final onLoadListener listener) {
        IRequest.post(mContext, url, SModel.class, params, new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i("AntiqueDetailModelImpl",jsonBody);
                    AntiqueDetailServerModel antiqueDetailModel = new Gson().fromJson(jsonBody,AntiqueDetailServerModel.class);
                    if(Constants.STATUS_SUCCESS.equals(antiqueDetailModel.getStatus())){
                        listener.onSuccess(antiqueDetailModel);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                listener.onFailure("load antique failure",e);
            }
        });
    }

    public interface onLoadListener{
        void onSuccess(AntiqueDetailServerModel antiqueDetailModel);
        void onFailure(String msg, Exception e);
    }
}
