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

import java.io.InputStream;

/**描述对象数据信息
 */
public class S3Object
{
    private String bucketName = null;
    
    private String objectKey = null;
    
    private ObjectMetadata metadata = new ObjectMetadata();
    
    private InputStream objectContent;
    
    /**获取对象所在的桶
     * 
     * @return 桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置对象所在的桶
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
    
    /**获取对象的元数据
     * 
     * @return 对象元数据
     */
    public ObjectMetadata getMetadata()
    {
        return metadata;
    }
    
    /**设置对象的元数据
     * 
     * @param metadata 对象的元数据
     */
    public void setMetadata(ObjectMetadata metadata)
    {
        this.metadata = metadata;
    }
    
    /**获取对象的数据流
     * 
     * @return 返回对象的数据流
     */
    public InputStream getObjectContent()
    {
        return objectContent;
    }
    
    /**设置对象的数据流
     * 
     * @param objectContent 对象的数据流
     */
    public void setObjectContent(InputStream objectContent)
    {
        this.objectContent = objectContent;
    }
}
