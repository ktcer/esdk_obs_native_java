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

/**描述put方式上传对象的请求信息
 */
public class PutObjectRequest
{

    private String bucketName;
    
    private String objectKey;
    
    private InputStream input;
    
    private ObjectMetadata metadata;

    /**获取对象数据内容
     * 
     * @return 对象数据内容
     */
    public InputStream getInput()
    {
        return input;
    }

    /**设置对象数据内容
     * 
     * @param input 对象数据内容
     */
    public void setInput(InputStream input)
    {
        this.input = input;
    }

    /**获取对象元数据
     * 
     * @return 对象元数据
     */
    public ObjectMetadata getMetadata()
    {
        return metadata;
    }

    /**设置对象元数据
     * 
     * @param metadata 对象元数据
     */
    public void setMetadata(ObjectMetadata metadata)
    {
        this.metadata = metadata;
    }

    /**获取对象上传到的桶
     * 
     * @return 对象上传到的桶的名称
     */
    public String getBucketName()
    {
        return bucketName;
    }
    
    /**设置对象上传到的桶
     * 
     * @param bucketName 对象上传到的桶的名称
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
}
