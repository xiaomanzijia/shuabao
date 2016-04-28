package com.jhlc.km.sb.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhlc.km.sb.R;
import com.jhlc.km.sb.adapter.TabTresureAdapter;
import com.jhlc.km.sb.common.ServerInterfaceHelper;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.model.AntiqueBean;
import com.jhlc.km.sb.model.CategoryBean;
import com.jhlc.km.sb.model.UserBean;
import com.jhlc.km.sb.model.UserModel;
import com.jhlc.km.sb.tabtresure.presenter.TresurePresenter;
import com.jhlc.km.sb.tabtresure.presenter.TresurePresenterImpl;
import com.jhlc.km.sb.tabtresure.view.TresureView;
import com.jhlc.km.sb.utils.ListUtils;
import com.jhlc.km.sb.utils.PreferencesUtils;
import com.jhlc.km.sb.utils.SoftInputUtils;
import com.jhlc.km.sb.utils.SqliteUtils;
import com.jhlc.km.sb.utils.StatusBarCompat;
import com.jhlc.km.sb.utils.StringUtils;
import com.jhlc.km.sb.utils.ToastUtils;
import com.jhlc.km.sb.view.SearchPopupWindow;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by licheng on 24/3/16.
 */
public class UserInfoPageAcitivity extends BaseActivity implements SearchPopupWindow.onClickListener, ServerInterfaceHelper.Listenter, TresureView, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.llBack)
    LinearLayout llBack;
    @Bind(R.id.sdUserHead)
    SimpleDraweeView sdUserHead;
    @Bind(R.id.btnFocus)
    ImageButton btnFocus;
    @Bind(R.id.textUserName)
    TextView textUserName;
    @Bind(R.id.textAddress)
    TextView textAddress;
    @Bind(R.id.textUserWeChatNum)
    TextView textUserWeChatNum;
    @Bind(R.id.textUserPhoneNum)
    TextView textUserPhoneNum;
    @Bind(R.id.llSearchCondition)
    LinearLayout llSearchCondition;
    @Bind(R.id.editSearch)
    EditText editSearch;
    @Bind(R.id.ibtnSearch)
    ImageButton ibtnSearch;
    @Bind(R.id.tabRecyler)
    RecyclerView recyclerView;
    @Bind(R.id.tabSwipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.textSearch)
    TextView textSearch;
    @Bind(R.id.llRecycler)
    LinearLayout llRecycler;
    @Bind(R.id.textNotice)
    TextView textNotice;
    @Bind(R.id.llNotice)
    LinearLayout llNotice;

    private SearchPopupWindow popupWindow;
    private SqliteUtils sqliteUtils;
    private List<CategoryBean> menuList;

    private int followStatus = 1;//关注状态 1 已关注 0 未关注


    private GridLayoutManager gridLayoutManager;
    private TabTresureAdapter adapter;

    private String userid;//关注用户id
    private ServerInterfaceHelper helper;
    private TresurePresenter presenter;
    private int pageIndex = 1;
    private int pageSize = 8;
    private boolean isLastPage = false;
    private int lastVisibleItem = 0;
    private List<AntiqueBean> antiqueBeanList;

    private static final String TAG = "UserInfoPageAcitivity";

    private String followId;//取消关注人用户id 整形
    private String followid;//关注人用户id 32位

    @Override
    public void initView() {
        super.initView();
        initDialog();
        antiqueBeanList = new ArrayList<>();
        sqliteUtils = new SqliteUtils(UserInfoPageAcitivity.this);
        menuList = sqliteUtils.getCategoryList();
        popupWindow = new SearchPopupWindow(UserInfoPageAcitivity.this, menuList);
        popupWindow.setListener(this);
        helper = new ServerInterfaceHelper(this, UserInfoPageAcitivity.this);
        userid = getIntent().getStringExtra(Constants.INTENT_USER_ID);
        presenter = new TresurePresenterImpl(this, UserInfoPageAcitivity.this);
        progressDialog.show();
        helper.GetUserInfoByID(TAG, userid);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo_page_layout);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimary));
        initView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        gridLayoutManager = new GridLayoutManager(UserInfoPageAcitivity.this, 2);

        //设置下拉刷新
        swipeRefreshLayout.setColorSchemeColors(R.color.refresh_blue,
                R.color.refresh_green, R.color.refresh_green_blue, R.color.refresh_white);
        swipeRefreshLayout.setOnRefreshListener(this);
        //第一次进入界面时候加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new TabTresureAdapter(antiqueBeanList, UserInfoPageAcitivity.this);


        adapter.setListener(new TabTresureAdapter.onItenClickListener() {
            @Override
            public void onItemClick(View view, int position, String id, String name) {
                Intent intent = new Intent(UserInfoPageAcitivity.this, TresureDetailActivity.class);
                intent.putExtra(Constants.INTENT_ANTIQUE_ID, id);
                intent.putExtra(Constants.INTENT_ANTIQUE_NAME, name);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    swipeRefreshLayout.setRefreshing(true);
                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                    Log.i("pageIndex", pageIndex + "");
                    //根据类目网络请求数据
                    if (!isLastPage) {
                        presenter.loadTresure(pageIndex, pageSize, "", "", userid, "", "");
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
            }
        });

        recyclerView.setAdapter(adapter);

        onRefresh();
    }

    @OnClick({R.id.llBack, R.id.btnFocus, R.id.llSearchCondition, R.id.ibtnSearch})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                finish();
                break;
            case R.id.btnFocus:
                if (PreferencesUtils.getBoolean(UserInfoPageAcitivity.this, Constants.PREFERENCES_IS_LOGIN)) {
                    if (!StringUtils.isBlank(followId)) {
                        switch (followStatus) {
                            case 1:
                                if (PreferencesUtils.getBoolean(UserInfoPageAcitivity.this, Constants.PREFERENCES_IS_LOGIN)) {
                                    if (!StringUtils.isBlank(followId)) {
                                        helper.cancelFollow(TAG, followId);
                                    }
                                } else {
                                    toLogin();
                                }
                                break;
                            case 0:
                                if (PreferencesUtils.getBoolean(UserInfoPageAcitivity.this, Constants.PREFERENCES_IS_LOGIN)) {
                                    if (!StringUtils.isBlank(followid)) {
                                        helper.doFollow(TAG, followid);
                                    }
                                } else {
                                    toLogin();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                } else {
                    Intent login = new Intent(UserInfoPageAcitivity.this, LoginActivity.class);
                    startActivity(login);
                }
                break;
            case R.id.llSearchCondition:
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    popupWindow.showAsDropDown(view);
                }
                break;
            case R.id.ibtnSearch:
                pageIndex = 1;
                if (antiqueBeanList != null || antiqueBeanList.size() > 0) {
                    antiqueBeanList.clear();
                }
                String name = editSearch.getText().toString();
                if (!StringUtils.isBlank(name)) {
                    presenter.loadTresure(pageIndex, pageSize, "", name, userid, "", "");
                }
                SoftInputUtils.hideSoftInput(UserInfoPageAcitivity.this, editSearch);
                break;
        }
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

    private void toLogin() {
        Intent login = new Intent(UserInfoPageAcitivity.this, LoginActivity.class);
        startActivity(login);
    }


    @Override
    public void onClick(int position, String menuTitle, String menuId) {
        textSearch.setText(menuTitle);
        pageIndex = 1;
        if (antiqueBeanList != null || antiqueBeanList.size() > 0) {
            antiqueBeanList.clear();
        }
        presenter.loadTresure(pageIndex, pageSize, menuId, "", userid, "", "");
        popupWindow.dismiss();
    }

    @Override
    public void success(Object object) {
        if (object instanceof String && object.equals(Constants.INTERFACE_FOLLOW_SUCCESS)) {
            btnFocus.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_cancle_follow));
            followStatus = 1;
            ToastUtils.show(UserInfoPageAcitivity.this, Constants.INTERFACE_FOLLOW_SUCCESS);
        } else if (object instanceof String && object.equals(Constants.INTERFACE_CACLEFOLLOW_SUCCESS)) {
            btnFocus.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_focus_backgronnd));
            followStatus = 0;
            ToastUtils.show(UserInfoPageAcitivity.this, Constants.INTERFACE_CACLEFOLLOW_SUCCESS);
        } else if (object instanceof UserModel) {
            UserModel userModel = (UserModel) object;
            UserBean userBean = userModel.getUserinfo();
            followStatus = userModel.getFollowflag();
            switch (followStatus) {
                case 1:
                    btnFocus.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_cancle_follow));
                    break;
                case 0:
                    btnFocus.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_focus_backgronnd));
                    break;
                default:
                    break;
            }
            sdUserHead.setImageURI(Uri.parse(userBean.getHeadimgurl() + Constants.OSS_IMAGE_SIZE100));
            textAddress.setText(userBean.getProvince() + userBean.getCity());
            textUserName.setText(userBean.getUsername());
            textUserPhoneNum.setText(userBean.getMobile());
            textUserWeChatNum.setText(userBean.getWechatno());
            followId = userBean.getUuid();
            followid = userBean.getId();
            progressDialog.dismiss();
        }
    }

    @Override
    public void failure(String status) {

    }

    @Override
    public void showProgress() {
        if (pageIndex == 1) {
            swipeRefreshLayout.setRefreshing(false);
        } else {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void addTresure(List<AntiqueBean> list) {
        if (pageIndex == 1 && ListUtils.isEmpty(list)) {
            recyclerView.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            llNotice.setVisibility(View.VISIBLE);
            textNotice.setText(Constants.TAG_NO_SEARCH_RESUTL);
        } else if (!ListUtils.isEmpty(list)) {

            recyclerView.setVisibility(View.VISIBLE);
            llNotice.setVisibility(View.GONE);

            if (antiqueBeanList != null) {
//            adapter.setShowFooter(true);

                if (!ListUtils.isEmpty(list)) {
                    antiqueBeanList.addAll(list);
                }

                adapter.notifyDataSetChanged();

                if (ListUtils.isEmpty(list)) {
                    isLastPage = true;
                } else if (list.size() < pageSize) {
                    isLastPage = true;
                } else {
                    isLastPage = false;
                    pageIndex++;
                }
            }
        }else {
            isLastPage = true;
        }
    }

    @Override
    public void hideProgress() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {
        if (pageIndex == 1) {
//            adapter.setShowFooter(false);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        textSearch.setText("全部");
        pageIndex = 1;
        if (!ListUtils.isEmpty(antiqueBeanList)) {
            antiqueBeanList.clear();
        }
        //根据类目网络请求数据
        presenter.loadTresure(pageIndex, pageSize, "", "", userid, "", "");
        adapter.notifyDataSetChanged();
    }
}
