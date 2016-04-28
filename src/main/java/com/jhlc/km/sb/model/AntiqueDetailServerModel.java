package com.jhlc.km.sb.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by licheng on 29/3/16.
 */
public class AntiqueDetailServerModel extends BaseModel{
    @SerializedName("detail")
    private AntiqueDetailBean antiqueDetailBean;
    @SerializedName("hotcomment")
    private List<CommentBean> commentHotBeanList;
    @SerializedName("latestlist")
    private List<CommentBean> commentLatestBeanList;
    @SerializedName("colflag")
    private String colflag;

    public AntiqueDetailBean getAntiqueDetailBean() {
        return antiqueDetailBean;
    }

    public void setAntiqueDetailBean(AntiqueDetailBean antiqueDetailBean) {
        this.antiqueDetailBean = antiqueDetailBean;
    }

    public List<CommentBean> getCommentHotBeanList() {
        return commentHotBeanList;
    }

    public void setCommentHotBeanList(List<CommentBean> commentHotBeanList) {
        this.commentHotBeanList = commentHotBeanList;
    }

    public List<CommentBean> getCommentLatestBeanList() {
        return commentLatestBeanList;
    }

    public void setCommentLatestBeanList(List<CommentBean> commentLatestBeanList) {
        this.commentLatestBeanList = commentLatestBeanList;
    }

    public String getColflag() {
        return colflag;
    }

    public void setColflag(String colflag) {
        this.colflag = colflag;
    }
}
