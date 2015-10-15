/*
 * Copyright Notice:
 *      Copyright  1998-2015, Huawei Technologies Co., Ltd.  ALL Rights Reserved.
 *
 *      Warning: This computer software sourcecode is protected by copyright law
 *      and international treaties. Unauthorized reproduction or distribution
 *      of this sourcecode, or any portion of it, may result in severe civil and
 *      criminal penalties, and will be prosecuted to the maximum extent
 *      possible under the law.
 */
package com.huawei.obs.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jets3t.service.Constants;
import org.jets3t.service.Jets3tProperties;
import org.jets3t.service.MultipartUploadChunk;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.StorageObjectsChunk;
import org.jets3t.service.VersionOrDeleteMarkersChunk;
import org.jets3t.service.acl.S3AccessControlList;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.InterfaceLogBean;
import org.jets3t.service.model.MultipartCompleted;
import org.jets3t.service.model.MultipartPart;
import org.jets3t.service.model.MultipleDeleteResult;
import org.jets3t.service.model.S3BucketLoggingStatus;
import org.jets3t.service.model.S3BucketVersioningStatus;
import org.jets3t.service.model.S3LifecycleConfiguration;
import org.jets3t.service.model.S3ListPartsRequest;
import org.jets3t.service.model.S3ListPartsResult;
import org.jets3t.service.model.S3MultipartUpload;
import org.jets3t.service.model.S3Owner;
import org.jets3t.service.model.S3Quota;
import org.jets3t.service.model.S3StorageInfo;
import org.jets3t.service.model.S3WebsiteConfiguration;
import org.jets3t.service.model.SS3Bucket;
import org.jets3t.service.model.SS3Object;
import org.jets3t.service.model.StorageObject;
import org.jets3t.service.model.container.ObjectKeyAndVersion;
import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.security.ProviderCredentials;

import com.huawei.obs.log.RunningLog;
import com.huawei.obs.services.exception.ObsException;
import com.huawei.obs.services.model.AbortMultipartUploadRequest;
import com.huawei.obs.services.model.AccessControlList;
import com.huawei.obs.services.model.BucketLoggingConfiguration;
import com.huawei.obs.services.model.BucketQuota;
import com.huawei.obs.services.model.BucketStorageInfo;
import com.huawei.obs.services.model.BucketVersioningConfiguration;
import com.huawei.obs.services.model.CompleteMultipartUploadRequest;
import com.huawei.obs.services.model.CompleteMultipartUploadResult;
import com.huawei.obs.services.model.CopyObjectRequest;
import com.huawei.obs.services.model.CopyObjectResult;
import com.huawei.obs.services.model.CopyPartRequest;
import com.huawei.obs.services.model.CopyPartResult;
import com.huawei.obs.services.model.DeleteObjectsRequest;
import com.huawei.obs.services.model.DeleteObjectsResult;
import com.huawei.obs.services.model.GetObjectRequest;
import com.huawei.obs.services.model.InitiateMultipartUploadRequest;
import com.huawei.obs.services.model.InitiateMultipartUploadResult;
import com.huawei.obs.services.model.KeyAndVersion;
import com.huawei.obs.services.model.LifecycleConfiguration;
import com.huawei.obs.services.model.ListMultipartUploadsRequest;
import com.huawei.obs.services.model.ListObjectsRequest;
import com.huawei.obs.services.model.ListPartsRequest;
import com.huawei.obs.services.model.ListPartsResult;
import com.huawei.obs.services.model.ListVersionsResult;
import com.huawei.obs.services.model.Multipart;
import com.huawei.obs.services.model.MultipartUploadListing;
import com.huawei.obs.services.model.ObjectListing;
import com.huawei.obs.services.model.ObjectMetadata;
import com.huawei.obs.services.model.PartEtag;
import com.huawei.obs.services.model.PutObjectRequest;
import com.huawei.obs.services.model.PutObjectResult;
import com.huawei.obs.services.model.S3Bucket;
import com.huawei.obs.services.model.S3Object;
import com.huawei.obs.services.model.UploadPartRequest;
import com.huawei.obs.services.model.UploadPartResult;
import com.huawei.obs.services.model.WebsiteConfiguration;

/**
 * 提供访问OBS系统服务的Java本地接口，以便在OBS系统获取或存储数据。
 * 
 * @version eSDK Storage 1.5.20
 */
public class ObsClient
{
    private RestS3Service s3Service = null;

    private static final Logger ilog = Logger.getLogger(ObsClient.class);

    private static final RunningLog runningLog = RunningLog.getRunningLog();

    /**
     * 指定配置参数的构造函数
     * 
     * @param accessId Access Key 
     * @param accessKey Secret Access Key
     * @param config 配置信息
     * @throws ObsException
     */
    public ObsClient(String accessId, String accessKey, ObsConfiguration config)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("ObsClient",
                config.getEndPoint(), "");

