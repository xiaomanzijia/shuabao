package com.jhlc.km.sb.antiquedetail.model;

import com.jhlc.km.sb.volley.RequestParams;

/**
 * Created by licheng on 31/3/16.
 */
public interface AntiqueDetailModel {
    void loadAntiqueDetail(String url, RequestParams params,AntiqueDetailModelImpl.onLoadListener listener);
}
