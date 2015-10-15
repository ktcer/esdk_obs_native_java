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

/**描述存量信息
 */
public class BucketStorageInfo
{
    private long size;
    
    private long objectNum;

    /**获取存量信息的大小，单位为字节
     * 
     * @return 存量信息的大小
     */
    public long getSize()
    {
        return size;
    }
    
    /** 设置存量信息的大小 ，单位为字节
     * 
     * @param storageSize 存量信息的大小.
     */
    public void setSize(long storageSize)
    {
        this.size = storageSize;
    }

    /**获取存储的对象个数
     * 
     * @return 对象个数
     */
    public long getObjectNumber()
    {
        return objectNum;
    }

    /** 设置存储的对象个数
     * 
     * @param objectNumber 对象个数.
     */
    public void setObjectNumber(long objectNumber)
    {
        this.objectNum = objectNumber;
    }
    
}
