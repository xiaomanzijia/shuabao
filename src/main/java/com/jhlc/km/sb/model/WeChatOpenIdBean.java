package com.jhlc.km.sb.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by licheng on 11/4/16.
 */
public class WeChatOpenIdBean {
    @SerializedName("access_token")
    private String access_token;
    @SerializedName("expires_in")
    private String expires_in;
    @SerializedName("refresh_token")
    private String refresh_token;
    @SerializedName("openid")
    private String openid;
    @SerializedName("scope")
    private String scope;
    @SerializedName("unionid")
    private String unionid;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
