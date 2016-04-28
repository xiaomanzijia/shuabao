package com.jhlc.km.sb.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by licheng on 1/3/16.
 */
public class MobileCheckModel extends BaseModel {
    @SerializedName("checkflag")
    private String checkflag;

    public String getCheckflag() {
        return checkflag;
    }

    public void setCheckflag(String checkflag) {
        this.checkflag = checkflag;
    }
}
