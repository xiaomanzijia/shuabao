package com.jhlc.km.sb.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhlc.km.sb.R;
import com.jhlc.km.sb.common.ServerInterfaceHelper;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.model.MobileCheckModel;
import com.jhlc.km.sb.utils.PreferencesUtils;
import com.jhlc.km.sb.utils.StringUtils;
import com.jhlc.km.sb.utils.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by licheng on 29/3/16.
 */
public class EditUserInfoActivity extends BaseActivity implements ServerInterfaceHelper.Listenter {
    @Bind(R.id.llBack)
    LinearLayout llBack;
    @Bind(R.id.textTresureName)
    TextView textTresureName;
    @Bind(R.id.btnReport)
    TextView btnReport;
    @Bind(R.id.editMobile)
    EditText editMobile;
    @Bind(R.id.ibtnClear)
    ImageButton ibtnClear;
    @Bind(R.id.editYZM)
    EditText editYZM;
    @Bind(R.id.btnGetYzM)
    Button btnGetYzM;
    @Bind(R.id.btnSave)
    Button btnSave;
    @Bind(R.id.rlYzM)
    RelativeLayout rlYzM;

    private ServerInterfaceHelper helper;
    private final static String TAG = "EditUserInfoActivity";
    private String edittype;
    private String editcontent;

    private String newPwd;
    private String mobileCheckCode;
    private String newPwdPhone;

    @Override
    public void initView() {
        super.initView();
        if (Constants.INTENT_TYPE_FORGET_PASSWORD.equals(getIntent().getStringExtra(Constants.INTENT_TYPE))) {
            initPwdView();
        } else if (Constants.INTENT_TYPE_EDIT_USERINFO.equals(getIntent().getStringExtra(Constants.INTENT_TYPE))) {
            edittype = getIntent().getStringExtra(Constants.INTENT_TYPE_EDIT);
            initEditInfoView();
        }
        helper = new ServerInterfaceHelper(this,EditUserInfoActivity.this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_userinfo_layout);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick({R.id.llBack, R.id.ibtnClear, R.id.btnGetYzM, R.id.btnSave})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                finish();
                break;
            case R.id.ibtnClear:
                editMobile.setText("");
                break;
            case R.id.btnGetYzM:
                String phonenum = editMobile.getText().toString();
                if(StringUtils.isBlank(phonenum)){
                    ToastUtils.show(EditUserInfoActivity.this,"输入不能为空");
                }else {
                    helper.getServerYzM(TAG,phonenum);
                }
                break;
            case R.id.btnSave:
                if ("下一步".equals(btnSave.getText().toString())) {
                    newPwdPhone = editMobile.getText().toString();
                    mobileCheckCode = editYZM.getText().toString();
                    helper.mobileCodeCheck(TAG,newPwdPhone,mobileCheckCode);
                } else if ("确认".equals(btnSave.getText().toString())) {
                    newPwd = editMobile.getText().toString();
                    String secondPwd = editYZM.getText().toString();
                    if(StringUtils.isBlank(newPwd) && StringUtils.isBlank(secondPwd)){
                        ToastUtils.show(EditUserInfoActivity.this,"输入不能为空");
                    }else if(!(newPwd.equals(secondPwd))){
                        ToastUtils.show(EditUserInfoActivity.this,"两次输入不一致");
                    }else {
                        helper.updatePwd(TAG,newPwdPhone,mobileCheckCode,newPwd);
                    }
                } else if ("保存".equals(btnSave.getText().toString())) {
                    editcontent = editMobile.getText().toString();
                    String edityzm = editYZM.getText().toString();

                    if(StringUtils.isBlank(editcontent)){
                        ToastUtils.show(EditUserInfoActivity.this,"输入不能为空");
                    }else {
                        if (edittype.equals(Constants.INTENT_TYPE_EDIT_USERINFO_MOBILE)) { //手机号修改
                            helper.updateMobile(TAG, editcontent, edityzm);
                        } else {
                            if (!StringUtils.isBlank(edittype)) { //微信号、姓名修改
                                helper.updateUserInfo(TAG, PreferencesUtils.getString(EditUserInfoActivity.this,
                                        Constants.PREFERENCES_USERID), edittype, editcontent);
                            }
                        }
                    }
                }
                break;
        }
    }

    private void initPwdView() {
        textTresureName.setText("找回密码");
        editMobile.setHint("手机号");
        editYZM.setHint("验证码");
        rlYzM.setVisibility(View.VISIBLE);
        btnReport.setVisibility(View.GONE);
        btnSave.setText("下一步");
    }

    private void initEditInfoView() {
        rlYzM.setVisibility(View.GONE);
        btnSave.setText("保存");
        btnReport.setVisibility(View.GONE);
        switch (edittype){
            case Constants.INTENT_TYPE_EDIT_USERINFO_USERNAME:
                editMobile.setHint("姓名");
                textTresureName.setText("姓名");
                break;
            case Constants.INTENT_TYPE_EDIT_USERINFO_WECHATNO:
                editMobile.setHint("微信号");
                textTresureName.setText("微信号");
                break;
            case Constants.INTENT_TYPE_EDIT_USERINFO_MOBILE:
                initPwdView();
                textTresureName.setText("手机号");
                btnSave.setText("保存");
                break;
            default:
                break;
        }
    }

    private void initPwdNext(){
        textTresureName.setText("找回密码");
        editMobile.setHint("新密码");
        editYZM.setHint("确认密码");
        editMobile.setText("");
        editYZM.setText("");
        btnSave.setText("确认");
        btnReport.setVisibility(View.GONE);
        btnGetYzM.setVisibility(View.GONE);

    }

    @Override
    public void success(Object object) {
        if(object instanceof String && object.equals(Constants.INTERFACE_MSG_CODE_SUCCESS)){
            ToastUtils.show(EditUserInfoActivity.this,Constants.INTERFACE_MSG_CODE_SUCCESS);
        }else if(object instanceof String && object.equals(Constants.INTERFACE_EDITUSERINFO_SUCCESS)){
            switch (edittype){
                case Constants.INTENT_TYPE_EDIT_USERINFO_USERNAME:
                    PreferencesUtils.putString(EditUserInfoActivity.this, Constants.PREFERENCES_USERNAME, editcontent);
                    break;
                case Constants.INTENT_TYPE_EDIT_USERINFO_WECHATNO:
                    PreferencesUtils.putString(EditUserInfoActivity.this, Constants.PREFERENCES_USER_WECHAT_NUM, editcontent);
                    break;
                case Constants.INTENT_TYPE_EDIT_USERINFO_MOBILE:
                    PreferencesUtils.putString(EditUserInfoActivity.this, Constants.PREFERENCES_USER_MOBILE, editcontent);
                    break;
                default:
                    break;
            }
            finish();
        }else if(object instanceof MobileCheckModel){
            MobileCheckModel mobileCheckModel = (MobileCheckModel) object;
            if("1".equals(mobileCheckModel.getCheckflag())){
                initPwdNext();
            }else if("0".equals(mobileCheckModel.getCheckflag())) {
                ToastUtils.show(EditUserInfoActivity.this,"验证码错误");
            }
        }else if(object instanceof String && object.equals(Constants.INTERFACE_EDIT_PWD_SUCCESS)){
            PreferencesUtils.putString(EditUserInfoActivity.this, Constants.PREFERENCES_USER_PWD, newPwd);
            ToastUtils.show(EditUserInfoActivity.this,"密码修改成功");
            finish();
        }
    }

    @Override
    public void failure(String status) {

    }
}
