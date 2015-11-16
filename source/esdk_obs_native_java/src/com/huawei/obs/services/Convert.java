package com.huawei.obs.services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jets3t.service.MultipartUploadChunk;
import org.jets3t.service.ServiceException;
import org.jets3t.service.VersionOrDeleteMarkersChunk;
import org.jets3t.service.acl.S3AccessControlList;
import org.jets3t.service.acl.S3CanonicalGrantee;
import org.jets3t.service.acl.S3GrantAndPermission;
import org.jets3t.service.acl.S3GranteeInterface;
import org.jets3t.service.acl.S3GroupGrantee;
import org.jets3t.service.acl.S3Permission;
import org.jets3t.service.model.BaseVersionOrDeleteMarker;
import org.jets3t.service.model.MultipartCompleted;
import org.jets3t.service.model.MultipartPart;
import org.jets3t.service.model.MultipleDeleteResult;
import org.jets3t.service.model.MultipleDeleteResult.DeletedObjectResult;
import org.jets3t.service.model.MultipleDeleteResult.ErrorResult;
import org.jets3t.service.model.RedirectRule;
import org.jets3t.service.model.RoutingRule;
import org.jets3t.service.model.RoutingRuleCondition;
import org.jets3t.service.model.S3BucketLoggingStatus;
import org.jets3t.service.model.S3LifecycleConfiguration;
import org.jets3t.service.model.S3LifecycleConfiguration.Expiration;
import org.jets3t.service.model.S3LifecycleConfiguration.Rule;
import org.jets3t.service.model.S3MultipartUpload;
import org.jets3t.service.model.S3OptionInfoRequest;
import org.jets3t.service.model.S3OptionInfoResult;
import org.jets3t.service.model.S3Owner;
import org.jets3t.service.model.S3Quota;
import org.jets3t.service.model.S3StorageInfo;
import org.jets3t.service.model.S3StoragePolicy;
import org.jets3t.service.model.S3WebsiteConfiguration;
import org.jets3t.service.model.SS3Bucket;
import org.jets3t.service.model.SS3BucketCors;
import org.jets3t.service.model.SS3CORSRule;
import org.jets3t.service.model.SS3Object;
import org.jets3t.service.model.StorageObject;
import org.jets3t.service.model.StorageOwner;
//import org.jets3t.service.multi.event.CreateObjectsEvent;

import com.huawei.obs.services.exception.ObsException;
import com.huawei.obs.services.model.AccessControlList;
import com.huawei.obs.services.model.BucketCorsRule;
import com.huawei.obs.services.model.BucketLoggingConfiguration;
import com.huawei.obs.services.model.BucketQuota;
import com.huawei.obs.services.model.BucketStorageInfo;
import com.huawei.obs.services.model.CanonicalGrantee;
import com.huawei.obs.services.model.CompleteMultipartUploadResult;
import com.huawei.obs.services.model.CopyObjectResult;
import com.huawei.obs.services.model.CopyPartResult;
import com.huawei.obs.services.model.DeleteObjectsResult;
import com.huawei.obs.services.model.GrantAndPermission;
import com.huawei.obs.services.model.GranteeInterface;
import com.huawei.obs.services.model.GroupGrantee;
import com.huawei.obs.services.model.InitiateMultipartUploadResult;
import com.huawei.obs.services.model.LifecycleConfiguration;
import com.huawei.obs.services.model.ListVersionsResult;
import com.huawei.obs.services.model.Multipart;
import com.huawei.obs.services.model.MultipartUpload;
import com.huawei.obs.services.model.MultipartUploadListing;
import com.huawei.obs.services.model.ObjectMetadata;
import com.huawei.obs.services.model.OptionsInfoRequest;
import com.huawei.obs.services.model.OptionsInfoResult;
import com.huawei.obs.services.model.Owner;
import com.huawei.obs.services.model.Permission;
import com.huawei.obs.services.model.RedirectAllRequest;
import com.huawei.obs.services.model.RouteRule;
import com.huawei.obs.services.model.RouteRuleCondition;
import com.huawei.obs.services.model.S3Bucket;
import com.huawei.obs.services.model.S3BucketCors;
import com.huawei.obs.services.model.S3Object;
import com.huawei.obs.services.model.StoragePolicy;
import com.huawei.obs.services.model.UploadPartResult;
import com.huawei.obs.services.model.VersionOrDeleteMarker;
import com.huawei.obs.services.model.WebsiteConfiguration;

public class Convert
{
    public static DeleteObjectsResult changeFromS3DeleteResult(
            MultipleDeleteResult s3result)
    {
        DeleteObjectsResult result = new DeleteObjectsResult();
        List<DeleteObjectsResult.DeleteObjectResult> deletedObjResults = new ArrayList<DeleteObjectsResult.DeleteObjectResult>();
        List<DeleteObjectsResult.ErrorResult> errResults = new ArrayList<DeleteObjectsResult.ErrorResult>();
        for (DeletedObjectResult delObjResult : s3result
                .getDeletedObjectResults())
        {
            deletedObjResults.add(result.new DeleteObjectResult(delObjResult
                    .getKey(), delObjResult.getVersion()));
        }

        for (ErrorResult errorResult : s3result.getErrorResults())
        {
            errResults.add(result.new ErrorResult(errorResult.getKey(),
                    errorResult.getVersion(), errorResult.getErrorCode(),
                    errorResult.getMessage()));
        }

        result.setDeletedObjectResults(deletedObjResults);
        result.setErrorResults(errResults);

        return result;
    }

