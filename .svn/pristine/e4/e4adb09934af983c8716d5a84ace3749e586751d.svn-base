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


public class OpinionFeedBackActivity extends BaseActivity implements ServerInterfaceHelper.Listenter {

    @Bind(R.id.llBack)
    LinearLayout llBack;
    @Bind(R.id.textTresureName)
    TextView textTresureName;
    @Bind(R.id.btnReport)
    TextView btnReport;
    @Bind(R.id.editFeedbackQuestion)
    EditText editFeedbackQuestion;
    @Bind(R.id.editFeedbackMobile)
    EditText editFeedbackMobile;

    private ServerInterfaceHelper helper;
    private final static String TAG = "OpinionFeedBackActivity";

    @Override
    public void initView() {
        super.initView();
        btnReport.setVisibility(View.GONE);
        textTresureName.setText("意见反馈");
        helper = new ServerInterfaceHelper(this,OpinionFeedBackActivity.this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion_feedback_layout);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick({R.id.llBack, R.id.btnFeedbackSubmit, R.id.btn_phone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                finish();
                break;
            case R.id.btnFeedbackSubmit:
                String mobile = editFeedbackMobile.getText().toString();
                String content = editFeedbackQuestion.getText().toString();
                if(!(StringUtils.isBlank(mobile) || StringUtils.isBlank(content))){
                    if(PreferencesUtils.getBoolean(OpinionFeedBackActivity.this,Constants.PREFERENCES_IS_LOGIN)){
                        helper.doFeedback(TAG,mobile,content);
                    }else {
                        Intent login = new Intent(OpinionFeedBackActivity.this,LoginActivity.class);
                        startActivity(login);
                    }
                }else {
                    ToastUtils.show(OpinionFeedBackActivity.this,"输入不能为空");
                }
                break;
            case R.id.btn_phone:
                ToastUtils.show(getApplicationContext(),"客服电话");
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
        if(object instanceof String  && object.equals(Constants.INTERFACE_FEEDBACK_SUCCESS)){
            ToastUtils.show(OpinionFeedBackActivity.this,Constants.INTERFACE_FEEDBACK_SUCCESS);
            finish();
        }
    }

    @Override
    public void failure(String status) {

    }
}
