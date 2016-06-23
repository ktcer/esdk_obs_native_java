/**
* Copyright 2015 Huawei Technologies Co., Ltd. All rights reserved.
* eSDK is licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.huawei.obs.services.multithread;
//package com.huawei.obs.services.multithread;
//
//import java.io.IOException;
//import java.security.NoSuchAlgorithmException;
//import java.util.Date;
//
//import org.apache.log4j.Logger;
//import org.jets3t.service.Jets3tProperties;
//import org.jets3t.service.ServiceException;
//import org.jets3t.service.StorageService;
//import org.jets3t.service.impl.rest.httpclient.RestS3Service;
//import org.jets3t.service.model.InterfaceLogBean;
//import org.jets3t.service.model.StorageObject;
//import org.jets3t.service.multi.StorageServiceEventListener;
//import org.jets3t.service.multi.ThreadedStorageService;
//import org.jets3t.service.security.AWSCredentials;
//import org.jets3t.service.security.ProviderCredentials;
//
//import com.huawei.obs.log.RunningLog;
//import com.huawei.obs.services.Convert;
//import com.huawei.obs.services.ObsConfiguration;
//import com.huawei.obs.services.exception.ObsException;
//import com.huawei.obs.services.model.S3Object;
//
///**
// * 提供访问OBS系统服务的Java本地接口,内部采用了多线程的方式，提供一次执行多个请求的服务。
// */
//public class MosThreadedStorageService
//{
//    private static final Logger ilog = Logger.getLogger(MosThreadedStorageService.class);
//
//    private RunningLog runningLog = RunningLog.getRunningLog();
//
//    ThreadedStorageService threadedStorageService = null;
//
//    /**
//     * 指定配置参数的构造函数
//     * 
//     * @param accessId Access Key
//     * @param accessKey Secret Access Key
//     * @param config 配置信息
//     * @throws ObsException ObsException
//     */
//    public MosThreadedStorageService(String accessId, String accessKey, ObsConfiguration config,
//        MosStorageServiceEventListener listener) throws ObsException
//    {
//        InterfaceLogBean reqBean = new InterfaceLogBean("MosThreadedStorageService", config.getEndPoint(), "");
//
//        ProviderCredentials credentials = new AWSCredentials(accessId, accessKey);
//        Jets3tProperties jets3tProperties = new Jets3tProperties();
//        configfieldToProperties(config, jets3tProperties);
//        try
//        {
//            this.runningLog.debug(
//                "MosThreadedStorageService",
//                "accessId:" + accessId + ", endPoint:" + config.getEndPoint() + ", MaxConnections:"
//                    + config.getMaxConnections());
//            StorageService service = new RestS3Service(credentials, null, null, jets3tProperties);
//            StorageServiceEventListener s3Listener = new StorageServiceEventListenerImpl(listener);
//            this.threadedStorageService = new ThreadedStorageService(service, s3Listener);
//
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode("0");
//            ilog.info(reqBean);
//        }
//        catch (ServiceException e)
//        {
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode(e.getErrorCode());
//            ilog.error(reqBean);
//            this.runningLog.error("MosThreadedStorageService", "Exception:" + e.getXmlMessage());
//            throw Convert.changeFromS3Exception(e);
//        }
//    }
//    
//    /**
//     * 一次创建多个对象<br/>
//     * @param bucketName
//     * @return boolean
//     * @throws IOException 
//     * @throws NoSuchAlgorithmException 
//     */
//    public boolean putObjects(String bucketName, S3Object[] s3Objects) throws NoSuchAlgorithmException, IOException
//    {
//        StorageObject[] storageObjects = new StorageObject[s3Objects.length];
//        for (int i = 0; i < storageObjects.length; i++)
//        {
//            storageObjects[i] = Convert.changToSS3Object(s3Objects[i]);
//        }
//        return this.threadedStorageService.putObjects(bucketName, storageObjects);
//    }
//    
//    /**
//     * 一次下载多个对象
//     * @param bucketName
//     * @param objectKeys
//     * @return boolean
//     */
//    public boolean getObjects(final String bucketName, final String[] objectKeys)
//    {
//        return this.threadedStorageService.getObjects(bucketName, objectKeys);
//    }
//    
////    /**
////     * @param bucketName 下载对象所属的桶
////     * @param downloadPackages TODO
////     */
////    public boolean downloadObjects(String bucketName, DownloadPackage[] downloadPackages)
////    {
////        try
////        {
////            return this.threadedStorageService.downloadObjects(bucketName, downloadPackages);
////        }
////        catch (ServiceException e)
////        {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////            return false;
////        }
////    }
//    
//    private void configfieldToProperties(ObsConfiguration config, Jets3tProperties jets3tProperties)
//    {
//        jets3tProperties.setProperty("hw-endpoint", config.getEndPoint());
//        jets3tProperties.setProperty("hw-endpoint-http-port", String.valueOf(config.getEndpointHttpPort()));
//        jets3tProperties.setProperty("hw.https-only", String.valueOf(config.isHttpsOnly()));
//        jets3tProperties.setProperty("hw.disable-dns-buckets", String.valueOf(config.isDisableDnsBucket()));
//        jets3tProperties.setProperty("hw-endpoint-https-port", String.valueOf(config.getEndpointHttpsPort()));
//        jets3tProperties.setProperty("httpclient.socket-timeout-ms", String.valueOf(config.getSocketTimeout()));
//        jets3tProperties.setProperty("httpclient.max-connections", String.valueOf(config.getMaxConnections()));
//        jets3tProperties.setProperty("threaded-service.max-thread-count", String.valueOf(config.getMaxConnections()));
//        jets3tProperties.setProperty("httpclient.retry-max", String.valueOf(config.getMaxErrorRetry()));
//        jets3tProperties.setProperty("httpclient.connection-timeout-ms", String.valueOf(config.getConnectionTimeout()));
//        jets3tProperties.setProperty("s3service.default-bucket-location",
//            String.valueOf(config.getDefaultBucketLocation()));
//    }
//}
