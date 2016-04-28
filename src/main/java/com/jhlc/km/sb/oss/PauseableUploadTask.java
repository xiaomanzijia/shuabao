package com.jhlc.km.sb.oss;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.InitiateMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.ListPartsRequest;
import com.alibaba.sdk.android.oss.model.ListPartsResult;
import com.alibaba.sdk.android.oss.model.PartETag;
import com.alibaba.sdk.android.oss.model.PartSummary;
import com.alibaba.sdk.android.oss.model.UploadPartRequest;
import com.alibaba.sdk.android.oss.model.UploadPartResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by OSS on 2015/12/8 0008.
 * 断点上传的任务
 * 目前Android SDK并不支持中途暂停任务，因此需要自己使用MulltiPart上传来包装一个支持暂停的断点续传功能
 * SDK中有ResumableUpload，同样也是使用MultiPart上传来实现的，ResumableUpload不支持暂停，
 * 但是可以在程序重启、网络出错等情况下使用同样的参数调用来实现断点续传，因为在该接口内部将正在执行的上传信息保存在了文件系统中。
 */
public class PauseableUploadTask {
    private OSS oss;
    private PauseableUploadRequest request;
    private OSSCompletedCallback<PauseableUploadRequest, PauseableUploadResult> callback;

    private List<PartETag> partETags = new ArrayList<>();

    private long currentUploadLength = 0;
    private long fileLength = 0;

    private boolean isPaused = false;
    private boolean isComplete = false;

    public PauseableUploadTask(OSS oss, PauseableUploadRequest request, OSSCompletedCallback<PauseableUploadRequest, PauseableUploadResult> callback) {
        this.oss = oss;
        this.request = request;
        this.callback = callback;
    }

    public synchronized void pause() {
        isPaused  = true;
    }
    public synchronized boolean isPause() {
        return isPaused;
    }

    public synchronized void setComplete() { isComplete = true; }
    public synchronized boolean isComplete() { return isComplete; }

    //使用指定的uploadId进行上传
    public void upload(String uploadId) throws ClientException, ServiceException, IOException {
        final String bucket = request.getBucket();
        final String object = request.getObjectKey();
        final String localFile = request.getLocalFile();
        final int partSize = request.getPartSize();

        try {
            //先通过uploadId去看对应的Id的Parts情况
            ListPartsRequest listParts = new ListPartsRequest(bucket, object, uploadId);
            ListPartsResult listResult = oss.listParts(listParts);

            Log.d("ListPartsFound", String.valueOf(listResult.getParts().size()));

            for (PartSummary part : listResult.getParts()) {
                partETags.add(new PartETag(part.getPartNumber(), part.getETag()));

            }

            long blockSize = partSize;
            int currentUploadIndex = partETags.size() + 1;
            File uploadFile = new File(localFile);
            fileLength = uploadFile.length();
            int totalBlockNum;

            final OSSProgressCallback<PauseableUploadRequest> progressCallback = request.getProgressCallback();

            totalBlockNum = (int) (fileLength / blockSize) + (fileLength % blockSize == 0 ? 0 : 1);
            if (currentUploadIndex <= totalBlockNum) {
                currentUploadLength = blockSize * (currentUploadIndex - 1);
            } else {
                currentUploadLength = fileLength;
            }

            InputStream in = new FileInputStream(uploadFile);

            long at = 0;
            while (at < currentUploadLength) {
                long realSkip = in.skip(currentUploadLength - at);
                if (realSkip == -1) {
                    throw new IOException("Skip failed! [fileLength]: " + fileLength + " [needSkip]: " + currentUploadLength);
                }
                at += realSkip;
            }

            while (currentUploadIndex <= totalBlockNum) {
                UploadPartRequest uploadPart = new UploadPartRequest(
                        bucket, object, uploadId, currentUploadIndex);

                uploadPart.setProgressCallback(new OSSProgressCallback<UploadPartRequest>() {
                    //通过UploadPart的进度回调来换算整体的进度
                    @Override
                    public void onProgress(UploadPartRequest request, long currentSize, long totalSize) {

                        if (progressCallback != null) {
                            progressCallback.onProgress(PauseableUploadTask.this.request, currentUploadLength + currentSize, fileLength);
                        }
                    }
                });

                int toUpload = (int) Math.min(blockSize, fileLength - currentUploadLength);
                uploadPart.setPartContent(IOUtils.readStreamAsBytesArray(in, toUpload));
                UploadPartResult uploadPartResult = oss.uploadPart(uploadPart);

                partETags.add(new PartETag(currentUploadIndex, uploadPartResult.getETag()));

                currentUploadLength += toUpload;
                currentUploadIndex++;
                Log.d("UploadPartIndex", String.valueOf(currentUploadIndex - 1));
                Log.d("UploadPartSize", String.valueOf(currentUploadLength));

                //如果暂停了就直接退出，需要断点续传的话直接使用同样的参数构建任务上传即可
                if (isPause()) {
                    Log.w("MultiPartUpload", "Pause");
                    Log.w("UploadId", uploadId);
                    return;
                }
            }

            CompleteMultipartUploadRequest complete = new CompleteMultipartUploadRequest(
                    bucket, object, uploadId, partETags);
            CompleteMultipartUploadResult completeResult = oss.completeMultipartUpload(complete);
            PauseableUploadResult result = new PauseableUploadResult(completeResult);

            setComplete();
            Log.d("multipartUpload", "multipart upload success! Location: " + completeResult.getLocation());

            callback.onSuccess(request, result);
        }
        catch (IOException e) {
            ClientException clientException = new ClientException(e.toString(), e);
            callback.onFailure(request, clientException, null);
            throw e;
        }
        catch (ServiceException e) {
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("RequestId", e.getRequestId());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
            callback.onFailure(request, null, e);
            throw e;
        }
        catch (ClientException e) {
            callback.onFailure(request, e, null);
            throw e;
        }
    }

    //如果是第一次上传，需要初始化uploadId
    public String initUpload() throws ClientException, ServiceException {
        try {
            String localFile = request.getLocalFile();
            String bucket = request.getBucket();
            String object = request.getObjectKey();

            Log.d("InitUpload", localFile);
            InitiateMultipartUploadRequest init = new InitiateMultipartUploadRequest(bucket, object);
            InitiateMultipartUploadResult initResult = oss.initMultipartUpload(init);
            return initResult.getUploadId();
        }
        catch (ServiceException e) {
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("RequestId", e.getRequestId());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
            callback.onFailure(request, null, e);
            throw e;
        }
        catch (ClientException e) {
            callback.onFailure(request, e, null);
            throw e;
        }
    }

}
