package com.jhlc.km.sb.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by licheng on 31/3/16.
 */
public class CommentBean {
    @SerializedName("anitqueid")
    private String anitqueid;
    @SerializedName("content")
    private String content;
    @SerializedName("createdate")
    private String createdate;
    @SerializedName("headimgurl")
    private String headimgurl;
    @SerializedName("id")
    private String id;
    @SerializedName("likecount")
    private String likecount;
    @SerializedName("parentid")
    private String parentid;
    @SerializedName("sonList")
    private List<CommentBean> sonList;
    @SerializedName("strdate")
    private String strdate;
    @SerializedName("userid")
    private String userid;
    @SerializedName("username")
    private String username;


    public String getAnitqueid() {
        return anitqueid;
    }

    public void setAnitqueid(String anitqueid) {
        this.anitqueid = anitqueid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLikecount() {
        return likecount;
    }

    public void setLikecount(String likecount) {
        this.likecount = likecount;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public List<CommentBean> getSonList() {
        return sonList;
    }

    public void setSonList(List<CommentBean> sonList) {
        this.sonList = sonList;
    }

    public String getStrdate() {
        return strdate;
    }

    public void setStrdate(String strdate) {
        this.strdate = strdate;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
