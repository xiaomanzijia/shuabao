package com.jhlc.km.sb.model;

/**
 * Created by licheng on 8/4/16.
 */
public class ThumbCountBean {
    private int position;
    private int thumbcount;
    private int commenttype;

    public int getCommenttype() {
        return commenttype;
    }

    public void setCommenttype(int commenttype) {
        this.commenttype = commenttype;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getThumbcount() {
        return thumbcount;
    }

    public void setThumbcount(int thumbcount) {
        this.thumbcount = thumbcount;
    }
}
