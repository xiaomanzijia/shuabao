package com.jhlc.km.sb.antiquedetail.presenter;

import android.content.Context;

import com.jhlc.km.sb.antiquedetail.model.AntiqueDetailModel;
import com.jhlc.km.sb.antiquedetail.model.AntiqueDetailModelImpl;
import com.jhlc.km.sb.antiquedetail.view.AntiqueDetailView;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.model.AntiqueDetailServerModel;
import com.jhlc.km.sb.utils.ServerUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by licheng on 31/3/16.
 */
public class AntiqueDetailPresenterImpl implements AntiqueDetailPresenter,AntiqueDetailModelImpl.onLoadListener {

    private AntiqueDetailView view;
    private AntiqueDetailModel model;
    private Context mContext;
    private final static String TAG = "AntiqueDetailPresenterImpl";

    public AntiqueDetailPresenterImpl(Context mContext, AntiqueDetailView view) {
        this.mContext = mContext;
        this.view = view;
        model = new AntiqueDetailModelImpl(mContext);
    }

    @Override
    public void loadAntiqueDeatial(int pageIndex, int pageSize, int id, String userid) {
        Map<String,String> params = new HashMap<>();
        params.put("op", Constants.INTERFACE_GET_ANTIQUEDETAIL);
        params.put("pageNum", String.valueOf(pageIndex));
        params.put("numPerPage", String.valueOf(pageSize));
        params.put("id", String.valueOf(id));
        params.put("userid", userid);
        //只有第一页加载才显示进度条
        if(pageIndex == 0 || pageIndex == 1){
            view.showProgress();
        }
        model.loadAntiqueDetail(Constants.SERVER_URL, ServerUtils.getRequestParams(params,TAG),this);
    }

    @Override
    public void onSuccess(AntiqueDetailServerModel antiqueDetailModel) {
        view.hideProgress();
        if(antiqueDetailModel != null){
            view.addTresureDetail(antiqueDetailModel);
        }
    }

    @Override
    public void onFailure(String msg, Exception e) {
        view.hideProgress();
        view.showLoadFailMsg(msg,e);
    }
}
