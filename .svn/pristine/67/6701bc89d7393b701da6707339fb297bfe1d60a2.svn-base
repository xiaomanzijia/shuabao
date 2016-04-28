package com.jhlc.km.sb.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhlc.km.sb.R;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.view.PreImageLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by licheng on 25/4/16.
 */
public class ImagePreviewActivity extends Activity {
    @Bind(R.id.llBack)
    LinearLayout llBack;
    @Bind(R.id.textTresureName)
    TextView textTresureName;
    @Bind(R.id.btnReport)
    TextView btnReport;
    @Bind(R.id.preimagelayout)
    PreImageLayout preimagelayout;
    private ArrayList<String> imageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagetest);
        ButterKnife.bind(this);
        textTresureName.setText("预览");
        btnReport.setVisibility(View.GONE);
        imageList = getIntent().getStringArrayListExtra(Constants.INTENT_IMAGE_LIST);
        preimagelayout.mZoomImageView.initImageUrlList(imageList);
    }

    @OnClick(R.id.llBack)
    public void onClick() {
        finish();
    }
}
