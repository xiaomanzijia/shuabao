package com.jhlc.km.sb.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by licheng on 5/4/16.
 */
public class LikeAntiqueCountModel extends BaseModel {
    @SerializedName("antiquecount")
    private String antiquecount;
    @SerializedName("likecount")
    private String likecount;

    public String getAntiquecount() {
        return antiquecount;
    }

    public void setAntiquecount(String antiquecount) {
        this.antiquecount = antiquecount;
    }

    public String getLikecount() {
        return likecount;
    }

    public void setLikecount(String likecount) {
        this.likecount = likecount;
    }
}
