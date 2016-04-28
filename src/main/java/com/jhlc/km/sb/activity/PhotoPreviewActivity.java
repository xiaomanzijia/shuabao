package com.jhlc.km.sb.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jhlc.km.sb.R;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.utils.FileUtils;
import com.jhlc.km.sb.utils.PreferencesUtils;
import com.jhlc.km.sb.utils.ToastUtils;
import com.jhlc.km.sb.view.PreImageLayout;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotoPreviewActivity extends BaseActivity {

    @Bind(R.id.ibtnClose)
    ImageButton ibtnClose;
    @Bind(R.id.btnDelete)
    TextView btnDelete;
    @Bind(R.id.btnSave)
    TextView btnSave;

    File[] pics;
    private int imgLength;
    @Bind(R.id.preimagelayout)
    PreImageLayout preimagelayout;


    @Override
    public void initView() {
        super.initView();
        //从文件夹中读取图片
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){ //判断内存卡是否存在
            createAndReadFile(Constants.CaptureFilePath + Constants.CaptureFileName);
        }else {
            createAndReadFile(getApplicationContext().getPackageResourcePath() + "/" +Constants.CaptureFileName);
        }
        preimagelayout.mZoomImageView.initPics(pics);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopreview_layout);
        ButterKnife.bind(this);
        initView();
    }

    private void createAndReadFile(String path){
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        } else {
            pics = file.listFiles();
            imgLength = pics.length;
        }
    }


    @OnClick({R.id.ibtnClose, R.id.btnDelete, R.id.btnSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtnClose:
                finish();
                break;
            case R.id.btnDelete:
                deleteFileAndFinish();
                break;
            case R.id.btnSave:
                if (PreferencesUtils.getBoolean(PhotoPreviewActivity.this, Constants.PREFERENCES_IS_LOGIN)) {
                    if (imgLength > 0) {
                        Intent publishTresure = new Intent(PhotoPreviewActivity.this, PublishTresureActivity.class);
                        publishTresure.putExtra(Constants.INTENT_TYPE_PUBLISH_TRESURE, Constants.INTENT_TYPE_PUBLISH_TRESURE_PREVIEW);
                        startActivity(publishTresure);
                    } else {
                        ToastUtils.show(PhotoPreviewActivity.this, "您还没给宝贝拍照片");
                    }

                } else {
                    Intent login = new Intent(PhotoPreviewActivity.this, LoginActivity.class);
                    startActivity(login);
                }
                break;
        }
    }

    private void deleteFileAndFinish() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { //判断内存卡是否存在
            if (FileUtils.deleteFile(Constants.CaptureFilePath + Constants.CaptureFileName)) {
                finish();
            }
        } else {
            if (FileUtils.deleteFile(getApplicationContext().getPackageResourcePath() + "/" + Constants.CaptureFileName)) {
                finish();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
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