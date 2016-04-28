package com.jhlc.km.sb.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by licheng on 28/3/16.
 */
public class AntiqueBean {
    @SerializedName("catagoryid")
    private String catagoryid;
    @SerializedName("commentcount")
    private String commentcount;
    @SerializedName("createdate")
    private String createdate;
    @SerializedName("createuserid")
    private String createuserid;
    @SerializedName("describe")
    private String describe;
    @SerializedName("headimgurl")
    private String headimgurl;
    @SerializedName("id")
    private String id;
    @SerializedName("indeximage")
    private String indeximage;
    @SerializedName("likecount")
    private String likecount;
    @SerializedName("name")
    private String name;
    @SerializedName("price")
    private String price;
    @SerializedName("status")
    private String status;
    @SerializedName("username")
    private String userName;


    public String getCatagoryid() {
        return catagoryid;
    }

    public void setCatagoryid(String catagoryid) {
        this.catagoryid = catagoryid;
    }

    public String getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(String commentcount) {
        this.commentcount = commentcount;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(String createuserid) {
        this.createuserid = createuserid;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
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

    public String getIndeximage() {
        return indeximage;
    }

    public void setIndeximage(String indeximage) {
        this.indeximage = indeximage;
    }

    public String getLikecount() {
        return likecount;
    }

    public void setLikecount(String likecount) {
        this.likecount = likecount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