    /**
     * 将jet3t的查询上传任务结果转换为obs的查询结果
     * @return obs查询到的结果，返回值不会为null
     */
    public static MultipartUploadListing changeFromS3MultipartUploadChunk(
        MultipartUploadChunk uploadChunk)
    {
        MultipartUploadListing mUploadListing = new MultipartUploadListing();
        S3MultipartUpload[] s3Uploads = uploadChunk.getUploads();
        mUploadListing.setMultipartTaskList(changeFromS3MultiUpload(s3Uploads));
        mUploadListing.setTruncated(uploadChunk.isTruncate());
        mUploadListing.setDelimiter(uploadChunk.getDelimiter());
        mUploadListing.setMaxUploads(uploadChunk.getMaxUploads());
        mUploadListing.setNextUploadIdMarker(uploadChunk.getPriorLastIdMarker());
        mUploadListing.setNextKeyMarker(uploadChunk.getPriorLastKey());
        return mUploadListing;
    }
    
    /**
     * 将jet3t查询到的上传任务转为obs的上传任务
     * @param s3Uploads 要转换的jet3t上传任务
     * @return 返回值不会为null
     */
    public static List<MultipartUpload> changeFromS3MultiUpload(
        S3MultipartUpload[] s3Uploads)
    {
        List<MultipartUpload> multiUploadList = new ArrayList<MultipartUpload>();
        if(null == s3Uploads)
        {
            return multiUploadList;
        }
        for (S3MultipartUpload multipartUpload : s3Uploads)
        {
            MultipartUpload multiUpload = new MultipartUpload();
            multiUpload.setBucketName(multipartUpload.getBucketName());
            multiUpload.setObjectKey(multipartUpload.getObjectKey());
            multiUpload.setUploadId(multipartUpload.getUploadId());
            multiUpload.setInitiatedDate(multipartUpload.getInitiatedDate());
            multiUpload.setOwner(changeFromS3Owner(multipartUpload.getOwner()));
            multiUpload.setInitiator(changeFromS3Owner(multipartUpload
                    .getInitiator()));
            multiUpload.setStorageClass(multipartUpload.getStorageClass());
            multiUploadList.add(multiUpload);
        }
        return multiUploadList;
    }

    public static CompleteMultipartUploadResult changeFromS3PartCompleted(
            MultipartCompleted partCompleted)
    {
        CompleteMultipartUploadResult completeResult = new CompleteMultipartUploadResult();
        completeResult.setBucketName(partCompleted.getBucketName());
        completeResult.setObjectKey(partCompleted.getObjectKey());
        completeResult.setEtag(partCompleted.getEtag());

        return completeResult;
    }

    /**
     * 将jets3t的段转为esdk的上传结果
     * @param partRet
     * @return 上传段的结果
     */
    public static UploadPartResult changeFromS3UploadPart(MultipartPart partRet)
    {
        UploadPartResult uploadResult = new UploadPartResult();
        uploadResult.setEtag(partRet.getEtag());
        uploadResult.setPartNumber(partRet.getPartNumber());

        return uploadResult;
    }
    
    /**
     * 将jets3t上传段列表转为esdk的上传段列表<br/>
     * @param multipartParts 不能为null，可以size为0
     * @return 返回结果不为null，size可能为0
     */
    public static List<Multipart> changeFromS3UploadPartList(List<MultipartPart> multipartParts)
    {
        List<Multipart> parts = new ArrayList<Multipart>();
        for (MultipartPart partRet : multipartParts)
        {
            parts.add(changeFromUploadPart(partRet));
        }
        return parts;
    }
    
    /**
     * 将jet3的段转为esdk的段
     * @param multipartPart 不能为null
     * @return 返回结果不为null
     */
    public static Multipart changeFromUploadPart(MultipartPart multipartPart)
    {
        Multipart multipart = new Multipart();
        multipart.setEtag(multipartPart.getEtag());
        multipart.setLastModified(multipartPart.getLastModified());
        multipart.setPartNumber(multipartPart.getPartNumber());
        multipart.setSize(multipartPart.getSize());
        return multipart;
    }

    public static CopyPartResult changeFromS3MultiCopyPart(MultipartPart partRet)
    {
        CopyPartResult multiCopyResult = new CopyPartResult();
        multiCopyResult.setEtag(partRet.getEtag());
        multiCopyResult.setPartNumber(partRet.getPartNumber());

        return multiCopyResult;
    }

    public static InitiateMultipartUploadResult changeFromS3MulipartRet(
            S3MultipartUpload multipartUpload)
    {
        InitiateMultipartUploadResult multiPartInfo = new InitiateMultipartUploadResult();
        multiPartInfo.setBucketName(multipartUpload.getBucketName());
        multiPartInfo.setObjectKey(multipartUpload.getObjectKey());
        multiPartInfo.setUploadId(multipartUpload.getUploadId());
        return multiPartInfo;
    }

    public static void fillCopyResult(CopyObjectResult copyRet,
            Map<String, Object> retMap)
    {
        copyRet.setLastModified((Date) retMap.get("Last-Modified"));
        copyRet.setEtag((String) retMap.get("ETag"));
    }

    public static S3Bucket changeFromS3Bucket(SS3Bucket s3Bucket)
    {
        S3Bucket obsBucket = new S3Bucket();
        obsBucket.setBucketName(s3Bucket.getName());
        obsBucket.setMetadata(s3Bucket.getMetadataMap());
        obsBucket.setLocation(s3Bucket.getLocation());
        if (s3Bucket.getOwner() != null)
        {
            obsBucket.setOwner(changeFromS3Owner(s3Bucket.getOwner()));
        }
        if (null != s3Bucket.getAcl())
        {
            obsBucket.setAcl(changeFromS3Acl(s3Bucket.getAcl()));
        }
        obsBucket.setCreationDate(s3Bucket.getCreationDate());

        return obsBucket;
    }
    
