package com.jhlc.km.sb.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.Postprocessor;
import com.jhlc.km.sb.R;
import com.jhlc.km.sb.antiquedetail.presenter.AntiqueDetailPresenter;
import com.jhlc.km.sb.antiquedetail.presenter.AntiqueDetailPresenterImpl;
import com.jhlc.km.sb.antiquedetail.view.AntiqueDetailView;
import com.jhlc.km.sb.common.ImageDownLoader;
import com.jhlc.km.sb.common.ServerInterfaceHelper;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.fragment.ShareDialogFragment;
import com.jhlc.km.sb.model.AntiqueDetailBean;
import com.jhlc.km.sb.model.AntiqueDetailServerModel;
import com.jhlc.km.sb.model.CommentBean;
import com.jhlc.km.sb.model.ImageBean;
import com.jhlc.km.sb.model.ThumbCountBean;
import com.jhlc.km.sb.utils.ListUtils;
import com.jhlc.km.sb.utils.PreferencesUtils;
import com.jhlc.km.sb.utils.SoftInputUtils;
import com.jhlc.km.sb.utils.StatusBarCompat;
import com.jhlc.km.sb.utils.StringUtils;
import com.jhlc.km.sb.utils.ToastUtils;
import com.jhlc.km.sb.view.PreImageLayout;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by licheng on 22/3/16.
 */
