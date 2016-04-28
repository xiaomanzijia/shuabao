package com.jhlc.km.sb.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by licheng on 1/3/16.
 */
public class SModel {
    @SerializedName("s")
    private String s;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}