    public static SS3Bucket changeToSS3Bucket(S3Bucket s3Bucket)
    {
        SS3Bucket ss3Bucket = new SS3Bucket();
        ss3Bucket.setCreationDate(s3Bucket.getCreationDate());
        ss3Bucket.setLocation(s3Bucket.getLocation());
        ss3Bucket.setName(s3Bucket.getBucketName());
        if (s3Bucket.getOwner() != null)
        {
            ss3Bucket.setOwner(changeToS3Owner(s3Bucket.getOwner()));
        }
        if (null != s3Bucket.getAcl())
        {
            ss3Bucket.setAcl(changeToS3Acl(s3Bucket.getAcl()));
        }
        return ss3Bucket;
    }

    /**
     * @param acl 不能为null，但是属性可以为null
     * @return
     */
    public static S3AccessControlList changeToS3Acl(AccessControlList obsAcl)
    {
        S3AccessControlList acl = new S3AccessControlList();
        acl.setOwner(changeToS3Owner(obsAcl.getOwner()));
        if(null != obsAcl.getGrantAndPermissions())
            acl.grantAllPermissions(changeToS3grants(obsAcl
                .getGrantAndPermissions()));

        return acl;
    }

    /**
     * @param acl 不能为null，但是属性可以为null
     * @return
     */
    public static AccessControlList changeFromS3Acl(S3AccessControlList acl)
    {
        AccessControlList obsAcl = new AccessControlList();
        obsAcl.setOwner(changeFromS3Owner(acl.getOwner()));
        obsAcl.grantAllPermissions(changeFromS3grants(acl
                .getGrantAndPermissions()));

        return obsAcl;
    }
    
    /**
     * @param grantAndPermissions 不能为null
     * @return
     */
    public static GrantAndPermission[] changeFromS3grants(
            S3GrantAndPermission[] grantAndPermissions)
    {
        List<GrantAndPermission> obsGrantAndPermissions = new ArrayList<GrantAndPermission>();
        for (S3GrantAndPermission gap : grantAndPermissions)
        {
            obsGrantAndPermissions.add(new GrantAndPermission(
                    changeFromS3grantee(gap.getGrantee()),
                    changeFromS3permission(gap.getPermission())));
        }
        return (GrantAndPermission[]) obsGrantAndPermissions
                .toArray(new GrantAndPermission[obsGrantAndPermissions.size()]);
    }

    public static S3GrantAndPermission[] changeToS3grants(
            GrantAndPermission[] obsGrantAndPermissions)
    {
        List<S3GrantAndPermission> grantAndPermissions = new ArrayList<S3GrantAndPermission>();
        for (GrantAndPermission gap : obsGrantAndPermissions)
        {
            grantAndPermissions.add(new S3GrantAndPermission(
                    changeToS3grantee(gap.getGrantee()),
                    changeToS3permission(gap.getPermission())));
        }

        return (S3GrantAndPermission[]) grantAndPermissions
                .toArray(new S3GrantAndPermission[grantAndPermissions.size()]);
    }

    public static S3GranteeInterface changeToS3grantee(GranteeInterface grantee)
    {
        if (grantee instanceof CanonicalGrantee)
        {
            S3CanonicalGrantee can = new S3CanonicalGrantee();
            if (null != grantee)
            {
                can.setIdentifier(grantee.getIdentifier());
                can.setDisplayName(((CanonicalGrantee) grantee)
                        .getDisplayName());
            }
            return can;
        }
        else
        /* (grantee instanceof GroupGrantee) */
        {
            return new S3GroupGrantee(grantee.getIdentifier());
        }
    }

    public static GranteeInterface changeFromS3grantee(
            S3GranteeInterface grantee)
    {
        if (grantee instanceof S3CanonicalGrantee)
        {
            CanonicalGrantee can = new CanonicalGrantee();
            can.setIdentifier(grantee.getIdentifier());
            can.setDisplayName(((S3CanonicalGrantee) grantee).getDisplayName());
            return can;
        }
        else
        /* (grantee instanceof GroupGrantee) */
        {
            return new GroupGrantee(grantee.getIdentifier());
        }
    }

    /**
     * @param obsPermission 可以为null,如果为null,则返回null
     * @return
     */
    public static S3Permission changeToS3permission(Permission obsPermission)
    {
        if(null == obsPermission)
        {
            return null;
        }
        S3Permission permission = S3Permission.parsePermission(obsPermission
                .toString());
        return permission;
    }

    /**
     * @param permission 可以为null,如果为null,则返回null
     * @return
     */
    public static Permission changeFromS3permission(S3Permission permission)
    {
        if(null == permission)
        {
            return null;
        }
        Permission obsPermission = Permission.parsePermission(permission
                .toString());
        return obsPermission;
    }

    public static StorageOwner changeToS3Owner(Owner obsOwner)
    {
        StorageOwner owner = null;
        if (null != obsOwner)
        {
            owner = new StorageOwner();
            owner.setDisplayName(obsOwner.getDisplayName());
            owner.setId(obsOwner.getId());
        }

        return owner;
    }

    /**
     * @param owner 不能为null,但是owner属性可以为null
     * @return
     */
    public static Owner changeFromS3Owner(StorageOwner owner)
    {
        Owner obsOwner = new Owner();
        obsOwner.setDisplayName(owner.getDisplayName());
        obsOwner.setId(owner.getId());
        return obsOwner;
    }

