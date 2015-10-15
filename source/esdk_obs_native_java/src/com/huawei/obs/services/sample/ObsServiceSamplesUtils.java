/*
 * Copyright Notice:
 *      Copyright  1998-2009, Huawei Technologies Co., Ltd.  ALL Rights Reserved.
 *
 *      Warning: This computer software sourcecode is protected by copyright law
 *      and international treaties. Unauthorized reproduction or distribution
 *      of this sourcecode, or any portion of it, may result in severe civil and
 *      criminal penalties, and will be prosecuted to the maximum extent
 *      possible under the law.
 */
package com.huawei.obs.services.sample;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.huawei.obs.services.ObsClient;
import com.huawei.obs.services.ObsConfiguration;
import com.huawei.obs.services.exception.ObsException;
import com.huawei.obs.services.model.AbortMultipartUploadRequest;
import com.huawei.obs.services.model.AccessControlList;
import com.huawei.obs.services.model.BucketQuota;
import com.huawei.obs.services.model.BucketStorageInfo;
import com.huawei.obs.services.model.CanonicalGrantee;
import com.huawei.obs.services.model.CompleteMultipartUploadRequest;
import com.huawei.obs.services.model.CopyObjectResult;
import com.huawei.obs.services.model.CopyPartRequest;
import com.huawei.obs.services.model.CopyPartResult;
import com.huawei.obs.services.model.DeleteObjectsRequest;
import com.huawei.obs.services.model.GroupGrantee;
import com.huawei.obs.services.model.InitiateMultipartUploadRequest;
import com.huawei.obs.services.model.InitiateMultipartUploadResult;
import com.huawei.obs.services.model.KeyAndVersion;
import com.huawei.obs.services.model.ListMultipartUploadsRequest;
import com.huawei.obs.services.model.ListPartsRequest;
import com.huawei.obs.services.model.MultipartUpload;
import com.huawei.obs.services.model.MultipartUploadListing;
import com.huawei.obs.services.model.ObjectListing;
import com.huawei.obs.services.model.ObjectMetadata;
import com.huawei.obs.services.model.Owner;
import com.huawei.obs.services.model.Permission;
import com.huawei.obs.services.model.PutObjectResult;
import com.huawei.obs.services.model.S3Bucket;
import com.huawei.obs.services.model.S3Object;
import com.huawei.obs.services.model.StoragePolicy;
import com.huawei.obs.services.model.UploadPartRequest;
import com.huawei.obs.services.model.UploadPartResult;

/**SDK示例程序，示例程序演示了如何使用sdk的基本功能
 */
