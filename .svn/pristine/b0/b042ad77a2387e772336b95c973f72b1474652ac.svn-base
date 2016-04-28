package com.jhlc.km.sb.oss;

import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;

/**
 * Created by Administrator on 2015/12/8 0008.
 */
public class PauseableUploadResult extends CompleteMultipartUploadResult {

    public PauseableUploadResult(CompleteMultipartUploadResult completeResult) {
        this.setBucketName(completeResult.getBucketName());
        this.setObjectKey(completeResult.getObjectKey());
        this.setETag(completeResult.getETag());
        this.setLocation(completeResult.getLocation());
        this.setRequestId(completeResult.getRequestId());
        this.setResponseHeader(completeResult.getResponseHeader());
        this.setStatusCode(completeResult.getStatusCode());
    }
}
