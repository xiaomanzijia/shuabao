package com.jhlc.km.sb.tabtresure.view;

import com.jhlc.km.sb.model.AntiqueBean;
import com.jhlc.km.sb.model.Beauty;

import java.util.List;

/**
 * Created by licheng on 21/3/16.
 */
public interface TresureView {
    void showProgress();

    void addTresure(List<AntiqueBean> antiqueBeanList);

    void hideProgress();

    void showLoadFailMsg();
}
