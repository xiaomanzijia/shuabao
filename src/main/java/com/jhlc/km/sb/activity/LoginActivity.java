package com.jhlc.km.sb.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.jhlc.km.sb.R;
import com.jhlc.km.sb.SbApplication;
import com.jhlc.km.sb.common.ServerInterfaceHelper;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.model.CategoryBean;
import com.jhlc.km.sb.model.UserBean;
import com.jhlc.km.sb.model.UserModel;
import com.jhlc.km.sb.utils.ListUtils;
import com.jhlc.km.sb.utils.PreferencesUtils;
import com.jhlc.km.sb.utils.SqliteUtils;
import com.jhlc.km.sb.utils.StringUtils;
import com.jhlc.km.sb.utils.ToastUtils;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by licheng on 24/3/16.
 */
public class LoginActivity extends BaseActivity implements ServerInterfaceHelper.Listenter {

    @Bind(R.id.llBack)
    LinearLayout llBack;
    @Bind(R.id.textTresureName)
    TextView textTresureName;
    @Bind(R.id.btnReport)
    TextView btnReport;
    @Bind(R.id.editMobile)
    EditText editMobile;
    @Bind(R.id.editPassword)
    EditText editPassword;
    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.btnForgetPassword)
    TextView btnForgetPassword;
    @Bind(R.id.btnRegist)
    TextView btnRegist;
    @Bind(R.id.textLableCompaireSite)
    TextView textLableCompaireSite;
    @Bind(R.id.ibtnWeChat)
    ImageButton ibtnWeChat;
    @Bind(R.id.ibtnQQ)
    ImageButton ibtnQQ;
    @Bind(R.id.editYZM)
    EditText editYZM;
    @Bind(R.id.btnGetYzM)
    Button btnGetYzM;
    @Bind(R.id.rlYzM)
    RelativeLayout rlYzM;

    private final String TAG = "LoginActivity";
    public final static String INTENT_TYPE_FORGET_PASSWORD = "FORGET_PASSWOED";

    private ServerInterfaceHelper helper;

    private SqliteUtils sqliteUtils;

    private Tencent mTencent;

    private IWXAPI api;

    private RequestQueue requestQueue = Volley.newRequestQueue(SbApplication
            .getContext());




    @Override
    public void initView() {
        super.initView();
        btnReport.setVisibility(View.GONE);
        textTresureName.setText("登陆");
        helper = new ServerInterfaceHelper(this, LoginActivity.this);
        sqliteUtils = new SqliteUtils(LoginActivity.this);
        api = ((SbApplication) getApplication()).api;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        ButterKnife.bind(this);
        initView();
    }

    @OnClick({R.id.llBack, R.id.btnLogin, R.id.btnForgetPassword, R.id.btnRegist, R.id.ibtnWeChat, R.id.ibtnQQ, R.id.btnGetYzM})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                finish();
                break;
            case R.id.btnLogin:
                if ("立即注册".equals(btnLogin.getText().toString())) {
                    String code = editYZM.getText().toString();
                    String phonenum = editMobile.getText().toString();
                    String pwd = editPassword.getText().toString();
                    if (StringUtils.isBlank(phonenum)) {
                        ToastUtils.show(getApplicationContext(), "手机号不能为空");
                    } else if (StringUtils.isBlank(code)) {
                        ToastUtils.show(getApplicationContext(), "验证码不能为空");
                    } else if (StringUtils.isBlank(pwd)) {
                        ToastUtils.show(getApplicationContext(), "密码不能为空");
                    } else {
                        String userid = PreferencesUtils.getString(LoginActivity.this, Constants.PREFERENCES_USERID);
                        if (!StringUtils.isBlank(userid)) {
                            helper.ServerRegist(TAG, phonenum, code, pwd, userid);
                        }
                    }
                } else if ("马上登陆".equals(btnLogin.getText().toString())) {
                    String phonenum = editMobile.getText().toString();
                    String pwd = editPassword.getText().toString();
                    if (StringUtils.isBlank(phonenum)) {
                        ToastUtils.show(getApplicationContext(), "手机号不能为空");
                    } else if (StringUtils.isBlank(pwd)) {
                        ToastUtils.show(getApplicationContext(), "密码不能为空");
                    } else {
                        helper.ServerLogin(TAG, phonenum, pwd);
                    }
                }
                break;
            case R.id.btnForgetPassword:
                Intent forgetpwd = new Intent(LoginActivity.this, EditUserInfoActivity.class);
                forgetpwd.putExtra(Constants.INTENT_TYPE, INTENT_TYPE_FORGET_PASSWORD);
                startActivity(forgetpwd);
                break;
            case R.id.btnRegist:
                initRegistView();
                break;
            case R.id.ibtnWeChat:
                toLoginWithWeChat();
