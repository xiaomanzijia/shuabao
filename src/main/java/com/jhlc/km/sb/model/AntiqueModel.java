package com.jhlc.km.sb.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by licheng on 29/3/16.
 */
public class AntiqueModel extends BaseModel{
    @SerializedName("alist")
    private List<AntiqueBean> alist;

    public List<AntiqueBean> getAlist() {
        return alist;
    }

    public void setAlist(List<AntiqueBean> alist) {
        this.alist = alist;
    }
}