public class TresureDetailActivity extends BaseActivity implements AntiqueDetailView, ServerInterfaceHelper.Listenter, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.imgTresureBig)
    ImageView imgTresure;
    @Bind(R.id.textTresureName)
    TextView textTresureName;
    @Bind(R.id.textPrice)
    TextView textPrice;
    @Bind(R.id.btnShare)
    Button btnShare;
    @Bind(R.id.imgCommentUserHead)
    SimpleDraweeView imgCommentUserHead;
    @Bind(R.id.textAddressTime)
    TextView textAddressTime;
    @Bind(R.id.imgUserHead)
    SimpleDraweeView imgUserHead;
    @Bind(R.id.textUserName)
    TextView textUserName;
    @Bind(R.id.textDescribe)
    TextView textDescribe;
    @Bind(R.id.textTime)
    TextView textTime;
    @Bind(R.id.ibtnThumb)
    ImageButton ibtnThumb;
    @Bind(R.id.textThumbNums)
    TextView textThumbNums;
    @Bind(R.id.editComment)
    EditText editComment;
    @Bind(R.id.btnSentComment)
    Button btnSentComment;
    @Bind(R.id.btnThumb)
    Button btnThumb;
    @Bind(R.id.llUserInfo)
    RelativeLayout llUserInfo;
    @Bind(R.id.btnReport)
    TextView btnReport;
    @Bind(R.id.btnCollect)
    ImageButton btnCollect;
    @Bind(R.id.llCommentHot)
    LinearLayout llCommentHot;
    @Bind(R.id.llCommentNews)
    LinearLayout llCommentNews;
    @Bind(R.id.textBack)
    TextView textBack;
    @Bind(R.id.llBack)
    LinearLayout llBack;
    @Bind(R.id.rlTresureInfo)
    RelativeLayout rlTresureInfo;
    @Bind(R.id.ibtnGo)
    ImageView ibtnGo;
    @Bind(R.id.rlCommentHot)
    RelativeLayout rlCommentHot;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.preimagelayout)
    PreImageLayout preimagelayout;

    private AntiqueDetailPresenter presenter;
    private String aniqueId;
    private String aniqueName;
    private ArrayList<String> imagesList;//保存网络图片地址

    private ServerInterfaceHelper helper;
    private static final String TAG = "TresureDetailActivity";
    //收藏标识 1已收藏 0未收藏
    private String colflag = "";
    //页面只刷新评论
    private boolean loadCommentDataOnly = false;

    private String userid;

    private int likeCount = 0; //宝贝详情点赞数量统计

    private List<CommentBean> commentBeanList;
    private int pageSize = 8;
    private int pageIndex = 1;

    private int imgLength;

    private int thumbPosition = 0;//记录点赞的item位置

    private int thumbCount = 0; //记录评论初始点赞数量

    private ImageRequest request;
    private Postprocessor redMeshPostprocessor;
    private PipelineDraweeController controller;

    private ImageDownLoader mImageDownLoader; //图片下载缓存本地


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tresure_detail_layout);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, getResources().getColor(R.color.colorPrimary));

        initView();

        initData();

    }

    private void initData() {
        aniqueId = getIntent().getStringExtra(Constants.INTENT_ANTIQUE_ID);
        aniqueName = getIntent().getStringExtra(Constants.INTENT_ANTIQUE_NAME);
        presenter = new AntiqueDetailPresenterImpl(TresureDetailActivity.this, this);
        if (aniqueName != null && !StringUtils.isBlank(aniqueName)) {
            textTresureName.setText(aniqueName);
        }
        imagesList = new ArrayList<>();
        helper = new ServerInterfaceHelper(this, TresureDetailActivity.this);
        commentBeanList = new ArrayList<>();
        mImageDownLoader = new ImageDownLoader(TresureDetailActivity.this);
        onRefresh();
    }


    @Override
    public void initView() {
        super.initView();
    }


    @OnClick({R.id.imgTresureBig,R.id.btnReport, R.id.llBack, R.id.btnCollect, R.id.btnShare, R.id.btnSentComment, R.id.btnThumb, R.id.llUserInfo, R.id.rlCommentHot, R.id.rlCommentNew})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgTresureBig:
                storeImageCache();
                break;
            case R.id.llBack:
                finish();
                break;
            case R.id.btnReport:
                Intent report = new Intent(getApplicationContext(), ReportActivity.class);
                report.putExtra(Constants.INTENT_REPORT_TYPE, aniqueId);
                startActivity(report);
                break;
            case R.id.btnCollect:
                if (!StringUtils.isBlank(colflag)) {
                    switch (colflag) {
                        case Constants.COLFLAG_ALREADY_COLLECTION:
                            if (PreferencesUtils.getBoolean(TresureDetailActivity.this, Constants.PREFERENCES_IS_LOGIN)) {
                                helper.cancelCollection(TAG, aniqueId);
                            } else {
                                toLogin();
                            }
                            break;
                        case Constants.COLFLAG_NOLONGER_COLLECTION:
                            if (PreferencesUtils.getBoolean(TresureDetailActivity.this, Constants.PREFERENCES_IS_LOGIN)) {
                                helper.doCollection(TAG, aniqueId);
                            } else {
                                toLogin();
                            }
                            break;
                        default:
                            break;
                    }
                }
                break;
            case R.id.btnShare:
                ShareDialogFragment dialogFragment = new ShareDialogFragment();
                dialogFragment.show(getFragmentManager(), "sharedialog");
                break;
            case R.id.llUserInfo:
                Intent userinfopage = new Intent(getApplicationContext(), UserInfoPageAcitivity.class);
                userinfopage.putExtra(Constants.INTENT_USER_ID, userid);
                startActivity(userinfopage);
                break;
            case R.id.btnSentComment: //评论
                String content = editComment.getText().toString();
                if (!StringUtils.isBlank(content)) {
                    helper.comment(TAG, content, aniqueId, "0");
                } else {
                    ToastUtils.show(TresureDetailActivity.this, "评论内容不能为空");
                }
                break;
            case R.id.btnThumb: //点赞
                helper.doLike(TAG, aniqueId);
                break;
            case R.id.rlCommentHot: //跳转到评论详细
                toCommentHotNew();
                break;
            case R.id.rlCommentNew:
                toCommentHotNew();
                break;
            default:
                break;
        }
    }

    private void toCommentHotNew() {
        Intent commentnew = new Intent(TresureDetailActivity.this, CommentHotNewActivity.class);
        commentnew.putExtra(Constants.INTENT_TRESUREDETAIL_COMMENTHOTNEW_TRESREID, aniqueId);
        startActivity(commentnew);
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

    @Override
    public void showProgress() {

    }

    //下拉刷新
    @Override
    public void onRefresh() {
        pageIndex = 1;
        if (!ListUtils.isEmpty(commentBeanList)) {
            commentBeanList.clear();
        }
        if (aniqueId != null && !StringUtils.isBlank(aniqueId)) {
            if (PreferencesUtils.getBoolean(TresureDetailActivity.this, Constants.PREFERENCES_IS_LOGIN)) {
                presenter.loadAntiqueDeatial(pageIndex, pageSize, Integer.valueOf(aniqueId), PreferencesUtils.getString(
                        TresureDetailActivity.this, Constants.PREFERENCES_USERID));
                progressDialog.show();
                loadCommentDataOnly = false;
            } else {
                presenter.loadAntiqueDeatial(pageIndex, pageSize, Integer.valueOf(aniqueId), "");
                loadCommentDataOnly = false;
            }
        }
    }

    @Override
    public void addTresureDetail(AntiqueDetailServerModel antiqueDetailModel) {

        final AntiqueDetailBean antiqueDetailBean = antiqueDetailModel.getAntiqueDetailBean();
        List<CommentBean> hotCommentList = antiqueDetailModel.getCommentHotBeanList();
        List<CommentBean> latestCommentList = antiqueDetailModel.getCommentLatestBeanList();
        List<ImageBean> imageList = antiqueDetailModel.getAntiqueDetailBean().getImageList();

        userid = antiqueDetailBean.getUuid();

        btnThumb.setText("赞" + antiqueDetailBean.getLikecount());

        likeCount = Integer.valueOf(antiqueDetailBean.getLikecount());


        if (loadCommentDataOnly) { //用户评论只刷新评论布局
            initCommentView(hotCommentList, latestCommentList);
        } else {
            colflag = antiqueDetailModel.getColflag();

            //收藏标签判断
            switch (colflag) {
                case Constants.COLFLAG_ALREADY_COLLECTION:
                    btnCollect.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_cancle_collect)); //已收藏
                    break;
                case Constants.COLFLAG_NOLONGER_COLLECTION:
                    btnCollect.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_collection_background)); // 未收藏
                    break;
                default:
                    break;
            }

            if (imageList != null && imageList.size() != 0) {
                imgLength = imageList.size();
                for (int i = 0; i < imageList.size(); i++) {
                    imagesList.add(imageList.get(i).getImgurl() + Constants.OSS_IMAGE_SIZE500);
                }
            }
            

            if (antiqueDetailBean != null) {
                textPrice.setText(antiqueDetailBean.getPrice() + "元");
                textDescribe.setText(antiqueDetailBean.getDescribe());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = Constants.OSS_IMAGE_URL + antiqueDetailBean.getIndeximage() + Constants.OSS_IMAGE_SIZE400;
                        try {
                            Bitmap bitmap = mImageDownLoader.getBitmap(url);
                            Message msg = new Message();
                            msg.what = 1;
                            msg.obj = bitmap;
                            handler.sendMessage(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();


                imgCommentUserHead.setImageURI(Uri.parse(antiqueDetailBean.getHeadimgurl() + Constants.OSS_IMAGE_SIZE100));
                textUserName.setText(antiqueDetailBean.getUsername());
                textAddressTime.setText(antiqueDetailBean.getAddress() + " " + antiqueDetailBean.getStrdate());
            }


            initCommentView(hotCommentList, latestCommentList);

            progressDialog.dismiss();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Bitmap bitmap = (Bitmap) msg.obj;
                    imgTresure.setImageBitmap(bitmap);
                    break;
                default:
                    break;
            }
        }
    };

    //评论界面刷新
    private void initCommentView(final List<CommentBean> hotCommentList, final List<CommentBean> latestCommentList) {

        llCommentNews.removeAllViews();
        llCommentHot.removeAllViews();

        if (hotCommentList != null && hotCommentList.size() != 0) {
            for (int i = 0; i < hotCommentList.size(); i++) {
                final int ii = i;
                final int thumbcount = Integer.valueOf(latestCommentList.get(i).getLikecount());
                View view = LayoutInflater.from(TresureDetailActivity.this).inflate(R.layout.comment_item, null);
                SimpleDraweeView imgUserHead = (SimpleDraweeView) view.findViewById(R.id.imgUserHead);
                TextView textUserName = (TextView) view.findViewById(R.id.textUserName);
                TextView textDescribe = (TextView) view.findViewById(R.id.textDescribe);
                TextView textTime = (TextView) view.findViewById(R.id.textTime);
                TextView textThumbNums = (TextView) view.findViewById(R.id.textThumbNums);
                ImageButton ibtnThumb = (ImageButton) view.findViewById(R.id.ibtnThumb);
                imgUserHead.setImageURI(Uri.parse(hotCommentList.get(i).getHeadimgurl() + Constants.OSS_IMAGE_SIZE50));
                textUserName.setText(hotCommentList.get(i).getUsername());
                textDescribe.setText(hotCommentList.get(i).getContent());
                textTime.setText(hotCommentList.get(i).getStrdate());
                textThumbNums.setText(hotCommentList.get(i).getLikecount());
                ibtnThumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ThumbCountBean thumbbean = new ThumbCountBean();
                        thumbbean.setCommenttype(0); // 0 热门 1 最新
                        thumbbean.setPosition(ii);
                        thumbbean.setThumbcount(thumbcount);
                        helper.doLikeToCommentWithThumb(TAG, latestCommentList.get(ii).getId(), thumbbean);
                    }
                });
                llCommentHot.addView(view);
            }
        }
        if (latestCommentList != null && latestCommentList.size() != 0) {
            for (int i = 0; i < latestCommentList.size(); i++) {
                final int ii = i;
                final int thumbcount = Integer.valueOf(latestCommentList.get(i).getLikecount());
                View view = LayoutInflater.from(TresureDetailActivity.this).inflate(R.layout.comment_item, null);
                SimpleDraweeView imgUserHead = (SimpleDraweeView) view.findViewById(R.id.imgUserHead);
                TextView textUserName = (TextView) view.findViewById(R.id.textUserName);
                TextView textDescribe = (TextView) view.findViewById(R.id.textDescribe);
                TextView textTime = (TextView) view.findViewById(R.id.textTime);
                TextView textThumbNums = (TextView) view.findViewById(R.id.textThumbNums);
                ImageButton ibtnThumb = (ImageButton) view.findViewById(R.id.ibtnThumb);

                imgUserHead.setImageURI(Uri.parse(latestCommentList.get(i).getHeadimgurl() + Constants.OSS_IMAGE_SIZE50));
                textUserName.setText(latestCommentList.get(i).getUsername());
                textDescribe.setText(latestCommentList.get(i).getContent());
                textTime.setText(latestCommentList.get(i).getStrdate());
                textThumbNums.setText(latestCommentList.get(i).getLikecount());
                ibtnThumb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (PreferencesUtils.getBoolean(TresureDetailActivity.this, Constants.PREFERENCES_IS_LOGIN)) {
                            ThumbCountBean thumbbean = new ThumbCountBean();
                            thumbbean.setCommenttype(1); // 0 热门 1 最新
                            thumbbean.setPosition(ii);
                            thumbbean.setThumbcount(thumbcount);
                            helper.doLikeToCommentWithThumb(TAG, latestCommentList.get(ii).getId(), thumbbean);
                        } else {
                            toLogin();
                        }
                    }
                });
                llCommentNews.addView(view);
            }
        }
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showLoadFailMsg(String msg, Exception e) {

    }

    @Override
    public void success(Object object) {
        if (object instanceof String && object.equals(Constants.INTERFACE_DO_COLLECTION_SUCCESS)) {  //收藏
            ToastUtils.show(TresureDetailActivity.this, Constants.INTERFACE_DO_COLLECTION_SUCCESS);
            btnCollect.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_cancle_collect));
            colflag = Constants.COLFLAG_ALREADY_COLLECTION;
        } else if (object instanceof String && object.equals(Constants.INTERFACE_CANCEL_COLLECTION_SUCCESS)) { //取消收藏
            btnCollect.setImageDrawable(getResources().getDrawable(R.drawable.ibtn_collection_background));
            colflag = Constants.COLFLAG_NOLONGER_COLLECTION;
        } else if (object instanceof String && object.equals(Constants.INTERFACE_COMMENT_SUCCESS)) { //评论
            ToastUtils.show(TresureDetailActivity.this, Constants.INTERFACE_COMMENT_SUCCESS);
            pageIndex = 1;
            if (!ListUtils.isEmpty(commentBeanList)) {
                commentBeanList.clear();
            }
            presenter.loadAntiqueDeatial(pageIndex, pageSize, Integer.valueOf(aniqueId), PreferencesUtils.getString(
                    TresureDetailActivity.this, Constants.PREFERENCES_USERID));
            loadCommentDataOnly = true;
            editComment.setText("");
            SoftInputUtils.hideSoftInput(TresureDetailActivity.this, editComment);
        } else if (object instanceof String && object.equals(Constants.INTERFACE_LIKE_SUCCESS)) { //点赞
            likeCount += 1;
            btnThumb.setText("赞" + String.valueOf(likeCount));
        } else if (object instanceof ThumbCountBean) { //给评论点赞
            ThumbCountBean bean = (ThumbCountBean) object;
            switch (bean.getCommenttype()) {
                case 0: //热门
                    updateThumbNum(llCommentHot, bean);
                    break;
                case 1: //最新
                    updateThumbNum(llCommentNews, bean);
                    break;
                default:
                    break;
            }
        }
    }

    private void updateThumbNum(LinearLayout viegroup, ThumbCountBean bean) {
        RelativeLayout rlComment = (RelativeLayout) viegroup.getChildAt(bean.getPosition());
        for (int j = 0; j < rlComment.getChildCount(); j++) {
            TextView textThumb = (TextView) rlComment.getChildAt(5);
            textThumb.setText(String.valueOf(bean.getThumbcount() + 1));
        }
    }

    @Override
    public void failure(String status) {

    }

    private void toLogin() {
        Intent login = new Intent(TresureDetailActivity.this, LoginActivity.class);
        startActivity(login);
    }


    //缓存图片到本地
    private void storeImageCache() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(imgLength);
        Bitmap image = null;
        final int[] cacheImageCount = {0}; //缓存图片数量统计
        if (imgLength != 0 && imagesList != null) {
            for (int i = 0; i < imgLength; i++) {
                image = mImageDownLoader.downloadImage(imagesList.get(i), new ImageDownLoader.onImageLoaderListener() {
                    @Override
                    public void onImageLoader(Bitmap bitmap, String url) { //从网络下载
                        if (bitmap != null) {
                            cacheImageCount[0]++;
                            progressBar.setProgress(cacheImageCount[0]);
                            if (cacheImageCount[0] == imgLength) {
                                progressBar.setVisibility(View.GONE);
                                toPreview();
                            }
                        }
                    }
                });
                if (image != null) { //加载本地缓存
                    cacheImageCount[0]++;
                    progressBar.setProgress(cacheImageCount[0]);
                    if (cacheImageCount[0] == imgLength) {
                        progressBar.setVisibility(View.GONE);
                        toPreview();
                    }
                }
            }
        }
    }

    //跳转到预览界面
    private void toPreview(){
        Intent preview = new Intent(TresureDetailActivity.this,ImagePreviewActivity.class);
        preview.putStringArrayListExtra(Constants.INTENT_IMAGE_LIST,imagesList);
        startActivity(preview);
    }

    //判断uri是否缓存
    private boolean isDownloaded(Uri loadUri) {
        if (loadUri == null) {
            return false;
        }
        ImageRequest imageRequest = ImageRequest.fromUri(loadUri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(imageRequest);
        return ImagePipelineFactory.getInstance()
                .getMainDiskStorageCache().hasKey(cacheKey);
    }

    //根据url获取缓存文件
    private Bitmap getUriFileBitmap(Uri uri) {
        Bitmap bitmap;
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(imageRequest);
        BinaryResource resource = ImagePipelineFactory.getInstance()
                .getMainDiskStorageCache().getResource(cacheKey);
        File file = ((FileBinaryResource) resource).getFile();
        if (file.exists()) {
            String filepath = file.getAbsolutePath();
            Log.i("filepath", filepath);
            bitmap = BitmapFactory.decodeFile(filepath);
            return bitmap;
        } else {
            return null;
        }
    }

    //清除缓存
//    private void clearMemory() {
//        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        imagePipeline.clearMemoryCaches();
////        imagePipeline.clearDiskCaches();
//    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (preimagelayout.getVisibility() == View.INVISIBLE) {
                finish();
//                clearMemory();
            } else {
                preimagelayout.setVisibility(View.INVISIBLE);
            }
        } else {
            return super.dispatchKeyEvent(event);
        }
        return true;
    }
}
