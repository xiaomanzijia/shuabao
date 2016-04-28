package com.jhlc.km.sb.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by licheng on 11/4/16.
 */
public class WeChatUserInfoBean {
    @SerializedName("openid")
    private String openid;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("sex")
    private String sex;
    @SerializedName("province")
    private String province;
    @SerializedName("city")
    private String city;
    @SerializedName("country")
    private String country;
    @SerializedName("headimgurl")
    private String headimgurl;
    @SerializedName("unionid")
    private String unionid;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
