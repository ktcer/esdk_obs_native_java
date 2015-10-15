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

/**描述列举多段上传对象的返回结果
 */
public class PartListing
{
    private String uploadId;
    
    private String bucketName;
    
    private String objectKey;
    
    private List<Multipart> multipartList;
    
    /**获取多段上传任务的上传标识
     * 
     * @return 多段上传任务的上传标识
     */
    public String getUploadId()
    {
        return uploadId;
    }
    
    /** 设置多段上传任务的上传标识
     * 
     * @param uploadId 多段上传任务的上传标识
     */
    public void setUploadId(String uploadId)
    {
        this.uploadId = uploadId;
    }
    
    /**获取桶的名称
     * 
     * @return 桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置桶的名称
     * 
     * @param bucketName 桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    /**获取对象名称
     * 
     * @return 对象名称
     */
    public String getObjectKey()
    {
        return objectKey;
    }
    
    /**设置对象名称
     * 
     * @param objectKey 对象名称
     */
    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
    }
    
    /**设置多段上传任务中分段对象的信息
     * 
     * @param multipartList 多段上传任务中分段对象的信息表
     */
    public void setMultipartList(List<Multipart> multipartList)
    {
        this.multipartList = multipartList;
    }
    
    /**列举多段的分段对象信息
     * 
     * @return 返回段信息列表
     */
    public List<Multipart> getMultipartList()
    {
        return multipartList;
    }
    
}
