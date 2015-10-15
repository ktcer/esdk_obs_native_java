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

/**描述多段合并的返回结果
 */
public class CompleteMultipartUploadResult
{
    private String bucketName;
    
    private String objectKey;
    
    private String etag;
    
    /**获取要合并的多段上传任务所属的桶
     * 
     * @return 返回桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置要合并的多段上传任务所属的桶
     * 
     * @param bucketName 桶的名称
     */
    public void setBucketName(String bucketName)
    {
        this.bucketName = bucketName;
    }
    
    /**获取要生成的对象名称
     * 
     * @return 对象名称
     */
    public String getObjectKey()
    {
        return objectKey;
    }
    
    /**设置要生成的对象名称
     * 
     * @param objectKey 对象名称
     */
    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
    }
    
    /**获取对象的eTag
     * 
     * @return 对象的etag
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
}