public class ObsServiceSamplesUtils
{
    /**测试创建桶
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     */
    public static void bucketCreateTest(ObsClient service, String bucketName)
    {
        try
        {
            S3Bucket bucketResult = service.createBucket(bucketName);
            if (bucketResult != null)
            {
                System.out.println("Create bucket :" + bucketResult.getBucketName() + " at "
                    + bucketResult.getCreationDate() + " sucessful.");
            }
        }
        catch (ObsException e)
        {
            System.out.println("Create bucket failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
    }
    
    /**测试获取桶区域
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     */
    public static void bucketGetLocationTest(ObsClient service, String bucketName)
    {
        try
        {
            String location = service.getBucketLocation(bucketName);
            System.out.println("Get bucket location :" + location);
        }
        catch (ObsException e)
        {
            System.out.println("Get bucket location failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
    }
    
    /**测试获取桶存量
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     */
    public static void bucketGetStorageInfoTest(ObsClient service, String bucketName)
    {
        try
        {
            BucketStorageInfo storageInfo = service.getBucketStorageInfo(bucketName);
            System.out.println("Get bucket storageInfo :");
            System.out.println("object number :" + storageInfo.getObjectNumber());
            System.out.println("total size :" + storageInfo.getSize());
        }
        catch (ObsException e)
        {
            System.out.println("Get bucket storageInfo failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
    }
    
    /**测试修改桶配额
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     */
    public static void bucketSetQuotaTest(ObsClient service, String bucketName)
    {
        final long quota = 512L;
        BucketQuota bucketQuota = new BucketQuota();
        bucketQuota.setBucketQuota(quota);
        try
        {
            service.setBucketQuota(bucketName, bucketQuota);
            System.out.println("set bucket quota success. ");
        }
        catch (ObsException e)
        {
            System.out.println("set bucket quota failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
    }
    
    /**测试获取桶配额
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     */
    public static void bucketGetQuotaTest(ObsClient service, String bucketName)
    {
        try
        {
            BucketQuota quota = service.getBucketQuota(bucketName);
            System.out.println("get bucket quota :" + quota.getBucketQuota());
        }
        catch (ObsException e)
        {
            System.out.println("get bucket quota failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
    }
    
    private static void aclDisplay(AccessControlList acl)
    {
        ownerDisplay(acl.getOwner());
    }
    
    /**测试获取桶ACL
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     */
    public static void bucketGetAcl(ObsClient service, String bucketName)
    {
        try
        {
            AccessControlList acl = service.getBucketAcl(bucketName);
            System.out.println("get bucket acl :");
            aclDisplay(acl);
        }
        catch (ObsException e)
        {
            System.out.println("get bucket acl failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
    }
    
    /**测试修改桶ACL
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     */
    public static void bucketSetAcl(ObsClient service, String bucketName)
    {
        AccessControlList hwAcl = new AccessControlList();
        Owner hwOwner = new Owner();
        hwOwner.setId("ECECECECECECECECECECECECECEC0001");
        hwOwner.setDisplayName("EC_user001");
        hwAcl.setOwner(hwOwner);
        CanonicalGrantee canonicalGrant = new CanonicalGrantee("ECECECECECECECECECECECECECEC0001");
        canonicalGrant.setDisplayName("EC_user001");
        GroupGrantee groupGrant = new GroupGrantee("http://acs.amazonaws.com/groups/global/AuthenticatedUsers");
        hwAcl.grantPermission(canonicalGrant, Permission.PERMISSION_WRITE);
        hwAcl.grantPermission(groupGrant, Permission.PERMISSION_READ);
        try
        {
            service.setBucketAcl(bucketName,null, hwAcl);
            System.out.println("set bucket acl success.");
        }
        catch (ObsException e)
        {
            System.out.println("set bucket acl failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
    }
    
//    /**测试获取桶存储策略
//     * 
//     * @param service MosService服务对象
//     * @param bucketName 桶名称
//     */
//    public static void bucketGetStoragePolicy(MosService service, String bucketName)
//    {
//        try
//        {
//            StoragePolicy policy = service.getBucketStoragePolicy(bucketName);
//            System.out.println("get bucket storagePolicy :" + policy.getStoragePolicyName());
//        }
//        catch (ObsException e)
//        {
//            System.out.println("get bucket storagePolicy failed. " + e.getErrorMessage() + " response code :"
//                + e.getResponseCode());
//        }
//    }
    
//    /**测试修改桶存储策略
//     * 
//     * @param service MosService服务对象
//     * @param bucketName 桶名称
//     */
//    public static void bucketSetStoragePolicy(MosService service, String bucketName)
//    {
//        StoragePolicy storagePolicy = new StoragePolicy();
//        storagePolicy.setStoragePolicyName("MDCPOLICY00");
//        try
//        {
//            service.setBucketStoragePolicy(bucketName, storagePolicy);
//            System.out.println("set bucket storagePolicy success.");
//        }
//        catch (ObsException e)
//        {
//            System.out.println("set bucket storagePolicy failed. " + e.getErrorMessage() + " response code :"
//                + e.getResponseCode());
//        }
//    }
    
    private static void ownerDisplay(Owner owner)
    {
        if (owner != null)
        {
            System.out.println("  owner id:" + owner.getId());
            System.out.println("  owner displayName:" + owner.getDisplayName());
        }
        else
        {
            System.out.println("  owner is null");
        }
    }
    
    private static void bucketDisplay(S3Bucket bucket)
    {
        System.out.println(" *bucketName:" + bucket.getBucketName());
        System.out.println("  createDate:" + bucket.getCreationDate());
        ownerDisplay(bucket.getOwner());
    }
    
    /**测试获取用户的桶列表
     * 
     * @param service MosService服务对象
     */
    public static void bucketListTest(ObsClient service)
    {
        try
        {
            List<S3Bucket> bucketList = service.listBuckets();
            Iterator<S3Bucket> itr = bucketList.iterator();
            System.out.println("List bucket success:");
            while (itr.hasNext())
            {
                S3Bucket bucketTmp = itr.next();
                bucketDisplay(bucketTmp);
            }
        }
        catch (ObsException e)
        {
            System.out.println("List bucket failed. " + e.getErrorMessage() + " response code :" + e.getResponseCode());
        }
    }
    
    /**测试获取桶内未完成的上传任务
     * 
     * @param service MosService服务对象
     */
    public static void bucketUploadTaskListTest(ObsClient service)
    {
        final int maxUploads = 2;
        try
        {
            ListMultipartUploadsRequest request = new ListMultipartUploadsRequest();
            request.setBucketName("bucketName");
            request.setMaxUploads(maxUploads);
            MultipartUploadListing result = service.listMultipartUploads(request);
            List<MultipartUpload> list = result.getMultipartTaskList();
            System.out.println("List bucket upload task success:");
            for (MultipartUpload task : list)
            {
                System.out.println("uploadId: " + task.getUploadId() + "objectKey: " + task.getObjectKey());
            }
        }
        catch (ObsException e)
        {
            System.out.println("List bucket failed. " + e.getErrorMessage() + " response code :" + e.getResponseCode());
        }
    }
    
    
    /**测试删除桶操作
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     */
    public static void bucketDeleteTest(ObsClient service, String bucketName)
    {
        try
        {
            service.deleteBucket(bucketName);
            System.out.println("Delete bucket success. ");
        }
        catch (ObsException e)
        {
            System.out.println("Delete bucket failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
    }

    private static void objectDisplay(S3Object object)
    {
        System.out.println("object :" + object.getObjectKey());
    }
    
    /**测试列举桶中的对象
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     */
    public static void objectListTest(ObsClient service, String bucketName)
    {
        try
        {
            ObjectListing result = service.listObjects(bucketName);
            Iterator<S3Object> itr = result.getObjectSummaries().iterator();
            System.out.println("List objects :");
            while (itr.hasNext())
            {
                S3Object objectTmp = (S3Object) itr.next();
                objectDisplay(objectTmp);
            }
            
        }
        catch (ObsException e)
        {
            System.out
                .println("List objects failed. " + e.getErrorMessage() + " response code :" + e.getResponseCode());
        }
    }
    
    /**测试上传对象
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     * @param objectKey 对象名称
     */
    public static void objectPutTest(ObsClient service, String bucketName, String objectKey)
    {
        String greeting = "Hello World!";
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength((long) greeting.getBytes().length);
        metadata.setContentType("text/plain");
        ByteArrayInputStream input = new ByteArrayInputStream(greeting.getBytes());
        try
        {
            PutObjectResult result = service.putObject(bucketName, objectKey, input, metadata);
            System.out.println("Put object etag: " + result.getEtag());
        }
        catch (ObsException e)
        {
            System.out.println("Put object failed. " + e.getErrorMessage() + " response code :" + e.getResponseCode());
        }
    }
    
    /**测试删除对象
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     * @param objectKey 对象名称
     */
    public static void objectDeleteTest(ObsClient service, String bucketName, String objectKey)
    {
        try
        {
            service.deleteObject(bucketName, objectKey,null);
            System.out.println("Delete object success.");
        }
        catch (ObsException e)
        {
            System.out.println("Delete object failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
    }
    
    /**测试批量删除对象
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     */
    public static void objectsDeleteTest(ObsClient service, String bucketName)
    {
        try
        {
            DeleteObjectsRequest request = new DeleteObjectsRequest();
            request.setBucketName(bucketName);
            KeyAndVersion[] keyAndVersions = new KeyAndVersion[2];
            keyAndVersions[0] = new KeyAndVersion("object001");
            keyAndVersions[1] = new KeyAndVersion("object002");// TODO 待测
            request.setKeyAndVersions(keyAndVersions);
            service.deleteObjects(request);
            System.out.println("Delete objects success.");
        }
        catch (ObsException e)
        {
            System.out.println("Delete objects failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
    }
    
    /**测试下载对象
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     * @param objectKey 对象名称
     */
    public static void objectGetTest(ObsClient service, String bucketName, String objectKey)
    {
//        try
//        {
//            S3Object object = service.getObject(bucketName, objectKey);
//            try
//            {
//                object.getObjectContent().close();
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//            System.out.println("Get object :");
//            objectDisplay(object);
//        }
//        catch (ObsException e)
//        {
//            System.out.println("Get object failed. " + e.getErrorMessage() + " response code :" + e.getResponseCode());
//        }
    }
    
    /**测试查看对象元数据
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     * @param objectKey 对象名称
     */
    public static void objectMetaGetTest(ObsClient service, String bucketName, String objectKey)
    {
//        try
//        {
//            ObjectMetadata objectMeta = service.getObjectMetadata(bucketName, objectKey);
//            System.out.println("Get object metadata :");
//            System.out.println("object Name :" + objectKey);
//            System.out.println("eTag :" + objectMeta.getEtag());
//            System.out.println("Date :" + objectMeta.getLastModified());
//            System.out.println("contentType :" + objectMeta.getContentType());
//            System.out.println("contentLength :" + objectMeta.getContentLength());
//        }
//        catch (ObsException e)
//        {
//            System.out.println("Get object metadata failed. " + e.getErrorMessage() + " response code :"
//                + e.getResponseCode());
//        }
    }
    
    /**测试获取对象ACL
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     * @param objectKey 对象名称
     */
    public static void objectGetAcl(ObsClient service, String bucketName, String objectKey)
    {
        try
        {
            AccessControlList acl = service.getObjectAcl(bucketName, objectKey, null);
            System.out.println("get object acl :");
            aclDisplay(acl);
        }
        catch (ObsException e)
        {
            System.out.println("get object acl failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
    }
    
    /**测试修改对象ACL
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     * @param objectKey 对象名称
     */
    public static void objectSetAcl(ObsClient service, String bucketName, String objectKey)
    {
        AccessControlList hwAcl = new AccessControlList();
        Owner hwOwner = new Owner();
        hwOwner.setId("ECECECECECECECECECECECECECEC0001");
        hwOwner.setDisplayName("EC_user001");
        hwAcl.setOwner(hwOwner);
        CanonicalGrantee canonicalGrant = new CanonicalGrantee("ECECECECECECECECECECECECECEC0001");
        canonicalGrant.setDisplayName("EC_user001");
        GroupGrantee groupGrant = new GroupGrantee("http://acs.amazonaws.com/groups/global/AuthenticatedUsers");
        hwAcl.grantPermission(canonicalGrant, Permission.PERMISSION_WRITE);
        hwAcl.grantPermission(groupGrant, Permission.PERMISSION_READ);
        try
        {
            service.setObjectAcl(bucketName, objectKey, hwAcl,null);
            System.out.println("set object acl success.");
        }
        catch (ObsException e)
        {
            System.out.println("set object acl failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
    }
    
    /**测试复制传对象
     * 
     * @param service MosService服务对象
     * @param bucketSrcName 桶名称
     * @param objectSrcKey 对象名称
     * @param bucketDstName 目标桶名称
     * @param objectDstKey 目标对象名称
     */
    public static void objectCopyTest(ObsClient service, String bucketSrcName, String objectSrcKey,
        String bucketDstName, String objectDstKey)
    {
        try
        {
            CopyObjectResult result = service.copyObject(bucketSrcName, objectSrcKey, bucketDstName, objectDstKey);
            System.out.println("copy object success." + result.toString());
        }
        catch (ObsException e)
        {
            System.out.println("copy object failed. " + e.getErrorMessage() + " response code :" + e.getResponseCode());
        }
    }
    
//    /**测试创建临时授权方式的URL
//     * 
//     * @param service MosService服务对象
//     * @param bucketDstName 桶名称
//     * @param objectKey 对象名称
//     */
//    private static void createSignedUrlTest(MosService service, String bucketName, String objectKey)
//    {
//        final long offsetTime = 60000L;
//        Date expiryTime = new Date(System.currentTimeMillis() + offsetTime);
//        try
//        {
//            String url = service.createSignedGetObjectListUrl(bucketName, expiryTime);
//            System.out.println("SignedGetObjectListUrl :" + url);
//
//            url = service.createSignedGetObjectUrl(bucketName, objectKey, expiryTime);
//            System.out.println("SignedGetObjectUrl :" + url);
//            
//            url = service.createSignedHeadObjectUrl(bucketName, objectKey, expiryTime);
//            System.out.println("SignedHeadObjectUrl :" + url);
//
//            url = service.createSignedGetBucketAclUrl(bucketName, expiryTime);
//            System.out.println("SignedGetBucketAclUrl :" + url);
//            
//            url = service.createSignedPutBucketAclUrl(bucketName, expiryTime);
//            System.out.println("SignedPutBucketAclUrl :" + url);
//
//            url = service.createSignedGetObjectAclUrl(bucketName, objectKey, expiryTime);
//            System.out.println("SignedGetObjectAclUrl :" + url);
//
//            url = service.createSignedPutObjectAclUrl(bucketName, objectKey, expiryTime);
//            System.out.println("SignedPutObjectAclUrl :" + url);
//        }
//        catch (ObsException e)
//        {
//            System.out.println("create signed url failed.");
//        }
//    }
    
    /**测试多段方式上传对象
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     * @param objectKey 对象名称
     */
    public static void multipartTest(ObsClient service, String bucketName, String objectKey)
    {
        final int partNumber = 2;
        InitiateMultipartUploadResult uploadInfo = null;
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest();
        request.setBucketName(bucketName);
        request.setObjectKey(objectKey);
        Map<String, Object> metadata = new HashMap<String, Object>();
        metadata.put("test-md-value", "testing, testing, 123");
        metadata.put("test-timestamp-value", System.currentTimeMillis());
        try
        {
            uploadInfo = service.initiateMultipartUpload(request);
            System.out.println("multipartUpload init success. uploadId ：" + uploadInfo.getUploadId());
            
        }
        catch (ObsException e)
        {
            System.out.println("multipartUpload init failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
            return;
        }
        
        UploadPartRequest requestUpload = null;//new UploadPartRequest();
        
        requestUpload.setBucketName(bucketName);
        requestUpload.setObjectKey(objectKey);
        requestUpload.setPartNumber(1);
        String greeting = "Hello zlw!";
        ByteArrayInputStream input = new ByteArrayInputStream(greeting.getBytes());
        requestUpload.setPartSize(Long.valueOf(greeting.getBytes().length));
//        requestUpload.setInputStream(input);
        requestUpload.setUploadId(uploadInfo.getUploadId());
        try
        {
            UploadPartResult result = service.uploadPart(requestUpload);
            System.out.println("multipart upload success. " + result.getEtag());
            
        }
        catch (ObsException e)
        {
            System.out.println("multipart upload failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println("multipart upload failed. " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("multipart upload failed. " + e.getMessage());
        }
        
        ListPartsRequest requestList = new ListPartsRequest();
        requestList.setBucketName(bucketName);
        requestList.setKey(objectKey);
        requestList.setUploadId(uploadInfo.getUploadId());
        try
        {
            service.listParts(requestList);
            System.out.println("multipart list success.");
            
        }
        catch (ObsException e)
        {
            System.out.println("multipart list failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
        
        AbortMultipartUploadRequest requestAbort = new AbortMultipartUploadRequest();
        requestAbort.setBucketName(bucketName);
        requestAbort.setObjectKey(objectKey);
        requestAbort.setUploadId(uploadInfo.getUploadId());
        try
        {
            service.abortMultipartUpload(requestAbort);
        }
        catch (ObsException e)
        {
            System.out.println("multipart abort failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
    }
    
    /**测试取消多段方式上传对象
     * 
     * @param service MosService服务对象
     * @param bucketName 桶名称
     * @param objectKey 对象名称
     */
    public static void multipartCompleteTest(ObsClient service, String bucketName, String objectKey)
    {
        InitiateMultipartUploadResult uploadInfo = null;
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest();
        request.setBucketName(bucketName);
        request.setObjectKey(objectKey);
        Map<String, Object> metadata = new HashMap<String, Object>();
        metadata.put("test-md-value", "testing, testing, 123");
        metadata.put("test-timestamp-value", System.currentTimeMillis());
        try
        {
            uploadInfo = service.initiateMultipartUpload(request);
        }
        catch (ObsException e)
        {
            System.out.println("multipartUpload init failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
            return;
        }
        
        UploadPartRequest requestUpload = null;//new UploadPartRequest();
        
        requestUpload.setBucketName(bucketName);
        requestUpload.setObjectKey(objectKey);
        requestUpload.setPartNumber(1);
        String greeting = "Hello zlw!";
        ByteArrayInputStream input = new ByteArrayInputStream(greeting.getBytes());
        requestUpload.setPartSize(Long.valueOf(greeting.getBytes().length));
//        requestUpload.setInputStream(input);
        requestUpload.setUploadId(uploadInfo.getUploadId());
        try
        {
            service.uploadPart(requestUpload);
        }
        catch (ObsException e)
        {
            System.out.println("multipart upload failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
        catch (NoSuchAlgorithmException e)
        {
            System.out.println("multipart upload failed. " + e.getMessage());
        }
        catch (IOException e)
        {
            System.out.println("multipart upload failed. " + e.getMessage());
        }
        
        CompleteMultipartUploadRequest requestCompliete = new CompleteMultipartUploadRequest();
        requestCompliete.setBucketName(bucketName);
        requestCompliete.setObjectKey(objectKey);
        requestCompliete.setUploadId(uploadInfo.getUploadId());
        try
        {
            service.completeMultipartUpload(requestCompliete);
            System.out.println("multipart complete success. ");
        }
        catch (ObsException e)
        {
            System.out.println("multipart complete failed. " + e.getErrorMessage() + " response code :"
                + e.getResponseCode());
        }
        
    }
    
    /**测试主入口
     * 
     * @param args 函数入参
     */
    public static void main(String[] args)
    {
        ObsClient service;
        final String bucketName = "bucketzlw001";
        final String objectKey = "object002";
        final String newObjectKey = "object000";
        final int httpPort = 5080;
        try
        {
            ObsConfiguration config = new ObsConfiguration();
            config.setEndPoint("129.4.234.2");
            config.setHttpsOnly(false);
            config.setEndpointHttpPort(httpPort);
            config.setDisableDnsBucket(true);
            service = new ObsClient("DF040F692AA69F0EEC55", "ffkZzMmozB4EzQr0r3HxNItX1pgAAAFLKqafDuId", config);

            System.out.println("===========TEST START===========");

            bucketCreateTest(service, bucketName);
            bucketListTest(service);
            bucketGetStorageInfoTest(service, bucketName);
            bucketGetLocationTest(service, bucketName);
            bucketSetQuotaTest(service, bucketName);
            bucketGetQuotaTest(service, bucketName);
            bucketSetAcl(service, bucketName);
            bucketGetAcl(service, bucketName);
            // In Single DC, the action setStoragePolicy is invalid.
            // bucketSetStoragePolicy(service, bucketName);
            // In Single DC, the action getStoragePolicy is invalid.
            // bucketGetStoragePolicy(service, bucketName);
            objectPutTest(service, bucketName, objectKey);
            objectCopyTest(service, bucketName, objectKey, bucketName, newObjectKey);
            objectGetTest(service, bucketName, objectKey);
            objectMetaGetTest(service, bucketName, objectKey);
            objectListTest(service, bucketName);
            objectGetAcl(service, bucketName, objectKey);
            objectSetAcl(service, bucketName, objectKey);
//            createSignedUrlTest(service, bucketName, objectKey);
            multipartTest(service, bucketName, objectKey);
            multipartCompleteTest(service, bucketName, objectKey);
            // test deleting
            objectDeleteTest(service, bucketName, objectKey);
            objectDeleteTest(service, bucketName, newObjectKey);
            bucketDeleteTest(service, bucketName);

            System.out.println("===========TEST END===========");
        }
        catch (ObsException e1)
        {
            e1.printStackTrace();
        }
        
    }

}
