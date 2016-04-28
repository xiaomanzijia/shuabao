package com.jhlc.km.sb.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jhlc.km.sb.R;
import com.jhlc.km.sb.activity.LoginActivity;
import com.jhlc.km.sb.activity.SeachActivity;
import com.jhlc.km.sb.adapter.PopWindowApater;
import com.jhlc.km.sb.adapter.TabPageAdapter;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.model.CategoryBean;
import com.jhlc.km.sb.utils.PreferencesUtils;
import com.jhlc.km.sb.utils.ScreenUtils;
import com.jhlc.km.sb.utils.SqliteUtils;
import com.jhlc.km.sb.utils.ToastUtils;
import com.jhlc.km.sb.utils.WidgetUtils;
import com.jhlc.km.sb.view.SearchPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by licheng on 21/3/16.
 */
public class ShuaBaoFragment extends Fragment implements SearchPopupWindow.onClickListener{
    private String str;
    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private ImageButton ibtnSearch;
    private ImageButton ibtnMenu;
    private List<CategoryBean> menuList;
    private SearchPopupWindow popupWindow;

    private SqliteUtils sqliteUtils;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        str = getArguments().getString("whichtab");
        sqliteUtils = new SqliteUtils(getActivity());
        menuList = sqliteUtils.getCategoryList();

//        popupWindow = SearchPopupWindow.getInstance(getActivity(),menuList);
        popupWindow = new SearchPopupWindow(getActivity(),menuList);
        popupWindow.setListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_new,null);
        mTablayout = (TabLayout) view.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) view.findViewById(R.id.tab_viewpager);
        ibtnMenu = (ImageButton) view.findViewById(R.id.ibtnMenu);
        ibtnSearch = (ImageButton) view.findViewById(R.id.ibtnSearch);

        FragmentPagerAdapter adapter = new TabPageAdapter(getChildFragmentManager(),getActivity());
        mViewPager.setAdapter(adapter);
        //解决多个frament切换被实例化  参数即是fragment数量
        mViewPager.setOffscreenPageLimit(3);
        mTablayout.setupWithViewPager(mViewPager);

        onClick();

        return view;
    }

    private void onClick() {
        ibtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PreferencesUtils.getBoolean(getActivity(),Constants.PREFERENCES_IS_LOGIN)){
                    if(popupWindow.isShowing()){
                        popupWindow.dismiss();
                    }else {
                        popupWindow.showAsDropDown(v);
                    }
                }else {
                    Intent login = new Intent(getActivity(), LoginActivity.class);
                    startActivity(login);
                }

            }
        });
        ibtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SeachActivity.class);
                intent.putExtra(Constants.INTENT_TYPE_SEARCH,Constants.INTENT_TYPE_SEARCH_BTN);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(int position, String menuTitle,String menuId) {
        Intent intent = new Intent(getActivity(), SeachActivity.class);
        intent.putExtra(Constants.INTENT_TYPE_SEARCH,Constants.INTENT_TYPE_MENU_CONDITION);
        intent.putExtra(Constants.INTENT_SEARCH_CONDITION_TYPE,menuTitle);
        startActivity(intent);
        if(popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }
}
