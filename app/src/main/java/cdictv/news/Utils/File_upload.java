package cdictv.news.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.TransferStateListener;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

import java.net.MalformedURLException;
import java.net.URL;

import cdictv.news.JavaBean.User;


public class File_upload {
    private CosXmlService cosXmlService;
    private final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 10001;
    private TransferManager transferManager;
    private Context context;

     public File_upload(Context context, String path, User user){
         this.context = context;
        initCosService("1251684550",
                "ap-chengdu", // bucket 的地域
                "https://songtell-1251684550.cos.ap-chengdu.myqcloud.com/sign.json"); // 临时密钥服务地址

         String bucket = "songtell-1251684550";
         String cosPath = "news/"+user.getName()+"Photo.jpg";
         String localPath = path;

         upload(bucket, cosPath, localPath);
    }

    private void upload(String bucket, String cosPath, String localPath) {

        // 开始上传，并返回生成的 COSXMLUploadTask
        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, cosPath,
                localPath, null);

        // 设置上传状态监听
        cosxmlUploadTask.setTransferStateListener(new TransferStateListener() {
            @Override
            public void onStateChanged(final TransferState state) {
                // TODO: 2018/10/22
            }
        });

        // 设置上传进度监听
        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(final long complete, final long target) {
                Log.i("*************", "onProgress: "+complete+"***********"+target);
            }
        });

        // 设置结果监听
        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // TODO: 2018/10/22
                Toast.makeText(context,"上传成功",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                // TODO: 2018/10/22
                Toast.makeText(context,"上传失败",Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void initCosService(String appid, String region, String signUrl) {

        CosXmlServiceConfig cosXmlServiceConfig = new CosXmlServiceConfig.Builder()
                .setAppidAndRegion(appid, region)
                .setDebuggable(true)
                .builder();

        URL url = null;

        try {
            url = new URL(signUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        /**
         * 初始化 {@link QCloudCredentialProvider} 对象，来给 SDK 提供临时密钥。
         */
        String secretId = "AKIDTlT1982eFesxuJPq01jJk184pSw6d1jr";
        String secretKey ="KUkqZAe9EAvkPyN7SlXAe8RhOxeEeXQE";

/**
 * 初始化 {@link QCloudCredentialProvider} 对象，来给 SDK 提供临时密钥。
 */
        QCloudCredentialProvider credentialProvider = new ShortTimeCredentialProvider(secretId,
                secretKey, 300);

        cosXmlService =  new CosXmlService(context,cosXmlServiceConfig, credentialProvider);

        TransferConfig transferConfig = new TransferConfig.Builder().build();
        transferManager = new TransferManager(cosXmlService, transferConfig);
    }


}
