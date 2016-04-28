package com.jhlc.km.sb.common;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.model.BaseModel;
import com.jhlc.km.sb.model.CategoryModel;
import com.jhlc.km.sb.model.LikeAntiqueCountModel;
import com.jhlc.km.sb.model.MobileCheckModel;
import com.jhlc.km.sb.model.RegistModel;
import com.jhlc.km.sb.model.SModel;
import com.jhlc.km.sb.model.ThumbCountBean;
import com.jhlc.km.sb.model.UserModel;
import com.jhlc.km.sb.model.WeChatOpenIdBean;
import com.jhlc.km.sb.utils.PreferencesUtils;
import com.jhlc.km.sb.utils.ServerUtils;
import com.jhlc.km.sb.utils.ToastUtils;
import com.jhlc.km.sb.volley.IRequest;
import com.jhlc.km.sb.volley.RequestJsonListener;
import com.jhlc.km.sb.volley.RequestListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by licheng on 30/3/16.
 */
public class ServerInterfaceHelper {

    private Listenter listenter;
    private Context mContext;

    public ServerInterfaceHelper(Listenter listenter,Context context) {
        this.listenter = listenter;
        this.mContext = context;
    }

    /**
     * 初始化用户
     * @param TAG 标识
     */
    public void initregist(final String TAG){
        Map<String,String> requestParams = new HashMap<>();
        requestParams.put("op", Constants.INTERFACE_INITREGIST);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(requestParams,TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    RegistModel registModel = new Gson().fromJson(jsonBody,RegistModel.class);
                    if(Constants.STATUS_SUCCESS.equals(registModel.getStatus())){
                        listenter.success(registModel.getUserid());
                    }else if(Constants.STATUS_FAILURE.equals(registModel.getStatus())){
                        ToastUtils.show(mContext,registModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });

    }

    /**
     * 获取验证码
     * @param TAG 标识
     * @param phonenum 手机号码
     */
    public void getServerYzM(final String TAG, String phonenum){
        Map<String,String> requestParams = new HashMap<>();
        requestParams.put("op", Constants.INTERFACE_MSG_CODE);
        requestParams.put("mobile",phonenum);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(requestParams,TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.INTERFACE_MSG_CODE_SUCCESS);
                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });

    }

    /**
     * 用户注册
     * @param TAG 标识
     * @param phonenum 手机号码
     * @param code 验证码
     * @param pwd 密码
     */
    public void ServerRegist(final String TAG,String phonenum, String code, String pwd, String userid) {
        Map<String, String> registparams = new HashMap<>();
        registparams.put("op", Constants.INTERFACE_REGIST);
        registparams.put("mobile", phonenum);
        registparams.put("code", code);
        registparams.put("pwd", pwd);
        registparams.put("userid", userid);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(registparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(baseModel.getStatus());
                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 用户登录
     * @param TAG 标识
     * @param phonenum 手机号码
     * @param pwd 密码
     */
    public void ServerLogin(final String TAG,String phonenum, String pwd) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_LOGIN);
        loginparams.put("mobile", phonenum);
        loginparams.put("pwd", pwd);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    UserModel userModel = new Gson().fromJson(jsonBody,UserModel.class);
                    if(Constants.STATUS_SUCCESS.equals(userModel.getStatus())){
                        listenter.success(userModel);

                    }else if(Constants.STATUS_FAILURE.equals(userModel.getStatus())){
                        ToastUtils.show(mContext,userModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }


    /**
     * 修改用户信息
     * @param TAG 标识
     * @param id 用户id
     * @param edittype 编辑项目
     * @param editcontent 编辑内容
     */
    public void updateUserInfo(final String TAG,String id, String edittype,String editcontent) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_UPDATE_UERINFO);
        loginparams.put("id", id);
        loginparams.put(edittype, editcontent);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.INTERFACE_EDITUSERINFO_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 更改用户地址信息
     * @param TAG 标识
     * @param id 用户id
     * @param province 省份
     * @param city 城市
     */
    public void updateUserInfoAddress(final String TAG,String id, String province, String city) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_UPDATE_UERINFO);
        loginparams.put("id", id);
        loginparams.put("province", province);
        loginparams.put("city", city);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.INTERFACE_EDITUSERINFO_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 用户手机号修改
     * @param TAG 标识
     * @param mobile 手机号
     * @param code 验证码
     */
    public void updateMobile(final String TAG, String mobile, String code) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_UPDATE_MOBILE);
        loginparams.put("id", PreferencesUtils.getString(mContext,
                Constants.PREFERENCES_USERID));
        loginparams.put("mobile", mobile);
        loginparams.put("code", code);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.INTERFACE_EDITUSERINFO_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 用户手机号验证
     * @param TAG 标识
     * @param mobile 手机号
     * @param code 验证码
     */
    public void mobileCodeCheck(final String TAG, String mobile, String code) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_MOBILE_CODECHECK);
        loginparams.put("mobile", mobile);
        loginparams.put("code", code);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    MobileCheckModel mobileCheckModel = new Gson().fromJson(jsonBody,MobileCheckModel.class);
                    if(Constants.STATUS_SUCCESS.equals(mobileCheckModel.getStatus())){
                        listenter.success(mobileCheckModel);

                    }else if(Constants.STATUS_FAILURE.equals(mobileCheckModel.getStatus())){
                        ToastUtils.show(mContext,mobileCheckModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }


    /**
     * 用户密码修改
     * @param TAG 标识
     * @param mobile 手机号
     * @param code 验证码
     * @param pwd 密码
     */
    public void updatePwd(final String TAG, String mobile,String code,String pwd) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_UPDATE_PWD);
        loginparams.put("pwd", pwd);
        loginparams.put("mobile", mobile);
        loginparams.put("code", code);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.INTERFACE_EDIT_PWD_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 发布古玩
     * @param TAG 标识
     * @param name 用户名
     * @param indeximage 首页图片
     * @param price 价格
     * @param catagoryid 分类id
     * @param describe 宝贝描述
     * @param images 宝贝图片
     */
    public void createAntique(final String TAG, String name,String indeximage,String price,String catagoryid,String describe,String images) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_CREATE_ANTIQUE);
        loginparams.put("userid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        loginparams.put("name", name);
        loginparams.put("indeximage", indeximage);
        loginparams.put("price", price);
        loginparams.put("catagoryid", catagoryid);
        loginparams.put("describe", describe);
        loginparams.put("images", images);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.INTERFACE_CREATE_ANTIQUES_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 删除古玩
     * @param TAG 标识
     * @param id 古玩id
     */
    public void deleteAntique(final String TAG, String id) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_DELETE_ANTIQUE);
        loginparams.put("userid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        loginparams.put("id", id);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.STATUS_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 获取古玩类别
     * @param TAG
     */
    public void getCatagorys(final String TAG) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_GET_CATEGORYS);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    CategoryModel categoryModel = new Gson().fromJson(jsonBody,CategoryModel.class);
                    if(Constants.STATUS_SUCCESS.equals(categoryModel.getStatus())){
                        listenter.success(categoryModel);

                    }else if(Constants.STATUS_FAILURE.equals(categoryModel.getStatus())){
                        ToastUtils.show(mContext,categoryModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 更新古玩信息
     * @param TAG 标识
     * @param id 古玩id
     * @param describe 描述
     * @param name 古玩名称
     * @param price 自估价
     */
    public void updateAntique(final String TAG, String id, String describe, String name, String price, String catagoryid) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_UPDATE_ANTIQUE);
        loginparams.put("userid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        loginparams.put("id", id);
        loginparams.put("describe", describe);
        loginparams.put("name", name);
        loginparams.put("price", price);
        loginparams.put("catagoryid", catagoryid);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.STATUS_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 收藏古玩
     * @param TAG 标识
     * @param antiqueid 古玩id
     */
    public void doCollection(final String TAG, String antiqueid) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_DO_COLLECTION);
        loginparams.put("userid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        loginparams.put("antiqueid", antiqueid);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.INTERFACE_DO_COLLECTION_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 取消收藏
     * @param TAG 标识
     * @param antiqueid 古玩id
     */
    public void cancelCollection(final String TAG, String antiqueid) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_CANCEL_COLLECTION);
        loginparams.put("userid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        loginparams.put("id", antiqueid);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.INTERFACE_CANCEL_COLLECTION_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 反馈
     * @param TAG 标识
     * @param mobile 手机号
     * @param content 反馈内容
     */
    public void doFeedback(final String TAG, String mobile ,String content) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_FEEDBACK);
        loginparams.put("userid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        loginparams.put("mobile", mobile);
        loginparams.put("content", content);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.INTERFACE_FEEDBACK_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 举报
     * @param TAG 标识
     * @param id 古玩id
     * @param content 举报内容
     */
    public void doReport(final String TAG, String id ,String content) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_REPORT);
        loginparams.put("userid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        loginparams.put("id", id);
        loginparams.put("content", content);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.INTERFACE_REPORT_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 评论
     * @param TAG 标识
     * @param content 评论内容
     * @param anitqueid 古玩id
     * @param parentid 父级id
     */
    public void comment(final String TAG, String content, String anitqueid, String parentid) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_COMMENT);
        loginparams.put("userid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        loginparams.put("anitqueid", anitqueid);
        loginparams.put("parentid", parentid);
        loginparams.put("content", content);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.INTERFACE_COMMENT_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 关注
     * @param TAG 标识
     * @param follow 被关注用户id
     */
    public void doFollow(final String TAG, String follow) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_FOLLOW);
        loginparams.put("userid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        loginparams.put("follow", follow);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.INTERFACE_FOLLOW_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 点赞
     * @param TAG 标识
     * @param id 古玩id
     */
    public void doLike(final String TAG, String id) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_DOLIKE);
        loginparams.put("userid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        loginparams.put("id", id);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.INTERFACE_LIKE_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 取消关注
     * @param TAG 标识
     * @param follow 被关注人id
     */
    public void cancelFollow(final String TAG, String follow) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_CANCLEFOLLOW);
        loginparams.put("userid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        loginparams.put("follow", follow);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.INTERFACE_CACLEFOLLOW_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 获取用户信息
     * @param TAG 标识
     * @param userid 获取用户id
     */
    public void GetUserInfoByID(final String TAG, String userid) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_GETUSRINFOBYID);
        loginparams.put("myuserid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        loginparams.put("userid", userid);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    UserModel userModel = new Gson().fromJson(jsonBody,UserModel.class);
                    if(Constants.STATUS_SUCCESS.equals(userModel.getStatus())){
                        listenter.success(userModel);

                    }else if(Constants.STATUS_FAILURE.equals(userModel.getStatus())){
                        ToastUtils.show(mContext,userModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 用户中心点赞宝贝数量统计
     * @param TAG 标识
     */
    public void computeByUserID(final String TAG) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_COMPUTEBYUSERID);
        loginparams.put("userid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    LikeAntiqueCountModel likeAntiqueCountModel = new Gson().fromJson(jsonBody,LikeAntiqueCountModel.class);
                    if(Constants.STATUS_SUCCESS.equals(likeAntiqueCountModel.getStatus())){
                        listenter.success(likeAntiqueCountModel);

                    }else if(Constants.STATUS_FAILURE.equals(likeAntiqueCountModel.getStatus())){
                        ToastUtils.show(mContext,likeAntiqueCountModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 宝贝详情评论点赞
     * @param TAG 标识
     * @param commentid 评论id
     */
    public void doLikeToComment(final String TAG, String commentid) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_DOLIKETOCOMMENT);
        loginparams.put("id", commentid);
        loginparams.put("userid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(Constants.INTERFACE_LIKETOCOMMENT_SUCCESS);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    /**
     * 带数据返回的评论点赞
     * @param TAG 标识
     * @param commentid 评论id
     * @param thumbCountBean 评论数据
     */
    public void doLikeToCommentWithThumb(final String TAG, String commentid, final ThumbCountBean thumbCountBean) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_DOLIKETOCOMMENT);
        loginparams.put("id", commentid);
        loginparams.put("userid", PreferencesUtils.getString(mContext,Constants.PREFERENCES_USERID));
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    BaseModel baseModel = new Gson().fromJson(jsonBody,BaseModel.class);
                    if(Constants.STATUS_SUCCESS.equals(baseModel.getStatus())){
                        listenter.success(thumbCountBean);

                    }else if(Constants.STATUS_FAILURE.equals(baseModel.getStatus())){
                        ToastUtils.show(mContext,baseModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }


    /**
     * 第三方登陆
     * @param TAG 标识
     * @param uuid 用户唯一编号
     * @param logintype 登陆类型 1 QQ 2 微信
     * @param loginaccount 用户账号
     * @param username 用户昵称
     * @param headimgurl 用户三方头像
     */
    public void doThirdLogin(final String TAG, String uuid, String logintype, String loginaccount, String username, String headimgurl) {
        Map<String, String> loginparams = new HashMap<>();
        loginparams.put("op", Constants.INTERFACE_THIRDLOGIN);
        loginparams.put("uuid", uuid);
        loginparams.put("logintype", logintype);
        loginparams.put("loginaccount", loginaccount);
        loginparams.put("username", username);
        loginparams.put("headimgurl", headimgurl);
        IRequest.post(mContext, Constants.SERVER_URL, SModel.class, ServerUtils.getRequestParams(loginparams, TAG), new RequestJsonListener<SModel>() {
            @Override
            public void requestSuccess(SModel result) {
                try {
                    String jsonBody = Des3.decode(result.getS());
                    Log.i(TAG,jsonBody);
                    UserModel userModel = new Gson().fromJson(jsonBody,UserModel.class);
                    if(Constants.STATUS_SUCCESS.equals(userModel.getStatus())){
                        listenter.success(userModel);

                    }else if(Constants.STATUS_FAILURE.equals(userModel.getStatus())){
                        ToastUtils.show(mContext,userModel.getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError(VolleyError e) {
                ToastUtils.show(mContext,Constants.INTERFACE_QUEST_FAILER);
            }
        });
    }

    //根据code获取微信access_token值
    public void getWeChatAccessToken(final String TAG, String code){
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                "appid="+Constants.WeChat_APP_ID+"&secret="+Constants.WeChat_APP_SECRET+"&code="+code+"&grant_type=authorization_code";
        IRequest.get(mContext, url, WeChatOpenIdBean.class, new RequestJsonListener<WeChatOpenIdBean>() {
            @Override
            public void requestSuccess(WeChatOpenIdBean result) {
                listenter.success(result);
            }

            @Override
            public void requestError(VolleyError e) {

            }
        });
    }


    public interface Listenter<T>{
        void success(T object);
        void failure(String status);
    }
}
