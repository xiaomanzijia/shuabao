package com.jhlc.km.sb.commentthumb.view;

import com.jhlc.km.sb.model.AntiqueBean;
import com.jhlc.km.sb.model.CommentThumbBean;

import java.util.List;

/**
 * Created by licheng on 6/4/16.
 */
public interface CommentThumbView {
    void showProgress();

    void addCommentThumb(List<CommentThumbBean> commentThumbBeanList);

    void hideProgress();

    void showLoadFailMsg(String msg,Exception e);
}
