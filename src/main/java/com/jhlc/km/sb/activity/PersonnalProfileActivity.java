package com.jhlc.km.sb.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jhlc.km.sb.R;
import com.jhlc.km.sb.SbApplication;
import com.jhlc.km.sb.common.LocationService;
import com.jhlc.km.sb.common.ServerInterfaceHelper;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.utils.PreferencesUtils;
import com.jhlc.km.sb.utils.StringUtils;
import com.jhlc.km.sb.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by licheng on 23/3/16.
 */
public class PersonnalProfileActivity extends BaseActivity implements ServerInterfaceHelper.Listenter {


    @Bind(R.id.btnBack)
    ImageView btnBack;
    @Bind(R.id.textBack)
    TextView textBack;
    @Bind(R.id.llBack)
    LinearLayout llBack;
    @Bind(R.id.textTresureName)
    TextView textTresureName;
    @Bind(R.id.btnReport)
    TextView btnReport;
    @Bind(R.id.imgUserHead)
    SimpleDraweeView imgUserHead;
    @Bind(R.id.rlUserHead)
    RelativeLayout rlUserHead;
    @Bind(R.id.textUserName)
    TextView textUserName;
    @Bind(R.id.rlUserName)
    RelativeLayout rlUserName;
    @Bind(R.id.textWeChatNum)
    TextView textWeChatNum;
    @Bind(R.id.rlWeChatNum)
    RelativeLayout rlWeChatNum;
    @Bind(R.id.textMobile)
    TextView textMobile;
    @Bind(R.id.rlMobile)
    RelativeLayout rlMobile;
    @Bind(R.id.textAddress)
    TextView textAddress;
    @Bind(R.id.ibtnWeChatForward)
    ImageView ibtnForward;
    @Bind(R.id.rlAddress)
    RelativeLayout rlAddress;
    @Bind(R.id.textLeft)
    TextView textLeft;
    @Bind(R.id.btnQuit)
    Button btnQuit;

    private LocationService locationService;
    private double Lon;
    private double Lat;
    private String province;
    private String city;

    private ServerInterfaceHelper helper;

    private static final String TAG = "PersonnalProfileActivity";

    @Override
    public void initView() {
        super.initView();
        textTresureName.setText("个人信息");
        btnReport.setVisibility(View.GONE);
        initUserInfoView();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_layout);
        ButterKnife.bind(this);
        initView();
        initData();
        initBaiduLoc();
    }

    private void initData() {
        helper = new ServerInterfaceHelper(this,PersonnalProfileActivity.this);
    }

    private void initBaiduLoc() {
        /**
         * 获取百度地图定位服务
         */
        // -----------location config ------------
        locationService = ((SbApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        int type = getIntent().getIntExtra("from", 0);
        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        //开启定位
        locationService.start();
    }

    @OnClick({R.id.llBack, R.id.rlUserHead, R.id.rlUserName, R.id.rlWeChatNum, R.id.rlMobile, R.id.rlAddress, R.id.btnQuit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBack:
                finish();
                break;
            case R.id.rlUserHead:
                Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
                startActivity(intent);
                break;
            case R.id.rlUserName:
                toIntent(Constants.INTENT_TYPE_EDIT_USERINFO_USERNAME);
                break;
            case R.id.rlWeChatNum:
                toIntent(Constants.INTENT_TYPE_EDIT_USERINFO_WECHATNO);
                break;
            case R.id.rlMobile:
                toIntent(Constants.INTENT_TYPE_EDIT_USERINFO_MOBILE);
                break;
            case R.id.rlAddress:
                break;
            case R.id.btnQuit: //退出登陆
                PreferencesUtils.putBoolean(PersonnalProfileActivity.this, Constants.PREFERENCES_IS_LOGIN, false);
                helper.initregist(TAG);
                PreferencesUtils.putString(PersonnalProfileActivity.this, Constants.PREFERENCES_USER_STATUS, "0");
                finish();
                break;
        }
    }

    private void toIntent(String tag){
        Intent intent = new Intent(PersonnalProfileActivity.this,EditUserInfoActivity.class);
        intent.putExtra(Constants.INTENT_TYPE,Constants.INTENT_TYPE_EDIT_USERINFO);
        intent.putExtra(Constants.INTENT_TYPE_EDIT,tag);
        startActivity(intent);
    }

    private void initUserInfoView(){
        imgUserHead.setImageURI(Uri.parse(PreferencesUtils.getString(PersonnalProfileActivity.this,
                Constants.PREFERENCES_USER_HEADIMG)+Constants.OSS_IMAGE_SIZE100));
        textUserName.setText(PreferencesUtils.getString(PersonnalProfileActivity.this, Constants.PREFERENCES_USERNAME));
        textWeChatNum.setText(PreferencesUtils.getString(PersonnalProfileActivity.this, Constants.PREFERENCES_USER_WECHAT_NUM));
        textMobile.setText(PreferencesUtils.getString(PersonnalProfileActivity.this, Constants.PREFERENCES_USER_MOBILE));
        textAddress.setText(PreferencesUtils.getString(PersonnalProfileActivity.this,Constants.PREFERENCES_USER_ADDRESS));
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                Lon = location.getLongitude();
                Lat = location.getLatitude();
                city = location.getCity();
                province = location.getProvince();
                String userAddress = province + city;
                textAddress.setText(userAddress);
                //将位置信息存储到本地和服务器
                PreferencesUtils.putString(PersonnalProfileActivity.this,Constants.PREFERENCES_USER_ADDRESS,userAddress);
                helper.updateUserInfoAddress(TAG,PreferencesUtils.getString(PersonnalProfileActivity.this,
                        Constants.PREFERENCES_USERID),province,city);

                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\nDescribe: ");
                sb.append(location.getLocationDescribe());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());
                sb.append("\nPoi: ");
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                Log.i("baiduloc",sb.toString());
            }
        }

    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        initUserInfoView();
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }

    @Override
    public void success(Object object) {
        if(object instanceof String && object.equals(Constants.INTERFACE_EDITUSERINFO_SUCCESS)){
            locationService.stop(); //停止定位服务
        }else if(object instanceof String && !StringUtils.isBlank((String) object)){ //用户退出重新初始化userid存入本地
            String userid = (String) object;
            PreferencesUtils.putString(PersonnalProfileActivity.this,Constants.PREFERENCES_USERID,userid);
        }
    }

    @Override
    public void failure(String status) {

    }
}
