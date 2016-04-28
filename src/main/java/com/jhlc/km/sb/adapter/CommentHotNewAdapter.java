package com.jhlc.km.sb.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jhlc.km.sb.R;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.fragment.TabCommentHotNewFragment;
import com.jhlc.km.sb.fragment.TabCommentThumbFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by licheng on 18/2/16.
 */
public class CommentHotNewAdapter extends FragmentPagerAdapter {

    private String[] TITLE = null;
    private Context mContext;
    private List<Fragment> fragmentList;
    private String antiqueid;

    public CommentHotNewAdapter(FragmentManager fm, Context context,String antiqueid) {
        super(fm);
        mContext = context;
        this.antiqueid = antiqueid;
        TITLE = mContext.getResources().getStringArray(R.array.tab_item_name_comment_hot_new);
        fragmentList = new ArrayList<>();
        fragmentList.add(new TabCommentHotNewFragment());
        fragmentList.add(new TabCommentHotNewFragment());
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_FRAGMENT_COMMENTTHUMB_TYPE,TITLE[position]);
        bundle.putString(Constants.INTENT_TRESUREDETAIL_COMMENTHOTNEW_TRESREID,antiqueid);
        fragmentList.get(position).setArguments(bundle);
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLE[position];
    }
}
