package com.jhlc.km.sb.tabtresure.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jhlc.km.sb.common.Des3;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.model.AntiqueBean;
import com.jhlc.km.sb.model.AntiqueModel;
import com.jhlc.km.sb.model.BaseModel;
import com.jhlc.km.sb.model.Beauty;
import com.jhlc.km.sb.model.BeautyList;
import com.jhlc.km.sb.model.SModel;
import com.jhlc.km.sb.utils.ServerUtils;
import com.jhlc.km.sb.utils.ToastUtils;
import com.jhlc.km.sb.volley.IRequest;
import com.jhlc.km.sb.volley.RequestJsonListener;
import com.jhlc.km.sb.volley.RequestListener;
import com.jhlc.km.sb.volley.RequestParams;

import java.util.List;
import java.util.Map;


/**
 * Created by licheng on 21/3/16.
 */
public class TresureMdolImpl implements TresureModel {

    private final String TAG = "TresureMdolImpl";
    private Context mContext;

    public TresureMdolImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void loadTresure(String url, RequestParams params,final onLoadListener listener) {
        IRequest.post(mContext, url, SModel.class, params, new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    AntiqueModel antiqueModel = new Gson().fromJson(jsonBody,AntiqueModel.class);
                    if(Constants.STATUS_SUCCESS.equals(antiqueModel.getStatus())) {
                        listener.onSuccess(antiqueModel.getAlist());
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

    public interface onLoadListener {
        void onSuccess(List<AntiqueBean> antiqueBeanList);
        void onFailure(String msg, Exception e);
    }
}
