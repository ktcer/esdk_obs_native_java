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

/**描述多段上传初始化返回结果
 */
public class InitiateMultipartUploadResult
{
    private String uploadId;
    
    private String bucketName;
    
    private String objectKey;
    
    private String etag;
    
    /**获取对象的eTag
     * 
     * @return 返回对象的eTag
     */
    public String getEtag()
    {
        return etag;
    }
    
    /**设置对象的eTag
     * 
     * @param objEtag 对象的eTag
     */
    public void setEtag(String objEtag)
    {
        this.etag = objEtag;
    }
    
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
    
    /**获取要初始化的多段上传任务所属的桶
     * 
     * @return 桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置要初始化的多段上传任务所属的桶
     * 
     * @param bucketName 桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    /**获取多段上传任务对应的对象名称
     * 
     * @return 多段上传任务对应的对象名称
     */
    public String getObjectKey()
    {
        return objectKey;
    }
    
    /**设置多段上传任务对应的对象名称
     * 
     * @param objectKey 多段上传任务对应的对象名称
     */
    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
    }
    
}
