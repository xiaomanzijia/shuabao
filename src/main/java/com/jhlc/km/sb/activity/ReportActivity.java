package com.jhlc.km.sb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jhlc.km.sb.R;
import com.jhlc.km.sb.common.ServerInterfaceHelper;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.utils.PreferencesUtils;
import com.jhlc.km.sb.utils.StringUtils;
import com.jhlc.km.sb.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ReportActivity extends BaseActivity implements ServerInterfaceHelper.Listenter {

    @Bind(R.id.llBack)
    LinearLayout llBack;
    @Bind(R.id.textTresureName)
    TextView textTresureName;
    @Bind(R.id.btnReport)
    TextView btnReport;
    @Bind(R.id.editReportContent)
    EditText editReportContent;

    private ServerInterfaceHelper helper;
    private static final String TAG = "ReportActivity";
    private String tresureid;

    @Override
    public void initView() {
        super.initView();
        textTresureName.setText("举报");
        btnReport.setVisibility(View.GONE);
        tresureid = getIntent().getStringExtra(Constants.INTENT_REPORT_TYPE);
        helper = new ServerInterfaceHelper(this,ReportActivity.this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_layout);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick({R.id.llBack, R.id.btnFeedbackSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                finish();
                break;
            case R.id.btnFeedbackSubmit:
                String content = editReportContent.getText().toString();
                if(!StringUtils.isBlank(tresureid) && PreferencesUtils.getBoolean(ReportActivity.this,Constants.PREFERENCES_IS_LOGIN)){
                    helper.doReport(TAG,tresureid,content);
                }else {
                    Intent login = new Intent(ReportActivity.this,LoginActivity.class);
                    startActivity(login);
                }
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

    @Override
    public void success(Object object) {
        if(object instanceof String && object.equals(Constants.INTERFACE_REPORT_SUCCESS)){
            ToastUtils.show(ReportActivity.this,Constants.INTERFACE_REPORT_SUCCESS);
            finish();
        }
    }

    @Override
    public void failure(String status) {

    }
}
