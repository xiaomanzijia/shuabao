package com.jhlc.km.sb.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by licheng on 28/3/16.
 */
public class CategoryBean {
    @SerializedName("id")
    private String id;
    @SerializedName("logourl")
    private String logourl;
    @SerializedName("name")
    private String name;
    @SerializedName("orderindex")
    private String orderindex;
    @SerializedName("status")
    private String status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogourl() {
        return logourl;
    }

    public void setLogourl(String logourl) {
        this.logourl = logourl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderindex() {
        return orderindex;
    }

    public void setOrderindex(String orderindex) {
        this.orderindex = orderindex;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