    /**
     * @param object 
     * @return OBS对象
     * @throws ObsException
     */
    public static S3Object changeFromS3Object(StorageObject object) throws ObsException
    {
        S3Object obsObject = new S3Object();
        obsObject.setBucketName(object.getBucketName());
        obsObject.setObjectKey(object.getKey());
        try
        {
            obsObject.setObjectContent(object.getDataInputStream());
        }
        catch (ServiceException e)
        {
            throw changeFromS3Exception(e);
        }
        obsObject.setMetadata(changeToS3ObjectMeta(object));

        return obsObject;
    }
    
    public static SS3Object changToSS3Object(S3Object s3Object) throws NoSuchAlgorithmException, IOException
    {
        SS3Object jetObject = new SS3Object();
        jetObject.setBucketName(s3Object.getBucketName());
        jetObject.setKey(s3Object.getObjectKey());
        jetObject.setDataInputStream(s3Object.getObjectContent());
//        jetObject.setMd5Hash(ServiceUtils.computeMD5Hash(s3Object.getObjectContent()));
        if(null != s3Object.getMetadata())
        {
            if(null != s3Object.getMetadata().getContentType())
            {
                jetObject.setContentType(s3Object.getMetadata().getContentType());
            }
            if(null != s3Object.getMetadata().getEtag())
            {
                jetObject.setETag(s3Object.getMetadata().getEtag());
            }
            if(null != s3Object.getMetadata().getContentEncoding())
            {
                jetObject.setContentEncoding(s3Object.getMetadata().getContentEncoding());
            }
            if(null != s3Object.getMetadata().getLastModified())
            {
                jetObject.setLastModifiedDate(s3Object.getMetadata().getLastModified());
            }
            if(null != s3Object.getMetadata().getContentLength())
            {
                jetObject.setContentLength(s3Object.getMetadata().getContentLength());
            }
            if(null != s3Object.getMetadata().getMetadata() &&
                !s3Object.getMetadata().getMetadata().isEmpty())
            {
                jetObject.addAllMetadata(s3Object.getMetadata().getMetadata());
            }
        }
        return jetObject;
    }

    public static ObjectMetadata changeToS3ObjectMeta(StorageObject object)
    {
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setMetadata(object.getMetadataMap());
        objMeta.setLastModified(object.getLastModifiedDate());
        objMeta.setContentLength(object.getContentLength());
        objMeta.setContentType(object.getContentType());
        objMeta.setContentEncoding(object.getContentEncoding());
        objMeta.setEtag(object.getETag());
        return objMeta;
    }
    
    public static BucketStorageInfo changeFromS3StroageInfo(
            S3StorageInfo storageInfo)
    {
        BucketStorageInfo obsStorageInfo = new BucketStorageInfo();
        if (storageInfo != null)
        {
            obsStorageInfo.setSize(Long.valueOf(storageInfo.getSize()));
            obsStorageInfo.setObjectNumber(Long.valueOf(storageInfo
                    .getObjectNum()));
        }
        return obsStorageInfo;
    }

    public static BucketQuota changeFromS3Quota(S3Quota quota)
    {
        BucketQuota obsQuota = new BucketQuota();
        if (quota != null)
        {
            obsQuota.setBucketQuota(Long.valueOf(quota.getStorageQuota()));
        }
        return obsQuota;
    }

    public static S3Quota changeToS3Quota(BucketQuota obsQuota)
    {
        S3Quota quota = new S3Quota();
        quota.setStorageQuota(Long.toString(obsQuota.getBucketQuota()));
        return quota;
    }

    public static StoragePolicy changeFromS3StoragePolicy(
            S3StoragePolicy storagePolicy)
    {
        StoragePolicy obsStoragePolicy = new StoragePolicy();
        if (storagePolicy != null)
        {
            obsStoragePolicy.setStoragePolicyName(storagePolicy.getName());
        }
        return obsStoragePolicy;
    }

    public static S3StoragePolicy changeToS3StoragePolicy(
            StoragePolicy obsStoragePolicy)
    {
        S3StoragePolicy storagePolicy = new S3StoragePolicy();
        storagePolicy.setName(obsStoragePolicy.getStoragePolicyName());
        return storagePolicy;
    }

    public static BucketLoggingConfiguration changeFromS3BucketLoggingStatus(
            S3BucketLoggingStatus loggingStatus)
    {
        BucketLoggingConfiguration loggingConfiguration = new BucketLoggingConfiguration();
        if (null != loggingStatus)
        {
            loggingConfiguration.setTargetBucketName(loggingStatus
                    .getTargetBucketName());
            loggingConfiguration.setLogfilePrefix(loggingStatus
                    .getLogfilePrefix());
            GrantAndPermission[] grantAndPermissions = changeFromS3grants(loggingStatus
                    .getTargetGrants());
            loggingConfiguration.setTargetGrants(grantAndPermissions);
        }
        return loggingConfiguration;
    }

    public static S3BucketLoggingStatus changeToS3BucketLoggingStatus(
            BucketLoggingConfiguration loggingConfiguration)
    {
        S3BucketLoggingStatus status = new S3BucketLoggingStatus();
        if (null != loggingConfiguration)
        {
            status.setTargetBucketName(loggingConfiguration
                    .getTargetBucketName());
            status.setLogfilePrefix(loggingConfiguration.getLogfilePrefix());
            S3GrantAndPermission[] s3GrantAndPermissions = changeToS3grants(loggingConfiguration
                    .getTargetGrants());
            status.setTargetGrants(s3GrantAndPermissions);
        }
        return status;
    }

