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


/**描述获取对象的请求
 */
public class GetObjectRequest
{
    private String bucketName;
    
    private String objectKey;
    
    private Long rangeStart;
    
    private Long rangeEnd;
    
    private String versionId;
    
    /**指定桶名和对象名的构造函数
     * 
     *@param bucketName 桶的名称
     *@param objectKey 对象名称
     */
    public GetObjectRequest(String bucketName, String objectKey)
    {
        this.bucketName = bucketName;
        this.objectKey = objectKey;
    }
    
    /**返回对象起始位置
     * 
     * @return 对象起始位置
     */
    public Long getRangeStart()
    {
        return rangeStart;
    }

    /**设置对象起始位置
     * 
     * @param rangeStart 对象起始位置
     */
    public void setRangeStart(Long rangeStart)
    {
        this.rangeStart = rangeStart;
    }

    /**返回对象结束位置
     * 
     * @return 对象结束位置
     */
    public Long getRangeEnd()
    {
        return rangeEnd;
    }

    /**设置对象结束
     * 
     * @param rangeEnd 对象内容结束位置
     */
    public void setRangeEnd(Long rangeEnd)
    {
        this.rangeEnd = rangeEnd;
    }
    
    /**返回对象所在的桶
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
    
    /**返回对象名称
     * 
     * @return 对象名称
     */
    public String getObjectKey()
    {
        return objectKey;
    }
    
    /**设置要获取的对象名称
     * 
     * @param objectKey 对象名称
     */
    public void setObjectKey(String objectKey)
    {
        this.objectKey = objectKey;
    }

    /**
     * 返回版本所属的对象
     * @return 版本所属的对象
     */
    public String getVersionId()
    {
        return versionId;
    }
    
    /**
     * 设置版本所属的对象
     * @param versionId 版本所属的对象
     */
    public void setVersionId(String versionId)
    {
        this.versionId = versionId;
    }
}
