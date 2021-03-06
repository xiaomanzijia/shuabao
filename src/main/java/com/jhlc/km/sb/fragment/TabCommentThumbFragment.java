package com.jhlc.km.sb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jhlc.km.sb.R;
import com.jhlc.km.sb.adapter.CommentThumbAdapter;
import com.jhlc.km.sb.adapter.TabCommentThumbAdapter;
import com.jhlc.km.sb.commentthumb.presenter.CommentThumbPresenter;
import com.jhlc.km.sb.commentthumb.presenter.CommentThumbPresenterImpl;
import com.jhlc.km.sb.commentthumb.view.CommentThumbView;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.model.CommentThumbBean;
import com.jhlc.km.sb.utils.ListUtils;
import com.jhlc.km.sb.utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by licheng on 21/3/16.
 */
public class TabCommentThumbFragment extends Fragment implements CommentThumbView,SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView tabRecyler;
    private SwipeRefreshLayout tabSwipeRefresh;
    private CommentThumbPresenter presenter;
    private int pageSize = 8;
    private int pageIndex = 1;
    private String fragementType;
    private TabCommentThumbAdapter adapter;
    private List<CommentThumbBean> commentThumbBeanList;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem = 0;
    private boolean isLastPage = false;
    private final static String TAG = "TabCommentThumbFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragementType = getArguments().getString(Constants.INTENT_FRAGMENT_COMMENTTHUMB_TYPE);
        presenter = new CommentThumbPresenterImpl(getActivity(),this);
        commentThumbBeanList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment_thumb_layout, null);

        tabRecyler = (RecyclerView) view.findViewById(R.id.tabRecyler);
        tabSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.tabSwipeRefresh);

        linearLayoutManager = new LinearLayoutManager(getActivity());

        //设置下拉刷新
        tabSwipeRefresh.setColorSchemeColors(R.color.refresh_blue,
                R.color.refresh_green, R.color.refresh_green_blue, R.color.refresh_white);
        tabSwipeRefresh.setOnRefreshListener(this);
        //第一次进入界面时候加载进度条
        tabSwipeRefresh.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        tabRecyler.setLayoutManager(linearLayoutManager);

        switch (fragementType) {
            case Constants.INTENT_FRAGMENT_TYPE_COMMENT:
                adapter = new TabCommentThumbAdapter(commentThumbBeanList, getActivity(), fragementType);
                break;
            case Constants.INTENT_FRAGMENT_TYPE_THUMB:
                adapter = new TabCommentThumbAdapter(commentThumbBeanList, getActivity(), fragementType);
                break;
            default:
                break;
        }

        tabRecyler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    swipeRefreshLayout.setRefreshing(true);
                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                    Log.i("MeFragment", "pageIndex:" + pageIndex);
                    //根据类目网络请求数据
                    if (!isLastPage) {
                        switch (fragementType) {
                            case Constants.INTENT_FRAGMENT_TYPE_COMMENT:
                                presenter.loadCommentThumbList(Constants.INTERFACE_SELECTCOMMENTDETAIL,pageIndex,pageSize);//加载评论
                                break;
                            case Constants.INTENT_FRAGMENT_TYPE_THUMB:
                                presenter.loadCommentThumbList(Constants.INTERFACE_SELECTLIKEDETAIL,pageIndex,pageSize);//加载点赞
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        tabRecyler.setAdapter(adapter);

        onRefresh();

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showProgress() {
        if (pageIndex == 1) {
            tabSwipeRefresh.setRefreshing(false);
        } else {
            tabSwipeRefresh.setRefreshing(true);
        }
    }

    @Override
    public void addCommentThumb(List<CommentThumbBean> list) {
        if (commentThumbBeanList != null) {
//            adapter.setShowFooter(true);
            if (commentThumbBeanList == null) {
                commentThumbBeanList = new ArrayList<>();
            }
            if (!ListUtils.isEmpty(list)) {
                commentThumbBeanList.addAll(list);
            }
            adapter.notifyDataSetChanged();

            if(ListUtils.isEmpty(list)){
                isLastPage = true;
            }else if(list.size() < pageSize){
                isLastPage = true;
            }else {
                isLastPage = false;
                pageIndex ++;
            }

        }
    }

    @Override
    public void hideProgress() {
        if(tabSwipeRefresh != null){
            tabSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showLoadFailMsg(String msg, Exception e) {
        if (pageIndex == 1) {
//            adapter.setShowFooter(false);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        Log.i(TAG, commentThumbBeanList.size() + "");
        if (commentThumbBeanList != null || commentThumbBeanList.size() > 0) {
            commentThumbBeanList.clear();
        }
        //根据类目网络请求数据
        switch (fragementType) {
            case Constants.INTENT_FRAGMENT_TYPE_COMMENT:
                presenter.loadCommentThumbList(Constants.INTERFACE_SELECTCOMMENTDETAIL,pageIndex,pageSize);//加载评论
                break;
            case Constants.INTENT_FRAGMENT_TYPE_THUMB:
                presenter.loadCommentThumbList(Constants.INTERFACE_SELECTLIKEDETAIL,pageIndex,pageSize);//加载点赞
                break;
            default:
                break;
        }
        adapter.notifyDataSetChanged();
    }
}