        ProviderCredentials credentials = new AWSCredentials(accessId,
                accessKey);
        Jets3tProperties jets3tProperties = new Jets3tProperties();
        configfieldToProperties(config, jets3tProperties);
        try
        {
            runningLog.debug("ObsClient", "accessId:" + accessId
                    + ", endPoint:" + config.getEndPoint()
                    + ", MaxConnections:" + config.getMaxConnections());
            s3Service = new RestS3Service(credentials, null, null,
                    jets3tProperties);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("ObsClient", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 创建桶 <br/>
     * 按照用户指定的桶名在默认区域创建一个新桶，默认区域为CHINA。
     * <p>
     * <b>桶命名规范：</b>
     * </p>
     * <ul>
     * <li>只能包含小写字母、数字、"-"、"."。
     * <li>只能以数字或字母开头。
     * <li>长度要求不少于3个字符，并且不能超过63个字符。
     * <li>不能是IP地址。
     * <li>不能以"-"结尾。
     * <li>不可以包括有两个相邻的"."。
     * <li>"."和"-"不能相邻，如"my-.bucket"和"my.-bucket "都是非法的。
     * </ul>
     * <p>
     * <b>说明：</b>
     * </p>
     * 1.一个用户可以拥有的桶的数量不能超过100个。
     * <p>
     * 2.新创建桶的桶名在OBS中必须是唯一的。如果是同一个用户采用相同的复制策略重复创建同名桶时返回200。
     * 除此以外的其他场景重复创建同名桶返回409，桶已存在。用户可以在请求消息头中加入x-amz-acl参数，
     * 设置要创建桶的权限控制策略。另外，用户还可以在请求消息头中加入x-hws-mdc-storage-policy参数，
     * 设置要创建桶的异步复制策略，该参数在MDC版本下才会生效。
     * <p>
     * 3.在单DC场景下，如果用户A创建一个桶，然后删除该桶，用户B立即创建同名桶，系统返回200。
     * 在多DC场景下，如果用户A创建一个桶，然后删除该桶，用户B立即创建同名桶，系统返回409。 如果用户B等待一个小时再创建同名桶，则系统返回200。
     * 
     * @param bucketName 【必选】桶的名称
     * @return 桶信息
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;    //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;    obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;    //调用ObsClient的createBucket接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;    <B>S3Bucket bucketResult = obsClient.createBucket(bucketName);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;    System.out.println("Create bucket success. BucketName: " + bucketResult.getBucketName() <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;        + "; CreationDate" + bucketResult.getCreationDate()<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;        + "; Location: " + bucketResult.getLocation());<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;    System.out.println("Create bucket failed. Error Message: " + e.getErrorMessage()<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;        + ". Response Code: " + e.getResponseCode()); <br/>
     *         }<br/>
     */
    public S3Bucket createBucket(String bucketName) throws ObsException
    {

        InterfaceLogBean reqBean = new InterfaceLogBean("createBucket(String bucketName)",
                s3Service.getEndpoint(), "");
        try
        {

            runningLog.debug("createBucket", "bucketName: " + bucketName);
            SS3Bucket s3Bucket = s3Service.createBucket(bucketName,null);
            S3Bucket obsBucket = Convert.changeFromS3Bucket(s3Bucket);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return obsBucket;
        }
        catch (ServiceException e)
        {
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("createBucket", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 创建桶 <br/>
     * 按照用户指定的桶名和指定的区域创建一个新桶。
     * <p>
     * <b>桶命名规范：</b>
     * </p>
     * <ul>
     * <li>只能包含小写字母、数字、"-"、"."。
     * <li>只能以数字或字母开头。
     * <li>长度要求不少于3个字符，并且不能超过63个字符。
     * <li>不能是IP地址。
     * <li>不能以"-"结尾。
     * <li>不可以包括有两个相邻的"."。
     * <li>"."和"-"不能相邻，如"my-.bucket"和"my.-bucket "都是非法的。
     * </ul>
     * <p>
     * <b>说明：</b>
     * </p>
     * 1.一个用户可以拥有的桶的数量不能超过100个。
     * <p>
     * 2.新创建桶的桶名在OBS中必须是唯一的。如果是同一个用户采用相同的复制策略重复创建同名桶时返回200。
     * 除此以外的其他场景重复创建同名桶返回409，桶已存在。用户可以在请求消息头中加入x-amz-acl参数，
     * 设置要创建桶的权限控制策略。另外，用户还可以在请求消息头中加入x-hws-mdc-storage-policy参数，
     * 设置要创建桶的异步复制策略，该参数在MDC版本下才会生效。
     * <p>
     * 3.在单DC场景下，如果用户A创建一个桶，然后删除该桶，用户B立即创建同名桶，系统返回200。
     * 在多DC场景下，如果用户A创建一个桶，然后删除该桶，用户B立即创建同名桶，系统返回409。 如果用户B等待一个小时再创建同名桶，则系统返回200。
     * 
     * @param bucketName 桶的名称
     * @param location 桶创建的区域
     * @return 桶信息
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001";<br/>
     *         String location = "suzhou"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;    //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;    obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;    //调用ObsClient的createBucket接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;    <B>S3Bucket bucketResult = obsClient.createBucket(bucketName, location);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;    System.out.println("Create bucket success. BucketName: " + bucketResult.getBucketName() <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;        + "; CreationDate" + bucketResult.getCreationDate()<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;        + "; Location: " + bucketResult.getLocation());<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;    System.out.println("Create bucket failed. Error Message: " + e.getErrorMessage()<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;        + ". Response Code: " + e.getResponseCode()); <br/>
     *         }<br/>
     * */
    public S3Bucket createBucket(String bucketName, String location)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("createBucket",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("createBucket", "bucketName:" + bucketName
                    + ",location: " + location);
            SS3Bucket s3Bucket = s3Service.createBucket(bucketName, location);
            S3Bucket obsBucket = Convert.changeFromS3Bucket(s3Bucket);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return obsBucket;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("createBucket", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }
    
    /**
     * 创建桶<br/>
     * 按照用户指定的桶名和指定的区域创建一个新桶。
     * <p>
     * <b>桶命名规范：</b>
     * </p>
     * <ul>
     * <li>只能包含小写字母、数字、"-"、"."。
     * <li>只能以数字或字母开头。
     * <li>长度要求不少于3个字符，并且不能超过63个字符。
     * <li>不能是IP地址。
     * <li>不能以"-"结尾。
     * <li>不可以包括有两个相邻的"."。
     * <li>"."和"-"不能相邻，如"my-.bucket"和"my.-bucket "都是非法的。
     * </ul>
     * <p>
     * <b>说明：</b>
     * </p>
     * 1.一个用户可以拥有的桶的数量不能超过100个。
     * <p>
     * 2.在单DC场景下，如果用户A创建一个桶，然后删除该桶，用户B立即创建同名桶，系统返回200。
     *   在多DC场景下，如果用户A创建一个桶，然后删除该桶，用户B立即创建同名桶，系统返回409。 如果用户B等待一个小时再创建同名桶，则系统返回200。
     * @param bucket 要创建的桶
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient;<br/>
     *         ObsConfiguration config;<br/>
     *         final String endPoint = "129.4.234.2"; //存储服务器地址<br/>
     *         final int httpPort = 5080; //HTTP请求对应的端口<br/>
     *         final String ak = "DF040F692AA69F0EEC55"; //接入证书<br/>
     *         final String sk = "ffkZzMmozB4EzQr0r3HxNItX1pgAAAFLKqafDuId";
     *         //安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<p>
     *   &nbsp;&nbsp;&nbsp;&nbsp;// 实例化ObsClient服务<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;// 创建桶实例<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;S3Bucket s3Bucket = new S3Bucket();<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;String bucketName = "bucket003";<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;String location = "region1";<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;Map&lt;String,Object&gt metadata = new HashMap&lt;String,Object&gt;();<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;metadata.put("x-amz-acl", "public-read");<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;s3Bucket.setBucketName(bucketName);<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;s3Bucket.setLocation(location);<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;s3Bucket.setMetadata(metadata);<br/>
         </p>
         <p>
         &nbsp;&nbsp;&nbsp;&nbsp;// 调用create接口创建桶，并获得创建的桶对象<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;S3Bucket rS3Bucket = obsClient.createBucket(s3Bucket);<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Bucket name: " + rS3Bucket.getBucketName()<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ", location: " + rS3Bucket.getLocation());<br/>
         </p>
        }<br/>
        catch (ObsException e)<br/>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Create bucket failed. Error message: " + e.getErrorMessage()<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
        }
     */
    public S3Bucket createBucket(S3Bucket bucket)
        throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("createBucket(S3Bucket bucket)",
            s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("createBucket", "bucketName:" + bucket.getBucketName()
                    + ", location: " + bucket.getLocation() + ", metadata:" + bucket.getMetadata());
            
            SS3Bucket s3Bucket = s3Service.createBucket(Convert.changeToSS3Bucket(bucket));
            S3Bucket obsBucket = Convert.changeFromS3Bucket(s3Bucket);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return obsBucket;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("createBucket", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 删除桶，必须保证桶内不存在对象
     * <p>
     * 用于删除用户指定的桶。只有桶的所有者可以执行删除桶的操作，且要删除的桶必须是空桶。
     * 
     * @param bucketName 桶的名称
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket002"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的deleteBucket接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>obsClient.deleteBucket(bucketName);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Delete bucket success.");<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Delete bucket failed. " + e.getErrorMessage() + " response code :"
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ e.getResponseCode());<br/>
     *         }<br/>
     * */
    public void deleteBucket(String bucketName) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("deleteBucket",
                s3Service.getEndpoint(), "");
        try
        {

            runningLog.debug("deleteBucket", "bucketName: " + bucketName);
            s3Service.deleteBucket(bucketName);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("deleteBucket", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 获取桶列表
     * <p>
     * 列出请求者拥有的所有桶的列表。
     * 
     * @return 桶的列表 包含:桶名，桶的创建时间，桶的所有者信息。
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的listBuckets接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>List&lt;S3Bucket&gt; bucketList = obsClient.listBuckets();</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  Iterator&lt;S3Bucket&gt; iterator = bucketList.iterator();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("List bucket success: ");<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  while (iterator.hasNext())<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  S3Bucket bucket = iterator.next();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" *bucketName: " + bucket.getBucketName());<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" createDate: " + bucket.getCreationDate());<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  Owner owner = bucket.getOwner();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  if (null != owner)<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" owner id: " + owner.getId());<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" owner displayname: " + owner.getDisplayName());<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  else<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" owner is null");<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" List bucket failed." + e.getErrorMessage() + ", response code : "
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; + e.getResponseCode());<br/>
     *         }<br/>
     * */
    public List<S3Bucket> listBuckets() throws ObsException
    {
        SS3Bucket[] bucketArray;

        InterfaceLogBean reqBean = new InterfaceLogBean("listBuckets",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("listBuckets", "excute listBuckets method.");
            bucketArray = s3Service.listAllBuckets();
            List<S3Bucket> bucketList = new ArrayList<S3Bucket>();
            for (SS3Bucket bucket : bucketArray)
            {
                bucketList.add(Convert.changeFromS3Bucket(bucket));
            }

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return bucketList;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("listBuckets", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 查询桶是否存在
     * <p>
     * 对桶拥有读权限的用户可以执行查询桶是否存在的操作。
     * 
     * @param bucketName 桶的名称
     * @return true 桶是存在的， false 桶不存在
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient;<br/>
     *         ObsConfiguration config;<br/>
     *         final String endPoint = "129.4.234.2"; //存储服务器地址<br/>
     *         final int httpPort = 5080; //HTTP请求对应的端口<br/>
     *         final String ak = "DF040F692AA69F0EEC55"; //接入证书<br/>
     *         final String sk = "ffkZzMmozB4EzQr0r3HxNItX1pgAAAFLKqafDuId";
     *         //安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;// 实例化ObsClient服务<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;// 调用isBucketExist接口判断桶是否存在<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;boolean isBucketExist = obsClient.headBucket("bucket001");<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;if(isBucketExist)<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;{<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("This bucket exists.");<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;else<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;{<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("This bucket doesn't exist.");<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
       }<br/>
       catch (ObsException e)<br/>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Error message: " + e.getErrorMessage()<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
        }
     * */
    public boolean headBucket(String bucketName) throws ObsException
    {
        asserParameterNotNull(bucketName, "The bucketName parameter must be specified.");
        boolean isExist = false;
        InterfaceLogBean reqBean = new InterfaceLogBean("doesBucketExist",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("doesBucketExist", "bucketName: " + bucketName);
            SS3Bucket s3Bucket = s3Service.getBucket(bucketName);
            if (s3Bucket != null)
            {
                isExist = true;
            }

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (ServiceException e)
        {

            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("doesBucketExist",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }

        return isExist;
    }

    /**
     * 获取桶ACL<br/>
     * 用户执行获取桶ACL(access control list)的操作，返回指定桶的权限控制列表信息。用户必须拥有
     * 对指定桶读ACP的权限或FULL_CONTROL权限，才能执行获取桶ACL的操作。
     * 
     * @param bucketName 桶的名称
     * @return 桶的访问控制策略
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的getBucketAcl接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>AccessControlList acl = obsClient.getBucketAcl(bucketName);</B>
     *         <p>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Get bucket acl success. ACL: ");<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  Owner owner = acl.getOwner();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  if (null != owner)<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" owner id:" + owner.getId());<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" owner displayname:" + owner.getDisplayName());<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  else<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" owner is null");<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  GrantAndPermission[] grantAndPermissions = acl.getGrantAndPermissions();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  if (null != grantAndPermissions) <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  for (int i = 0; i < grantAndPermissions.length; i++) <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" permissions :" + grantAndPermissions[i].getPermission());<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Get bucket acl failed. " + e.getErrorMessage() + " response code :"
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ e.getResponseCode());<br/>
     *         }<br/>
     */
    public AccessControlList getBucketAcl(String bucketName)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("getBucketAcl",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("getBucketAcl", "bucketName: " + bucketName);

            S3AccessControlList acl = s3Service.getBucketAcl(bucketName);
            AccessControlList obsAcl = Convert.changeFromS3Acl(acl);

            reqBean.setRespTime(new Date());
            reqBean.setSourceAddr(InterfaceLogBean.getLocalIP());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return obsAcl;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("getBucketAcl", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 更改桶ACL<br/>
     * OBS支持对桶操作进行权限控制。默认情况下，只有桶的创建者才有该桶的读写权限。
     * 用户也可以设置其他的访问策略，比如对一个桶可以设置公共访问策略，允许所有人 对其都有读权限。
     * <p>
     * OBS用户在创建桶时可以设置权限控制策略，也可以通过ACL操作接口对已存在的桶更改或者获取ACL(access control list)。
     * 
     * @param bucketName 桶的名称
     * @param acl ACL(访问控制策略) acl和cannedACL不能同时使用
     * @param cannedACL 通过canned ACL的方式来设置桶的ACL。有效值：<br>
     *  <ul>
     *      <li>private 桶或对象的所有者拥有完全控制的权限，其他任何人都没有访问权限</li>
     *      <li>public-read 桶或对象的所有者拥有完全控制的权限，其他所有用户包括匿名用户拥有读的权限。</li>
     *      <li>public-read-write 桶或对象的所有者拥有完全控制的权限，其他所有用户包括匿名用户拥有读和写的权限。</li>
     *      <li>authenticated-read 桶或对象的所有者拥有完全控制的权限，其他OBS授权用户拥有读权限。</li>
     *      <li>bucketowner-read 对象的所有者拥有完全控制的权限，桶的所有者拥有只读的权限。</li>
     *      <li>bucket-ownerfull-control 对象的所有者拥有完全控制的权限，桶的所有者拥有完全控制的权限。</li>
     *      <li>log-deliverywrite 日志投递用户组拥有对桶的写权限以及读ACP的权限。</li>
     *  </ul>
     *  </b>
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         </p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;//实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *         <p/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;// 封装修改访问权限的请求<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;AccessControlList acl = new AccessControlList();<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;Owner own = new Owner();// 设置桶的所有者的信息<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;own.setId("2BE58D565E4A443E2D596F63470AAF4C");<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;own.setDisplayName("user001");<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;acl.setOwner(own);<br/>
               <p/>
               &nbsp;&nbsp;&nbsp;&nbsp;// 设置被授权用户的信息以及被赋予的权限<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;CanonicalGrantee canonicalGrant =<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;new CanonicalGrantee("5D3735FF5BD3E127CA22266BF46A8E60");<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;canonicalGrant.setDisplayName("user002");<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;acl.grantPermission(canonicalGrant, Permission.PERMISSION_READ);<br/>
               <p/>
               &nbsp;&nbsp;&nbsp;&nbsp;// 设置被授权用户组的信息以及被赋予的权限<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;GroupGrantee groupGrant =<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;new GroupGrantee("http://acs.amazonaws.com/groups/global/AuthenticatedUsers");<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;acl.grantPermission(groupGrant, Permission.PERMISSION_READ);<br/>
               <p/>
               &nbsp;&nbsp;&nbsp;&nbsp;// 调用setBucketAcl设置桶的访问权限<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;String bucketName = "bucket002";<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;<B>obsClient.setBucketAcl(bucketName, null, acl);</B><br/>
               &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("set bucket acl success.");<br/>
        }<br/>
        catch (ObsException e)<br/>
        {<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Error message: " + e.getErrorMessage()<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
        }<br/>
     */
    public void setBucketAcl(String bucketName, String cannedACL, AccessControlList acl)
            throws ObsException
    {
        
        S3AccessControlList s3acl = null;
        if(null != acl)
        {
            s3acl = Convert.changeToS3Acl(acl);
        }
        InterfaceLogBean reqBean =
            new InterfaceLogBean("setBucketAcl", s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("setBucketAcl", "bucketName: "+bucketName);
            
            s3Service.putBucketAcl(bucketName, cannedACL, s3acl);

            reqBean.setRespTime(new Date());
            reqBean.setSourceAddr(InterfaceLogBean.getLocalIP());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("setBucketAcl", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }

    }

    /**
     * 获取桶区域位置<br/>
     * 对桶拥有读权限的用户可以执行获取桶区域位置信息的操作。
     * 
     * @param bucketName 桶的名称
     * @return 桶所在的区域
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的getBucketLocation接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>String location = obsClient.getBucketLocation(bucketName);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Get bucket location success. Bucket location: " + location);<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Get bucket location failed. " + e.getErrorMessage() + " response code :"
     *          + e.getResponseCode());<br/>
     *         }<br/>
     */
    public String getBucketLocation(String bucketName) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("getBucketLocation",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.info("getBucketLocation", "bucketName: " + bucketName);
            String bucketLocation = s3Service.getBucketLocation(bucketName);

            reqBean.setRespTime(new Date());
            reqBean.setSourceAddr(InterfaceLogBean.getLocalIP());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return bucketLocation;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("getBucketLocation",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 获取桶存量信息，单位为B<br/>
     * 桶的拥有者可以执行获取桶存量信息的操作，获取指定桶的空间大小以及对象个数。如果没有开启桶的多版本状态，那么返回的空间大小就是桶内现存所有对象大小之和;<br/>
     * 返回的对象个数就是桶内现存的对象个数。如果开启了桶的多版本状态，那么返回的空间大小会包含已经删除的所有对象所占用的容量，返回的对象个数也会包含所有被<br/>
     * 删除的历史对象。<br/>
     * @param bucketName 桶的名称
     * @return 桶存量信息描述
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/> 
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的getBucketStorageInfo接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>BucketStorageInfo storageInfo = obsClient.getBucketStorageInfo(bucketName);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Get bucket storageinfo success. objectNumber: " + storageInfo.getObjectNumber() 
     *         + "; Size: " + storageInfo.getSize());<br/>
     *         } <br/>
     *         catch (ObsException e) <br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Get bucket storageinfo failed. " + e.getErrorMessage() + " response code : " + e.getResponseCode());<br/>
     *         }<br/>
     */
    public BucketStorageInfo getBucketStorageInfo(String bucketName)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("getBucketStorageInfo",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("getBucketStorageInfo", "bucketName: "
                    + bucketName);
            S3StorageInfo storageInfo = s3Service
                    .getBucketStorageInfo(bucketName);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            BucketStorageInfo obsStorageInfo = Convert
                    .changeFromS3StroageInfo(storageInfo);
            return obsStorageInfo;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("getBucketStorageInfo",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 获取桶配额<br/>
     * 桶的拥有者可以执行获取桶配额信息的操作。<br>
     * 当桶的拥有者的状态是inactive状态时不可以查询桶配额信息。<br>
     * 桶空间配额值的单位为B(字节)，0代表不设上限。
     * 
     * @param bucketName 桶名
     * @return 桶配额
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的getBucketQuota接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>BucketQuota quota = obsClient.getBucketQuota(bucketName);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Get bucket quota success. Bucket quota: " + quota.getBucketQuota());<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Get bucket quota failed. " + e.getErrorMessage() + " response code :"
     *          + e.getResponseCode());<br/>
     *         }<br/>
     */
    public BucketQuota getBucketQuota(String bucketName) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("getBucketQuota",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("getBucketQuota", "bucketName: " + bucketName);
            S3Quota quota = s3Service.getBucketQuota(bucketName);
            BucketQuota bucketQuota = Convert.changeFromS3Quota(quota);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return bucketQuota;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog
                    .error("getBucketQuota", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 设置桶配额<br/>
     * 只有桶的拥有者才可以修改桶配额值。<br>
     * 当桶的拥有者的状态是inactive状态不可以更改桶配额信息。<br/>
     * 桶空间配额值必须为非负整数，单位为B(字节)，最大值为2<SUP>63</SUP>-1。<br/>
     * 配额值设为0表示桶的配额没有上限。
     * 
     * @param bucketName 桶名
     * @param bucketQuota 桶配额
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         final long quota = 512L;<br/>
     *         BucketQuota bucketQuota = new BucketQuota();<br/>
     *         bucketQuota.setBucketQuota(quota);<br/>
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的setBucketQuota接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>obsClient.setBucketQuota(bucketName, bucketQuota);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("set bucket quota success.");<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("set bucket quota failed. " + e.getErrorMessage() + " response code :"
     *         &nbsp;&nbsp;&nbsp;&nbsp; + e.getResponseCode());<br/>
     *         }<br/>
     */
    public void setBucketQuota(String bucketName, BucketQuota bucketQuota)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("setBucketQuota",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("setBucketQuota", "bucketName: " + bucketName);
            S3Quota quota = Convert.changeToS3Quota(bucketQuota);
            s3Service.putBucketQuota(bucketName, quota);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog
                    .error("setBucketQuota", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

//    /**
//     * 获取桶复制策略<br/>
//     * 桶的拥有者可以执行查询桶的复制策略操作，OBS系统将返回桶的复制策略名。<br/>
//     * 复制策略是指在MDC版本下，对象可以通过一个复制策略方案来决定本地对象被异步上传至哪几个远程数据中心上。<br/>
//     * <p>
//     * <b>注意：</b>
//     * </p>
//     * 当且仅当在MDC(Multiple Data Center)场景下，OBS支持用户查询桶复制策略，而在单DC或 region 场景下均不支持。
//     * 
//     * @param bucketName 桶名
//     * @return 桶的复制策略信息
//     * @throws ObsException ObsException
//     * @since eSDK Storage 1.5.10
//     */
//    public StoragePolicy getBucketStoragePolicy(String bucketName)
//            throws ObsException
//    {
//        InterfaceLogBean reqBean = new InterfaceLogBean(
//                "getBucketStoragePolicy", s3Service.getEndpoint(), "");
//        try
//        {
//            runningLog.debug("getBucketStoragePolicy", "bucketName: "
//                    + bucketName);
//            S3StoragePolicy storagePolicy = s3Service
//                    .getBucketStoragePolicy(bucketName);
//            StoragePolicy obsStoragePolicy = Convert
//                    .changeFromS3StoragePolicy(storagePolicy);
//
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode("0");
//            ilog.info(reqBean);
//            return obsStoragePolicy;
//        }
//        catch (ServiceException e)
//        {
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode(e.getErrorCode());
//            ilog.error(reqBean);
//            runningLog.error("getBucketStoragePolicy",
//                    "Exception:" + (null == e.getXmlMessage() ? e.getErrorMessage() : e.getXmlMessage()));
//            throw Convert.changeFromS3Exception(e);
//        }
//    }

//    /**
//     * 更改桶复制策略<br/>
//     * 当且仅当在MDC场景下，OBS系统支持用户对桶设定复制策略，而在单DC或 region 场景下均不支持。<br/>
//     * 复制策略是指在MDC版本下，对象可以通过一个复制策略方案来决定本地对象被异步上传至哪几个远程数据中心上。<br/>
//     * 对象的复制策略与对象所在的桶将保持一致，因此用户无需也无法设置对象的复制策略。<br/>
//     * 该功能的目的旨在提高多数据中心之间的异步复制过程的可靠性与性能。
//     * 
//     * @param bucketName 桶名
//     * @param storagePolicy 存储策略
//     * @throws ObsException ObsException
//     * @since eSDK Storage 1.5.10
//     */
//    public void setBucketStoragePolicy(String bucketName,
//            StoragePolicy storagePolicy) throws ObsException
//    {
//        InterfaceLogBean reqBean = new InterfaceLogBean(
//                "setBucketStoragePolicy", s3Service.getEndpoint(), "");
//        try
//        {
//            runningLog.debug("setBucketStoragePolicy",
//                    "bucketName: " + bucketName + ", storagePolicy: "
//                            + storagePolicy.getStoragePolicyName());
//            S3StoragePolicy s3storagePolicy = Convert
//                    .changeToS3StoragePolicy(storagePolicy);
//            s3Service.putBucketStoragePolicy(bucketName, s3storagePolicy);
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
//            runningLog.error("setBucketStoragePolicy",
//                    "Exception:" + (null == e.getXmlMessage() ? e.getErrorMessage() : e.getXmlMessage()));
//            throw Convert.changeFromS3Exception(e);
//        }
//    }

    /**
     * 获取桶的日志管理配置<br/>
     * 查询当前桶的日志管理配置情况。
     * <p>
     * 要使用该接口，使用者必须是桶的所有者或者是被桶策略授权s3:GetBucketLogging权限的用户。
     * 
     * @param bucketName 桶名
     * @return 桶的日志管理配置信息
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; //调用ObsClient的getBucketLoggingConfiguration接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; <B>BucketLoggingConfiguration loggingConfiguration = obsClient.getBucketLoggingConfiguration(bucketName);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; if (loggingConfiguration != null) <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; System.out.println("Get bucket logging configuration success. Logging configuration: " + loggingConfiguration.toString());<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; }<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; else<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; System.out.println("Get bucket logging configuration failed or logging configuration is null.");<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; }<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; System.out.println("Get bucket logging configuration failed. " + e.getErrorMessage() + "response code :"
     *              + e.getResponseCode());<br/>
     *         }<br/>
     */
    public BucketLoggingConfiguration getBucketLoggingConfiguration(
            String bucketName) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean(
                "getBucketLoggingConfiguration", s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("getBucketLoggingConfiguration", "bucketName: "
                    + bucketName);

            S3BucketLoggingStatus bucketLoggingStatus = s3Service
                    .getBucketLoggingStatus(bucketName);
            BucketLoggingConfiguration loggingConfiguration = Convert
                    .changeFromS3BucketLoggingStatus(bucketLoggingStatus);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return loggingConfiguration;
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("getBucketLoggingConfiguration",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 设置桶的日志管理配置<br/>
     * 提供OBS日志访问的设置功能。指定哪些用户可以查看和修改桶的日志文件。
     * <p>
     * 只有桶的所有者或被桶策略授权s3:PutBucketLogging权限的用户才具备对桶日志配置参数查看和修改的权限。
     * <p>
     * 桶的所有者拥有对所有生成日志的FULL_CONTROL权限。所有者可以通过日志配置参数表授予其他人访问桶日志的权限。
     * <p>
     * 创建桶时，默认是不生成桶的日志的，如果需要生成桶的日志，需要打开日志配置管理的开关，同时授予日志投递用户组对该桶的WRITE和READ_ACP权限
     * 。<br/>
     * 配置管理开关的打开方法是在上传的logging文件中在LoggingEnabled标签下配置相应的日志管理功能。
     * 关闭的方法是上传一个带有空的BucketLoggingStatus标签的logging文件。
     * 
     * @param bucketName 桶名
     * @param loggingConfiguration 日志管理配置
     * @param updateTargetACLifRequired 是否需要更新桶的ACL
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         //设置日志管理配置信息<br/>
     *         BucketLoggingConfiguration loggingConfig = new BucketLoggingConfiguration();<br/>
     *         loggingConfig.setTargetBucketName(bucketName);<br/>
     *         loggingConfig.setLogfilePrefix("access_log");<br/>
     *         CanonicalGrantee grantee = new CanonicalGrantee();<br/>
     *         grantee.setDisplayName("testuser");<br/>
     *         grantee.setIdentifier("59272A0571CB15F4ACC0CB35D7DCE7E2");   //存储系统的用户ID<br/>
     *         Permission permission = Permission.PERMISSION_FULL_CONTROL; <br/>
     *         GrantAndPermission[] grants = new GrantAndPermission[1];<br/>
     *         grants[0] = new GrantAndPermission(grantee, permission);<br/>
     *         loggingConfig.setTargetGrants(grants);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; //调用ObsClient的setBucketLoggingConfiguration接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; <B>obsClient.setBucketLoggingConfiguration(bucketName, loggingConfig, true);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; System.out.println("set bucket logging configuration success.");<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; System.out.println("set bucket logging configuration failed. " + e.getErrorMessage() + "response code :" 
     *              + e.getResponseCode());<br/>
     *         }<br/>
     */
    public void setBucketLoggingConfiguration(String bucketName,
            BucketLoggingConfiguration loggingConfiguration,
            boolean updateTargetACLifRequired) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean(
                "setBucketLoggingConfiguration", s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("setBucketLoggingConfiguration", "bucketName: "
                    + bucketName);

            S3BucketLoggingStatus status = Convert
                    .changeToS3BucketLoggingStatus(loggingConfiguration);
            s3Service.setBucketLoggingStatus(bucketName, status,
                    updateTargetACLifRequired);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("setBucketLoggingConfiguration",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 设置桶的多版本状态<br/>
     * 用来开启或暂停桶的多版本功能。
     * <p>
     * 用户可以使用多版本功能来保存、检索和还原对象的各个版本，这样用户能够从意外操作或应用程序故障中轻松恢复数据。<br/>
     * 多版本功能还可用于数据保留和存档。<br/>
     * <p>
     * <b>注意：</b>
     * </p>
     * 默认情况下，桶没有设置多版本功能。<br/>
     * 只有桶的所有者可以设置桶的多版本状态。
     * <p>
     * 设置桶的多版本状态为Enabled，开启桶的多版本功能：
     * <ul>
     * <li>上传对象时，系统为每一个对象创建一个唯一版本号，上传同名的对象将不再覆盖旧的对象，而是创建新的不同版本号的同名对象；
     * <li>可以指定版本号下载对象，不指定版本号默认下载最新对象；
     * <li>删除对象时可以指定版本号删除，不带版本号删除对象仅产生一个带唯一版本号的删除标记，并不删除对象；
     * <li>列出桶内对象列表时默认列出最新对象列表，可以指定列出桶内所有版本对象列表；
     * <li>除了删除标记外，每个版本的对象存储均需计费（不包括对象元数据）。
     * </ul>
     * <p>
     * 设置桶的多版本状态为Suspended，暂停桶的多版本功能：
     * <ul>
     * <li>旧的版本数据继续保留；
     * <li>上传对象时创建对象的版本号为null，上传同名的对象将覆盖原有同名的版本号为null的对象；
     * <li>可以指定版本号下载对象，不指定版本号默认下载最新对象；
     * <li>删除对象时可以指定版本号删除，不带版本号删除对象将产生一个版本号为null的删除标记，并删除版本号为null的对象；
     * <li>除了删除标记外，每个版本的对象存储均需计费（不包括对象元数据）。
     * 
     * @param bucketName 桶名
     * @param versioningConfiguration 多版本状态实例 see {@link BucketVersioningConfiguration}<br/>
     * <B>桶的多版本状态一旦开启，则不能关闭，只能暂停。桶的多版本状态处于暂停或开启时，不能设置桶生命周期属性。</B>
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);<br/>
     *         </p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;// 实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *         <p/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;// 创建多版本状态实例<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;BucketVersioningConfiguration bucketVersioningConfiguration =<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; new BucketVersioningConfiguration(BucketVersioningConfiguration.ENABLED);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;// 调用setBucketVersioning接口开启桶的多版本状态<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;<B>obsClient.setBucketVersioning("bucket001", bucketVersioningConfiguration);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("set bucket versioning configuration success.");<br/>
     *         }<br/>
               catch (ObsException e)<br/>
               {<br/>
                   &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("failed to set bucket versioning status.ErrorMessage: "<br/>
                   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; + e.getErrorMessage() + ". Response code :" <br/>
                   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; + e.getResponseCode());<br/>
               }<br/>
     */
    public void setBucketVersioning(String bucketName,BucketVersioningConfiguration versioningConfiguration)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("setBucketVersioning",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("setBucketVersioning", "bucketName: " + bucketName
                    + ", status: " + versioningConfiguration.getStatus());
            
            s3Service.setBucketVersioning(bucketName, versioningConfiguration.getStatus());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("setBucketVersioning",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 获取桶的多版本状态<br/>
     * 桶的所有者可以获取指定桶的多版本状态。<br/>
     * 如果从未设置桶的多版本状态，则此操作不会返回桶的多版本状态。
     * 
     * @param bucketName 桶名
     * @return 桶的多版本状态
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         </p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;// 实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *         <p>
     *         &nbsp;&nbsp;&nbsp;&nbsp;String bucketName = "doc-bucket";<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;// 调用getBucketVersioning接口查询桶的多版本状态是否开启<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;BucketVersioningConfiguration versioningConfiguration
            = obsClient.getBucketVersioning(bucketName);<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("The versioning state is: " + versioningConfiguration.getStatus());
               </p>
       }<br/>
       catch (ObsException e)<br/>
       {<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Error message: " + e.getErrorMessage()<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
       }
     */
    public BucketVersioningConfiguration getBucketVersioning(String bucketName)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("getBucketVersioning",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog
                    .debug("setBucketVersioning", "bucketName: " + bucketName);
            S3BucketVersioningStatus versionStatus = s3Service
                    .getBucketVersioningStatus(bucketName);
            BucketVersioningConfiguration versionConfiguration
            = new BucketVersioningConfiguration(versionStatus.getVersioningStatus());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return versionConfiguration;
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.info(reqBean);
            runningLog.error("getBucketVersioning",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 获取桶内所有对象的版本信息<br/>
     * 对桶拥有读权限的用户可以执行列举桶内对象的版本信息（含多版本）操作。
     * 
     * @param bucketName 桶名
     * @param prefix 对象的前缀名称
     * @param delimiter 名称分隔符
     * @param keyMarker 标识符，返回的对象列表将是按照字典顺序排序后在这个标识符以后的所有对象
     * @param versionIdMarker 与key-marker配合使用，返回的对象列表将是按照字典顺序排序后在该标识符以后的所有对象。
     * @return 桶内所有对象的版本信息
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "image-bucket"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         </p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;//实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;// 调用listVersions接口查询桶中所有对象的版本信息<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;ListVersionsResult result = obsClient.listVersions(bucketName, null, null, null, null, 10, null);<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;System.out.println(result);<br/>
        }<br/>
        catch (ObsException e)<br/>
        {<br/>
            &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Error message: " + e.getErrorMessage()<br/>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
        }<br/>
     */
    public ListVersionsResult listVersions(String bucketName, String prefix,
            String delimiter, String keyMarker, String versionIdMarker,
            long maxKeys,String nextVersionIdMarker) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("listVersions",
                s3Service.getEndpoint(), "");
        try
        {
            VersionOrDeleteMarkersChunk markersChunk
            = s3Service.listVersionedObjectsChunked(bucketName, prefix, delimiter, maxKeys, keyMarker, nextVersionIdMarker, true);
            ListVersionsResult result = Convert.changeFromS3ListVersionsResult(markersChunk);
            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            runningLog.debug("listVersions", result.toString());
            return result;
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.debug("listVersions", (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 获取桶的生命周期配置<br/>
     * 获取为该桶设置的生命周期配置信息（OBS系统支持通过指定规则来实现定时删除桶中对象）。
     * <p>
     * 要正确执行此操作，需要确保执行者有s3:GetLifecycleConfiguration执行权限。
     * 缺省情况下只有桶的所有者可以执行此操作，也可以通过设置桶策略或用户策略授权给其他用户。
     * 
     * @param bucketName 桶名
     * @return 桶的生命周期配置信息
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的getBucketLifecycleConfiguration接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>LifecycleConfiguration lifecycleConfig = obsClient.getBucketLifecycleConfiguration(bucketName);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  if (null != lifecycleConfig) <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Get bucket lifecycle configuration success.");<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  else<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Get bucket lifecycle configuration failed or lifecycleConfig is null");<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" Get bucket lifecycle configuration failed." + e.getErrorMessage() + ", response code:"
     *                                     + e.getResponseCode());<br/>
     *         }
     */
    public LifecycleConfiguration getBucketLifecycleConfiguration(
            String bucketName) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean(
                "getBucketLifecycleConfiguration", s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("getBucketLifecycleConfiguration", "bucketName: "
                    + bucketName);

            S3LifecycleConfiguration s3LifecycleConfig = s3Service
                    .getBucketLifecycleConfiguration(bucketName);
            LifecycleConfiguration lifecycleConfig = Convert
                    .changeFromS3LifecycleConfiguration(s3LifecycleConfig);

            runningLog.debug("getBucketLifecycleConfiguration",
                    "LifecycleConfiguration: " + lifecycleConfig);
            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return lifecycleConfig;
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("getBucketLifecycleConfiguration", "Exception:"
                    + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 设置桶的生命周期配置<br/>
     * 为桶创建或更新生命周期配置信息（OBS系统支持通过指定规则来实现定时删除桶中对象）。
     * <p>
     * 典型的应用场景如：
     * <ul>
     * <li>周期性上传的日志文件，可能只需要保留一个星期或一个月，到期后要删除它们。
     * <li>某些文档在一段时间内经常访问，但是超过一定时间后就可能不会再访问了。这种文档您可能会先选择归档，然后在一定时间后删除。
     * </ul>
     * <p>
     * 要正确执行此操作，需要确保执行者有s3:PutLifecycleConfiguration权限。缺省情况下只有桶的所有者可以执行此操作，
     * 也可以通过设置桶策略或用户策略授权给其他用户。
     * <p>
     * <B>说明</B>
     * </p>
     * 如果桶的多版本状态是开启或暂停的，不能执行此操作。
     * <p>
     * 生命周期配置实现了定时删除对象的功能，所以如果想要阻止用户删除对象，以下几项操作的权限都应该被禁止：
     * <ul>
     * <li>s3:DeleteObject
     * <li>s3:DeleteObjectVersion
     * <li>s3:PutLifecycleConfiguration
     * </ul>
     * 如果需要阻止用户管理桶的生命周期配置，应该禁止s3:PutLifecycleConfiguration权限。
     * 
     * @param bucketName 桶名
     * @throws ObsException  SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  LifecycleConfiguration lifecycleConfig = new LifecycleConfiguration();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  LifecycleConfiguration.Rule rule = lifecycleConfig.newRule("lifecycle001", "user001", true);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  LifecycleConfiguration.Expiration expiration = rule.newExpiration();<br/>
     *         <p>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //指定生命周期配置生效的时间,这里指定本配置截止到2018年4月9日（UTC时间）生效<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  Date date = new Date(118, 3, 9);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  expiration.setDate(date);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  rule.setExpiration(expiration);<br/>
     *         </p>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的setBucketLifecycleConfiguration接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>obsClient.setBucketLifecycleConfiguration(bucketName, lifecycleConfig);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Set bucket lifecycle configuration success.");<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" Set bucket lifecycle configuration failed." + e.getErrorMessage() + ", response code:"
     *                                     + e.getResponseCode());<br/>
     *         }
     */
    public void setBucketLifecycleConfiguration(String bucketName,
            LifecycleConfiguration lifecycleConfig) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean(
                "setBucketLifecycleConfiguration", s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("setBucketLifecycleConfiguration", "bucketName: "
                    + bucketName + ", LifecycleConfiguration: "
                    + lifecycleConfig);

            S3LifecycleConfiguration s3LifecycleConfig = Convert
                    .changeToS3LifecycleConfiguration(lifecycleConfig);
            s3Service.setBucketLifecycleConfiguration(bucketName,
                    s3LifecycleConfig);
            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("setBucketLifecycleConfiguration", "Exception:"
                    + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 删除桶的生命周期配置<br/>
     * 删除指定桶的生命周期配置信息。删除后桶中的对象不会过期，OBS不会自动删除桶中对象。
     * <p>
     * 要正确执行此操作，需要确保执行者有s3:PutLifecycleConfiguration权限。缺省情况下只有桶的所有者可以执行此操作，
     * 也可以通过设置桶策略或用户策略授权给其他用户。
     * 
     * @param bucketName 桶名
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的deleteBucketLifecycleConfiguration接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>obsClient.deleteBucketLifecycleConfiguration(bucketName);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Delete bucket lifecycle configuration success.");<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Delete bucket lifecycle configuration failed." + e.getErrorMessage() + ", response code:"
     *              + e.getResponseCode());<br/>
     *         }<br/>
     */
    public void deleteBucketLifecycleConfiguration(String bucketName)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean(
                "deleteBucketLifecycleConfiguration", s3Service.getEndpoint(),
                "");
        try
        {
            runningLog.debug("deleteBucketLifecycleConfiguration",
                    "bucketName: " + bucketName);
            s3Service.deleteBucketLifecycleConfiguration(bucketName);
            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("deleteBucketLifecycleConfiguration", "Exception:"
                    + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 获取桶策略<br/>
     * <p>
     * 要使用该接口，使用者必须是桶的所有者或者是被桶策略授权s3:GetBucketPolicy权限的用户。<br/>
     * 如果Bucket已有Policy，则新的Policy会覆盖老的Policy。 Bucket Owner也可以执行GetBucket Policy或<br/>
     * Delete Bucket Policy操作， 获取或删除已设置的Bucket Policy。<br/>
     * 设置Policy后，后续对此Bucket的访问请求都会受到Policy的限制，表现为拒绝或接受请求。<br/>
     * @param bucketName 桶名
     * @return 桶策略，一个JSON格式的桶策略字符串(如果桶无策略，返回null)
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的getBucketPolicy接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>String policy = obsClient.getBucketPolicy(bucketName);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Get bucket policy success. Policy: " + policy);<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Get bucket policy failed." + e.getErrorMessage() 
     *              + " response code :" + e.getResponseCode());<br/>
     *         }<br/>
     */
    public String getBucketPolicy(String bucketName) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("getBucketPolicy",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("getBucketPolicy", "bucketName: " + bucketName);
            String policy = s3Service.getBucketPolicy(bucketName);
            runningLog.debug("getBucketPolicy", "policy: " + policy);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return policy;
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("getBucketPolicy",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 设置桶策略<br/>
     * 创建或者修改一个桶的策略。
     * <p>
     * 如果桶已经存在一个策略，那么当前请求中的策略将完全覆盖桶中现存的策略。
     * <p>
     * 要使用该接口，使用者要求必须是桶的所有者或者是被桶策略授权s3:PutBucketPolicy权限的用户。
     * 
     * @param bucketName 桶名
     * @param policy 桶策略信息（符合JSON格式的字符串）
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         String policy = "{\"Id\": \"Policy1375342051334\", " <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; + "\"Statement\": [{" <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; + "\"Sid\": \"Stmt1375240018061\", " <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; + "\"Action\": [\"s3:GetBucketLogging\"]," <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; + "\"Effect\": \"Allow\"," <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; + "\"Resource\": \"arn:aws:s3:::testbucket001\"," <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; + "\"Principal\":{" <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; + "\"AWS\":*}" <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; + "}" <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; + "]" <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; + "}"; <br/>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; obsClient = new ObsClient(ak, sk, config);<br/> 
     *         &nbsp;&nbsp;&nbsp;&nbsp; //调用ObsClient的setBucketPolicy接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; <B>obsClient.setBucketPolicy(bucketName, policy);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; System.out.println("Set bucket policy success.");<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; System.out.println("Set bucket policy failed." + e.getErrorMessage()
     *              + " response code:" + e.getResponseCode());<br/>
     *         }<br/>
     */
    public void setBucketPolicy(String bucketName, String policy)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("setBucketPolicy",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("setBucketPolicy", "bucketName: " + bucketName
                    + "， policy: " + policy);
            s3Service.setBucketPolicy(bucketName, policy);
            runningLog.debug("setBucketPolicy", "bucketName: " + bucketName
                    + "， policy: " + policy);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);

            runningLog.error("setBucketPolicy",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 删除桶策略<br/>
     * 删除一个指定桶的策略。
     * <p>
     * 要使用该接口，使用者必须是桶的所有者或者是被桶策略授权s3:DeleteBucketPolicy权限的用户。
     * 
     * @param bucketName 桶名
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; //调用ObsClient的deleteBucketPolicy接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; <B>obsClient.deleteBucketPolicy(bucketName);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; System.out.println("Delete bucket policy success.");<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; System.out.println("Delete bucket policy failed. " + e.getErrorMessage()
     *          + ", response code: " + e.getResponseCode());<br/>
               }<br/>
     */
    public void deleteBucketPolicy(String bucketName) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("deleteBucketPolicy",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("deleteBucketPolicy", "bucketName: " + bucketName);
            s3Service.deleteBucketPolicy(bucketName);
            runningLog.debug("deleteBucketPolicy", "bucketName: " + bucketName);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("deleteBucketPolicy",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 删除桶数据<br/>
     * 即使桶内存在对象，仍然可以执行删除桶数据操作，删除桶和桶内对象。
     * <p>
     * 桶数据删除使用标记删除的方式后台删除。删除标志记录在桶的元数据中，加了删除桶标志后，此桶处于不可用状态。
     * 在桶内数据完全删除前，后续创建同名桶时返回409，对桶的其他请求均返回404。
     * <p>
     * 桶数据删除作为OBS私有接口（非S3接口）默认情况下禁用， 但可以在OBS管理系统的动态配置页面进行配置。
     * 如果系统开启了桶删，所有用户都能使用桶删功能。
     * 
     * @param bucketName 桶名
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; //调用ObsClient的forceDeleteBucket接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; <B>obsClient.forceDeleteBucket(bucketName);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; System.out.println("Force delete bucket success.");<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp; System.out.println("Force delete bucket failed. " + e.getErrorMessage() + " resposne code : " + e.getResponseCode());<br/>
     *         }<br/>
     */
    public void forceDeleteBucket(String bucketName) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("forceDeleteBucket",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("forceDeleteBucket", "bucketName: " + bucketName);
            s3Service.forceDeleteBucket(bucketName);
            runningLog.debug("forceDeleteBucket", "bucketName: " + bucketName);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("forceDeleteBucket",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 获取桶的Website配置<br/>
     * 获取该桶设置的Website配置信息。 要正确执行此操作，需要确保执行者有s3:GetBucketWebsite执行权限。
     * 缺省情况下只有桶的所有者可以执行此操作，也可以通过设置桶策略或用户策略授权给其他用户。
     * 
     * @param bucketName 桶名
     * @return Website配置信息
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         </p>
     *         try<br/>
     *         {<br/>
     *&nbsp;&nbsp;&nbsp;&nbsp;//实例化ObsClient服务<br/>
     *&nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;// 调用getBucketWebsiteConfiguration接口查询桶的website配置信息<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;WebsiteConfiguration conf =<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;obsClient.getBucketWebsiteConfiguration("bucket00101");<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;List&lt;RouteRule&gt; list = conf.getRouteRules();// 获得所有路由规则<br/>
      <p/>
      &nbsp;&nbsp;&nbsp;&nbsp;// 输出桶的WebsiteConfiguration信息<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Key: "+conf.getKey()+", Suffix: "+conf.getSuffix());<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;for (RouteRule routeRule : list)<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;{<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("KeyPrefixEquals: "+ routeRule.getCondition().getKeyPrefixEquals()<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ", HttpErrorCodeEquals: " + routeRule.getCondition().getHttpErrorCodeReturnedEquals());<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("ReplaceKeyPrefixWith: "+routeRule.getRedirect().getReplaceKeyPrefixWith()<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+", HostName: " + routeRule.getRedirect().getHostName());<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
        }<br/>
        catch (ObsException e)<br/>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Get bucket website cofiguration failed. Error message: " + e.getErrorMessage()<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
        }<br/>
     */
    public WebsiteConfiguration getBucketWebsiteConfiguration(String bucketName)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean(
                "getBucketWebsiteConfiguration", s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("getBucketWebsiteConfiguration", "bucketName: "
                    + bucketName);
            WebsiteConfiguration websiteConfig = new WebsiteConfiguration();
            
            S3WebsiteConfiguration s3WebsiteConfig = s3Service
                    .getWebsiteConfig(bucketName);
            websiteConfig = Convert.changeFromS3WebsiteConfiguration(s3WebsiteConfig);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            
            return websiteConfig;
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("getBucketWebsiteConfiguration",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 设置桶的Website配置<br/>
     * 为桶创建或更新Website配置信息。
     * <p>
     * OBS允许在桶内保存静态的网页资源，如.html网页文件、flash文件、音视频文件等，
     * 当客户端通过桶的Website接入点访问这些对象资源时，浏览器可以直接解析出这些支持的网页资源，呈现给最终用户。
     * <p>
     * 典型的应用场景有：
     * <ul>
     * <li>重定向所有的请求到另外一个站点。
     * <li>设定特定的重定向规则来重定向特定的请求。
     * </ul>
     * 要正确执行此操作，需要确保执行者有s3:PutBucketWebsite权限。缺省情况下只有桶的所有者可以执行此操作，
     * 也可以通过设置桶策略或用户策略授权给其他用户。
     * 
     * @param bucketName 桶的名称
     * @param websiteConfig 桶的Website配置
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         </p>
     *         try<br/>
     *         {<br/>
     *&nbsp;&nbsp;&nbsp;&nbsp;//实例化ObsClient服务<br/>
     *&nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *<p/>
      &nbsp;&nbsp;&nbsp;&nbsp;// 设置website配置信息<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;WebsiteConfiguration conf = new WebsiteConfiguration();<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;conf.setKey("index.html");<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;conf.setSuffix("Error.html");<br/>
      <p/>
      &nbsp;&nbsp;&nbsp;&nbsp;RouteRule routeRule = new RouteRule();// 设置重定向规则容器<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;RouteRuleCondition condition = new RouteRuleCondition();// 设置重定向条件<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;condition.setHttpErrorCodeReturnedEquals("404");<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;condition.setKeyPrefixEquals("pptx");<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;routeRule.setCondition(condition);<br/>
      <p/>
      &nbsp;&nbsp;&nbsp;&nbsp;Redirect redirect = new Redirect();// 设置重定向规则<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;redirect.setHostName("www.host-example.com");<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;redirect.setReplaceKeyWith("errorpage.html");<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;routeRule.setRedirect(redirect);<br/>
      <p/>
      &nbsp;&nbsp;&nbsp;&nbsp;List&lt;RouteRule&gt; routeRules = new ArrayList&lt;RouteRule&gt;();// 重定向规则集合<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;routeRules.add(routeRule);<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;conf.setRouteRules(routeRules);<br/>
      <p/>
      &nbsp;&nbsp;&nbsp;&nbsp;// 调用setBucketWebsiteConfiguration接口设置桶的website<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;<B>obsClient.setBucketWebsiteConfiguration("bucket003",conf);</B><br/>
      &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("set bucket website configuration success.");<br/>
      }<br/>
      catch (ObsException e)<br/>
      {<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("failed to set bucket website configuration. Error message: " + e.getErrorMessage()<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
      }<br/>
     */
    public void setBucketWebsiteConfiguration(String bucketName,
            WebsiteConfiguration websiteConfig) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean(
                "setBucketWebsiteConfiguration", s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("setBucketWebsiteConfiguration", "bucketName: "
                    + bucketName + ", WebsiteConfig: " + websiteConfig);

            S3WebsiteConfiguration s3WebsiteConfig = Convert
                    .changeToS3WebsiteConfiguration(websiteConfig);
            s3Service.setWebsiteConfig(bucketName, s3WebsiteConfig);
            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("setBucketWebsite",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 删除桶的Website配置<br/>
     * 删除指定桶的Website配置信息。
     * <p>
     * 要正确执行此操作，需要确保执行者有s3:DeleteBucketWebsite权限。缺省情况下只有桶的所有者可以执行此操作，
     * 也可以通过设置桶策略或用户策略授权给其他用户。
     * 
     * @param bucketName 桶的名称
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         </p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;//实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;obsClient.deleteBucketWebsiteConfiguration("bucket006");<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("delete bucket success.");<br/>
        }<br/>
        catch (ObsException e)<br/>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Delete bucket website configuration failed. Error message: " + e.getErrorMessage()<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
        }<br/>
     *  <p>
     */
    public void deleteBucketWebsiteConfiguration(String bucketName)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("deleteBuketWebsite",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("deleteBuketWebsite", "bucketName: " + bucketName);
            s3Service.deleteWebsiteConfig(bucketName);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("deleteBuketWebsite",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 上传对象<br/>
     * 在指定的桶内增加一个对象，执行该操作需要用户拥有桶的写权限。
     * <p>
     * <B>说明</B>
     * </p>
     * 用户上传的对象存储在桶中。用户必须对桶有WRITE权限，才可以在桶中上传对象。<br/>
     * 同一个桶中存储的对象名必须是唯一的。
     * <p>
     * 如果在指定的桶内已经有相同的对象键值的对象，用户上传的新对象会覆盖原来的对象；<br/>
     * 为了确保数据在传输过程中没有遭到破坏，用户可以在请求消息头中加入Content-MD5参数。
     * 在这种情况下，OBS收到上传的对象后，会对对象进行MD5校验，如果不一致则返回出错信息。<br/>
     * 用户还可以在上传对象时指定x-amz-acl参数，设置对象的权限控制策略。
     * <p>
     * 如果桶的多版本状态是开启的，系统会自动为对象生成一个唯一的版本号，并且会在响应报头x-amz-version-id返回该版本号。
     * 如果桶的多版本状态是暂停的，则对象的版本号为null。
     * 
     * @param bucketName 桶名
     * @param objectKey 对象名
     * @param input 对象数据流
     * @param metadata 对象元数据
     * @return 上传对象的返回结果
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         String bucketName = "testbucket002"; <br/>
     *         String objectKey = "testobject001";<br/>
     *         String greeting = "Hello World!";<br/>
     *         ObjectMetadata metadata = new ObjectMetadata();<br/>
     *         metadata.setContentLength((long)greeting.getBytes().length);<br/>
     *         metadata.setContentType("text/plain");<br/>
     *         Map&lt;String,Object&gt; meta = new HashMap&lt;String,Object&gt;();<br/>
     *         meta.put("x-amz-acl", "public-read-write");<br/>
     *         meta.put("x-amz-meta-test", "test metadata");<br/>
     *         meta.put("x-amz-website-redirect-location", "/anotherPage.html");<br/>
     *         metadata.setMetadata(meta);<br/>
     *         ByteArrayInputStream input = new ByteArrayInputStream(greeting.getBytes());
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的putObject接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>PutObjectResult result = obsClient.putObject(bucketName, objectKey, input, metadata);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Put object success. Etag: " + result.getEtag());<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Put object failed. " + e.getErrorMessage() + " response code :" + e.getResponseCode());<br/>
     *         }<br/>
     */
    public PutObjectResult putObject(String bucketName, String objectKey,
            InputStream input, ObjectMetadata metadata) throws ObsException
    {
        InterfaceLogBean reqBean =
            new InterfaceLogBean("putObject", s3Service.getEndpoint(), "");
        
        runningLog.debug("putObject","bucketName: "
            + bucketName + ", objectKey: " + objectKey + ", ObjectMetadata: " + metadata);
        SS3Object object = new SS3Object();
        object.setBucketName(bucketName);
        object.setKey(objectKey);
        object.setDataInputStream(input);
        // jetS3t会使用S3Object中contentType属性的覆盖头域中的Content-Type
        object.setContentType(metadata.getContentType());
        object.setContentLength(metadata.getContentLength());
        if(null != metadata.getMetadata())
        {
            Map<String,Object> map = metadata.getMetadata();
            Set<String> keys = map.keySet();
            for (String key : keys)
            {
                object.addMetadata(key, (String)map.get(key));
            }
        }
        try
        {
            SS3Object objRet = s3Service.putObject(bucketName, object);
            PutObjectResult putObjectResult = new PutObjectResult();
            putObjectResult.setEtag(objRet.getETag());
            runningLog.debug("putObject", "bucketName: " + bucketName
                    + ", putObjectResult: " + putObjectResult.getEtag());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return putObjectResult;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("putObject", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }
    
    /**
     * 上传对象<br/>
     * 在指定的桶内增加一个对象，执行该操作需要用户拥有桶的写权限。
     * <p>
     * <B>说明</B>
     * </p>
     * 用户上传的对象存储在桶中。用户必须对桶有WRITE权限，才可以在桶中上传对象。<br/>
     * 同一个桶中存储的对象名必须是唯一的。
     * <p>
     * 如果在指定的桶内已经有相同的对象键值的对象，用户上传的新对象会覆盖原来的对象；<br/>
     * 为了确保数据在传输过程中没有遭到破坏，用户可以在请求消息头中加入Content-MD5参数。
     * 在这种情况下，OBS收到上传的对象后，会对对象进行MD5校验，如果不一致则返回出错信息。<br/>
     * 用户还可以在上传对象时指定x-amz-acl参数，设置对象的权限控制策略。
     * <p>
     * 如果桶的多版本状态是开启的，系统会自动为对象生成一个唯一的版本号，并且会在响应报头x-amz-version-id返回该版本号。
     * 如果桶的多版本状态是暂停的，则对象的版本号为null。
     * 
     * @param request 上传对象请求
     * @return 上传对象的返回结果
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient;<br/>
     *         ObsConfiguration config;<br/>
     *         final String endPoint = "129.4.234.2"; //存储服务器地址<br/>
     *         final int httpPort = 5080; //HTTP请求对应的端口<br/>
     *         final String ak = "DF040F692AA69F0EEC55"; //接入证书<br/>
     *         final String sk = "ffkZzMmozB4EzQr0r3HxNItX1pgAAAFLKqafDuId";
     *         //安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<p>
     * <p>
     * &nbsp;&nbsp;&nbsp;&nbsp;// 实例化ObsClient服务<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;File file = new File("c:/test/example.xml"); // 要上传的文件<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;FileInputStream fis = new FileInputStream(file);<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;ObjectMetadata metadata = new ObjectMetadata();// 设置上传对象的元数据<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;metadata.setContentLength(file.length());// 设置头信息中的文件长度<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;metadata.setContentType("txt/xml");// 设置上传的文件类型<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;String bucketName = "bucket006";// 要上传到的桶<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;String objectKey = "example.xml";// 对象名称<br/>
       </p>
       <p>
       &nbsp;&nbsp;&nbsp;&nbsp;// 封装上传对象的请求<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;PutObjectRequest request = new PutObjectRequest();<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;request.setBucketName(bucketName);<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;request.setInput(fis);<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;request.setMetadata(metadata);<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;request.setObjectKey(objectKey);<br/>
       </p>
       <p>
       &nbsp;&nbsp;&nbsp;&nbsp;// 调用putObject接口创建对象<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;PutObjectResult result = obsClient.putObject(request);<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Object etag: " + result.getEtag());<br/>
       </p>
       }<br/>
        catch (ObsException e)<br/>
        {<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Create object failed. Error message: " + e.getErrorMessage()<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
        }<br/>
        catch (FileNotFoundException e)<br/>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("FileNotFoundException: " + e.getMessage());<br/>
        }<br/>
     */
    public PutObjectResult putObject(PutObjectRequest request)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("putObject",
                s3Service.getEndpoint(), "");

        runningLog.debug("putObject", "bucketName: " + request.getBucketName()
                + ", objectKey: " + request.getObjectKey() + ", metadata: "
                + request.getMetadata().getContentType());
        SS3Object object = new SS3Object();
        object.setBucketName(request.getBucketName());
        object.setKey(request.getObjectKey());
        object.setDataInputStream(request.getInput());
        object.setContentType(request.getMetadata().getContentType());
        object.setContentLength(request.getMetadata().getContentLength());
        object.addAllMetadata(request.getMetadata().getMetadata());

        try
        {
            SS3Object objRet = s3Service.putObject(request.getBucketName(),
                    object);
            PutObjectResult putObjectResult = new PutObjectResult();
            putObjectResult.setEtag(objRet.getETag());

            runningLog
                    .debug("putObject",
                            "SS3Object BucketName: " + objRet.getBucketName()
                                    + ", SS3Object name: " + objRet.getName()
                                    + ", SS3Object versionId: "
                                    + objRet.getVersionId());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return putObjectResult;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("putObject", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 上传对象<br/>
     * 在指定的桶内增加一个对象，执行该操作需要用户拥有桶的写权限。
     * <p>
     * <B>说明</B>
     * </p>
     * 用户上传的对象存储在桶中。用户必须对桶有WRITE权限，才可以在桶中上传对象。<br/>
     * 同一个桶中存储的对象名必须是唯一的。
     * <p>
     * 如果在指定的桶内已经有相同的对象键值的对象，用户上传的新对象会覆盖原来的对象；<br/>
     * 为了确保数据在传输过程中没有遭到破坏，用户可以在请求消息头中加入Content-MD5参数。
     * 在这种情况下，OBS收到上传的对象后，会对对象进行MD5校验，如果不一致则返回出错信息。<br/>
     * 用户还可以在上传对象时指定x-amz-acl参数，设置对象的权限控制策略。
     * <p>
     * 如果桶的多版本状态是开启的，系统会自动为对象生成一个唯一的版本号，并且会在响应报头x-amz-version-id返回该版本号。
     * 如果桶的多版本状态是暂停的，则对象的版本号为null。
     * 
     * @param bucketName 桶名
     * @param objectKey 对象名
     * @param file 对象文件
     * @return 上传对象的返回结果
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient;<br/>
     *         ObsConfiguration config;<br/>
     *         final String endPoint = "129.4.234.2"; //存储服务器地址<br/>
     *         final int httpPort = 5080; //HTTP请求对应的端口<br/>
     *         final String ak = "DF040F692AA69F0EEC55"; //接入证书<br/>
     *         final String sk = "ffkZzMmozB4EzQr0r3HxNItX1pgAAAFLKqafDuId";
     *         //安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<p>
     * &nbsp;&nbsp;&nbsp;&nbsp;// 实例化ObsClient服务<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     * <p>
     * &nbsp;&nbsp;&nbsp;&nbsp;File file = new File("c:/upload/example.xml");<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;String bucketName = "bucket006";// 要上传到的桶<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;String objectKey = "example2.xml";// 对象名称<br/>
       </p> 
       &nbsp;&nbsp;&nbsp;&nbsp;// 调用putObject接口，上传对象<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;PutObjectResult result = obsClient.putObject(bucketName, objectKey, file);<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Object etag: " + result.getEtag());<br/>
       }<br/>
       catch (ObsException e)<br/>
        {<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Create object failed. Error message: " + e.getErrorMessage()<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
        }<br/>
     */
    public PutObjectResult putObject(String bucketName, String objectKey,
            File file) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("putObject",
                s3Service.getEndpoint(), "");

        SS3Object object = new SS3Object();
        object.setBucketName(bucketName);
        object.setKey(objectKey);
        object.setDataInputFile(file);
        object.setContentLength(file.length());
        runningLog.debug("putObject", "bucketName: " + bucketName
                + ", objectKey: " + objectKey);

        try
        {
            SS3Object objRet = s3Service.putObject(bucketName, object);
            PutObjectResult putObjectResult = new PutObjectResult();
            putObjectResult.setEtag(objRet.getETag());
            runningLog
                    .debug("putObject",
                            "SS3Object BucketName: " + objRet.getBucketName()
                                    + ", SS3Object name: " + objRet.getName()
                                    + ", SS3Object versionId: "
                                    + objRet.getVersionId());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return putObjectResult;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("putObject", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 获取对象<br/>
     * 获取对象内容和对象的元数据信息。
     * <p>
     * <B>多版本</B>
     * </p>
     * 默认情况下，获取的是最新版本的对象。如果最新版本的对象是删除标记，则返回404。<br/>
     * 如果要获取指定版本的对象，请求可携带versionId消息参数。
     * 
     * @param getObjectRequest 获取对象信息的请求参数集合
     * @return 对象数据描述信息
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient;<br/>
     *         ObsConfiguration config;<br/>
     *         final String endPoint = "129.4.234.2"; //存储服务器地址<br/>
     *         final int httpPort = 5080; //HTTP请求对应的端口<br/>
     *         final String ak = "DF040F692AA69F0EEC55"; //接入证书<br/>
     *         final String sk = "ffkZzMmozB4EzQr0r3HxNItX1pgAAAFLKqafDuId";
     *         //安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         </p>
     *                 // 封装请求信息<br/>
        String bucketName = "img-bucket";<br/>
        String objectKey = "waterlily.jpg";<br/>
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, objectKey);<br/>

        InputStream is = null;<br/>
        FileOutputStream fos = null;<br/>
        BufferedOutputStream bos = null;<br/>
        try<br/>
        {<p>
     *  &nbsp;&nbsp;&nbsp;&nbsp;// 实例化ObsClient服务<br/>
     *  &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;S3Object s3object = obsClient.getObject(getObjectRequest);<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;// 获得对象内容，并保存到本地<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;is = s3object.getObjectContent();<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;File file = new File("c:/download/waterlily.jpg");<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;fos = new FileOutputStream(file);<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;int b = 0;<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;bos = new BufferedOutputStream(fos, 1024 * 1024 * 2);<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;while ((b = is.read()) != -1)<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;{<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;bos.write(b);<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;bos.flush();
        }<br/>
        catch (ObsException e)<br/>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Get object failed. Error message: " + e.getErrorMessage()
        + ". ResponseCode: " + e.getResponseCode());<br/>
        }<br/>
        catch (FileNotFoundException e)<br/>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Get object failed. Error message: " + e.getMessage());<br/>
        }<br/>
        catch (IOException e)<br/>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Get object failed. Error message: " + e.getMessage());<br/>
        }<br/>
        finally<br/>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;try<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;{<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (null != is)// 如果网络异常中断，可能会导致输入流未正常初始化<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;is.close();<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (null != fos)<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;fos.close();<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (null != bos)<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;bos.close();<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;catch (IOException e)<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;{<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Fail to close stream. Error message: " + e.getMessage());<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
        }<br/>
     * 
     */
    public S3Object getObject(GetObjectRequest getObjectRequest)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("getObject",
                s3Service.getEndpoint(), "");

        String bucketName = getObjectRequest.getBucketName();
        String objectKey = getObjectRequest.getObjectKey();
        Long rangeStart = getObjectRequest.getRangeStart();
        Long rangeEnd = getObjectRequest.getRangeEnd();
        try
        {
            runningLog.debug("getObject", "bucketName: " + bucketName
                    + ", objectKey: " + objectKey + ", rangeStart: "
                    + rangeStart + ", rangeEnd: " + rangeEnd);
            SS3Bucket bucket = new SS3Bucket(bucketName);
            SS3Object object = s3Service.getObject(bucket, objectKey, null,
                    null, null, null, rangeStart, rangeEnd);
            S3Object obsObject = Convert.changeFromS3Object(object);
            runningLog
                    .debug("putObject",
                            "SS3Object BucketName: " + object.getBucketName()
                                    + ", SS3Object name: " + object.getName()
                                    + ", SS3Object versionId: "
                                    + object.getVersionId());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return obsObject;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("getObject", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 指定桶名、对象名获取对象
     * 
     * @param bucketName 桶名
     * @param objectKey 对象名
     * @return 对象数据描述信息
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         String bucketName = "img-bucket";<br/>
     *         String objectKey = "waterlily.jpg";<br/>
     *         <p>
     *         InputStream is = null;<br/>
     *         FileOutputStream fos = null;<br/>
     *         BufferedOutputStream bos = null;<br/>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的getObject接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>S3Object s3object = obsClient.getObject(bucketName, objectKey, null);</B><br/>
     *         <p>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  // 获得对象内容，并保存到本地<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  is = s3object.getObjectContent();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  File file = new File("c:/download/light.jpg");<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  fos = new FileOutputStream(file);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  bos = new BufferedOutputStream(fos, 1024 * 1024 * 2);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  int b = 0;<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  while ((b = is.read()) != -1)<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; bos.write(b);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  bos.flush();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("get object success.");<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("object :" + s3object.getObjectKey());<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Get object failed. " + e.getErrorMessage() + " response code :" + e.getResponseCode());<br/>
     *         }<br/>
     *         catch (IOException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  e.printStackTrace();<br/>
     *         }<br/>
     *         finally<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;try<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;{<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (null != is)// 如果网络异常中断，可能会导致输入流未正常初始化<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;is.close();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (null != fos)<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;fos.close();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (null != bos)<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;bos.close();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;catch (IOException e)<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;{<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Fail to close stream. Error message: " + e.getMessage());<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
     *         }<br/>
     */
    public S3Object getObject(String bucketName, String objectKey,
            String versionId) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("getObject",
                s3Service.getEndpoint(), "");

        try
        {
            runningLog.debug("getObject", "bucketName: " + bucketName
                    + ", objectKey: " + objectKey);
            SS3Object object = s3Service.getObject(bucketName, objectKey,
                    versionId);
            S3Object obsObject = Convert.changeFromS3Object(object);
            runningLog
                    .debug("getObject",
                            "SS3Object BucketName: " + object.getBucketName()
                                    + ", SS3Object name: " + object.getName()
                                    + ", SS3Object versionId: "
                                    + object.getVersionId());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return obsObject;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("getObject", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 获取对象元数据<br/>
     * 获取对象的元数据信息，需要用户拥有对象的读权限。
     * <p>
     * <B>多版本</B>
     * </p>
     * 默认情况下，获取的是最新版本的对象元数据。如果最新版本的对象是删除标记，则返回404。<br/>
     * 如果要获取指定版本的对象元数据，请求可携带versionId消息参数。
     * @param bucketName 桶名
     * @param objectKey 对象名
     * @return 对象的属性信息
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         </p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;//实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *         <p>
               &nbsp;&nbsp;&nbsp;&nbsp;// 调用getObjectMetadata接口查询对象元数据<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;<B>ObjectMetadata metadata = obsClient.getObjectMetadata("bucket005", "test005.txt", null);</B><br/>
               &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("ContentLength: " + metadata.getContentLength()<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ", ContentType: " + metadata.getContentType()<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ", Metadata: " + metadata.getMetadata());<br/>
                }<br/>
                catch (ObsException e)<br/>
                {<br/>
                &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Get metadata failed. Error message: " + e.getErrorMessage()<br/>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
                }<br/>
     *         
     */
    public ObjectMetadata getObjectMetadata(String bucketName,
            String objectKey, String versionId) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("getObjectMetadata",
                s3Service.getEndpoint(), "");

        try
        {
            runningLog.debug("getObjectMetadata", "bucketName: " + bucketName
                    + ", objectKey: " + objectKey);
            StorageObject object = s3Service.getObjectDetails(bucketName, objectKey,versionId);
            ObjectMetadata objMeta = new ObjectMetadata();
            if (object != null)
            {
                objMeta.setEtag(object.getETag());
                objMeta.setLastModified(object.getLastModifiedDate());
                objMeta.setContentLength(object.getContentLength());
                objMeta.setContentType(object.getContentType());
                objMeta.setMetadata(object.getMetadataMap());
                objMeta.setContentEncoding(object.getContentEncoding());
            }
            runningLog.debug("getObjectMetadata",
                    "ContentEncoding: " + objMeta.getContentEncoding()
                            + ", ContentLength: " + objMeta.getContentLength()
                            + ", Etag: " + objMeta.getEtag());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return objMeta;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("getObjectMetadata",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 列出桶内的对象列表<br/>
     * 对桶拥有读权限的用户可以执行获取桶内对象列表的操作。
     * 
     * @param listObjectsRequest 请求信息
     * @return 对象列表描述
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient;<br/>
     *         ObsConfiguration config;<br/>
     *         final String endPoint = "129.4.234.2"; //存储服务器地址<br/>
     *         final int httpPort = 5080; //HTTP请求对应的端口<br/>
     *         final String ak = "DF040F692AA69F0EEC55"; //接入证书<br/>
     *         final String sk = "ffkZzMmozB4EzQr0r3HxNItX1pgAAAFLKqafDuId";
     *         //安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<p>
     *   &nbsp;&nbsp;&nbsp;&nbsp;// 实例化ObsClient服务<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *   <p>
     *   &nbsp;&nbsp;&nbsp;&nbsp;// 设置查询条件<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;ListObjectsRequest listObjectsRequest = new ListObjectsRequest();<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;listObjectsRequest.setBucketName("bucket005");<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;listObjectsRequest.setPrefix("test");<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;listObjectsRequest.setMaxKeys(3);<br/>
     *   </p>
     *   <p>
     *   &nbsp;&nbsp;&nbsp;&nbsp;// 调用listObjects接口查询bucket005桶下的所有对象<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;ObjectListing objListing = obsClient.listObjects(listObjectsRequest);<br/>
         </p>
         <p>
         &nbsp;&nbsp;&nbsp;&nbsp;// 输出桶内对象的信息<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;List&lt;S3Object&gt; objList = objListing.getObjectSummaries();<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;for (S3Object s3Object : objList) <br/>
         &nbsp;&nbsp;&nbsp;&nbsp;{<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("ObjectKey: " + s3Object.getObjectKey()<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ", content type: " + s3Object.getMetadata().getContentType()<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ", content length: " + s3Object.getMetadata().getContentLength());<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
         </p>
        }<br/>
        catch (ObsException e)<br/>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("List objects failed. Error message: " + e.getErrorMessage()<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
        }
     * 
     */
    public ObjectListing listObjects(ListObjectsRequest listObjectsRequest)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("listObjects",
                s3Service.getEndpoint(), "");

        String bucketName = listObjectsRequest.getBucketName();
        String prefix = listObjectsRequest.getPrefix();
        String delimiter = listObjectsRequest.getDelimiter();
        String marker = listObjectsRequest.getMarker();
        int maxKeys = listObjectsRequest.getMaxKeys();

        try
        {
            runningLog.debug("listObjects", "bucketName: " + bucketName
                    + ", prefix: " + prefix + ", prefix: " + prefix
                    + ", maxKeys: " + maxKeys + ", delimiter: " + delimiter
                    + ", marker: " + marker);
            StorageObjectsChunk obj = s3Service.listObjectsChunked(bucketName,
                    prefix, delimiter, maxKeys, marker, false);
            ObjectListing objList = new ObjectListing();
            objList.setBucketName(bucketName);
            objList.setPrefix(prefix);
            objList.setDelimiter(delimiter);
            objList.setMarker(marker);
            objList.setMaxKeys(maxKeys);
            List<String> comPrefixList = new ArrayList<String>(
                    Arrays.asList(obj.getCommonPrefixes()));
            List<S3Object> objectSummaries = new ArrayList<S3Object>();
            for (StorageObject object : obj.getObjects())
            {
                objectSummaries.add(Convert.changeFromS3Object(object));
            }
            objList.setCommonPrefixes(comPrefixList);
            objList.setObjectSummaries(objectSummaries);
            runningLog.debug(
                    "listObjects",
                    "S3Objects amount: " + objectSummaries.size()
                            + "ObjectListing BucketName: "
                            + objList.getBucketName() + ", Delimiter: "
                            + objList.getDelimiter() + ", Prefix: "
                            + objList.getPrefix() + ", Marker: "
                            + objList.getMarker() + ", MaxKeys:"
                            + objList.getMaxKeys());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return objList;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("listObjects", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 列出桶内对象列表，默认返回最大1000个对象的信息列表
     * 
     * @param bucketName 桶名
     * @return 对象列表描述
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         String bucketName = "testbucket001"; 
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的listObjects接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>ObjectListing result = obsClient.listObjects(bucketName);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  Iterator&lt;S3Object&gt; itr = result.getObjectSummaries().iterator();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("List objects success. Objects: ");<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  while (itr.hasNext())<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  S3Object s3Object = (S3Object)itr.next();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("object: " + s3Object.getObjectKey());<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("List objects failed. " + e.getErrorMessage() + " response code :" + e.getResponseCode());<br/>
     *         }<br/>
     */
    public ObjectListing listObjects(String bucketName) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("listObjects",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("listObjects", "bucketName: " + bucketName);
            StorageObjectsChunk obj = s3Service.listObjectsChunked(bucketName,
                    null, null, Constants.DEFAULT_OBJECT_LIST_CHUNK_SIZE, null,
                    false);
            ObjectListing objList = new ObjectListing();
            objList.setBucketName(bucketName);
            List<String> comPrefixList = new ArrayList<String>(
                    Arrays.asList(obj.getCommonPrefixes()));

            List<S3Object> objectSummaries = new ArrayList<S3Object>();
            for (StorageObject object : obj.getObjects())
            {
                objectSummaries.add(Convert.changeFromS3Object(object));
            }
            objList.setCommonPrefixes(comPrefixList);
            objList.setObjectSummaries(objectSummaries);
            runningLog.debug(
                    "listObjects",
                    "S3Objects amount: " + objectSummaries.size()
                            + "ObjectListing BucketName: "
                            + objList.getBucketName() + ", Delimiter: "
                            + objList.getDelimiter() + ", Prefix: "
                            + objList.getPrefix() + ", Marker: "
                            + objList.getMarker() + ", MaxKeys:"
                            + objList.getMaxKeys());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return objList;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("listObjects", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 删除对象<br/>
     * 拥有对象所在桶的写权限的用户可以执行删除对象的操作。
     * <p>
     * 如果要删除的对象不存在，则仍然返回成功信息。
     * <p>
     * <B>多版本</B>
     * </p>
     * 当桶的多版本状态是开启时，不指定版本删除对象将产生一个带唯一版本号的删除标记，并不删除对象；<br/>
     * 当桶的多版本状态是暂停时，不指定版本删除对象将产生一个版本号为null的删除标记，并删除版本号为null的对象（如果该对象存在）。<br/>
     * 如果要删除指定版本的对象，请求可携带versionId消息参数。
     * 
     * @param bucketName 桶名
     * @param objectKey 对象名
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         String bucketName = "testbucket002"; <br/>
     *         String objectKey = "testobject001"; 
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的deleteObject接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>obsClient.deleteObject(bucketName, objectKey, null);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Delete object success.");<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Delete object failed. " + e.getErrorMessage() + " response code :"
     *           + e.getResponseCode());<br/>
     *         }<br/>
     */
    public void deleteObject(String bucketName, String objectKey,
            String versionId) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("deleteObjects",
                s3Service.getEndpoint(), "");
        try
        {
            runningLog.debug("deleteObjects", "bucketName: " + bucketName
                    + ", objectKey: " + objectKey);
            s3Service.deleteObject(bucketName, objectKey, versionId);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("deleteObjects", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 批量删除对象<br/>
     * 将一个桶内的部分对象一次性删除，删除后不可恢复。
     * <p>
     * 批量删除对象返回结果里包含每个对象的删除结果。<br/>
     * 删除成功的对象不能进行List和Get操作。<br/>
     * OBS的批量删除对象使用同步删除对象的方式，每个对象的删除结果都将返回给请求用户。
     * <p>
     * 批量删除对象支持两种响应方式：verbose和quiet。<br/>
     * Verbose是指在返回响应时，不管对象是否删除成功都将删除结果包含在XML响应里；<br/>
     * quiet是指在返回响应时，只返回删除失败的对象结果，没有返回的认为删除成功。<br/>
     * OBS默认使用verbose模式，如果用户在请求消息体中指定quiet模式的话， 则使用quiet模式。
     * <p>
     * 批量删除的请求消息头中必须包含Content-MD5以及Content-Length，以保证请求的消息体在服务端出现网络传输错误时，可以检测出来。
     * 
     * @param deleteObjectsRequest 批量删除对象的请求
     * @return 返回批量删除对象的结果
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         String bucketName = "testbucket002"; <br/>
     *         String objectKey1 = "testobject001"; <br/>
     *         String objectKey2 = "testobject002"; 
     *         <p>
     *         //构造deleteObjects接口的入参对象DeleteObjectsRequest<br/>
     *         DeleteObjectsRequest request = new DeleteObjectsRequest();<br/>
     *         request.setBucketName(bucketName);<br/>
     *         KeyAndVersion[] keyAndVersions = new KeyAndVersion[2];<br/>
     *         keyAndVersions[0] = new KeyAndVersion(objectKey1);<br/>
     *         keyAndVersions[1]= new KeyAndVersion(objectKey2);<br/>
     *         request.setKeyAndVersions(keyAndVersions);
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的deleteObjects接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>obsClient.deleteObjects(request);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Delete objects success.");<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Delete objects failed. " + e.getErrorMessage() + " response code :" +
     *           e.getResponseCode());<br/>
     *         }<br/>
     */
    public DeleteObjectsResult deleteObjects(
            DeleteObjectsRequest deleteObjectsRequest) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("deleteObjects",
                s3Service.getEndpoint(), "");

        KeyAndVersion[] keyAndVersions = deleteObjectsRequest
                .getKeyAndVersions();
        try
        {
            runningLog.debug("deleteObjects",
                    "keys: " + Arrays.toString(keyAndVersions));
            int i = 0;
            ObjectKeyAndVersion[] objectKeyAndVersion = new ObjectKeyAndVersion[keyAndVersions.length];
            for (KeyAndVersion keyAndVersion : keyAndVersions)
            {
                objectKeyAndVersion[i++] = new ObjectKeyAndVersion(
                        keyAndVersion.getKey(), keyAndVersion.getVersion());
            }
            MultipleDeleteResult s3result = s3Service.deleteMultipleObjects(
                    deleteObjectsRequest.getBucketName(), objectKeyAndVersion,
                    deleteObjectsRequest.isQuiet());
            runningLog.debug("deleteObjects",
                    "keys: " + s3result.getDeletedObjectResults());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return Convert.changeFromS3DeleteResult(s3result);
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("deleteObjects", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 获取对象访问控制列表<br/>
     * 获取指定对象的权限控制列表（ACL）信息。
     * <p>
     * 用户必须拥有对指定对象读ACP(access control policy)的权限，才能执行获取对象ACL的操作。
     * <p>
     * <B>多版本</B>
     * </p>
     * 默认情况下，获取最新版本的对象ACL。如果最新版本的对象是删除标记，则返回404。<br/>
     * 如果要获取指定版本的对象ACL，请求可携带versionId消息参数。
     * 
     * @param bucketName 桶名
     * @param objectKey 对象名
     * @return 访问控制策略
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         String bucketName = "testbucket002"; <br/>
     *         String objectKey = "testobject001"; 
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的getObjectAcl接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>AccessControlList acl = obsClient.getObjectAcl(bucketName, objectKey, null);</B>
     *         <p>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //在控制台输出获取的对象ACL信息<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Get object acl success. Object: " + objectKey);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  Owner owner = acl.getOwner();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  if (null != owner)<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" owner id:" + owner.getId());<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" owner displayname:" + owner.getDisplayName());<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  else<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" owner is null");<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  GrantAndPermission[] grantAndPermissions = acl.getGrantAndPermissions();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  if (null != grantAndPermissions) <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  for (int i = 0; i < grantAndPermissions.length; i++) <br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  System.out.println(" permissions :" + grantAndPermissions[i].getPermission());<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  }<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Get object acl failed. " + e.getErrorMessage() + " response code :" 
     *           + e.getResponseCode());<br/>
     *         }<br/>
     */
    public AccessControlList getObjectAcl(String bucketName, String objectKey, String versionId)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("getObjectAcl",
                s3Service.getEndpoint(), "");
        S3AccessControlList acl;
        try
        {
            runningLog.debug("getObjectAcl","bucketName: " + bucketName + ", objectKey: " + objectKey);
            acl = s3Service.getObjectAcl(bucketName, objectKey, versionId);
            AccessControlList obsAcl = Convert.changeFromS3Acl(acl);
            runningLog.debug("getObjectAcl","displayName: "
            + obsAcl.getOwner().getDisplayName() + ", id: " + obsAcl.getOwner().getId());
            
            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return obsAcl;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("getObjectAcl", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 设置对象访问控制列表<br/>
     * OBS支持对对象的操作进行权限控制。该接口可以更改对象ACL，以改变对象的访问权限。
     * <p>
     * 默认情况下，只有对象的创建者才有该对象的读写权限。<br/>
     * 用户也可以设置其他的访问策略，比如对一个对象可以设置公共访问策略，允许所有人对其都有读权限。<br/>
     * OBS用户在上传对象时可以设置权限控制策略，也可以通过ACL操作接口对已存在的对象设置或者获取ACL(access control list)。
     * <p>
     * <B>多版本</B>
     * </p>
     * 默认情况下，更改的是最新版本的对象ACL。要设置指定版本的对象ACL，请求可以带参数versionId。
     * 
     * @param bucketName 桶名
     * @param objectKey 对象名
     * @param acl 访问控制列表
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.1";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "D4590CA996A86A393D9B";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "pi68uEEOYGydBNE/b5kNr2A9rNgAAAFMlqhqOaGA";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         String bucketName = "testbucket001";<br/>
     *         String objectKey = "testfile.txt";
     *         <p>
     *         AccessControlList acl = new AccessControlList();
     *         <p>
     *         Owner owner = new Owner();<br/>
     *         owner.setId("9E255823DE68934DA24D5F101FA75A14");  
     *         <p>
     *         CanonicalGrantee cGrantee = new CanonicalGrantee();<br/>
     *         cGrantee.setIdentifier("A02393F7694D18CAEF6B77EB96FF587A");
     *         <p>
     *         GroupGrantee gGrantee = new GroupGrantee();<br/>
     *         gGrantee.setIdentifier("http://acs.amazonaws.com/groups/global/AuthenticatedUsers");            
     *         <p>   
     *         acl.grantPermission(cGrantee, Permission.PERMISSION_READ);<br/>
     *         acl.grantPermission(gGrantee, Permission.PERMISSION_READ);<br/>
     *         acl.setOwner(owner);  
     *         <p>        
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的setObjectAcl接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>obsClient.setObjectAcl(bucketName, objectKey, acl, null);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Set object acl success.");<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Set object acl failed. " + e.getErrorMessage() + " response code :" 
     *           + e.getResponseCode());<br/>
     *         }<br/>
     */
    public void setObjectAcl(String bucketName, String objectKey,
            AccessControlList acl, String versionId) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("setObjectAcl",
                s3Service.getEndpoint(), "");
        S3AccessControlList s3acl = Convert.changeToS3Acl(acl);
        try
        {
            runningLog.debug("setObjectAcl", "bucketName: " + bucketName
                    + ", objectKey: " + objectKey);
            s3Service.putObjectAcl(bucketName, objectKey, s3acl, versionId);

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("setObjectAcl", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 复制对象<br/>
     * 为OBS上已经存在的对象创建一个副本。
     * <p>
     * 当进行复制对象操作时，目标对象默认复制源对象的元数据；用户也可以将目标对象的元数据替换为本次请求中所带的元数据。<br/>
     * 新建的目标对象不会复制源对象的ACL信息，默认的新建对象的ACL是private，用户可以使用设置ACL的操作接口来重新设定新对象的ACL。
     * <p>
     * <B>多版本</B>
     * </p>
     * 默认情况下，x-amz-copy-source标识复制源对象的最新版本。<br/>
     * 如果源对象的最新版本是删除标记，则认为该对象已删除。<br/>
     * 要复制指定版本的对象，可以在x-amz-copy-source请求消息头中携带versionId参数。<br/>
     * 如果目标对象的桶的多版本状态是开启的，系统为目标对象生成唯一的版本号（此版本号与源对象的版本号不同），并且会在响应报头x-amz-version-
     * id返回该版本号。<br/>
     * 如果目标对象的桶的多版本状态是暂停的，则目标对象的版本号为null。
     * 
     * @param copyObjectRequest 复制对象请求
     * @return 复制对象操作的返回结果
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient;<br/>
     *         ObsConfiguration config;<br/>
     *         final String endPoint = "129.4.234.2"; //存储服务器地址<br/>
     *         final int httpPort = 5080; //HTTP请求对应的端口<br/>
     *         final String ak = "DF040F692AA69F0EEC55"; //接入证书<br/>
     *         final String sk = "ffkZzMmozB4EzQr0r3HxNItX1pgAAAFLKqafDuId";
     *         //安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<p>
     *   &nbsp;&nbsp;&nbsp;&nbsp;// 实例化ObsClient服务<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *   <p>
     *   &nbsp;&nbsp;&nbsp;&nbsp;String sourceBucketName = "bucket003"; // 源对象所属的桶<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;String sourceObjectKey = "test.txt"; // 源对象<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;String destBucketName = "bucket005"; // 要拷贝到的目标桶<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;String destObjectKey = "test005.txt"; // 目标对象<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;Map&lt;String,Object&gt; metadata = new HashMap&lt;String,Object&gt;();<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;metadata.put("x-amz-acl", "public-read");<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;ObjectMetadata newObjectMetadata = new ObjectMetadata();<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;newObjectMetadata.setMetadata(metadata);<br/>
         </p>
         
         <p>
         &nbsp;&nbsp;&nbsp;&nbsp;CopyObjectRequest copyObjectRequest = <br/>
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;new CopyObjectRequest(sourceBucketName, sourceObjectKey,<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;destBucketName, destObjectKey); <br/>
         &nbsp;&nbsp;&nbsp;&nbsp;copyObjectRequest.setNewObjectMetadata(newObjectMetadata); <br/>
         &nbsp;&nbsp;&nbsp;&nbsp;CopyObjectResult copyObjectResult = obsClient.copyObject(copyObjectRequest); <br/>
         </p>
         <p>
         &nbsp;&nbsp;&nbsp;&nbsp;// 新对象的Etag和修改日期 <br/>
         &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Etag: " + copyObjectResult.getEtag() <br/>
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ", LastModified: " + copyObjectResult.getLastModified()); <br/>
         </p>
        }<br/>
        catch (ObsException e)<br/>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Copy object failed. Error message: " + e.getErrorMessage()<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
        }
     * 
     */
    public CopyObjectResult copyObject(CopyObjectRequest copyObjectRequest)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("copyObject",
                s3Service.getEndpoint(), "");

        String sourceBucketName = copyObjectRequest.getSourceBucketName();
        String sourceKey = copyObjectRequest.getSourceObjectKey();
        String destinationBucketName = copyObjectRequest
                .getDestinationBucketName();
        String destinationKey = copyObjectRequest.getDestinationObjectKey();

        runningLog.debug("copyObject", "Source bucket name: "
                + sourceBucketName + ", sourceKey: " + sourceKey
                + ", destinationBucketName: " + destinationBucketName
                + ", destinationKey: " + destinationKey);

        StorageObject destinationObject = new StorageObject(destinationKey);
        try
        {
            Map<String, Object> retMap = s3Service.copyObject(sourceBucketName,
                    sourceKey, destinationBucketName, destinationObject, false);
            CopyObjectResult copyRet = new CopyObjectResult();
            Convert.fillCopyResult(copyRet, retMap);
            runningLog.debug(
                    "copyObject",
                    "CopyObjectResult Etag: " + copyRet.getEtag()
                            + ", CopyObjectResult LastModified: "
                            + copyRet.getLastModified());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return copyRet;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("copyObject", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 复制对象<br/>
     * 为OBS上已经存在的对象创建一个副本。
     * <p>
     * 当进行复制对象操作时，目标对象默认复制源对象的元数据；用户也可以将目标对象的元数据替换为本次请求中所带的元数据。<br/>
     * 新建的目标对象不会复制源对象的ACL信息，默认的新建对象的ACL是private，用户可以使用设置ACL的操作接口来重新设定新对象的ACL。
     * <p>
     * <B>多版本</B>
     * </p>
     * 默认情况下，x-amz-copy-source标识复制源对象的最新版本。<br/>
     * 如果源对象的最新版本是删除标记，则认为该对象已删除。<br/>
     * 要复制指定版本的对象，可以在x-amz-copy-source请求消息头中携带versionId参数。<br/>
     * 如果目标对象的桶的多版本状态是开启的，系统为目标对象生成唯一的版本号（此版本号与源对象的版本号不同），并且会在响应报头x-amz-version-
     * id返回该版本号。<br/>
     * 如果目标对象的桶的多版本状态是暂停的，则目标对象的版本号为null。
     * 
     * @param sourceBucketName 源桶名
     * @param sourceObjectKey 源对象名
     * @param destBucketName 目标桶名
     * @param destObjectKey 目标对象名
     * @return 复制对象操作的返回结果
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         String bucketName = "testbucket002"; <br/>
     *         String objectKey = "testobject001"; <br/>
     *         String objectNewKey = "testobject001New"; 
     *         <p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  //调用ObsClient的copyObject接口<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  <B>CopyObjectResult result = obsClient.copyObject(bucketName, objectKey, bucketName, objectNewKey);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Copy object success.");<br/>
     *         }<br/>
     *         catch (ObsException e)<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;  System.out.println("Copy object failed. " + e.getErrorMessage() + " response code :" + e.getResponseCode());<br/>
     *         }<br/>
     */
    public CopyObjectResult copyObject(String sourceBucketName,
            String sourceObjectKey, String destBucketName, String destObjectKey)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("copyObject",
                s3Service.getEndpoint(), "");

        StorageObject destinationObject = new StorageObject(destObjectKey);
        try
        {
            runningLog.debug("copyObject", "Source bucket name: "
                    + sourceBucketName + ", sourceKey: " + sourceObjectKey
                    + ", destinationBucketName: " + destBucketName
                    + ", destinationKey: " + destObjectKey);

            Map<String, Object> retMap = s3Service.copyObject(sourceBucketName,
                    sourceObjectKey, destBucketName, destinationObject, false);
            CopyObjectResult copyRet = new CopyObjectResult();
            Convert.fillCopyResult(copyRet, retMap);
            runningLog.debug(
                    "copyObject",
                    "CopyObjectResult Etag: " + copyRet.getEtag()
                            + ", CopyObjectResult LastModified: "
                            + copyRet.getLastModified());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return copyRet;
        }
        catch (ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("copyObject", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

//    /**
//     * 创建获取桶内对象列表临时授权
//     * 
//     * @param bucketName 桶名
//     * @param expiryTime 临时授权的有效时间
//     * @return 临时授权的URL字符串
//     * @throws ObsException ObsException
//     */
//    public String createSignedGetObjectListUrl(String bucketName,
//            Date expiryTime) throws ObsException
//    {
//        InterfaceLogBean reqBean = new InterfaceLogBean(
//                "createSignedGetObjectListUrl", s3Service.getEndpoint(), "");
//
//        try
//        {
//            runningLog.debug("createSignedGetObjectListUrl", "bucketName: "
//                    + bucketName + ", expiryTime: " + expiryTime);
//            String a = s3Service.createSignedGetUrl(bucketName, null,
//                    expiryTime);
//            runningLog
//                    .debug("createSignedGetObjectListUrl", "return url: " + a);
//
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode("0");
//            ilog.info(reqBean);
//            return a;
//        }
//        catch (S3ServiceException e)
//        {
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode(e.getErrorCode());
//            ilog.error(reqBean);
//            runningLog.error("createSignedGetObjectListUrl",
//                    "Exception:" + (null == e.getXmlMessage() ? e.getErrorMessage() : e.getXmlMessage()));
//            throw Convert.changeFromS3Exception(e);
//        }
//    }

//    /**
//     * 创建获取对象内容的临时授权
//     * 
//     * @param bucketName 桶名
//     * @param objectKey 对象名
//     * @param expiryTime 临时授权的有效时间
//     * @return 临时授权的URL字符串
//     * @throws ObsException ObsException
//     */
//    public String createSignedGetObjectUrl(String bucketName, String objectKey,
//            Date expiryTime) throws ObsException
//    {
//        InterfaceLogBean reqBean = new InterfaceLogBean(
//                "createSignedGetObjectListUrl", s3Service.getEndpoint(), "");
//
//        try
//        {
//            runningLog.debug("createSignedGetObjectListUrl", "bucketName: "
//                    + bucketName + ", objectKey" + objectKey + ", expiryTime: "
//                    + expiryTime);
//            String a = s3Service.createSignedGetUrl(bucketName, objectKey,
//                    expiryTime);
//            runningLog
//                    .debug("createSignedGetObjectListUrl", "return url: " + a);
//
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode("0");
//            ilog.info(reqBean);
//            return a;
//        }
//        catch (S3ServiceException e)
//        {
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode(e.getErrorCode());
//            ilog.error(reqBean);
//            runningLog.error("createSignedGetObjectUrl",
//                    "Exception:" + (null == e.getXmlMessage() ? e.getErrorMessage() : e.getXmlMessage()));
//            throw Convert.changeFromS3Exception(e);
//        }
//    }

//    /**
//     * 创建获取对象元数据的临时授权
//     * 
//     * @param bucketName 桶名
//     * @param objectKey 对象名
//     * @param expiryTime 临时授权的有效时间
//     * @return 临时授权的URL字符串
//     * @throws ObsException ObsException
//     */
//    public String createSignedHeadObjectUrl(String bucketName,
//            String objectKey, Date expiryTime) throws ObsException
//    {
//        InterfaceLogBean reqBean = new InterfaceLogBean(
//                "createSignedGetObjectListUrl", s3Service.getEndpoint(), "");
//
//        try
//        {
//            runningLog.debug("createSignedHeadObjectUrl", "bucketName: "
//                    + bucketName + ", objectKey" + objectKey + ", expiryTime: "
//                    + expiryTime);
//            String a = s3Service.createSignedHeadUrl(bucketName, objectKey,
//                    expiryTime);
//            runningLog.debug("createSignedHeadObjectUrl", "return url: " + a);
//
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode("0");
//            ilog.info(reqBean);
//            return a;
//        }
//        catch (S3ServiceException e)
//        {
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode(e.getErrorCode());
//            ilog.error(reqBean);
//            runningLog.error("createSignedHeadObjectUrl",
//                    "Exception:" + (null == e.getXmlMessage() ? e.getErrorMessage() : e.getXmlMessage()));
//            throw Convert.changeFromS3Exception(e);
//        }
//    }

//    /**
//     * 创建获取桶ACL的临时授权
//     * 
//     * @param bucketName 桶名
//     * @param expiryTime 临时授权的有效时间
//     * @return 临时授权的URL字符串
//     * @throws ObsException ObsException
//     */
//    public String createSignedGetBucketAclUrl(String bucketName, Date expiryTime)
//            throws ObsException
//    {
//        InterfaceLogBean reqBean = new InterfaceLogBean(
//                "createSignedGetBucketAclUrl", s3Service.getEndpoint(), "");
//        try
//        {
//            runningLog.debug("createSignedGetBucketAclUrl", "bucketName: "
//                    + bucketName + ", expiryTime: " + expiryTime);
//            String a = s3Service.createSignedGetAclUrl(bucketName, null,
//                    expiryTime);
//            runningLog.debug("createSignedGetBucketAclUrl", "return url: " + a);
//
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode("0");
//            ilog.info(reqBean);
//            return a;
//        }
//        catch (S3ServiceException e)
//        {
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode(e.getErrorCode());
//            ilog.error(reqBean);
//            runningLog.error("createSignedGetBucketAclUrl",
//                    "Exception:" + (null == e.getXmlMessage() ? e.getErrorMessage() : e.getXmlMessage()));
//            throw Convert.changeFromS3Exception(e);
//        }
//    }

//    /**
//     * 创建获取对象ACL的临时授权
//     * 
//     * @param bucketName 桶名
//     * @param objectKey 对象名
//     * @param expiryTime 临时授权的有效时间
//     * @return 临时授权的URL字符串
//     * @throws ObsException ObsException
//     */
//    public String createSignedGetObjectAclUrl(String bucketName,
//            String objectKey, Date expiryTime) throws ObsException
//    {
//        InterfaceLogBean reqBean = new InterfaceLogBean(
//                "createSignedGetObjectAclUrl", s3Service.getEndpoint(), "");
//        try
//        {
//            runningLog.debug("createSignedGetObjectAclUrl", "bucketName: "
//                    + bucketName + ", expiryTime: " + expiryTime);
//            String a = s3Service.createSignedGetAclUrl(bucketName, objectKey,
//                    expiryTime);
//            runningLog.debug("createSignedGetObjectAclUrl", "return url: " + a);
//
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode("0");
//            ilog.info(reqBean);
//            return a;
//        }
//        catch (S3ServiceException e)
//        {
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode(e.getErrorCode());
//            ilog.error(reqBean);
//            runningLog.error("createSignedGetObjectAclUrl",
//                    "Exception:" + (null == e.getXmlMessage() ? e.getErrorMessage() : e.getXmlMessage()));
//            throw Convert.changeFromS3Exception(e);
//        }
//    }

//    /**
//     * 创建修改桶的ACL临时授权,返回的URL字符串可以供其他用户修改桶ACL使用
//     * 
//     * @param bucketName 桶名
//     * @param expiryTime 临时授权的有效时间
//     * @return 临时授权的URL字符串
//     * @throws ObsException ObsException
//     */
//    public String createSignedPutBucketAclUrl(String bucketName, Date expiryTime)
//            throws ObsException
//    {
//        InterfaceLogBean reqBean = new InterfaceLogBean(
//                "createSignedPutBucketAclUrl", s3Service.getEndpoint(), "");
//        try
//        {
//            runningLog.debug("createSignedPutBucketAclUrl", "bucketName: "
//                    + bucketName + ", expiryTime: " + expiryTime);
//            String a = s3Service.createSignedPutAclUrl(bucketName, null,
//                    expiryTime);
//            runningLog.debug("createSignedPutBucketAclUrl", "return url: " + a);
//
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode("0");
//            ilog.info(reqBean);
//            return a;
//        }
//        catch (S3ServiceException e)
//        {
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode(e.getErrorCode());
//            ilog.error(reqBean);
//            runningLog.error("createSignedPutBucketAclUrl",
//                    "Exception:" + (null == e.getXmlMessage() ? e.getErrorMessage() : e.getXmlMessage()));
//            throw Convert.changeFromS3Exception(e);
//        }
//    }

//    /**
//     * 创建修改对象的ACL临时授权，返回的URL字符串可以供其他用户修改对象ACL使用
//     * 
//     * @param bucketName 桶名
//     * @param objectKey 对象名
//     * @param expiryTime 临时授权的有效时间
//     * @return 临时授权的URL字符串
//     * @throws ObsException ObsException
//     */
//    public String createSignedPutObjectAclUrl(String bucketName,
//            String objectKey, Date expiryTime) throws ObsException
//    {
//        InterfaceLogBean reqBean = new InterfaceLogBean(
//                "createSignedPutBucketAclUrl", s3Service.getEndpoint(), "");
//        try
//        {
//            runningLog.debug("createSignedPutObjectAclUrl", "bucketName: "
//                    + bucketName + ", expiryTime: " + expiryTime);
//            String a = s3Service.createSignedPutAclUrl(bucketName, objectKey,
//                    expiryTime);
//            runningLog.debug("createSignedPutObjectAclUrl", "return url: " + a);
//
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode("0");
//            ilog.info(reqBean);
//            return a;
//        }
//        catch (S3ServiceException e)
//        {
//            reqBean.setRespTime(new Date());
//            reqBean.setResultCode(e.getErrorCode());
//            ilog.error(reqBean);
//            runningLog.error("createSignedPutObjectAclUrl",
//                    "Exception:" + (null == e.getXmlMessage() ? e.getErrorMessage() : e.getXmlMessage()));
//            throw Convert.changeFromS3Exception(e);
//        }
//    }

    /**
     * 初始化多段上传任务<br/>
     * 使用多段上传特性时，用户必须首先调用初始化多段上传任务接口创建任务。<br/>
     * 系统会给用户返回一个全局唯一的多段上传任务号，作为任务标识。后续用户可以根据这个标识发起相关的请求，如：上传段、合并段、列举段等。<br/>
     * 创建多段上传任务不影响已有的同名对象；同一个对象可以同时存在多个多段上传任务；<br/>
     * 每个多段上传任务在初始化时可以附加消息头信息，包括acl、用户自定义元数据和通用的HTTP消息头contentType<br/>
     * 、contentEncoding等， 这些附加的消息头信息将先记录在多段上传任务元数据中。<br/>
     * 
     * @param request 请求描述
     * @return 多段上传任务的描述信息
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         </p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;//实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;String bucketName = "bucket001";<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;String objectKey = "object00101";<br/>
               <p/> 
               &nbsp;&nbsp;&nbsp;&nbsp;// 封装初始化上传任务的请求<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;InitiateMultipartUploadRequest request =<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;new InitiateMultipartUploadRequest();<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;request.setBucketName(bucketName);<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;request.setObjectKey(objectKey);<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;request.setWebSiteRedirectLocation("/object00101/website");<br/>
               <p/> 
               &nbsp;&nbsp;&nbsp;&nbsp;// 调用initiateMultipartUpload接口初始化上传任务，获得任务Id<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;<B>InitiateMultipartUploadResult imu =<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;obsClient.initiateMultipartUpload(request);</B><br/>
               &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("bucketName:" + imu.getBucketName()<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ "\tObjectKey:" + imu.getObjectKey()<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ "\tUploadId:" + imu.getUploadId());<br/>
            }<br/>
            catch (ObsException e)<br/>
            {<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Initiate multipart upload failed. Error message: " + e.getErrorMessage()<br/>
               &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
            }<br/>
     */
    public InitiateMultipartUploadResult initiateMultipartUpload(
            InitiateMultipartUploadRequest request) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean(
                "initiateMultipartUpload", s3Service.getEndpoint(), "");

        String bucketName = request.getBucketName();
        String objectKey = request.getObjectKey();
        Map<String, Object> metadata = new HashMap<String, Object>();
        if(null != request.getWebSiteRedirectLocation())
        {
            metadata.put("x-amz-website-redirect-location",
                request.getWebSiteRedirectLocation());
        }
        S3MultipartUpload testMultipartUpload;
        try
        {
            runningLog.debug("InitiateMultipartUploadResult", "bucketName: "
                    + bucketName + ", objectKey: " + objectKey);
            testMultipartUpload = s3Service.multipartStartUpload(bucketName,
                    objectKey, metadata);
            InitiateMultipartUploadResult ret = Convert
                    .changeFromS3MulipartRet(testMultipartUpload);
            runningLog.debug(
                    "InitiateMultipartUploadResult",
                    "bucketName: " + ret.getBucketName() + ", objectKey: "
                            + ret.getObjectKey() + ", UploadId:"
                            + ret.getUploadId());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return ret;
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("InitiateMultipartUploadResult",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 取消上传多段上传任务<br/>
     * 如果用户希望取消一个任务，可以调用取消多段上传任务接口取消任务。
     * <p>
     * 合并段或取消任务接口被调用后，用户不能再对任务进行上传段和列举段的操作。
     * 
     * @param request 请求描述
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient;<br/>
     *         ObsConfiguration config;<br/>
     *         final String endPoint = "129.4.234.2"; //存储服务器地址<br/>
     *         final int httpPort = 5080; //HTTP请求对应的端口<br/>
     *         final String ak = "DF040F692AA69F0EEC55"; //接入证书<br/>
     *         final String sk = "ffkZzMmozB4EzQr0r3HxNItX1pgAAAFLKqafDuId";
     *         //安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<p>
     *   &nbsp;&nbsp;&nbsp;&nbsp;// 实例化ObsClient服务<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;// 封装取消上传任务的请求<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;AbortMultipartUploadRequest request = new AbortMultipartUploadRequest();<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;request.setBucketName("jyzbucket003");<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;request.setObjectKey("obj00301");<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;request.setUploadId("39F29AA08D20817C43B15792996124F1");<br/>
            
         &nbsp;&nbsp;&nbsp;&nbsp;// 调用abortMultipartUpload接口，取消上传任务<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;obsClient.abortMultipartUpload(request);<br/>
         }<br/>
         catch (ObsException e)<br/>
         {<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Abort multipart upload failed. Error message: " + e.getErrorMessage()<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
         }<br/>
     */
    public void abortMultipartUpload(AbortMultipartUploadRequest request)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("abortMultipartUpload",
                s3Service.getEndpoint(), "");

        runningLog.debug("abortMultipartUpload",
                "objectKey: " + request.getObjectKey() + ", UploadId: "
                        + request.getUploadId());
        String uploadId = request.getUploadId();
        String bucketName = request.getBucketName();
        String objectKey = request.getObjectKey();
        S3MultipartUpload upload = new S3MultipartUpload(uploadId, bucketName,
                objectKey);
        try
        {
            s3Service.multipartAbortUpload(upload);
            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("abortMultipartUpload",
                    "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 上传段<br/>
     * 多段上传任务创建后，用户可以通过指定多段上传任务号，利用该接口为特定的任务上传段。
     * <p>
     * 同一个对象的同一个多段上传任务在上传段时，上传的顺序对后续的合并操作没有影响，也即支持多个段并发上传。
     * <p>
     * 段大小范围是[5MB，5GB]，但在进行合并段操作时，最后一个段的大小范围为[0,5GB]。<br/>
     * 上传的段的编号也有范围限制，其范围是[1,10000]。
     * 
     * @param request 请求描述，必填参数bucketName,objectKey,
     * @return 合并多段的返回描述
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @throws IOException 
     * @throws NoSuchAlgorithmException 
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         </p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;//实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *         <p/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;// 封装上传段的请求信息<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;File file = new File("C:/soft/example.rar");// 要上传的文件<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;UploadPartRequest request = new UploadPartRequest("bucket001", "object00101",<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;file);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;request.setPartNumber(7);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;request.setUploadId("1808A8027800AB8580C5FD69CBDE5C71");<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;request.setPartSize(file.length());<br/>
     *         <p/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;// 调用uploadPart接口上传段<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;<B>UploadPartResult upr = obsClient.uploadPart(request);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Etag: " + upr.getEtag() + " PartNumber: "+upr.getPartNumber());<br/>
        }<br/>
        catch (ObsException e)<br/>
        {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Error message: " + e.getErrorMessage()<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
        }<br/>
        catch (NoSuchAlgorithmException e)<br/>
        {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("NoSuchAlgorithmException: " + e.getMessage());<br/>
        }<br/>
        catch (IOException e)<br/>
        {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("IOException: " + e.getMessage());<br/>
        }<br/>
     */
    public UploadPartResult uploadPart(UploadPartRequest request)
            throws ObsException, NoSuchAlgorithmException, IOException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("uploadPart",
                s3Service.getEndpoint(), "");

        runningLog.debug("uploadPart", "bucketName: " + request.getBucketName()
                + "objectKey: " + request.getObjectKey() + ", partNumber: "
                + request.getPartNumber() + ", UploadId: " + request.getUploadId());

        try
        {
            S3MultipartUpload upload = new S3MultipartUpload(request.getUploadId(), request.getBucketName(),
                request.getObjectKey());// 上传段的请求，其实只需要一个upload
            SS3Object object = new SS3Object(request.getFile());// 上传的对象
            object.setBucketName(request.getBucketName());
            object.setKey(request.getObjectKey());
            if(null != request.getPartSize())
            {
                object.setContentLength(request.getPartSize());
            }
            
            MultipartPart partRet = s3Service.multipartUploadPart(upload,
                request.getPartNumber(), object);// 执行上传
            UploadPartResult ret = Convert.changeFromS3UploadPart(partRet);// 返回上传结果,段号和Etag
            
            runningLog.debug("uploadPart", "bucketName: " + ret.getEtag()
                    + ", partNumber: " + ret.getPartNumber());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return ret;
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("uploadPart", "Exception:" + (null == e.getXmlMessage() ? e.getMessage() : e.getXmlMessage()));
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 拷贝段<br/>
     * 以一个对象为源拷贝为多段上传任务中的一个段。
     * <p>
     * 多段上传任务创建后，用户除了可以通过指定多段上传任务号，为特定的任务上传段。 还可以通过调用拷贝段接口，将已上传对象的一部分或全部拷贝为段。
     * 
     * @param request 请求描述
     * @return 拷贝多段的返回描述
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         </p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;// 实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *  <p>
        &nbsp;&nbsp;&nbsp;&nbsp;// 设置请求信息<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;CopyPartRequest request = new CopyPartRequest();<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;request.setDestinationBucketName("bucket001");// 目标桶名<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;request.setDestinationObjectKey("object00101");// 目标对象名<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;request.setUploadId("2614F01D9D384485AD9BD3A8EA7280F4");// 上传任务id<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;request.setPartNumber(1);// 要上传的段的段号<br/>
        </p>
        <p>
        &nbsp;&nbsp;&nbsp;&nbsp;request.setSourceBucketName("bucket002");// 源桶名<br/>
        &nbsp;&nbsp;&nbsp;&nbsp; request.setSourceObjectKey("object00101");// 源对象名<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;request.setByteRangeStart(0l);// 拷贝的起始位置<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;request.setByteRangeEnd(50l);// 拷贝的终止位置<br/>
        </p>
        &nbsp;&nbsp;&nbsp;&nbsp;// 调用copyPart接口拷贝段<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;CopyPartResult cpr = obsClient.copyPart(request);<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Etag:"+cpr.getEtag()<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+", Partnumber:"+cpr.getPartNumber());<br/>
        }<br/>
        catch (ObsException e)<br/>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Copy part failed. Error message: " + e.getErrorMessage()<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
        }<br/>
     */
    public CopyPartResult copyPart(CopyPartRequest request) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("copyPart",
                s3Service.getEndpoint(), "");

        String uploadId = request.getUploadId();
        String sourceBucketName = request.getSourceBucketName();
        String sourceObjectKey = request.getSourceObjectKey();
        String destBucketName = request.getDestinationBucketName();
        String destObjectKey = request.getDestinationObjectKey();
        Integer partNumber = request.getPartNumber();
        runningLog.debug("copyPart", "sourceBucketName: " + sourceBucketName
                + "sourceObjectKey: " + sourceObjectKey + "destBucketName: "
                + destBucketName + "destObjectKey: " + destObjectKey
                + ", partNumber: " + partNumber + ", UploadId: " + uploadId);

        try
        {
            MultipartPart partRet = s3Service.multipartUploadPartCopy(uploadId,
                    partNumber, sourceBucketName, destBucketName,
                    destObjectKey, sourceObjectKey, request.getByteRangeStart(),
                    request.getByteRangeEnd());
            CopyPartResult ret = Convert.changeFromS3MultiCopyPart(partRet);
            runningLog.debug("copyPart", "bucketName: " + ret.getEtag()
                    + ", partNumber: " + ret.getPartNumber());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return ret;
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("copyPart", "Exception:" + e.getMessage());
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 合并段<br/>
     * 合并一个多段上传任务的段。
     * <p>
     * 如果用户上传完所有的段，就可以调用合并段接口，系统将在服务端将用户指定的段合并成一个完整的对象。<br/>
     * 在执行“合并段”操作以前，用户不能下载已经上传的数据。<br/>
     * 在合并段时需要将多段上传任务初始化时记录的附加消息头信息拷贝到对象元数据中，其处理过程和普通上传对象带这些消息头的处理过程相同。<br/>
     * 在并发合并段的情况下，仍然遵循Last Write Win策略，但“Last Write”的时间定义为段任务的初始化时间。
     * <p>
     * 已经上传的段，只要没有取消对应的多段上传任务，都要占用用户的容量配额；<br/>
     * 对应的多段上传任务“合并段”操作完成后，只有指定的多段数据占用容量配额，
     * 用户上传的其他此多段任务对应的段数据如果没有包含在“合并段”操作制定的段列表中，<br>
     * “合并段”完成后删除多余的段数据，且同时释放容量配额。
     * <p>
     * 合并完成的多段上传数据可以通过已有的下载对象接口，下载整个多段上传对象或者指定Range下载整个多段上传对象的某部分数据。
     * <p>
     * 合并完成的多段上传数据可以通过已有的删除对象接口，删除整个多段上传对象的所有分段数据，删除后不可恢复。
     * <p>
     * 合并完成的多段上传数据不记录整个对象的MD5作为Etag，在下载多段数据或List桶内对象看到的多段数据其Etag的生成方式为： <br/>
     * MD5（M1M2……MN）-N，其中，Mn表示第n段的MD5值， N表示总共的段数。
     * <p>
     * <B>多版本</B>
     * </p>
     * 如果桶的多版本状态是开启的，则合并段后得到的对象生成一个唯一的版本号，并且会在响应报头x-amz-version-id返回该版本号。<br/>
     * 如果桶的多版本状态是暂停的，则合并段后得到的对象版本号为null。
     * 
     * @param request 请求描述
     * @return 合并多段的返回描述
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient;<br/>
     *         ObsConfiguration config;<br/>
     *         final String endPoint = "129.4.234.2"; //存储服务器地址<br/>
     *         final int httpPort = 5080; //HTTP请求对应的端口<br/>
     *         final String ak = "DF040F692AA69F0EEC55"; //接入证书<br/>
     *         final String sk = "ffkZzMmozB4EzQr0r3HxNItX1pgAAAFLKqafDuId";
     *         //安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         <p>
     *         try<br/>
     *         {<p>
     *   &nbsp;&nbsp;&nbsp;&nbsp;// 实例化ObsClient服务<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *   &nbsp;&nbsp;&nbsp;&nbsp;// 合并段的请求对象<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest();<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;request.setBucketName("bucket001");// 设置桶名<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;request.setObjectKey("object00101");// 设置对象名<br/>
         </p>
         <p>
         &nbsp;&nbsp;&nbsp;&nbsp;// 要合并的段<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;PartEtag partEtag = new PartEtag();<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;partEtag.seteTag("4a11e911b2403bb6c6cf5e746497fe7a");<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;partEtag.setPartNumber(7);<br/>
         </p>
         <p>
         &nbsp;&nbsp;&nbsp;&nbsp;// 要合并的段集合<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;List&lt;PartEtag&gt; partEtags = new ArrayList&lt;PartEtag&gt;();<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;partEtags.add(partEtag);<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;request.setPartEtag(partEtags);<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;request.setUploadId("E609943E5BADF92B5792A4ACC9ED4573");<br/>
         </p>
         <p>
         &nbsp;&nbsp;&nbsp;&nbsp;// 调用completeMultipartUpload接口，合并已上传的段<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;CompleteMultipartUploadResult result = obsClient.completeMultipartUpload(request);<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("ObjectKey: "+result.getObjectKey()<br/>
         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; + ", Etag: " + result.getEtag());<br/>
         </p>
        }<br/>
        catch (ObsException e)<br/>
        {<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Complete uploading failed. Error message: " + e.getErrorMessage()<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
        }<br/>
     */
    public CompleteMultipartUploadResult completeMultipartUpload(
            CompleteMultipartUploadRequest request) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean(
                "completeMultipartUpload", s3Service.getEndpoint(), "");

        String uploadId = request.getUploadId();
        String bucketName = request.getBucketName();
        String objectKey = request.getObjectKey();
        S3MultipartUpload upload = new S3MultipartUpload(uploadId, bucketName,
                objectKey);
        List<PartEtag> partEtags = request.getPartEtag();
        List<MultipartPart> parts = new ArrayList<MultipartPart>();
        for (PartEtag partEtag : partEtags)
        {
            MultipartPart multiPart = new MultipartPart(
                    partEtag.getPartNumber(), new Date(), partEtag.geteTag(),
                    0l);
            parts.add(multiPart);
        }
        runningLog.debug("completeMultipartUpload", "bucketName: " + bucketName
                + "objectKey: " + objectKey + "uploadId: " + uploadId);

        try
        {
            MultipartCompleted partCompleted = s3Service
                    .multipartCompleteUpload(upload, parts);
            CompleteMultipartUploadResult result = Convert
                    .changeFromS3PartCompleted(partCompleted);
            runningLog.debug(
                    "completeMultipartUpload",
                    "BucketName: " + result.getBucketName() + "Etag: "
                            + result.getEtag() + ", ObjectKey: "
                            + result.getObjectKey());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return result;
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("completeMultipartUpload",
                    "Exception:" + e.getMessage());
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 列出已上传的段<br/>
     * 查询一个多段上传任务所属的所有段信息。
     * 
     * @param request 请求描述
     * @return 多段列表信息
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         </p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;//实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *         <p/>
     *      &nbsp;&nbsp;&nbsp;&nbsp;// 封装请求信息<br/>
            &nbsp;&nbsp;&nbsp;&nbsp;ListPartsRequest request = new ListPartsRequest();<br/>
            &nbsp;&nbsp;&nbsp;&nbsp;request.setBucketName("bucket001");<br/>
            &nbsp;&nbsp;&nbsp;&nbsp;request.setKey("object00101");<br/>
            &nbsp;&nbsp;&nbsp;&nbsp;request.setUploadId("1808A8027800AB8580C5FD69CBDE5C71");<br/>
            <p/>
            &nbsp;&nbsp;&nbsp;&nbsp;// 调用listParts接口列出Id为1808A8027800AB8580C5FD69CBDE5C71<br/>
            &nbsp;&nbsp;&nbsp;&nbsp;// 的上传任务的所有段<br/>
            &nbsp;&nbsp;&nbsp;&nbsp;<B>ListPartsResult partsList = obsClient.listParts(request);</B><br/>
            <p/>
            &nbsp;&nbsp;&nbsp;&nbsp;List&lt;Multipart&gt; parts = partsList.getMultipartList();<br/>
            &nbsp;&nbsp;&nbsp;&nbsp;for (Multipart multipart : parts)<br/>
            &nbsp;&nbsp;&nbsp;&nbsp;{<br/>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Etag: "+multipart.getEtag()<br/>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+", PartNumber: "+multipart.getPartNumber()<br/>
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+", Size: " +multipart.getSize());<br/>
            &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
        }<br/>
        catch (ObsException e)<br/>
        {<br/>
            &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Error message: " + e.getErrorMessage()<br/>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
        }<br/>
     */
    public ListPartsResult listParts(ListPartsRequest request)
            throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("listParts",
                s3Service.getEndpoint(), "");

        S3ListPartsRequest s3Request = new S3ListPartsRequest();
        s3Request.setBucketName(request.getBucketName());
        s3Request.setKey(request.getKey());
        s3Request.setMaxParts(request.getMaxParts());
        s3Request.setPartNumberMarker(request.getPartNumberMarker());
        s3Request.setUploadId(request.getUploadId());
        try
        {
            runningLog.debug("listParts", reqBean.toString());
            S3ListPartsResult s3Result = s3Service.listParts(s3Request);
            runningLog.debug("listParts", s3Result.toString());

            ListPartsResult result = new ListPartsResult();
            result.setBucket(s3Result.getBucket());
            S3Owner initiator = s3Result.getOwner();
            if(null != initiator)
            result.setInitiator(Convert.changeFromS3Owner(initiator));
            result.setKey(s3Result.getKey());
            result.setMaxParts(result.getMaxParts());
            S3Owner owner = s3Result.getOwner();
            if(null != owner)
            result.setOwner(Convert.changeFromS3Owner(owner));
            List<MultipartPart> parts = s3Result.getPart();// 获得所有的上传段
            List<Multipart> list = Convert.changeFromS3UploadPartList(parts);// 转换查询到的段
            result.setMultipartList(list);
            result.setStorageClass(s3Result.getStorageClass());
            result.setUploadId(s3Result.getUploadId());

            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return result;
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("listParts", "Exception:" + e.getMessage());
            throw Convert.changeFromS3Exception(e);
        }
    }

    /**
     * 列出所有执行中的多段上传任务。
     * 
     * @param request 请求描述
     * @return 多段上传任务列表信息
     * @throws ObsException SDK自定义异常（表示SDK或服务器不能正常处理业务时返回的信息。包括对应请求的描述信息及不能正常处理的原因和错误码）
     * @since eSDK Storage 1.5.10
     * @sample ObsClient obsClient = null;<br/>
     *         ObsConfiguration config = null;<br/>
     *         final String endPoint = "129.7.140.2";                           //存储服务器地址<br/>
     *         final int httpPort = 5080;                                       //HTTP请求对应的端口<br/>
     *         final String ak = "BE190CE793E89AABD780";                        //存储服务器用户的接入证书<br/>
     *         final String sk = "TKSbDHVXQnoEzYniUR+zKenbYtMAAAFMk+iaq8t5";    //存储服务器用户的安全证书<br/>
     *         <p>
     *         config = new ObsConfiguration();<br/>
     *         config.setEndPoint(endPoint);<br/>
     *         config.setHttpsOnly(false);<br/>
     *         config.setEndpointHttpPort(httpPort);<br/>
     *         config.setDisableDnsBucket(true);
     *         </p>
     *         try<br/>
     *         {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;//实例化ObsClient服务<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;obsClient = new ObsClient(ak, sk, config);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;String bucketName = "bucket001";<br/>
            <p/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;// 封装请求信息<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;ListMultipartUploadsRequest request = new ListMultipartUploadsRequest();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;request.setBucketName(bucketName);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;request.setMaxUploads(2);<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;request.setPrefix("obj");<br/>
            <p/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;// 调用listMultipartUploads方法列出上传任务的信息<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;<B>MultipartUploadListing listing = obsClient.listMultipartUploads(request);</B><br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;List&lt;MultipartUpload&gt;  uploads = listing.getMultipartTaskList();<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;for (MultipartUpload multipartUpload : uploads)<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;{<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;System.out.println("UploadId: " + multipartUpload.getUploadId()<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+ ", Initiator: "  + multipartUpload.getInitiator().getId());<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;}<br/>
        }<br/>
        catch (ObsException e)<br/>
        {<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;System.out.println("Error message: " + e.getErrorMessage()<br/>
     *         &nbsp;&nbsp;&nbsp;&nbsp;+ ". ResponseCode: " + e.getResponseCode());<br/>
        }<br/>
     */
    public MultipartUploadListing listMultipartUploads(
            ListMultipartUploadsRequest request) throws ObsException
    {
        InterfaceLogBean reqBean = new InterfaceLogBean("listMultipartUploads",
                s3Service.getEndpoint(), "");

        String bucketName = request.getBucketName();
        Integer maxUploads = request.getMaxUploads();
        String prefix = request.getPrefix();
        String delimiter = request.getDelimiter();
        String keyMarker = request.getKeyMarker();
        String uploadIdMarker = request.getUploadIdMarker();
        try
        {
            runningLog.debug("listMultipartUploads", "BucketName: "
                    + bucketName + ", maxUploads: " + maxUploads + ", prefix: "
                    + prefix + ", delimiter: " + delimiter + ", keyMarker: "
                    + keyMarker + ", uploadIdMarker: " + uploadIdMarker);
            MultipartUploadChunk result = s3Service
                    .multipartListUploadsChunked(bucketName, prefix, delimiter,
                            keyMarker, uploadIdMarker, maxUploads, false);
            MultipartUploadListing listResult =
                Convert.changeFromS3MultipartUploadChunk(result);// 将jet3的段任务转换为obs的段任务;
            listResult.setKeyMarker(keyMarker);// TODO
            listResult.setUploadIdMarker(uploadIdMarker);// TODO
            runningLog.debug(
                    "listMultipartUploads",
                    "BucketName: " + listResult.getBucketName()
                            + ", Delimiter: " + listResult.getDelimiter()
                            + ", KeyMarker: " + listResult.getKeyMarker()
                            + ", MaxUploads:" + listResult.getMaxUploads()
                            + ", MultipartTask amount: "
                            + listResult.getMultipartTaskList());
            reqBean.setRespTime(new Date());
            reqBean.setResultCode("0");
            ilog.info(reqBean);
            return listResult;
        }
        catch (S3ServiceException e)
        {
            reqBean.setRespTime(new Date());
            reqBean.setResultCode(e.getErrorCode());
            ilog.error(reqBean);
            runningLog.error("listMultipartUploads",
                    "Exception:" + e.getMessage());
            throw Convert.changeFromS3Exception(e);
        }

    }

    private void configfieldToProperties(ObsConfiguration config,
            Jets3tProperties jets3tProperties)
    {
        jets3tProperties.setProperty(ObsConstraint.END_POINT,
                config.getEndPoint());
        jets3tProperties.setProperty(ObsConstraint.HTTP_PORT,
                String.valueOf(config.getEndpointHttpPort()));
        jets3tProperties.setProperty(ObsConstraint.HTTPS_ONLY,
                String.valueOf(config.isHttpsOnly()));
        jets3tProperties.setProperty(ObsConstraint.DISABLE_DNS_BUCKET,
                String.valueOf(config.isDisableDnsBucket()));
        jets3tProperties.setProperty(ObsConstraint.HTTPS_PORT,
                String.valueOf(config.getEndpointHttpsPort()));
        jets3tProperties.setProperty(ObsConstraint.HTTP_SOCKET_TIMEOUT,
                String.valueOf(config.getSocketTimeout()));
        jets3tProperties.setProperty(ObsConstraint.HTTP_MAX_CONNECT,
                String.valueOf(config.getMaxConnections()));
        jets3tProperties.setProperty(ObsConstraint.HTTP_RETRY_MAX,
                String.valueOf(config.getMaxErrorRetry()));
        jets3tProperties.setProperty(ObsConstraint.HTTP_CONNECT_TIMEOUT,
                String.valueOf(config.getConnectionTimeout()));
        jets3tProperties.setProperty(ObsConstraint.DEFAULT_BUCKET_LOCATION,
                String.valueOf(config.getDefaultBucketLocation()));
    }
    
    private void asserParameterNotNull(String value,String errorMessage)
    {
        if (value == null || "".equals(value)) throw new IllegalArgumentException(errorMessage);
    }
}
