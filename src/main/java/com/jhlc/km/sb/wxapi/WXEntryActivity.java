package com.jhlc.km.sb.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.jhlc.km.sb.R;
import com.jhlc.km.sb.SbApplication;
import com.jhlc.km.sb.activity.MainActivity;
import com.jhlc.km.sb.common.BitmapCache;
import com.jhlc.km.sb.common.ServerInterfaceHelper;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.model.CategoryBean;
import com.jhlc.km.sb.model.UserBean;
import com.jhlc.km.sb.model.UserModel;
import com.jhlc.km.sb.model.WeChatOpenIdBean;
import com.jhlc.km.sb.model.WeChatUserInfoBean;
import com.jhlc.km.sb.utils.ListUtils;
import com.jhlc.km.sb.utils.PreferencesUtils;
import com.jhlc.km.sb.utils.SqliteUtils;
import com.jhlc.km.sb.utils.StringUtils;
import com.jhlc.km.sb.utils.ToastUtils;
import com.jhlc.km.sb.volley.IRequest;
import com.jhlc.km.sb.volley.RequestJsonListener;
import com.jhlc.km.sb.volley.RequestListener;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by licheng on 11/4/16.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler,ServerInterfaceHelper.Listenter {

    private final static String TAG = "WXEntryActivity";
    private IWXAPI api;

    private RequestQueue requestQueue = Volley.newRequestQueue(SbApplication
            .getContext());


    private SqliteUtils sqliteUtils;
    private ServerInterfaceHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sqliteUtils = new SqliteUtils(WXEntryActivity.this);
        helper = new ServerInterfaceHelper(this,WXEntryActivity.this);


        api = ((SbApplication)getApplication()).api;

        //将应用id注册到微信
//        api = WXAPIFactory.createWXAPI(getApplicationContext(), Constants.WeChat_APP_ID,true);
//        api.registerApp(Constants.WeChat_APP_ID);

        api.handleIntent(getIntent(), this);//在WXEntryActivity中将接收到的intent及
                                            // 实现了IWXAPIEventHandler接口的对象传递给IWXAPI接口的handleIntent方法

    }

    @Override
    public void onReq(BaseReq baseReq) { //微信发送的请求将回调到onReq方法

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onResp(BaseResp baseResp) { //发送到微信请求的响应结果将回调到onResp方法
        if(baseResp instanceof SendAuth.Resp){
            SendAuth.Resp newresp = (SendAuth.Resp) baseResp;
            String code = newresp.code; //获取微信返回的code
            Log.i(TAG,"code:"+code);
            if(!StringUtils.isBlank(code)){
                //根据code获取access_token
                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                        "appid="+Constants.WeChat_APP_ID+"&secret="+Constants.WeChat_APP_SECRET+"&code="+code+"&grant_type=authorization_code";
                Log.i(TAG,url);
                IRequest.get(WXEntryActivity.this, url, WeChatOpenIdBean.class, new RequestJsonListener<WeChatOpenIdBean>() {
                    @Override
                    public void requestSuccess(WeChatOpenIdBean result) {
                        if(result != null){
                            String accessToken = result.getAccess_token();
                            final String openid = result.getOpenid();
                            Log.i(TAG,"access_token:"+accessToken);
                            Log.i(TAG,"openid:"+openid);
                            final String wechatUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?" +
                                    "access_token="+accessToken+"&openid="+openid;
                            IRequest.get(WXEntryActivity.this, wechatUserInfoUrl, WeChatUserInfoBean.class, new RequestJsonListener<WeChatUserInfoBean>() {
                                @Override
                                public void requestSuccess(WeChatUserInfoBean result) {
                                    final String nickname = result.getNickname();
                                    Log.i(TAG,"nickname:"+nickname);
                                    Log.i(TAG,"province:"+result.getProvince());
                                    String headimgurl = result.getHeadimgurl();
                                    helper.doThirdLogin(TAG, PreferencesUtils.getString(WXEntryActivity.this,Constants.PREFERENCES_USERID),
                                            "1", openid,nickname,"");
                                    try {
                                        String wechatimgurl = headimgurl.substring(0,headimgurl.lastIndexOf("/"))+Constants.WECHAT_IMAGE_SIZE64;
                                        Log.i(TAG,"headimgurl:" + wechatimgurl);
//                                        imageLoader.get(wechatimgurl,listener,200,200);
                                        ImageRequest imageRequest = new ImageRequest(wechatimgurl, new Response.Listener<Bitmap>() {
                                            @Override
                                            public void onResponse(Bitmap bitmap) {
                                                saveBitmap(bitmap,nickname); //把头像保存到本地
                                            }
                                        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError volleyError) {
                                            }
                                        });
                                        requestQueue.add(imageRequest);
                                    }catch (Exception e){}
                                }

                                @Override
                                public void requestError(VolleyError e) {

                                }
                            });
                        }
                    }

                    @Override
                    public void requestError(VolleyError e) {

                    }
                });
            }
        }else if(baseResp instanceof SendMessageToWX.Resp){
            int result = 0;
            switch (baseResp.errCode){
                case BaseResp.ErrCode.ERR_OK:
                    result = R.string.errcode_success;
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = R.string.errcode_cancel;
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = R.string.errcode_deny;
                    break;
                default:
                    result = R.string.errcode_unknown;
                    break;
            }
            Toast.makeText(WXEntryActivity.this,result,Toast.LENGTH_LONG).show();
        }
    }


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
    public void success(Object object) {
        if(object instanceof UserModel){
            UserModel userModel = (UserModel) object;
            UserBean userBean = userModel.getUserBean();
            List<CategoryBean> categoryBeanList = userModel.getCategoryBeanList();
            //存用户信息
            PreferencesUtils.putBoolean(WXEntryActivity.this, Constants.PREFERENCES_IS_LOGIN, true);
            PreferencesUtils.putString(WXEntryActivity.this, Constants.PREFERENCES_USER_LOGINID, userBean.getId());
            PreferencesUtils.putString(WXEntryActivity.this, Constants.PREFERENCES_USER_ADDRESS,
                    userBean.getProvince() + userBean.getCity());
            PreferencesUtils.putString(WXEntryActivity.this, Constants.PREFERENCES_USER_STATUS, userBean.getStatus());
            PreferencesUtils.putString(WXEntryActivity.this, Constants.PREFERENCES_USER_HEADIMG, userBean.getHeadimgurl());
            PreferencesUtils.putString(WXEntryActivity.this, Constants.PREFERENCES_USER_CITY, userBean.getCity());
            PreferencesUtils.putString(WXEntryActivity.this, Constants.PREFERENCES_USER_CCREATE_DATE, userBean.getCreatedate());
            PreferencesUtils.putString(WXEntryActivity.this, Constants.PREFERENCES_USER_MOBILE, userBean.getMobile());
            PreferencesUtils.putString(WXEntryActivity.this, Constants.PREFERENCES_USER_PROVINCE, userBean.getProvince());
            PreferencesUtils.putString(WXEntryActivity.this, Constants.PREFERENCES_USERNAME, userBean.getUsername());
            PreferencesUtils.putString(WXEntryActivity.this, Constants.PREFERENCES_USERID, userBean.getUuid());
            PreferencesUtils.putString(WXEntryActivity.this, Constants.PREFERENCES_USER_WECHAT_NUM, userBean.getWechatno());
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
