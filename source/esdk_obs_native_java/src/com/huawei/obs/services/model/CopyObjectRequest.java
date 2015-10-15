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


/**描述复制对象的请求
 */
public class CopyObjectRequest
{
    private String sourceBucketName;
    
    private String sourceObjectKey;
    
    private String destinationBucketName;
    
    private String destinationObjectKey;
    
    private ObjectMetadata newObjectMetadata;
    
    private boolean replaceMetadata;
    

    /**创建对象拷贝的请求的构造参数
     * 
     * @param sourceBucketName 要复制的源桶
     * @param sourceObjectKey 要复制的源对象
     * @param destinationBucketName 要复制的目标桶
     * @param destinationObjectKey 要复制的目标对象
     */
    public CopyObjectRequest(String sourceBucketName, String sourceObjectKey, String destinationBucketName,
        String destinationObjectKey)
    {
        this.sourceBucketName = sourceBucketName;
        this.sourceObjectKey = sourceObjectKey;
        this.destinationBucketName = destinationBucketName;
        this.destinationObjectKey = destinationObjectKey;
    }
    
    /**获取要复制的源桶的名称
     * 
     * @return  桶的名称
     */
    public String getSourceBucketName()
    {
        return sourceBucketName;
    }
    
    /**设置要复制的源桶的名称
     * 
     * @param sourceBucketName 源桶的名称
     */
    public void setSourceBucketName(String sourceBucketName)
    {
        this.sourceBucketName = sourceBucketName;
    }
    
    /**获取要复制的源对象名称
     * 
     * @return  对象名称
     */
    public String getSourceObjectKey()
    {
        return sourceObjectKey;
    }
    
    /**设置要复制的源对象名称
     * 
     * @param sourceObjectKey 源对象名称
     */
    public void setSourceObjectKey(String sourceObjectKey)
    {
        this.sourceObjectKey = sourceObjectKey;
    }
    
    /**获取要复制的目标桶的名称
     * 
     * @return  桶的名称
     */
    public String getDestinationBucketName()
    {
        return destinationBucketName;
    }
    
    /**设置要复制的目标桶
     * 
     * @param  destinationBucketName 桶的名称
     */
    public void setDestinationBucketName(String destinationBucketName)
    {
        this.destinationBucketName = destinationBucketName;
    }
    
    /**获取要复制的目标对象名称
     * 
     * @return  对象名称
     */
    public String getDestinationObjectKey()
    {
        return destinationObjectKey;
    }
    
    /**设置要复制的目标对象的名称
     * 
     * @param  destinationObjectKey 对象的名称
     */
    public void setDestinationObjectKey(String destinationObjectKey)
    {
        this.destinationObjectKey = destinationObjectKey;
    }
    
    /**获取要复制的对象的元数据
     * 
     * @return  ObjectMetadata 新对象元数据
     */
    public ObjectMetadata getNewObjectMetadata()
    {
        return newObjectMetadata;
    }
    
    /**设置要复制的对象的元数据
     * 
     * @param  newObjectMetadata 新对象元数据
     */
    public void setNewObjectMetadata(ObjectMetadata newObjectMetadata)
    {
        this.newObjectMetadata = newObjectMetadata;
    }

    /**是否创建新对象
     * 
     * @return true 创建新的元数据， false 不创建新的元数据 
     */
    public boolean isReplaceMetadata()
    {
        return replaceMetadata;
    }
    
    /**设置是否创建新对象
     * 
     * @param replaceMetadata 是否创建新对象标志
     */
    public void setReplaceMetadata(boolean replaceMetadata)
    {
        this.replaceMetadata = replaceMetadata;
    }
}
