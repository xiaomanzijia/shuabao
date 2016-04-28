package com.jhlc.km.sb.commentthumb.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jhlc.km.sb.common.Des3;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.model.CommentThumbBean;
import com.jhlc.km.sb.model.CommentThumbServerModel;
import com.jhlc.km.sb.model.SModel;
import com.jhlc.km.sb.volley.IRequest;
import com.jhlc.km.sb.volley.RequestJsonListener;
import com.jhlc.km.sb.volley.RequestParams;

import java.util.List;

/**
 * Created by licheng on 6/4/16.
 */
public class CommentThumbModelImpl implements CommentThumbModel {

    private final String TAG = "CommentThumbModelImpl";
    private Context mContext;

    public CommentThumbModelImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void loadCommentThumbList(String url, RequestParams params, final onLoadListener listener) {
        IRequest.post(mContext, url, SModel.class, params, new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    CommentThumbServerModel commentThumbServerModel = new Gson().fromJson(jsonBody,CommentThumbServerModel.class);
                    if(Constants.STATUS_SUCCESS.equals(commentThumbServerModel.getStatus())) {
                        listener.onSuccess(commentThumbServerModel.getCommentThumbBeanList());
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
        void onSuccess(List<CommentThumbBean> commentThumbBeanList);
        void onFailure(String msg, Exception e);
    }
}
