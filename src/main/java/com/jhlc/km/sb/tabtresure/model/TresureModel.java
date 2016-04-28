package com.jhlc.km.sb.tabtresure.model;

import com.jhlc.km.sb.volley.RequestParams;

import java.util.Map;

/**
 * Created by licheng on 21/3/16.
 */
public interface TresureModel {

    void loadTresure(String url, RequestParams params, TresureMdolImpl.onLoadListener listener);

}
