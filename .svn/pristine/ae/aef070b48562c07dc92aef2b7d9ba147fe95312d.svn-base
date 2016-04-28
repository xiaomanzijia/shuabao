package com.jhlc.km.sb.oss;

import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by OSS on 2015/12/8 0008.
 * 完成断点上传的任务管理
 */
public class MultiPartUploadManager {
    //任务信息保存在内存中，如果需要重启之后依旧能够使用，应该使用文件系统来保存这些信息
    private Map<String, String> multiPartStatus = new ConcurrentHashMap<>();

    private OSS oss;
    private String bucket;
    private int partSize;
    private ImageDisplayer ImageDisplayer;

    public MultiPartUploadManager(OSS oss, String bucket, int partSize, ImageDisplayer ImageDisplayer) {
        this.oss = oss;
        this.bucket = bucket;
        this.partSize = partSize;
        this.ImageDisplayer = ImageDisplayer;
    }

    public PauseableUploadTask asyncUpload(String objectName,
                                           String localFileName,
                                           @NonNull final OSSCompletedCallback<PauseableUploadRequest, PauseableUploadResult> userCallback,
                                           final OSSProgressCallback<PauseableUploadRequest> userProgressCallback) {

        final String object = new String(objectName);
        final String localFile = new String(localFileName);

        final PauseableUploadRequest request = new PauseableUploadRequest(bucket, object, localFile, partSize);
        /*
        request.setProgressCallback(new OSSProgressCallback<PauseableUploadRequest>() {
            //上传回调，这里的进度是指整个分片上传的进度
            @Override
            public void onProgress(PauseableUploadRequest pauseableUploadRequest, long currentSize, long totalSize) {
                int progress = (int) (100 * currentSize / totalSize);

                ImageDisplayer.updateProgress(progress);
                ImageDisplayer.displayInfo("上传进度: " + String.valueOf(progress) + "%");
            }
        });*/

        if (userProgressCallback != null) {
            request.setProgressCallback(userProgressCallback);
        }

        final PauseableUploadTask task = new PauseableUploadTask(oss, request, userCallback);

        Log.d("AsyncMultiPartUpload", "Begin");
        Log.d("Object", objectName);
        Log.d("LocalFile", localFileName);

        //因为demo中只允许一个断点上传任务，因此简单使用新线程来完成任务，实际使用过程中如果需要减少资源消耗，可以使用线程池来代替
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //计算上传任务的唯一校验码，将文件的MD5、bucket、object和分片大小编码成一个校验码来校验本次上传任务和上次上传任务是否一致
                    String fileMd5 = BinaryUtil.calculateMd5Str(localFile);
                    String totalMd5 = BinaryUtil.calculateMd5Str((fileMd5 + bucket + object + String.valueOf(partSize)).getBytes());

                    Log.d("MultipartUploadMd5", totalMd5);

                    String uploadId = multiPartStatus.get(totalMd5);

                    //如果没有找到对应的上传记录，表示是新的上传任务，因此使用initupload来初始化一个断点上传（分片上传）任务
                    if (uploadId == null) {
                        uploadId = task.initUpload();
                        Log.d("InitUploadId", uploadId);
                        multiPartStatus.put(totalMd5, uploadId);
                    }
                    else {
                        //如果之前已经存在一样的上传任务，那么继续使用上次的uploadId
                        Log.d("GetPausedUploadId", uploadId);
                    }

                    //使用uploadId来完成上传
                    task.upload(uploadId);
                    if (task.isComplete()) {
                        multiPartStatus.remove(totalMd5);
                    }
                }
                catch (ServiceException e) {
                    e.printStackTrace();
                }
                catch (ClientException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        return task;
    }
}