    public static LifecycleConfiguration changeFromS3LifecycleConfiguration(
            S3LifecycleConfiguration s3lifecycleConfig)
    {
        LifecycleConfiguration lifecycleConfig = new LifecycleConfiguration();
        if (null != s3lifecycleConfig)
        {
            List<Rule> s3Rules = s3lifecycleConfig.getRules();
            com.huawei.obs.services.model.LifecycleConfiguration.Rule rule = null;
            for (Rule s3Rule : s3Rules)
            {
                rule = lifecycleConfig.newRule(s3Rule.getId(),
                        s3Rule.getPrefix(), s3Rule.getEnabled());

                Expiration s3Expiration = s3Rule.getExpiration();
                com.huawei.obs.services.model.LifecycleConfiguration.Expiration expiration = rule
                        .newExpiration();
                if (null != s3Expiration.getDate())
                {
                    expiration.setDate(s3Expiration.getDate());
                }
                else
                {
                    expiration.setDays(s3Expiration.getDays());
                }

                rule.setExpiration(expiration);
            }
        }
        return lifecycleConfig;

    }

    public static S3LifecycleConfiguration changeToS3LifecycleConfiguration(
            LifecycleConfiguration lifecycleConfig)
    {
        S3LifecycleConfiguration s3LifecycleConfig = new S3LifecycleConfiguration();
        if (null != lifecycleConfig)
        {
            List<com.huawei.obs.services.model.LifecycleConfiguration.Rule> rules = lifecycleConfig
                    .getRules();
            Rule s3Rule = null;
            for (com.huawei.obs.services.model.LifecycleConfiguration.Rule rule : rules)
            {
                s3Rule = s3LifecycleConfig.newRule(rule.getId(),
                        rule.getPrefix(), rule.getEnabled());

                com.huawei.obs.services.model.LifecycleConfiguration.Expiration expiration = rule
                        .getExpiration();
                Expiration s3Expiration = s3Rule.newExpiration();
                if (null != expiration.getDate())
                {
                    Date date = expiration.getDate();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    //如果用户使用本地时区，则补齐本地时区与UTC间的偏移量(此处是增加偏移量<偏移量本身区分正负>，在jets3t处会格式化为UTC的0时区)。 
                    long offSet = 0;
                    offSet = calendar.getTimeInMillis() + calendar.getTimeZone().getRawOffset();
                    calendar.setTimeInMillis(offSet);
                    
                    s3Expiration.setDate(calendar.getTime());
                }
                else
                {
                    s3Expiration.setDays(expiration.getDays());
                }

                s3Rule.setExpiration(s3Expiration);
            }
        }
        return s3LifecycleConfig;
    }

    public static WebsiteConfiguration changeFromS3WebsiteConfiguration(
            S3WebsiteConfiguration s3WebsiteConfig)
    {
        WebsiteConfiguration websiteConfig = new WebsiteConfiguration();

        if (null != s3WebsiteConfig)
        {
            websiteConfig.setSuffix(s3WebsiteConfig.getIndexDocumentSuffix());
            websiteConfig.setKey(s3WebsiteConfig.getErrorDocumentKey());

            //将S3WebsiteConfiguration的RedirectRule转换为WebsiteConfiguration的RedirectAllRequest
            RedirectRule s3RedirectRule = s3WebsiteConfig.getRedirectAllRequestsTo();
            if (null != s3RedirectRule)
            {
                RedirectAllRequest redirectAll = new RedirectAllRequest();
                redirectAll.setHostName(s3RedirectRule.getHostName());
                redirectAll.setProtocol(s3RedirectRule.getProtocol());
                websiteConfig.setRedirectAllRequestsTo(redirectAll);
            }

            //将S3WebsiteConfiguration的List<RoutingRule>转换为WebsiteConfiguration的List<RouteRule>
            List<RouteRule> routeRules = new ArrayList<RouteRule>();
            
            List<RoutingRule> s3routingRules = s3WebsiteConfig
                    .getRoutingRules();
            for (RoutingRule routingRule : s3routingRules)
            {
                RoutingRuleCondition s3Condition = routingRule.getCondition();
                RedirectRule s3Redirect = routingRule.getRedirect();

                RouteRule rule = new RouteRule();
                RouteRuleCondition condition = new RouteRuleCondition();
                com.huawei.obs.services.model.Redirect redirect = new com.huawei.obs.services.model.Redirect();

                if (null != s3Condition)
                {
                    condition.setHttpErrorCodeReturnedEquals(s3Condition
                            .getHttpErrorCodeReturnedEquals());
                    condition.setKeyPrefixEquals(s3Condition
                            .getKeyPrefixEquals());
                    rule.setCondition(condition);
                }

                if (null != s3Redirect)
                {
                    redirect.setHostName(s3Redirect.getHostName());
                    redirect.setHttpRedirectCode(s3Redirect
                            .getHttpRedirectCode());
                    redirect.setProtocol(s3Redirect.getProtocol());
                    redirect.setReplaceKeyPrefixWith(s3Redirect
                            .getReplaceKeyPrefixWith());
                    redirect.setReplaceKeyWith(s3Redirect.getReplaceKeyWith());
                    rule.setRedirect(redirect);
                }

                routeRules.add(rule);
            }
            websiteConfig.setRouteRules(routeRules);
        }
        return websiteConfig;
    }

