package com.jhlc.km.sb.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhlc.km.sb.R;
import com.jhlc.km.sb.adapter.CommentThumbAdapter;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by licheng on 24/3/16.
 */
public class CommentThumbActivity extends BaseActivity {

    @Bind(R.id.llBack)
    LinearLayout llBack;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.btnBack)
    ImageView btnBack;
    @Bind(R.id.textBack)
    TextView textBack;


    @Override
    public void initView() {
        super.initView();
        FragmentPagerAdapter adapter = new CommentThumbAdapter(getSupportFragmentManager(),getApplicationContext());
        viewpager.setAdapter(adapter);
        //解决多个frament切换被实例化  参数即是fragment数量
        viewpager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewpager);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_thumb_layout);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick(R.id.llBack)
    public void onClick() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }



    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
