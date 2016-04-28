package com.jhlc.km.sb.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by licheng on 29/3/16.
 */
public class UserModel extends BaseModel{
    @SerializedName("user")
    private UserBean userBean;
    @SerializedName("userinfo")
    private UserBean userinfo;
    @SerializedName("catagorylist")
    private List<CategoryBean> categoryBeanList;
    @SerializedName("followflag")
    private int followflag;

    public int getFollowflag() {
        return followflag;
    }

    public void setFollowflag(int followflag) {
        this.followflag = followflag;
    }

    public UserBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserBean userinfo) {
        this.userinfo = userinfo;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public List<CategoryBean> getCategoryBeanList() {
        return categoryBeanList;
    }

    public void setCategoryBeanList(List<CategoryBean> categoryBeanList) {
        this.categoryBeanList = categoryBeanList;
    }
}