    public static S3WebsiteConfiguration changeToS3WebsiteConfiguration(
            WebsiteConfiguration WebsiteConfig)
    {
        S3WebsiteConfiguration s3WebsiteConfig = new S3WebsiteConfiguration();

        if (null != WebsiteConfig)
        {
            s3WebsiteConfig.setIndexDocumentSuffix(WebsiteConfig.getSuffix());
            s3WebsiteConfig.setErrorDocumentKey(WebsiteConfig.getKey());

            // 将WebsiteConfiguration的RedirectAllRequest转换为S3WebsiteConfiguration的RedirectRule
            RedirectAllRequest redirectAllRequest = WebsiteConfig
                    .getRedirectAllRequestsTo();
            if (null != redirectAllRequest)
            {
                RedirectRule s3RedirectRule = new RedirectRule();
                s3RedirectRule.setHostName(redirectAllRequest.getHostName());
                s3RedirectRule.setProtocol(redirectAllRequest.getProtocol());
                s3WebsiteConfig.setRedirectAllRequestsTo(s3RedirectRule);
            }

            // 将WebsiteConfiguration的List<RouteRule>转换为S3WebsiteConfiguration的List<RoutingRule>
            List<RoutingRule> s3RouteRules = new ArrayList<RoutingRule>();
            List<RouteRule> routeRules = WebsiteConfig.getRouteRules();
            for (RouteRule routeRule : routeRules)
            {
                RoutingRule s3RoutingRule = new RoutingRule();

                RouteRuleCondition condition = routeRule.getCondition();
                if (null != condition)
                {
                    RoutingRuleCondition s3Condition = new RoutingRuleCondition();
                    s3Condition.setHttpErrorCodeReturnedEquals(condition
                            .getHttpErrorCodeReturnedEquals());
                    s3Condition.setKeyPrefixEquals(condition
                            .getKeyPrefixEquals());
                    s3RoutingRule.setCondition(s3Condition);
                }

                com.huawei.obs.services.model.Redirect redirectRule = routeRule
                        .getRedirect();

                if (null != redirectRule)
                {
                    RedirectRule s3Redirect = new RedirectRule();
                    s3Redirect.setHostName(redirectRule.getHostName());
                    s3Redirect.setHttpRedirectCode(redirectRule
                            .getHttpRedirectCode());
                    s3Redirect.setProtocol(redirectRule.getProtocol());
                    s3Redirect.setReplaceKeyPrefixWith(redirectRule
                            .getReplaceKeyPrefixWith());
                    s3Redirect.setReplaceKeyWith(redirectRule
                            .getReplaceKeyWith());
                    s3RoutingRule.setRedirect(s3Redirect);
                }

                s3RouteRules.add(s3RoutingRule);
            }
            s3WebsiteConfig.setRoutingRules(s3RouteRules);
        }
        return s3WebsiteConfig;
    }

    public static ObsException changeFromS3Exception(ServiceException se)
    {
        ObsException exception;
        // 主要是考虑空指针，网络超时等其他异常
//        if(!(se.getCause() instanceof ServiceException) && null == se.getErrorMessage())
//        {
//            String errorMessage = se.getMessage();
//            exception = new ObsException(errorMessage);
//            exception.setErrorMessage(errorMessage);
//            exception.setResponseCode(se.getResponseCode());
//            exception.setResponseStatus(se.getResponseStatus());
//        }
        if (se.getResponseCode() < 0)
        {
            exception = new ObsException("OBS servcie Error Message. "
                    + se.getMessage());
        }
        else
        {
            exception = new ObsException("OBS servcie Error Message.",
                    se.getXmlMessage());
            exception.setErrorCode(se.getErrorCode());
            exception.setErrorMessage(se.getErrorMessage()== null ?se.getMessage():se.getErrorMessage());
            exception.setErrorRequestId(se.getErrorRequestId());
            exception.setErrorHostId(se.getErrorHostId());
            exception.setResponseCode(se.getResponseCode());
            exception.setResponseStatus(se.getResponseStatus());
            exception.setResponseDate(se.getResponseDate());
            exception.setResponseHeaders(se.getResponseHeaders());
            exception.setRequestVerb(se.getRequestVerb());
            exception.setRequestPath(se.getRequestPath());
            exception.setRequestHost(se.getRequestHost());
        }
        return exception;
    }
    
