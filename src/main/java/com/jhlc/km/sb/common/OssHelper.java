package com.jhlc.km.sb.common;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.jhlc.km.sb.constants.Constants;
import com.jhlc.km.sb.oss.ImageDisplayer;
import com.jhlc.km.sb.oss.OssService;
import com.jhlc.km.sb.oss.STSGetter;

/**
 * Created by licheng on 5/4/16.
 */
public class OssHelper {

    private Context mContext;
    private ImageDisplayer displayer;

    public OssHelper(Context mContext) {
        this.mContext = mContext;
    }

    public OssService initOSS() {
        //如果希望直接使用accessKey来访问的时候，可以直接使用OSSPlainTextAKSKCredentialProvider来鉴权。
        //OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);

        //使用自己的获取STSToken的类
        OSSCredentialProvider credentialProvider = new STSGetter(Constants.stsServer);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        OSS oss = new OSSClient(mContext, Constants.endpoint, credentialProvider, conf);

        return new OssService(oss, Constants.bucket, displayer);

    }


}
