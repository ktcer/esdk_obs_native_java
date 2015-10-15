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

/**描述拷贝多段操作的请求
 */
public class CopyPartRequest
{
    private String uploadId;
    
    private String sourceBucketName;
    
    private String sourceObjectKey;
    
    private String destinationBucketName;
    
    private String destinationObjectKey;
    
    private Long byteRangeStart;
    
    private Long byteRangeEnd;
    
    /**
     * 返回复制的起始位置
     * @return 复制的起始位置
     */
    public Long getByteRangeStart()
    {
        return byteRangeStart;
    }
    
    /**
     * 设置复制的起始位置
     * @param byteRangeStart 复制的起始位置
     */
    public void setByteRangeStart(Long byteRangeStart)
    {
        this.byteRangeStart = byteRangeStart;
    }
    
    /**
     * 返回复制对象的终止位置
     * @return 复制的终止位置
     */
    public Long getByteRangeEnd()
    {
        return byteRangeEnd;
    }
    
    /**
     * 设置复制对象的终止位置
     * @param byteRangeEnd 复制的终止位置
     */
    public void setByteRangeEnd(Long byteRangeEnd)
    {
        this.byteRangeEnd = byteRangeEnd;
    }

    private int partNumber;

    /**获取上传段的编号
     * 
     * @return 返回上传段的编号
     */
    public int getPartNumber()
    {
        return partNumber;
    }

    /**设置上传段的编号
     * 
     * @param partNumber 上传段的编号
     */
    public void setPartNumber(int partNumber)
    {
        this.partNumber = partNumber;
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
     * @param uploadId 多段上传任务标识
     */
    public void setUploadId(String uploadId)
    {
        this.uploadId = uploadId;
    }
    
    /**获取多段上传任务所属的桶
     * 
     * @return 返回桶的名称
     */
    public String getSourceBucketName()
    {
        return sourceBucketName;
    }
    
    /**设置多段上传任务所属的桶
     * 
     * @param bucketName 桶的名称
     */
    public void setSourceBucketName(String bucketName)
    {
        this.sourceBucketName = bucketName;
    }
    
    /**获取拷贝段操作的源对象名称
     * 
     * @return 拷贝段操作的源对象名称
     */
    public String getSourceObjectKey()
    {
        return sourceObjectKey;
    }
    
    /**设置拷贝段操作的源对象名称
     * 
     * @param objectKey 拷贝段操作的源对象名称
     */
    public void setSourceObjectKey(String objectKey)
    {
        this.sourceObjectKey = objectKey;
    }

    /**获取拷贝段操作的目的对象所属的桶
     * 
     * @return 拷贝段操作的目的对象所属的桶的名称
     */
    public String getDestinationBucketName()
    {
        return destinationBucketName;
    }

    /**设置拷贝段操作的目标对象所属的桶
     * 
     * @param destBucketName 拷贝段操作的目的对象所属的桶的名称
     */
    public void setDestinationBucketName(String destBucketName)
    {
        this.destinationBucketName = destBucketName;
    }

    /**获取段拷贝操作的目的对象名称
     * 
     * @return 返回段拷贝操作的目的对象名称
     */
    public String getDestinationObjectKey()
    {
        return destinationObjectKey;
    }

    /**设置段拷贝操作的目的对象名称
     * 
     * @param destObjectKey 段拷贝操作的目的对象名称
     */
    public void setDestinationObjectKey(String destObjectKey)
    {
        this.destinationObjectKey = destObjectKey;
    }
    
}