    // TODO 非空校验
//    public static GetObjectsEvent changeFromS3GetObjectsEvent(
//        org.jets3t.service.multi.event.GetObjectsEvent event)
//    {
//        GetObjectsEvent getObjectsEvent = null;
//        switch(event.getEventCode())
//        {
//            case GetObjectsEvent.EVENT_STARTED :
//                ThreadWatcher threadWatcher = changeFromS3ThreadWatcher(event.getThreadWatcher());
//                getObjectsEvent = GetObjectsEvent.newStartedEvent(threadWatcher, event.getUniqueOperationId());
//                break;
//            case GetObjectsEvent.EVENT_IN_PROGRESS :
//                StorageObject[] objects = event.getCompletedObjects();// TODO 需要再确认
//                ThreadWatcher threadWatcher2 = changeFromS3ThreadWatcher(event.getThreadWatcher());
//                S3Object[] s3Objects;
//                try
//                {
//                    s3Objects = changeFromSS3Objects(objects);
//                }
//                catch (ObsException e1)
//                {
//                    getObjectsEvent = GetObjectsEvent.newErrorEvent(e1, event.getUniqueOperationId());
//                    break;
//                }
//                getObjectsEvent = GetObjectsEvent.newInProgressEvent(threadWatcher2, s3Objects,
//                    event.getUniqueOperationId());
//                break;
//            case GetObjectsEvent.EVENT_CANCELLED :
//                StorageObject[] cancelledObjects = event.getCancelledObjects();
//                S3Object[] cancelledS3Objects = null;
//                try
//                {
//                    cancelledS3Objects = changeFromSS3Objects(cancelledObjects);
//                }
//                catch (ObsException e)
//                {
//                    getObjectsEvent = GetObjectsEvent.newErrorEvent(e, event.getUniqueOperationId());
//                    break;
//                }
//                getObjectsEvent =
//                    GetObjectsEvent.newCancelledEvent(cancelledS3Objects, event.getUniqueOperationId());
//                break;
//            case GetObjectsEvent.EVENT_ERROR :
//                getObjectsEvent =
//                GetObjectsEvent.newErrorEvent(event.getErrorCause(), event.getUniqueOperationId());
//                break;
//            case GetObjectsEvent.EVENT_IGNORED_ERRORS :
//                ThreadWatcher threadWatcher3 = changeFromS3ThreadWatcher(event.getThreadWatcher());
//                getObjectsEvent =
//                GetObjectsEvent.newIgnoredErrorsEvent(threadWatcher3, event.getIgnoredErrors(), event.getUniqueOperationId());
//                break;
//            case GetObjectsEvent.EVENT_COMPLETED :
//                getObjectsEvent = GetObjectsEvent.newCompletedEvent(event.getUniqueOperationId());
//        }
//        return getObjectsEvent;
//    }
//    
//    public static PutObjectsEvent changeFromS3CreateObjectsEvent(CreateObjectsEvent event)
//    {
//        PutObjectsEvent putObjectsEvent = null;
//        switch(event.getEventCode())
//        {
//            case PutObjectsEvent.EVENT_STARTED :
//                ThreadWatcher threadWatcher = changeFromS3ThreadWatcher(event.getThreadWatcher());
//                putObjectsEvent = PutObjectsEvent.newStartedEvent(threadWatcher, event.getUniqueOperationId());
//                break;
//            case PutObjectsEvent.EVENT_IN_PROGRESS :
//                ThreadWatcher threadWatcher2 = changeFromS3ThreadWatcher(event.getThreadWatcher());
//                StorageObject[] objects = event.getCreatedObjects();
//                S3Object[] s3Objects;
//                try
//                {
//                    s3Objects = changeFromSS3Objects(objects);
//                }
//                catch (ObsException e1)
//                {
//                    putObjectsEvent = PutObjectsEvent.newErrorEvent(e1, event.getUniqueOperationId());
//                    break;// TODO log
//                }
//                putObjectsEvent = PutObjectsEvent.newInProgressEvent(threadWatcher2, s3Objects,
//                    event.getUniqueOperationId());
//                break;
//            case PutObjectsEvent.EVENT_CANCELLED :
//                StorageObject[] cancelledObjects = event.getCancelledObjects();
//                S3Object[] cancelledS3Objects = null;
//                try
//                {
//                    cancelledS3Objects = changeFromSS3Objects(cancelledObjects);
//                }
//                catch (ObsException e)
//                {
//                    putObjectsEvent = PutObjectsEvent.newErrorEvent(e, event.getUniqueOperationId());
//                    break;// TODO log
//                }
//                putObjectsEvent =
//                    PutObjectsEvent.newCancelledEvent(cancelledS3Objects, event.getUniqueOperationId());
//                break;
//            case PutObjectsEvent.EVENT_ERROR :
//                putObjectsEvent =
//                PutObjectsEvent.newErrorEvent(event.getErrorCause(), event.getUniqueOperationId());
//                break;
//            case PutObjectsEvent.EVENT_IGNORED_ERRORS :
//                ThreadWatcher threadWatcher3 = changeFromS3ThreadWatcher(event.getThreadWatcher());
//                putObjectsEvent =
//                PutObjectsEvent.newIgnoredErrorsEvent(threadWatcher3, event.getIgnoredErrors(), event.getUniqueOperationId());
//                break;
//            case PutObjectsEvent.EVENT_COMPLETED :
//                putObjectsEvent = PutObjectsEvent.newCompletedEvent(event.getUniqueOperationId());
//        }
//        return putObjectsEvent;
//    }
//    
    public static S3Object[] changeFromSS3Objects(StorageObject[] completedObjects) throws ObsException
    {
        S3Object[] s3Objects = null;
        if(null != completedObjects)
        {
            s3Objects = new S3Object[completedObjects.length];
            for (int i = 0;i < completedObjects.length; i++)
            {
                s3Objects[i] = changeFromS3Object(completedObjects[i]);
            }
        }
        return s3Objects;
    }
    
    public static BucketCorsRule changFromSS3BucketCorsRule(SS3CORSRule s3cors)
    {
        BucketCorsRule bucketCors =  new BucketCorsRule();
        if(s3cors != null){
            bucketCors.setId(s3cors.getId());
            bucketCors.setAllowedHeader(s3cors.getAllowedHeaders());
            bucketCors.setAllowedMethod(s3cors.getAllowedMethods());
            bucketCors.setAllowedOrigin(s3cors.getAllowedOrigins());
            bucketCors.setMaxAgeSecond(s3cors.getMaxAgeSeconds());
            bucketCors.setExposeHeader(s3cors.getExposedHeaders());
        }
        return bucketCors;
    }
    
    public static SS3CORSRule changeToSS3CORSRule(BucketCorsRule corsRule)
    {
        SS3CORSRule cors = new SS3CORSRule();
        if (corsRule != null){
            cors.setId(corsRule.getId());
            cors.setAllowedHeaders(corsRule.getAllowedHeader());
            cors.setAllowedMethods(corsRule.getAllowedMethod());
            cors.setAllowedOrigins(corsRule.getAllowedOrigin());
            cors.setExposedHeaders(corsRule.getExposeHeader());
        }
        return cors;
    }
    
