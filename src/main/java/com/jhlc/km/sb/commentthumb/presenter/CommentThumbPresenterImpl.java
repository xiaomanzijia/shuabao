package com.jhlc.km.sb.commentthumb.presenter;

import android.content.Context;

import com.jhlc.km.sb.commentthumb.model.CommentThumbModel;
import com.jhlc.km.sb.commentthumb.model.CommentThumbModelImpl;
import com.jhlc.km.sb.commentthumb.view.CommentThumbView;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.model.CommentThumbBean;
import com.jhlc.km.sb.utils.PreferencesUtils;
import com.jhlc.km.sb.utils.ServerUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by licheng on 6/4/16.
 */
public class CommentThumbPresenterImpl implements CommentThumbPresenter,CommentThumbModelImpl.onLoadListener {

    private CommentThumbView view;
    private Context mContext;
    private CommentThumbModel model;
    private final static String TAG = "CommentThumbPresenterImpl";

    public CommentThumbPresenterImpl(Context mContext, CommentThumbView view) {
        this.mContext = mContext;
        this.view = view;
        model = new CommentThumbModelImpl(mContext);
    }

    @Override
    public void loadCommentThumbList(String interfaceName, int pageNum, int numPerPage) {
        Map<String,String> params = new HashMap<>();
        params.put("op", interfaceName);
        params.put("userid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        params.put("pageNum", String.valueOf(pageNum));
        params.put("numPerPage", String.valueOf(numPerPage));
        //只有第一页加载才显示进度条
        if(pageNum == 0 || pageNum == 1){
            view.showProgress();
        }
        model.loadCommentThumbList(Constants.SERVER_URL, ServerUtils.getRequestParams(params,TAG),this);
    }

    @Override
    public void onSuccess(List<CommentThumbBean> commentThumbBeanList) {
        view.hideProgress();
        if(commentThumbBeanList != null){
            view.addCommentThumb(commentThumbBeanList);
        }
    }

    @Override
    public void onFailure(String msg, Exception e) {
        view.hideProgress();
        view.showLoadFailMsg(msg, e);
    }
}