//                sendMessageText();
                break;
            case R.id.ibtnQQ:
                toLoginWithQQ();
                break;
            case R.id.btnGetYzM:
                String phonenum = editMobile.getText().toString();
                if (!StringUtils.isBlank(phonenum)) {
                    helper.getServerYzM(TAG, phonenum);
                } else {
                    ToastUtils.show(LoginActivity.this, "手机号不能为空");
                }
                break;
        }
    }

    //微信三方登陆
    private void toLoginWithWeChat() {
        //将应用id注册到微信
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo";
        Log.i(TAG,api.sendReq(req)+"");
        if(api.sendReq(req)){
            finish();
        }
    }

    private void sendMessageText() {
        WXTextObject textObj = new WXTextObject();
        textObj.text = "你好微信";
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = "你好微信";
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    private void initRegistView() {
        rlYzM.setVisibility(View.VISIBLE);
        btnForgetPassword.setVisibility(View.GONE);
        btnRegist.setVisibility(View.GONE);
        editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        btnLogin.setText("立即注册");
        textTresureName.setText("注册");
    }

    private void initLoginView() {
        rlYzM.setVisibility(View.GONE);
        btnForgetPassword.setVisibility(View.VISIBLE);
        btnRegist.setVisibility(View.VISIBLE);
        editPassword.setText("");
        editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        btnLogin.setText("马上登陆");
        textTresureName.setText("登陆");
    }

    //QQ三方登陆
    private void toLoginWithQQ() {
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, this.getApplicationContext());
        int a = mTencent.login(LoginActivity.this, "all", listener);
        if(a == 1){
            finish();
        }
    }

    //QQ三方登陆回调
    IUiListener listener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            Log.i(TAG, o.toString());
            if (o != null) {
                String openid = null;
                String accessToken = null;
                try {
                    JSONObject object = (JSONObject) o;
                    openid = object.getString("openid");
                    accessToken = object.getString("access_token");
                    String expires = object.getString("expires_in");
                    Log.i(TAG,openid+" "+accessToken);
                    mTencent.setOpenId(openid);
                    mTencent.setAccessToken(accessToken,expires);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (!StringUtils.isBlank(openid)) {
                    QQToken qqToken = mTencent.getQQToken();
                    UserInfo info = new UserInfo(getApplicationContext(), qqToken);
                    final String finalOpenid = openid;
                    info.getUserInfo(new IUiListener() {
                        @Override
                        public void onComplete(Object o) {
                            JSONObject object = (JSONObject) o;
                            String nickname = null;
                            String userheadImageUrl = null;
                            try {
                                nickname = object.getString("nickname");
                                userheadImageUrl = object.getString("figureurl_qq_1");
                                final String finalNickname = nickname;
                                helper.doThirdLogin(TAG,PreferencesUtils.getString(LoginActivity.this,Constants.PREFERENCES_USERID),
                                        "1", finalOpenid,nickname,"");
                                ImageRequest imageRequest = new ImageRequest(userheadImageUrl, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
//                                        imageView.setImageBitmap(bitmap);
                                        saveBitmap(bitmap, finalNickname); //把头像保存到本地
                                    }
                                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
//                                        imageView.setImageResource(R.drawable.image_userinfopage_phone);
                                    }
                                });
                                requestQueue.add(imageRequest);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(UiError uiError) {

                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    };

    //把bitmap转成jpg存储到到指定文件夹
    public void saveBitmap(Bitmap mBitmap,String bitName)  {
        File f = null;
        File headimageFile;
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){ //判断内存卡是否存在
            headimageFile = new File(Constants.CaptureFilePath +  "headimage/");
            if(!headimageFile.exists()){
                headimageFile.mkdir();
            }
            String path = headimageFile.getAbsolutePath() + "/" +bitName + ".jpg";
            Log.i(TAG,path);
            f = new File(path);
        }else {
            headimageFile = new File(getApplicationContext().getPackageResourcePath()
                    + "/" +Constants.CaptureFileName +  "headimage/");
            if(!headimageFile.exists()){
                headimageFile.mkdir();
            }
            String path = headimageFile.getAbsolutePath() + "/" +bitName + ".jpg";
            Log.i(TAG,path);
            f = new File(path);
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
        super.onActivityResult(requestCode, resultCode, data);
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
        if (object instanceof String && object.equals(Constants.INTERFACE_MSG_CODE_SUCCESS)) {
            ToastUtils.show(LoginActivity.this, Constants.INTERFACE_MSG_CODE_SUCCESS);
        } else if (object instanceof String && object.equals(Constants.STATUS_SUCCESS)) {
            ToastUtils.show(LoginActivity.this, "注册成功");
            initLoginView();
        } else if (object instanceof UserModel) {
            UserModel userModel = (UserModel) object;
            UserBean userBean = userModel.getUserBean();
            List<CategoryBean> categoryBeanList = userModel.getCategoryBeanList();
            //存用户信息
            PreferencesUtils.putBoolean(LoginActivity.this, Constants.PREFERENCES_IS_LOGIN, true);
            PreferencesUtils.putString(LoginActivity.this, Constants.PREFERENCES_USER_LOGINID, userBean.getId());
            PreferencesUtils.putString(LoginActivity.this, Constants.PREFERENCES_USER_ADDRESS,
                    userBean.getProvince() + userBean.getCity());
            PreferencesUtils.putString(LoginActivity.this, Constants.PREFERENCES_USER_STATUS, userBean.getStatus());
            PreferencesUtils.putString(LoginActivity.this, Constants.PREFERENCES_USER_HEADIMG, userBean.getHeadimgurl());
            PreferencesUtils.putString(LoginActivity.this, Constants.PREFERENCES_USER_CITY, userBean.getCity());
            PreferencesUtils.putString(LoginActivity.this, Constants.PREFERENCES_USER_CCREATE_DATE, userBean.getCreatedate());
            PreferencesUtils.putString(LoginActivity.this, Constants.PREFERENCES_USER_MOBILE, userBean.getMobile());
            PreferencesUtils.putString(LoginActivity.this, Constants.PREFERENCES_USER_PROVINCE, userBean.getProvince());
            PreferencesUtils.putString(LoginActivity.this, Constants.PREFERENCES_USERNAME, userBean.getUsername());
            PreferencesUtils.putString(LoginActivity.this, Constants.PREFERENCES_USERID, userBean.getUuid());
            PreferencesUtils.putString(LoginActivity.this, Constants.PREFERENCES_USER_WECHAT_NUM, userBean.getWechatno());
            //存古玩分类信息
            if (ListUtils.getSize(categoryBeanList) != 0) {
                sqliteUtils.clearCatefory();
                for (int i = 0; i < categoryBeanList.size(); i++) {
                    sqliteUtils.insertCategory(categoryBeanList.get(i).getId(),
                            categoryBeanList.get(i).getName());
                }
            }
            sqliteUtils.getDb().close();
            finish();
        }
    }

    @Override
    public void failure(String status) {

    }
}