    public static S3BucketCors changFromSS3BucketCors(SS3BucketCors s3cors)
    {
        S3BucketCors s3BucketCors = new S3BucketCors();
        List<BucketCorsRule> rules = new ArrayList<BucketCorsRule>();
        List<SS3CORSRule> ss3Rules = s3cors.getRules();
        for (int i = 0; i < ss3Rules.size(); i++)
        {
            BucketCorsRule rule = changFromSS3BucketCorsRule(ss3Rules.get(i));
            rules.add(rule);
        }
        s3BucketCors.setRules(rules);
        return s3BucketCors;
    }
    
    public static SS3BucketCors changeToSS3BucketCors(S3BucketCors s3BucketCors)
    {
        SS3BucketCors sS3BucketCors = new SS3BucketCors();
        List<SS3CORSRule> ss3Rules = new ArrayList<SS3CORSRule>();
        List<BucketCorsRule> s3Rules =s3BucketCors.getRules();
        for (int i = 0; i < s3Rules.size(); i++)
        {
            SS3CORSRule ss3CORSRule = changeToSS3CORSRule(s3Rules.get(i));
            ss3Rules.add(ss3CORSRule);
        }
        sS3BucketCors.setRules(ss3Rules);
        return sS3BucketCors;
    }
    
    public static OptionsInfoResult changeFromSS3Options(S3OptionInfoResult S3option)
    {
        OptionsInfoResult option = new OptionsInfoResult();
        if(S3option != null){
            option.setAllowOrigin(S3option.getAllowOrigin());
            option.setMaxAge(S3option.getMaxAge());
            option.setAllowHeaders(S3option.getAllowHeaders());
            option.setAllowMethods(S3option.getAllowMethods());
            option.setExposeHeaders(S3option.getExposeHeaders()); 
        }
        return option;
    }
    
    public static S3OptionInfoRequest changeToS3Options(OptionsInfoRequest option)
    {
        S3OptionInfoRequest S3option = new  S3OptionInfoRequest();
        if(option != null){
            S3option.setOrigin(option.getOrigin());
            S3option.setRequestHeaders(option.getRequestHeaders());
            S3option.setRequestMethod(option.getRequestMethod());
        }
        return S3option;
    }
   
    /**
     * @param threadWatcher
     * @return 不会为NULL 但是线程可能为0
     */
//    public static ThreadWatcher changeFromS3ThreadWatcher(org.jets3t.service.multi.ThreadWatcher threadWatcher)
//    {
//        ThreadWatcher obsThreadWatcher = new ThreadWatcher(threadWatcher == null ? 0l : threadWatcher.getThreadCount());
//        threadWatcher.getBytesPerSecond();
//        threadWatcher.getBytesTotal();
//        threadWatcher.getBytesTransferred();
//        threadWatcher.getCancelEventListener();
//        threadWatcher.getCompletedThreads();
//        threadWatcher.getThreadCount();
//        threadWatcher.getTimeRemaining();
//        return obsThreadWatcher;// TODO 具体转换过程待处理
//    }
    
    /**
     * 
     * 将listVersionsResult转换
     * @param chunk 可以为null,如果chunk为null,则直接返回null
     * @return 可能为null,或者其中的参数也可能为null
     */
    public static ListVersionsResult changeFromS3ListVersionsResult(VersionOrDeleteMarkersChunk chunk)
    {
        if(null == chunk)
        {
            return null;
        }
        ListVersionsResult listVersionsResult = new ListVersionsResult();
        listVersionsResult.setBuketName(chunk.getBucketName());
        listVersionsResult.setKeyMaker(chunk.getNextKeyMarker());
        listVersionsResult.setPrefix(chunk.getPrefix());
        listVersionsResult.setTruncated(chunk.isListingComplete());
        listVersionsResult.setVersionIdMarker(chunk.getNextVersionIdMarker());
        BaseVersionOrDeleteMarker[] baseVersionOrDeleteMarkers = chunk.getItems();
        VersionOrDeleteMarker[] versions = changeFromS3BaseVersionOrDeleteMarkers(baseVersionOrDeleteMarkers);
        listVersionsResult.setVersions(versions);
        return listVersionsResult;
    }
    
    /**
     * @param baseVersionOrDeleteMarkers 如果参数为null,则返回null
     * @return array
     */
    public static VersionOrDeleteMarker[] changeFromS3BaseVersionOrDeleteMarkers(
        BaseVersionOrDeleteMarker[] baseVersionOrDeleteMarkers)
    {
        if(null == baseVersionOrDeleteMarkers)
        {
            return null;
        }
        VersionOrDeleteMarker[] arr = new VersionOrDeleteMarker[baseVersionOrDeleteMarkers.length];
        for (int i = 0; i < baseVersionOrDeleteMarkers.length; i++)
        {
            arr[i] = changeFromS3BaseVersionOrDeleteMarker(baseVersionOrDeleteMarkers[i]);
        }
        return arr;
    }
    
    public static VersionOrDeleteMarker changeFromS3BaseVersionOrDeleteMarker(BaseVersionOrDeleteMarker baseVersionOrDeleteMarker)
    {
        S3Owner s3Owner = baseVersionOrDeleteMarker.getOwner();
        Owner owner = changeFromS3Owner(s3Owner);
        VersionOrDeleteMarker versionOrDeleteMarker =
            new VersionOrDeleteMarker(baseVersionOrDeleteMarker.getKey(),
                baseVersionOrDeleteMarker.getVersionId(),
                baseVersionOrDeleteMarker.isLatest(),
                baseVersionOrDeleteMarker.getLastModified(),
                owner);
        return versionOrDeleteMarker;
    }
    
}
