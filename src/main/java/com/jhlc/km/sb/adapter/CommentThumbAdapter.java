package com.jhlc.km.sb.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jhlc.km.sb.R;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.fragment.TabCommentThumbFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by licheng on 18/2/16.
 */
public class CommentThumbAdapter extends FragmentPagerAdapter {

    private String[] TITLE = null;
    private Context mContext;
    private List<Fragment> fragmentList;

    public CommentThumbAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        TITLE = mContext.getResources().getStringArray(R.array.tab_item_name_comment_thumb);
        fragmentList = new ArrayList<>();
        fragmentList.add(new TabCommentThumbFragment());
        fragmentList.add(new TabCommentThumbFragment());
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.INTENT_FRAGMENT_COMMENTTHUMB_TYPE,TITLE[position]);
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
