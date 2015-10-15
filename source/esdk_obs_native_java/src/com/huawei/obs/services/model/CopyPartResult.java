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

/**描述拷贝多段的返回结果
 */
public class CopyPartResult
{
    private int partNumber;
    
    private String etag;
    
    /**获取多段上传任务上传分段的编号
     * 
     * @return 返回多段上传任务上传分段的编号
     */
    public int getPartNumber()
    {
        return partNumber;
    }
    
    /**设置上传任务上传分段的编号
     * 
     * @param partNumber 多段上传任务上传分段的编号
     */
    public void setPartNumber(int partNumber)
    {
        this.partNumber = partNumber;
    }
    
    /** 获取对象的eTag
     * 
     * @return  对象的eTag
     */
    public String getEtag()
    {
        return etag;
    }
    
    /** 设置对象的eTag
     * 
     * @param objeTag  对象的eTag
     */
    public void setEtag(String objeTag)
    {
        this.etag = objeTag;
    }
}
