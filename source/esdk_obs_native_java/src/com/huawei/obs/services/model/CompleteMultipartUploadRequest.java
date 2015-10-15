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
package com.huawei.obs.services.model;

import java.util.List;

/**描述多段合并的请求
 */
public class CompleteMultipartUploadRequest
{
    private String uploadId;
    
    private String bucketName;
    
    private String objectKey;
    
    private List<PartEtag> partEtag;
    
    /**获取多段上传任务的标识
     * 
     * @return 返回多段上传任务的标识
     */
    public String getUploadId()
    {
        return uploadId;
    }
    
    /**设置多段上传任务的标识
     * 
     * @param uploadId 多段上传任务的标识
     */
    public void setUploadId(String uploadId)
    {
        this.uploadId = uploadId;
    }
    
    /**获取多段上传任务所属的桶
     * 
     * @return 返回桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置多段上传任务所属的桶
     * 
     * @param bucketName 桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    /**返回合并分段后形成的对象名称
     * 
     * @return 对象名称
     */
    public String getObjectKey()
    {
        return objectKey;
    }
    
    /**设置合并分段后形成的对象名称
     * 
     * @param objectKey 对象名称
     */
    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
    }
    
    /**
     * 返回请求中的段信息
     * @return 请求中的段信息
     */
    public List<PartEtag> getPartEtag()
    {
        return partEtag;
    }
    
    /**
     * 设置要合并的段
     * @param partEtags 要合并的段的集合
     */
    public void setPartEtag(List<PartEtag> partEtags)
    {
        this.partEtag = partEtags;
    }
    
}
