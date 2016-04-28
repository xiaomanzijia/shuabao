package com.jhlc.km.sb.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhlc.km.sb.R;
import com.jhlc.km.sb.constants.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by licheng on 8/4/16.
 */
public class HelpAcitivity extends BaseActivity {

    @Bind(R.id.llBack)
    LinearLayout llBack;
    @Bind(R.id.textTresureName)
    TextView textTresureName;
    @Bind(R.id.btnReport)
    TextView btnReport;
    @Bind(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_layout);
        ButterKnife.bind(this);
        textTresureName.setText("帮助");
        btnReport.setVisibility(View.GONE);
        webview.loadUrl(Constants.WEB_HELP);
    }


    @OnClick(R.id.llBack)
    public void onClick() {
        finish();
    }
}
