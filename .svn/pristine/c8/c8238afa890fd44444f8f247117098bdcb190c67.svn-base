package com.jhlc.km.sb.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jhlc.km.sb.R;
import com.jhlc.km.sb.adapter.PopWindowApater;
import com.jhlc.km.sb.model.CategoryBean;
import com.jhlc.km.sb.utils.ListUtils;
import com.jhlc.km.sb.utils.ScreenUtils;
import com.jhlc.km.sb.utils.WidgetUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by licheng on 31/3/16.
 */
public class SearchPopupWindow extends PopupWindow implements AdapterView.OnItemClickListener{
    private Context mContext;
    private PopWindowApater adapter;
    private List<CategoryBean> menuList = new ArrayList<>();
    private View popview;
    private ListView lvMenus;
    private int lvPopMenuHeight;

    private onClickListener listener;

    public SearchPopupWindow(Context context, List<CategoryBean> menuList) {
        this.mContext = context;
        this.menuList = menuList;
        popview = LayoutInflater.from(mContext).inflate(R.layout.popwindow_layout,null);
        lvMenus = (ListView) popview.findViewById(R.id.lvMenus);
        lvMenus.setOnItemClickListener(this);
        lvMenus.getBackground().setAlpha(240);
        adapter = new PopWindowApater(menuList,mContext);
        lvMenus.setAdapter(adapter);
        setContentView(popview);
        lvPopMenuHeight = WidgetUtils.getTotalHeightofListView(lvMenus);
        setHeight(lvPopMenuHeight);
        setWidth(ScreenUtils.getDeviceWidth(mContext));
        setBackgroundDrawable(new BitmapDrawable());
        setOutsideTouchable(true);
        setFocusable(true);
    }

    private static SearchPopupWindow searchPopupWindow;

    public static SearchPopupWindow getInstance(Context context, List<CategoryBean> menuList){
        if(searchPopupWindow == null){
            searchPopupWindow = new SearchPopupWindow(context, menuList);
        }
        return searchPopupWindow;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(!ListUtils.isEmpty(menuList) && listener != null){
            listener.onClick(position,menuList.get(position).getName(),menuList.get(position).getId());
        }
    }

    public interface onClickListener{
        void onClick(int position,String menuTitle,String menuId);
    }

    public onClickListener getListener() {
        return listener;
    }

    public void setListener(onClickListener listener) {
        this.listener = listener;
    }
}
